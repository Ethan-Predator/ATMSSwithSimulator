package ATMSS.PrinterHandler;

import ATMSS.HWHandler.HWHandler;
import ATMSS.ATMSSStarter;
import AppKickstarter.misc.*;


//======================================================================
// PrinterHandler
public class PrinterHandler extends HWHandler {

    //------------------------------------------------------------
    // PrinterHandler
    /**
     * Constructor for PrinterHandler
     *
     * @param id           name of the application
     * @param atmssStarter name of ATM starter
     */
    public PrinterHandler(String id, ATMSSStarter atmssStarter) {
        super(id, atmssStarter);
    } // PrinterHandler


    //------------------------------------------------------------
    // processMsg
    /**
     * Process message received from ATMSS
     *
     * @param msg message received from ATMSS
     */
    protected void processMsg(Msg msg) {
        switch (msg.getType()) {
            case PT_Print:
                // print out the transaction details
                handlePrinter(msg);
                break;

            case Severe_Hardware_Error:
                severe_problem = true;
                break;

            case Normal_Hardware_Error:
                normal_problem = true;
                break;

            case Expected_Problem:
                atmss.send(new Msg(id, mbox, Msg.Type.Expected_Problem, ""));
                break;

            default:
                log.warning(id + ": unknown message type: [" + msg + "]");
        }
    } // processMsg


    //------------------------------------------------------------
    // handlePrinter
    /**
     * Log the message information that should be printed out
     *
     * @param msg message received from ATMSS
     */
    protected void handlePrinter(Msg msg) {
        log.info(id + ": print -- " + msg.getDetails());
        // set a format to print out the msg details
    } // handlePrinter
} // PrinterHandler
