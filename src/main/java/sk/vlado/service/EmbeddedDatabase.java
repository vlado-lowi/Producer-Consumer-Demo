package sk.vlado.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sk.vlado.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EmbeddedDatabase {
    private static final Logger logger = LogManager.getLogger(EmbeddedDatabase.class);
    // DB_CLOSE_DELAY=-1 is added to prevent wipe of DB, when connection to DB is closed
    private static final String CONNECTION_STRING = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";
    private static final String CREATE_TABLE = """
    CREATE TABLE IF NOT EXISTS SUSERS (
        USER_ID     INT PRIMARY KEY,
        USER_GUID   VARCHAR(255),
        USER_NAME   VARCHAR(255)
    )""";
    private static final String INSERT_USER = "INSERT INTO SUSERS (USER_ID, USER_GUID, USER_NAME) VALUES (?, ?, ?)";
    private static final String SELECT_ALL_USERS = "SELECT * FROM SUSERS";
    private static final String DELETE_ALL_USERS = "DELETE FROM SUSERS";

    public EmbeddedDatabase() {
        try (Connection connection = DriverManager.getConnection(CONNECTION_STRING)) {
            try (Statement stmt = connection.createStatement()) {
                stmt.executeUpdate(CREATE_TABLE);
            }
        } catch (SQLException e) {
            logger.error(e);
        }
    }

    public boolean addUser(User user) {
        try (Connection connection = DriverManager.getConnection(CONNECTION_STRING)) {
            try (PreparedStatement stmt = connection.prepareStatement(INSERT_USER)) {
                stmt.setInt(1, user.id());
                stmt.setString(2, user.uuid());
                stmt.setString(3, user.name());

                stmt.executeUpdate();

                logger.debug("Inserted {} into database", user);
                return true;
            }
        } catch (SQLException e) {
            logger.error(e);
            return false;
        }
    }

    public List<User> getAllUsers() {
        try (Connection connection = DriverManager.getConnection(CONNECTION_STRING)) {
            try (Statement stmt = connection.createStatement()) {
                var resultSet = stmt.executeQuery(SELECT_ALL_USERS);

                List<User> users = new ArrayList<>();
                while (resultSet.next()) {
                    users.add(new User(
                            resultSet.getInt("USER_ID"),
                            resultSet.getString("USER_GUID"),
                            resultSet.getString("USER_NAME")));
                }

                logger.debug("Retrieved {} users from database", users.size());
                return users;
            }
        } catch (SQLException e) {
            logger.error(e);
            return Collections.emptyList();
        }
    }

    public int deleteAllUsers() {
        try (Connection connection = DriverManager.getConnection(CONNECTION_STRING)) {
            try (Statement stmt = connection.createStatement()) {
                int deleteCount = stmt.executeUpdate(DELETE_ALL_USERS);

                logger.debug("Deleted all {} users from database", deleteCount);
                return deleteCount;
            }
        } catch (SQLException e) {
            logger.error(e);
            return -1;
        }
    }

}
