package ATMSS.PrinterHandler.Emulator;


import ATMSS.ATMSSStarter;
import ATMSS.PrinterHandler.PrinterHandler;
import AppKickstarter.misc.Msg;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

//======================================================================
// PrinterEmulator
public class PrinterEmulator extends PrinterHandler {
    private ATMSSStarter atmssStarter;
    private String id;
    private Stage myStage;
    private PrinterEmulatorController printerEmulatorController;


    //------------------------------------------------------------
    // PrinterEmulator
    /**
     * Constructor for PrinterEmulator
     *
     * @param id       name of the application
     * @param atmssStarter name of ATM starter
     */
    public PrinterEmulator(String id, ATMSSStarter atmssStarter) {
        super(id, atmssStarter);
        this.atmssStarter = atmssStarter;
        this.id = id;
    } // PrinterEmulator


    //------------------------------------------------------------
    // handlePrinter
    /**
     * Handle the message received from atmss
     * @param msg message received from its own message box
     *
     */
    protected void handlePrinter(Msg msg) {
        String details = msg.getDetails();
        String accType = details.split("/")[0];
        String accNum = details.split("/")[1];
        String transacType = details.split("/")[2];
        String amount = details.split("/")[3];
        String status = details.split("/")[4];
        printerEmulatorController.printMsg(accType, accNum, transacType, amount, status);
    } // handlePrinter


    //------------------------------------------------------------
    // start
    /**
     * Start the card printer emulator's GUI
     *
     */
    public void start() throws Exception {
        Parent root;
        myStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        String fxmlName = "PrinterEmulator.fxml";
        loader.setLocation(PrinterEmulator.class.getResource(fxmlName));
        root = loader.load();
        printerEmulatorController = (PrinterEmulatorController) loader.getController();
        printerEmulatorController.initialize(id, atmssStarter, log, this);
        myStage.initStyle(StageStyle.DECORATED);
        myStage.setScene(new Scene(root, 400, 450));
        myStage.setTitle("PrinterHandler");
        myStage.setResizable(false);
//        myStage.setOnCloseRequest((WindowEvent event) -> {
//            atmssStarter.stopApp();
//            Platform.exit();
//        });
        myStage.setX(1000);
        myStage.setY(800);
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
    } // stageClose()


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
} // PrinterEmulator
