package ATMSS.TouchDisplayHandler.Emulator;

import ATMSS.ATMSSStarter;
import ATMSS.TouchDisplayHandler.TouchDisplayHandler;
import AppKickstarter.misc.Msg;

import AppKickstarter.timer.Timer;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


//======================================================================
// TouchDisplayEmulator
public class TouchDisplayEmulator extends TouchDisplayHandler {
    private final int WIDTH = 680;
    private final int HEIGHT = 520;
    private ATMSSStarter atmssStarter;
    private String id;
    private Stage myStage;
    private TouchDisplayEmulatorController touchDisplayEmulatorController;
    private String currentPageName = "";

    //------------------------------------------------------------
    // TouchDisplayEmulator

    /**
     * Constructor for TouchDisplayEmulator
     *
     * @param id           name of the application
     * @param atmssStarter name of ATM starter
     **/

    public TouchDisplayEmulator(String id, ATMSSStarter atmssStarter) throws Exception {
        super(id, atmssStarter);
        this.atmssStarter = atmssStarter;
        this.id = id;
    } // TouchDisplayEmulator


    //------------------------------------------------------------
    // start

    /**
     * Start the touch display emulator's GUI
     */

    public void start() throws Exception {
        Parent root;
        myStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        String fxmlName = "TouchDisplayEmulator.fxml";
        loader.setLocation(TouchDisplayEmulator.class.getResource(fxmlName));
        root = loader.load();
        touchDisplayEmulatorController = (TouchDisplayEmulatorController) loader.getController();
        touchDisplayEmulatorController.initialize(id, atmssStarter, log, this);
        myStage.initStyle(StageStyle.DECORATED);
        myStage.setScene(new Scene(root, WIDTH, HEIGHT));
        myStage.setTitle("Touch Display");
        myStage.setResizable(false);
//        myStage.setOnCloseRequest((WindowEvent event) -> {
//            atmssStarter.stopApp();
//            Platform.exit();
//        });

        myStage.setX(10);
        myStage.setY(10);
        myStage.show();
    } // start


    //------------------------------------------------------------
    // handleUpdateDisplay

    /**
     * Handle message received from ATMSS to reload display pages
     *
     * @param msg message received from ATMSS
     */

    protected void handleUpdateDisplay(Msg msg) {
        log.info(id + ": update display -- " + msg.getDetails());
        currentPageName = msg.getDetails();

        switch (msg.getDetails()) {
            case "BlankScreen":
                reloadStage("TouchDisplayEmulator.fxml");
                break;

            case "BlankScreen_severe":
                reloadStage("TouchDisplayEmulator.fxml");
                break;

            case "BlankScreen_normal":
                reloadStage("TouchDisplayEmulator.fxml");
                break;

            case "MainMenu":
                reloadStage("TouchDisplayMainMenu.fxml");
                break;

            case "Confirmation":
                reloadStage("TouchDisplayTransferConfirm.fxml");
                break;

            case "tryLoginAgain":
                reloadStage("TouchDisplayTryLoginAgain.fxml");
                break;

            case "EnterPin":
                reloadStage("EnterPin.fxml");
                break;

            case "SelectAcc":
                reloadStage("TouchDisplayShowAcc.fxml");
                break;

            case "SelectTransferAcc":
                reloadStage("TouchDisplayTransferAcc.fxml");
                break;

            case "EnterAmt":
                reloadStage("MoneyAmountInput.fxml");
                break;

            case "AccEnquiry":
                reloadStage("TouchDisplayAccEnquiry.fxml");
                break;

            case "Withdraw_success":
                reloadStage("TouchDisplayWithdrawConfirm.fxml");
                break;

            case "Transfer_success":
                reloadStage("TouchDisplayTransferConfirm.fxml");
                break;

            case "DisplayDep":
                reloadStage("DisplayDeposit.fxml");
                break;

            case "DisplayWithdrawal":
                reloadStage("DisplayWithdrawal.fxml");
                break;

            case "Deposit_success":
                reloadStage("TouchDisplayDepositConfirm.fxml");
                break;

            case "StmReq":
                reloadStage("TouchDisplayStmReq.fxml");
                break;

            case "ChqBookReq":
                reloadStage("TouchDisplayChqBookReq.fxml");
                break;

            case "ChgPinReq":
                reloadStage("TouchDisplayChgPinReq.fxml");
                break;

            case "StmReq_success":
                reloadStage("TouchDisplayStmReqConfirm.fxml");
                break;

            case "ChqBookReq_success":
                reloadStage("TouchDisplayChqBookReqConfirm.fxml");
                break;

            case "ChgPinReq_success":
                reloadStage("TouchDisplayChgPinReqConfirm.fxml");
                break;

            case "EnterOldPin":
                reloadStage("TouchDisplayEnterOldPin.fxml");
                break;

            case "EnterNewPin":
                reloadStage("TouchDisplayEnterNewPin.fxml");
                break;

            case "tryEnterAgain":
                reloadStage("TouchDisplayTryPinAgain.fxml");
                break;

            case "fail":
                reloadStage("TouchDisplayFailure.fxml");
                break;

            default:
                log.severe(id + ": update display with unknown display type -- " + msg.getDetails());
                break;
        }
    } // handleUpdateDisplay

