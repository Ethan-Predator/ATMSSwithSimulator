package ATMSS.ATMSS;

import ATMSS.BuzzerHandler.Emulator.BuzzerEmulator;
import ATMSS.ATMSSEmulatorStarter;
import ATMSS.CardReaderHandler.Emulator.CardReaderEmulator;
import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.*;
import AppKickstarter.timer.Timer;
import javafx.application.Platform;
import javafx.stage.Stage;

//======================================================================
// ATMSS
public class ATMSS extends AppThread {
    private int pollingTime;

    private MBox cardReaderMBox;
    private MBox keypadMBox;
    private MBox touchDisplayMBox;
    private MBox cashCollectorMBox;
    private MBox cashDispenserMBox;
    private MBox printerMBox;
    private MBox buzzerMBox;

    private String cred = "";
    private String cardNo = "";
    private String accNo = "";
    private String accType = "";
    private String pin = "";
    private String toAcc = "";
    private String toAccType = "";
    private String transactionType = "";
    private String transactionStatus = "";
    private String failureType = "";
    private String failureReason = "";
    private String amount = "";
    private Boolean pinLocker = false;
    private Boolean transferLocker = false;
    private Boolean withdrawLocker = false;
    private Boolean enquiryLocker = false;
    private Boolean depositLocker = false;
    private Boolean statementReqLocker = false;
    private Boolean chequeBookReqLocker = false;
    private Boolean chgPinReqLocker = false;
    private Boolean chgLanLocker = false;

    private int pinErrorCounter = 0;
    private int loginErrorCounter = 0;

    private String saving_acc = "";
    private String investment_acc = "";
    private String current_acc = "";
    private String visa_acc = "";
    private int transCountter = 1;
    private int changePinCounter = 1;

    private String newPin = "";

    private int touchdisplayTimerID = 0;
    private int totalBalance = 0;
    private int inventory_100 = 0;
    private int inventory_500 = 0;
    private int inventory_1000 = 0;

    //------------------------------------------------------------
    // ATMSS

    /**
     * Constructor for KeypadEmulator
     *
     * @param id             name of the application
     * @param appKickstarter Oject of app Kick Starter
     */
    public ATMSS(String id, AppKickstarter appKickstarter) throws Exception {
        super(id, appKickstarter);

        // get the property specified in the configuration file
        pollingTime = Integer.parseInt(appKickstarter.getProperty("ATMSS.PollingTime"));
        inventory_100 = Integer.parseInt(appKickstarter.getProperty("ATMSS.inventory_100"));
        inventory_500 = Integer.parseInt(appKickstarter.getProperty("ATMSS.inventory_500"));
        inventory_1000 = Integer.parseInt(appKickstarter.getProperty("ATMSS.inventory_1000"));
        totalBalance = inventory_100 * 100 + inventory_500 * 500 + inventory_1000 * 1000;

    } // ATMSS


    //------------------------------------------------------------
    // run

