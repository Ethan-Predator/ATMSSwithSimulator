package ATMSS.BuzzerHandler.Emulator;

import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.MBox;

import java.io.File;
import java.util.logging.Logger;

import AppKickstarter.misc.Msg;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.media.AudioClip;


//======================================================================
// BuzzerEmulatorController
public class BuzzerEmulatorController {
    private String id;
    private AppKickstarter appKickstarter;
    private Logger log;
    private BuzzerEmulator buzzerEmulator;
    private MBox buzzerMBox;

    private AudioClip ac;
    private boolean BuzzerStatus = false;


    //------------------------------------------------------------
    // initialize

    /**
     * Initialize buzzer emulator controller
     *
     * @param id name of the application
     * @param appKickstarter object for starting the application
     * @param log object for logging
     * @param buzzerEmulator object for emulating the buzzer
     */
    public void initialize(String id, AppKickstarter appKickstarter, Logger log, BuzzerEmulator buzzerEmulator) {
        this.id = id;
        this.appKickstarter = appKickstarter;
        this.log = log;
        this.buzzerEmulator = buzzerEmulator;
        this.buzzerMBox = appKickstarter.getThread("BuzzerHandler").getMBox();
        this.ac = new AudioClip(new File("src/ATMSS/BuzzerHandler/Emulator/buzzerAlert.mp3").toURI().toString());
    } // initialize


    //------------------------------------------------------------
    // setBuzzerRing

    /**
     * Ring the buzzer in another thread
     *
     */
    public void setBuzzerRing() throws Exception {

        BuzzerStatus = true;

        Thread t = new Thread(() -> {
            try {
                while (BuzzerStatus) {
                    ac.play();
                    Thread.sleep(2000);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        t.start();

    } // setBuzzerRing

    //------------------------------------------------------------
    // buttonPressed
    public void buttonPressed(ActionEvent actionEvent) {
        javafx.scene.control.Button btn = (Button) actionEvent.getSource();

        switch (btn.getText()) {
            case "Severe Error":
                buzzerMBox.send(new Msg(id, buzzerMBox, Msg.Type.Severe_Hardware_Error, ""));
                break;

            case "Normal Error":
                buzzerMBox.send(new Msg(id, buzzerMBox, Msg.Type.Normal_Hardware_Error, ""));
                break;

            default:
                log.warning(id + ": unknown button: [" + btn.getText() + "]");
                break;
        }
    } // buttonPressed

    //------------------------------------------------------------
    // setBuzzerStop

    /**
     * Stop the buzzer
     *
     */
    public void setBuzzerStop() {
        BuzzerStatus = false;
    }// setBuzzerStop

} // BuzzerEmulatorController
