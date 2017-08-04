// This class belongs to the following package
package Quota.QuotaSQL;

// Class dependant imports
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.javalite.activejdbc.Model;

import java.text.StringCharacterIterator;
import java.util.*;

import static Quota.QuotaGlobal.user;

/**
 *
 * Created by Mark on 4/13/2017.
 * Updated by James on 4/16/2017
 *
 */

/**
 *
 * This class is used to handle the accounts
 *
 */
public class Account extends Model {
    // Class dependant variables
    private int accountId;
    private String accountName;
    private String accountType;
    private double balance;
    private boolean withdrawable;
    private ArrayList<Transaction> transactions;

    /**
     *
     * This method is used to create the accounts and the respective transactions
     *
     * @param accountId is the account id
     * @param accountName is the account name
     * @param accountType is the account type
     * @param balance is the account balance
     * @param withdrawable is the account withdrawale?
     * @param transactions list of all of the transaction by that account
     *
     */
    public Account(int accountId, String accountName, String accountType, double balance, boolean withdrawable, ArrayList<Transaction> transactions) {
        // Set the respective variables with the passed values
        this.accountId = accountId;
        this.accountName = accountName;
        this.accountType = accountType;
        this.balance = balance;
        this.withdrawable = withdrawable;
        this.transactions = transactions;
    }

    /**
     *
     * This method is used to create an account
     *
     * @param accountName is the account name
     * @param accountType is the account type
     * @param balance is the account balance
     * @param withdrawable is the account withdrawale?
     * @param transactions list of all of the transaction by that account
     */
    public Account(String accountName, String accountType, double balance, boolean withdrawable, ArrayList<Transaction> transactions) {
        // Set the respective variables with the passed values
        this.accountName = accountName;
        this.accountType = accountType;
        this.balance = balance;
        this.withdrawable = withdrawable;
        this.transactions = transactions;
    }

    // Create a dummy account creator
    public Account() {
        // Does nothing
    }

    /**
     *
     * This method is used to return the account id
     *
     * @return the account id
     *
     */
    public int getAccountId() {
        // Return account id
        return accountId;
    }

    /**
     *
     * This method is used to return the account type
     *
     * @return the account type
     *
     */
    public String getAccountType() {
        // Return the account type
        return accountType;
    }

    /**
     *
     * This method is used to return the account name
     *
     * @return the account name
     *
     */
    public String getAccountName() {
        // Return the account name
        return accountName;
    }

    /**
     *
     * This method is used to set the account name
     *
     * @param accountName is a string representing the account name
     */
    public void setAccountName(String accountName) {
        // Set account name
        this.accountName = accountName;
    }

    /**
     *
     * This method is used to return the arraylist of transactions linked to the requested account
     *
     * @return the array list of transactions
     *
     */
    public ArrayList<Transaction> getTransactions() {
        // Return the list of transactinos
        return transactions;
    }

    public void setTransactions(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
    }

    /**
     *
     * This method is used to return the balance of the account
     *
     * @return the balance of the account
     *
     */
    public double getBalance() {
        // Return account balance
        return balance;
    }

    /**
     *
     * This method is used to set the account balance
     *
     * @param balance is a double representing the account balance
     *
     */
    public void setBalance(double balance) {
        // Set the account balance
        this.balance = balance;
    }

    /**
     *
     * This method is to check if the account is withdrawable or not
     *
     * @return a bool value if the account is withdrawable or not
     */
    public boolean isWithdrawable() {
        // Return account withdrawable
        return withdrawable;
    }

    /**
     *
     * This method is to handle depositing money int the account
     *
     * @param money double value of the amount to deposit
     * @return is the money was successful to deposit or not
     *
     */
    public boolean depositMoney(double money) {
        // Check the passed value
        if (money > 0) {
            // Determine the type of account
            if (getAccountType().equals("credit") || getAccountType().equals("loan")) {
                // If the account type is of credit or loan, remove money and set the new balance
                setBalance(getBalance() - money);
            } else {
                // Increase the account balance
                setBalance(getBalance() + money);
            }

            // Return true for successful deposit
            return true;
        } else {
            // Return false for
            return false;
        }
    }

