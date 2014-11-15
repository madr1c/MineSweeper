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
import java.util.ArrayList;
import java.util.Observable;

/**
 * Created by Mark Alexander Dietrich on 06/11/14.
 * <p/>
 * File: Field.java
 * <p/>
 * Class: Field
 * Represents a part of the pitch of a {@link model.Game}.
 *
 * @author Mark Alexander Dietrich
 * @see java.util.Observable
 * @see java.io.Serializable
 */
public class Field extends Observable implements Serializable {

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

    // Coordinates
    private int x;
    private int y;

    // Is this {@link model.Field} a mine?
    private boolean mine;

    // Is this {@link model.Field} opened?
    private boolean open;

    // Is this {@link model.Field} opened?
    private boolean marked;

    // {@link model.Field}s which border on this {@link model.Field}
    private ArrayList<Field> neighbors;

    /**
     * Class constructor.
     *
     * @param x      {@link int} x coordinate
     * @param y      {@link int} y coordinate
     * @param isMine {@link boolean} is this {@link model.Field} a mine?
     */
    public Field(int x, int y, boolean isMine) {
        this.mine = isMine;
        this.open = false;
        this.marked = false;
        this.x = x;
        this.y = y;
        neighbors = new ArrayList<Field>();
    }

    /**
     * Adds a bordering {@link model.Field} to the list of neighbours.
     *
     * @param neighbor {@link model.Field} that borders to this one.
     */
    public void addNeighbor(Field neighbor) {
        neighbors.add(neighbor);
    }

    /**
     * Tells you how many mines border to this {@link model.Field}.
     *
     * @return {@link int} how many mines border on this {@link model.Field}?
     */
    public int getAmountNeighborMines() {
        int amountMines = 0;
        for (Field neighbor : neighbors) {
            if (neighbor.isMine()) {
                amountMines++;
            }
        }
        return amountMines;
    }

    /**
     * Opens this {@link model.Field} if it wasn't already opened or marked.
     */
    public void open() {
        if (!isOpen() && !isMarked()) {
            setChanged();
            notifyObservers(new Object());
            setOpen(true);
            if (!isMine()) {
                // If no mines border to this {@link model.Field} all neighbours will be opened too
                if (0 == getAmountNeighborMines()) {
                    for (Field neighbor : neighbors) {
                        neighbor.open();
                    }
                }
            }
            setChanged();
            notifyObservers();
        }
    }

    public void unmark() {
        this.marked = false;
    }

    public boolean isExploded() {
        return isOpen() && isMine();
    }

    public boolean isMarked() {
        return marked;
    }

    public void setMarked(boolean marked) {
        this.marked = marked;
        // Notifies {@link java.util.Observer} because maybe it was the last {@link model.Field} of the
        // {@link model.Game} which means it is over
        setChanged();
        notifyObservers();
    }

    public boolean isMine() {
        return mine;
    }

    public void setMine(boolean mine) {
        this.mine = mine;
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
