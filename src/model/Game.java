/*
 * (C) Copyright 2014 Mark Alexander Dietrich <mark.dietrich93@gmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *     Mark Alexander Dietrich
 */

package model;

import java.io.Serializable;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Mark Alexander Dietrich on 06/11/14.
 * <p/>
 * File: Game.java
 * <p/>
 * Class: Game
 * Represents state of the {@link model.Game}.
 *
 * @author Mark Alexander Dietrich
 * @see java.util.Observable
 * @see java.io.Serializable
 */
public class Game extends Observable implements Serializable {

    /**
     * {@code serialVersionUID}, which is used during deserialization to verify that the sender and receiver of a
     * serialized object have loaded classes for that object that are compatible with respect to serialization. It has
     * to be {@link final} and {@link long}.
     *
     * @see java.io.ObjectOutputStream
     * @see java.io.ObjectInputStream
     * @see final
     * @see long
     */
    private static final long serialVersionUID = 1L;

    /**
     * Matrix which contains the {@link model.Field}s of the {@link model.Game}.
     */
    Field[][] fields;

    /**
     * Game over?
     */
    boolean ended = false;

    /**
     * Game won?
     */
    boolean won = false;

    /**
     * Contains three {@link java.lang.Integer}s which are representing hours, minutes and seconds. It shows the
     * {@link view.ControlInterface} the time to initialize the {@link javax.swing.Timer} with.
     *
     * @see javax.swing.Timer
     */
    private int[] initialTime;

    /**
     * Height of the pitch.
     */
    private int height;

    /**
     * Width of the pitch.
     */
    private int width;

    /**
     * Probability that a {@link model.Field} is a mine.
     */
    private double pMine;

    /**
     * Is it the first turn of the game?
     */
    private boolean firstTurn;

    /**
     * Class constructor.
     *
     * @param height      {@link int} Height of the pitch.
     * @param width       {@link int} Width of the pitch.
     * @param pMine       {@link double} Represents the probability that a {@link model.Field} is a mine.
     * @param initialTime {@link int[]} Time to initialize the {@link model.Game} with.
     */
    public Game(int height, int width, double pMine, int[] initialTime) {

        // Initialize parameters
        this.won = false;
        this.ended = false;
        this.height = height;
        this.width = width;
        this.pMine = pMine;
        this.fields = new Field[height][width];
        this.firstTurn = true;

        // If no initialTime was given start at 0
        if (null == initialTime)
            this.initialTime = new int[]{0, 0, 0};
        else
            this.initialTime = initialTime;

        // Initialize {@link model.Field}s of the pitch
        for (int x = 0; x <= height - 1; x++) {
            for (int y = 0; y <= width - 1; y++) {
                Field tmp = new Field(x, y, Math.random() < this.pMine);
                // Add {@link model.Game.StartObserver}
                tmp.addObserver(new StartObserver());
                this.fields[x][y] = tmp;
            }
        }
        // Let the {@link model.Field}s know which {@link model.Field}s border on them
        this.connectNeighbors();
    }

    /**
     * Class constructor.
     *
     * @param height {@link int} Height of the pitch.
     * @param width  {@link int} Width of the pitch.
     * @param pMine  {@link double} Represents the probability that a {@link model.Field} is a mine.
     */
    public Game(int height, int width, double pMine) {
        // Constructor chaining with the constructor above
        this(height, width, pMine, null);
    }

    /**
     * Resets this {@link model.Game}.
     */
    public void reset() {

        // Reinitialize parameters
        this.won = false;
        this.ended = false;
        this.initialTime = new int[]{0, 0, 0};
        this.fields = new Field[height][width];
        this.firstTurn = true;

        // Reinitialize {@link model.Field}s of the pitch
        for (int x = 0; x <= this.height - 1; x++) {
            for (int y = 0; y <= this.width - 1; y++) {
                Field tmp = new Field(x, y, Math.random() < this.pMine);
                tmp.addObserver(new StartObserver());
                this.fields[x][y] = tmp;
            }
        }
        // Let the {@link model.Field}s know which {@link model.Field}s border on them
        connectNeighbors();
    }

    /**
     * Iterates trough the {@link model.Field}s and let them know which {@link model.Field}s border on them.
     */
    private void connectNeighbors() {
        for (int i = 0; i < this.fields.length; i++) {
            for (int j = 0; j < this.fields[i].length; j++) {
                for (int iHelp = i - 1; iHelp <= i + 1; iHelp++) {
                    for (int jHelp = j - 1; jHelp <= j + 1; jHelp++) {
                        if (iHelp >= 0 && jHelp >= 0 &&
                                iHelp < this.fields.length &&
                                jHelp < this.fields[i].length
                                && (iHelp != i || jHelp != j)
                                ) {
                            this.fields[i][j].addNeighbor(fields[iHelp][jHelp]);
                        }
                    }
                }
            }
        }
    }

    public Field[][] getFields() {
        return fields;
    }

    public Field getField(int x, int y) {
        return fields[x][y];
    }

    public void openField(int x, int y) {
        this.fields[x][y].open();
    }

    public void markField(int x, int y) {
        this.fields[x][y].setMarked(!this.fields[x][y].isMarked());
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
        // Notify {@link controller.Controller.GameObserver} if this {@link model.Game} is over
        this.setChanged();
        this.notifyObservers();
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

    /**
     * Class: StartObserver
     * Implements an {@link java.util.Observer} which should observe a {@link model.Game}. If it is the first turn of
     * the {@link model.Game} it makes you invulnerable against mines.
     *
     * @see java.util.Observer
     * @see model.Game
     */
    class StartObserver implements Observer {

        /**
         * Class constructor.
         */
        public StartObserver() {
            super();
        }

        /**
         * Update method of this observer. If the referencing {@link model.Field} changes it's state and it's the first
         * turn of the referencing {@link model.Game} it makes the user invulnerable against mines.
         *
         * @param arg0 {@link java.util.Observable} observed observable object.
         * @param arg1 {@link java.lang.Object} optional parameter.
         * @see java.util.Observer
         * @see java.util.Observable
         * @see java.lang.Object
         * @see model.Field
         * @see model.Game
         */
        public void update(Observable arg0, Object arg1) {
            Field field = (Field) arg0;

            if (null != arg1) {
                if (firstTurn) {
                    // If the {@link model.Field#isMine} is true refuse it
                    if (field.isMine()) {
                        field.setMine(false);
                    }
                    // First turn is over
                    firstTurn = false;
                }
            }
        }
    }
}
