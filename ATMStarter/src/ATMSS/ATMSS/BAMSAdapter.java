package ATMSS.ATMSS;

import ATMSS.BAMSHandler.BAMSHandler;
import ATMSS.BAMSHandler.BAMSInvalidReplyException;

import java.io.IOException;
import java.util.Calendar;
import java.util.logging.*;

//======================================================================
// TestBASMHandler
public class BAMSAdapter {
    static String urlPrefix = "http://cslinux0.comp.hkbu.edu.hk/comp4107_20-21_grp02/Request_handler.php";
    static BAMSHandler bams = new BAMSHandler(urlPrefix, initLogger());    // with logger

    //------------------------------------------------------------
    // login

    /**
     * Send login request to the MySQL database
     *
     * @param cardNo card number
     * @param pin    card pin
     */

    static String login(String cardNo, String pin) throws BAMSInvalidReplyException, IOException {
        String cred = bams.login(cardNo, pin);
        return cred;
    } // login


    //------------------------------------------------------------
    // getAcc

    /**
     * Send get account request to the MySQL database
     *
     * @param cardNo card number
     * @param cred   credential
     */
    static String getAcc(String cardNo, String cred) throws BAMSInvalidReplyException, IOException {
        String accounts = bams.getAccounts(cardNo, cred);
        return accounts;
    } // getAcc


    //------------------------------------------------------------
    // withdraw

    /**
     * Send withdraw cash request to the MySQL database
     *
     * @param cardNo card number
     * @param accNo  account number
     * @param cred   credential
     * @param amount withdraw amount
     */
    static int withdraw(String cardNo, String accNo, String cred, String amount) throws BAMSInvalidReplyException, IOException {
        int outAmount = bams.withdraw(cardNo, accNo, cred, amount);
        return outAmount;
    } // withdraw


    //------------------------------------------------------------
    // deposit

    /**
     * Send deposit cash request to the MySQL database
     *
     * @param cardNo card number
     * @param accNo  account number
     * @param cred   credential
     * @param amount deposit amount
     */

    static double deposit(String cardNo, String accNo, String cred, String amount) throws BAMSInvalidReplyException, IOException {
        double depAmount = bams.deposit(cardNo, accNo, cred, amount);
        return depAmount;
    } // deposit


    //------------------------------------------------------------
    // enquiry

    /**
     * Send enquiry account info request to the MySQL database
     *
     * @param cardNo card number
     * @param accNo  account number
     * @param cred   credential
     */
    static double enquiry(String cardNo, String accNo, String cred) throws BAMSInvalidReplyException, IOException {
        double amount = bams.enquiry(cardNo, accNo, cred);
        return amount;
    } // enquiry


    //------------------------------------------------------------
    // transfer

    /**
     * Send transfer cash request to the MySQL database
     *
     * @param cardNo  card number
     * @param cred    credential
     * @param fromAcc the account transferred from
     * @param toAcc   the account transferred to
     * @param amount  transfer amount
     */
    static double transfer(String cardNo, String cred, String fromAcc, String toAcc, String amount) throws BAMSInvalidReplyException, IOException {
        double transAmount = bams.transfer(cardNo, cred, fromAcc, toAcc, amount);
        return transAmount;
    } // transfer


    //------------------------------------------------------------
    // accStmtReq

    /**
     * Send account statement request to the MySQL database
     *
     * @param cardNo card number
     * @param accNo  account number
     * @param cred   credential
     */

    static String accStmtReq(String cardNo, String accNo, String cred) throws BAMSInvalidReplyException, IOException {
        String result = bams.accStmtReq(cardNo, accNo, cred);
        return result;
        // return "succ": success
        // return "-1": invalid credentials
    } // accStmtReq


    //------------------------------------------------------------
    // chqBookReq

    /**
     * Send cheque book request to the MySQL database
     *
     * @param cardNo card number
     * @param accNo  account number
     * @param cred   credential
     */

    static String chqBookReq(String cardNo, String accNo, String cred) throws BAMSInvalidReplyException, IOException {
        String result = bams.chqBookReq(cardNo, accNo, cred);
        return result;
        // return "succ": success
        // return "-1": invalid credentials
    } // chqBookReq


    //------------------------------------------------------------
    // chgPinReq

    /**
     * Send change pin request to the MySQL database
     *
     * @param cardNo card number
     * @param oldPin  previous card pin
     * @param newPin  new card pin
     * @param cred   credential
     */

    static String chgPinReq(String cardNo, String oldPin, String newPin, String cred) throws BAMSInvalidReplyException, IOException {
        String result = bams.chgPinReq(cardNo, oldPin, newPin, cred);
        return result;
        // return "succ": success
        // return "-1": invlaid credentials
        // return "Fail to change Pin"
    } // chgPinReq


    //------------------------------------------------------------
    // logout

    /**
     * Send logout request to the MySQL database
     *
     * @param cardNo card number
     * @param cred   credential
     */

    static String logout(String cardNo, String cred) throws BAMSInvalidReplyException, IOException {
        String result = bams.logout(cardNo, cred);
        return result;
        // return "succ": success
        // return "-1": invlaid credentials
    } // logout


    //------------------------------------------------------------
    // initLogger

    /**
     * Initialize logger
     *
     */

    static Logger initLogger() {
        // init our logger
        ConsoleHandler logConHdr = new ConsoleHandler();
        logConHdr.setFormatter(new LogFormatter());
        Logger log = Logger.getLogger("BAMSHandler");
        log.setUseParentHandlers(false);
        log.setLevel(Level.ALL);
        log.addHandler(logConHdr);
        logConHdr.setLevel(Level.ALL);
        return log;
    } // initLogger


    //------------------------------------------------------------
    // LogFormatter
    static class LogFormatter extends Formatter {
        //------------------------------------------------------------
        // format

        /**
         * Formatting the logging format
         *
         */

        public String format(LogRecord rec) {
            Calendar cal = Calendar.getInstance();
            String str = "";

            // get date
            cal.setTimeInMillis(rec.getMillis());
            str += String.format("%02d%02d%02d-%02d:%02d:%02d ",
                    cal.get(Calendar.YEAR) - 2000,
                    cal.get(Calendar.MONTH) + 1,
                    cal.get(Calendar.DAY_OF_MONTH),
                    cal.get(Calendar.HOUR_OF_DAY),
                    cal.get(Calendar.MINUTE),
                    cal.get(Calendar.SECOND));

            // level of the log
            str += "[" + rec.getLevel() + "] -- ";

            // message of the log
            str += rec.getMessage();
            return str + "\n";
        } // format
    } // LogFormatter
} // TestBAMSHandler

