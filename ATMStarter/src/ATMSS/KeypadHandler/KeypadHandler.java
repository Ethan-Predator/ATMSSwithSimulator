package ATMSS.KeypadHandler;

import ATMSS.HWHandler.HWHandler;
import ATMSS.ATMSSStarter;
import AppKickstarter.misc.*;


//======================================================================
// KeypadHandler
public class KeypadHandler extends HWHandler {
    //------------------------------------------------------------
    // KeypadHandler
    /**
     * Constructor for KeypadHandler
     *
     * @param id           name of the application
     * @param atmssStarter name of ATM starter
     */
    public KeypadHandler(String id, ATMSSStarter atmssStarter) {
	super(id, atmssStarter);
    } // KeypadHandler


    //------------------------------------------------------------
    // processMsg
    /**
     * Process the message put in the keypad message box
     * @param msg  msg that is put in the keypad message box
     */
    protected void processMsg(Msg msg) {
        switch (msg.getType()) {
            case KP_KeyPressed:
                atmss.send(new Msg(id, mbox, Msg.Type.KP_KeyPressed, msg.getDetails()));
                break;

            case Severe_Hardware_Error:
                severe_problem = true;
                break;

            case Normal_Hardware_Error:
                normal_problem = true;
                break;

            default:
                log.warning(id + ": unknown message type: [" + msg + "]");
        }
    } // processMsg
} // KeypadHandler
