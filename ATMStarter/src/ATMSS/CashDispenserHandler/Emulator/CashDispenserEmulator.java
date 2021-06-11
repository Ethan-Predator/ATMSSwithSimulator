package ATMSS.CashDispenserHandler.Emulator;

import ATMSS.ATMSSStarter;
import ATMSS.CashDispenserHandler.CashDispenserHandler;
import AppKickstarter.misc.Msg;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

public class CashDispenserEmulator extends CashDispenserHandler {
    private ATMSSStarter atmssStarter;
    private String id;
    private Stage myStage;
    private CashDispenserEmulatorController cashDispenserEmulatorController;


    //------------------------------------------------------------
    // CashDispenserEmulator

    /**
     * Constructor for CashDispenserEmulator
     *
     * @param id           name of the application
     * @param atmssStarter name of ATM starter
     */
    public CashDispenserEmulator(String id, ATMSSStarter atmssStarter) throws Exception {
        super(id, atmssStarter);
        this.atmssStarter = atmssStarter;
        this.id = id;
    } // CashDispenserEmulator


    //------------------------------------------------------------
    // start

    /**
     * Start the cash dispenser emulator's GUI
     */

    public void start() throws Exception {
        Parent root;
        myStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        String fxmlName = "CashDispenserEmulator.fxml";
        loader.setLocation(CashDispenserEmulator.class.getResource(fxmlName));
        root = loader.load();
        cashDispenserEmulatorController = (CashDispenserEmulatorController) loader.getController();
        cashDispenserEmulatorController.initialize(id, atmssStarter, log, this);
        myStage.initStyle(StageStyle.DECORATED);
        myStage.setScene(new Scene(root, 350, 350));
        myStage.setTitle("CashDispenserHandler");
        myStage.setResizable(false);
//        myStage.setOnCloseRequest((WindowEvent event) -> {
//            atmssStarter.stopApp();
//            Platform.exit();
//        });
        myStage.setX(550);
        myStage.setY(800);
        myStage.show();
    } // start


    //------------------------------------------------------------
    // openDispenser

    /**
     * Ask cash dispenser emulator controller to open the dispenser
     *
     * @param msg message received from ATMSS
     */

    protected void openDispenser(Msg msg) {
        cashDispenserEmulatorController.openDispenser();
    }//openDispenser


    //------------------------------------------------------------
    // closeDispenser

    /**
     * Ask cash dispenser emulator controller to close the dispenser
     *
     * @param msg message received from ATMSS
     */

    protected void closeDispenser(Msg msg) {
        cashDispenserEmulatorController.closeDispenser();
    }//closeDispenser

    //------------------------------------------------------------
    // handleMoneyWithdraw

    /**
     * Ask cash dispenser emulator controller to withdraw money
     *
     * @param msg message received from ATMSS
     */

    protected void handleMoneyWithdraw(Msg msg) {
        cashDispenserEmulatorController.displayMoney(msg.getDetails());
//        cashDispenserEmulatorController.openDispenser();
    } // handleMoneyWithdraw


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
}
