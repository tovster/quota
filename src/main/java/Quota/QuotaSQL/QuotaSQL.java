// This Class is a part of the Quota.QuotaSQL package
package Quota.QuotaSQL;

// Includes all required imports
import org.javalite.activejdbc.Base;

/**
 *
 * Created: 3/14/2017 - Mark Tov
 * Updated: 3/17/2017 - James Grau
 *
 */

// This is the main class for dealing with the DataBase connection and status
public class QuotaSQL {
    // Variables holding the DataBase connection information and required information
    private static final String driver = "com.mysql.jdbc.Driver";
    private static final String url = null;
    private static final String username = null;
    private static final String password = null;

    // Boolean values holding connection status (True|False)
    private static boolean active = false;

    /**
     *
     * Method to open a connection to the DataBase and set to connection status to true
     *
     */
    public static void connectToDB() {
        // If there is no active connection then open a connection to the DataBase
        if (!isActive()) {
            // Open a connection to the DataBase
            Base.open(driver, url, username, password);

            // Used when in development to allow for a visual appearance of an open connection to a database
            /// System.out.println("A connection has been opened.");

            // Set DataBase connection to active
            active = true;
        }
    }

    /**
     *
     * Method to close the connection to the DataBase and sets the connection status to false
     *
     */
    public static void disconnectFromDB() {
        // Close the connection to the DataBase
        Base.close();

        // Set DataBase connection to false
        active = false;
    }

    /**
     *
     * Method to return if a DataBase connection is active or not
     *
     * @return boolean value of active connection (True|False)
     *
     */
    public static boolean isActive() {
        // Return DataBase Connection active state
        return active;
    }
}
