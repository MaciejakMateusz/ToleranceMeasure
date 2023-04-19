package measure;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Product {

    private String name;
    private BigDecimal length;
    private BigDecimal posTolerance;
    private BigDecimal negTolerance;
    private final List<BigDecimal> productMeasurements = new ArrayList<>();

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setName() {
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
                this.name = nameInput;
                correctName = true;
            }
        }
    }

    public BigDecimal getLength() {
        return this.length;
    }

    public void setLength(BigDecimal productLength) {
        this.length = productLength;
    }

    public void setLength() {

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
                this.length = length;
                correctLength = true;
            }
        }
    }

    public BigDecimal getPosTolerance() {
        return this.posTolerance;
    }

    public void setPosTolerance(BigDecimal posTolerance) {
        this.posTolerance = posTolerance;
    }

    public void setPosTolerance() {
        Scanner scanner = new Scanner(System.in);

        boolean correctPosTolerance = false;
        while (!correctPosTolerance) {

            System.out.print("Enter positive tolerance: ");

            while (!scanner.hasNextBigDecimal()) {
                System.out.print(Color.RED + "Wrong input, try again: " + Color.RESET);
                scanner.next();
            }

            BigDecimal posTolerance = scanner.nextBigDecimal();

            if (posTolerance.compareTo(BigDecimal.ZERO) < 0) {
                System.out.println(Color.RED + "Positive tolerance cannot be below zero, try again." + Color.RESET);
            } else if (posTolerance.compareTo(getLength()) > 0) {
                System.out.println(Color.RED + "Positive tolerance cannot be bigger than product length." + Color.RESET);
            } else if (posTolerance.compareTo(getLength()) == 0) {
                System.out.println(Color.RED + "Positive tolerance cannot be equal product length." + Color.RESET);
            } else {
                this.posTolerance = posTolerance;
                correctPosTolerance = true;
            }
        }

    }

    public BigDecimal getNegTolerance() {
        return this.negTolerance;
    }

    public void setNegTolerance(BigDecimal negTolerance) {
        this.negTolerance = negTolerance;
    }

    public void setNegTolerance() {

        Scanner scanner = new Scanner(System.in);

        boolean correctNegTolerance = false;
        while (!correctNegTolerance) {

            System.out.print("Enter negative tolerance: ");

            while (!scanner.hasNextBigDecimal()) {
                System.out.print(Color.RED + "Wrong input, try again: " + Color.RESET);
                scanner.next();
            }

            BigDecimal negTolerance = scanner.nextBigDecimal();

            if (negTolerance.compareTo(BigDecimal.ZERO) > 0) {
                System.out.println(Color.RED + "Negative tolerance cannot be over zero, try again." + Color.RESET);
            } else if (negTolerance.compareTo(getLength().negate()) < 0) {
                System.out.println(Color.RED + "Negative tolerance cannot be smaller than negative product length." + Color.RESET);
            } else if (negTolerance.compareTo(getLength().negate()) == 0) {
                System.out.println(Color.RED + "Negative tolerance cannot be equal negative product length." + Color.RESET);
            } else {
                this.negTolerance = negTolerance;
                correctNegTolerance = true;
            }
        }


    }

    public void tolerancesSetter() {

        boolean correctTolerances = false;
        while (!correctTolerances) {

            setPosTolerance();
            setNegTolerance();

            if (getPosTolerance().compareTo(BigDecimal.ZERO) == 0 && getNegTolerance().compareTo(BigDecimal.ZERO) == 0) {
                System.out.println(Color.RED + "Positive and negative tolerance equals 0! " + Color.RESET + "Enter correct values.");
            } else {
                correctTolerances = true;
            }
        }
    }

    protected List<BigDecimal> getProductMeasurements() {
        return this.productMeasurements;
    }

    protected void setProductMeasurements() {

        String confirmation =
                "Do you want to confirm the measurements? "
                        + "Enter " + Color.GREEN + "(y/n)" + Color.RESET
                        + "\n" + "Current measurements: ";
        String undoConfirmation =
                Color.BLUE + "Successfully deleted the last measurement. Current measurements are: " + "\n";

        boolean measurementsConfirmed = false;
        while (!measurementsConfirmed) {

            Scanner scanner = new Scanner(System.in);

            while (!scanner.hasNextBigDecimal()) {
                scanner.next();
                System.out.print(Color.RED + "Wrong measurement, try again: " + Color.RESET);
            }
            BigDecimal measurement = scanner.nextBigDecimal();
            scanner.nextLine(); // to consume newline left-over

            if (measurement.compareTo(BigDecimal.ZERO) == 0 && getProductMeasurements().size() != 0) {

                System.out.println(confirmation + Color.GREEN + getProductMeasurements() + Color.RESET);

                boolean correctCommand = false;
                while (!correctCommand) {
                    String check = scanner.nextLine();
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
            } else if (getProductMeasurements().size() == 0 && measurement.compareTo(BigDecimal.ZERO) == 0) {
                System.out.println(Color.RED + "You should enter at least one measurement." + Color.BLUE + "\n" + "Please continue: " + Color.RESET);
                continue;
            }

            if (measurement.compareTo(BigDecimal.ZERO) < 0 && !measurement.equals(BigDecimal.valueOf(-1))) {
                System.out.println(Color.RED + "Measurement cannot be less than 0! " + Color.RESET + "Try again: ");
            } else if (measurement.equals(BigDecimal.valueOf(-1)) && getProductMeasurements().size() != 0) {
                getProductMeasurements().remove(getProductMeasurements().size() - 1);
                System.out.println(undoConfirmation + Color.GREEN + getProductMeasurements() + Color.RESET + "\n" + Color.BLUE + "Continue: " + Color.RESET);
            } else if (measurement.equals(BigDecimal.valueOf(-1)) && getProductMeasurements().size() == 0) {
                System.out.println(Color.RED + "There is nothing to delete." + Color.RESET + " Try again: ");
            } else if (measurement.compareTo(BigDecimal.ZERO) != 0) {
                this.productMeasurements.add(measurement);
            }
        }
        System.out.println();
    }

    public void clearList() {
        productMeasurements.clear();
    }

    @Override
    public String toString() {
        System.out.println();
        return Color.BLUE + "PRODUCT NAME = " + Color.GREEN_BOLD + name + "\n" +
                Color.BLUE + "PRODUCT LENGTH = " + Color.GREEN_BOLD + length + "mm" + "\n" +
                Color.BLUE + "POSITIVE TOLERANCE = " + Color.GREEN_BOLD + posTolerance + "mm" + "\n" +
                Color.BLUE + "NEGATIVE TOLERANCE = " + Color.GREEN_BOLD + negTolerance + "mm" + Color.RESET;
    }
}