package measure;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class MeasureEN {
    private static void productDataInput(String message, Scanner scanner, String errorMessage) {
        System.out.print(ConsoleColors.RESET + message);
        while (!scanner.hasNextDouble()) {
            scanner.next();
            System.out.print(ConsoleColors.RED + errorMessage + ConsoleColors.RESET);
        }
    }

    private static void toleranceCheck(Double neg, Double pos) {
        if (neg == 0 && pos == 0) {
            System.out.println("\n" + ConsoleColors.RED_BOLD + "Product has 0.0mm tolerance! " + ConsoleColors.YELLOW_BOLD + "Try again:" + ConsoleColors.RESET + "\n");
            measureProgram();
        } else if (pos < 0) {
            System.out.println("\n" + ConsoleColors.RED_BOLD + "Positive tolerance must be 0 or higher! " + ConsoleColors.YELLOW_BOLD + "Try again:" + ConsoleColors.RESET + "\n");
            measureProgram();
        } else if (neg > 0) {
            System.out.println("\n" + ConsoleColors.RED_BOLD + "Negative tolerance must be 0 or lower! " + ConsoleColors.YELLOW_BOLD + "Try again:" + ConsoleColors.RESET + "\n");
            measureProgram();
        }
    }

    private static void lengthCheck(double length) {
        if (length <= 0) {
            System.out.println(ConsoleColors.RED + "Wrong product length! " + ConsoleColors.YELLOW_BOLD + "(" + length + ")" + ConsoleColors.RESET + "\n");
            measureProgram();
        }
    }

    public static void measureProgram() {

        Scanner scanner = new Scanner(System.in);

        productDataInput("Enter product length (mm): ", scanner, "Wrong length, enter correct: ");
        double product = scanner.nextDouble();
        lengthCheck(product);
        System.out.println(ConsoleColors.GREEN + "PRODUCT LENGTH IS: " + product + "mm");

        productDataInput("Enter positive tolerance (e.g. 0,5): ", scanner, "Wrong tolerance, enter correct: ");
        double posTolerance = scanner.nextDouble();
        System.out.println(ConsoleColors.GREEN + "POSITIVE TOLERANCE IS: " + posTolerance + "mm");


        productDataInput("Enter negative tolerance (e.g -0,5): ", scanner, "Wrong tolerance, enter correct: ");
        double negTolerance = scanner.nextDouble();
        System.out.println(ConsoleColors.GREEN + "NEGATIVE TOLERANCE IS: " + negTolerance + "mm");

        toleranceCheck(negTolerance, posTolerance);

        System.out.println(ConsoleColors.RESET + "Enter the measurements, enter '0' after the last measurement to confirm: ");

        List<Double> list = new ArrayList<>();
        while (true) {
            productDataInput("", scanner, "Wrong measurement, enter correct: " + ConsoleColors.RESET);
            double input = scanner.nextDouble();
            if (input == 0) {
                break;
            }
            list.add(input);
        }


        double[] allMeasures = list.stream().mapToDouble(Double::doubleValue).toArray();

        System.out.println();

        double sum = 0;
        int measuresQ = allMeasures.length;
        double avg;
        int measuresBad = 0;
        int measuresGood;
        int measuresOutTolPlus = 0;
        int measuresOutTolMinus = 0;
        double biggestDifference;
        NumberFormat decimal = new DecimalFormat("##.##");

        for (double allMeasure : allMeasures) {
            sum = sum + allMeasure;
        }
        avg = sum / measuresQ;

        System.out.println(ConsoleColors.BLUE + "All measurements(mm): " + ConsoleColors.RESET + Arrays.toString(allMeasures) + "\n");
        System.out.println(ConsoleColors.BLUE + "Amount measured: " + ConsoleColors.RESET + allMeasures.length + " piece(s)");
        System.out.println(ConsoleColors.BLUE + "Average of all measurements: " + ConsoleColors.RESET + decimal.format(avg) + "mm");
        System.out.println();

        for (double allMeasure : allMeasures) {
            if (allMeasure > (product + posTolerance)) {
                measuresBad++;
                measuresOutTolPlus++;
            } else if (allMeasure < (product + negTolerance)) {
                measuresBad++;
                measuresOutTolMinus++;
            }
        }

        for (double allMeasure : allMeasures) {
            if (allMeasure > (product + posTolerance)) {
                measuresOutTolPlus++;
            } else if (allMeasure < (product + negTolerance)) {
                measuresOutTolMinus++;
            }
        }

        measuresGood = measuresQ - measuresBad;


        System.out.println(ConsoleColors.BLUE_UNDERLINED + "TOLERANCE DATA" + ConsoleColors.RESET);
        System.out.println(ConsoleColors.BLUE + "Outside tolerance: " + ConsoleColors.RESET + ConsoleColors.RED + measuresBad + " piece(s)" + ConsoleColors.RESET);
        System.out.println(ConsoleColors.BLUE + "Inside tolerance: " + ConsoleColors.RESET + ConsoleColors.GREEN + measuresGood + " piece(s)" + ConsoleColors.RESET);
        System.out.println(ConsoleColors.BLUE + "Bigger than " + (product + posTolerance) + "mm" + ": " + ConsoleColors.RESET + measuresOutTolPlus + " piece(s)");
        System.out.println(ConsoleColors.BLUE + "Smaller than " + (product + negTolerance) + "mm" + ": " + ConsoleColors.RESET + measuresOutTolMinus + " piece(s)");
        System.out.println();

        Arrays.sort(allMeasures);

        System.out.println(ConsoleColors.BLUE + "The biggest measurement: " + ConsoleColors.RESET + allMeasures[allMeasures.length - 1] + "mm");
        System.out.println(ConsoleColors.BLUE + "The smallest measurement: " + ConsoleColors.RESET + allMeasures[0] + "mm");

        biggestDifference = allMeasures[allMeasures.length - 1] - allMeasures[0];

        System.out.println(ConsoleColors.BLUE + "The difference between the smallest and the biggest measurement: " + ConsoleColors.RESET + ConsoleColors.CYAN_BOLD + decimal.format(biggestDifference) + "mm" + ConsoleColors.RESET);
        System.out.println();
        System.out.println(ConsoleColors.BLUE + "Measurements sorted from min to max(mm): " + ConsoleColors.RESET + Arrays.toString(allMeasures));

        System.out.println("\n"); //Space for restart option

        mainMenu();

    }

    static void mainMenu() {

        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print(ConsoleColors.BLUE + "Main Menu" + ConsoleColors.RESET + "\n");
            System.out.println("Enter one of the following commands: ");
            System.out.println(ConsoleColors.GREEN_BOLD + "start" + ConsoleColors.RESET + " - start the program");
            System.out.println(ConsoleColors.GREEN_BOLD + "exit" + ConsoleColors.RESET + " - exit the program" + "\n");
            String choice = scanner.nextLine();

            switch (choice) {
                case "start":
                    System.out.println();
                    measureProgram();
                    break;
                case "exit":
                    System.out.println();
                    System.out.println("\n" + ConsoleColors.BLUE_BOLD + "Thanks for using ToleranceMeasure." + ConsoleColors.RESET);
                    System.exit(0);
                    break;
                default:
                    System.out.println(ConsoleColors.RED + "Wrong command, try again." + ConsoleColors.RESET);
                    mainMenu();

            }
        }
    }

    public static void main(String[] args) {

        System.out.print(ConsoleColors.GREEN + "Welcome to ToleranceMeasure!" + ConsoleColors.RESET + "\n" + "\n");
        mainMenu();
    }
}