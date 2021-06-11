package ATMSS.CashCollectorHandler;

import ATMSS.ATMSSStarter;
import ATMSS.HWHandler.HWHandler;
import AppKickstarter.misc.Msg;
import AppKickstarter.timer.Timer;


//======================================================================
// CashCollectorHandler
public class CashCollectorHandler extends HWHandler {

    //------------------------------------------------------------
    // CashCollectorHandler

    /**
     * Constructor for CashCollectorHandler
     *
     * @param id           name of the application
     * @param atmssStarter name of ATM starter
     */
    public CashCollectorHandler(String id, ATMSSStarter atmssStarter) {
        super(id, atmssStarter);
    } // CashCollectorHandler


    //------------------------------------------------------------
    // processMsg

    /**
     * Process message received from ATMSS
     *
     * @param msg message received from ATMSS
     */
    protected void processMsg(Msg msg) {
        switch (msg.getType()) {
            // here is to process the msg put in collector mbox
            case CC_Open:
                openCollector(msg);
                timerID = Timer.setTimer(id,mbox,20000);
                log.info("CashCollecterOpened: " + msg.getDetails());
                atmss.send(new Msg(id, mbox, Msg.Type.CC_Open, msg.getDetails()));
                break;

            case CC_Close:
                closeCollector(msg);
                try{
                    Timer.cancelTimer(id,mbox,timerID);
                }catch (Exception e){}
                log.info("CashCollecterClosed: " + msg.getDetails());
//                atmss.send(new Msg(id, mbox, Msg.Type.CC_Close, msg.getDetails()));
                break;

            case CC_MoneyCollected:
                atmss.send(new Msg(id, mbox, Msg.Type.CC_MoneyCollected, msg.getDetails()));
                break;

            case Severe_Hardware_Error:
                severe_problem = true;
                break;

            case Normal_Hardware_Error:
                normal_problem = true;
                break;

            case TimesUp:
                closeCollector(msg);
                atmss.send(new Msg(id, mbox, Msg.Type.CC_Close, msg.getDetails()));
                break;

            default:
                log.warning(id + ": unknown message type: [" + msg + "]");
        }
    } // processMsg


    //------------------------------------------------------------
    // openCollector

    /**
     * Log the open collector message received from ATMSS
     *
     * @param msg message received from ATMSS
     */

    protected void openCollector(Msg msg) {
        log.info(id + ": open cash collector");
    }//openCollector


    //------------------------------------------------------------
    // closeCollector

    /**
     * Log the close collector message received from ATMSS
     *
     * @param msg message received from ATMSS
     */

    protected void closeCollector(Msg msg) {
        log.info(id + ": close cash collector");
    }//closeCollector

} // CashCollectorHandler