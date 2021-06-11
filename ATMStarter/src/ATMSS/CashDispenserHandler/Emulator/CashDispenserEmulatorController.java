package ATMSS.CashDispenserHandler.Emulator;

import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.MBox;
import AppKickstarter.misc.Msg;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.logging.Logger;


//======================================================================
// CashDispenserEmulatorController
public class CashDispenserEmulatorController {
    private String id;
    private AppKickstarter appKickstarter;
    private Logger log;
    private CashDispenserEmulator cashDispenserEmulator;
    private MBox cashDispenserMBox;
    public Label withdrawStatusField;
    public Label withdraw_100;
    public Label withdraw_500;
    public Label withdraw_1000;
    public Button takeOutButton;
    public Label dispenserStatus;

    //------------------------------------------------------------
    // initialize

    /**
     * Initialize cash dispenser emulator controller
     *
     * @param id                    name of the application
     * @param appKickstarter        object for starting the application
     * @param log                   object for logging
     * @param cashDispenserEmulator object for emulating the cash dispenser
     */

    public void initialize(String id, AppKickstarter appKickstarter, Logger log, CashDispenserEmulator cashDispenserEmulator) {
        this.id = id;
        this.appKickstarter = appKickstarter;
        this.log = log;
        this.cashDispenserEmulator = cashDispenserEmulator;
        this.cashDispenserMBox = appKickstarter.getThread("CashDispenserHandler").getMBox();
    } // initialize

    //------------------------------------------------------------
    // buttonPressed

    /**
     * Handle the button pressed event on Cash dispenser emulator
     *
     * @param actionEvent action received from GUI's button
     */

    public void buttonPressed(ActionEvent actionEvent) {
        Button btn = (Button) actionEvent.getSource();

        switch (btn.getText()) {
            case "Take Out":
                cashDispenserMBox.send(new Msg(id, cashDispenserMBox, Msg.Type.CD_MoneyTakenOut, ""));
                cashDispenserMBox.send(new Msg(id, cashDispenserMBox, Msg.Type.CD_Close, "withdraw successfully"));
                updateWithdrawStatus("Cash Taken Out");
                break;

            case "Severe Error":
                cashDispenserMBox.send(new Msg(id, cashDispenserMBox, Msg.Type.Severe_Hardware_Error, ""));
                break;

            case "Normal Error":
                cashDispenserMBox.send(new Msg(id, cashDispenserMBox, Msg.Type.Normal_Hardware_Error, ""));
                break;

            default:
                log.warning(id + ": unknown button: [" + btn.getText() + "]");
                break;
        }
    } // buttonPressed

    //------------------------------------------------------------
    // updateWithdrawStatus

    /**
     * Update the withdraw status on cash dispenser emulator
     *
     * @param status withdraw status
     */

    public void updateWithdrawStatus(String status) {
        Platform.runLater(() -> {
            withdrawStatusField.setText(status);
        });
    } // updateWithdrawStatus


    //------------------------------------------------------------
    // openDispenser

    /**
     * Update the GUI of emulator after the cash dispenser opened
     */

    public void openDispenser() {
        takeOutButton.setVisible(true);
        updateWithdrawStatus("");
        Platform.runLater(() -> {
            dispenserStatus.setText("Dispenser Open");
        });
    }//openDispenser


    //------------------------------------------------------------
    // closeDispenser

    /**
     * Update the GUI of emulator after the cash dispenser closed
     */

    public void closeDispenser() {
        takeOutButton.setVisible(false);
        Platform.runLater(() -> {
            dispenserStatus.setText("Dispenser Close");
            withdraw_100.setText("");
            withdraw_500.setText("");
            withdraw_1000.setText("");
//            withdrawStatusField.setText("");
        });
    }//closeDispenser


    //------------------------------------------------------------
    // displayMoney

    /**
     * Update the GUI of emulator and displaying the amount of different type of cash withdrew
     */

    public void displayMoney(String status) {
        String hundred = status.split("/")[1];
        String halfThousands = status.split("/")[2];
        String thousands = status.split("/")[3];

        Platform.runLater(() -> {
            withdraw_1000.setText(String.valueOf(thousands));
            withdraw_500.setText(String.valueOf(halfThousands));
            withdraw_100.setText(String.valueOf(hundred));
        });
    }// displayMoney
}