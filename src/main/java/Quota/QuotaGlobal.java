// Quota Java file belongs to the Quota Package
package Quota;

// Application dependant Imports
import Quota.QuotaSQL.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

/**
 *
 * A public class holding package global variables
 *
 * Created by James on 3/20/2017
 *
 * @author James Grau
 * @version 1.0
 *
 */
public class QuotaGlobal {

    // Creating global variables accessible project wide

    // Global variables follow 'static "DATA TYPE" "VARIABLE NAME" [ = value | optional]' -- Access global variables by QuotaGlobal."variable"
    public static User user;// = User.findFirst("username = ?", "test");

    /*
     *
     *   -------- Display variables for visual appeal --------
     *
     */

    // Logo Image constant variable -- Logo File
    static String logoFileBlack = "/src/images/logos/Quota-Logo_Black.png";
    static String logoFileWhite = "/src/images/logos/Quota-Logo_White.png";

    // Icon Image Constants Variable -- Icon File
    static String iconFileBlack = "/src/images/icons/Quota-Icon_Black.png";
    static String iconFileWhite = "/src/images/icons/Quota-Icon_White.png";

    /**
     *
     * Method to create and return the Quota Logo
     *
     * @return the logo image for the Quota Application
     *
     */
    static ImageView logoImage(String logoType) {
        // Logo Type
        // Black -- Black (inverted) version of Quota Logo
        // White/null -- White version of Quota Logo

        // Create a file the will hold the link for the correct called logo
        File logoImageFile;

        // Gets the current directory of execution
        File currentDirectory = new File("*");

        // If the passed logoType is black then process
        if(logoType.equals("Black")) {
            // Creates a new file and links it to the correct logo
            logoImageFile = new File(currentDirectory.getAbsolutePath().substring(0, currentDirectory.getAbsolutePath().length() - 2) + logoFileBlack);
        }else{
            // Creates a new file and links it to the correct logo
            logoImageFile = new File(currentDirectory.getAbsolutePath().substring(0, currentDirectory.getAbsolutePath().length() - 2) + logoFileWhite);
        }

        // Return the Quota Logo Image
        // Create the new image and pass it to and create a new image view then return
        return new ImageView(new Image(logoImageFile.toURI().toString()));
    }

    /**
     *
     * Method to create and return the Quota icon
     *
     * @return the icon image for the Quota Application
     *
     */
    static Image iconImage(String iconType) {
        // Icon Type
        // Black -- Black (inverted) version of Quota Logo
        // White/null -- White version of Quota Logo

        // Create a file the will hold the link for the correct called logo
        File iconImageFile;

        // Gets the current directory of execution
        File currentDirectory = new File("*");

        // If the passed logoType is black then process
        if(iconType.equals("Black")) {
            // Creates a new file and links it to the correct logo
            iconImageFile = new File(currentDirectory.getAbsolutePath().substring(0, currentDirectory.getAbsolutePath().length() - 2) + iconFileBlack);
        }else{
            // Creates a new file and links it to the correct logo
            iconImageFile = new File(currentDirectory.getAbsolutePath().substring(0, currentDirectory.getAbsolutePath().length() - 2) + iconFileWhite);
        }

        // Return the Quota Icon Image
        return new Image(iconImageFile.toURI().toString());
    }

    // Initialize a label to hold the error messages and success messages -- allow the class to use it privately
    static Label lblError = new Label();


    /**
     *
     * Constant handling for error messaging
     *
     */

