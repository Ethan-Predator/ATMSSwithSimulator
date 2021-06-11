package ATMSS.TouchDisplayHandler;

import ATMSS.HWHandler.HWHandler;
import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.*;
import AppKickstarter.timer.Timer;


//======================================================================
// TouchDisplayHandler
public class TouchDisplayHandler extends HWHandler {
    protected String broken_component = "";
    protected Boolean out_of_paper = false;

    //------------------------------------------------------------
    // TouchDisplayHandler
    public TouchDisplayHandler(String id, AppKickstarter appKickstarter) throws Exception {
        super(id, appKickstarter);
    } // TouchDisplayHandler


    //------------------------------------------------------------
    // processMsg
    protected void processMsg(Msg msg) {
        switch (msg.getType()) {
            case TD_MouseClicked:
                atmss.send(new Msg(id, mbox, Msg.Type.TD_MouseClicked, msg.getDetails()));
                break;

            case TD_ButtonPressed:
                atmss.send(new Msg(id, mbox, Msg.Type.TD_ButtonPressed, msg.getDetails()));
                break;

            case Function_Change:
                atmss.send(new Msg(id, mbox, Msg.Type.Function_Change, msg.getDetails()));
                break;

            case Acc_Select:
                atmss.send(new Msg(id, mbox, Msg.Type.Acc_Select, msg.getDetails()));
                break;

            case TD_AccStatementRequest:
                atmss.send(new Msg(id, mbox, Msg.Type.TD_AccStatementRequest, msg.getDetails()));
                break;

            case TD_ChequeBookRequest:
                atmss.send(new Msg(id, mbox, Msg.Type.TD_ChequeBookRequest, msg.getDetails()));
                break;

            case TD_UpdateDisplay:
                handleUpdateDisplay(msg);
                break;

            case TD_DisplayAccBalance:
                // the msg here is accDetails
                handleAccEnquiry(msg);
                break;

            case Pin_Msg:
                handlePinMsg(msg);
                break;

            case Erase_Pin:
                handleErasePin(msg);
                break;

            case Field_Msg:
                handleFieldMsg(msg);
                break;

            case Erase_Field:
                handleEraseField(msg);
                break;

            case TD_WithdrawConfirmation:
                displayWithdrawConfirmation(msg);
                break;

            case TD_TransferConfirmation:
                displayTransferConfirmation(msg);
                break;

            case TD_DepositConfirmation:
                displayDepositConfirmation(msg);
                break;

            case TD_failureDisplay:
                displayFailureMsg(msg);
                break;

            case TimesUp:
                handleTimesUp(msg);
                break;

            case Severe_Hardware_Error:
                if (msg.getDetails().equals("itself")){
                    severe_problem = true;
                }
                else {
                    broken_component = msg.getDetails();
                    handleUpdateDisplay(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "BlankScreen_severe"));
                }
                break;

            case Normal_Hardware_Error:
                if (msg.getDetails().equals("itself")){
                    normal_problem = true;
                }
                else {
                    broken_component = msg.getDetails();
                    handleUpdateDisplay(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "BlankScreen_normal"));
                }
                break;
                
            case Expected_Problem:
                out_of_paper = true;
                break;
                
            default:
                log.warning(id + ": unknown message type: [" + msg + "]");
        }
    } // processMsg


    //------------------------------------------------------------
    // handleUpdateDisplay
    protected void handleUpdateDisplay(Msg msg) {
        log.info(id + ": update display -- " + msg.getDetails());
    } // handleUpdateDisplay

    //handlePinMsg
    protected void handlePinMsg(Msg msg) {
        log.info(id + ": handle pin message -- *");
    } //handlePinMsg

    //handleErasePin
    protected void handleErasePin(Msg msg) {
        log.info(id + ": erase pin message -- *");
    } //handleErasePin

    //handleFieldMsg
    protected void handleFieldMsg(Msg msg) {
        log.info(id + ": handle field message -- *");
    } //handleFieldMsg

    //handleEraseField
    protected void handleEraseField(Msg msg) {
        log.info(id + ": erase field message -- *");
    } //handleEraseField

    //handleAccEnquiry
    protected void handleAccEnquiry(Msg msg) {
        log.info(id + ": show account balance -- *");
    } //handleAccEnquiry

    protected void countdownMsg(int countDownTime) {
        log.info(id + ": handle Tick Message -- *");
    } //countdownMsg


    protected void handleTimesUp(Msg msg) {
        log.info(id + ": handleTimesUp -- *");
    } //handleTimesUp

    //displayWithdrawConfirmation
    protected void displayWithdrawConfirmation(Msg msg) {
        log.info(id + ": display withdraw confirmation -- *");
    }

    //displayTransferConfirmation
    protected void displayTransferConfirmation(Msg msg) {
        log.info(id + ": display transfer confirmation -- *");
    }

    //displayDepositConfirmation
    protected void displayDepositConfirmation(Msg msg) {
        log.info(id + ": display deposit confirmation -- *");
    }

    protected void displayFailureMsg(Msg msg) {
        log.info(id + ": display failure message -- *");
    }

    protected void setAlertMsg(String status) {
        log.info(id + ": display failure message -- *");
    }

    protected void setTimerCounter(String status) {
        log.info(id + ": display failure message -- *");
    }

    protected void prompt_out_of_paper() {
        log.info(id + ": display failure message -- *");
    }



} // TouchDisplayHandler
