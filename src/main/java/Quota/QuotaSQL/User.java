package Quota.QuotaSQL;

import org.javalite.activejdbc.Model;
import org.mindrot.jbcrypt.BCrypt;

/**
 * Created by Mark on 3/14/2017.
 */

public class User extends Model {

    private String userName; //Username
    private String password; //Password
    private String firstName; //First Name
    private String lastName; //Last Name

    public User(String userName, String password, String firstName, String lastName) {
        this.userName = userName;
        this.password = encryptPassword(password);  //Encrypt the password instantly
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public User(){}

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = encryptPassword(password);
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public static String encryptPassword(String password){
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static Boolean verifyPassword(String password, String passwordDB){
        return BCrypt.checkpw(password, passwordDB);
    }

    public void register(){
        this.set("username", this.userName);
        this.set("password", this.password);
        this.set("firstName", this.firstName);
        this.set("lastName", this.lastName);
        this.set("newUser", true);
        this.saveIt();
    }

}
