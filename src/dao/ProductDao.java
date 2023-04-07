package dao;

import measure.Color;
import measure.MeasureMain;

import java.math.BigDecimal;
import java.sql.*;
import java.util.Scanner;

public class ProductDao extends MeasureMain {
    private static final String DATABASE = "products_DB";

    public static void createProduct() {

        System.out.println(Color.BLUE + "Create new product" + Color.RESET);
        nameSetter();
        lengthSetter();
        tolerancesSetter();
        insertIntoDB(PRODUCT.getName(),
                PRODUCT.getLength(),
                PRODUCT.getPosTolerance(),
                PRODUCT.getNegTolerance());
        mainMenu();
    }

    private static void insertIntoDB (String name,
                                      BigDecimal length,
                                      BigDecimal posTolerance,
                                      BigDecimal negTolerance) {
        try (Connection conn = DbUtil.getConnection(DATABASE);
             PreparedStatement prepStmt = conn.prepareStatement(INSERT_QUERY)) {
            prepStmt.setString(1, name);
            prepStmt.setBigDecimal(2, length);
            prepStmt.setBigDecimal(3, posTolerance);
            prepStmt.setBigDecimal(4, negTolerance);
            prepStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(Color.GREEN_BOLD + "Product created succesfully." + Color.RESET);
        sleep(1500);
    }

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
                            createProduct();
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
                    sb
                            .append(Color.BLUE + "Product ID: " + Color.RESET)
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

    public static void readId() {
        readAll();
        System.out.println();
        int productID = idInput("Enter ID of the product to measure");

        if (productID == -1) {
            System.out.println();
            mainMenu();
        }

        try (Connection conn = DbUtil.getConnection(DATABASE);
        PreparedStatement prepStmt = conn.prepareStatement(READ_ID_QUERY)) {
            prepStmt.setInt(1, productID);
            ResultSet rs = prepStmt.executeQuery();
            if (!rs.next()) {
                System.out.println(Color.RED + "No product with given ID found" + Color.RESET);
                sleep(1000);
                mainMenu();
            } else {
                do {
                    PRODUCT.setName(rs.getString("name"));
                    PRODUCT.setLength(rs.getBigDecimal("length"));
                    PRODUCT.setPosTolerance(rs.getBigDecimal("posTolerance"));
                    PRODUCT.setNegTolerance(rs.getBigDecimal("negTolerance"));
                } while (rs.next());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(Color.GREEN_BOLD + "Product selected successfully" + Color.RESET);
        sleep(1000);
        measureProgram();
    }

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

    public static void deleteProduct() {

        readAll();
        System.out.println();
        int productID = idInput("Enter ID of the product to delete");

        if (productID == -1) {
            System.out.println();
            subMenu();
        }

        try (Connection conn = DbUtil.getConnection(DATABASE);
        PreparedStatement prepStmt = conn.prepareStatement(DELETE_QUERY)) {
            prepStmt.setInt(1, productID);
            prepStmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(Color.RED + "No product with given ID found" + Color.RESET);
            sleep(1000);
            mainMenu();
        }
        System.out.println(Color.GREEN_BOLD + "Product deleted successfully." + Color.RESET);
        System.out.println();
        subMenu();
    }

    private static  void sleep(int ms){
            try {
                Thread.sleep(ms);
            } catch (InterruptedException e) {
                System.out.println("-0");
            }
        }
}