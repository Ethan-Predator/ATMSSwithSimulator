package ATMSS.CashDispenserHandler;

import ATMSS.HWHandler.HWHandler;
import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.*;
import AppKickstarter.timer.Timer;


//======================================================================
// CashDispenserHandler
public class CashDispenserHandler extends HWHandler {
    //------------------------------------------------------------
    // TouchDisplayHandler

    /**
     * Constructor for CashCollectorHandler
     *
     * @param id             name of the application
     * @param appKickstarter name of appKickstarter
     */

    public CashDispenserHandler(String id, AppKickstarter appKickstarter) throws Exception {
        super(id, appKickstarter);
    } // CashDispenserHandler


    //------------------------------------------------------------
    // processMsg

    /**
     * Process message received from ATMSS
     *
     * @param msg message received from ATMSS
     */
    protected void processMsg(Msg msg) {
        switch (msg.getType()) {
            case CD_Open:
                openDispenser(msg);
                timerID = Timer.setTimer(id,mbox,20000);
                handleMoneyWithdraw(msg);
                log.info("CashDispenserOpened: " + msg.getDetails());
                atmss.send(new Msg(id, mbox, Msg.Type.CD_Open, msg.getDetails()));
                break;

            case CD_Close:
                closeDispenser(msg);
                try{
                    Timer.cancelTimer(id,mbox,timerID);
                }catch (Exception e){}
                log.info("CashDispenserClosed: " + msg.getDetails());
                atmss.send(new Msg(id, mbox, Msg.Type.CD_Close, msg.getDetails()));
                break;

            case CD_MoneyTakenOut:
                atmss.send(new Msg(id, mbox, Msg.Type.CD_MoneyTakenOut, msg.getDetails()));
                break;

            case Withdraw_Confirm:
                atmss.send(new Msg(id, mbox, Msg.Type.Withdraw_Confirm, msg.getDetails()));
                break;

            case Severe_Hardware_Error:
                severe_problem = true;
                break;

            case Normal_Hardware_Error:
                normal_problem = true;
                break;

            case TimesUp:
                closeDispenser(msg);
                break;
            default:
                log.warning(id + ": unknown message type: [" + msg + "]");
        }
    } // processMsg


    //------------------------------------------------------------
    // openDispenser

    /**
     * Log the open dispenser message received from ATMSS
     *
     * @param msg message received from ATMSS
     */

    protected void openDispenser(Msg msg) {
        log.info(id + ": open cash collector");
    }//openDispenser


    //------------------------------------------------------------
    // closeDispenser

    /**
     * Log the close dispenser message received from ATMSS
     *
     * @param msg message received from ATMSS
     */

    protected void closeDispenser(Msg msg) {
        log.info(id + ": close cash collector");
    }//closeDispenser


    //------------------------------------------------------------
    // handleMoneyWithdraw

    /**
     * Log the money withdraw message received from ATMSS
     *
     * @param msg message received from ATMSS
     */

    protected void handleMoneyWithdraw(Msg msg) {
        log.info(id + ": money withdraw");
    } //handleMoneyWithdraw
}