    /**
     * Run the ATMSS, initialize the hardware components, and handle receive message
     */
    public void run() {
        Timer.setTimer(id, mbox, pollingTime);
        log.info(id + ": starting...");

        cardReaderMBox = appKickstarter.getThread("CardReaderHandler").getMBox();
        keypadMBox = appKickstarter.getThread("KeypadHandler").getMBox();
        touchDisplayMBox = appKickstarter.getThread("TouchDisplayHandler").getMBox();
        cashCollectorMBox = appKickstarter.getThread("CashCollectorHandler").getMBox();
        cashDispenserMBox = appKickstarter.getThread("CashDispenserHandler").getMBox();
        printerMBox = appKickstarter.getThread("PrinterHandler").getMBox();
        buzzerMBox = appKickstarter.getThread("BuzzerHandler").getMBox();

        for (boolean quit = false; !quit; ) {
            // msg here is the msg received by ATMSS
            // this mbox is the mbox of ATMSS
            Msg msg = mbox.receive();

            log.fine(id + ": message received: [" + msg + "].");

            switch (msg.getType()) {
                case TD_MouseClicked:
                    log.info("MouseCLicked: " + msg.getDetails());
                    processMouseClicked(msg);
                    break;

                case Function_Change:
                    log.info("FunctionChanged: " + msg.getDetails());
                    String message = funcionChange(msg);
//                    if (!message.equals("Take Card") ) {
                    touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, message));
//                    }
                    break;

                case Acc_Select:
                    log.info("AccountSelected: " + msg.getDetails());
                    accSelect(msg);
                    if (transferLocker) {
                        if (transCountter == 1) {
                            transCountter++;
                            touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "SelectTransferAcc"));
                        } else {
                            touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "EnterAmt"));
                        }
                    }
                    // to be implemented
                    else if (depositLocker) {
                        touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "DisplayDep"));
                        cashCollectorMBox.send(new Msg(id, mbox, Msg.Type.CC_Open, "CollectorOpen"));
                        // ask collector emulator to set the put in button visible + textField editable
                    } else if (withdrawLocker) {
                        touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "EnterAmt"));
                    } else if (enquiryLocker) {
                        touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "AccEnquiry"));
                    } else if (statementReqLocker) {
                        touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "StmReq"));
                    } else if (chequeBookReqLocker) {
                        touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "ChqBookReq"));
                    } else if (chgPinReqLocker) {
                        touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "ChgPinReq"));
                    }
                    break;

                case KP_KeyPressed:
                    processKeyPressed(msg);
                    log.info("KeyPressed: " + msg.getDetails());
                    if (pinLocker) {
                        processPinInput(msg);
                    } else if (transferLocker) {
                        processMoneyTransfer(msg);
                    } else if (withdrawLocker) {
                        processCashWithdraw(msg);
                    } else if (chgPinReqLocker) {
                        processChgPinReq(msg);
                    }
                    break;

                case CR_CardInserted:
                    log.info("CardInserted: " + msg.getDetails());
                    cardNo = msg.getDetails();
                    pinLocker = true;
                    touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "EnterPin"));
                    break;

                case CR_CardRemoved:
                    log.info("CardRemoved: " + msg.getDetails());
                    // ask buzzer to stop ringing at the same time
                    buzzerMBox.send(new Msg(id, mbox, Msg.Type.BZ_Stop, "StopBuzzer"));
                    break;

                case CC_MoneyCollected:
                    buzzerMBox.send(new Msg(id, mbox, Msg.Type.BZ_Stop, "StopBuzzer"));
                    log.info("MoneyCollected: " + msg.getDetails());
                    processDeposit(msg);
                    break;

                case Acc_Enquiry:
                    log.info("AccountEnquiry: " + msg.getDetails());
                    processEnquiry(msg);
                    break;

                case TimesUp:
                    log.info("Poll: " + msg.getDetails());
                    Timer.setTimer(id, mbox, pollingTime);
                    cardReaderMBox.send(new Msg(id, mbox, Msg.Type.Poll, ""));
                    keypadMBox.send(new Msg(id, mbox, Msg.Type.Poll, ""));
                    touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.Poll, ""));
                    cashCollectorMBox.send(new Msg(id, mbox, Msg.Type.Poll, ""));
                    cashDispenserMBox.send(new Msg(id, mbox, Msg.Type.Poll, ""));
                    printerMBox.send(new Msg(id, mbox, Msg.Type.Poll, ""));
                    buzzerMBox.send(new Msg(id, mbox, Msg.Type.Poll, ""));
                    break;

                case PollAck:
                    log.info("PollAck: " + msg.getDetails());
                    break;

                case Terminate:
                    log.info(id + ": Times up");
                    quit = true;
                    break;

                case CD_Open:
                    log.info("CardDispenserOpened: " + msg.getDetails());
                    buzzerMBox.send(new Msg(id, mbox, Msg.Type.BZ_Ring, "RingBuzzer"));
//                    log.info("CashDispenserOpened: " + msg.getDetails());
                    break;

                case CD_Close:
                    log.info("CardDispenserClosed: " + msg.getDetails());
                    buzzerMBox.send(new Msg(id, mbox, Msg.Type.BZ_Stop, "RingBuzzer"));
//                    log.info("CashDispenserClosed: " + msg.getDetails());
                    break;

                case CC_Open:
                    log.info("CardCollectorOpened: " + msg.getDetails());
                    buzzerMBox.send(new Msg(id, mbox, Msg.Type.BZ_Ring, "RingBuzzer"));
