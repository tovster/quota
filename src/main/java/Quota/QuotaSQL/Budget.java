// Quota Java file belongs to the Quota Package
package Quota.QuotaSQL;

// Class Dependant import
import Quota.QuotaGlobal;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.javalite.activejdbc.Model;

import java.util.Formatter;
import java.util.List;

/**
 *
 * This class is used to create an instance of budget
 *
 * @author James Grau
 * @version v1.0.0
 * @date  April 11, 2017
 *
 **/
public class Budget extends Model {
    // Class dependant variables
    private String category;
    private double amount;
    public static ObservableList<Budget> obsBudgetEntries;

    /**
     *
     * This method is used to create a blank budget instance
     *
     */
    public Budget() { }

    /**
     *
     * This method is used to create a budget entry
     *
     * @param category is the string that hold the category name
     * @param amount is a double amount that holds the category amount
     *
     */
    public Budget(String category, double amount) {
        // Set the category
        this.category = category;

        // Set the amount
        this.amount = amount;

        //obsBudgetEntries.add(this);
    }

    /**
     *
     * The method is used to return/get the category name
     *
     * @return the category name as a string
     *
     */
    public String getCategory() {
        // Return the category as a string
        return category;
    }

    /**
     *
     * This method is used to set a category name
     *
     * @param category is a string that represents the category name
     *
     */
    public void setCategory(String category) {
        // Set the category
        this.category = category;
    }

    /**
     *
     * This method is used to return/get the category amount as a double
     *
     * @return the category amount as a string
     *
     */
    public double getAmount() {
        // Return the category amount as a double
        return amount;
    }

    /**
     *
     * This method is used to return/get the category amount as a formatted string
     *
     * @return the category amount as a string
     *
     */
    public String getAmountString() {
        // Create a string builder
        StringBuilder currencyFormatted = new StringBuilder();

        // Create a formatter
        Formatter currencyFormatter = new Formatter(currencyFormatted);

        // Create the string format
        String currencyFormat = "$%.2f";

        // Format the string with the correct format and then pass in the category amount to get formatted
        currencyFormatter.format(currencyFormat, getAmount());

        // Return the formatted category amount as a string
        return currencyFormatted.toString();
    }

    /**
     *
     * This method is used to set the category amount as a double
     *
     * @param amount is a double value that represents the category amount
     *
     */
    public void setAmount(double amount) {
        // Set the category amount as a double
        this.amount = amount;
    }

    /**
     *
     * This method is used to save the budget to the database along with the currently logged in user's id value.
     *
     * @param budget represents the budget to add to the database
     * @return category boolean if adding was successful or not
     *
     */
    public static boolean addBudget(Budget budget){
        // Insert the created budget to the database
        budget.set("associatedUser", QuotaGlobal.user.getId().toString());
        budget.set("category", budget.getCategory());
        budget.set("value", budget.getAmount());

        // Add entry to the database
        budget.saveIt();

        // Return true if the entry to the Database was successful
        return true;
    }

    /**
     *
     * This method is used to save the budget to the database along with the currently logged in user's id value.
     *
     * @param category represents the entry to edit
     * @return category boolean if saving was successful or not
     *
     */
    public static boolean updateBudget(double amount, String category){
        // Grab the entry from the database and update its amount value
        Budget budgetVerify = Budget.findFirst("associatedUser = ? AND category = ?", Quota.QuotaGlobal.user.get("id").toString(), category);

        // If the requested is in the database, only update the record if different
        if (budgetVerify != null){
            // Update the value for the requested budget entry
            budgetVerify.set("value", amount);

            // Save entry to the database
            budgetVerify.saveIt();

            // Return true if the entry to the Database has been successful
            return true;
        }else{
            // Return false for update error
            return false;
        }
    }

    /**
     *
     * This method is used to remove an instance of the budget in the database
     *
     * @param category is the category that the user wants to delete
     * @return boolean if the removal was successful
     *
     */
    public static boolean removeBudget(String category){
        // Delete the requested budget entry from the database
        Budget budgetDelete = Budget.findFirst("associatedUser = ? AND category = ?", Quota.QuotaGlobal.user.get("id").toString(), category);

        // If the database return not null then process
        if (budgetDelete != null) {
            // Delete the requested budget
            budgetDelete.delete();

            // Return true for successful deletion
            return true;
        }

        // Return false for failed deletion
        return false;
    }

    /**
     *
     * This method is used to query the database and add any stored entries in the budget list
     *
     */
    public static ObservableList<Budget> getBudgetFromDB() {
        // Temp array hlding the data fromthe database
        ObservableList<Budget> tmpList = FXCollections.observableArrayList();

        // Query the database based on the current user that is logged in and then store into a list
        List<Budget> queryList = Budget.where("associatedUser = ?", Quota.QuotaGlobal.user .get("id").toString());

        // Loop through each query result and then add to the list getting returned
        for(Budget budgetIteration: queryList) {
            // Add a new instance of budget into the return list
            tmpList.add(new Budget(budgetIteration.get("category").toString(), Double.parseDouble(budgetIteration.get("value").toString())));
        }

        // Return the list
        return tmpList;
    }

    /**
     *
     * This method is used to check if there is already an entry in the database
     *
     * @param category pass in the category to check if it is a category that already exists or not
     * @return tue if there is an entry in the database
     *
     */
    public static boolean isBudget(String category){
        // Check the requested budget entry from the database
        Budget budgetDelete = Budget.findFirst("associatedUser = ? AND category = ?", Quota.QuotaGlobal.user.get("id").toString(), category);

        // If the database return not null then process
        if (budgetDelete != null) {
            // Return true for being an entry
            return true;
        }

        // Return false for there not being an entry
        return false;
    }
}