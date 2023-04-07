package dao;

public class Queries extends DbUtil {
    protected static final String INSERT_QUERY =
            "INSERT INTO products " +
                    "(name, length, posTolerance, negTolerance) " +
                    "VALUE (?, ?, ?, ?)";
    protected static final String READ_ALL_QUERY =
            "SELECT * FROM products";
    protected static final String READ_ID_QUERY =
            "SELECT DISTINCT name, length, posTolerance, negTolerance FROM products WHERE id = ?";
    protected static final String DELETE_QUERY = "DELETE FROM products WHERE id = ?";
}