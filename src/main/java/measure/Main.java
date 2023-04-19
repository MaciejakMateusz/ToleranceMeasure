package measure;

import dao.ProductDao;

import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.math.BigDecimal;
import java.math.RoundingMode;

import static measure.FileManager.productDataWriter;

public class Main {

    public static Product product = new Product();

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
                    product.clearList();
                    product.setName();
                    product.setLength();
                    product.tolerancesSetter();
                    ProductDao.insertIntoProducts(product);
                    mainProgram();
                    correctChoice = true;
                    break;
                case "select":
                    System.out.println();
                    ProductDao.readId(product, "Enter ID of the product to measure");
                    mainProgram();
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
                    ProductDao.createProduct(product);
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

    public static void mainProgram() {

        String productName = product.getName();
        BigDecimal productLength = product.getLength();
        BigDecimal posTolerance = product.getPosTolerance();
        BigDecimal negTolerance = product.getNegTolerance();

        System.out.println(product + "\n");

        Messages.enterMeasurements();

        product.setProductMeasurements();
        List<BigDecimal> measurementsList = product.getProductMeasurements();

        BigDecimal measurementsSum = BigDecimal.valueOf(0);
        BigDecimal measurementsQuantity = BigDecimal.valueOf(measurementsList.size());
        for (int i = 0; i < measurementsList.size(); i++) {
            measurementsSum = measurementsList.get(i).add(measurementsSum);
        }
        BigDecimal avg = measurementsSum.divide(measurementsQuantity, 2, RoundingMode.HALF_UP);

        Messages.measurementsAvg_Quantity(measurementsList, measurementsQuantity, avg);

        BigDecimal measurementsBad = BigDecimal.valueOf(0);
        BigDecimal measurementsOutsidePositive = BigDecimal.valueOf(0);
        BigDecimal measurementsOutsideNegative = BigDecimal.valueOf(0);

        for (int i = 0; i < measurementsList.size(); i++) {
            if (measurementsList.get(i).compareTo(productLength.add(posTolerance)) > 0) {
                measurementsBad = measurementsBad.add(BigDecimal.valueOf(1));
                measurementsOutsidePositive = measurementsOutsidePositive.add(BigDecimal.valueOf(1));
            } else if (measurementsList.get(i).compareTo(productLength.add(negTolerance)) < 0) {
                measurementsBad = measurementsBad.add(BigDecimal.valueOf(1));
                measurementsOutsideNegative = measurementsOutsideNegative.add(BigDecimal.valueOf(1));
            }
        }

        BigDecimal measurementsInTolerance = measurementsQuantity.subtract(measurementsBad);

        Messages.tolerancesData(productLength, posTolerance, negTolerance, measurementsBad, measurementsOutsidePositive, measurementsOutsideNegative, measurementsInTolerance);

        Collections.sort(measurementsList);

        Messages.biggestSmallest_Measurement(measurementsList);

        BigDecimal biggestDifference = measurementsList
                .get(measurementsList.size() - 1)
                .subtract(measurementsList.get(0));

        Messages.measurementsDifferences_SortedList(measurementsList, biggestDifference);

        productDataWriter(productName,
                productLength,
                posTolerance,
                negTolerance,
                measurementsBad,
                measurementsInTolerance,
                avg,
                measurementsOutsidePositive,
                measurementsOutsideNegative,
                measurementsList);

        mainMenu();
    }

}