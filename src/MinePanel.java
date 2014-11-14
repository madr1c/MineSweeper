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

    JButton[][] buttons;
    private Game game;

    public MinePanel(){

        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

    }

    public void init(final Game game){
        this.game = game;
        this.buttons = new JButton[this.game.getHeight()][this.game.getWidth()];

        for(Field[] row : game.getFields()){

            JPanel panel = new JPanel(new GridLayout());

            for(Field current : row){

                int x = current.getX();
                int y = current.getY();

                final ObserverButton tmp = new ObserverButton(this.getButtonCaption(x, y));
                tmp.setPreferredSize(new Dimension(60, 60));
                this.initMouseListener(tmp, x, y);
                current.addObserver(tmp);
                this.buttons[x][y] = tmp;
                panel.add(tmp);
            }
            this.add(panel);
        }
    }

    public void initMouseListener(ObserverButton button, final int x, final int y){
        button.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                if (SwingUtilities.isRightMouseButton(mouseEvent)) {
                    game.markField(x, y);
                } else if (SwingUtilities.isLeftMouseButton(mouseEvent)) {
                    game.openField(x, y);
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
    }

    public String getButtonCaption(int x, int y){

        Field field = this.game.getField(x, y);

        if (field.isMarked())
            return "!";
        else if(!field.isOpen())
            return "?";
        else if(field.isOpen())
            if( 0 == field.getAmountNeighborMines())
                return " ";
            else
                return Integer.toString(field.getAmountNeighborMines());

        return null;
    }

    public Game getGame() {
        return game;
    }

    public void clear(){
        this.removeAll();
        this.repaint();
    }

    class ObserverButton extends JButton implements Observer {

        public ObserverButton(String string) {
            super(string);
        }

        public void update(Observable arg0, Object arg1) {
            Field field = (Field) arg0;
                if (field.isExploded()) {
                    this.setText("boom");
                    URL url = this.getClass().getResource("/" + "mine.png");
                    Image img = new ImageIcon(url).getImage();
                    Image newImg = img.getScaledInstance(60, 60, java.awt.Image.SCALE_SMOOTH);
                    Icon captionPic = new ImageIcon(newImg);
                    setIcon(captionPic);
                    for (Field[] row : game.fields) {
                        for (Field current : row) {
                            current.unmark();
                            //current.setMarked(false);
                            current.open();
                            current.deleteObservers();
                        }
                    }
                    game.setEnded(true);
                    game.setWon(false);
                    //controller endGame(won);

                } else if (field.isMarked()) {
                    this.setText("!");
                } else if (field.isOpen()) {
                    if (field.getAmountNeighborMines() > 0)
                        this.setText(Integer.toString(field.getAmountNeighborMines()));
                    else
                        this.setText("  ");
                } else {
                    this.setText("?");
                    this.setIcon(null);
                }

            if (this.checkVictory()) {
                for(Field[] row : game.fields){
                    for(Field current : row){
                        current.open();
                        current.deleteObservers();
                    }
                }
                game.setEnded(true);
                game.setWon(true);
                //containingFrame.endGame(won);
            }
        }

        private boolean checkVictory(){
            for(Field[] row : game.fields){
                for(Field current : row){
                    if(!current.isMarked() && !current.isOpen() || current.isExploded())
                        return false;

                }
            }
            return true;
        }

    }

}