//                    log.info("CashCollecterOpened: " + msg.getDetails());
                    break;

                case CC_Close:
                    log.info("CardCollectorClosed: " + msg.getDetails());
                    buzzerMBox.send(new Msg(id, mbox, Msg.Type.BZ_Stop, "RingBuzzer"));
                    touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "MainMenu"));
//                    log.info("CashCollecterClosed: " + msg.getDetails());
                    break;

                case TD_AccStatementRequest:
                    log.info("AccStatementRequest: " + msg.getDetails());
                    processStatementReq(msg);
                    break;

                case TD_ChequeBookRequest:
                    log.info("ChqBookRequest: " + msg.getDetails());
                    processChequeBookReq(msg);
                    break;

                case TD_ChangePinRequest:
                    log.info("ChgPinRequest: " + msg.getDetails());
                    processChgPinReq(msg);
                    break;

                case CD_MoneyTakenOut:
                    log.info("MoneyInCardCollectorTakenOut: " + msg.getDetails());
                    touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "Withdraw_success"));
                    break;

                case Withdraw_Confirm:
                    String successWithdraw = accNo + "/" + accType + "/" + Double.parseDouble(amount);
                    withdrawLocker = false;
                    touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_WithdrawConfirmation, successWithdraw));
                    break;

                case Deposit_Confirm:
                    String successDeposit = accNo + "/" + accType + "/" + Double.parseDouble(amount);
                    touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_DepositConfirmation, successDeposit));
                    break;

                case Transfer_Confirm:
                    String successTransfer = accNo + "/" + accType + "/" + toAcc + "/" + toAccType + "/" + Double.parseDouble(amount);
                    touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_TransferConfirmation, successTransfer));
                    break;

                case Failure_Msg:
                    String failureMsg = failureType + "/" + failureReason;