    /**
     *
     *
     *
     * @param messageString passes a string the will be displayed as the error message
     * @param errorState passes a boolean state to refer to if there is an error or not
     *
     */
    static void messageArea(String messageString, boolean errorState) {
        // Set the padding for the label to 10 top-right-bottom-left
        lblError.setPadding(new Insets(10));

        // Set the text alignment in the label to center
        lblError.setAlignment(Pos.CENTER);

        // Enable test wrap in the label
        lblError.setWrapText(true);

        lblError.setText(messageString);

        // If there is an error then set the error label to display an error style
        if(!errorState) {
            // Add the styling for a successful message
            lblError.getStyleClass().add("message-success");

            // If the passed string is null/"" then clear the message and styling
            if(messageString.equals("")) {
                // Set the text of the label to null
                lblError.setText(null);

                // Remove the styling from the error message
                lblError.getStyleClass().remove("message-success");
                lblError.getStyleClass().remove("message-error");
            }
        }else{
            // If there is an error than set the styling for the error message label to display error
            lblError.getStyleClass().add("message-error");
        }
    }

    /**
     *
     * Changing the input style of the passed in text field based on the error states boolean value
     *
     * @param txtField the text field to perform the change on
     * @param errorState boolean value to check if there is a error or not
     *
     */
    static void inputErrorTXT(TextField txtField, boolean errorState) {
        // If there is an error then perform error actions
        if (!errorState) {
            // Add the error class to the field
            txtField.getStyleClass().add("message-success");
        } else {
            // If there is no error then perform success actions
            txtField.getStyleClass().add("message-error");
        }

    }

    /**
     *
     * Changing the input style of the passed in date field based on the error states boolean value
     *
     * @param dtpField the date field to perform the change on
     * @param errorState boolean value to check if there is a error or not
     *
     */
    static void inputErrorDTE(DatePicker dtpField, boolean errorState) {
        // If there is an error then perform error actions
        if (!errorState) {
            // Add the error class to the field
            dtpField.getStyleClass().add("message-success");
        } else {
            // If there is no error then perform success actions
            dtpField.getStyleClass().add("message-error");
        }

    }

    /**
     *
     * Changing the input style of the passed in password field based on the error states boolean value
     *
     * @param pswdField the password field to perform the change on
     * @param errorState boolean value to check if there is a error or not
     *
     */
    static void inputErrorPSW(PasswordField pswdField, boolean errorState) {
        // If there is an error then perform error actions
        if (!errorState) {
            // Add the error class to the field
            pswdField.getStyleClass().add("message-success");
        }else{
            // If there is no error then perform success actions
            pswdField.getStyleClass().add("message-error");
        }
    }


    /**
     *
     * Constant handling for the application style
     *
     */

    /**
     *
     * Method to set the styling of the scene
     *
     * @param scene pass in the scene to set the styling for
     * @param style pass in the string that refers to the
     *
     */
    static void setApplicationStyle(Scene scene, String style) {
        // Styling Sheets Constants Variable -- CSS File
        String defaultStyling = "/src/styling/QuotaStyle-Default.css";

        // Gets the current directory of execution
        File currentDirectory = new File("*");

        // Keep a temporary string that will hold the selected styling file link
        String selectedStyle = "";

        // Based on the styling requested set the temp. string for processing later
        if(style.equals("Default")) {
            // Set the styling for the requested style
            selectedStyle = defaultStyling;
        }else{
            // If no styling is selected or not a valid style then select default styling
            selectedStyle = defaultStyling;
        }

        // Try setting the styling for the scene
        try {
            // Add the styling to the passed scene
            scene.getStylesheets().add(new File(currentDirectory.getAbsolutePath().substring(0, currentDirectory.getAbsolutePath().length() - 2) + selectedStyle).toURI().toString());
        } catch (Exception e) {
            /// Catch any Exception
            e.printStackTrace();

            // Dump the error to the console
            System.out.println("Could not load style");
        }

    }

    /**
     *
     * This method is used to handle the display of errors
     *
     * @param header string representing the error header
     * @param message string representing the error message
     *
     */
    public static void displayError(String header, String message) {
        // Create a new error
        Alert alert = new Alert(Alert.AlertType.ERROR);

        // Set the error title, header and body messages
        alert.setTitle("Oh no!");
        alert.setHeaderText(header);
        alert.setContentText(message);

        // Show the error
        alert.showAndWait();
    }
}