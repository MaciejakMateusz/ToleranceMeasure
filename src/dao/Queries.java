package dao;

public class Queries extends DbUtil {
    protected static final String INSERT_QUERY =
            "INSERT INTO products " +
                    "(name, length, posTolerance, negTolerance) " +
                    "VALUE (?, ?, ?, ?)";

    protected static final String READ_ALL_QUERY =
            "SELECT * FROM products";
    protected static final String READ_ID_QUERY =
            "SELECT DISTINCT id, name, length, posTolerance, negTolerance FROM products WHERE id = ?";

    protected static final String UPDATE_NAME_QUERY = "UPDATE products SET name = ? WHERE id = ?";
    protected static final String UPDATE_LENGTH_QUERY = "UPDATE products SET length = ? WHERE id = ?";
    protected static final String UPDATE_TOLERANCES_QUERY = "UPDATE products SET posTolerance = ?, negTolerance = ? WHERE id = ?";
    protected static final String DELETE_QUERY =
            "DELETE FROM products WHERE id = ?";

}