//                    System.out.println(failureMsg);
                    touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_failureDisplay, failureMsg));
                    break;

                case UpdataDisplayTimerID:
                    touchdisplayTimerID = Integer.parseInt(msg.getDetails());
                    break;

                case Keypad_Enter_TimeOut:
                    resetLocker();
                    if (msg.getDetails().equals("backToWelcomePage")) {
                        cardReaderMBox.send(new Msg(id, mbox, Msg.Type.CR_EjectCard, "EjectCard"));
                    }
                    break;

                case Severe_Hardware_Error:
                    log.severe(msg.getSender() + ": severe problem happened");
                    if (msg.getSender().equals("TouchDisplayHandler")) {
                        touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.Terminate, ""));

                    } else
                        touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.Severe_Hardware_Error, msg.getSender()));
                    break;

                case Normal_Hardware_Error:
                    log.warning(msg.getSender() + ": normal problem happened");
                    if (msg.getSender().equals("TouchDisplayHandler")) {
                        touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.Reset, ""));
                    } else
                        touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.Normal_Hardware_Error, msg.getSender()));
                    break;

                case Expected_Problem:
                    log.warning(msg.getSender() + ": Out of paper");
                    touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.Expected_Problem, ""));
                    break;

                case Reset_Component:
                    log.warning(msg.getSender() + ": resetting");
                    pocessResetComponent(msg);
                    break;

                case Shutdown_Component:
                    log.warning(msg.getSender() + ": shutting down");
                    pocessShutdownComponent(msg);
                    break;

                default:
                    log.warning(id + ": unknown message type: [" + msg + "]");
            }
        }

        // declaring our departure
        appKickstarter.unregThread(this);
        log.info(id + ": terminating...");
    } // run


    //------------------------------------------------------------
    // processKeyPressed

    /**
     * Process the message from keypad emulator
     *
     * @param msg the message sent by keypad emulator
     */
    private void processKeyPressed(Msg msg) {
        if (msg.getDetails().compareToIgnoreCase("Cancel") == 0) {
//            if (!cred.equals("")) {
//                resetLocker();
            processLogout();
//                touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "BlankScreen"));


//            }
            // ask cardReader to eject card, and starts the timer
            // wait for a second, then ask buzzer to start ringing (timer)
        }
    } // processKeyPressed


    //------------------------------------------------------------
    // processMouseClicked

    /**
     * Process the message
     *
     * @param msg msg received from touch display emulator
     */
    private void processMouseClicked(Msg msg) {
        // *** process mouse click here!!! ***
    } // processMouseClicked


    //------------------------------------------------------------
    // processPinInput

    /**
     * Process the message when entering Pin and logging into the ATMSS
     *
     * @param msg the message sent from keypad emulator
     */
    private void processPinInput(Msg msg) {
        if (msg.getDetails().equals("Enter")) {
            try {
                cred = BAMSAdapter.login(cardNo, pin);
                log.fine("Login result: " + cred);

                if (cred.equals("ERROR")) {
                    log.info("Card(" + cardNo + ") Login Failed");
                    loginErrorCounter++;
                    pin = "";
                    if (loginErrorCounter == 3) {
                        loginErrorCounter = 0;
                        processLogout();
                    } else if (loginErrorCounter < 3) {
                        touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "tryLoginAgain"));
                    }
                    Timer.cancelTimer("TouchDisplayHandler", touchDisplayMBox, touchdisplayTimerID);
                } else {
                    String accounts =BAMSAdapter.getAcc(cardNo, cred);
                    log.fine("Get Account result"+accounts);
                    String[] accs = accounts.split("/");
                    visa_acc = accs[0];
                    current_acc = accs[1];
                    investment_acc = accs[2];
                    saving_acc = accs[3];
                    pinLocker = false;
                    pin = "";
                    Timer.cancelTimer("TouchDisplayHandler", touchDisplayMBox, touchdisplayTimerID);

                    log.info("Card(" + cardNo + ") Login Success");
                    touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "MainMenu"));
                }

            } catch (Exception e) {
                System.out.println("BAMSAdapter: Exception caught: " + e.getMessage());
                e.printStackTrace();
            }
        } else if (msg.getDetails().equals("Erase")) {
            touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.Erase_Pin, ""));
            pin = "";
        } else {
            touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.Pin_Msg, msg.getDetails()));
            pin += msg.getDetails();
        }
    } // processPinInput


    //------------------------------------------------------------
    // processMoneyTransfer

    /**
     * Process the message involved in the money transfer function
     *
     * @param msg message sent from touch display emulator
     */
    private void processMoneyTransfer(Msg msg) {
        if (accNo.equals("")) return;
        if (msg.getDetails().equals("Enter") && transCountter == 2) {
            try {
                double transMoney = BAMSAdapter.transfer(cardNo, cred, accNo, toAcc, amount);
                log.fine("Money Transfer Result: " + transMoney);

                if (transMoney == -1) {
                    log.info("Log out due to unknown credential");
                    processLogout();
                } else if (transMoney == -2) {
                    log.info("Money Transfer Fail: Not enough balance");
                    failureType = "Transfer";
                    failureReason = "Not enough balance";
                    transactionStatus = "Rejected";
                    touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "fail"));
                } else {
                    transactionStatus = "Accepted";
                    Timer.cancelTimer("TouchDisplayHandler", touchDisplayMBox, touchdisplayTimerID);

                    log.info("Money Transfer Success: " + amount + "HKD from " + accNo + " to " + toAcc);
                    touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "Transfer_success"));

                    transCountter = 1;
                    transferLocker = false;
                }
//                System.out.println(transMoney);
            } catch (Exception e) {
                System.out.println("BAMSAdapter: Exception caught: " + e.getMessage());
                e.printStackTrace();
            }

