import java.io.Serializable;

/**
 * Created by alexander on 10/11/14.
 */
public class Game implements Serializable {

    private static final long serialVersionUID = 1L;

    Field[][] fields;
    boolean ended = false;
    boolean won = false;
    private int[] initialTime;
    private int height;
    private int width;
    private double pMine;

    public Game(int height, int width, double pMine, int[] initialTime) {

        this.won = false;
        this.ended = false;
        this.height = height;
        this.width = width;
        this.pMine = pMine;
        this.fields = new Field[height][width];

        if( null == initialTime )
            this.initialTime = new int[] {0, 0, 0};
        else
            this.initialTime = initialTime;

        for(int x = 0; x <= height -1; x++) {
            for (int y = 0; y <= width - 1; y++) {
                this.fields[x][y] = new Field(x, y, Math.random() < pMine);
            }
        }

        this.connectNeighbors();
    }

    public Game(int height, int width, double pMine){
        this(height, width, pMine, null);
    }

    public void reset(){

        this.won = false;
        this.ended = false;
        this.initialTime = new int[] {0, 0, 0};
        this.fields = new Field[height][width];

        for(int x = 0; x <= this.height -1; x++) {
            for (int y = 0; y <= this.width - 1; y++) {
                this.fields[x][y] = new Field(x, y, Math.random() < this.pMine);
            }
        }

        connectNeighbors();
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

    public void setFields(Field[][] fields){
        this.fields = fields;
    }

    public Field[][] getFields(){
        return fields;
    }

    public Field getField(int x, int y){
        return fields[x][y];
    }

    public void openField(int x, int y){
        this.fields[x][y].open();
    }

    public void markField(int x, int y){
        this.fields[x][y].setMarked(!this.fields[x][y].isMarked());
    }

    public int getAmountFields(){
        return height * width;
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

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int[] getInitialTime() {
        return initialTime;
    }

    public void setInitialTime(int[] initialTime) {
        this.initialTime = initialTime;
    }

    public double getpMine() {
        return pMine;
    }
}
