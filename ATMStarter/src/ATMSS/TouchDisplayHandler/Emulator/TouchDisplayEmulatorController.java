package ATMSS.TouchDisplayHandler.Emulator;

import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.MBox;
import AppKickstarter.misc.Msg;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.util.logging.Logger;


//======================================================================
// TouchDisplayEmulatorController
public class TouchDisplayEmulatorController {
    private String id;
    private AppKickstarter appKickstarter;
    private Logger log;
    private TouchDisplayEmulator touchDisplayEmulator;
    private MBox touchDisplayMBox;
    public TextField pinNumberField;
    public TextField textField;
    public Label accTypeField;
    public Label accNoField;
    public Label accBalanceField;
    public Label accNumField;
    public Label toAccNumField;
    public Label toAccField;
    public Label accField;
    public Label amountField;
    public Label timerCounter;
    public Label failureTypeField;
    public Label reasonField;
    public Label alertMsg;
    public Label paperStatus;
    public Button toPrint1;
    public Button toPrint2;


    //------------------------------------------------------------
    // initialize

    /**
     * Initialize cash dispenser emulator controller
     *
     * @param id                    name of the application
     * @param appKickstarter        object for starting the application
     * @param log                   object for logging
     * @param touchDisplayEmulator object for emulating the touch display emulator
     */

    public void initialize(String id, AppKickstarter appKickstarter, Logger log, TouchDisplayEmulator touchDisplayEmulator) {
        this.id = id;
        this.appKickstarter = appKickstarter;
        this.log = log;
        this.touchDisplayEmulator = touchDisplayEmulator;
        this.touchDisplayMBox = appKickstarter.getThread("TouchDisplayHandler").getMBox();
    } // initialize


    //------------------------------------------------------------
    // td_mouseClick

    /**
     * Send the mouse click X and Y coordinates message
     *
     * @param mouseEvent mouse click event on the touch display emulator GUI
     */

