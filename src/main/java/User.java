import java.sql.Connection;
import java.util.ArrayList;

/**
 * Created by jaradtouchberry on 4/28/17.
 */
public class User {
    private Integer Id;
    private String userName;
    private String address;
    private String email;
    public static ArrayList<User> users = new ArrayList<>();

    public User() {

    }

    public User(Integer id, String userName, String address, String email) {
        Id = id;
        this.userName = userName;
        this.address = address;
        this.email = email;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
}

