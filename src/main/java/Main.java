import jodd.json.JsonParser;
import jodd.json.JsonSerializer;
import org.h2.tools.Server;
import spark.Spark;

import java.sql.*;
import java.util.ArrayList;


public class Main {

    public static void createTables(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS User (id INTEGER, userName VARCHAR, address VARCHAR, email VARCHAR)");
    }

    public static void insertUser(Connection conn, String userName, String address, String email) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO User VALUES (NULL, ?, ?, ?)");
        stmt.setString(1, userName);
        stmt.setString(2, address);
        stmt.setString(3, email);
        stmt.execute();
    }

    public static ArrayList<User> selectUsers(Connection conn) throws SQLException {
        ArrayList<User> users = new ArrayList<>();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users");
        ResultSet results = stmt.executeQuery();
        while (results.next()) {
            Integer id = results.getInt("id");
            String userName = results.getString("userName");
            String address = results.getString("address");
            String email = results.getString("email");
            users.add(new User(id, userName, address, email));
        }
        return users;
    }

    public static void updateUser(Connection conn, User user) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("UPDATE users SET username=?, address=?, email=?, WHERE id=?");
        stmt.setString(1, user.getUserName());
        stmt.setString(2, user.getAddress());
        stmt.setString(3, user.getEmail());
        stmt.setInt(4, user.getId());
    }

    public static void deleteUser(Connection conn, Integer id) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("DELETE * FROM user Where id = ?");
        stmt.setInt(1, id);
        stmt.execute();
    }
    
    public static void main(String[] args) throws SQLException {
        Server.createWebServer().start();
        Spark.staticFileLocation("/public");
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        createTables(conn);
        Spark.init();

        Spark.get(
                "/get-users",
                ((request, response) -> {
                    ArrayList<User> users = selectUsers(conn);
                    JsonSerializer s = new JsonSerializer();
                    return s.serialize(users);
                })
        );

        Spark.post(
                "add-user",
                ((request, response) -> {
                    String body = request.body();
                    JsonParser p = new JsonParser();
                    User user = p.parse(body, User.class);
                    insertUser(conn, user.getUserName(), user.getAddress(), user.getEmail());
                    return "";
                })
        );

        Spark.delete(
                "/user/:id",
                (req, rep) -> {
                    Integer id = Integer.valueOf(req.params(":id"));
                    deleteUser(conn, id);
                    return "";
                }
        );

        Spark.put(
                "/user",
                (req, res) -> {
                    String body = req.body();
                    JsonParser p = new JsonParser();
                    User user = p.parse(body, User.class);
                    updateUser(conn, user);
                    return "";
                }
        );
    }
}



