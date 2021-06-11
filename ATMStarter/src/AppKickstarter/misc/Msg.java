package AppKickstarter.misc;


//======================================================================
// Msg
public class Msg {
    private String sender;
    private MBox senderMBox;
    private Type type;
    private String details;

    //------------------------------------------------------------
    // Msg

    /**
     * Constructor for a msg.
     *
     * @param sender     id of the msg sender (String)
     * @param senderMBox mbox of the msg sender
     * @param type       message type
     * @param details    details of the msg (free format String)
     */
    public Msg(String sender, MBox senderMBox, Type type, String details) {
        this.sender = sender;
        this.senderMBox = senderMBox;
        this.type = type;
        this.details = details;
    } // Msg


    //------------------------------------------------------------
    // getSender

    /**
     * Returns the id of the msg sender
     *
     * @return the id of the msg sender
     */
    public String getSender() {
        return sender;
    }


    //------------------------------------------------------------
    // getSenderMBox

    /**
     * Returns the mbox of the msg sender
     *
     * @return the mbox of the msg sender
     */
    public MBox getSenderMBox() {
        return senderMBox;
    }


    //------------------------------------------------------------
    // getType

    /**
     * Returns the message type
     *
     * @return the message type
     */
    public Type getType() {
        return type;
    }


    //------------------------------------------------------------
    // getDetails

    /**
     * Returns the details of the msg
     *
     * @return the details of the msg
     */
    public String getDetails() {
        return details;
    }


    //------------------------------------------------------------
    // toString

    /**
     * Returns the msg as a formatted String
     *
     * @return the msg as a formatted String
     */
    public String toString() {
        return sender + " (" + type + ") -- " + details;
    } // toString


    //------------------------------------------------------------
    // Msg Types

    /**
     * Message Types used in Msg.
     *
     * @see Msg
     */
    public enum Type {
        /**
         * Terminate the running thread
         */
        Terminate,
        /**
         * Generic error msg
         */
        Error,
        /**
         * Set a timer
         */
        SetTimer,
        /**
         * Set a timer
         */
        CancelTimer,
        /**
         * Timer clock ticks
         */
        Tick,
        /**
         * Time's up for the timer
         */
        TimesUp,
        /**
         * Health poll
         */
        Poll,
        /**
         * Health poll acknowledgement
         */
        PollAck,
        /**
         * Update Display
         */
        TD_UpdateDisplay,
        /**
         * Mouse Clicked
         */
        TD_MouseClicked,
        /**
         * Button Pressed
         */
        TD_ButtonPressed,
        /**
         * Card inserted
         */
        CR_CardInserted,
        /**
         * Card removed
         */
        CR_CardRemoved,
        /**
         * Eject card
         */
        CR_EjectCard,
        /**
         * Key pressed
         */
        KP_KeyPressed,
        /**
         * Send Pin Message
         */
        Pin_Msg,
        /**
         * Erase Pin Message
         */
        Erase_Pin,
        /**
         * Printer print
         */
        PT_Print,
        /**
         * Ring Buzzer
         */
        BZ_Ring,
        /**
         * Stop Buzzer
         */
        BZ_Stop,
        /**
         * Open Cash Collection
         */
        CC_Open,
        /**
         * Close Cash Collection
         */
        CC_Close,
        /**
         * Open Cash Deposit
         */
        CD_Open,
        /**
         * Close Cash Deposit
         */
        CD_Close,
        /**
         * Send Account Number Message
         */
        Field_Msg,
        /**
         * Erase TextField
         */
        Erase_Field,
        /**
         * Change Function
         */
        Function_Change,
        /**
         * Select an Account
         */
        Acc_Select,
        /**
         * Account Enquiry
         */
        Acc_Enquiry,
        /**
         * Display Account Balance
         */
        TD_DisplayAccBalance,
        /**
         * Ask ATMSS to display withdraw confirm
         */
        Withdraw_Confirm,
        /**
         * Display Withdraw Confirmation
         */
        TD_WithdrawConfirmation,
        /**
         * Ask ATMSS to display transfer confirm
         */
        Transfer_Confirm,
        /**
         * Display Transfer Confirmation
         */
        TD_TransferConfirmation,
        /**
         * Collected Money
         */
        CC_MoneyCollected,
        /**
         * Money taken out by customer
         * /
         */
        CD_MoneyTakenOut,
        /**
         * Ask ATMSS to display deposit confirm
         */
        Deposit_Confirm,
        /**
         * Display Transfer Confirmation
         */
        TD_DepositConfirmation,
        /**
         * update touch displau Timer ID in atmss
         */
        UpdataDisplayTimerID,
        /**
         * TD send account statement request
         */
        TD_AccStatementRequest,
        /**
         * TD send cheque book request
         */
        TD_ChequeBookRequest,
        /**
         * TD send change PIN request
         */
        TD_ChangePinRequest,
        /**
         * atmss send failure msg to TD
         */
        Failure_Msg,
        /**
         * Ask TD to display failure msg
         */
        TD_failureDisplay,
        /**
         * timeout when entering from keypad
         */
        Keypad_Enter_TimeOut,
        /**
         * handle Severe Hardware Error Msg
         */
        Severe_Hardware_Error,
        /**
         * handle normal Hardware Error Msg
         */
        Normal_Hardware_Error,
        /**
         * handle restart other components
         */
        Reset_Component,
        /**
         * reset/restart a component
         */
        Reset,
        /**
         * handle shutdown other components
         */
        Shutdown_Component,
        /**
         * handle the problem that can be expected like printer out of paper
         */
        Expected_Problem,
    } // Type
} // Msg
