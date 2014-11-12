import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.Serializable;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by alexander on 10/11/14.
 */
public class MinePanel extends JPanel implements Serializable {

    private static final long serialVersionUID = 1L;

    private MainFrame containingFrame;
    Field[][] fields;
    private boolean won;
    private boolean ended;
    private int height;
    private int width;
    private double pMine;
    private int amountFields;
    private int[] initialTime;

    public MinePanel(MainFrame containingFrame, int height, int width, double pMine, int[] initialTime){

        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.containingFrame = containingFrame;
        this.won = false;
        this.ended = false;
        this.height = height;
        this.width = width;
        this.pMine = pMine;
        this.initialTime = initialTime;
        this.amountFields = height * width;
        this.fields = new Field[height][width];
        for(int y = 0; y <= height -1; y++) {
            JPanel panel = new JPanel();
            panel.setLayout(new FlowLayout());
            for (int x = 0; x <= width -1; x++) {
                final Field parent = new Field(x, y, Math.random() < pMine);
                final ObserverButton tmp = new ObserverButton("?");
                tmp.setPreferredSize(new Dimension(60, 60));
                tmp.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent mouseEvent) {
                        if (SwingUtilities.isRightMouseButton(mouseEvent)) {
                            parent.setMarked(!parent.isMarked());
                        } else if (SwingUtilities.isLeftMouseButton(mouseEvent)) {
                            parent.open();
                        }
                    }

                    @Override
                    public void mousePressed(MouseEvent mouseEvent) {

                    }

                    @Override
                    public void mouseReleased(MouseEvent mouseEvent) {

                    }

                    @Override
                    public void mouseEntered(MouseEvent mouseEvent) {

                    }

                    @Override
                    public void mouseExited(MouseEvent mouseEvent) {

                    }
                });
                parent.addObserver(tmp);
                this.fields[x][y] = parent;
                panel.add(tmp);
            }
            this.add(panel);
        }
        this.connectNeighbors();
        containingFrame.startGame(this.initialTime);
    }

    private void connectNeighbors(){
        for (int i=0; i< this.fields.length; i++){
            for (int j=0; j< this.fields[i].length; j++){
                for (int iHelp=i-1; iHelp<=i+1; iHelp++){
                    for (int jHelp=j-1; jHelp<=j+1; jHelp++){
                        if (iHelp >= 0 && jHelp >= 0 &&
                            iHelp < this.fields.length &&
                            jHelp < this.fields[i].length
                            && (iHelp != i || jHelp != j)
                            ){
                            this.fields[i][j].addNeighbor(fields[iHelp][jHelp]);
                            }
                        }
                    }
                }
            }
    }

    public void resume(){

    }

    public boolean isWon() {
        return won;
    }

    public void setWon(boolean won) {
        this.won = won;
    }

    public boolean isEnded() {
        return ended;
    }

    public void setEnded(boolean ended) {
        this.ended = ended;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getWidth() {
        return width;
    }

    public double getpMine() {
        return pMine;
    }

    public int getAmountFields() {
        return amountFields;
    }

    class ObserverButton extends JButton implements Observer {

        public ObserverButton(String string) {
            super(string);
        }

        public void update(Observable arg0, Object arg1) {
            Field field = (Field) arg0;
            int tmp = 0;
            if(!isEnded()) {
                if (field.isExploded()) {
                    setEnded(true);
                    setWon(false);
                    this.setText("boom");
                    URL url = this.getClass().getResource("/" + "mine.png");
                    Image img = new ImageIcon(url).getImage();
                    Image newImg = img.getScaledInstance(60, 60, java.awt.Image.SCALE_SMOOTH);
                    Icon captionPic = new ImageIcon(newImg);
                    setIcon(captionPic);

                    containingFrame.endGame(won);

                } else if (field.isMarked()) {
                    this.setText("!");
                    tmp++;
                } else if (field.isOpen()) {
                    if (field.getAmountNeighborMines() > 0) {
                        this.setText(Integer.toString(field.getAmountNeighborMines()));
                    } else {
                        this.setText("  ");
                    }
                    tmp++;
                } else {
                    this.setText("?");
                    this.setIcon(null);

                }
                if (tmp == getAmountFields()) {
                    setEnded(true);
                    setWon(true);
                    containingFrame.endGame(won);
                }
            }
        }

    }

}

