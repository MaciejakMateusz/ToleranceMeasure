package measure;

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MeasureMain {

    protected static Product product = new Product();
    protected static Path path = Paths.get("productData.txt");
    protected static final String confirmation = "Do you want to confirm the measurements? " + "Enter " + Color.GREEN + "(y/n)" + Color.RESET + "\n" + "Current measurements: ";
    protected static final String undoConfirmation = Color.BLUE + "Successfully deleted the last measurement. Current measurements are: " + "\n";

    public static void main(String[] args) {
        System.out.print(Color.GREEN + "Welcome!" + Color.RESET + "\n" + "\n");
        mainMenu();
    }

    protected static void mainMenu () {

        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println();
            System.out.print(Color.BLUE +"Main Menu" + Color.RESET + "\n");
            System.out.println("Enter one of the following commands: ");
            System.out.println(Color.GREEN_BOLD + "start" + Color.RESET + " - to start the program");
            System.out.println(Color.GREEN_BOLD + "last" + Color.RESET + " - to show data from last measurement session");
            System.out.println(Color.GREEN_BOLD + "exit" + Color.RESET + " - to exit the program"  + "\n");


               String choice = scanner.nextLine();
               while (!choice.equals("start") && !choice.equals("last") && !choice.equals("exit") ) {
                   System.out.println(Color.RED + "Wrong command, try again." + Color.RESET);
                   choice = scanner.nextLine();
               }
                switch (choice) {

                    case "start":
                        System.out.println();
                        setName();
                        measureProgram();
                        break;
                    case "last":
                        System.out.println();
                        productDataReader();
                        break;
                    case "exit":
                        System.out.println(Color.BLUE + "Program ended, thanks for using ToleranceMeasure." + Color.RESET);
                        System.exit(0);
                }

        }
    }

    protected static void measureProgram () {

        setLength();
        BigDecimal productLength = product.getLength();

        setTolerances();
        BigDecimal posTolerance = product.getPosTolerance();
        BigDecimal negTolerance = product.getNegTolerance();

        System.out.println(product + "\n");

        System.out.println("Enter products measurements." + "\n" + Color.GREEN + "0" + Color.RESET + " - to confirm your measurements" + "\n" + Color.GREEN + "-1" + Color.RESET + " - to delete last measurement (undo)");

        List<BigDecimal> allMeasures = productMeasures();

        System.out.println();

        BigDecimal sum = BigDecimal.valueOf(0);
        BigDecimal measuresQuantity = BigDecimal.valueOf(allMeasures.size());
        for (int i = 0; i < allMeasures.size(); i++) {
            sum = allMeasures.get(i).add(sum);
        }
        BigDecimal avg = sum.divide(measuresQuantity, 2, RoundingMode.HALF_UP);

        System.out.println(Color.BLUE + "All measurements(mm): " + Color.RESET + allMeasures + "\n");
        System.out.println(Color.BLUE + "Amount measured: " + Color.RESET + measuresQuantity + " piece(s)");
        System.out.println(Color.BLUE + "Average of all measurements: " + Color.RESET + avg + "mm");
        System.out.println();

        BigDecimal measuresBad = BigDecimal.valueOf(0);
        BigDecimal measuresOutsidePositive = BigDecimal.valueOf(0);
        BigDecimal measuresOutsideNegative = BigDecimal.valueOf(0);

        for (int i = 0; i < allMeasures.size(); i++) {
            if (allMeasures.get(i).compareTo(productLength.add(posTolerance)) > 0) {
                measuresBad = measuresBad.add(BigDecimal.valueOf(1));
                measuresOutsidePositive = measuresOutsidePositive.add(BigDecimal.valueOf(1));
            } else if (allMeasures.get(i).compareTo(productLength.add(negTolerance)) < 0) {
                measuresBad = measuresBad.add(BigDecimal.valueOf(1));
                measuresOutsideNegative = measuresOutsideNegative.add(BigDecimal.valueOf(1));
            }
        }

        BigDecimal measuresGood = measuresQuantity.subtract(measuresBad);

        System.out.println("TOLERANCE DATA");
        System.out.println(Color.BLUE + "Outside tolerance: " + Color.RESET + Color.RED + measuresBad + " piece(s)" + Color.RESET);
        System.out.println(Color.BLUE + "Inside tolerance: " + Color.RESET + Color.GREEN + measuresGood + " piece(s)" + Color.RESET);
        System.out.println(Color.BLUE + "Bigger than " + (productLength.add(posTolerance)) + "mm" + ": " + Color.RESET + measuresOutsidePositive + " piece(s)");
        System.out.println(Color.BLUE + "Smaller than " + (productLength.add(negTolerance)) + "mm" + ": " + Color.RESET + measuresOutsideNegative + " piece(s)");
        System.out.println();

        Collections.sort(allMeasures);

        System.out.println(Color.BLUE + "Biggest measurement: " + Color.RESET + allMeasures.get(allMeasures.size() - 1) + "mm");
        System.out.println(Color.BLUE + "Smallest measurement: " + Color.RESET + allMeasures.get(0) + "mm");

        BigDecimal biggestDifference = allMeasures.get(allMeasures.size() - 1).subtract(allMeasures.get(0));

        System.out.println(Color.BLUE + "Difference between the smallest and the biggest measurement: " + Color.RESET + Color.CYAN_BOLD + biggestDifference + "mm" + Color.RESET);
        System.out.println();
        System.out.println(Color.BLUE + "Measurements sorted ascending: " + Color.RESET + allMeasures);

        String productName = product.getName();
        productDataWriter(productName,
                productLength,
                posTolerance,
                negTolerance,
                measuresBad,
                measuresGood,
                avg,
                measuresOutsidePositive,
                measuresOutsideNegative,
                allMeasures,
                biggestDifference);

        System.out.println("\n");
        mainMenu();

    }

    protected static void setLength() {

        Scanner scanner = new Scanner(System.in);
        boolean correctLength = false;
        while (!correctLength) {
            System.out.print("Enter product length: ");
            while (!scanner.hasNextBigDecimal()) {
                System.out.print(Color.RED + "Wrong input, try again: " + Color.RESET);
                scanner.next();
            }
            BigDecimal tmpLength = scanner.nextBigDecimal();
            if (tmpLength.compareTo(BigDecimal.ZERO) < 0) {
                System.out.println(Color.RED + "Product length cannot be negative" + Color.RESET);
                continue;
            } else if (tmpLength.compareTo(BigDecimal.ZERO) == 0) {
                System.out.println(Color.RED + "Product length cannot be equal 0" + Color.RESET);
                continue;
            }
            product.setLength(tmpLength);
            correctLength = true;
        }

    }

    protected static void setTolerances() {

        boolean correctTolerances = false;
        while (!correctTolerances) {
            setPosTolerance();
            setNegTolerance();
            if (product.getPosTolerance().compareTo(BigDecimal.ZERO) == 0 && product.getNegTolerance().compareTo(BigDecimal.ZERO) == 0) {
                System.out.println(Color.RED + "Positive and negative tolerance equals 0! " + Color.RESET + "Enter the right values.");
                continue;
            }
            correctTolerances = true;
        }

    }

    protected static void setPosTolerance() {

        Scanner scanner = new Scanner(System.in);
        boolean correctPosTolerance = false;
        while (!correctPosTolerance) {
            System.out.print("Enter positive tolerance: ");
            while (!scanner.hasNextBigDecimal()) {
                System.out.print(Color.RED + "Wrong input, try again: " + Color.RESET);
                scanner.next();
            }
            BigDecimal tmpPosTol = scanner.nextBigDecimal();
            if (tmpPosTol.compareTo(BigDecimal.ZERO) < 0) {
                System.out.println(Color.RED + "Positive tolerance cannot be below zero, try again." + Color.RESET);
                continue;
            } else if (tmpPosTol.compareTo(product.getLength()) > 0) {
                System.out.println(Color.RED + "Positive tolerance cannot be bigger than product length." + Color.RESET);
                continue;
            } else if (tmpPosTol.compareTo(product.getLength()) == 0) {
                System.out.println(Color.RED + "Positive tolerance cannot be equal product length." + Color.RESET);
                continue;
            }
            product.setPosTolerance(tmpPosTol);
            correctPosTolerance = true;
        }

    }

    protected static void setNegTolerance() {

        Scanner scanner = new Scanner(System.in);
        boolean correctNegTolerance = false;
        while (!correctNegTolerance) {
            System.out.print("Enter negative tolerance: ");
            while (!scanner.hasNextBigDecimal()) {
                System.out.print(Color.RED + "Wrong input, try again: " + Color.RESET);
                scanner.next();
            }
            BigDecimal tmpNegTol = scanner.nextBigDecimal();
            if (tmpNegTol.compareTo(BigDecimal.ZERO) > 0) {
                System.out.println(Color.RED + "Negative tolerance cannot be over zero, try again." + Color.RESET);
                continue;
            } else if (tmpNegTol.compareTo(product.getLength().negate()) < 0) {
                System.out.println(Color.RED + "Negative tolerance cannot be smaller than negative product length." + Color.RESET);
                continue;
            } else if (tmpNegTol.compareTo(product.getLength().negate()) == 0) {
                System.out.println(Color.RED + "Negative tolerance cannot be equal negative product length." + Color.RESET);
                continue;
            }
            product.setNegTolerance(tmpNegTol);
            correctNegTolerance = true;
        }

    }

    protected static void setName() {

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter product name: ");
        product.setName(scanner.nextLine());

    }

    protected static List<BigDecimal> productMeasures() {

        Scanner scanner = new Scanner(System.in);
        List<BigDecimal> list = new ArrayList<>();
        boolean measurementsConfirmed = false;

        while (!measurementsConfirmed) {
            while (!scanner.hasNextBigDecimal()) {
                scanner.next();
                System.out.print(Color.RED + "Wrong measurement, try again: " + Color.RESET);
            }
            BigDecimal input = scanner.nextBigDecimal();
            if (input.compareTo(BigDecimal.ZERO) == 0 && list.size() != 0) {
                System.out.println(confirmation + Color.GREEN + list + Color.RESET);
                boolean correctCommand = false;
                while (!correctCommand) {
                    Scanner scanner1 = new Scanner(System.in);
                    String check = scanner1.nextLine();
                    if (check.equals("y")) {
                        measurementsConfirmed = true;
                        correctCommand = true;
                    } else if (check.equals("n")) {
                        System.out.println(Color.BLUE + "You can continue with measuring: " + Color.RESET);
                        correctCommand = true;
                    } else {
                        System.out.print(Color.RED + "Wrong command! " + Color.RESET + "Try again: ");
                    }
                }
            } else if (list.size() == 0 && input.compareTo(BigDecimal.ZERO) == 0) {
                System.out.println(Color.RED + "You should enter at least one measurement." + Color.BLUE + "\n" + "Please continue: " + Color.RESET);
                continue;
            }

            if (input.compareTo(BigDecimal.ZERO) < 0 && !input.equals(BigDecimal.valueOf(-1))) {
                System.out.println(Color.RED + "Measurement cannot be less than 0! " + Color.RESET + "Try again: ");
            } else if (input.equals(BigDecimal.valueOf(-1)) && list.size() != 0) {
                list.remove(list.size() - 1);
                System.out.println(undoConfirmation + Color.GREEN + list + Color.RESET + "\n" + Color.BLUE + "Continue: " + Color.RESET);
            } else if (input.equals(BigDecimal.valueOf(-1)) && list.size() == 0) {
                System.out.println(Color.RED + "There is nothing to delete." + Color.RESET + " Try again: ");
            } else if (input.compareTo(BigDecimal.ZERO) != 0) {
                list.add(input);
            }
        }
        return list;
    }

    protected static void productDataWriter(String productName,
                                            BigDecimal productLength,
                                            BigDecimal posTolerance,
                                            BigDecimal negTolerance,
                                            BigDecimal measuresBad,
                                            BigDecimal measuresGood,
                                            BigDecimal avg,
                                            BigDecimal measuresOutsidePositive,
                                            BigDecimal measuresOutsideNegative,
                                            List<BigDecimal> allMeasures,
                                            BigDecimal biggestDifference) {

        List<String> outList = new ArrayList<>();
        outList.add("PRODUCT NAME = " +  productName);
        outList.add("PRODUCT LENGTH = " +  productLength + "mm");
        outList.add("POSITIVE TOLERANCE = " +  posTolerance + "mm");
        outList.add("NEGATIVE TOLERANCE = " +  negTolerance + "mm");
        outList.add("");
        outList.add("All measurements(mm): " + allMeasures.toString());
        outList.add("Amount measured: " + allMeasures.size() + " piece(s)");
        outList.add("Average of all measurements: " + avg + "mm");
        outList.add("");
        outList.add("TOLERANCE DATA");
        outList.add("Outside tolerance: " + measuresBad + " piece(s)");
        outList.add("Inside tolerance: " + measuresGood + " piece(s)");
        outList.add("Bigger than " + (productLength.add(posTolerance)) + "mm: " + measuresOutsidePositive + " piece(s)");
        outList.add("Smaller than " + (productLength.add(negTolerance)) + "mm: " + measuresOutsideNegative + " piece(s)");
        outList.add("");
        outList.add("Biggest measurement: " + allMeasures.get(allMeasures.size() - 1) + "mm");
        outList.add("Smallest measurement: " + allMeasures.get(0) + "mm");
        outList.add("Difference between the smallest and the biggest measurement: " + biggestDifference + "mm");
        outList.add("");
        outList.add("Measurements sorted ascending: " + allMeasures);
        try {
            Files.write(path, outList);
        } catch (IOException ex) {
            System.out.println(Color.RED + "Can't save changes..." + Color.RESET);
        }
    }

    protected static void productDataReader() {

        try {
            for (String line : Files.readAllLines(path)) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println(Color.RED + "Couldn't read the file" + Color.RESET);
        }
        mainMenu();
    }

}