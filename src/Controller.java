import javax.swing.*;
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
        System.out.println(Integer.toString(this.game.getInitialTime()[0]) + ":" + Integer.toString(this.game.getInitialTime()[1]) + ":" + Integer.toString(this.game.getInitialTime()[2]));
        this.controlInterface.startTimer(this.game.getInitialTime());
    }

    public void resetGame(){
        this.controlInterface.stopTimer();
        this.mineField.clear();
        this.game.reset();
        this.mineField.init(this.game);
        this.controlInterface.startTimer(this.game.getInitialTime());
    }

    public void loadGame() {
        this.pauseGame();
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {

            try {
                File f = chooser.getSelectedFile();
                FileInputStream fis;
                fis = new FileInputStream(f);
                ObjectInputStream ois = new ObjectInputStream(fis);

                this.mineField = (MinePanel) ois.readObject();

                ois.close();
                fis.close();
                this.game = this.mineField.getGame();
                this.mineField.clear();
                this.mineField.init(this.game);
                this.containingFrame.clear();
                this.containingFrame.setMineField(this.mineField);
                this.containingFrame.initComponents();
                System.out.println(f);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        this.startGame();

    }

    public void saveGame() {
        this.pauseGame();
        this.game.setInitialTime(this.controlInterface.getTime());
        JFileChooser chooser = new JFileChooser();
        if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {

            try {
                File f = chooser.getSelectedFile();
                FileOutputStream fos;
                fos = new FileOutputStream(f);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(this.mineField);
                oos.close();
                fos.close();
                System.out.println(f);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        this.resumeGame();
    }

    public void setControlInterface(ControlInterface controlInterface) {
        this.controlInterface = controlInterface;
    }
}