    /**
     *
     * This method is used to handle the withdrawl from an account
     *
     * @param money double value representing the amount to be withdrawln
     * @return boolean if the withdrawl of the money was successful or not
     *
     */
    public boolean withdrawMoney(double money) {
        // Setting money to anything above 0 because overdraftable accounts exist
        if (money > 0 && isWithdrawable()) {
            // Determine if the account type is a credit type
            if (getAccountType().equals("credit")) {
                // Set the new balance of the account
                setBalance(getBalance() + money);
            } else {
                // Set the new balance of the account
                setBalance(getBalance() - money);
            }

            // Reuturn if the withdrawl is successful
            return true;
        } else {
            // Reutun if the withdrawl failed
            return false;
        }
    }

    /**
     *
     * This method deals with adding a transaction to the account
     *
     * @param transaction add a transaction to the account
     * @return if the adding of a transaction was successful or not
     *
     */
    public boolean addTransaction(Transaction transaction) {
        // Check if the transaction is not blank
        if (transaction != null) {
            // Add the transaction to the list
            this.transactions.add(transaction);

            // Retrn true on add successful
            return true;
        } else {
            // Return false on adding failure
            return false;
        }
    }

    /**
     *
     * This method deals with removing a transaction from the account
     *
     * @param transaction that is to be removed from the account
     * @return if the removal of the transaction is successful or not
     *
     */
    public boolean removeTransaction(Transaction transaction) {
        // Check to see if the transaction is not null
        if (transaction != null) {
            // Get the index of the requested transaction
            int indexToRemove = this.transactions.indexOf(transaction);

            // Make sure removal is in bounds
            if (indexToRemove == -1) {
                // Return false for removal error
                return false;
            } else {
                // Remove transaction entry from account list
                this.transactions.remove(indexToRemove);

                // Return for removal success
                return true;
            }
        } else {
            // Return false for removal error
            return false;
        }
    }

    /**
     *
     * This method is used to edit a transaction date
     *
     * @param transaction represents the transaction to edit
     * @param date represents the date of the transaction
     * @return boolean value if the transaction was edited successfully
     *
     */
    public boolean editTransaction(Transaction transaction, Date date) {
        // Make sure the passed transaction is not blank
        if (transaction != null) {
            // Get the array value of the transaction
            int indexToEdit = this.transactions.indexOf(transaction);

            // Make sure transaction is in bounds
            if (indexToEdit == -1) {
                // Return false on bound error
                return false;
            } else {
                // Edit the transaction
                this.transactions.get(indexToEdit).setDate(date);

                // Return true on edit successful
                return true;
            }
        } else {
            // Return false for transaction edit failure
            return false;
        }
    }

    /**
     *
     * This method is used to edit a transactions description or category
     *
     * @param transaction represents the transaction
     * @param value represents the transaction value
     * @param type represents the transaction type
     * @return boolean if the editing of the transaction was successful
     *
     */
    public boolean editTransaction(Transaction transaction, String value, String type) {
        // Make sure the transaction is not null
        if (transaction != null) {
            // Get index of the transactino in the list
            int indexToEdit = this.transactions.indexOf(transaction);

            // Make sure the transaction is in bounds
            if (indexToEdit == -1) {
                // Return false for bounds error
                return false;
            } else {
                // Switch based on passed transaction type
                switch (type) {
                    // Edit the description of the transaction
                    case "description":
                        // Set the transaction description
                        this.transactions.get(indexToEdit).setDescription(value);

                        // Return true on transaction edit success
                        return true;
                    //Edit the category of the transaction
                    case "category":
                        // Set the transaction category
                        this.transactions.get(indexToEdit).setCategory(value);

                        // Return true on transaction edit success
                        return true;
                    default:
                        // Return false for transaction edit error
                        return false;
                }
            }
        } else {
            // Return false for transaction edit error
            return false;
        }
    }

