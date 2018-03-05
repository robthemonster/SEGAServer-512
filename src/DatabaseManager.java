import SEGAMessages.CreateUserRequest;
import SEGAMessages.UserLoginRequest;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.*;
import java.util.Base64;


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

    public static boolean authenticateUser(UserLoginRequest request) {
        try {
            Connection dbConnection = getDBConnection();
            ResultSet user = getUser(dbConnection, request.getUsername());
            if (user.next()) {
                byte[] salt = Base64.getDecoder().decode(user.getString("salt"));
                String hash = getSaltedPasswordHash(salt, request.getPassword());
                return hash.equals(user.getString("hash"));
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
        String insertQuery = "insert into users values(?, ?, ?);";
        PreparedStatement statement = dbConnection.prepareStatement(insertQuery);
        statement.setString(1, request.getUsername());
        statement.setString(2, Base64.getEncoder().encodeToString(salt));
        statement.setString(3, getSaltedPasswordHash(salt, request.getPassword()));
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

    private static Connection getDBConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        return DriverManager.getConnection(DB_URL, "sonic", "gottagofast");
    }
}
