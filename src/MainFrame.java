import javax.swing.*;
import java.awt.*;

/**
 * Created by alexander on 10/11/14.
 */
public class MainFrame extends JFrame {

    private MinePanel mineField;
    private ControlInterface controlInterface;
    private Controller controller;

    public MainFrame (int height, int width, double pMine){

        this.mineField = new MinePanel();
        this.controlInterface = new ControlInterface();
        this.controller = new Controller(this, this.mineField, this.controlInterface, height, width, pMine);
        controlInterface.initComponents(this.controller);

        this.initComponents();

        this.controller.startGame();

    }

    public void initComponents(){
        this.setTitle("Minesweeper");
        this.setLayout(new BorderLayout());
        this.add(controlInterface, BorderLayout.SOUTH);
        this.add(mineField, BorderLayout.CENTER);
        this.pack();
        this.setResizable(false);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void clear(){
        this.removeAll();
    }

    public void setMineField(MinePanel mineField){
        this.mineField = mineField;
    }

}
