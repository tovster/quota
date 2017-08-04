// Quota Java file belongs to the Quota Package
package Quota;

// Class Dependant imports

import Quota.QuotaSQL.Account;
import Quota.QuotaSQL.Category;
import Quota.QuotaSQL.Transaction;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

import static Quota.Dashboard.displayDashboard;
import static Quota.QuotaSQL.Account.getAccountsFromDB;
import static Quota.QuotaSQL.Transaction.uploadTransactionToDB;

/**
 * This class is used to display and handle the Wizard Screen
 *
 * @author James Grau
 * @version v0.0.1
 * @date March 30, 2017
 **/
public class Wizard {
    // Class global variables
    private static Label lblError = QuotaGlobal.lblError;
    // Determining the current stage for an account (Add/Edit/Remove)
    private static String currentState = "";
    // Create an observable list that is to hold the database results for the records table
    private static ObservableList<Category> budgetList = FXCollections.observableArrayList();

    /**
     * This method is used to display the left progress sidebar of the wizard so users can see how far into the wizard they are.
     *
     * @param progress is a variable passed to show how far the user is (0 = welcome, 1 = accounts and so on)
     * @return a BorderPane that can be used in layout classes later on
     */
    private static BorderPane displaySidebar(int progress) {
        // Create a left Borderpane to hold the left sidebar items
        BorderPane leftSidebar = new BorderPane();

        // Set the padding of the left sidebar
        leftSidebar.setPadding(new Insets(20));

        // Set the temp Color of the left sidebar
        leftSidebar.setStyle("-fx-background-color: #3d348b");

        // Left Selection items as labels
        Label lblWelcome = new Label("Welcome");
        Label lblAccounts = new Label("Add/Edit/Remove Accounts");
        Label lblTransactions = new Label("Import Transactions");
        Label lblBudgets = new Label("Setting up Budgets");
        Label lblFinish = new Label("Finish");

        // Temp label styling
        lblWelcome.setStyle("-fx-text-fill: #FFFFFF");
        lblAccounts.setStyle("-fx-text-fill: #FFFFFF");
        lblTransactions.setStyle("-fx-text-fill: #FFFFFF");
        lblBudgets.setStyle("-fx-text-fill: #FFFFFF");
        lblFinish.setStyle("-fx-text-fill: #FFFFFF");

        // Gets the current directory of execution
        File currentDirectory = new File("*");

        // Create a file the will hold the link for the correct called dot image
        File completed = new File(currentDirectory.getAbsolutePath().substring(0, currentDirectory.getAbsolutePath().length() - 2) + "/src/images/resources/complete_dot.png");
        File inProgress = new File(currentDirectory.getAbsolutePath().substring(0, currentDirectory.getAbsolutePath().length() - 2) + "/src/images/resources/inprogress_dot.png");
        File incomplete = new File(currentDirectory.getAbsolutePath().substring(0, currentDirectory.getAbsolutePath().length() - 2) + "/src/images/resources/incomplete_dot.png");

        // Create ImageViews for the dots
        Image completedIcon = new Image(completed.toURI().toString(), 10, 10, true, false);
        Image inProgressIcon = new Image(inProgress.toURI().toString(), 10, 10, true, false);
        Image incompleteIcon = new Image(incomplete.toURI().toString(), 10, 10, true, false);

        // Create selection area using GridPane and add to vbox
        GridPane selectionPane = new GridPane();

        // Set the alignment of the selectionPane to center
        selectionPane.setAlignment(Pos.CENTER_LEFT);

        // Set the horizontal and vertical gaps to 10
        selectionPane.setHgap(10);
        selectionPane.setVgap(10);

        // Add the welcome, import, enter, settings, finish labels to the inputPane starting the line with a dot depending on the progress of the user
        switch (progress) {
            // Welcome
            case 0:
                // Add the left sidebar items
                selectionPane.addRow(0, new ImageView(inProgressIcon), lblWelcome);
                selectionPane.addRow(1, new ImageView(incompleteIcon), lblAccounts);
                selectionPane.addRow(2, new ImageView(incompleteIcon), lblTransactions);
                selectionPane.addRow(3, new ImageView(incompleteIcon), lblBudgets);
                selectionPane.addRow(4, new ImageView(incompleteIcon), lblFinish);
                break;
            // Accounts
            case 1:
                // Add the left sidebar items
                selectionPane.addRow(0, new ImageView(completedIcon), lblWelcome);
                selectionPane.addRow(1, new ImageView(inProgressIcon), lblAccounts);
                selectionPane.addRow(2, new ImageView(incompleteIcon), lblTransactions);
                selectionPane.addRow(3, new ImageView(incompleteIcon), lblBudgets);
                selectionPane.addRow(4, new ImageView(incompleteIcon), lblFinish);
                break;
            // Transactions
            case 2:
                // Add the left sidebar items
                selectionPane.addRow(0, new ImageView(completedIcon), lblWelcome);
                selectionPane.addRow(1, new ImageView(completedIcon), lblAccounts);
                selectionPane.addRow(2, new ImageView(inProgressIcon), lblTransactions);
                selectionPane.addRow(3, new ImageView(incompleteIcon), lblBudgets);
                selectionPane.addRow(4, new ImageView(incompleteIcon), lblFinish);
                break;
            // Budget
            case 3:
                // Add the left sidebar items
                selectionPane.addRow(0, new ImageView(completedIcon), lblWelcome);
                selectionPane.addRow(1, new ImageView(completedIcon), lblAccounts);
                selectionPane.addRow(2, new ImageView(completedIcon), lblTransactions);
                selectionPane.addRow(3, new ImageView(inProgressIcon), lblBudgets);
                selectionPane.addRow(4, new ImageView(incompleteIcon), lblFinish);
                break;
            // Finish
            case 4:
                // Add the left sidebar items
                selectionPane.addRow(0, new ImageView(completedIcon), lblWelcome);
                selectionPane.addRow(1, new ImageView(completedIcon), lblAccounts);
                selectionPane.addRow(2, new ImageView(completedIcon), lblTransactions);
                selectionPane.addRow(3, new ImageView(completedIcon), lblBudgets);
                selectionPane.addRow(4, new ImageView(inProgressIcon), lblFinish);
                break;
        }

        // Vbox to hold the left selection items
        VBox selectionItems = new VBox(10);

        // Add the elements to the left sidebar and set the alignment
        selectionItems.getChildren().add(selectionPane);
        selectionItems.setAlignment(Pos.CENTER);

        // Add the content to the left sidebar
        leftSidebar.setCenter(selectionItems);
        leftSidebar.setBottom(QuotaGlobal.logoImage(""));

        // return the created sidebar
        return leftSidebar;
    }

