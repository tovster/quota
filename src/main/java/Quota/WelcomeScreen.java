// Quota Java file belongs to the Quota Package
package Quota;

/**
 * 
 * This class is used to display the main page/login page of the Quota Application
 * 
 * @author James Grau
 * @version v0.0.1
 * @date  March 14, 2017
 * 
 **/

// Application dependant Imports

import Quota.QuotaSQL.QuotaSQL;
import Quota.QuotaSQL.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.regex.Pattern;

/**
 * 
 * This class is used as the core of the Quota application which extends the imported Javafx Application package
 * 
 * @author James Grau
 * 
 */
class WelcomeScreen {

    // Initialize a label to hold the error messages and success messages -- allow the class to use it privately
    private static Label lblError = QuotaGlobal.lblError;

    /**
     *
     * A method to display the main "Welcome/Login" screen of the Quota Application
     *
     * @param primaryStage passed in from the Quota core class in-order to process and add to the screen
     *
     */
    static void displayLogin(Stage primaryStage) {
        // Create an input field allowing the user to enter their username
        TextField txtUsername = new TextField();

        // Sets the styling for the username input field
        txtUsername.getStyleClass().add("textbox-normal");

        // Set the username input field column count to 18
        txtUsername.setPrefColumnCount(18);

        // Set the username input field Prompt Text
        txtUsername.setPromptText("Username");

        // Creates a password input field allowing the user to enter a password without showing the raw data
        PasswordField txtPassword = new PasswordField();

        // Sets the styling for the password input field
        txtPassword.getStyleClass().add("textbox-normal");

        // Set the password input field column count to 18
        txtPassword.setPrefColumnCount(18);

        // Set the password input field Prompt Text
        txtPassword.setPromptText("Password");

        // Creates a horizontal box holding the form buttons in line
        HBox buttonPane = new HBox(18);

        // Sets the buttons row alignment to center
        buttonPane.setAlignment(Pos.CENTER);

        // Creates the login button
        Button btnLogin = new Button("Login");

        // Sets the login button Style
        btnLogin.getStyleClass().add("button-login");

        // Handles the action when the user presses the login button
        btnLogin.setOnAction(e -> {
            // Creates strings holding the username and the password from the input fields
            String username = txtUsername.getText(), password = txtPassword.getText();

            // If information is present in the input fields the continue on to processing the actual form data
            if(username != null && !username.equals("") && password != null && !password.equals("")) {
                // Open a connection to the DataBase
                // QuotaSQL.connectToDB();

                // Create a temp user for form validation -- query the DataBase and pulls the data if there us a user
                User userVerify = User.findFirst("username = ?", username);

                // If the user is in the DataBase then perform validation for the username and the password
                if (userVerify != null) {
                    // Change the style for the username field to green on success
                    QuotaGlobal.inputErrorTXT(txtUsername, false);

                    // If the password (hashed) does matches the DataBases hashed password then perform successful login
                    if (User.verifyPassword(password, userVerify.get("password").toString())) {
                        // Clear the error labels text and the style
                        QuotaGlobal.messageArea("", false);

                        // Change the style for the password field to green on success
                        QuotaGlobal.inputErrorPSW(txtPassword, false);

                        // Register the user to the global user variable/constant
                        QuotaGlobal.user = userVerify;

                        // Check which state the user is
                        if(QuotaGlobal.user.get("newUser").toString().equals("true")) {
                            // Set the primary stage to the wizard
                            Wizard.displayWelcome(primaryStage);
                        }else{
                            Dashboard.displayDashboard(primaryStage);
                        }

                        // Close the connection to the DataBase
                        // QuotaSQL.disconnectFromDB();

                    // Process for when the password is invalid
                    } else {
                        // Display the error message and set the error state to true
                        QuotaGlobal.messageArea("Oops... Your password is invalid. Please try again.", true);

                        // Set the style for the password field to red for visual error
                        QuotaGlobal.inputErrorPSW(txtPassword, true);
                    }
                } else {
                    // Display the error message and set the error state to true
                    QuotaGlobal.messageArea("Oops... That is not a valid user. Please try again.", true);

                    // Set the style for the username field to red for visual error
                    QuotaGlobal.inputErrorTXT(txtUsername, true);
                }

                /*  -- For development purposes only -- ? Displaying raw input data to the console
                    System.out.println("Username: " + username);
                    System.out.println("Password_Raw: " + password);
                    System.out.println("Password_hashed: " + User.encryptPassword(password));
                    System.out.println("Password Match: " + User.verifyPassword(password, userVerify.get("password").toString()));
                */

            // Process for when a user tries to submit and there has been no data entered
            }else{
                // Display the error message and set the error state to true
                QuotaGlobal.messageArea("Oops... In order to login, you need to enter a username and a password.", true);

                // Set the style for the username field to red for visual error
                QuotaGlobal.inputErrorTXT(txtUsername, true);

                // Set the style for the password field to red for visual error
                QuotaGlobal.inputErrorPSW(txtPassword, true);
            }
        });

        // Create the register button
        Button btnRegister = new Button("Register");

        // Set the register button style
        btnRegister.getStyleClass().add("button-login");

        // Perform the action to display the registration page on button click
        btnRegister.setOnAction(e -> {
            // Clear the error labels text and the style
            QuotaGlobal.messageArea("", false);

            // Call the displayRegistration method and pass in the primaryStage
            displayRegistration(primaryStage);
        });

        // Add the buttons to the button row
        buttonPane.getChildren().addAll(btnLogin, btnRegister);
        
        // Create a border pane and use it as the main pane for the login form
        BorderPane mainPane = new BorderPane();

        // Set the padding on the mainPane to 10 -- top-right-bottom-left
        mainPane.setPadding(new Insets(10));

        // Create a vbox to hold every object of the form in a vertical list like appearance
        VBox vboxWelcome = new VBox(10);

        // Set the alignment of the vbox to center
        vboxWelcome.setAlignment(Pos.CENTER);
        
        // Create input area using GridPane and add to vbox
        GridPane inputPane = new GridPane();

        // Set the alignment of the inputPane to center
        inputPane.setAlignment(Pos.CENTER);

        // Set the horizontal and vertical gaps to 10
        inputPane.setHgap(10);
        inputPane.setVgap(10);

        // Add the username, password and error label to the inputPane
        inputPane.addRow(0, txtUsername);
        inputPane.addRow(1, txtPassword);
        inputPane.addRow(2, lblError);

        // Add each login form object to the vbox
        vboxWelcome.getChildren().addAll(QuotaGlobal.logoImage(""), inputPane, buttonPane);

        // Set the alignment of the borderPane to center
        BorderPane.setAlignment(vboxWelcome, Pos.CENTER);

        // Set the mainPanes center content to the vbox of form elements
        mainPane.setCenter(vboxWelcome);

        // Create the login scene
        Scene loginScene = new Scene(mainPane);

        // Setting the scene styling for the login
        QuotaGlobal.setApplicationStyle(loginScene, "default");

        // Sets the main scene background color to a dark purple
        mainPane.getStyleClass().add("welcomescreen-scene");

        // Sets the primary stage scene to the loginScene
        primaryStage.setScene(loginScene);

        // Sets the application title
        primaryStage.setTitle("Quota Login");
    }

