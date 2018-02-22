import SEGAMessages.CreateUserRequest;

import java.sql.*;


public class DatabaseManager {
    private static final String DB_URL = "jdbc:mysql://sega-server-sql.crqgwxj3d6jd.us-east-2.rds.amazonaws.com:3306/sega_server_sql";

    public static void printUsers() {
        try {
            Connection connection = getDBConnection();
            String query = "select * from users;";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                System.out.println(resultSet.getString(1) + resultSet.getString(2) + resultSet.getString(3));
            }
            System.out.println("bush did 9/11");
            connection.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static boolean createUser(CreateUserRequest request) {
        try {
            Connection connection = getDBConnection();
            String query = "select * from users where username = '" + request.getUsername() + "';";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                return false;
            }
            String insertQuery = "insert into users values('" + request.getUsername() + "', '7','" + request.getPassword() + "');";
            return statement.execute(insertQuery);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static Connection getDBConnection() throws ClassNotFoundException, SQLException {
        Connection connection = null;
        Class.forName("com.mysql.jdbc.Driver");
        return DriverManager.getConnection(DB_URL, "sonic", "gottagofast");
    }
}
