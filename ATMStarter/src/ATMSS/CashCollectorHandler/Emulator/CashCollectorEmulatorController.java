package ATMSS.CashCollectorHandler.Emulator;

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
// CashCollectorEmulatorController
public class CashCollectorEmulatorController {
    private String id;
    private AppKickstarter appKickstarter;
    private Logger log;
    private CashCollectorEmulator cashCollectorEmulator;
    private MBox cashCollectorMBox;
    public TextField deposit_100;
    public TextField deposit_500;
    public TextField deposit_1000;
    public Label moneyStatusField;
    public Label collectorStatus;
    public Button inputButton;

    private double amt_100;
    private double amt_500;
    private double amt_1000;

    //------------------------------------------------------------
    // initialize

    /**
     * Initialize cash collector emulator controller
     *
     * @param id name of the application
     * @param appKickstarter object for starting the application
     * @param log object for logging
     * @param cashCollectorEmulator object for emulating the cash collector
     */

    public void initialize(String id, AppKickstarter appKickstarter, Logger log, CashCollectorEmulator cashCollectorEmulator) {
        this.id = id;
        this.appKickstarter = appKickstarter;
        this.log = log;
        this.cashCollectorEmulator = cashCollectorEmulator;
        this.cashCollectorMBox = appKickstarter.getThread("CashCollectorHandler").getMBox();
    } // initialize

    //------------------------------------------------------------
    // buttonPressed

    /**
     * Handle the button pressed event on Cash collector emulator
     *
     * @param actionEvent action received from GUI's button
     *
     */

    public void buttonPressed(ActionEvent actionEvent) {
        Button btn = (Button) actionEvent.getSource();

        switch (btn.getText()) {
            case "Put In":
                if (checkCash()) {
                    double depositAmount = amt_100*100 + amt_500*500 + amt_1000*1000;
                    if (depositAmount != 0) {
                        cashCollectorMBox.send(new Msg(id, cashCollectorMBox, Msg.Type.CC_MoneyCollected, depositAmount + ""));
                        cashCollectorMBox.send(new Msg(id, cashCollectorMBox, Msg.Type.CC_Close, depositAmount + ""));
                        updateDepositStatus("Money Collected");
                        closeCollector();
                    }
                } else {
                    log.warning(id + ": invalid cash input: [" + btn.getText() + "]");
                    updateDepositStatus("INVALID CASH INPUT");
                    // handle cash eject
                }
                break;

            case "Severe Error":
                cashCollectorMBox.send(new Msg(id, cashCollectorMBox, Msg.Type.Severe_Hardware_Error, ""));
                break;

            case "Normal Error":
                cashCollectorMBox.send(new Msg(id, cashCollectorMBox, Msg.Type.Normal_Hardware_Error, ""));
                break;

            default:
                log.warning(id + ": unknown button: [" + btn.getText() + "]");
                break;
        }
    } // buttonPressed

    //------------------------------------------------------------
    // updateDepositStatus

    /**
     * Update the deposit status on cash collector emulator
     *
     * @param status deposit status
     *
     */

    public void updateDepositStatus(String status) {
        Platform.runLater(() -> {
            moneyStatusField.setText(status);
        });
    } // updateDepositStatus


    //------------------------------------------------------------
    // checkCash

    /**
     * Check the amount of different type of cash collected from the cash collector emulator
     *
     */
    public boolean checkCash() {
        try {
            if (deposit_100.getText().equalsIgnoreCase("")) {
                amt_100 = 0;
            } else {
                amt_100 = Double.parseDouble(deposit_100.getText());
            }

            if (deposit_500.getText().equalsIgnoreCase("")) {
                amt_500 = 0;
            } else {
                amt_500 = Double.parseDouble(deposit_500.getText());
            }

            if (deposit_1000.getText().equalsIgnoreCase("")) {
                amt_1000 = 0;
            } else {
                amt_1000 = Double.parseDouble(deposit_1000.getText());
            }

            if (amt_100%1 == 0 && amt_500%1 == 0 && amt_1000%1 == 0) {
                return true;
            }
        }
        catch (Exception e) {
            return false;
        }
        return false;
    } // checkCash


    //------------------------------------------------------------
    // openCollector

    /**
     * Update the GUI of emulator after the cash collector opened
     *
     */

    public void openCollector(){
        inputButton.setVisible(true);
        deposit_100.setEditable(true);
        deposit_500.setEditable(true);
        deposit_1000.setEditable(true);
        updateDepositStatus("");
        Platform.runLater(() -> {
            collectorStatus.setText("Collecotr Open");
        });
    }//openCollector


    //------------------------------------------------------------
    // closeCollector

    /**
     * Update the GUI of emulator after the cash collector closed
     *
     */
    public void closeCollector(){

        inputButton.setVisible(false);
        deposit_100.setEditable(false);
        deposit_500.setEditable(false);
        deposit_1000.setEditable(false);

        deposit_100.setText("");
        deposit_500.setText("");
        deposit_1000.setText("");

        Platform.runLater(() -> {
            collectorStatus.setText("Collector Close");
        });
    }//closeCollector

} // CashCollectorEmulatorController