    /**
     *
     * This method is used to display the back/next buttons for the wizard
     *
     * @param progress     symbolizes the point where the buttons point to
     * @param primaryStage is the stage where the buttons are displayed
     * @return the stage that the buttons are at
     *
     */
    private static HBox displayPrevNext(int progress, Stage primaryStage) {
        // Create the containing HBox that hold the buttons
        HBox prevNext = new HBox(10);
        prevNext.setPadding(new Insets(10));
        prevNext.setAlignment(Pos.BOTTOM_RIGHT);

        // Create the Back and Next Buttons
        Button btnPrev = new Button("Back");
        Button btnNext = new Button("Next");

        // Add the buttons to the hbox
        prevNext.getChildren().addAll(btnPrev, btnNext);

        // Based on the stage of the wizard, change where the buttons point to
        switch (progress) {
            // Welcome stage case
            case 0:
                // Hide the Back button on the first stage of the wizard
                btnPrev.setVisible(false);

                // Handle action for when the next button is pressed on the welcome stage
                btnNext.setOnAction(e -> {
                    // Display the next stage
                    displayAccounts(primaryStage);
                });

                // Break out of the switch case
                break;

            // Accounts stage case
            case 1:
                // Handle action for when the back button is pressed on the accounts stage
                btnPrev.setOnAction(e -> {
                    // Display the prevoius stage
                    displayWelcome(primaryStage);
                });

                // Handle action for when the next button is pressed on the accounts stage
                btnNext.setOnAction(e -> {
                    // Display the next stage
                    displayTransactions(primaryStage);
                });

                // Break out of the switch case
                break;

            // Transactions stage case
            case 2:
                // Handle action for when the back button is pressed on the Transactions stage
                btnPrev.setOnAction(e -> {
                    // Display the prevoius stage
                    displayAccounts(primaryStage);
                });

                // Handle action for when the next button is pressed on the Transactions stage
                btnNext.setOnAction(e -> {
                    // Display the next stage
                    displayBudget(primaryStage);
                });

                // Break out of the switch case
                break;

            // Budget stage case
            case 3:
                // Handle action for when the back button is pressed on the Budget stage
                btnPrev.setOnAction(e -> {
                    // Display the previous stage
                    displayTransactions(primaryStage);
                });

                // Handle action for when the next button is pressed on the Budget stage
                btnNext.setOnAction(e -> {
                    // Display the next stage
                    displayFinish(primaryStage);
                });

                // Break out of the switch case
                break;

            // Finish stage case
            case 4:
                // Handle action for when the back button is pressed on the Finish stage
                btnPrev.setOnAction(e -> {
                    // Display the previous stage
                    displayBudget(primaryStage);
                });

                // Hide the next button as there is no next stage
                btnNext.setVisible(false);

                // Break out of the switch case
                break;
        }

        // Return the buttons with the respective stage
        return prevNext;
    }

    /**
     * This function is used to display the welcome messsage
     *
     * @param primaryStage is the stage where everyting is displayed
     */
    static void displayWelcome(Stage primaryStage) {
        //// Left Sidebar ////
        // Create the border that hold the information of the left sidebar
        BorderPane leftSidebar = displaySidebar(0);

        //// Right Sidebar ////
        //--------------------------------------------------------------------------------------------------------------Main Start
        // Create the right sidebar container
        BorderPane rightSidebar = new BorderPane();
        // Add padding to the container
        rightSidebar.setPadding(new Insets(20));

        // Set the temp color
        rightSidebar.setStyle("-fx-background-color: #ffffff;");
        //--------------------------------------------------------------------------------------------------------------Main End

        // Create a Border Pane holding the table view and a input and text and then set the padding
        BorderPane rightContent = new BorderPane();
        rightContent.setPadding(new Insets(10, 0, 0, 0));

        // Create the welcome messages and add the respective styling
        Label lblInformation = new Label("Welcome to Quota");
        lblInformation.setStyle("-fx-font-size: 50px;");

        Label lblDescription = new Label("Before you start using the application, you'll need to set up a few things:");
        lblDescription.setStyle("-fx-font-size: 25px;");

        Label lblAccounts = new Label("• Your bank accounts");
        lblAccounts.setStyle("-fx-font-size: 18px;");

        Label lblTransactions = new Label("• Any transactions you'd like to import");
        lblTransactions.setStyle("-fx-font-size: 18px;");

        Label lblBudget = new Label("• and, your budget!");
        lblBudget.setStyle("-fx-font-size: 18px;");

        // Add the welcome message to the vbox with spacing of 10 and align it center
        VBox welcomeMessage = new VBox(10);
        welcomeMessage.setAlignment(Pos.CENTER);
        welcomeMessage.getChildren().addAll(lblInformation, lblDescription, lblAccounts, lblTransactions, lblBudget);

        // set the center of the welcome screen with the vbox that hold the messages
        rightContent.setCenter(welcomeMessage);

        // Add the right side title to the right side container and then add the contents
        rightSidebar.setCenter(rightContent);
        rightSidebar.setBottom(displayPrevNext(0, primaryStage));

        // Create a border pane and use it as the main pane for the wizard
        BorderPane mainPane = new BorderPane();

        // Add the elements to the mainPane
        mainPane.setLeft(leftSidebar);
        mainPane.setCenter(rightSidebar);

        // Setting up the main pane to be displayed
        mainPane.setLeft(leftSidebar);
        mainPane.setCenter(rightSidebar);

        // Create the wizard scene
        Scene welcomeScene = new Scene(mainPane);

        // Setting the scene styling for the wizard
        QuotaGlobal.setApplicationStyle(welcomeScene, "default");

        // Sets the main scene background color to a dark purple
        mainPane.getStyleClass().add("welcomescreen-scene");

        // Sets the primary stage scene to the loginScene
        primaryStage.setScene(welcomeScene);

        // Sets the application title
        primaryStage.setTitle(QuotaGlobal.user.get("username").toString() + " - Wizard | Quota");
    }

