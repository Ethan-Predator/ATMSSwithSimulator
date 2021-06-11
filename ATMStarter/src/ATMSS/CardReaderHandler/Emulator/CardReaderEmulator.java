package ATMSS.CardReaderHandler.Emulator;

import ATMSS.ATMSSStarter;
import ATMSS.CardReaderHandler.CardReaderHandler;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;


//======================================================================
// CardReaderEmulator
public class CardReaderEmulator extends CardReaderHandler {
    private ATMSSStarter atmssStarter;
    private String id;
    private Stage myStage;
    private CardReaderEmulatorController cardReaderEmulatorController;

    //------------------------------------------------------------
    // CardReaderEmulator

    /**
     * Constructor for CardReaderEmulator
     *
     * @param id       name of the application
     * @param atmssStarter name of ATM starter
     */
    public CardReaderEmulator(String id, ATMSSStarter atmssStarter) {
        super(id, atmssStarter);
        this.atmssStarter = atmssStarter;
        this.id = id;
    } // CardReaderEmulator


    //------------------------------------------------------------
    // start
    /**
     * Start the card reader emulator's GUI
     *
     */
    public void start() throws Exception {
        Parent root;
        myStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        String fxmlName = "CardReaderEmulator.fxml";
        loader.setLocation(CardReaderEmulator.class.getResource(fxmlName));
        root = loader.load();
        cardReaderEmulatorController = (CardReaderEmulatorController) loader.getController();
        cardReaderEmulatorController.initialize(id, atmssStarter, log, this);
        myStage.initStyle(StageStyle.DECORATED);
        myStage.setScene(new Scene(root, 350, 470));
        myStage.setTitle("Card Reader");
        myStage.setResizable(false);
//        myStage.setOnCloseRequest((WindowEvent event) -> {
//            atmssStarter.stopApp();
//            Platform.exit();
//        });
        myStage.setX(700);
        myStage.setY(30);
        myStage.show();
    } // CardReaderEmulator


    //------------------------------------------------------------
    // handleCardInsert
    /**
     * Handle card insert case when the CR_CardInserted message type is received by card reader handler
     *
     */
    protected void handleCardInsert() {
        super.handleCardInsert();
        cardReaderEmulatorController.appendTextArea("Card Inserted");
        cardReaderEmulatorController.updateCardStatus("Card Inserted");
    } // handleCardInsert


    //------------------------------------------------------------
    // handleCardEject
    /**
     * Handle card eject case when the CR_EjectCard message type is received by card reader handler
     *
     */
    protected void handleCardEject() {
        super.handleCardEject();
        cardReaderEmulatorController.appendTextArea("Card Ejected");
        cardReaderEmulatorController.updateCardStatus("Card Ejected");
        cardReaderEmulatorController.removeButton.setVisible(true);
    } // handleCardEject


    //------------------------------------------------------------
    // handleCardRemove
    /**
     * Handle card remove case when the CR_CardRemoved message type is received by card reader handler
     *
     */
    protected void handleCardRemove() {
        super.handleCardRemove();
        cardReaderEmulatorController.appendTextArea("Card Removed");
        cardReaderEmulatorController.updateCardStatus("Card Reader Empty");
    } // handleCardRemove


    //------------------------------------------------------------
    // stageClose()
    /**
     * Close the stage of GUI
     *
     */
    public void stageClose(){
        Platform.runLater(
                () -> {
                    myStage.close();
                    // Update UI here.
                }
        );
    } // stageClose()


    //------------------------------------------------------------
    // reset
    /**
     * Reset clear the all the data and cache in the emulator when restarting the emulator
     *
     */
    public void reset(){
        cardReaderEmulatorController.updateCardStatus("");
        cardReaderEmulatorController.updateTextArea("");
        cardReaderEmulatorController.updatecardNumField("");
        cardReaderEmulatorController.removeButton.setVisible(false);
        cardReaderEmulatorController.insertButton.setVisible(true);
    } // reset


    //------------------------------------------------------------
    // stageHide
    /**
     * Hide the stage
     *
     */
    public void stageHide(){
        Platform.runLater(
                () -> {
                    myStage.hide();
                    // Update UI here.
                }
        );
    } // stageHide


    //------------------------------------------------------------
    // stageHide
    /**
     * Show the stage
     *
     */
    public void stageShow(){
        Platform.runLater(
                () -> {
                    myStage.show();
                    // Update UI here.
                }
        );
    } // stageShow
} // CardReaderEmulator
