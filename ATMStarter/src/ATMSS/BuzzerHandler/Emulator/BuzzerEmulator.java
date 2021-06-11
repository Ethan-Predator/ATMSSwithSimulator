package ATMSS.BuzzerHandler.Emulator;

import ATMSS.ATMSSStarter;
import ATMSS.BuzzerHandler.BuzzerHandler;

import AppKickstarter.misc.Msg;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;


//======================================================================
// BuzzerEmulator
public class BuzzerEmulator extends BuzzerHandler {


    private ATMSSStarter atmssStarter;
    private String id;
    private Stage myStage;
    private BuzzerEmulatorController BuzzerEmulatorController;

    //------------------------------------------------------------
    // BuzzerEmulator

    /**
     * Constructor for BuzzerEmulator
     *
     * @param id           name of the application
     * @param atmssStarter name of ATM starter
     */
    public BuzzerEmulator(String id, ATMSSStarter atmssStarter) {
        super(id, atmssStarter);
        this.atmssStarter = atmssStarter;
        this.id = id;
    } // BuzzerEmulator


    //------------------------------------------------------------
    // start

    /**
     * Start the buzzer emulator's GUI
     */
    public void start() throws Exception {
        Parent root;
        myStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        String fxmlName = "BuzzerEmulator.fxml";
        loader.setLocation(BuzzerEmulator.class.getResource(fxmlName));
        root = loader.load();
        BuzzerEmulatorController = (BuzzerEmulatorController) loader.getController();
        BuzzerEmulatorController.initialize(id, atmssStarter, log, this);
        myStage.initStyle(StageStyle.DECORATED);
        myStage.setScene(new Scene(root, 290, 290));
        myStage.setTitle("BuzzerHandler");
        myStage.setResizable(false);
//        myStage.setOnCloseRequest((WindowEvent event) -> {
//            atmssStarter.stopApp();
//            Platform.exit();
//        });
        myStage.setX(1000);
        myStage.setY(800);
        myStage.show();
    } // start


    //------------------------------------------------------------
    // handleBuzzerRing

    /**
     * Ask buzzer emulator controller to ring the buzzer
     *
     * @param msg message received from ATMSS
     */

    public void handleBuzzerRing(Msg msg) throws Exception {
        BuzzerEmulatorController.setBuzzerRing();


    }// handleBuzzerRing


    //------------------------------------------------------------
    // handleBuzzerStop

    /**
     * Ask buzzer emulator controller to stop the buzzer
     *
     * @param msg message received from ATMSS
     */

    protected void handleBuzzerStop(Msg msg) {
        BuzzerEmulatorController.setBuzzerStop();
    } // handleBuzzerStop

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


}// BuzzerEmulator
