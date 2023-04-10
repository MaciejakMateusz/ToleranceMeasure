package measure;

import dao.ProductDao;

import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.math.BigDecimal;
import java.math.RoundingMode;

import static measure.FileManager.productDataWriter;

public class MeasureMain {

    public static Product PRODUCT = new Product();
    protected static final String confirmation =
            "Do you want to confirm the measurements? "
                    + "Enter " + Color.GREEN + "(y/n)" + Color.RESET
                    + "\n" + "Current measurements: ";
    protected static final String undoConfirmation =
            Color.BLUE + "Successfully deleted the last measurement. Current measurements are: " + "\n";

    public static void main(String[] args) {
        mainMenu();
    }

    public static void mainMenu() {

        System.out.println();
        System.out.println(Color.BLUE + "Main Menu" + Color.RESET);
        System.out.println("Enter one of the following commands: ");
        System.out.println(Color.GREEN_BOLD + "start" + Color.RESET + " - start the program and manually enter dimensions");
        System.out.println(Color.GREEN_BOLD + "select" + Color.RESET + " - select created product preset");
        System.out.println(Color.GREEN_BOLD + "manager" + Color.RESET + " - manage your products");
        System.out.println(Color.GREEN_BOLD + "exit" + Color.RESET + " - exit the program" + "\n");

        Scanner scanner = new Scanner(System.in);

        boolean correctChoice = false;
        while (!correctChoice) {
            String choice = scanner.nextLine();
            switch (choice) {
                case "start":
                    System.out.println();
                    PRODUCT.clearList();
                    nameSetter();
                    lengthSetter();
                    tolerancesSetter();
                    ProductDao.insertIntoProducts(PRODUCT);
                    measureProgram();
                    correctChoice = true;
                    break;
                case "select":
                    System.out.println();
                    ProductDao.readId(PRODUCT, "Enter ID of the product to measure");
                    measureProgram();
                    correctChoice = true;
                    break;
                case "manager":
                    subMenu();
                    break;
                case "exit":
                    System.out.println(Color.BLUE + "Program ended, thanks for using ToleranceMeasure." + Color.RESET);
                    System.exit(0);
                default:
                    System.out.println(Color.RED + "Wrong command, try again." + Color.RESET);
                    break;
            }
        }
    }

    public static void subMenu() {

        System.out.println(Color.BLUE_BOLD + "Product manager" + Color.RESET);
        System.out.println(Color.GREEN_BOLD + "create" + Color.RESET + " - create new product");
        System.out.println(Color.GREEN_BOLD + "edit" + Color.RESET + " - edit existing product");
        System.out.println(Color.GREEN_BOLD + "delete" + Color.RESET + " - delete existing product");
        System.out.println(Color.GREEN_BOLD + "back" + Color.RESET + " - main menu" + "\n");

        Scanner scanner = new Scanner(System.in);

        boolean correctChoice = false;
        while (!correctChoice) {
            String choice = scanner.nextLine();
            switch (choice) {
                case "create":
                    System.out.println();
                    ProductDao.createProduct(PRODUCT);
                    correctChoice = true;
                    break;
                case "edit":
                    System.out.println();
                    ProductDao.editProduct();
                    correctChoice = true;
                    break;
                case "delete":
                    System.out.println();
                    ProductDao.deleteProduct();
                    correctChoice = true;
                    break;
                case "back":
                    mainMenu();
                    correctChoice = true;
                    break;
                default:
                    System.out.println(Color.RED + "Wrong command, try again." + Color.RESET);
                    break;
            }
        }
    }

    public static void measureProgram() {

        String productName = PRODUCT.getName();
        BigDecimal productLength = PRODUCT.getLength();
        BigDecimal posTolerance = PRODUCT.getPosTolerance();
        BigDecimal negTolerance = PRODUCT.getNegTolerance();

        System.out.println(PRODUCT + "\n");

        System.out.println("Enter measurements."
                + "\n" + Color.GREEN + "0" + Color.RESET + " - to confirm measurements"
                + "\n" + Color.GREEN + "-1" + Color.RESET + " - to delete last measurement (undo)");

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
        System.out.println(Color.BLUE + "Outside tolerance: " + Color.RED + measuresBad + " piece(s)" + Color.RESET);
        System.out.println(Color.BLUE + "Inside tolerance: " + Color.GREEN + measuresGood + " piece(s)" + Color.RESET);
        System.out.println(Color.BLUE + "Bigger than " + (productLength.add(posTolerance)) + "mm" + ": " + Color.RESET + measuresOutsidePositive + " piece(s)");
        System.out.println(Color.BLUE + "Smaller than " + (productLength.add(negTolerance)) + "mm" + ": " + Color.RESET + measuresOutsideNegative + " piece(s)");
        System.out.println();

        Collections.sort(allMeasures);

        System.out.println(Color.BLUE + "Biggest measurement: " + Color.RESET + allMeasures.get(allMeasures.size() - 1) + "mm");
        System.out.println(Color.BLUE + "Smallest measurement: " + Color.RESET + allMeasures.get(0) + "mm");

        BigDecimal biggestDifference = allMeasures
                .get(allMeasures.size() - 1)
                .subtract(allMeasures.get(0));

        System.out.println(Color.BLUE + "Difference between the smallest and the biggest measurement: " + Color.CYAN_BOLD + biggestDifference + "mm" + Color.RESET);
        System.out.println();
        System.out.println(Color.BLUE + "Measurements sorted ascending: " + Color.RESET + allMeasures);
        System.out.println();

        productDataWriter(productName,
                productLength,
                posTolerance,
                negTolerance,
                measuresBad,
                measuresGood,
                avg,
                measuresOutsidePositive,
                measuresOutsideNegative,
                allMeasures);

        mainMenu();
    }

