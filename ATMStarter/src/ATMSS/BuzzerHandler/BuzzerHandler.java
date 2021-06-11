package ATMSS.BuzzerHandler;

import ATMSS.HWHandler.HWHandler;
import ATMSS.ATMSSStarter;
import AppKickstarter.misc.*;
import AppKickstarter.timer.Timer;


//======================================================================
// BuzzerHandler
public class BuzzerHandler extends HWHandler {
    //------------------------------------------------------------
    // BuzzerHandler

    /**
     * Constructor for BuzzerHandler
     *
     * @param id           name of the application
     * @param atmssStarter name of ATM starter
     */

    public BuzzerHandler(String id, ATMSSStarter atmssStarter) {
        super(id, atmssStarter);
    } // BuzzerHandler


    //------------------------------------------------------------
    // processMsg

    /**
     * Process message received from ATMSS
     *
     * @param msg message received from ATMSS
     */
    protected void processMsg(Msg msg) {
        switch (msg.getType()) {
            // to process the msg put in buzzer mbox
            case BZ_Ring:
                try {
                    handleBuzzerRing(msg);
                    timerID = Timer.setTimer(id,mbox,20000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case BZ_Stop:
//                System.out.println("bz stop");
                handleBuzzerStop(msg);
                try{
                    Timer.cancelTimer(id,mbox,timerID);
                }catch (Exception e){}
                break;

            case Severe_Hardware_Error:
                severe_problem = true;
                break;

            case Normal_Hardware_Error:
                normal_problem = true;
                break;

            case TimesUp:
                handleBuzzerStop(msg);
                break;

            default:
                log.warning(id + ": unknown message type: [" + msg + "]");
        }
    } // processMsg


    //------------------------------------------------------------
    // handleBuzzerRing

    /**
     * Log the ring buzzer message received from ATMSS
     *
     * @param msg message received from ATMSS
     */
    protected void handleBuzzerRing(Msg msg) throws Exception {
        log.info(id + ": ring buzzer -- " + msg.getDetails());
        // ring the buzzer here
    } // handleBuzzerRing


    //------------------------------------------------------------
    // handleBuzzerStop

    /**
     * Log the stop buzzer message received from ATMSS
     *
     * @param msg message received from ATMSS
     */
    protected void handleBuzzerStop(Msg msg) {
        log.info(id + ": stop buzzer -- " + msg.getDetails());
        // stop the buzzer here
        // can be coded together with ring buzzer
    } // handleBuzzerStop
} // BuzzerHandler