    //------------------------------------------------------------
    // handlePinMsg

    /**
     * Ask touch display emulator controller to display pin message
     *
     * @param msg message received from ATMSS
     */

    protected void handlePinMsg(Msg msg) {
        touchDisplayEmulatorController.appendPinField("*");
    }  //handlePinMsg


    //------------------------------------------------------------
    //handleErasePin

    /**
     * Ask touch display emulator controller to erase pin message
     *
     * @param msg message received from ATMSS
     */

    protected void handleErasePin(Msg msg) {
        touchDisplayEmulatorController.erasePinField(msg.getDetails());
    } //handleErasePin


    //------------------------------------------------------------
    //handleFieldMsg

    /**
     * Ask touch display emulator controller to display field message
     *
     * @param msg message received from ATMSS
     */

    protected void handleFieldMsg(Msg msg) {
        touchDisplayEmulatorController.appendField(msg.getDetails());
    } //handleFieldMsg


    //------------------------------------------------------------
    //handleEraseField

    /**
     * Ask touch display emulator controller to erase field message
     *
     * @param msg message received from ATMSS
     */

    protected void handleEraseField(Msg msg) {
        touchDisplayEmulatorController.eraseField(msg.getDetails());
    } //handleEraseField


    //------------------------------------------------------------
    //handleAccEnquiry

    /**
     * Ask touch display emulator controller to display account balance
     *
     * @param msg message received from ATMSS
     */

    protected void handleAccEnquiry(Msg msg) {
        touchDisplayEmulatorController.showAccBalance(msg.getDetails());
    } //handleAccEnquiry


    //------------------------------------------------------------
    //displayWithdrawConfirmation

    /**
     * Ask touch display emulator controller to display withdraw confirmation
     *
     * @param msg message received from ATMSS
     */

    protected void displayWithdrawConfirmation(Msg msg) {
        touchDisplayEmulatorController.showWithdrawConfirmation(msg.getDetails());
    }//displayWithdrawConfirmation


    //------------------------------------------------------------
    //displayTransferConfirmation

    /**
     * Ask touch display emulator controller to display transfer confirmation
     *
     * @param msg message received from ATMSS
     */

    protected void displayTransferConfirmation(Msg msg) {
        touchDisplayEmulatorController.showTransferConfirmation(msg.getDetails());
    }//displayTransferConfirmation


    //------------------------------------------------------------
    //displayDepositConfirmation

    /**
     * Ask touch display emulator controller to display deposit confirmation
     *
     * @param msg message received from ATMSS
     */

    protected void displayDepositConfirmation(Msg msg) {
        touchDisplayEmulatorController.showDepositConfirmation(msg.getDetails());
    }//displayDepositConfirmation


    //------------------------------------------------------------
    //displayFailureMsg

    /**
     * Ask touch display emulator controller to display failure message for all errors
     *
     * @param msg message received from ATMSS
     */

    protected void displayFailureMsg(Msg msg) {
        touchDisplayEmulatorController.showFailureMsg(msg.getDetails());
    }//displayFailureMsg


    //------------------------------------------------------------
    //setAlertMsg

    /**
     * Ask touch display emulator controller to update alert message
     *
     * @param status alert status to be set
     */

    protected void setAlertMsg(String status) {
        touchDisplayEmulatorController.setAlertMsg(status);
    }//setAlertMsg


    //------------------------------------------------------------
    //setTimerCounter

    /**
     * Ask touch display emulator controller to set counter for timer
     *
     * @param status time counter status to be set
     */

    protected void setTimerCounter(String status) {
        touchDisplayEmulatorController.setTimerCounter(status);
    }//setTimerCounter


    //------------------------------------------------------------
    //prompt_out_of_paper

    /**
     * Ask touch display emulator controller to show "out of paper" status of printer
     */

    protected void prompt_out_of_paper() {
        touchDisplayEmulatorController.showOutOfPaper();
    }//prompt_out_of_paper


    //------------------------------------------------------------
    //handleTimesUp

    /**
     * Handle time up event in different condition by sending message to ATMSS
     *
     * @param msg message received from ATMSS
     */

    protected void handleTimesUp(Msg msg) {
        if (currentPageName.equals("EnterPin")) {
            handleUpdateDisplay(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "BlankScreen"));
            atmss.send(new Msg(id, mbox, Msg.Type.Keypad_Enter_TimeOut, "backToWelcomePage"));
        } else if (currentPageName.equals("BlankScreen_severe")) {
            handleUpdateDisplay(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "BlankScreen"));
            atmss.send(new Msg(id, mbox, Msg.Type.Shutdown_Component, broken_component));
        } else if (currentPageName.equals("BlankScreen_normal")) {
            handleUpdateDisplay(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "BlankScreen"));
            atmss.send(new Msg(id, mbox, Msg.Type.Reset_Component, broken_component));
        } else if(currentPageName.equals("EnterAmt") || currentPageName.equals("EnterOldPin") ||currentPageName.equals("EnterNewPin")){
            handleUpdateDisplay(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "MainMenu"));
            atmss.send(new Msg(id, mbox, Msg.Type.Keypad_Enter_TimeOut, "backToMainMenu"));
        }

