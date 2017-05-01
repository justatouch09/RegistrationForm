import java.sql.Connection;
import java.util.ArrayList;

/**
 * Created by jaradtouchberry on 4/28/17.
 */
public class User {
    public Integer Id;
    public String address;
    public String email;
    public String name;
    public static ArrayList<User> users = new ArrayList<>();

    public User() {

    }

    public User(String address, String email, String name) {
        this.address = address;
        this.email = email;
        this.name = name;


    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

