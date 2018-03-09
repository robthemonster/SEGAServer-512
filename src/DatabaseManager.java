import SEGAMessages.*;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;


public class DatabaseManager {
    private static final String DB_URL = "jdbc:mysql://sega-server-sql.crqgwxj3d6jd.us-east-2.rds.amazonaws.com:3306/sega_server_sql";

    public static boolean createUser(CreateUserRequest request) {
        try {
            Connection dbConnection = getDBConnection();
            if (!usernameTaken(dbConnection, request.getUsername())) {
                byte[] salt = getSalt();
                return addUserToDatabase(dbConnection, salt, request);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean createGroup(CreateGroupRequest request) {
        try {
            Connection dbConnection = getDBConnection();
            if (!groupNameTaken(dbConnection, request.getGroupName())) {
                return addGroupToDatabase(dbConnection, request);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<String> getGroupsForUser(GetGroupsForUserRequest request) {
        try {
            Connection dbConnection = getDBConnection();
            return getGroupsForUserFromDatabase(dbConnection, request.getUsername());
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static List<String> getUsersForGroup(GetUsersForGroupRequest request) {
        try {
            Connection dbConnection = getDBConnection();
            if (userIsInGroup(dbConnection, request.getUsername(), request.getGroupname())) {
                return getUsersForGroupFromDatabase(dbConnection, request.getGroupname());
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    private static List<String> getUsersForGroupFromDatabase(Connection dbConnection, String groupname) throws SQLException {
        String query = "select username from groups where groupname=?;";
        PreparedStatement statement = dbConnection.prepareStatement(query);
        statement.setString(1, groupname);
        ResultSet resultSet = statement.executeQuery();
        List<String> users = new ArrayList<>();
        while (resultSet.next()) {
            users.add(resultSet.getString("username"));
        }
        return users;
    }

    private static boolean userIsInGroup(Connection dbConnection, String username, String groupname) throws SQLException {
        String query = "select * from groups where username=? and groupname=?;";
        PreparedStatement statement = dbConnection.prepareStatement(query);
        statement.setString(1, username);
        statement.setString(2, groupname);
        return statement.executeQuery().next();
    }

    private static List<String> getGroupsForUserFromDatabase(Connection dbConnection, String username) throws SQLException {
        String query = "select groupname from groups where username=?;";
        PreparedStatement statement = dbConnection.prepareStatement(query);
        statement.setString(1, username);
        ResultSet resultSet = statement.executeQuery();
        List<String> groups = new ArrayList<>();
        while (resultSet.next()) {
            groups.add(resultSet.getString("groupname"));
        }
        return groups;
    }

    public static boolean authenticateUser(UserLoginRequest request) {
        try {
            Connection dbConnection = getDBConnection();
            ResultSet user = getUser(dbConnection, request.getUsername());
            if (user.next()) {
                byte[] salt = Base64.getDecoder().decode(user.getString("salt"));
                String hash = getSaltedPasswordHash(salt, request.getPassword());
                boolean authenticated = hash.equals(user.getString("hash"));
                if (authenticated) {
                    updateFirebaseTokenInDatabase(dbConnection, request.getUsername(), request.getFirebaseToken());
                }
                return authenticated;
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static ResultSet getUser(Connection dbConnection, String username) throws SQLException {
        String query = "select * from users where username = (?);";
        PreparedStatement statement = dbConnection.prepareStatement(query);
        statement.setString(1, username);
        return statement.executeQuery();
    }

    private static boolean addUserToDatabase(Connection dbConnection, byte[] salt, CreateUserRequest request) throws SQLException {
        String insertStatement = "insert into users values(?, ?, ?, ?);";
        PreparedStatement statement = dbConnection.prepareStatement(insertStatement);
        statement.setString(1, request.getUsername());
        statement.setString(2, Base64.getEncoder().encodeToString(salt));
        statement.setString(3, getSaltedPasswordHash(salt, request.getPassword()));
        statement.setString(4, request.getFirebaseToken());
        return statement.executeUpdate() == 1;
    }

    private static boolean addGroupToDatabase(Connection dbConnection, CreateGroupRequest request) throws SQLException {
        String insertStatement = "insert into groups values(?, ?);";
        PreparedStatement statement = dbConnection.prepareStatement(insertStatement);
        statement.setString(1, request.getGroupName());
        statement.setString(2, request.getCreator());
        return statement.executeUpdate() == 1;
    }

    private static boolean updateFirebaseTokenInDatabase(Connection dbConnection, String username, String firebaseToken) throws SQLException {
        String updateStatement = "update users set firebasetoken=? where username=?;";
        PreparedStatement statement = dbConnection.prepareStatement(updateStatement);
        statement.setString(1, firebaseToken);
        statement.setString(2, username);
        return statement.executeUpdate() == 1;
    }

    private static boolean updateSaltAndHashInDatabase(Connection dbConnection, String username, String newSalt, String newHash) throws SQLException {
        String updateStatement = "update users set salt=?, hash=? where username=?;";
        PreparedStatement statement = dbConnection.prepareStatement(updateStatement);
        statement.setString(1, newSalt);
        statement.setString(2, newHash);
        statement.setString(3, username);
        return statement.executeUpdate() == 1;
    }

    private static String getSaltedPasswordHash(byte[] salt, String password) {
        try {
            KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            return Base64.getEncoder().encodeToString(
                    keyFactory.generateSecret(keySpec).getEncoded()
            );
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return "FUCKING ERROR";
    }

    private static byte[] getSalt() {
        byte[] salt = new byte[32];
        new SecureRandom().nextBytes(salt);
        return salt;
    }

    private static boolean usernameTaken(Connection dbConnection, String username) throws SQLException {
        return getUser(dbConnection, username).next(); //returns true if a result is found with the same username
    }

    private static boolean groupNameTaken(Connection dbConnection, String groupName) throws SQLException {
        String query = "select * from groups where groupname=? limit 1;";
        PreparedStatement statement = dbConnection.prepareStatement(query);
        statement.setString(1, groupName);
        ResultSet resultSet = statement.executeQuery();
        return resultSet.next();
    }

    private static Connection getDBConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        return DriverManager.getConnection(DB_URL, "sonic", "gottagofast");
    }
}
