// Belongs to the following package
package Quota.QuotaSQL;

// Class dependant imports
import Quota.QuotaGlobal;
import com.opencsv.CSVReader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.javalite.activejdbc.Model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static Quota.QuotaGlobal.user;

/**
  *
  * Created by Mark on 4/13/2017.
  * Updated by James Grau 4/16/2017
  *
  */

 /**
  *
  * This class is used to handle the transactions
  *
  * @author Mark Tov
  *
 */
public class Transaction extends Model {
    // Class dependant variables
    private Date date;
    private String description;
    private double value;
    private String category;

     /**
      *
      * This method is used as a constructor when a transaction is being updated
      *
      * @param date represents date of transaction
      * @param description represents description of transaction
      * @param value represents value of transaction
      * @param category represents category or transaction
      */
    public Transaction(Date date, String description, double value, String category){
        // Set the respective values
        this.date = date;
        this.description = description;
        this.value = value;
        this.category = category;
    }

    // Create a dummy constructor
    public Transaction() {

    }

     /**
      *
      * This method is used to return the date of a transaction
      *
      * @return the date of a transaction
      *
      */
    public Date getDate() {
        // Return the transaction date
        return date;
    }

     /**
      *
      * This method id used to set the date of the transaction
      *
      * @param date the date of transaction
      *
      */
    public void setDate(Date date) {
        // Set the transaction date
        this.date = date;
    }

     /**
      *
      * This method is used to return the description of a transaction
      *
      * @return the description of a transaction
      *
      */
    public String getDescription() {
        // Return the description of a transaction
        return description;
    }

     /**
      *
      * This method is used to set the description of the transaction
      *
      * @param description string representing the transaction description
      *
      */
    public void setDescription(String description) {
        // Set the transaction description
        this.description = description;
    }

     /**
      *
      * This methos is used to return the transaction value
      *
      * @return the transaction value
      *
      */
    public double getValue() {
        // Return the value of the transaction
        return value;
    }

     /**
      *
      * This method is used to set the value of the transaction
      *
      * @param value double representing the transaction value
      *
      */
    public void setValue(double value) {
        // Set the value of the transaction
        this.value = value;
    }

     /**
      *
      * This method is used to return the transaction category
      *
      * @return transaction category
      *
      */
    public String getCategory() {
        // Return the transaction category
        return category;
    }

     /**
      *
      * This method is used to set the category of a transaction
      *
      * @param category string representing the transaction category
      *
      */
    public void setCategory(String category) {
        // Set the transaction category
        this.category = category;
    }

     /**
      *
      * This method is used to import data from csv
      *
      * @param csvToImport file to be processed
      * @return boolean if the parsing of a csv file was successful or not
      *
      */
    public static ArrayList<Transaction> importFromCSV(File csvToImport) {
        // Create temp array list of transactions
        ArrayList<Transaction> transactions = new ArrayList<>();

        // Create date format
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");

        // Try parsing the csv passed file
        try {
            // Create new csv reader and read the passed file
            CSVReader reader = new CSVReader(new FileReader(csvToImport));

            // Create a list with the information if the file
            List csvData = reader.readAll();

            // Make sure list is not empty, process
            if (csvData != null) {
                // Loop though each list item
                for (int i = 0; i < csvData.size(); i++) {
                    // Create the required variables to hold the information from the passed csv file
                    String[] stringTransaction = (String[]) csvData.get(i);
                    Date date = df.parse(stringTransaction[0]);
                    String description = stringTransaction[1];
                    double value = Double.parseDouble(stringTransaction[2]);
                    String category = stringTransaction[3];

                    // Create a new transaction based on the data from the passed cav file
                    Transaction transactionToAdd = new Transaction(date, description, value, category);

                    // Add the transaction to the transaction list
                    transactions.add(transactionToAdd);
                }
            }
        } catch (FileNotFoundException ex) {
            // Display an error -- File Not Found
            QuotaGlobal.displayError("File not found!", "We can not find the file you chose!");
        } catch (IOException ex) {
            // Display an error -- File not readable
            QuotaGlobal.displayError("Could not read the file!", "Unfortunately, we couldn't read your file, are you sure it's a comma delimited file?");
        } catch (ParseException ex) {
            // Display an error -- Date error
            QuotaGlobal.displayError("Date Error!", "Are you sure all the dates in your comma delimited file are in the format of mm/dd/yyyy?");
        }

        // Return transactions
        return transactions;
    }

     /**
      *
      * This method handles uploading a transaction to the database
      *
      * @param transaction is the transaction to process
      * @param account is the account to deal with
      */
    public static boolean uploadTransactionToDB(Transaction transaction, Account account) {
        // Set the values and then save values to database
        transaction.set("date", transaction.getDate());
        transaction.set("accountId", account.getAccountId());
        transaction.set("description", transaction.getDescription());
        transaction.set("value", transaction.getValue());
        transaction.set("category", transaction.getCategory());
        transaction.set("userId", user.getId());
        transaction.saveIt();

        return true;
    }

