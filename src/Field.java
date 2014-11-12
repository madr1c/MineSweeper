import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Observable;
import javax.swing.SwingUtilities;

/**
 * Created by alexander on 10/11/14.
 */
public class Field extends Observable implements Serializable {

    private static final long serialVersionUID = 1L;

    private int x;
    private int y;
    private boolean mine;
    private boolean open;
    private boolean marked;
    public boolean isExploded() {
        return isOpen() && isMine();
    }
    private ArrayList<Field> neighbors;

    public Field(int x, int y, boolean isMine){
        this.mine = isMine;
        this.open = false;
        this.marked = false;
        this.x = x;
        this.y = y;
        neighbors = new ArrayList<Field>();
    }

    public void addNeighbor(Field neighbor){
        neighbors.add(neighbor);
    }

    public int getAmountNeighborMines() {
        int amountMines = 0;
        for (Field neighbor : neighbors) {
            if (neighbor.isMine()) {
                amountMines++;
            }
        }
        return amountMines;
    }

    public void open(){
        if(!isOpen() && !isMarked()){
            setOpen(true);
            if(!isMine() ){
                if(0 == getAmountNeighborMines()) {
                    for (Field neighbor : neighbors) {
                        neighbor.open();
                    }
                }
            }
            setChanged();
            notifyObservers();
        }
    }

    public boolean isMarked() {
        return marked;
    }

    public void setMarked(boolean marked) {
        this.marked = marked;
        setChanged();
        notifyObservers();
    }

    public boolean isMine() {
        return mine;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
