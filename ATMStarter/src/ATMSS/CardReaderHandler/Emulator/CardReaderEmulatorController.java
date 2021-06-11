package ATMSS.CardReaderHandler.Emulator;

import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.MBox;
import AppKickstarter.misc.Msg;

import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;


//======================================================================
// CardReaderEmulatorController
public class CardReaderEmulatorController {
    private String id;
    private AppKickstarter appKickstarter;
    private Logger log;
    private CardReaderEmulator cardReaderEmulator;
    private MBox cardReaderMBox;
    public TextField cardNumField;
    public TextField cardStatusField;
    public TextArea cardReaderTextArea;
//    public TextField amountField;
    public Button insertButton;
    public Button removeButton;


    //------------------------------------------------------------
    // initialize

    /**
     * Initialize card reader emulator controller
     *
     * @param id name of the application
     * @param appKickstarter object for starting the application
     * @param log object for logging
     * @param cardReaderEmulator object for emulating the card reader
     */
    public void initialize(String id, AppKickstarter appKickstarter, Logger log, CardReaderEmulator cardReaderEmulator) {
        this.id = id;
        this.appKickstarter = appKickstarter;
        this.log = log;
        this.cardReaderEmulator = cardReaderEmulator;
        this.cardReaderMBox = appKickstarter.getThread("CardReaderHandler").getMBox();
    } // initialize


    //------------------------------------------------------------
    // buttonPressed

    /**
     * Call different functions when different buttons are pressed
     * @param actionEvent the action received from the GUI
     *
     */
    public void buttonPressed(ActionEvent actionEvent) {
        Button btn = (Button) actionEvent.getSource();

        switch (btn.getText()) {
            case "Card 1":
                cardNumField.setText(appKickstarter.getProperty("CardReader.Card1"));
                break;

            case "Card 2":
                cardNumField.setText(appKickstarter.getProperty("CardReader.Card2"));
                break;

            case "Card 3":
                cardNumField.setText(appKickstarter.getProperty("CardReader.Card3"));
                break;

            case "Reset":
                cardNumField.setText("");
                break;

            case "Insert Card":
                if (cardNumField.getText().length() != 0) {
                    cardReaderMBox.send(new Msg(id, cardReaderMBox, Msg.Type.CR_CardInserted, cardNumField.getText()));
                    cardReaderTextArea.appendText("Sending " + cardNumField.getText() + "\n");
                    cardStatusField.setText("Card Inserted");
                    insertButton.setVisible(false);
                }
                break;

            case "Remove Card":
                if (cardStatusField.getText().compareTo("Card Ejected") == 0) {
                    insertButton.setVisible(true);
                    removeButton.setVisible(false);
                    cardReaderTextArea.appendText("Removing card\n");
                    cardReaderMBox.send(new Msg(id, cardReaderMBox, Msg.Type.CR_CardRemoved, cardNumField.getText()));
                }
                break;

            case "Severe Error":
                cardReaderMBox.send(new Msg(id, cardReaderMBox, Msg.Type.Severe_Hardware_Error, ""));
                break;

            case "Normal Error":
                cardReaderMBox.send(new Msg(id, cardReaderMBox, Msg.Type.Normal_Hardware_Error, ""));
                break;

            default:
                log.warning(id + ": unknown button: [" + btn.getText() + "]");
                break;
        }
    } // buttonPressed


    //------------------------------------------------------------
    // updateCardStatus
    /**
     * Call different functions when different buttons are pressed
     *
     */
    public void updateCardStatus(String status) {
        cardStatusField.setText(status);
    } // updateCardStatus


    //------------------------------------------------------------
    // appendTextArea
    /**
     * Append the card status in the text area
     *
     */
    public void appendTextArea(String status) {
        cardReaderTextArea.appendText(status + "\n");
    } // appendTextArea

    //------------------------------------------------------------
    // updateTextArea
    /**
     * Update the card status in GUI
     *
     */
    public void updateTextArea(String status) {
        cardReaderTextArea.setText(status);
    } // updatecardReaderTextArea

    //------------------------------------------------------------
    // updatecardNumField
    /**
     * Update the card number in GUI
     *
     */
    public void updatecardNumField(String status) {
        cardNumField.setText(status);
    } // updatecardcardNumField
} // CardReaderEmulatorController
