package ATMSS.KeypadHandler.Emulator;

import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.MBox;
import AppKickstarter.misc.Msg;

import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;


//======================================================================
// KeypadEmulatorController
public class KeypadEmulatorController {
    private String id;
    private AppKickstarter appKickstarter;
    private Logger log;
    private KeypadEmulator keypadEmulator;
    private MBox keypadMBox;


    //------------------------------------------------------------
    // initialize

    /**
     * Initialize keypad emulator controller
     *
     * @param id name of the application
     * @param appKickstarter object for starting the application
     * @param log object for logging
     * @param keypadEmulator object for emulating the keypad
     */
    public void initialize(String id, AppKickstarter appKickstarter, Logger log, KeypadEmulator keypadEmulator) {
        this.id = id;
        this.appKickstarter = appKickstarter;
        this.log = log;
        this.keypadEmulator = keypadEmulator;
        this.keypadMBox = appKickstarter.getThread("KeypadHandler").getMBox();
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
        String btnTxt = btn.getText();
        switch (btnTxt) {
            case "Severe Error":
                keypadMBox.send(new Msg(id, keypadMBox, Msg.Type.Severe_Hardware_Error, ""));
                break;

            case "Normal Error":
                keypadMBox.send(new Msg(id, keypadMBox, Msg.Type.Normal_Hardware_Error, ""));
                break;

            default:
                keypadMBox.send(new Msg(id, keypadMBox, Msg.Type.KP_KeyPressed, btnTxt));
                break;
        }
    } // buttonPressed


    //------------------------------------------------------------
    // keyPressed
    /**
     * Call different functions when different keys are pressed in the keypad emulator GUI
     * @param keyEvent the action received from the GUI
     *
     */
    public void keyPressed(KeyEvent keyEvent) {
        String keyCodeStr = keyEvent.getCode().toString();

        if (keyCodeStr.startsWith("DIGIT") || keyCodeStr.startsWith("NUMPAD")) {
            // send the digit one by one to the MBox
            char inputChar = keyCodeStr.charAt(keyCodeStr.length() - 1);
            keypadMBox.send(new Msg(id, keypadMBox, Msg.Type.KP_KeyPressed, "" + inputChar));
        } else if (keyCodeStr.compareTo("DECIMAL") == 0) {
            // send the '.' to the MBox
            keypadMBox.send(new Msg(id, keypadMBox, Msg.Type.KP_KeyPressed, "."));
        } else if (keyCodeStr.compareTo("ENTER") == 0) {
            keypadMBox.send(new Msg(id, keypadMBox, Msg.Type.KP_KeyPressed, "ENTER"));
        } else if (keyCodeStr.compareTo("ESCAPE") == 0) {
            keypadMBox.send(new Msg(id, keypadMBox, Msg.Type.KP_KeyPressed, "CANCEL"));
        } else if (keyCodeStr.compareTo("BACK_SPACE") == 0) {
            keypadMBox.send(new Msg(id, keypadMBox, Msg.Type.KP_KeyPressed, "ERASE"));
        } else {
            // other key pressed
            log.finer(id + ": Key Pressed " + keyCodeStr);
        }
    } // keyPressed
} // KeypadEmulatorController
