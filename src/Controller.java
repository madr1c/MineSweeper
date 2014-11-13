import javax.swing.*;
import java.awt.*;
import java.io.*;

/**
 * Created by alexander on 10/11/14.
 */
public class Controller {

    private MainFrame containingFrame;
    private ControlInterface controlInterface;
    private MinePanel mineField;
    private Game game;

    public Controller(MainFrame containingFrame, MinePanel mineField, ControlInterface controlInterface, int height, int width, double pMine){

        this.containingFrame = containingFrame;
        this.mineField = mineField;
        this.controlInterface = controlInterface;

        this.newGame(height, width, pMine);
    }

    public void newGame(int height, int width, double pMine){
        this.game = new Game(height, width, pMine);
        this.mineField.init(this.game);
        this.startGame();
    }

    public void endGame(boolean won){

        int dialogButton;

        if(!won) {
            dialogButton = JOptionPane.YES_NO_OPTION;
            JOptionPane.showConfirmDialog(null,
                    "Warum so verlierend? Nochmal?",
                    "Fasager", dialogButton);
            if (dialogButton == JOptionPane.YES_OPTION) {
                this.resetGame();
            } else if (dialogButton == JOptionPane.NO_OPTION) {
                this.containingFrame.dispose();
            }
        } else  if(won){
            dialogButton = JOptionPane.YES_NO_OPTION;
            JOptionPane.showConfirmDialog(null,
                    "Haste tatsächlich gewonnen. Nochmal?",
                    "Sieg", dialogButton);
            if (dialogButton == JOptionPane.YES_OPTION) {
                this.resetGame();
            } else if (dialogButton == JOptionPane.NO_OPTION) {
                this.containingFrame.dispose();
            }
        }

    }

    public void resumeGame() { this.controlInterface.resumeTimer(); }

    public void pauseGame(){
        this.controlInterface.stopTimer();
    }

    public void startGame(){
        this.controlInterface.startTimer(this.game.getInitialTime());
    }

    public void resetGame(){
        this.controlInterface.stopTimer();
        this.mineField.clear();
        this.game.reset();
        this.mineField.init(this.game);
        this.controlInterface.startTimer(this.game.getInitialTime());
    }

    public void laden() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {

            try {
                File f = chooser.getSelectedFile();
                FileInputStream fis;
                fis = new FileInputStream(f);
                ObjectInputStream ois = new ObjectInputStream(fis);
                this.mineField=(MinePanel) ois.readObject();
                ois.close();
                this.game = mineField.getGame();
                this.containingFrame.setMineField(this.mineField);
                this.containingFrame.clear();
                this.containingFrame.initComponents();
                this.startGame();
                System.out.println(f);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

    }

    public void speichern() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {

            try {
                this.pauseGame();
                File f = chooser.getSelectedFile();
                FileOutputStream fos;
                fos = new FileOutputStream(f);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(this.mineField);
                oos.close();
                this.resumeGame();
                System.out.println(f);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }

    // start Game

    // stop Game

    // reset Game

    // load Game

    // save Game
}
