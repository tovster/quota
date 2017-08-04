package Quota;

/**
 *
 * Created by james on 18/04/17.
 *
 */

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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;
import javafx.util.converter.DoubleStringConverter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import static Quota.QuotaSQL.Account.getAccountsFromDB;

/**
 *
 * This class is used to hold the core components of the Quota application
 *
 * @author James Grau
 *
 */
class Dashboard {
    // Class global variables
    private static Label lblError = QuotaGlobal.lblError;

    // Determining the current stage for an account (Add/Edit/Remove)
    private static String currentState = "";

    static void displayDashboard(Stage primaryStage) {
        // Create a tab pane to hold the different elements
        TabPane tabPane = new TabPane();

        // Create tab button - Transaction, set name, disable closing and set content of tab
        Tab tabTransactions = new Tab();
        tabTransactions.setText("Transactions");
        tabTransactions.setClosable(false);
        tabTransactions.setContent(transactionsView());

        Tab tabAccounts = new Tab();
        tabAccounts.setText("Accounts");
        tabAccounts.setClosable(false);
        tabAccounts.setContent(accountView());



        // Add the tab buttons to the button bar
        tabPane.getTabs().addAll(tabAccounts, tabTransactions);

        // Create the wizard scene
        Scene welcomeScene = new Scene(tabPane);

        // Setting the scene styling for the wizard
        QuotaGlobal.setApplicationStyle(welcomeScene, "default");

        // Sets the main scene background color to a dark purple
        tabPane.getStyleClass().add("welcomescreen-scene");

        // Sets the primary stage scene to the loginScene
        primaryStage.setScene(welcomeScene);

        // Sets the application title
        primaryStage.setTitle(QuotaGlobal.user.get("username").toString() + " - Dashboard | Quota");
    }


