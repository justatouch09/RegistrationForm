import sun.plugin2.message.Message;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by jaradtouchberry on 4/27/17.
 */
public class Main {

    public static void createTables(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS User (id INTEGER, userName VARCHAR, address VARCHAR, email VARCHAR)");
    }
    //prepare statement so no one tampers with database, so no one can mess with database IMPORTANT
    //replace question marks witg 1 2
    public static void insertUsers(Connection conn, Integer id, String userName, String address, String email) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO User VALUES (NULL, ?, ?, ?, ?)");
        stmt.setInt(1, id);
        stmt.setString(2, userName);
        stmt.setString(3, address);
        stmt.setString(4, email);
        stmt.execute();//execute sql db// changing data not returning//
    }

    //read messages from database over connection
    public static ArrayList<User> selectUsers(Connection conn) throws SQLException {
        //define variable to get read to return
        ArrayList<User> users = new ArrayList<>();
        //goal to fill
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users");
        ResultSet results = stmt.executeQuery();//execute query get while results back
        while (results.next()) {
            Integer id = results.getInt("id");//id collum store as int
            String userName = results.getString("userName");// id store string
            String address = results.getString("address");//value of text collum return
            String email = results.getString("email");//value of text collum return
            users.add(new User(id, userName, address, email));//craft values these into an object and store to message arraylist
        }
        return users;//return arraylist
    }

//    public static updateUser
//
//    public static deleteUser
//
//    public static void main(String[] args) throws SQLException { //main method cant define methods in method
//        //tels h2 to give us interface to work with
//        Server.createWebServer().start();
//
//        //put static in resources directory
//        Spark.staticFileLocation("/public");
//
//        //no routes need sparkinit
//        Spark.init();
//        //need a connection to a database//look at connection string. looks at drover to connect to db "(h2) driver"
//        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
//        //using method defined to create tables
//        createTables(conn);
//        //after adding public folder spark now know to use static//in the root of my resources directory
//    public static void main(String[] args) {
//        // your code here.
    }
}