//        switch (msg.getDetails()){
//            case "pin_timeout":
//                handleUpdateDisplay(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "BlankScreen"));
//                break;
//
//            default:
//                log.fine(id + ": unknown timesup message -- " + msg.getDetails());
//                break;
//        }
    } //handleTimesUp


    //------------------------------------------------------------
    //countdownMsg

    /**
     * Ask touch display emulator controller to show timer counting down message
     *
     * @param countDownTime counter of timer
     */

    protected void countdownMsg(int countDownTime) {
        touchDisplayEmulatorController.countdownMsg(countDownTime);
    } //countdownMsg


    //------------------------------------------------------------
    // reloadStage

    /**
     * Reload the stage of touch display emulator
     *
     * @param fxmlFName filename of fxml file to be reloaded
     */

    private void reloadStage(String fxmlFName) {
        TouchDisplayEmulator touchDisplayEmulator = this;

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    log.info(id + ": loading fxml: " + fxmlFName);

                    Parent root;
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(TouchDisplayEmulator.class.getResource(fxmlFName));
                    root = loader.load();
                    touchDisplayEmulatorController = (TouchDisplayEmulatorController) loader.getController();
                    touchDisplayEmulatorController.initialize(id, atmssStarter, log, touchDisplayEmulator);
                    myStage.setScene(new Scene(root, WIDTH, HEIGHT));

                    updateFXML(fxmlFName);
                } catch (Exception e) {
                    log.severe(id + ": failed to load " + fxmlFName);
                    e.printStackTrace();
                }
            }
        });
    } // reloadStage


    //------------------------------------------------------------
    // updateFXML

    /**
     * Send message to ATMSS to indicate that the stage has been fully reload
     *
     * @param fxmlFName filename of fxml file to be reloaded
     */

    private void updateFXML(String fxmlFName) {
        if (fxmlFName.equalsIgnoreCase("TouchDisplayAccEnquiry.fxml")) {
            atmss.send(new Msg(id, mbox, Msg.Type.Acc_Enquiry, "Account Enquiry"));
        } else if (fxmlFName.equals("TouchDisplayWithdrawConfirm.fxml")) {
            atmss.send(new Msg(id, mbox, Msg.Type.Withdraw_Confirm, ""));
        } else if (fxmlFName.equals("TouchDisplayTransferConfirm.fxml")) {
            atmss.send(new Msg(id, mbox, Msg.Type.Transfer_Confirm, ""));
        } else if (fxmlFName.equals("TouchDisplayDepositConfirm.fxml")) {
            atmss.send(new Msg(id, mbox, Msg.Type.Deposit_Confirm, ""));
        } else if (fxmlFName.equals("EnterPin.fxml")) {
            atmss.send(new Msg(id, mbox, Msg.Type.UpdataDisplayTimerID, Timer.setTimer(id, mbox, 61800) + ""));
            countdownMsg(60000);
        } else if (fxmlFName.equals("MoneyAmountInput.fxml")) {
            atmss.send(new Msg(id, mbox, Msg.Type.UpdataDisplayTimerID, Timer.setTimer(id, mbox, 61800) + ""));
            countdownMsg(60000);
        } else if (fxmlFName.equals("TouchDisplayEnterNewPin.fxml")) {
            atmss.send(new Msg(id, mbox, Msg.Type.UpdataDisplayTimerID, Timer.setTimer(id, mbox, 61800) + ""));
            countdownMsg(60000);
        }else if (fxmlFName.equals("TouchDisplayEnterOldPin.fxml")) {
            atmss.send(new Msg(id, mbox, Msg.Type.UpdataDisplayTimerID, Timer.setTimer(id, mbox, 61800) + ""));
            countdownMsg(60000);
        }else if (fxmlFName.equals("TouchDisplayFailure.fxml")) {
            atmss.send(new Msg(id, mbox, Msg.Type.Failure_Msg, ""));
        } else if (currentPageName.equals("BlankScreen_severe")) {
            setAlertMsg(broken_component + " is encountering a severe problem, \n" + broken_component + " will shutdown within 5 seconds");
            setTimerCounter("5");
            countdownMsg(5000);
            Timer.setTimer(id, mbox, 6000);
        } else if (currentPageName.equals("BlankScreen_normal")) {
            setAlertMsg(broken_component + " is encountering a normal problem, \n" + broken_component + " will restart within 5 seconds");
            setTimerCounter("5");
            countdownMsg(5000);
            Timer.setTimer(id, mbox, 6000);
        }
        if (fxmlFName.endsWith("Confirm.fxml") && !fxmlFName.contains("ChgPinReq")) {
            if (out_of_paper) prompt_out_of_paper();
        }
    }//updateFXML


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

} // TouchDisplayEmulator