    private static BorderPane transactionsView() {
        BorderPane mainPane = new BorderPane();
        // Create the Table View for the list of transactions
        TableView<Transaction> tblTransactionData = new TableView<>();
        tblTransactionData.setEditable(true);

        // Create the table columns
        TableColumn<Transaction, String> colDate = new TableColumn<>("Date");
        TableColumn<Transaction, String> colDesc = new TableColumn<>("Description");
        TableColumn<Transaction, String> colCategory = new TableColumn<>("Category");
        TableColumn<Transaction, Double> colValue = new TableColumn<>("Value");
        colDesc.prefWidthProperty().bind(tblTransactionData.widthProperty().divide(2));

        // Set the cell factory for each column - Category Name and Amount
        colDate.setCellValueFactory(Transaction -> {
            // Formater
            SimpleStringProperty property = new SimpleStringProperty();

            // Reformat the date output
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            // Set the value of the date cell
            property.setValue(dateFormat.format(Transaction.getValue().getDate()));

            // Return the new date format
            return property;
        });
        colDate.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));

        colDesc.setCellValueFactory(new PropertyValueFactory<>("Description"));
        colDesc.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));

        colCategory.setCellValueFactory(new PropertyValueFactory<>("Category"));
        colCategory.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));

        colValue.setCellValueFactory(new PropertyValueFactory<>("Value"));
        colValue.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));

        // Add the columns to the table and then add in the list
        tblTransactionData.getColumns().addAll(colDate, colDesc, colCategory, colValue);
        tblTransactionData.setItems(Transaction.getFromDB());

        // Add the table view to the screen - center
        mainPane.setCenter(tblTransactionData);

        // Perform save action after done editing
        colDesc.setOnEditCommit(e -> {
            // Set the amount of the selected transaction description
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setDescription(e.getNewValue());

            // Set values based on changed entry
            String description = e.getTableView().getItems().get(e.getTablePosition().getRow()).getDescription();
            String category = e.getTableView().getItems().get(e.getTablePosition().getRow()).getCategory();
            double value = e.getTableView().getItems().get(e.getTablePosition().getRow()).getValue();
            Date date = e.getTableView().getItems().get(e.getTablePosition().getRow()).getDate();

            // Save the Transaction
            if(!Transaction.updateTransaction(description, category, value, date,"D")){
                // Display An error message
                QuotaGlobal.displayError("Editing Error", "There has been an error saving the description to the database.");
            }
        });

        // Perform save action after done editing
        colCategory.setOnEditCommit(e -> {
            // Set the amount of the selected Transaction category
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setCategory(e.getNewValue());
            // Set values based on changed entry
            String description = e.getTableView().getItems().get(e.getTablePosition().getRow()).getDescription();
            String category = e.getTableView().getItems().get(e.getTablePosition().getRow()).getCategory();
            double value = e.getTableView().getItems().get(e.getTablePosition().getRow()).getValue();
            Date date = e.getTableView().getItems().get(e.getTablePosition().getRow()).getDate();

            // Save the transaction
            if(!Transaction.updateTransaction(description, category, value, date, "C")){
                // Display An error message
                QuotaGlobal.displayError("Editing Error", "There has been an error saving the category to the database.");
            }
        });

        // Perform save action after done editing
        colValue.setOnEditCommit(e -> {
            // Set the amount of the selected Transaction value
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setValue(e.getNewValue());

            // Set values based on changed entry
            String description = e.getTableView().getItems().get(e.getTablePosition().getRow()).getDescription();
            String category = e.getTableView().getItems().get(e.getTablePosition().getRow()).getCategory();
            double value = e.getTableView().getItems().get(e.getTablePosition().getRow()).getValue();
            Date date = e.getTableView().getItems().get(e.getTablePosition().getRow()).getDate();

            // Save the transaction
            if(!Transaction.updateTransaction(description, category, value, date, "V")){
                // Display An error message
                QuotaGlobal.displayError("Editing Error", "There has been an error saving the value to the database.");
            }
        });

        // Perform save action after done editing
        colDate.setOnEditCommit(e -> {
            try {
                // Set the amount of the selected Transactions date
                e.getTableView().getItems().get(e.getTablePosition().getRow()).setDate(new SimpleDateFormat("dd/MM/yyyy").parse(e.getNewValue()));

                // Set values based on changed entry
                String description = e.getTableView().getItems().get(e.getTablePosition().getRow()).getDescription();
                String category = e.getTableView().getItems().get(e.getTablePosition().getRow()).getCategory();
                double value = e.getTableView().getItems().get(e.getTablePosition().getRow()).getValue();
                Date date = e.getTableView().getItems().get(e.getTablePosition().getRow()).getDate();

                // Save the transaction
                if (!Transaction.updateTransaction(description, category, value, date, "DD")) {
                    // Display An error message
                    QuotaGlobal.displayError("Editing Error", "There has been an error saving the date to the database.");
                }
            }catch(ParseException ex){}
        });

        // Create a vbox to hold the account list and title with padding 10
        VBox accountList = new VBox(10);
        accountList.setPadding(new Insets(10));

        // Create the accounts title
        Label lblAccounts = new Label("Account(s)");
        lblAccounts.setStyle("-fx-font-size: 50px; -fx-fill: #FFFFFF;");

        // Create a list view holding the account for that user
        ListView<Account> accountView = new ListView<>();

        // Add the items to the list view
        accountView.setItems(Account.getAccountsFromDB());

        // When user selects an account, change the table view to reflect
        accountView.setOnMouseClicked(e -> {
            // Update the table view
            tblTransactionData.setItems(Transaction.getFromDB(accountView.getSelectionModel().getSelectedItem()));
        });

        // Add a grid pane to add in entries
        GridPane addTransactionsPane = new GridPane();

        // Set the alignment of the selectionPane to center
        addTransactionsPane.setAlignment(Pos.CENTER_LEFT);

        // Set the horizontal and vertical gaps to 10
        addTransactionsPane.setHgap(10);
        addTransactionsPane.setVgap(10);

        // Create buttons for adding and removing an entry
        Button btnAdd = new Button("Add Transaction");
        Button btnRemove = new Button("Remove Selected Transaction");

        // Create the text inputs, prompt text and then width of field
        TextField txtCategory = new TextField();
        txtCategory.setPromptText("Transaction Category");
        txtCategory.setPrefColumnCount(33);

        TextField txtAmount = new TextField();
        txtAmount.setPromptText("Transaction Amount");
        txtAmount.setPrefColumnCount(33);
        txtAmount.setTextFormatter(new TextFormatter<>(new DoubleStringConverter())); // Set entry can only be a double value

        TextField txtDescription = new TextField();
        txtDescription.setPromptText("Transaction Description");
        txtDescription.setPrefColumnCount(33);

        DatePicker dtpDate = new DatePicker();
        dtpDate.setPromptText("Transaction Date");

        // Add the elements to the gridpane
        addTransactionsPane.addRow(0, dtpDate);
        addTransactionsPane.addRow(1, txtDescription);
        addTransactionsPane.addRow(2, txtCategory);
        addTransactionsPane.addRow(3, txtAmount);
        addTransactionsPane.addRow(4, btnAdd);
        addTransactionsPane.addRow(4, new Label(""));
        addTransactionsPane.addRow(6, btnRemove);

        // Add the handling for when a user selects a budget entry to remove
        btnRemove.setOnAction(e -> {
            // Set values based on changed entry
            String description = tblTransactionData.getSelectionModel().getSelectedItem().getDescription();
            String category = tblTransactionData.getSelectionModel().getSelectedItem().getCategory();
            double value = tblTransactionData.getSelectionModel().getSelectedItem().getValue();
            Date date = tblTransactionData.getSelectionModel().getSelectedItem().getDate();

            // Remove the Budget entry
            if(!Transaction.removeTransaction(description, category, value, date)){
                // Display An error message
                QuotaGlobal.displayError("Removing Error", "There has been an error removing the selected transaction from the database.");
            }else{
                // Remove the entry from the TableView only if removal from the database was successful
                tblTransactionData.getItems().remove(tblTransactionData.getSelectionModel().getSelectedItem());
            }
        });

        // Handle for adding a budget entry to the Database and table view
        btnAdd.setOnAction(e -> {
            // Hold the entered data in temp variables
            Date date;

            //Set date to today if nothing was entered
            try {
               date = java.sql.Date.valueOf(dtpDate.getValue());
            }catch(NullPointerException ex) {
                date = new Date();
            }

            String description = txtDescription.getText();
            String category = txtCategory.getText();
            Double amount;

            // Don't create temp variable when field is blank as an exception is thrown
            if(!txtAmount.getText().equals("")) {
                // Create when there is something in the field
                amount = Double.parseDouble(txtAmount.getText());
            }else{
                amount = 0.0;
            }

            // Create a transaction that will be added to the table view and database
            Transaction newTransaction = new Transaction(date, description, amount, category);

            // Validate input fields
            if(date != new Date() && !description.equals("") && !category.equals("") && (!Double.toString(amount).equals("") || amount == 0.0)) {
                if(accountView.getSelectionModel().getSelectedItem() != null) {
                    if (date.before(new Date()) || date.equals(new Date())) {
                        // Check if transaction value is negative or anything else
                        if (amount >= 0.0 || amount <= 0.0) {
                            // Add the entry to the db and add to the table view if successful
                            if(Transaction.uploadTransactionToDB(newTransaction, accountView.getSelectionModel().getSelectedItem())) {
                                // Add the budget to the table view and then refresh the table view
                                tblTransactionData.getItems().add(newTransaction);
                                tblTransactionData.refresh();

                                // Clear the inputs
                                txtAmount.clear();
                                txtCategory.clear();
                                txtDescription.clear();
                                dtpDate.setValue(null);
                            }else{
                                // Set the change to the text fields visually
                                QuotaGlobal.inputErrorTXT(txtDescription, false);
                                QuotaGlobal.inputErrorTXT(txtCategory, false);
                                QuotaGlobal.inputErrorDTE(dtpDate, false);
                                QuotaGlobal.inputErrorTXT(txtAmount, false);

                                // Display an error message
                                QuotaGlobal.displayError("Failed Entry", "The entry was not successful...");
                            }
                        } else {
                            // Set the change to the text fields visually
                            QuotaGlobal.inputErrorTXT(txtDescription, false);
                            QuotaGlobal.inputErrorTXT(txtCategory, false);
                            QuotaGlobal.inputErrorDTE(dtpDate, false);
                            QuotaGlobal.inputErrorTXT(txtAmount, true);

                            // Set focus to error field
                            txtAmount.requestFocus();

                            // Display an error message
                            QuotaGlobal.displayError("Small Entry", "The transaction must be greater than or equal to $0.0.");
                        }
                    } else {
                        // Set the change to the text fields visually
                        QuotaGlobal.inputErrorDTE(dtpDate, true);

                        // Set focus to error field
                        dtpDate.requestFocus();

                        // Display an error message
                        QuotaGlobal.displayError("Future Date", "Sorry, you can only select days including today and before.");
                    }
                }else{
                    // Display an error message
                    QuotaGlobal.displayError("Account Empty", "Must select an Account from the list.");
                }
            }else{
                // Set the change to the text fields visually
                QuotaGlobal.inputErrorDTE(dtpDate, true);
                QuotaGlobal.inputErrorTXT(txtDescription, true);
                QuotaGlobal.inputErrorTXT(txtCategory, true);
                QuotaGlobal.inputErrorTXT(txtAmount, true);

                // Set focus to error field
                txtCategory.requestFocus();

                // Display an error message
                QuotaGlobal.displayError("Empty Entry", "The required fields must be filled out!.");
            }
        });

        // Add the elements to the vbox
        accountList.getChildren().addAll(lblAccounts, accountView, addTransactionsPane);

        // Set the accounts vbox to the left side screen
        mainPane.setLeft(accountList);

        mainPane.setStyle("-fx-background-color: #FFFFFF;");


        // Return th pane to display
        return mainPane;
    }


    private static BorderPane accountView() {
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

        // Setting up the main pane to be displayed
        mainPane.setCenter(rightSidebar);

        // Returm the account view
        return mainPane;
    }
}