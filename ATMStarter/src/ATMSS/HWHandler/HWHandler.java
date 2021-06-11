package ATMSS.HWHandler;

import ATMSS.ATMSSEmulatorStarter;
import ATMSS.BuzzerHandler.Emulator.BuzzerEmulator;
import ATMSS.CardReaderHandler.Emulator.CardReaderEmulator;
import ATMSS.CashCollectorHandler.Emulator.CashCollectorEmulator;
import ATMSS.CashDispenserHandler.Emulator.CashDispenserEmulator;
import ATMSS.KeypadHandler.Emulator.KeypadEmulator;
import ATMSS.PrinterHandler.Emulator.PrinterEmulator;
import ATMSS.TouchDisplayHandler.Emulator.TouchDisplayEmulator;
import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.*;
import javafx.application.Platform;


//======================================================================
// HWHandler
public class HWHandler extends AppThread {
    protected MBox atmss = null;
    protected Boolean severe_problem = false;
    protected Boolean normal_problem = false;
    private Boolean restart = false;
    protected int timerID = 0;

    //------------------------------------------------------------
    // HWHandler

    /**
     * Constructor for HWHandler
     *
     * @param id             name of the application
     * @param appKickstarter name of the app kick starter
     */
    public HWHandler(String id, AppKickstarter appKickstarter) {
        super(id, appKickstarter);
    } // HWHandler


    //------------------------------------------------------------
    // run

    /**
     * Run the hardware handler and start receiving the message
     */
    public void run() {
        atmss = appKickstarter.getThread("ATMSS").getMBox();
        log.info(id + ": starting...");

        for (boolean quit = false; !quit; ) {
            Msg msg = mbox.receive();

            log.fine(id + ": message received: [" + msg + "].");

            switch (msg.getType()) {
                case Poll:
                    if (severe_problem) {
                        atmss.send(new Msg(id, mbox, Msg.Type.Severe_Hardware_Error, ""));
                        severe_problem = false;
                    } else if (normal_problem) {
                        atmss.send(new Msg(id, mbox, Msg.Type.Normal_Hardware_Error, ""));
                        normal_problem = false;
                    } else atmss.send(new Msg(id, mbox, Msg.Type.PollAck, id + " is up!"));
                    break;

                case Terminate:
                    stageClose();
                    quit = true;
                    break;

                case Reset:
                    stageHide();
                    normal_problem = false;
                    severe_problem = false;
                    restart = true;
                    quit = true;
                    break;

                default:
                    processMsg(msg);
            }
        }

        // declaring our departure
        appKickstarter.unregThread(this);
        log.info(id + ": terminating...");

        if (restart) {
            try {
                Thread.sleep(800);
                switch (id) {
                    case "CardReaderHandler":
                        CardReaderEmulator cardReaderEmulator = ((ATMSSEmulatorStarter) appKickstarter).getCardReaderEmulator();
                        new Thread(cardReaderEmulator).start();
                        cardReaderEmulator.reset();
                        cardReaderEmulator.stageShow();
                        break;

                    case "CashCollectorHandler":
                        CashCollectorEmulator cashCollectorEmulator = ((ATMSSEmulatorStarter) appKickstarter).getCashCollectorEmulator();
                        new Thread(cashCollectorEmulator).start();
                        cashCollectorEmulator.stageShow();
                        break;

                    case "BuzzerHandler":
                        BuzzerEmulator buzzerEmulator = ((ATMSSEmulatorStarter) appKickstarter).getBuzzerEmulator();
                        new Thread(buzzerEmulator).start();
                        buzzerEmulator.stageShow();
                        break;

                    case "PrinterHandler":
                        PrinterEmulator printerEmulator = ((ATMSSEmulatorStarter) appKickstarter).getPrinterEmulator();
                        new Thread(printerEmulator).start();
                        printerEmulator.stageShow();
                        break;

                    case "CashDispenserHandler":
                        CashDispenserEmulator cashDispenserEmulator = ((ATMSSEmulatorStarter) appKickstarter).getCashDispenserEmulator();
                        new Thread(cashDispenserEmulator).start();
                        cashDispenserEmulator.stageShow();
                        break;

                    case "KeypadHandler":
                        KeypadEmulator keypadEmulator = ((ATMSSEmulatorStarter) appKickstarter).getKeypadEmulator();
                        new Thread(keypadEmulator).start();
                        keypadEmulator.stageShow();
                        break;

                    case "TouchDisplayHandler":
                        TouchDisplayEmulator touchDisplayEmulator = ((ATMSSEmulatorStarter) appKickstarter).getTouchDisplayEmulator();
                        new Thread(touchDisplayEmulator).start();
                        touchDisplayEmulator.stageShow();
//                        mbox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "BlankScreen"));
                        break;

                    default:
                        log.info(id + "unknown hardware component to restart");
                }
                restart = false;
            } catch (Exception e) {
                System.out.println(id + ": restart failed");
                e.printStackTrace();
                Platform.exit();
            }
        }

    } // run


    //------------------------------------------------------------
    // processMsg

    /**
     * Log the message info if it received unknown source message
     */
    protected void processMsg(Msg msg) {
        log.warning(id + ": unknown message type: [" + msg + "]");
    } // processMsg


    //------------------------------------------------------------
    // stageClose

    /**
     * Log the info about closing stage
     */
    protected void stageClose() {
        log.info(id + ": closing the stage ");
    } // stageClose


    //------------------------------------------------------------
    // stageHide
    /**
     * Log the info about hiding closing
     */
    protected void stageHide() {
        log.info(id + ": hide the stage ");
    } // stageHide

    //------------------------------------------------------------
    // stageShow
    /**
     * Log the info about showing stage
     */
    protected void stageShow() {
        log.info(id + ": show the stage ");
    } // stageShow

}
