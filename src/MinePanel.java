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

    public MinePanel(MainFrame containingFrame){

        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

    }

    public void init(final Game game){
        this.game = game;
        this.buttons = new JButton[this.game.getHeight()][this.game.getWidth()];

        for(Field[] row : game.getFields()){

            JPanel panel = new JPanel(new GridLayout());

            for(Field current : row){

                final int x = current.getX();
                final int y = current.getY();

                final ObserverButton tmp = new ObserverButton("?");
                tmp.setPreferredSize(new Dimension(60, 60));
                tmp.addMouseListener(new MouseListener() {
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
                current.addObserver(tmp);
                this.buttons[x][y] = tmp;
                panel.add(tmp);
            }
            this.add(panel);
        }
    }

    public Game getGame() {
        return game;
    }

    public void clear(){
        this.removeAll();
    }

    class ObserverButton extends JButton implements Observer {

        public ObserverButton(String string) {
            super(string);
        }

        public void update(Observable arg0, Object arg1) {
            Field field = (Field) arg0;
            int tmp = 0;
            //if(!isEnded()) {
                if (field.isExploded()) {
                    game.setEnded(true);
                    game.setWon(false);
                    this.setText("boom");
                    URL url = this.getClass().getResource("/" + "mine.png");
                    Image img = new ImageIcon(url).getImage();
                    Image newImg = img.getScaledInstance(60, 60, java.awt.Image.SCALE_SMOOTH);
                    Icon captionPic = new ImageIcon(newImg);
                    setIcon(captionPic);
                    for(Field[] row : game.fields){
                        for(Field current : row){
                            current.open();
                            current.deleteObservers();
                        }
                    }
                    game.setEnded(true);
                    game.setWon(false);
                    //containingFrame.endGame(won);

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
            //}
//            if (tmp == getAmountFields()) {
//                for(Field[] row : fields){
//                    for(Field current : row){
//                        current.open();
//                       // current.deleteObservers();
//                    }
//                }
//                setEnded(true);
//                setWon(true);
//                //containingFrame.endGame(won);
//            }
        }

    }

}

