package ATMSS.CashCollectorHandler.Emulator;

import ATMSS.ATMSSStarter;
import ATMSS.CashCollectorHandler.CashCollectorHandler;
import AppKickstarter.misc.Msg;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;


//======================================================================
// CashCollectorEmulator
public class CashCollectorEmulator extends CashCollectorHandler {
    private ATMSSStarter atmssStarter;
    private String id;
    private Stage myStage;
    private CashCollectorEmulatorController cashCollectorEmulatorController;

    //------------------------------------------------------------
    // CashCollectorEmulator

    /**
     * Constructor for CashCollectorEmulator
     *
     * @param id           name of the application
     * @param atmssStarter name of ATM starter
     */

    public CashCollectorEmulator(String id, ATMSSStarter atmssStarter) {
        super(id, atmssStarter);
        this.atmssStarter = atmssStarter;
        this.id = id;
    } // CashCollectorEmulator


    //------------------------------------------------------------
    // openCollector

    /**
     * Ask cash collector emulator controller to open the collector
     *
     * @param msg message received from ATMSS
     */

    protected void openCollector(Msg msg) {
        cashCollectorEmulatorController.openCollector();
    }// openCollector


    //------------------------------------------------------------
    // closeCollector

    /**
     * Ask cash collector emulator controller to close the collector
     *
     * @param msg message received from ATMSS
     */

    protected void closeCollector(Msg msg) {
        cashCollectorEmulatorController.closeCollector();
    }//closeCollector


    //------------------------------------------------------------
    // start

    /**
     * Start the cash collector emulator's GUI
     */

    public void start() throws Exception {
        Parent root;
        myStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        String fxmlName = "CashCollectorEmulator.fxml";
        loader.setLocation(CashCollectorEmulator.class.getResource(fxmlName));
        root = loader.load();
        cashCollectorEmulatorController = (CashCollectorEmulatorController) loader.getController();
        cashCollectorEmulatorController.initialize(id, atmssStarter, log, this);
        myStage.initStyle(StageStyle.DECORATED);
        myStage.setScene(new Scene(root, 350, 350));
        myStage.setTitle("CashCollectorHandler");
        myStage.setResizable(false);
//        myStage.setOnCloseRequest((WindowEvent event) -> {
//            atmssStarter.stopApp();
//            Platform.exit();
//        });
        myStage.setX(150);
        myStage.setY(800);
        myStage.show();
    } // start


    //------------------------------------------------------------
    // stageClose

    /**
     * Close the stage of GUI
     */

    public void stageClose() {
        Platform.runLater(
                () -> {
                    myStage.close();
                    // Update UI here.
                }
        );
    }//stageClose


    //------------------------------------------------------------
    // stageHide

    /**
     * Hide the stage of GUI
     */

        public void stageHide() {
        Platform.runLater(
                () -> {
                    myStage.hide();
                    // Update UI here.
                }
        );
    }//stageHide


    //------------------------------------------------------------
    // stageShow

    /**
     * Show the stage of GUI
     */

    public void stageShow() {
        Platform.runLater(
                () -> {
                    myStage.show();
                    // Update UI here.
                }
        );
    }//stageShow

} // CashCollectorEmulator