    /**
     *
     * A Class to display the registration screen for a user to register for an account
     *
     * @param primaryStage passing in the primary stage from the main method of the Quota application
     *
     */
    private static void displayRegistration(Stage primaryStage){
        // Clear the error labels text and the style
        QuotaGlobal.messageArea("", false);

        // Create a input field for the username
        TextField txtUsername = new TextField();

        // Sets the styling for the username input field
        txtUsername.getStyleClass().add("textbox-normal");

        // Set the preferred column count to 18 for the username input field
        txtUsername.setPrefColumnCount(18);

        // Set the prompt text of the username input field
        txtUsername.setPromptText("Username");

        // Create a input field for the password
        PasswordField txtPassword = new PasswordField();

        // Sets the styling for the password input field
        txtPassword.getStyleClass().add("textbox-normal");

        // Set the preferred column count to 18 for the password input field
        txtPassword.setPrefColumnCount(18);

        // Set the prompt text of the password input field
        txtPassword.setPromptText("Password");

        // Create a input field for the password-confirm
        PasswordField txtPassword2 = new PasswordField();

        // Sets the styling for the password input field
        txtPassword2.getStyleClass().add("textbox-normal");

        // Set the preferred column count to 18 for the password input field
        txtPassword2.setPrefColumnCount(18);

        // Set the prompt text of the password input field
        txtPassword2.setPromptText("Confirm Password");

        // Create a input field for the first name
        TextField txtFirstName = new TextField();

        // Sets the styling for the first name input field
        txtFirstName.getStyleClass().add("textbox-normal");

        // Set the preferred column count to 18 for the first name input field
        txtFirstName.setPrefColumnCount(18);

        // Set the prompt text of the first name input field
        txtFirstName.setPromptText("First Name");

        // Create a input field for the last name
        TextField txtLastName = new TextField();

        // Sets the styling for the last name input field
        txtLastName.getStyleClass().add("textbox-normal");

        // Set the preferred column count to 18 for the last name input field
        txtLastName.setPrefColumnCount(18);

        // Set the prompt text of the last name input field
        txtLastName.setPromptText("Last Name");

        // Create the button to register the user
        Button btnRegister = new Button("Register");

        // Styling for the registration button
        btnRegister.getStyleClass().add("button-login");

        //  Making the registration button "onclick" actions
        btnRegister.setOnAction(e -> {
            // Dummy console text -- Development Purpose
            ///System.out.println("Work in progress!");

            // Setting variables to hold form input values
            String firstName = txtFirstName.getText(), lastName = txtLastName.getText(), username = txtUsername.getText(), password1 = txtPassword.getText(), password2 = txtPassword2.getText();

            // Make a connection to the DataBase
            //QuotaSQL.connectToDB();

            // Create a user to later validate and add to the DataBase
            User registrationUser = new User();

            // Perform Form Validation
            // Check to make sure the user has entered all the information into the input fields
            if(!firstName.equals("") || !lastName.equals("") || !username.equals("") || !password1.equals("") || !password2.equals("")) {
                // Set the style for the first/last name fields to green for visual success
                QuotaGlobal.inputErrorTXT(txtFirstName, false);
                QuotaGlobal.inputErrorTXT(txtLastName, false);

                // Check to see if the password field length is greater than 6 characters
                if(password1.length() >= 6) {
                    // Check to see if the password field matched the required attributes. 1 lower & UPERCASE letter, 1 number and 1 special character
                    if(Pattern.compile("^(?=.*?[A-Z])(?=(.*[a-z]){1,})(?=(.*[\\d]){1,})(?=(.*[\\W]){1,})(?!.*\\s).{6,}$").matcher(password1).matches()) {
                        // Check to make sure that the passwords that have been entered match
                        if (User.verifyPassword(password1, User.encryptPassword(password2))) {
                            // Set the style for the password fields to green for visual success
                            QuotaGlobal.inputErrorPSW(txtPassword, false);
                            QuotaGlobal.inputErrorPSW(txtPassword2, false);

                            // Create a temp user for form validation -- query the DataBase and pulls the data if there is a user
                            User userVerify = User.findFirst("username = ?", username);

                            // If the user is in the DataBase then perform validation for the username and the password
                            if (userVerify == null) {
                                // Set the style for the username field to green for visual success
                                QuotaGlobal.inputErrorTXT(txtUsername, false);

                                // Pass the correct field values to the registration process in the users class
                                registrationUser.setFirstName(txtFirstName.getText());
                                registrationUser.setLastName(txtLastName.getText());
                                registrationUser.setUserName(txtUsername.getText());
                                registrationUser.setPassword(txtPassword.getText());

                                // Register the user
                                registrationUser.register();

                                // Create a success message that is displayed to the use when their account has been created
                                QuotaGlobal.messageArea("Success! You have now been registered and can now login.", false);

                                // Close the connection to the DataBase
                                //QuotaSQL.disconnectFromDB();

                                // Go back to the login screen
                                displayLogin(primaryStage);
                            } else {
                                // Display an error when the username is a duplicate
                                QuotaGlobal.messageArea("Oops... Looks like that username is already taken. Please try again.", true);

                                // Set the style for the username input field to red for visual error
                                QuotaGlobal.inputErrorTXT(txtUsername, true);
                            }
                        } else {
                            // Display an error when the password fields do not match
                            QuotaGlobal.messageArea("Oops... Looks like your passwords don't match. Please try again.", true);

                            // Set the style for the password fields to red for visual error
                            QuotaGlobal.inputErrorPSW(txtPassword, true);
                            QuotaGlobal.inputErrorPSW(txtPassword2, true);
                        }
                    }else{
                        // Display an error when the password does not match the cases required
                        QuotaGlobal.messageArea("Oops... Your password must be alphanumeric with one upper and lowercase letter, one number and one special character.", true);

                        // Set the style for the password fields to red for visual error
                        QuotaGlobal.inputErrorPSW(txtPassword, true);
                        QuotaGlobal.inputErrorPSW(txtPassword2, true);
                    }
                }else{
                    // Display an error when the password character count is less than 6
                    QuotaGlobal.messageArea("Oops... Your password must be greater then 6 characters long.", true);

                    // Set the style for the password fields to red for visual error
                    QuotaGlobal.inputErrorPSW(txtPassword, true);
                    QuotaGlobal.inputErrorPSW(txtPassword2, true);
                }
            }else{
                // Display an error when no data has been provided
                QuotaGlobal.messageArea("Oops... In order to register, you must fill out all of the fields above.", true);

                // Set the style for the input/password fields to red for visual error
                QuotaGlobal.inputErrorTXT(txtFirstName, true);
                QuotaGlobal.inputErrorTXT(txtLastName, true);
                QuotaGlobal.inputErrorTXT(txtUsername, true);

                // Password Fields
                QuotaGlobal.inputErrorPSW(txtPassword, true);
                QuotaGlobal.inputErrorPSW(txtPassword2, true);
            }
        });

        // Create a button that will login
        Button btnLogin = new Button("Login");

        // Set the class for the button
        btnLogin.getStyleClass().add("button-login");

        // Perform the action when the user selects the login button
        btnLogin.setOnAction(e -> {
            // Clear the error labels text and the style
            QuotaGlobal.messageArea("", false);

            // Display the login form and pass in the primaryStage
            displayLogin(primaryStage);
        });

        // Create a border pane to hold the main elements of the form
        BorderPane mainBorder = new BorderPane();

        // Setting the padding for the mainBorder -- 10 on top - right - bottom - left
        mainBorder.setPadding(new Insets(10));

        // Create a HBOX btnPane to hold al buttons with 18 spacing
        HBox btnPane = new HBox(18);

        // Create a VBox mainPane to hold all of the elements with spacing 10
        VBox mainPane = new VBox(10);

        // Set the padding for the mainPane - 10 on top- right - bottom - left
        mainPane.setPadding(new Insets(10));

        // Add the buttons to the button pane
        btnPane.getChildren().addAll(btnRegister, btnLogin);

        // Set the alignment of the btnPane to center
        btnPane.setAlignment(Pos.CENTER);

        // Create our input area using GridPane and add to Vbox
        GridPane inputPane = new GridPane();

        // Set the alignment of the input pane to center
        inputPane.setAlignment(Pos.CENTER);

        // Set the gap for horizontal and vertical to 10
        inputPane.setHgap(10);
        inputPane.setVgap(10);

        // Add the input fields to the grid
        inputPane.addRow(0, txtFirstName);
        inputPane.addRow(1, txtLastName);
        inputPane.addRow(2, txtUsername);
        inputPane.addRow(3, txtPassword);
        inputPane.addRow(4, txtPassword2);
        inputPane.addRow(5, lblError);

        // Add the children to the mainPane
        mainPane.getChildren().addAll(QuotaGlobal.logoImage(""), inputPane, btnPane);

        // Set the alignment of the mainPane to align center
        mainPane.setAlignment(Pos.CENTER);

        BorderPane.setAlignment(mainPane, Pos.CENTER);

        // Set the alignment of the main pane to center
        mainBorder.setCenter(mainPane);

        // set the styling class for the main Border
        mainBorder.getStyleClass().add("welcomescreen-scene");

        // Creates the registrationSceen for the registration
        Scene registrationScene = new Scene(mainBorder);

        // Setting the scene styling for the login
        QuotaGlobal.setApplicationStyle(registrationScene, "default");

        // Sets the primary stage scene to the registrationScene
        primaryStage.setScene(registrationScene);

        // Sets the application title
        primaryStage.setTitle("Quota Registration");
    }
}