     /**
      *
      * This is used to return database entries
      *
      * @return a list with the account from the Database
      *
      */
    public static ObservableList<Transaction> getFromDB() {
        // Create a blank list to hold the data from the database
        ObservableList<Transaction> transactionDB = FXCollections.observableArrayList();

        // Query the database based on the current user that is logged in and then store into a list
        List<Transaction> queryList = Transaction.where("userId = ?", Quota.QuotaGlobal.user .get("id").toString());

        // Loop through each query result and then add to the list getting returned
        for(Transaction transactionIteration: queryList) {
            // Grab the values from the database and set the into temp variables
            // Try to create date format and save correct date
            SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
            try {
                String[] splitDate = transactionIteration.get("date").toString().replaceAll("-", "/").split("/"); // Split and make date correct
                String dateFormat = splitDate[1] + "/" + splitDate[2] + "/" + splitDate[0]; // Create string for pare date

                Date date = df.parse(dateFormat);
                String description = transactionIteration.get("description").toString();
                double value = Double.parseDouble(transactionIteration.get("value").toString());
                String category = transactionIteration.get("category").toString();


                // Add a new instance of budget into the return list
                transactionDB.add(new Transaction(date, description, value, category));
            }catch(ParseException ex){
                System.out.println(ex.getMessage());
            }

        }

        // Return the list from the database
        return transactionDB;
    }

     /**
      *
      * This is used to return database entries
      *
      * @param account is the account at witch to filter
      * @return a list with the account from the Database
      *
      */
     public static ObservableList<Transaction> getFromDB(Account account) {
         // Create a blank list to hold the data from the database
         ObservableList<Transaction> transactionDB = FXCollections.observableArrayList();

         // Query the database based on the current user that is logged in and then store into a list
         List<Transaction> queryList = Transaction.where("userId = ? AND accountID = ?", Quota.QuotaGlobal.user .get("id").toString(), account.getAccountId());

         // Loop through each query result and then add to the list getting returned
         for(Transaction transactionIteration: queryList) {
             // Grab the values from the database and set the into temp variables
             // Try to create date format and save correct date
             SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
             try {
                 String[] splitDate = transactionIteration.get("date").toString().replaceAll("-", "/").split("/"); // Split and make date correct
                 String dateFormat = splitDate[1] + "/" + splitDate[2] + "/" + splitDate[0]; // Create string for pare date

                 Date date = df.parse(dateFormat);
                 String description = transactionIteration.get("description").toString();
                 double value = Double.parseDouble(transactionIteration.get("value").toString());
                 String category = transactionIteration.get("category").toString();


                 // Add a new instance of budget into the return list
                 transactionDB.add(new Transaction(date, description, value, category));
             }catch(ParseException ex){
                 System.out.println(ex.getMessage());
             }

         }

         // Return the list from the database
         return transactionDB;
     }

     /**
      *
      * This method is used to save the transaction to the database along with the currently logged in user's id value.
      *
      * @param description represents the description to edit
      * @param value represents the value to edit
      * @param category represents the category to edit
      * @param date represents the date to edit
      * @return boolean if saving was successful or not
      *
      */
     public static boolean updateTransaction(String description, String category, double value, Date date, String editingField){
         // Create a tmp transactions instance
         Transaction transactionVerify = new Transaction();

         // Check if description is being saved
         if(editingField.equals("D")) {
             // Grab the entry from the database and update its description value
             transactionVerify = Transaction.findFirst("userId = ? AND category = ? AND value = ? AND date = ?", Quota.QuotaGlobal.user.get("id").toString(), category, value, date);
         }else if(editingField.equals("C")) {
             // Grab the entry from the database and update its category value
             transactionVerify = Transaction.findFirst("userId = ? AND description = ? AND value = ? AND date = ?", Quota.QuotaGlobal.user.get("id").toString(), description, value, date);
         }else if(editingField.equals("V")) {
             // Grab the entry from the database and update its value value
             transactionVerify = Transaction.findFirst("userId = ? AND description = ? AND category = ? AND date = ?", Quota.QuotaGlobal.user.get("id").toString(), description, category, date);
         }else if(editingField.equals("DD")) {
             // Grab the entry from the database and update its date value
             transactionVerify = Transaction.findFirst("userId = ? AND description = ? AND category = ? AND value = ?", Quota.QuotaGlobal.user.get("id").toString(), description, category, value);
         }

         // If the requested is in the database, only update the record if different
         if (transactionVerify != null) {
             // Update for description update
             if (editingField.equals("D")) {
                 // Update the description for the requested transaction entry
                 transactionVerify.set("description", description);
             }else if (editingField.equals("C")) {
                 // Update the category for the requested transaction entry
                 transactionVerify.set("category", category);
             }else if (editingField.equals("V")) {
                 // Update the value for the requested transaction entry
                 transactionVerify.set("value", value);
             }else if (editingField.equals("DD")) {
                 // Update the value for the requested transaction entry
                 transactionVerify.set("date", date);
             }

             // Save entry to the database
             transactionVerify.saveIt();

             // Return true if the entry to the Database has been successful
             return true;
         }else{
             // Return false for update error
             return false;
         }
     }


     /**
      *
      * This method is used to remove an instance of the transaction in the database
      *
      * @param category is the category that the user wants to delete
      * @param date is the date of the transaction occurs
      * @param description is the description of the transaction occurs
      * @return boolean if the removal was successful
      *
      */
     public static boolean removeTransaction(String description, String category, Double value, Date date){
         // Delete the requested Transaction entry from the database
         Transaction transactionDelete = Transaction.findFirst("userId = ? AND category = ? AND description = ? AND value = ? AND date = ?", Quota.QuotaGlobal.user.get("id").toString(), category, description, value, date);

         // If the database return not null then process
         if (transactionDelete != null) {
             // Delete the requested transaction
             transactionDelete.delete();

             // Return true for successful deletion
             return true;
         }

         // Return false for failed deletion
         return false;
     }
}