    /**
     *
     * This method is used to edit a transaction value
     *
     * @param transaction represents the editing transaction
     * @param value represents the transaction value
     * @return boolean if the edit of the transaction was successful
     *
     */
    public boolean editTransaction(Transaction transaction, double value) {
        // Make sure the transaction is not null
        if (transaction != null) {
            // Get the index of the transaction from the list
            int indexToEdit = this.transactions.indexOf(transaction);

            // Make sure thr transaction is in bounds
            if (indexToEdit == -1) {
                // Return false if bounds error
                return false;
            } else {
                // Edit the transaction value
                this.transactions.get(indexToEdit).setValue(value);

                // Return true on transaction edit error
                return true;
            }
        } else {
            // Return false on transaction edit error
            return false;
        }
    }

    /**
     *
     * This method is used to edit a transaction category
     *
     * @param transaction represents the requested transaction to edit
     * @param category represents the transaction category
     * @return boolean value stating if the edit of a transaction was successful or not
     *
     */
    public boolean editTransaction(Transaction transaction, String category) {
        // Make sure the transaction is not null
        if (transaction != null) {
            // Get the index of the transaction from the list
            int indexToEdit = this.transactions.indexOf(transaction);

            // Make sure the transaction is in bounds
            if (indexToEdit == -1) {
                // Return false on bounds error
                return false;
            } else {
                // Set the transaction category
                this.transactions.get(indexToEdit).setCategory(category);

                // Return true on transaction edit
                return true;
            }
        } else {
            // Return false on transaction edit
            return false;
        }
    }

    /**
     *
     * This method is used to return an observable list of accounts from the database
     *
     * @return an observable list of accounts
     *
     */
    public static ObservableList<Account> getAccountsFromDB() {
        // Create an observable list of accounts
        ObservableList<Account> accountList = FXCollections.observableArrayList(new ArrayList<>());

        // Create a temp list that holds the accounts from the database
        List<Account> list = Account.where("user_id = ?", user.get("id").toString());

        // Loop through each item in the list and process
        for (Account a : list) {
            // Set the withdrawable state of the account baced on the account type
            if (a.get("account_type").toString().equals("checking") || a.get("account_type").toString().equals("savings") || a.get("account_type").toString().equals("credit")) {
                // Create a new account with ability to withdraw
                accountList.add(new Account((int) a.getId(), a.get("account_name").toString(), a.get("account_type").toString(), Double.parseDouble(a.get("balance").toString()), true, new ArrayList<>()));
            } else if (a.get("account_type").toString().equals("loan")) {
                // Create a new account without the ability to withdraw
                accountList.add(new Account((int) a.getId(), a.get("account_name").toString(), a.get("account_type").toString(), Double.parseDouble(a.get("balance").toString()), false, new ArrayList<>()));
            }
        }

        // Return the new account list
        return accountList;
    }

    /**
     *
     * This method handles the editing of an account in the database
     *
     * @param accountId represents the account id
     * @param accountName represents the account name
     * @param accountType represents the account type
     * @param balance represents the account balance
     * @param transactions represents the associated transactions with the account
     * @return boolean of edit successful
     *
     */
    public static boolean editAccountsDB(Integer accountId, String accountName, String accountType, double balance, ArrayList<Transaction> transactions) {
        // Check to see if account name is long enough
        if(accountName.length() > 254 || accountName.length() < 1) {
            // Return false on account name error
            return false;
        }

        // Create a blank temp account
        Account account;

        // Check to see if the account id is blank
        if (accountId == null) {
            // Create a new account
            account = new Account();
        } else {
            // Set the account to the one form the database
            account = Account.findById(accountId);
        }

        // Make the change to the database and then save
        account.set("account_name", accountName);
        account.set("account_type", accountType);
        account.set("balance", balance);
        account.set("user_id", user.getId());
        account.saveIt();

        // Return true for edit success
        return true;
    }

    /**
     *
     * This method is used to override the toString method and return the account name
     *
     * @return the account name
     *
     */
    @Override
    public String toString() {
        // Return the account name
        return getAccountName();
    }
}
