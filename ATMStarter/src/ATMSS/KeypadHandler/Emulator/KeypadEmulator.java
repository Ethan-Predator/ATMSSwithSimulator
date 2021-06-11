package ATMSS.KeypadHandler.Emulator;

import ATMSS.ATMSSStarter;
import ATMSS.KeypadHandler.KeypadHandler;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;


//======================================================================
// KeypadEmulator
public class KeypadEmulator extends KeypadHandler {
    private ATMSSStarter atmssStarter;
    private String id;
    private Stage myStage;
    private KeypadEmulatorController keypadEmulatorController;

    //------------------------------------------------------------
    // KeypadEmulator
	/**
	 * Constructor for KeypadEmulator
	 *
	 * @param id       name of the application
	 * @param atmssStarter name of ATM starter
	 */
    public KeypadEmulator(String id, ATMSSStarter atmssStarter) {
	super(id, atmssStarter);
	this.atmssStarter = atmssStarter;
	this.id = id;
    } // KeypadEmulator


    //------------------------------------------------------------
    // start
	/**
	 * Start the keypad emulator's GUI
	 *
	 */
    public void start() throws Exception {
	Parent root;
	myStage = new Stage();
	FXMLLoader loader = new FXMLLoader();
	String fxmlName = "KeypadEmulator.fxml";
	loader.setLocation(KeypadEmulator.class.getResource(fxmlName));
	root = loader.load();
	keypadEmulatorController = (KeypadEmulatorController) loader.getController();
	keypadEmulatorController.initialize(id, atmssStarter, log, this);
	myStage.initStyle(StageStyle.DECORATED);
	myStage.setScene(new Scene(root, 340, 300));
	myStage.setTitle("KeypadHandler");
	myStage.setResizable(false);
//	myStage.setOnCloseRequest((WindowEvent event) -> {
//	    atmssStarter.stopApp();
//	    Platform.exit();
//	});
		myStage.setX(1100);
		myStage.setY(50);
	myStage.show();
    } // start


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
	} // stageClose


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

} // KeypadEmulator
