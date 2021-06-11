package ATMSS.PrinterHandler.Emulator;

import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.MBox;
import AppKickstarter.misc.Msg;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.logging.Logger;
import java.util.Date;


//======================================================================
// PrinterEmulatorController
public class PrinterEmulatorController {
    private String id;
    private AppKickstarter appKickstarter;
    private Logger log;
    private PrinterEmulator printerEmulator;
    private MBox printerMBox;
    public Label accNumField;
    public Label dateField;
    public Label amountField;
    public Label transacStatus;
    public Label transacTypeField;


    //------------------------------------------------------------
    // printMsg

    /**
     * Handle the button pressed event on Cash dispenser emulator
     *
     * @param accType     the account type of the certain account
     * @param accNum      the account number involved in the transaction
     * @param transacType the executed transaction type
     * @param amount      the amount involved in the transaction
     * @param status      the transaction status
     */
    public void printMsg(String accType, String accNum, String transacType, String amount, String status) {
        if (!amount.equals(" ")) {
            amount += "HKD";
        }

        final String amt = amount;

        Platform.runLater(() -> {
            accNumField.setText(accType + accNum);
            transacTypeField.setText(transacType);
            amountField.setText(amt);
            transacStatus.setText(status);
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            dateField.setText(dateFormat.format(date));
        });
    } // printMsg

    //------------------------------------------------------------
    // buttonPressed

    /**
     * Handle the button pressed event on Cash dispenser emulator
     *
     * @param actionEvent action received from GUI's button
     */
    public void buttonPressed(ActionEvent actionEvent) {
        javafx.scene.control.Button btn = (Button) actionEvent.getSource();

        switch (btn.getText()) {
            case "Severe Error":
                printerMBox.send(new Msg(id, printerMBox, Msg.Type.Severe_Hardware_Error, ""));
                break;

            case "Normal Error":
                printerMBox.send(new Msg(id, printerMBox, Msg.Type.Normal_Hardware_Error, ""));
                break;

            case "Out Of Paper":
                printerMBox.send(new Msg(id, printerMBox, Msg.Type.Expected_Problem, ""));
                break;

            default:
                log.warning(id + ": unknown button: [" + btn.getText() + "]");
                break;
        }
    } // buttonPressed


    //------------------------------------------------------------
    // initialize

    /**
     * Initialize printer emulator controller
     *
     * @param id              name of the application
     * @param appKickstarter  object for starting the application
     * @param log             object for logging
     * @param printerEmulator object for emulating the printer
     */
    public void initialize(String id, AppKickstarter appKickstarter, Logger log, PrinterEmulator printerEmulator) {
        this.id = id;
        this.appKickstarter = appKickstarter;
        this.log = log;
        this.printerEmulator = printerEmulator;
        this.printerMBox = appKickstarter.getThread("PrinterHandler").getMBox();
    } // initialize
} // PrinterEmulatorController
