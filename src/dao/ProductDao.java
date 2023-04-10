package dao;

import measure.Color;
import measure.MeasureMain;
import measure.Product;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class ProductDao extends Queries {
    private static final String DATABASE = "products_DB";


    // --------CREATE--------

    public static void createProduct(Product product) {

        System.out.println(Color.BLUE + "Create new product" + Color.RESET);
        MeasureMain.nameSetter();
        MeasureMain.lengthSetter();
        MeasureMain.tolerancesSetter();
        insertIntoProducts(product);
        MeasureMain.mainMenu();
    }

    public static void insertIntoProducts(Product product) {
        try (Connection conn = DbUtil.getConnection(DATABASE);
             PreparedStatement prepStmt = conn.prepareStatement(INSERT_QUERY)) {
            prepStmt.setString(1, product.getName());
            prepStmt.setBigDecimal(2, product.getLength());
            prepStmt.setBigDecimal(3, product.getPosTolerance());
            prepStmt.setBigDecimal(4, product.getNegTolerance());
            prepStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(Color.GREEN_BOLD + "Product created successfully." + Color.RESET);
        sleep(1500);
    }

    // --------READ--------


    public static void readAll() {

        try (Connection conn = DbUtil.getConnection(DATABASE);
             PreparedStatement prepStmt = conn.prepareStatement(READ_ALL_QUERY)) {

            ResultSet rs = prepStmt.executeQuery();

            if (!rs.next()) {
                System.out.println(Color.RED + "No products created." + Color.RESET);
                System.out.println("Do you want to create new product? " + Color.GREEN + "y/n" + Color.RESET);

                Scanner scanner = new Scanner(System.in);

                boolean correctChoice = false;
                while (!correctChoice) {
                    String choice = scanner.nextLine();
                    switch (choice) {
                        case "y":
                            createProduct(MeasureMain.PRODUCT);
                            correctChoice = true;
                            break;
                        case "n":
                            MeasureMain.mainMenu();
                            correctChoice = true;
                            break;
                        default:
                            System.out.print("Wrong command, try again: ");
                    }
                }
            } else {
                do {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    BigDecimal length = rs.getBigDecimal("length");
                    BigDecimal posTolerance = rs.getBigDecimal("posTolerance");
                    BigDecimal negTolerance = rs.getBigDecimal("negTolerance");
                    StringBuilder sb = new StringBuilder();
                    sb.append(Color.BLUE + "Product ID: " + Color.RESET)
                            .append(id)
                            .append(Color.BLUE + " Name: " + Color.RESET)
                            .append(name)
                            .append(Color.BLUE + " Length: " + Color.RESET)
                            .append(length)
                            .append(Color.BLUE + " Positive tolerance: " + Color.RESET)
                            .append(posTolerance)
                            .append(Color.BLUE + " Negative tolerance: " + Color.RESET)
                            .append(negTolerance);
                    System.out.println(sb);
                } while (rs.next());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int readId(Product product, String message) {

        readAll();
        System.out.println();
        int productID = idInput(message);

        if (productID == -1) {
            System.out.println();
            MeasureMain.mainMenu();
        }

        try (Connection conn = DbUtil.getConnection(DATABASE);
             PreparedStatement prepStmt = conn.prepareStatement(READ_ID_QUERY)) {
            prepStmt.setInt(1, productID);
            ResultSet rs = prepStmt.executeQuery();
            if (!rs.next()) {
                System.out.println(Color.RED + "No product with given ID found" + Color.RESET);
                sleep(1000);
                MeasureMain.mainMenu();
                return 0;
            } else {
                do {
                    product.setName(rs.getString("name"));
                    product.setLength(rs.getBigDecimal("length"));
                    product.setPosTolerance(rs.getBigDecimal("posTolerance"));
                    product.setNegTolerance(rs.getBigDecimal("negTolerance"));
                    product.clearList();
                } while (rs.next());
                return productID;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }


    // --------UPDATE/EDIT--------

    public static void editProduct() {

        int productID = readId(MeasureMain.PRODUCT, "Enter ID of the product to edit");

        System.out.println();
        System.out.println(Color.BLUE + "What should be changed?" + Color.RESET);
        System.out.println("Enter one of the following commands:");
        System.out.println(Color.GREEN + "name" + Color.RESET);
        System.out.println(Color.GREEN + "length" + Color.RESET);
        System.out.println(Color.GREEN + "tolerances" + Color.RESET);
        System.out.println(Color.GREEN + "back" + Color.RESET + "\n");

        Scanner scanner = new Scanner(System.in);
        boolean correctChoice = false;

        while (!correctChoice) {
            String choice = scanner.nextLine();
            switch (choice) {
                case "name":
                    editName(productID);
                    correctChoice = true;
                    break;
                case "length":
                    editLength(productID);
                    correctChoice = true;
                    break;
                case "tolerances":
                    editTolerances(productID);
                    correctChoice = true;
                    break;
                case "back":
                    MeasureMain.subMenu();
                    correctChoice = true;
                    break;
                default:
                    System.out.print(Color.RED + "Wrong command, try again: " + Color.RESET);
                    break;
            }
        }

    }

    private static void editName(int productID) {

        String MAIN_DIRECTORY = "Measurements/";
        Path path = Paths.get(MAIN_DIRECTORY + MeasureMain.PRODUCT.getName());

        MeasureMain.nameSetter();

        try (Connection conn = DbUtil.getConnection(DATABASE);
             PreparedStatement prepStmt = conn.prepareStatement(UPDATE_NAME_QUERY)) {
            prepStmt.setString(1, MeasureMain.PRODUCT.getName());
            prepStmt.setInt(2, productID);
            prepStmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(Color.RED + "No product with given ID found" + Color.RESET);
            sleep(1000);
            MeasureMain.mainMenu();
        }
        System.out.println(Color.GREEN_BOLD + "Product data changed successfully." + Color.RESET);
        System.out.println();

        try {
            if (!Files.notExists(path)) {
                Files.move(path, path.resolveSibling(MeasureMain.PRODUCT.getName()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        MeasureMain.subMenu();
    }

    private static void editLength(int productID) {

        MeasureMain.lengthSetter();

        try (Connection conn = DbUtil.getConnection(DATABASE);
             PreparedStatement prepStmt = conn.prepareStatement(UPDATE_LENGTH_QUERY)) {
            prepStmt.setBigDecimal(1, MeasureMain.PRODUCT.getLength());
            prepStmt.setInt(2, productID);
            prepStmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(Color.RED + "No product with given ID found" + Color.RESET);
            sleep(1000);
            MeasureMain.mainMenu();
        }
        System.out.println(Color.GREEN_BOLD + "Product data changed successfully." + Color.RESET);
        System.out.println();
        MeasureMain.subMenu();
    }

    private static void editTolerances(int productID) {

        MeasureMain.tolerancesSetter();

        try (Connection conn = DbUtil.getConnection(DATABASE);
             PreparedStatement prepStmt = conn.prepareStatement(UPDATE_TOLERANCES_QUERY)) {
            prepStmt.setBigDecimal(1, MeasureMain.PRODUCT.getPosTolerance());
            prepStmt.setBigDecimal(2, MeasureMain.PRODUCT.getNegTolerance());
            prepStmt.setInt(3, productID);
            prepStmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(Color.RED + "No product with given ID found" + Color.RESET);
            sleep(1000);
            MeasureMain.mainMenu();
        }
        System.out.println(Color.GREEN_BOLD + "Product data changed successfully." + Color.RESET);
        System.out.println();
        MeasureMain.subMenu();
    }


    // --------DELETE--------

    public static void deleteProduct() {

        readAll();
        System.out.println();
        int productID = idInput("Enter ID of the product to delete");

        if (productID == -1) {
            System.out.println();
            MeasureMain.subMenu();
        }

        try (Connection conn = DbUtil.getConnection(DATABASE);
             PreparedStatement prepStmt = conn.prepareStatement(DELETE_QUERY)) {
            prepStmt.setInt(1, productID);
            prepStmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(Color.RED + "No product with given ID found" + Color.RESET);
            sleep(1000);
            MeasureMain.mainMenu();
        }
        System.out.println(Color.GREEN_BOLD + "Product deleted successfully." + Color.RESET);
        System.out.println();
        MeasureMain.subMenu();
    }

    // --------HELPERS--------

    private static int idInput(String message) {

        Scanner scanner = new Scanner(System.in);

        StringBuilder sb = new StringBuilder();
        sb.append(message)
                .append("(")
                .append(Color.GREEN)
                .append("-1 ")
                .append(Color.RESET)
                .append("to go back): ");

        System.out.print(sb);

        while (!scanner.hasNextInt()) {
            System.out.print("Wrong ID, try again: ");
            scanner.next();
        }
        return scanner.nextInt();
    }

    private static void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            System.out.println("-0");
        }
    }
}