    public void td_mouseClick(MouseEvent mouseEvent) {
        int x = (int) mouseEvent.getX();
        int y = (int) mouseEvent.getY();

        log.fine(id + ": mouse clicked: -- (" + x + ", " + y + ")");
        touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.TD_MouseClicked, x + " " + y));
    } // td_mouseClick


    //------------------------------------------------------------
    // td_acc_buttonPressed

    /**
     * Send the message indicating select account button was pressed
     *
     * @param actionEvent button pressed event on the touch display emulator GUI
     */

    public void td_acc_buttonPressed(ActionEvent actionEvent) {
        Button btn = (Button) actionEvent.getSource();
        String btnTxt = btn.getText();
        touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.Acc_Select, btnTxt));
    } // td_acc_buttonPressed


    //------------------------------------------------------------
    // td_stmReq_buttonPressed

    /**
     * Send the message indicating statement request button was pressed
     *
     * @param actionEvent button pressed event on the touch display emulator GUI
     */

    public void td_stmReq_buttonPressed(ActionEvent actionEvent) {
        Button btn = (Button) actionEvent.getSource();
        String btnTxt = btn.getText();
        touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.TD_AccStatementRequest, btnTxt));
    } // td_stmReq_buttonPressed


    //------------------------------------------------------------
    // td_chqBookReq_buttonPressed

    /**
     * Send the message indicating cheque book event request button was pressed
     *
     * @param actionEvent button pressed event on the touch display emulator GUI
     */

    public void td_chqBookReq_buttonPressed(ActionEvent actionEvent) {
        Button btn = (Button) actionEvent.getSource();
        String btnTxt = btn.getText();
        touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.TD_ChequeBookRequest, btnTxt));
    } // td_chqBookReq_buttonPressed


    //------------------------------------------------------------
    // function_buttonPressed

    /**
     * Send the message indicating function change button was pressed
     *
     * @param actionEvent button pressed event on the touch display emulator GUI
     */

    public void function_buttonPressed(ActionEvent actionEvent) {
        Button btn = (Button) actionEvent.getSource();
        String btnTxt = btn.getText();
        touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.Function_Change, btnTxt));
    } // function_buttonPressed


    //------------------------------------------------------------
    // appendPinField

    /**
     * Update the touch display emulator GUI to display Pin
     *
     * @param status Pin field message received
     */

    public void appendPinField(String status) {
        pinNumberField.appendText(status);
    } // appendPinField


    //------------------------------------------------------------
    // erasePinField

    /**
     * Update the touch display emulator GUI to erase Pin
     *
     * @param status Pin field message received
     */

    public void erasePinField(String status) {
        pinNumberField.setText(status);
    } // erasePinField


    //------------------------------------------------------------
    // appendField

    /**
     * Update the touch display emulator GUI to append field
     *
     * @param status Field message received
     */

    public void appendField(String status) {
        textField.appendText(status);
    } // appendField


    //------------------------------------------------------------
    // eraseField

    /**
     * Update the touch display emulator GUI to erase field
     *
     * @param status Field message received
     */

    public void eraseField(String status) {
        textField.setText(status);
    } // eraseField


    //------------------------------------------------------------
    // showAccBalance

    /**
     * Update the touch display emulator GUI to display account balance
     *
     * @param status Account balance message received
     */

    public void showAccBalance(String status) {
        String[] acc = status.split("/");
        String accType = acc[0].replaceFirst("\\(","").replaceFirst("\\)", "");
        String accNo = acc[1];
        String accBalance = acc[2];

        Platform.runLater(() -> {
            accTypeField.setText(String.valueOf(accType + " account"));
            accNoField.setText(String.valueOf(accNo));
            accBalanceField.setText(String.valueOf(accBalance));
        });

    } // showAccBalance


    //------------------------------------------------------------
    // showWithdrawConfirmation

    /**
     * Update the touch display emulator GUI to display withdraw confirmation page
     *
     * @param status Withdraw confirmation message received
     */

    public void showWithdrawConfirmation(String status) {

        String AccountNum = status.split("/")[0];
        String AccountType = status.split("/")[1];
        String WithdrawAmount = status.split("/")[2];

        Platform.runLater(() -> {
            accField.setText(AccountType + " " + AccountNum);
            amountField.setText(WithdrawAmount);
        });

    }// showWithdrawConfirmation


    //------------------------------------------------------------
    // showDepositConfirmation

    /**
     * Update the touch display emulator GUI to display deposit confirmation page
     *
     * @param status Deposit confirmation message received
     */

    public void showDepositConfirmation(String status) {

        String AccountNum = status.split("/")[0];
        String AccountType = status.split("/")[1];
        String WithdrawAmount = status.split("/")[2];

        Platform.runLater(() -> {
            accField.setText(AccountType + " " + AccountNum);
            amountField.setText(WithdrawAmount);
        });

    }// showDepositConfirmation


    //------------------------------------------------------------
    // showTransferConfirmation

    /**
     * Update the touch display emulator GUI to display transfer confirmation page
     *
     * @param status Transfer confirmation message received
     */

    public void showTransferConfirmation(String status) {
        String AccountNum = status.split("/")[0];
        String AccountType = status.split("/")[1];
        String toAccountNum = status.split("/")[2];
        String toAccountType = status.split("/")[3];
        String WithdrawAmount = status.split("/")[4];

        Platform.runLater(() -> {
            accField.setText(String.valueOf(AccountType + " "+AccountNum));
            toAccField.setText(String.valueOf(toAccountType +" "+ toAccountNum));
            amountField.setText(String.valueOf(WithdrawAmount));
        });

    }// showTransferConfirmation


    //------------------------------------------------------------
    // showFailureMsg

    /**
     * Update the touch display emulator GUI to display failure page
     *
     * @param status Failure message received
     */

    public void showFailureMsg(String status) {
        // status: failureType / failureReason
        String failureType = status.split("/")[0];
        String failureReason = status.split("/")[1];

        Platform.runLater(() -> {
            failureTypeField.setText(String.valueOf(failureType + " Failed!"));
            reasonField.setText(String.valueOf(failureReason));
        });
    }// showFailureMsg


    //------------------------------------------------------------
    // showFailureMsg

    /**
     * Update the touch display emulator GUI to display counting down the seconds
     *
     * @param countdownTime count down second message
     */

    protected void countdownMsg(int countdownTime) {
        Thread t = new Thread(() -> {
            try {
                for(int i = countdownTime/1000-1; i>=0;i--) {
                    Thread.sleep(1000);
                    Platform.runLater(() -> {
                        timerCounter.setText((Integer.parseInt(timerCounter.getText()) - 1) + "");
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        t.start();
    } //countdownMsg


    //------------------------------------------------------------
    // setAlertMsg

    /**
     *Update the touch display emulator GUI to display alert message
     *
     * @param status alert message
     */

    public void setAlertMsg(String status) {
        Platform.runLater(() -> {
            alertMsg.setText(status);
        });
    } // setAlertMsg


    //------------------------------------------------------------
    // setAlertMsg

    /**
     *Update the touch display emulator GUI to display second counting down
     *
     * @param status time counter message
     */

    protected void setTimerCounter(String status) {
        Platform.runLater(() -> {
            timerCounter.setText(status);
        });
    }//setTimerCounter


    //------------------------------------------------------------
    // setAlertMsg

    /**
     *Update the touch display emulator GUI to display "out of paper" status
     *
     */

    protected void showOutOfPaper() {
        Platform.runLater(() -> {
            paperStatus.setText("WARNING: Printer is out of paper");
            toPrint1.setDisable(true);
            toPrint2.setDisable(true);
        });
    }//setTimerCounter


    //------------------------------------------------------------
    // NormalProblem

    /**
     * Send "normal problem" message to ATMSS
     *
     */

    public void NormalProblem(MouseEvent mouseEvent) {
        touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.Normal_Hardware_Error, "itself"));
    }// NormalProblem


    //------------------------------------------------------------
    // SevereProblem

    /**
     * Send "servere problem" message to ATMSS
     *
     */

    public void SevereProblem(MouseEvent mouseEvent) {
        touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.Severe_Hardware_Error, "itself"));
    }// SevereProblem

} // TouchDisplayEmulatorController
