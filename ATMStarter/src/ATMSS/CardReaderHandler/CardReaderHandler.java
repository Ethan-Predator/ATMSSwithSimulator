package ATMSS.CardReaderHandler;

import ATMSS.HWHandler.HWHandler;
import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.*;


//======================================================================
// CardReaderHandler
public class CardReaderHandler extends HWHandler {
    //------------------------------------------------------------
    // CardReaderHandler
    /**
     * Constructor for BuzzerHandler
     *
     * @param id           name of the application
     * @param appKickstarter name of App Kcik starter
     */
    public CardReaderHandler(String id, AppKickstarter appKickstarter) {
	super(id, appKickstarter);
    } // CardReaderHandler


    //------------------------------------------------------------
    // processMsg
    /**
     * Process the message put in the card reader message box
     * @param msg  msg that is put in the card reader message box
     */
    protected void processMsg(Msg msg) {
        switch (msg.getType()) {
            case CR_CardInserted:
                // tell atmss the card has been inserted
                atmss.send(new Msg(id, mbox, Msg.Type.CR_CardInserted, msg.getDetails()));
                break;

            case CR_EjectCard:
                // received from atmss
                handleCardEject();
                break;

            case CR_CardRemoved:
                // have to tell atmss that the card has been removed
                atmss.send(new Msg(id, mbox, Msg.Type.CR_CardRemoved, msg.getDetails()));
                handleCardRemove();
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


    //------------------------------------------------------------
    // handleCardInsert
    /**
     *
     * log card inserted information
     */
    protected void handleCardInsert() {
	log.info(id + ": card inserted");
    } // handleCardInsert


    //------------------------------------------------------------
    // handleCardEject
    /**
     * log card ejected information
     *
     */
    protected void handleCardEject() {
	log.info(id + ": card ejected");
    } // handleCardEject


    //------------------------------------------------------------
    // handleCardRemove
    /**
     * log card status information
     *
     */
    protected void handleCardRemove() {
	log.info(id + ": card removed");
    } // handleCardRemove
} // CardReaderHandler
