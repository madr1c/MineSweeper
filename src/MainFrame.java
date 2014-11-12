import javax.swing.*;
import java.awt.*;

/**
 * Created by alexander on 10/11/14.
 */
public class MainFrame extends JFrame {

    private MinePanel mineField;
    private ControlInterface controlInterface;

    public MainFrame (int height, int width, double pMine){

        this.controlInterface = new ControlInterface(this);
        this.mineField = new MinePanel(this, height, width, pMine, new int[] {0, 0, 0});

        this.initComponents();

    }

    private void initComponents(){
        this.setLayout(new BorderLayout());
        this.add(controlInterface, BorderLayout.SOUTH);
        this.add(mineField, BorderLayout.CENTER);
        this.pack();
        this.setResizable(false);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void endGame(boolean won){

        int dialogButton;

        if(!won) {
            dialogButton = JOptionPane.YES_NO_OPTION;
            JOptionPane.showConfirmDialog(null,
                    "Warum so verlierend? Nochmal?",
                    "Fasager", dialogButton);
            if (dialogButton == JOptionPane.YES_OPTION) {
                resetGame();
            } else if (dialogButton == JOptionPane.NO_OPTION) {
                this.dispose();
            }
        } else  if(won){
            dialogButton = JOptionPane.YES_NO_OPTION;
            JOptionPane.showConfirmDialog(null,
                "Haste tatsächlich gewonnen. Nochmal?",
                "Sieg", dialogButton);
            if (dialogButton == JOptionPane.YES_OPTION) {
                resetGame();
            } else if (dialogButton == JOptionPane.NO_OPTION) {
                this.dispose();
            }
        }

    }

    public void pauseGame(){
        this.controlInterface.stopTimer();
    }

    public void startGame(int[] initialTime){
        this.controlInterface.startTimer(initialTime);
    }

    public void resetGame(){
        this.controlInterface.stopTimer();
        this.mineField = new MinePanel(this, mineField.getHeights(), mineField.getWidths(), mineField.getpMine(), new int[] {0, 0, 0});
        this.add(mineField, BorderLayout.CENTER);

        this.controlInterface.resetTimer();
        this.controlInterface.resumeTimer();
    }

//    public void laden() {
//        JFileChooser chooser = new JFileChooser();
//        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
//
//            try {
//                File f = chooser.getSelectedFile();
//                FileInputStream fis;
//                fis = new FileInputStream(f);
//                ObjectInputStream ois = new ObjectInputStream(fis);
//                mineField=(MinePanel) ois.readObject();
//                System.out.println(f);
//                init(breite, hoehe, spiel);
//            } catch (Exception e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//
//        }
//
//    }
//
//    public void speichern() {
//        JFileChooser chooser = new JFileChooser();
//        if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
//
//            try {
//                File f = chooser.getSelectedFile();
//                FileOutputStream fos;
//                fos = new FileOutputStream(f);
//                ObjectOutputStream oos = new ObjectOutputStream(fos);
//                //this.trennen();
//                oos.writeObject(mineField);
//                oos.close();
//                System.out.println(f);
//            } catch (Exception e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//
//        }
//    }

}