    /**
     * This method is used to display the account wizard of the Quota Application
     *
     * @param primaryStage is a passed variable that is used to display the information on the screen
     */
    private static void displayAccounts(Stage primaryStage) {
        //// Left Sidebar ////
        // Create a borderpane that holds the information of the left sidebar
        BorderPane leftSidebar = displaySidebar(1);

        /// Right Sidebar ///
        //--------------------------------------------------------------------------------------------------------------Main Start
        // Create the right sidebar container
        BorderPane rightSidebar = new BorderPane();
        // Add padding to the container
        rightSidebar.setPadding(new Insets(20));

        // Set the temp color
        rightSidebar.setStyle("-fx-background-color: #ffffff;");
        //--------------------------------------------------------------------------------------------------------------Main End

        //--------------------------------------------------------------------------------------------------------------Title Start
        // Create labels that displays on the Right side and add styling
        VBox vboxTitleDescription = new VBox(10);

        // Create a label and add style and set the description
        Label lblTitle = new Label("Accounts");
        lblTitle.setStyle("-fx-font-size: 50px;");
        Label lblDescription = new Label("Let's start you off by adding some accounts!");
        lblDescription.setStyle("-fx-font-size: 20px;");

        // Add the items to the vbox
        vboxTitleDescription.getChildren().addAll(lblTitle, lblDescription);
        //--------------------------------------------------------------------------------------------------------------Title End

        //--------------------------------------------------------------------------------------------------------------Right Content Start

        // Create a Border Pane holding the table view and a input and text and then set the padding
        BorderPane rightContent = new BorderPane();
        rightContent.setPadding(new Insets(10, 0, 0, 0));

        // Add the right side title to the right side container and then add the contents
        rightSidebar.setTop(vboxTitleDescription);
        rightSidebar.setCenter(rightContent);

        // Create a border pane and use it as the main pane for the wizard
        BorderPane mainPane = new BorderPane();

        // Add the elements to the mainPane
        mainPane.setLeft(leftSidebar);
        mainPane.setCenter(rightSidebar);

        // Create an HBox for the buttons underneath the ListView
        HBox buttonBar = new HBox(10);
        buttonBar.setPadding(new Insets(10));
        buttonBar.setAlignment(Pos.CENTER);

        // Create the buttons for editing and removing accounts
        Button btnAdd = new Button("Add");

        Button btnEdit = new Button("Edit");
        btnEdit.setDisable(true); // Disable the edit button

        Button btnRemove = new Button("Remove");
        btnRemove.setDisable(true); // Disable the remove button

        // Add the buttons to the button bar
        buttonBar.getChildren().addAll(btnAdd, btnEdit, btnRemove);

        //--------------------------------------------------------------------------------------------------------------List Start
        // Create a new ListView that displays all the current user's accounts
        ListView<Account> accountListView = new ListView<>();
        accountListView.setFocusTraversable(true);
        accountListView.setMouseTransparent(false);
        accountListView.setMinWidth(350);

        // Temp styling for the text to be centered and be larger
        accountListView.setStyle("-fx-alignment: CENTER;");
        accountListView.setStyle("-fx-font-size: 18px");

        // Add the list
        ObservableList<Account> accounts = getAccountsFromDB(); // @deprecate
        accountListView.setItems(accounts);
        accountListView.getSelectionModel().select(null);

        // Creating a new BorderPane for the left section of the main area
        BorderPane accountsView = new BorderPane();
        accountsView.setPadding(new Insets(10));

        // Adding the ListView and Button Bar
        accountsView.setCenter(accountListView);
        accountsView.setBottom(buttonBar);

        // Setting the left to the newly created BorderPane
        rightContent.setLeft(accountsView);

        //--------------------------------------------------------------------------------------------------------------List End

        //--------------------------------------------------------------------------------------------------------------Text / Entry Area Begin

        // Creating the different labels for the fields a user can adjust accounts with
        Label lblAccountName = new Label("Account Name:");
        Label lblAccountType = new Label("Account Type:");
        Label lblCurrentBalance = new Label("Current Balance");

        // Creating the widgets for a user to interact with and adjust account values
        TextField txtAccountName = new TextField();
        txtAccountName.setEditable(false);

        // Create a combo box and an arraylist of account types
        ComboBox<String> cbAccountType = new ComboBox<>();
        ArrayList<String> accountTypes = new ArrayList<>();

        // Populating the combo box with all the different account options
        accountTypes.addAll(Arrays.asList("Checking", "Savings", "Credit", "Loan"));
        cbAccountType.setItems(FXCollections.observableArrayList(accountTypes));
        cbAccountType.setDisable(true);
        cbAccountType.setValue(null);

        // Add a text field to handle entering the current balance
        TextField txtCurrentBalance = new TextField();
        txtCurrentBalance.setEditable(false);
        txtCurrentBalance.setTextFormatter(new TextFormatter<>(new DoubleStringConverter()));

        // Create save and cancel buttons but don't display them
        Button btnSave = new Button("Save");
        btnSave.setVisible(false);

        Button btnCancel = new Button("Cancel");
        btnCancel.setVisible(false);

        // Create an area that contains all the widgets in one pane
        GridPane inputPane = new GridPane();
        inputPane.setPadding(new Insets(10, 0, 0, 0));
        inputPane.setStyle("-fx-font-size: 18px;");

        // Set the alignment of the inputPane to center
        inputPane.setAlignment(Pos.TOP_LEFT);

        // Set the h and v gaps to 10
        inputPane.setHgap(10);
        inputPane.setVgap(20);

        // Adding all the items to the inputPane
        inputPane.addRow(0, lblAccountName, txtAccountName);
        inputPane.addRow(1, lblAccountType, cbAccountType);
        inputPane.addRow(2, lblCurrentBalance, txtCurrentBalance);
        inputPane.add(lblError, 0, 3, 2, 1);
        inputPane.addRow(4, btnSave, btnCancel);

        // Populate the gridpane with the correct information
        rightContent.setCenter(inputPane);

        //--------------------------------------------------------------------------------------------------------------Text / Entry Area End

        //--------------------------------------------------------------------------------------------------------------Widget Logic Start
        // ListView Listener
        accountListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            // Check if the new value is not null
            if (newValue != null) {
                // Sets the TextFields with information from the currently selected ListView item
                txtAccountName.setText(accountListView.getSelectionModel().getSelectedItem().getAccountName());
                cbAccountType.setValue(Character.toUpperCase(accountListView.getSelectionModel().getSelectedItem().getAccountType().charAt(0)) + accountListView.getSelectionModel().getSelectedItem().getAccountType().substring(1));
                txtCurrentBalance.setText(Double.toString(accountListView.getSelectionModel().getSelectedItem().getBalance()));

                // Don't disable the buttons
                btnEdit.setDisable(false);
                btnRemove.setDisable(false);
            }
        });

        // Add Action
        btnAdd.setOnAction(e -> {
            // Set current state to Add
            currentState = "Add";

            // Don't set focus to the list view and allow mouse transparent
            accountListView.setFocusTraversable(false);
            accountListView.setMouseTransparent(true);

            // Don't disable the buttons
            btnAdd.setDisable(true);
            btnEdit.setDisable(true);
            btnRemove.setDisable(true);
            btnSave.setVisible(true);
            btnSave.setText("Add");
            btnCancel.setVisible(true);

            // Change the fields to be editable
            txtAccountName.setEditable(true);
            txtAccountName.setText("");
            cbAccountType.setDisable(false);
            cbAccountType.setValue("Checking");
            txtCurrentBalance.setEditable(true);
            txtCurrentBalance.setText("");
        });

        // Edit Action
        btnEdit.setOnAction(e -> {
            // Set current state to edit
            currentState = "Edit";

            // Create some temp variables that will hold the information to edit
            String accountName = accountListView.getSelectionModel().getSelectedItem().getAccountName();
            String accountType = accountListView.getSelectionModel().getSelectedItem().getAccountType();
            Double currentBalance = accountListView.getSelectionModel().getSelectedItem().getBalance();

            // Set the focus to true and don't put the mouse to transparent
            accountListView.setFocusTraversable(false);
            accountListView.setMouseTransparent(true);

            // Don't disable the buttons
            btnAdd.setDisable(true);
            btnEdit.setDisable(true);
            btnRemove.setDisable(true);
            btnSave.setVisible(true);
            btnCancel.setVisible(true);

            // Allow only certain fields to be edited
            txtAccountName.setEditable(true);
            cbAccountType.setDisable(false);
            txtCurrentBalance.setEditable(true);

            // Populate the correct fields
            txtAccountName.setText(accountName);
            cbAccountType.setValue(Character.toUpperCase(accountType.charAt(0)) + accountType.substring(1));
            txtCurrentBalance.setText(currentBalance.toString());
        });

        // Remove Action
        btnRemove.setOnAction(e -> {
            // Based on account selected, remove that account
            int accountIdToRemove = accountListView.getSelectionModel().getSelectedItem().getAccountId();

            // Grab the correct account from the database
            Account accountToRemove = Account.findById(accountIdToRemove);

            // Delete the record
            accountToRemove.delete();

            // Clear the accounts array list ans listview
            accounts.clear();
            accountListView.getItems().clear();

            // Refresh the array list and list view
            accounts.setAll(getAccountsFromDB());
            accountListView.setItems(accounts);
            accountListView.getSelectionModel().select(null);

            // Clear the inputs
            txtAccountName.setText("");
            cbAccountType.setValue(null);
            txtCurrentBalance.setText("");
        });

        // Handle for when the save button is pressed
        btnSave.setOnAction(e -> {
            // Handle for when the user is editing an account
            if(currentState.equals("Edit")) {
                // Try editing the account in the DB
                if (!Account.editAccountsDB(accountListView.getSelectionModel().getSelectedItem().getAccountId(), txtAccountName.getText(), cbAccountType.getValue().toLowerCase(), Double.parseDouble(txtCurrentBalance.getText()), null)) {
                    // Display Error message
                    QuotaGlobal.messageArea("The account was not able to be added to the Database. Please try again.", true);
                } else {
                    // Clear the error message
                    QuotaGlobal.messageArea("", false);

                    // set focus on list view and don't disable mouse
                    accountListView.setFocusTraversable(true);
                    accountListView.setMouseTransparent(false);

                    // Don't disable and hide some buttons
                    btnAdd.setDisable(false);
                    btnEdit.setDisable(false);
                    btnRemove.setDisable(false);
                    btnSave.setVisible(false);
                    btnCancel.setVisible(false);

                    // Don't allow edit on fields
                    txtAccountName.setEditable(false);
                    cbAccountType.setDisable(true);
                    txtCurrentBalance.setEditable(false);

                    // Clear the arraylist and listview
                    accounts.clear();
                    accountListView.getItems().clear();

                    // Set the array list and listview
                    accounts.setAll(getAccountsFromDB());
                    accountListView.setItems(accounts);
                    accountListView.getSelectionModel().select(null);

                    // Clear the inputs
                    txtAccountName.setText("");
                    cbAccountType.setValue(null);
                    txtCurrentBalance.setText("");
                }
            }else if(currentState.equals("Add")){
                // Set default value if the account balance is blank
                if (txtCurrentBalance.getText().equals("")) {
                    // Set the default to 0
                    txtCurrentBalance.setText("0");
                }

                // Check if the account was not added to database
                if (!Account.editAccountsDB(null, txtAccountName.getText(), cbAccountType.getValue().toLowerCase(), Double.parseDouble(txtCurrentBalance.getText()), null)) {
                    // Display error
                    QuotaGlobal.messageArea("The account was not able to be added to the Database. Please try again.", true);
                }else{
                    // Clear the error message
                    QuotaGlobal.messageArea("", false);

                    // Set focus to the listview and no mouse transparent
                    accountListView.setFocusTraversable(true);
                    accountListView.setMouseTransparent(false);

                    // Disable the buttons
                    btnRemove.setDisable(false);
                    btnSave.setVisible(false);
                    btnCancel.setVisible(false);

                    // Revert save button to say save again
                    btnSave.setText("Save");

                    // Don't allow edit on fields
                    txtAccountName.setEditable(false);
                    cbAccountType.setDisable(true);
                    txtCurrentBalance.setEditable(false);

                    // Clear the array list and listview
                    accounts.clear();
                    accountListView.getItems().clear();

                    // Reset the arraylist and listview
                    accounts.setAll(getAccountsFromDB());
                    accountListView.setItems(accounts);
                    accountListView.getSelectionModel().select(null);

                    // Don't disable the buttons
                    btnAdd.setDisable(false);
                    btnEdit.setDisable(false);
                    btnRemove.setDisable(false);
                    btnSave.setVisible(false);
                    btnCancel.setVisible(false);

                    // Clear the inputs
                    txtAccountName.setText("");
                    cbAccountType.setValue(null);
                    txtCurrentBalance.setText("");
                }
            }
        });

        // Cancel Action
        btnCancel.setOnAction(e -> {
            // Set the focus to true and don't put the mouse to transparent
            accountListView.setFocusTraversable(true);
            accountListView.setMouseTransparent(false);

            // Don't disable any button
            btnAdd.setDisable(false);
            btnEdit.setDisable(false);
            btnRemove.setDisable(false);
            btnSave.setVisible(false);
            btnCancel.setVisible(false);

            // Disable the editing ability of the input fields
            txtAccountName.setEditable(false);
            cbAccountType.setDisable(true);
            txtCurrentBalance.setEditable(false);

            // Revert save button to say save again
            btnSave.setText("Save");

            // Clear the error message
            QuotaGlobal.messageArea("", false);
        });

        //--------------------------------------------------------------------------------------------------------------Widget Logic End
        // Populate the bottom of the right sidebar
        rightSidebar.setBottom(displayPrevNext(1, primaryStage));

        // Setting up the main pane to be displayed
        mainPane.setLeft(leftSidebar);
        mainPane.setCenter(rightSidebar);

        // Create the wizard scene
        Scene accountScene = new Scene(mainPane);

        // Setting the scene styling for the wizard
        QuotaGlobal.setApplicationStyle(accountScene, "default");

        // Sets the main scene background color to a dark purple
        mainPane.getStyleClass().add("welcomescreen-scene");

        // Sets the primary stage scene to the loginScene
        primaryStage.setScene(accountScene);

        // Sets the application title
        primaryStage.setTitle(QuotaGlobal.user.get("username").toString() + " - Wizard | Quota");
    }

    /**
     * This method is used to display the transaction wizard of the Quota Application
     *
     * @param primaryStage is a passed variable that is used to display the information on the screen
     */
    static void displayTransactions(Stage primaryStage) {
        //// Left Sidebar ////
        // Create a borderpane that holds the information of the left sidebar
        BorderPane leftSidebar = displaySidebar(2);


        /// Right Sidebar ///
        //--------------------------------------------------------------------------------------------------------------Main Start
        // Create the right sidebar container
        BorderPane rightSidebar = new BorderPane();
        // Add padding to the container
        rightSidebar.setPadding(new Insets(20));

        // Set the temp color
        rightSidebar.setStyle("-fx-background-color: #ffffff;");
        //--------------------------------------------------------------------------------------------------------------Main End

        //--------------------------------------------------------------------------------------------------------------Title Start
        // Create labels that displays on the Right side and add styling
        VBox vboxTitleDescription = new VBox(10);

        // Create a label and add style and set the description
        Label lblTitle = new Label("Transactions");
        lblTitle.setStyle("-fx-font-size: 50px;");
        Label lblDescription = new Label("Want to start with information? You can import it here!");
        lblDescription.setStyle("-fx-font-size: 20px;");

        // Add the items to the vbox
        vboxTitleDescription.getChildren().addAll(lblTitle, lblDescription);
        //--------------------------------------------------------------------------------------------------------------Title End

        // Create the Table View for the list of transactions
        TableView<Transaction> tblTransactionData = new TableView<>();

        // Create the table columns
        TableColumn<Transaction, String> colDate = new TableColumn<>("Date");
        TableColumn<Transaction, String> colDesc = new TableColumn<>("Description");
        TableColumn<Transaction, String> colCategory = new TableColumn<>("Category");
        TableColumn<Transaction, String> colValue = new TableColumn<>("Value");
        colDesc.prefWidthProperty().bind(tblTransactionData.widthProperty().divide(2));

        // Set the cell factory for each column - Category Name and Amount
        colDate.setCellValueFactory(Transaction -> {
            SimpleStringProperty property = new SimpleStringProperty();
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            property.setValue(dateFormat.format(Transaction.getValue().getDate()));
            return property;
        });
        colDesc.setCellValueFactory(new PropertyValueFactory<>("Description"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("Category"));
        colValue.setCellValueFactory(new PropertyValueFactory<>("Value"));

        // Add the columns to the table and then add in the list
        tblTransactionData.getColumns().addAll(colDate, colDesc, colCategory, colValue);
        tblTransactionData.setItems(null);

        // Making the HBox that contains the account choosing combobox and the import button
        HBox underTable = new HBox(10);
        underTable.setPadding(new Insets(10));
        underTable.setAlignment(Pos.CENTER);

        // Creating the Combobox and label that allows the user to choose an account thats associated with their account
        Label lblUserAccounts = new Label("Accounts: ");
        ComboBox<Account> cbUserAccounts = new ComboBox<>();

        // Adding data to the combobox
        cbUserAccounts.setItems(getAccountsFromDB());

        // Set the preselected item to be a null object to not conflict with the listener
        cbUserAccounts.getSelectionModel().select(null);

        // Making the import button
        Button btnImport = new Button("Import Existing Data");

        // Disabling the import button (Will be reimported when the user selects an account)
        btnImport.setDisable(true);

        // Adding everything to the underTable HBox
        underTable.getChildren().addAll(lblUserAccounts, cbUserAccounts, btnImport);
        //--------------------------------------------------------------------------------------------------------------Right Content Start

        //--------------------------------------------------------------------------------------------------------------Widget Logic Start
        // User Accounts Combobox Listener
        cbUserAccounts.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            // Check if the new value is not null
            if (newValue != null) {
                // Enable the import button now that there's an actual account selected
                btnImport.setDisable(false);
            }
            try {
                tblTransactionData.setItems(FXCollections.observableArrayList(newValue != null ? newValue.getTransactions() : null));
            } catch (NullPointerException ex) {
                QuotaGlobal.displayError("Something went wrong!", "There was an issue involving loading the transactions!");
            }
        });

        // Import Button Listener
        btnImport.setOnAction(e -> {
            FileChooser fcImporter = new FileChooser();
            fcImporter.setTitle("Please choose a .csv file to import: ");
            fcImporter.getExtensionFilters().add(new FileChooser.ExtensionFilter("Comma Delimited Files", "*.csv"));

            File csvToImport = fcImporter.showOpenDialog(null);

            if (csvToImport != null) {
                cbUserAccounts.getSelectionModel().getSelectedItem().setTransactions(Transaction.importFromCSV(csvToImport));
                for (Transaction transaction: cbUserAccounts.getSelectionModel().getSelectedItem().getTransactions()) {
                    uploadTransactionToDB(transaction, cbUserAccounts.getSelectionModel().getSelectedItem());
                }
            }

            try {
                ObservableList<Transaction> olistTransactions = FXCollections.observableArrayList(cbUserAccounts.getSelectionModel().getSelectedItem().getTransactions());
                tblTransactionData.setItems(olistTransactions);
            } catch (NullPointerException ex) {
                QuotaGlobal.displayError("Something went wrong!", "There was an issue involving loading the transactions!");
            }
        });

        //--------------------------------------------------------------------------------------------------------------Widget Logic End

        // Create a Border Pane holding the table view and a input and text and then set the padding
        BorderPane rightContent = new BorderPane();
        rightContent.setPadding(new Insets(10, 0, 0, 0));

        rightContent.setCenter(tblTransactionData);
        rightContent.setBottom(underTable);

        // Add the right side title to the right side container and then add the contents
        rightSidebar.setTop(vboxTitleDescription);
        rightSidebar.setCenter(rightContent);
        rightSidebar.setBottom(displayPrevNext(2, primaryStage));

        // Create a border pane and use it as the main pane for the wizard
        BorderPane mainPane = new BorderPane();

        // Add the elements to the mainPane
        mainPane.setLeft(leftSidebar);
        mainPane.setCenter(rightSidebar);

        // Create the wizard scene
        Scene transactionScene = new Scene(mainPane);

        // Setting the scene styling for the wizard
        QuotaGlobal.setApplicationStyle(transactionScene, "default");

        // Sets the main scene background color to a dark purple
        mainPane.getStyleClass().add("welcomescreen-scene");

        // Sets the primary stage scene to the loginScene
        primaryStage.setScene(transactionScene);

        // Sets the application title
        primaryStage.setTitle(QuotaGlobal.user.get("username").toString() + " - Wizard | Quota");
    }


            /**
             * This method is used to display the budget wizard of the Quota Application
             *
             * @param primaryStage is a passed variable that is used to display the information on the screen
             */
    static void displayBudget(Stage primaryStage) {
        // Run query to db
        budgetList = Category.getBudgetFromDB();

        //// Left Sidebar ////
        // Create a borderpane that holds the information of the left sidebar
        BorderPane leftSidebar = displaySidebar(3);

        /// Right Sidebar ///
        //--------------------------------------------------------------------------------------------------------------Main Start
        // Create the right sidebar container
        BorderPane rightSidebar = new BorderPane();
        // Add padding to the container
        rightSidebar.setPadding(new Insets(20));

        // Set the temp color
        rightSidebar.setStyle("-fx-background-color: #ffffff;");
        //--------------------------------------------------------------------------------------------------------------Main End

        //--------------------------------------------------------------------------------------------------------------Title Start
        // Create a label that displays on the Right side and add styling
        VBox vboxTitleDescription = new VBox(10);

        // Create a title label and then set the style and description
        Label lblTitle = new Label("Budget");
        lblTitle.setStyle("-fx-font-size: 50px;");
        Label lblDescription = new Label("Now let's sort how your cash flows. Give every dollar a job!");
        lblDescription.setStyle("-fx-font-size: 20px;");

        // Add the title to the vbox
        vboxTitleDescription.getChildren().addAll(lblTitle, lblDescription);

        //--------------------------------------------------------------------------------------------------------------Title End

        //--------------------------------------------------------------------------------------------------------------Right Content Start
        // Create a Border Pane holding the table view and a input and text and then set the padding
        BorderPane rightContent = new BorderPane();
        rightContent.setPadding(new Insets(10, 0, 0, 0));

        //------------------------------------------------------------------------------------------------------------------------List Start
        // Create a tableView that displays the budget elements
        TableView<Category> tblBudgetData = new TableView<>();
        tblBudgetData.setEditable(true);
        tblBudgetData.setPlaceholder(new Label("There are currently no Budget entries to display."));

        // Create the table columns - category
        TableColumn<Category, String> colCategory = new TableColumn<>("Category");

        // Set the column width
        colCategory.prefWidthProperty().bind(tblBudgetData.widthProperty().divide(2));

        // Sort by the category column
        colCategory.setSortType(TableColumn.SortType.ASCENDING);

        // Temp styling for the text to be center
        colCategory.setStyle("-fx-alignment: CENTER;");

        // Create the table columns - amount
        TableColumn<Category, Double> colAmount = new TableColumn<>("Amount");

        // Set the column width
        colAmount.prefWidthProperty().bind(tblBudgetData.widthProperty().divide(2));

        // Temp styling for the text to be center
        colAmount.setStyle("-fx-alignment: CENTER;");

        // Set the sell factory for each column - category name and amount
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));

        // Add the columns to the table and then add in the list and then sort by category
        tblBudgetData.getColumns().addAll(colCategory, colAmount);
        tblBudgetData.setItems(budgetList);
        tblBudgetData.getSortOrder().add(colCategory);

        // Add the dummy array to the list view
        tblBudgetData.prefWidthProperty().bind(rightContent.widthProperty().divide(2));

        // Added the ability to edit cell
        colAmount.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));

        // Perform save action after done editing
        colAmount.setOnEditCommit(e -> {
            // Set the amount of the selected Budget Amount
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setAmount(e.getNewValue());

            // Set values based on changed entry
            String valueCategory = e.getTableView().getItems().get(e.getTablePosition().getRow()).getCategory();
            double valueAmount = e.getTableView().getItems().get(e.getTablePosition().getRow()).getAmount();

            // Save the Budget
            if(!Category.updateBudget(valueAmount, valueCategory)){
                // Display An error message
                QuotaGlobal.displayError("Editing Error", "There has been an error saving the amount to the database.");
            }
        });
        //------------------------------------------------------------------------------------------------------------------------List End

        // Create an hbox that will hold the ability to add and remove a record and set padding
        HBox budgetBar = new HBox(10);
        budgetBar.setPadding(new Insets(10));

        // Create buttons for adding and removing an entry
        Button btnAdd = new Button("Add");
        Button btnRemove = new Button("Remove");

        // Create the text inputs, prompt text and then width of field
        TextField txtCategory = new TextField();
        txtCategory.setPromptText("Budget Category");
        txtCategory.setPrefColumnCount(50);

        TextField txtAmount = new TextField();
        txtAmount.setPromptText("Budget Amount");
        txtAmount.setPrefColumnCount(50);
        txtAmount.setTextFormatter(new TextFormatter<>(new DoubleStringConverter())); // Set entry can only be a double value

        // Add the elements to the budget Bar
        budgetBar.getChildren().addAll(txtCategory, txtAmount, btnAdd, btnRemove);

        // Handle for adding a budget entry to the Database and table view
        btnAdd.setOnAction(e -> {
            // Hold the entered data in temp variables
            String category = txtCategory.getText();
            Double amount;

            // Dont create temp variable when field is blank as an exception is thrown
            if(!txtAmount.getText().equals("")) {
                // Create when there is something in the field
                amount = Double.parseDouble(txtAmount.getText());
            }else{
                amount = 0.0;
            }

            // Create a budget that will be added to the table view and database
            Category newBudget = new Category(category, amount);

            // Validate input fields
            if(!category.equals("") && (!Double.toString(amount).equals("") || amount == 0.0)) {
                // Process for validating category
                if(!Category.isBudget(category)) {
                    // Check if budget value is negative or anything else
                    if(amount >= 0) {
                        // Add the entry to the db and add to the table view if successful
                        if(Category.addBudget(newBudget)) {
                            // Add the budget to the table view and then refresh the table view
                            tblBudgetData.getItems().add(newBudget);
                            tblBudgetData.refresh();

                            // Clear the inputs
                            txtAmount.clear();
                            txtCategory.clear();
                        }else{
                            // Set the change to the text fields visually
                            QuotaGlobal.inputErrorTXT(txtCategory, false);
                            QuotaGlobal.inputErrorTXT(txtAmount, false);

                            // Display an error message
                            QuotaGlobal.displayError("Failed Entry", "The entry was not successful...");
                        }
                    }else{
                        // Set the change to the text fields visually
                        QuotaGlobal.inputErrorTXT(txtCategory, false);
                        QuotaGlobal.inputErrorTXT(txtAmount, true);

                        // Set focus to error field
                        txtAmount.requestFocus();

                        // Display an error message
                        QuotaGlobal.displayError("Small Entry", "The budget must be greater than or equal to $0.0.");
                    }
                }else{
                    // Set the change to the text fields visually
                    QuotaGlobal.inputErrorTXT(txtCategory, false);
                    QuotaGlobal.inputErrorTXT(txtAmount, true);

                    // Set focus to error field
                    txtAmount.requestFocus();

                    // Display an error message
                    QuotaGlobal.displayError("Duplicate Entry", "There's a duplicate Budget entry with that category.");
                }
            }else{
                // Set the change to the text fields visually
                QuotaGlobal.inputErrorTXT(txtCategory, true);
                QuotaGlobal.inputErrorTXT(txtAmount, true);

                // Set focus to error field
                txtCategory.requestFocus();

                // Display an error message
                QuotaGlobal.displayError("Empty Entry", "The required fields must be filled out!.");
            }
        });

        // Add the handling for when a user selects a budget entry to remove
        btnRemove.setOnAction(e -> {
            // Set values based on changed entry
            String valueCategory = tblBudgetData.getSelectionModel().getSelectedItem().getCategory();

            // Remove the Budget entry
            if(!Category.removeBudget(valueCategory)){
                // Display An error message
                QuotaGlobal.displayError("Removing Error", "There has been an error removing the selected budget from the database.");
            }else{
                // Remove the entry from the TableView only if removal from the database was successful
                tblBudgetData.getItems().remove(tblBudgetData.getSelectionModel().getSelectedItem());
            }
        });

        // Add the elements to the BorderPane
        rightContent.setCenter(tblBudgetData);
        rightContent.setBottom(budgetBar);

        // Add the right side title to the right side container and then add the contents
        rightSidebar.setTop(vboxTitleDescription);
        rightSidebar.setCenter(rightContent);
        rightSidebar.setBottom(displayPrevNext(3, primaryStage));

        // Create a border pane and use it as the main pane for the wizard
        BorderPane mainPane = new BorderPane();

        // Add the elements to the mainPane
        mainPane.setLeft(leftSidebar);
        mainPane.setCenter(rightSidebar);

        // Create the wizard scene
        Scene budgetScene = new Scene(mainPane);

        // Setting the scene styling for the wizard
        QuotaGlobal.setApplicationStyle(budgetScene, "default");

        // Sets the main scene background color to a dark purple
        mainPane.getStyleClass().add("welcomescreen-scene");

        // Sets the primary stage scene to the loginScene
        primaryStage.setScene(budgetScene);

        // Sets the application title
        primaryStage.setTitle(QuotaGlobal.user.get("username").toString() + " - Wizard | Quota");
    }

    /**
     *
     * This method is used to display the final stage of the wizard
     *
     * @param primaryStage Display the information in this stage
     *
     */
    private static void displayFinish(Stage primaryStage) {
        // Change the user to be not a new user anymore and save it in the database
        QuotaGlobal.user.set("newUser", "false");
        QuotaGlobal.user.saveIt();

        //// Left Sidebar ////
        // Create the border that hold the information of the left sidebar
        BorderPane leftSidebar = displaySidebar(4);

        //// Right Sidebar ////
        //--------------------------------------------------------------------------------------------------------------Main Start
        // Create the right sidebar container
        BorderPane rightSidebar = new BorderPane();
        // Add padding to the container
        rightSidebar.setPadding(new Insets(20));

        // Set the temp color
        rightSidebar.setStyle("-fx-background-color: #ffffff;");
        //--------------------------------------------------------------------------------------------------------------Main End

        // Create a Border Pane holding the table view and a input and text and then set the padding
        BorderPane rightContent = new BorderPane();
        rightContent.setPadding(new Insets(10, 0, 0, 0));

        // Label displaying the finish point
        Label lblMessage = new Label("Let's Dive in!");
        lblMessage.setStyle("-fx-font-size: 50px;");

        // Button to dive into application
        Button btnDive = new Button("Dive In");

        // Handle for pressing the dive button
        btnDive.setOnAction(e -> {
            // Change to the proper scene now
            displayDashboard(primaryStage);
        });

        // Add the welcome message to the vbox with spacing of 10 and align it center
        VBox welcomeMessage = new VBox(10);
        welcomeMessage.setAlignment(Pos.CENTER);
        welcomeMessage.getChildren().addAll(lblMessage, btnDive);

        // set the center of the welcome screen with the vbox that hold the messages
        rightContent.setCenter(welcomeMessage);

        // Add the right side title to the right side container and then add the contents
        rightSidebar.setCenter(rightContent);
        rightSidebar.setBottom(displayPrevNext(4, primaryStage));

        // Create a border pane and use it as the main pane for the wizard
        BorderPane mainPane = new BorderPane();

        // Add the elements to the mainPane
        mainPane.setLeft(leftSidebar);
        mainPane.setCenter(rightSidebar);

        // Setting up the main pane to be displayed
        mainPane.setLeft(leftSidebar);
        mainPane.setCenter(rightSidebar);

        // Create the wizard scene
        Scene welcomeScene = new Scene(mainPane);

        // Setting the scene styling for the wizard
        QuotaGlobal.setApplicationStyle(welcomeScene, "default");

        // Sets the main scene background color to a dark purple
        mainPane.getStyleClass().add("welcomescreen-scene");

        // Sets the primary stage scene to the loginScene
        primaryStage.setScene(welcomeScene);

        // Sets the application title
        primaryStage.setTitle(QuotaGlobal.user.get("username").toString() + " - Wizard | Quota");
    }
}