    public static void nameSetter() {

        boolean correctName = false;
        while (!correctName) {

            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter product name: ");
            String nameInput = scanner.nextLine();

            if (nameInput.contains("/")) {
                System.out.print(Color.RED + "Illegal character in product name: '/'" + Color.RESET + "\n");
            } else if (nameInput.contains(".")) {
                System.out.print(Color.RED + "Illegal character in product name: '.'" + Color.RESET + "\n");
            } else {
                PRODUCT.setName(nameInput);
                correctName = true;
            }
        }
    }

    public static void lengthSetter() {

        Scanner scanner = new Scanner(System.in);

        boolean correctLength = false;
        while (!correctLength) {

            System.out.print("Enter product length: ");

            while (!scanner.hasNextBigDecimal()) {
                System.out.print(Color.RED + "Wrong input, try again: " + Color.RESET);
                scanner.next();
            }

            BigDecimal length = scanner.nextBigDecimal();

            if (length.compareTo(BigDecimal.ZERO) < 0) {
                System.out.println(Color.RED + "Product length cannot be negative." + Color.RESET);
            } else if (length.compareTo(BigDecimal.ZERO) == 0) {
                System.out.println(Color.RED + "Product length cannot be 0." + Color.RESET);
            } else {
                PRODUCT.setLength(length);
                correctLength = true;
            }
        }
    }

    public static void tolerancesSetter() {

        boolean correctTolerances = false;
        while (!correctTolerances) {

            posToleranceSetter_Validator();
            NegToleranceSetter_Validator();

            if (PRODUCT.getPosTolerance().compareTo(BigDecimal.ZERO) == 0 && PRODUCT.getNegTolerance().compareTo(BigDecimal.ZERO) == 0) {
                System.out.println(Color.RED + "Positive and negative tolerance equals 0! " + Color.RESET + "Enter correct values.");
            } else {
                correctTolerances = true;
            }
        }
    }

    private static void posToleranceSetter_Validator() {

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
            } else if (tmpPosTol.compareTo(PRODUCT.getLength()) > 0) {
                System.out.println(Color.RED + "Positive tolerance cannot be bigger than product length." + Color.RESET);
            } else if (tmpPosTol.compareTo(PRODUCT.getLength()) == 0) {
                System.out.println(Color.RED + "Positive tolerance cannot be equal product length." + Color.RESET);
            } else {
                PRODUCT.setPosTolerance(tmpPosTol);
                correctPosTolerance = true;
            }
        }
    }

    private static void NegToleranceSetter_Validator() {

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
            } else if (tmpNegTol.compareTo(PRODUCT.getLength().negate()) < 0) {
                System.out.println(Color.RED + "Negative tolerance cannot be smaller than negative product length." + Color.RESET);
            } else if (tmpNegTol.compareTo(PRODUCT.getLength().negate()) == 0) {
                System.out.println(Color.RED + "Negative tolerance cannot be equal negative product length." + Color.RESET);
            } else {
                PRODUCT.setNegTolerance(tmpNegTol);
                correctNegTolerance = true;
            }
        }
    }

    protected static List<BigDecimal> productMeasures() {

        boolean measurementsConfirmed = false;
        while (!measurementsConfirmed) {

            Scanner scanner = new Scanner(System.in);

            while (!scanner.hasNextBigDecimal()) {
                scanner.next();
                System.out.print(Color.RED + "Wrong measurement, try again: " + Color.RESET);
            }
            BigDecimal input = scanner.nextBigDecimal();


            if (input.compareTo(BigDecimal.ZERO) == 0 && PRODUCT.getList().size() != 0) {

                System.out.println(confirmation + Color.GREEN + PRODUCT.getList() + Color.RESET);

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
            } else if (PRODUCT.getList().size() == 0 && input.compareTo(BigDecimal.ZERO) == 0) {
                System.out.println(Color.RED + "You should enter at least one measurement." + Color.BLUE + "\n" + "Please continue: " + Color.RESET);
                continue;
            }

            if (input.compareTo(BigDecimal.ZERO) < 0 && !input.equals(BigDecimal.valueOf(-1))) {
                System.out.println(Color.RED + "Measurement cannot be less than 0! " + Color.RESET + "Try again: ");
            } else if (input.equals(BigDecimal.valueOf(-1)) && PRODUCT.getList().size() != 0) {
                PRODUCT.getList().remove(PRODUCT.getList().size() - 1);
                System.out.println(undoConfirmation + Color.GREEN + PRODUCT.getList() + Color.RESET + "\n" + Color.BLUE + "Continue: " + Color.RESET);
            } else if (input.equals(BigDecimal.valueOf(-1)) && PRODUCT.getList().size() == 0) {
                System.out.println(Color.RED + "There is nothing to delete." + Color.RESET + " Try again: ");
            } else if (input.compareTo(BigDecimal.ZERO) != 0) {
                PRODUCT.addToList(input);
            }
        }

        return PRODUCT.getList();
    }

}