//      may redirect to other pages
        } else if (msg.getDetails().equals("Erase")) {
            touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.Erase_Field, ""));
            amount = "";
        } else {
            touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.Field_Msg, msg.getDetails()));
            amount += msg.getDetails();
        }
    } // processMoneyTransfer


    //------------------------------------------------------------
    // processEnquiry

    /**
     * Process the account enquiry function
     *
     * @param msg message received from the touch display
     */
    private void processEnquiry(Msg msg) {
        try {
            double balance = BAMSAdapter.enquiry(cardNo, accNo, cred);
            log.fine("Account Enquiry Result: " + balance);

            if (balance < 0) {
                log.info("Log out due to unknown credential");
                processLogout();
            } else {
                String accDetails = accType + '/' + accNo + '/' + balance;
                transactionStatus = "Accepted";

                log.info("Account(" + accNo + ") Enquiry Success");
                touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_DisplayAccBalance, accDetails));
            }


        } catch (Exception e) {
            System.out.println("BAMSAdapter: Exception caught: " + e.getMessage());
            e.printStackTrace();
        }
        enquiryLocker = false;
    } // processEnquiry


    //------------------------------------------------------------
    // processDeposit

    /**
     * Process the deposit function
     *
     * @param msg message received from cash collector emulator
     */
    private void processDeposit(Msg msg) {
        try {
            amount = msg.getDetails();
            double depAmount = BAMSAdapter.deposit(cardNo, accNo, cred, amount);
            log.fine("Cash Deposit Result: " + depAmount);

            if (depAmount < 0) {
                log.info("Log out due to unknown credential");
                processLogout();
            } else {

                String depDetails = "deposit: " + accType + '/' + accNo + '/' + depAmount;
                transactionStatus = "Accepted";

                log.info("Cash Deposit Success: " + amount + "HKD");
                touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "Deposit_success"));
            }
        } catch (Exception e) {
            System.out.println("BAMSAdapter: Exception caught: " + e.getMessage());
            e.printStackTrace();
        }
        depositLocker = false;
    } // processDeposit


    //------------------------------------------------------------
    // processCashWithdraw

    /**
     * Process the cash withdrawal function
     *
     * @param msg message received from keypad emulator
     */
    private void processCashWithdraw(Msg msg) {
        if (accNo.equals("")) return;
        if (msg.getDetails().equals("Enter")) {

            double acc_balance = 0;

            try {
                acc_balance = BAMSAdapter.enquiry(cardNo, accNo, cred);
                log.fine("Account Enquiry Result: " + acc_balance);

            } catch (Exception e) {
                System.out.println("BAMSAdapter: Exception caught: " + e.getMessage());
                e.printStackTrace();
            }
            if (acc_balance == -1) {
                log.info("Log out due to unknown credential");
                processLogout();
                return;
            }


            if (!amount.equals("")&&acc_balance > Integer.parseInt(amount)) {
                String ATMInventory = checkATMInventory(amount);
                if (ATMInventory.startsWith("/")) {

                    try {
                        amount = "" + BAMSAdapter.withdraw(cardNo, accNo, cred, amount);
                        log.fine("Cash Withdrawal Result: " + amount);

                        Timer.cancelTimer("TouchDisplayHandler", touchDisplayMBox, touchdisplayTimerID);

                        if (Integer.parseInt(amount) > 0) {

                            log.info("Cash Withdraw Success: " + amount + "HKD");
                            transactionStatus = "Accepted";
                            touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "DisplayWithdrawal"));
//                            touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "Withdraw_success"));
                        }

                    } catch (Exception e) {
                        log.warning("BAMSAdapter: Exception caught:" + e.getMessage());
                        e.printStackTrace();
                    }

                    cashDispenserMBox.send(new Msg(id, mbox, Msg.Type.CD_Open, ATMInventory));
                    buzzerMBox.send(new Msg(id, mbox, Msg.Type.BZ_Ring, "RingBuzzer"));

                } else if (ATMInventory.startsWith("We")) {
                    log.info("Cash Withdraw Fail: Invalid input amount");
                    failureType = "Withdraw";
                    failureReason = "Invalid input amount";
                    transactionStatus = "Rejected";
                    touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "fail"));
                } else {
                    log.info("Cash Withdraw Fail: Cash in ATM is not enough");
                    failureType = "Withdraw";
                    failureReason = "Cash in ATM is not enough";
                    transactionStatus = "Rejected";
                    touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "fail"));

                }
            } else {
                log.info("Cash Withdraw Fail: User's balance is not enough");
                failureType = "Withdraw";
                failureReason = "User's balance is not enough";
                transactionStatus = "Rejected";
                touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "fail"));
            }


        } else if (msg.getDetails().equals("Erase")) {
            touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.Erase_Field, ""));
            amount = "";
        } else {
            touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.Field_Msg, msg.getDetails()));
            amount += msg.getDetails();
        }
    }// processCashWithdraw


    //------------------------------------------------------------
    // processStatementReq

    /**
     * Process the statement request function
     *
     * @param msg message received from touch display emulator
     */
    private void processStatementReq(Msg msg) {
        try {
            String result = BAMSAdapter.accStmtReq(cardNo, accNo, cred);
            log.fine("Account statement request Result: " + result);

            if (result.equals("-1")) {
                log.info("Log out due to unknown credential");
                processLogout();
            } else {
                transactionStatus = "Accepted";
                log.info("Request Statement Sucess");
                touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "StmReq_success"));
            }

        } catch (Exception e) {
            System.out.println("BAMSAdapter: Exception caught: " + e.getMessage());
            e.printStackTrace();
        }
        statementReqLocker = false;
    } // processStatementReq


    //------------------------------------------------------------
    // processChequeBookReq

    /**
     * Process the cheque book request function
     *
     * @param msg message received from touch display emulator
     */
    private void processChequeBookReq(Msg msg) {
        try {
            String result = BAMSAdapter.chqBookReq(cardNo, accNo, cred);
            log.fine("Request for Cheque Book Result: " + result);

            if (result.equals("-1")) {
                log.info("Log out due to unknown credential");
                processLogout();
            } else {
                transactionStatus = "Accepted";

                log.info("Request Cheque Book Success");
                touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "ChqBookReq_success"));
            }

        } catch (Exception e) {
            System.out.println("BAMSAdapter: Exception caught: " + e.getMessage());
            e.printStackTrace();
        }
        chequeBookReqLocker = false;
    } // processChequeBookReq
    //------------------------------------------------------------


    //------------------------------------------------------------
    // processChgPinReq

    /**
     * Process the function of changing Pin
     *
     * @param msg message received from keypad emulator
     */
    private void processChgPinReq(Msg msg) {

        if (msg.getDetails().equals("Enter") && changePinCounter == 1) {
            changePinCounter = 2;
            touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "EnterNewPin"));

        } else if (msg.getDetails().equals("Enter") && changePinCounter == 2) {
            String result = "";
            try {
                result = BAMSAdapter.chgPinReq(cardNo, pin, newPin, cred);
                log.fine("Change pin request result: " + result);


            } catch (Exception e) {
                System.out.println("BAMSAdapter: Exception caught: " + e.getMessage());
                e.printStackTrace();
            }
            if (result.equals("succ")) {
                chgPinReqLocker = false;
                pin = "";
                newPin = "";
                changePinCounter = 1;
                transactionStatus = "Accepted";
                log.info("Change Pin Success");
                touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "ChgPinReq_success"));
            } else if (result.equals("-1")) {
                log.info("Log out due to unknown credential");
                processLogout();
            } else if (result.equals("Fail to change Pin")) {
                log.info("Change Pin Fail: Invalid old pin");
                pinErrorCounter++;
                pin = "";
                newPin = "";
                changePinCounter = 1;
                if (pinErrorCounter == 3) {
                    pinErrorCounter = 0;
                    processLogout();
                } else if (pinErrorCounter < 3) {
                    touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "tryEnterAgain"));
                }
            }
        } else if (msg.getDetails().equals("Erase")) {
            touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.Erase_Pin, ""));
            if (changePinCounter == 1) pin = "";
            else newPin = "";
        } else {
            touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.Pin_Msg, msg.getDetails()));
            if (changePinCounter == 1) pin += msg.getDetails();
            else newPin += msg.getDetails();
        }
    } // processChgPinReq
    //------------------------------------------------------------


    //------------------------------------------------------------
    // pocessResetComponent

    /**
     * Process the reset function
     *
     * @param msg message received each hardware components
     */
    private void pocessResetComponent(Msg msg) {
        switch (msg.getDetails()) {
            case "BuzzerHandler":
                if (!cardNo.equals("")) processLogout();
                buzzerMBox.send(new Msg(id, mbox, Msg.Type.Reset, ""));
                break;
            case "CardReaderHandler":
                if (!cardNo.equals("")) processLogout();
                cardReaderMBox.send(new Msg(id, mbox, Msg.Type.Reset, ""));
                break;
            case "CashCollectorHandler":
                if (!cardNo.equals("")) processLogout();
                cashCollectorMBox.send(new Msg(id, mbox, Msg.Type.Reset, ""));
                break;
            case "CashDispenserHandler":
                if (!cardNo.equals("")) processLogout();
                cashDispenserMBox.send(new Msg(id, mbox, Msg.Type.Reset, ""));
                break;
            case "KeypadHandler":
                if (!cardNo.equals("")) processLogout();
                keypadMBox.send(new Msg(id, mbox, Msg.Type.Reset, ""));
                break;
            case "PrinterHandler":
                if (!cardNo.equals("")) processLogout();
                printerMBox.send(new Msg(id, mbox, Msg.Type.Reset, ""));
                break;
//            case "TouchDisplayHandler":
//                touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.Reset, ""));
//                break;
            default:
                log.info("Unknown component: " + msg.getDetails());

        }
    }//pocessRestartComponent


    //------------------------------------------------------------
    // pocessShutdownComponent

    /**
     * Process the shutdown function
     *
     * @param msg message received from each hardware components
     */
    public void pocessShutdownComponent(Msg msg) {
        switch (msg.getDetails()) {
            case "BuzzerHandler":
                if (!cardNo.equals("")) processLogout();
                buzzerMBox.send(new Msg(id, mbox, Msg.Type.Terminate, ""));
                break;
            case "CardReaderHandler":
                if (!cardNo.equals("")) processLogout();
                cardReaderMBox.send(new Msg(id, mbox, Msg.Type.Terminate, ""));
                break;
            case "CashCollectorHandler":
                if (!cardNo.equals("")) processLogout();
                cashCollectorMBox.send(new Msg(id, mbox, Msg.Type.Terminate, ""));
                break;
            case "CashDispenserHandler":
                if (!cardNo.equals("")) processLogout();
                cashDispenserMBox.send(new Msg(id, mbox, Msg.Type.Terminate, ""));
                break;
            case "KeypadHandler":
                if (!cardNo.equals("")) processLogout();
                keypadMBox.send(new Msg(id, mbox, Msg.Type.Terminate, ""));
                break;
            case "PrinterHandler":
                if (!cardNo.equals("")) processLogout();
                printerMBox.send(new Msg(id, mbox, Msg.Type.Terminate, ""));
                break;
//            case "TouchDisplayHandler":
//                touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.Terminate, ""));
//                break;
            default:
                log.info("Unknown component: " + msg.getDetails());

        }
    } // pocessShutdownComponent


    //------------------------------------------------------------
    // processLogout

    /**
     * Process the log out function
     */
    private void processLogout() {

        resetLocker();

        cardReaderMBox.send(new Msg(id, mbox, Msg.Type.CR_EjectCard, "EjectCard"));
        // ask cardReader to eject card, and starts the timer

        try {
            String result = BAMSAdapter.logout(cardNo, cred);
            log.fine("Logout Result: " + result);

            touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "BlankScreen"));
        } catch (Exception e) {
            System.out.println("BAMSAdapter: Exception caught: " + e.getMessage());
            e.printStackTrace();
        }
        cred = "";
        cardNo = "";
        pin = "";
        // wait for a second, then ask buzzer to start ringing (timer)
        buzzerMBox.send(new Msg(id, mbox, Msg.Type.BZ_Ring, "RingBuzzer"));

    }//processLogout


    //------------------------------------------------------------
    // funcionChange

    /**
     * Process the function changing according to user different input
     *
     * @param msg message received from touch display emulator
     */
    private String funcionChange(Msg msg) {
        switch (msg.getDetails()) {
            case "Money Transfer":
                transferLocker = true;
                transactionType = "Money Transfer";
                break;

            case "Cash Deposit":
                depositLocker = true;
                transactionType = "Cash Deposit";
                break;

            case "Account Enquiry":
                enquiryLocker = true;
                transactionType = "Account Enquiry";
                break;

            case "Cash Withdrawal":
                withdrawLocker = true;
                transactionType = "Cash Withdrawal";
                break;

            case "Request Statement":
                statementReqLocker = true;
                transactionType = "Request Statement";
                break;

            case "Request Cheque Book":
                chequeBookReqLocker = true;
                transactionType = "Request Cheque Book";
                break;

            case "Change Pin":
                chgPinReqLocker = true;
                transactionType = "Change Pin";
                return "EnterOldPin";

            case "Go Back":
                buzzerMBox.send(new Msg(id, mbox, Msg.Type.BZ_Stop, "BZ_Stop"));

//                touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "MainMenu"));
                resetLocker();
                return "MainMenu";

            case "Print Advice & Go Back":
                String printMsg = accType + " " + '/' + accNo + '/' + transactionType + '/' + amount + " " + '/' + transactionStatus;
                printerMBox.send(new Msg(id, mbox, Msg.Type.PT_Print, printMsg));
//                touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "MainMenu"));
                transferLocker = withdrawLocker = enquiryLocker = depositLocker = false;
                resetLocker();
                return "MainMenu";

            case "Print Advice & Take Card":
                // logout & eject card & print
                String printAdvice = accType + " " + '/' + accNo + '/' + transactionType + '/' + amount + " " + '/' + transactionStatus;
                printerMBox.send(new Msg(id, mbox, Msg.Type.PT_Print, printAdvice));
//                printerMBox.send(new Msg(id, mbox, Msg.Type.PT_Print, "print msg"));
//                resetLocker();
                processLogout();
                return "BlankScreen";

            case "Take Card":
//                resetLocker();
                processLogout();
                return "BlankScreen";

            case "Try Again":
                return "EnterOldPin";

            case "Enter Again":
                return "EnterPin";

            default:
                log.warning(id + ": unknown function: [" + msg.getDetails() + "]");
                break;
        }

        return "SelectAcc";
    } // funcionChange


    //------------------------------------------------------------
    // accSelect

    /**
     * Select the account depending on the received message and saved in the ATM
     *
     * @param msg message received from touch display emulator indicating the selected account
     */

    private void accSelect(Msg msg) {
        switch (msg.getDetails()) {
            case "Saving Account":
                if (transCountter == 2) {
                    toAcc = saving_acc;
                    toAccType = "(saving)";
                } else {
                    accNo = saving_acc;
                    accType = "(saving)";
                }
                break;

            case "Investment Account":
                if (transCountter == 2) {
                    toAcc = investment_acc;
                    toAccType = "(investment)";
                } else {
                    accNo = investment_acc;
                    accType = "(investment)";
                }
                break;

            case "Current Account":
                if (transCountter == 2) {
                    toAcc = investment_acc;
                    toAccType = "(current)";
                } else {
                    accNo = current_acc;
                    accType = "(current)";
                }
                break;

            case "Visa Account":
                if (transCountter == 2) {
                    toAcc = visa_acc;
                    toAccType = "(visa)";
                } else {
                    accNo = visa_acc;
                    accType = "(visa)";
                }
                break;

            default:
                log.warning(id + ": unknown button: [" + msg.getDetails() + "]");
                break;
        }
    } // accSelect


    //------------------------------------------------------------
    // checkATMInventory

    /**
     * Check the inventory of different type of cash in the ATM
     *
     * @param amount the amount of money user wants to withdraw
     */

    private String checkATMInventory(String amount) {
        int amount_num = Integer.parseInt(amount);

        if (amount_num % 100 != 0) {
            return "We only provide $100 $500 $1000 banknotes";
        }

        if (amount_num >= totalBalance) {
            return "Total inventory not enough.";
        } else {
            int need_100 = 0;
            int need_500 = 0;
            int need_1000 = 0;

            int check_1000 = amount_num / 1000;
            if (check_1000 > 0 && check_1000 <= inventory_1000) {
                need_1000 = check_1000;
                amount_num = amount_num - need_1000 * 1000;
            }
            int check_500 = amount_num / 500;
            if (check_500 > 0 && check_500 <= inventory_500) {
                need_500 = check_500;
                amount_num = amount_num - need_500 * 500;
            }
            int check_100 = amount_num / 100;
            if (check_100 > 0 && check_100 <= inventory_100) {
                need_100 = check_100;
                amount_num = amount_num - need_100 * 100;
            }
            if (amount_num == 0) {
                inventory_100 -= need_100;
                inventory_500 -= need_500;
                inventory_1000 -= need_1000;
                return "/" + need_100 + "/" + need_500 + "/" + need_1000;
            } else {
                return "Cash inventory not enough";
            }
        }
    }// checkATMInventory


    //------------------------------------------------------------
    // resetLocker

    /**
     * Reset all the function locker and global variables
     */

    public void resetLocker() {
        pinLocker = false;
        transferLocker = false;
        withdrawLocker = false;
        enquiryLocker = false;
        depositLocker = false;
        statementReqLocker = false;
        chequeBookReqLocker = false;
        chgPinReqLocker = false;
        chgLanLocker = false;
        transCountter = 1;
        changePinCounter = 1;
        accNo = "";
        accType = "";
        toAcc = "";
        toAccType = "";
        transactionType = "";
        transactionStatus = "";
        failureType = "";
        failureReason = "";
        amount = "";
        touchdisplayTimerID = 0;
    }// resetLocker


} // ATMSS
