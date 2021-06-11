package ATMSS;

import ATMSS.BuzzerHandler.BuzzerHandler;
import ATMSS.BuzzerHandler.Emulator.BuzzerEmulator;
import ATMSS.CashCollectorHandler.CashCollectorHandler;
import ATMSS.CashCollectorHandler.Emulator.CashCollectorEmulator;
import ATMSS.CashDispenserHandler.CashDispenserHandler;
import ATMSS.CashDispenserHandler.Emulator.CashDispenserEmulator;
import ATMSS.PrinterHandler.Emulator.PrinterEmulator;
import ATMSS.PrinterHandler.PrinterHandler;
import AppKickstarter.timer.Timer;
import ATMSS.ATMSS.ATMSS;
import ATMSS.CardReaderHandler.Emulator.CardReaderEmulator;
import ATMSS.KeypadHandler.KeypadHandler;
import ATMSS.TouchDisplayHandler.Emulator.TouchDisplayEmulator;
import ATMSS.CardReaderHandler.CardReaderHandler;
import ATMSS.KeypadHandler.Emulator.KeypadEmulator;
import ATMSS.TouchDisplayHandler.TouchDisplayHandler;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

//======================================================================
// ATMSSEmulatorStarter
public class ATMSSEmulatorStarter extends ATMSSStarter {
    //------------------------------------------------------------
    // main
    public static void main(String[] args) {
        // call the startApp() of its superclass: ATMSSStarter
        new ATMSSEmulatorStarter().startApp();
    } // main


    //------------------------------------------------------------
    // startHandlers
    @Override
    protected void startHandlers() {
        // set the attribute 'atmssEmulatorStarter' of Emulators class to 'this' ATMSSEmulatorStarter object
        // all objects under this class would have this attribute
        Emulators.atmssEmulatorStarter = this;

        new Emulators().start();
    } // startHandlers


    //------------------------------------------------------------
    // Emulators
    public static class Emulators extends Application {
        private static ATMSSEmulatorStarter atmssEmulatorStarter;

        //----------------------------------------
        // start
        public void start() {
            launch();
        } // start

        //----------------------------------------
        // start
        public void start(Stage primaryStage) {
            Timer timer = null;
            ATMSS atmss = null;
            CardReaderEmulator cardReaderEmulator = null;
            KeypadEmulator keypadEmulator = null;
            TouchDisplayEmulator touchDisplayEmulator = null;
            CashCollectorEmulator cashCollectorEmulator = null;
            CashDispenserEmulator cashDispenserEmulator = null;
            PrinterEmulator printerEmulator = null;
            BuzzerEmulator buzzerEmulator = null;


            // create emulators
            try {
                timer = new Timer("timer", atmssEmulatorStarter);
                atmss = new ATMSS("ATMSS", atmssEmulatorStarter);

                cardReaderEmulator = new CardReaderEmulator("CardReaderHandler", atmssEmulatorStarter);
                keypadEmulator = new KeypadEmulator("KeypadHandler", atmssEmulatorStarter);
                cashCollectorEmulator = new CashCollectorEmulator("CashCollectorHandler", atmssEmulatorStarter);
                cashDispenserEmulator = new CashDispenserEmulator("CashDispenserHandler", atmssEmulatorStarter);
                printerEmulator = new PrinterEmulator("PrinterHandler", atmssEmulatorStarter);
                touchDisplayEmulator = new TouchDisplayEmulator("TouchDisplayHandler", atmssEmulatorStarter);
                buzzerEmulator = new BuzzerEmulator("BuzzerHandler", atmssEmulatorStarter);

                // start emulator GUIs
                touchDisplayEmulator.start();
                cardReaderEmulator.start();
                keypadEmulator.start();
                cashCollectorEmulator.start();
                cashDispenserEmulator.start();
                printerEmulator.start();
                buzzerEmulator.start();

            } catch (Exception e) {
                System.out.println("Emulators: start failed");
                e.printStackTrace();
                Platform.exit();
            }
            atmssEmulatorStarter.setTimer(timer);
            atmssEmulatorStarter.setATMSS(atmss);

            atmssEmulatorStarter.setCardReaderHandler(cardReaderEmulator);
            atmssEmulatorStarter.setKeypadHandler(keypadEmulator);
            atmssEmulatorStarter.setTouchDisplayHandler(touchDisplayEmulator);
            atmssEmulatorStarter.setCashCollectorHandler(cashCollectorEmulator);
            atmssEmulatorStarter.setCashDispenserHandler(cashDispenserEmulator);
            atmssEmulatorStarter.setPrinterHandler(printerEmulator);
            atmssEmulatorStarter.setBuzzerHandler(buzzerEmulator);

            // start threads
            new Thread(timer).start();
            new Thread(atmss).start();

            new Thread(cardReaderEmulator).start();
            new Thread(keypadEmulator).start();
            new Thread(touchDisplayEmulator).start();
            new Thread(cashCollectorEmulator).start();
            new Thread(cashDispenserEmulator).start();
            new Thread(printerEmulator).start();
            new Thread(buzzerEmulator).start();
        } // start
    } // Emulators


    //------------------------------------------------------------
    //  setters
    private void setTimer(Timer timer) {
        this.timer = timer;
    }

    private void setATMSS(ATMSS atmss) {
        this.atmss = atmss;
    }

    private void setCardReaderHandler(CardReaderHandler cardReaderHandler) {
        this.cardReaderHandler = cardReaderHandler;
    }

    private void setKeypadHandler(KeypadHandler keypadHandler) {
        this.keypadHandler = keypadHandler;
    }

    private void setTouchDisplayHandler(TouchDisplayHandler touchDisplayHandler) {
        this.touchDisplayHandler = touchDisplayHandler;
    }

    private void setCashCollectorHandler(CashCollectorHandler cashCollectorHandler) {
        this.cashCollectorHandler = cashCollectorHandler;
    }

    private void setCashDispenserHandler(CashDispenserHandler cashDispenserHandler) {
        this.cashDispenserHandler = cashDispenserHandler;
    }

    private void setPrinterHandler(PrinterHandler printerHandler) {
        this.printerHandler = printerHandler;
    }

    private void setBuzzerHandler(BuzzerHandler buzzerHandler) {
        this.buzzerHandler = buzzerHandler;
    }

    public CardReaderEmulator getCardReaderEmulator(){
        return (CardReaderEmulator)this.cardReaderHandler;
    }

    public KeypadEmulator getKeypadEmulator(){
        return (KeypadEmulator)this.keypadHandler;
    }

    public TouchDisplayEmulator getTouchDisplayEmulator(){
        return (TouchDisplayEmulator)this.touchDisplayHandler;
    }

    public CashCollectorEmulator getCashCollectorEmulator(){
        return (CashCollectorEmulator) this.cashCollectorHandler;
    }

    public CashDispenserEmulator getCashDispenserEmulator(){
        return (CashDispenserEmulator) this.cashDispenserHandler;
    }

    public PrinterEmulator getPrinterEmulator(){
        return (PrinterEmulator) this.printerHandler;
    }

    public BuzzerEmulator getBuzzerEmulator(){
        return (BuzzerEmulator) this.buzzerHandler;
    }







} // ATMSSEmulatorStarter
