package measure;

import dao.ProductDao;

import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.math.BigDecimal;
import java.math.RoundingMode;

import static measure.FileManager.productDataWriter;

public class MeasureMain {

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

        System.out.println("Enter measurements."
                + "\n" + Color.GREEN + "0" + Color.RESET + " - to confirm measurements"
                + "\n" + Color.GREEN + "-1" + Color.RESET + " - to delete last measurement (undo)");

        product.setProductMeasurements();
        List<BigDecimal> measurementsList = product.getProductMeasurements();

        System.out.println();

        BigDecimal sum = BigDecimal.valueOf(0);
        BigDecimal measuresQuantity = BigDecimal.valueOf(measurementsList.size());
        for (int i = 0; i < measurementsList.size(); i++) {
            sum = measurementsList.get(i).add(sum);
        }
        BigDecimal avg = sum.divide(measuresQuantity, 2, RoundingMode.HALF_UP);

        System.out.println(Color.BLUE + "All measurements(mm): " + Color.RESET + measurementsList + "\n");
        System.out.println(Color.BLUE + "Amount measured: " + Color.RESET + measuresQuantity + " piece(s)");
        System.out.println(Color.BLUE + "Average of all measurements: " + Color.RESET + avg + "mm");
        System.out.println();

        BigDecimal measuresBad = BigDecimal.valueOf(0);
        BigDecimal measuresOutsidePositive = BigDecimal.valueOf(0);
        BigDecimal measuresOutsideNegative = BigDecimal.valueOf(0);

        for (int i = 0; i < measurementsList.size(); i++) {
            if (measurementsList.get(i).compareTo(productLength.add(posTolerance)) > 0) {
                measuresBad = measuresBad.add(BigDecimal.valueOf(1));
                measuresOutsidePositive = measuresOutsidePositive.add(BigDecimal.valueOf(1));
            } else if (measurementsList.get(i).compareTo(productLength.add(negTolerance)) < 0) {
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

        Collections.sort(measurementsList);

        System.out.println(Color.BLUE + "Biggest measurement: " + Color.RESET + measurementsList.get(measurementsList.size() - 1) + "mm");
        System.out.println(Color.BLUE + "Smallest measurement: " + Color.RESET + measurementsList.get(0) + "mm");

        BigDecimal biggestDifference = measurementsList
                .get(measurementsList.size() - 1)
                .subtract(measurementsList.get(0));

        System.out.println(Color.BLUE + "Difference between the smallest and the biggest measurement: " + Color.CYAN_BOLD + biggestDifference + "mm" + Color.RESET);
        System.out.println();
        System.out.println(Color.BLUE + "Measurements sorted ascending: " + Color.RESET + measurementsList);
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
                measurementsList);

        mainMenu();
    }

}