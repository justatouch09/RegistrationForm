import jodd.json.JsonParser;
import jodd.json.JsonSerializer;
import org.h2.tools.Server;
import spark.Spark;

import java.sql.*;
import java.util.ArrayList;


public class Main {

    public static void createTables(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS Users (id INTEGER, name VARCHAR, address VARCHAR, email VARCHAR)");
    }

    public static void insertUser(Connection conn, String name, String email, String address) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO users VALUES (NULL, ?, ?, ?)");
        stmt.setString(1, name);
        stmt.setString(2, email);
        stmt.setString(3, address);
        stmt.execute();
    }

    public static ArrayList<User> getUsers(Connection conn) throws SQLException {
        ArrayList<User> users = new ArrayList<>();
        PreparedStatement stmt = conn.prepareStatement("Select * FROM users");
        ResultSet results = stmt.executeQuery();

        while (results.next()) {
            String name = results.getString("name");
            String address = results.getString("address");
            String email = results.getString("email");

            User u = new User(address, email, name);
            users.add(u);
        }
        return users;
    }

    public static void updateUser(Connection conn, User user) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("UPDATE users WHERE username=?, address=?, email=?, WHERE id=?");
        stmt.setString(1, user.getName());
        stmt.setString(2, user.getAddress());
        stmt.setString(3, user.getEmail());
        stmt.setInt(4, user.getId());
    }

    public static void deleteUser(Connection conn, Integer id) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("DELETE * FROM users Where id = ?");
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
                "/user",
                ((request, response) -> {
                    ArrayList<User> users = getUsers(conn);
                    JsonSerializer s = new JsonSerializer();
                    return s.serialize(users);
                })
        );

        Spark.post(
                "/user",
                ((request, response) -> {
                    String body = request.body();
                    JsonParser p = new JsonParser();
                    User user = p.parse(body, User.class);
                    insertUser(conn, user.getName(), user.getEmail(), user.getAddress());
                    return "";
                })
        );

        Spark.delete(
                "/user/:id",
                (req, res) -> {
                    Integer id = Integer.valueOf(req.params("id"));
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



