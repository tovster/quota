// Quota Java file belongs to the Quota Package
package Quota;

/**
 * @author James Grau & Mark Tov
 * @version v0.0.1
 * @date March 14, 2017
 **/

// Application dependant Imports

import Quota.QuotaSQL.QuotaSQL;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;

import static Quota.Dashboard.displayDashboard;
import static Quota.WelcomeScreen.displayLogin;
import static Quota.Wizard.displayBudget;
import static Quota.Wizard.displayWelcome;


/**
 *
 * This class is used as the core of the Quota application which extends the imported Javafx Application package
 *
 * @author Mark Tov
 *
 */
public class Quota extends Application {

    /**
     *
     * Method to pass argument and launch the main application
     *
     * @param args holds arguments that are passed through to the console
     *
     */
    public static void main(String[] args) {
        // Pass the arguments to javafx
        launch(args);
    }

    /**
     *
     * Method that runs the visual application
     *
     * @param primaryStage hold the main stage of the application
     *
     */
    @Override
    public void start(Stage primaryStage) {
        QuotaSQL.connectToDB();

        // Display the login screen
        displayLogin(primaryStage);

        /// DEVELOPMENT PURPOSES
        //displayWelcome(primaryStage);
        //displayAccounts(primaryStage);
        //displayTransactions(primaryStage);
        //displayBudget(primaryStage);
        //displayDashboard(primaryStage);

        // Get the screen size
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        // set the stage artifacts
        primaryStage.setX(bounds.getMinX());
        primaryStage.setY(bounds.getMinY());
        primaryStage.setWidth(bounds.getWidth());
        primaryStage.setHeight(bounds.getHeight());
        primaryStage.setTitle("Quota");
        primaryStage.getIcons().add(QuotaGlobal.iconImage("Black"));
        primaryStage.show();
    }
}
