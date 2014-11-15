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

package view;

import model.Field;
import model.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.Serializable;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Mark Alexander Dietrich on 06/11/14.
 *
 * File: MinePanel.java
 *
 * Class: MainPanel
 * This {@link javax.swing.JPanel} represents a minefield with lots of {@link javax.swing.JButton}s. Every
 * {@link javax.swing.JButton} references a {@link model.Field}. Every {@link model.Field} belongs to a
 * {@link model.Game}. This {@link view.MinePanel} implements {@link java.io.Serializable} which means you can use a
 * {@link java.io.ObjectOutputStream} to save it in a file.
 *
 * @author Mark Alexander Dietrich
 *
 * @see javax.swing.JPanel
 * @see java.io.Serializable
 *
 */
public class MinePanel extends JPanel implements Serializable {

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
     * Matrix which contains {@link javax.swing.JButton}s. Each of them represents a {@link model.Field} of the
     * {@link model.Game} below.
     */
    JButton[][] buttons;

    /**
     * {@link model.Game} containing the fields the buttons belong to.
     */
    private Game game;

    /**
     * Resolution of your screen.
     */
    private Dimension screenSize;

    /**
     * Size of the {@link javax.swing.JButton}s.
     */
    private Dimension buttonSize;

    /**
     * Class constructor.
     *
     * @param screenSize        {@link java.awt.Dimension} which represents the resolution of your screen.
     *
     * @see java.awt.Dimension
     */
    public MinePanel(Dimension screenSize) {

        // Set layout
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.screenSize = screenSize;

    }

    /**
     * Checks if the given {@link java.lang.String} is numeric.
     *
     * @param str       {@link java.lang.String} to check.
     * @return          {@link boolean} is the given {@link java.lang.String} numeric?
     *
     * @see java.lang.String
     * @see java.lang.Number
     */
    public static boolean isNumeric(String str) {
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    /**
     * Calculates the size of the {@link javax.swing.JButton}s and the {@link java.awt.Font} based on the
     * {@code screensize}. Then it iterates trough the {@link model.Field}s of the given {@link model.Game} and adds
     * {@link view.MinePanel.ObserverButton}s to them.
     *
     * @param game      {@link model.Game} to get the {@link model.Field}s from.
     *
     * @see view.MinePanel.ObserverButton
     * @see model.Game
     * @see model.Field
     */
    public void init(final Game game) {
        this.game = game;

        // Initialize buttons matrix
        this.buttons = new JButton[this.game.getHeight()][this.game.getWidth()];

        Dimension tmpDim = this.screenSize;

        // Calculate buttonSize
        this.buttonSize = new Dimension((int) tmpDim.getWidth() / this.game.getHeight(),
                (int) tmpDim.getWidth() / this.game.getWidth());

        // Calculate fontSize
        int fontSize = (int) (this.buttonSize.getHeight() / 2);
        Font font = new Font("serif", Font.BOLD, fontSize);

        // Iterate trough the {@link model.Field}s of the given {@link model.Game} and connect the
        // {@link view.MinePanel.ObserverButton}s
        for (Field[] row : game.getFields()) {

            JPanel panel = new JPanel(new GridLayout());

            for (Field current : row) {

                // Get coordinates of the current {@link model.Field}
                int x = current.getX();
                int y = current.getY();

                // Initialize new {@link view.MinePanel.ObserverButton}
                final ObserverButton tmp = new ObserverButton(this.getInitialButtonCaption(x, y));
                tmp.setPreferredSize(this.buttonSize);
                tmp.setFont(font);

                // Fit caption of the {@link view.MinePanel.ObserverButton} the state of the current {@link model.Field}
                this.fitButtonCaption(tmp);

                // Add a {@link java.awt.event.MouseListener} to the {@link view.MinePanel.ObserverButton}
                this.initMouseListener(tmp, x, y);

                // Add {@link view.MinePanel.ObserverButton} to the current {@link model.Field}
                current.addObserver(tmp);

                // Add {@link view.MinePanel.ObserverButton} to the matrix
                this.buttons[x][y] = tmp;

                // Add the {@link view.MinePanel.ObserverButton} to the current {@link java.awt.GridBagLayout}
                panel.add(tmp);
            }
            // Add the {@link java.awt.GridBagLayout} to the {@link java.awt.BoxLayout} of this {@link view.MinePanel}
            this.add(panel);
        }
    }

    /**
     * Initializes the {@link java.awt.event.MouseListener} which should handle the {@link java.awt.event.MouseEvent}
     * of the given {@link view.MinePanel.ObserverButton}.
     *
     * @param button        {@link view.MinePanel.ObserverButton} to add the {@link java.awt.event.MouseListener} to.
     * @param x             {@link int} x coordinate of the {@link model.Field} the given
     *                      {@link view.MinePanel.ObserverButton} observes.
     * @param y             {@link int} y coordinate of the {@link model.Field} the given
     *                      {@link view.MinePanel.ObserverButton} observes.
     *
     * @see java.awt.event.MouseListener
     */
    public void initMouseListener(ObserverButton button, final int x, final int y) {
        button.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                // When the user used the right mousebutton the clicked {@link model.Field} will be marked
                if (SwingUtilities.isRightMouseButton(mouseEvent)) {
                    game.markField(x, y);
                // When the user used the left mousebutton the clicked {@link model.Field} will be opened
                } else if (SwingUtilities.isLeftMouseButton(mouseEvent)) {
                    game.openField(x, y);
                }
            }
            // Unused dummy event
            @Override
            public void mousePressed(MouseEvent mouseEvent) {

            }
            // Unused dummy event
            @Override
            public void mouseReleased(MouseEvent mouseEvent) {

            }
            // Unused dummy event
            @Override
            public void mouseEntered(MouseEvent mouseEvent) {

            }
            // Unused dummy event
            @Override
            public void mouseExited(MouseEvent mouseEvent) {

            }
        });
    }

    /**
     * This method is important when you use an {@link java.io.ObjectInputStream} to load a saved {@link model.Game}.
     * It uses the given coordinates to find the right {@link model.Field} and return the right caption {@link String}
     * depending on the state of the found {@link model.Field}.
     *
     * @param x         {@link int} x coordinate of the {@link model.Field} to find.
     * @param y         {@link int} y coordinate of the {@link model.Field} to find.
     * @return          {@link java.lang.String} which represents a button caption.
     *
     * @see model.Field
     * @see java.lang.String
     */
    public String getInitialButtonCaption(int x, int y) {

        // Get {@link model.Field} that fits the given coordinates
        Field field = this.game.getField(x, y);

        // Detect right caption {@link java.lang.String}
        if (field.isMarked())
            return "!";
        else if (!field.isOpen())
            return "?";
        else {
            if (0 == field.getAmountNeighborMines())
                return " ";
            else
                return Integer.toString(field.getAmountNeighborMines());
        }
    }

    /**
     * Converts the {@link java.awt.Image} behind the given {@link java.net.URL} to an {@link javax.swing.ImageIcon}
     * which is a swing element which you can use as caption for a {@link javax.swing.JButton}.
     *
     * @param url           {@link java.net.URL} path to the {@link java.awt.Image} to convert.
     * @return              {@link javax.swing.Icon} to use for your {@link view.MinePanel.ObserverButton}.
     *
     * @see java.net.URL
     * @see java.awt.Image
     * @see javax.swing.ImageIcon
     * @see javax.swing.Icon
     */
    private Icon renderCaptionPic(URL url) {

        Image img = new ImageIcon(url).getImage();
        Image newImg = img.getScaledInstance((int) this.buttonSize.getHeight(),
                (int) this.buttonSize.getWidth(), java.awt.Image.SCALE_SMOOTH);
        return new ImageIcon(newImg);
    }

    /**
     * Select right {@link javax.swing.Icon} for the given {@link javax.swing.JButton} depending on its name.
     *
     * @param btn       {@link javax.swing.JButton} to select the right {@link javax.swing.Icon} for.
     */
    private void fitButtonCaption(JButton btn) {

        if ("boom".equals(btn.getName())) {
            URL url = this.getClass().getResource("/" + "mine.png");
            btn.setIcon(this.renderCaptionPic(url));
        } else if ("?".equals(btn.getName())) {
            URL url = this.getClass().getResource("/" + "questionmark.png");
            btn.setIcon(this.renderCaptionPic(url));
        } else if ("!".equals(btn.getName())) {
            URL url = this.getClass().getResource("/" + "flag.png");
            btn.setIcon(this.renderCaptionPic(url));
        // If the name is numeric no {@link javax.swing.Icon} is required
        } else if (isNumeric(btn.getName())) {
            btn.setText(btn.getName());
            this.setColor(btn);
            btn.setIcon(null);
        } else {
            btn.setIcon(null);
        }
    }

    /**
     * Selects the right foreground {@link java.awt.Color} for the given {@link javax.swing.JButton} depending on the
     * caption {@link String}.
     *
     * @param btn           {@link javax.swing.JButton} to set the foreground
     */
    public void setColor(JButton btn){

        if (isNumeric(btn.getName())) {
            if ("1".equals(btn.getName()))
                btn.setForeground(Color.BLUE);
            else if("2".equals(btn.getName()))
                btn.setForeground(Color.GREEN);
            else if("3".equals(btn.getName()))
                btn.setForeground(Color.RED);
            else if("4".equals(btn.getName()))
                btn.setForeground(Color.MAGENTA);
            else if("5".equals(btn.getName()))
                btn.setForeground(Color.DARK_GRAY);
            else if("6".equals(btn.getName()))
                btn.setForeground(Color.GRAY);
            else if("7".equals(btn.getName()))
                btn.setForeground(Color.PINK);
            else if("8".equals(btn.getName()))
                btn.setForeground(Color.BLACK);
        }
    }

    public Game getGame() {
        return game;
    }

    /**
     * Removes all {@link java.awt.Component}s from this {@link javax.swing.JPanel}.
     */
    public void clear() {
        this.removeAll();
        this.repaint();
    }

    /**
     * Class: ObserverButton
     * Implements an {@link java.util.Observer} in terms of a {@link javax.swing.JButton} which should observe a
     * {@link model.Field}.
     *
     * @see java.util.Observer
     * @see javax.swing.JButton
     */
    class ObserverButton extends JButton implements Observer {

        /**
         * Class constructor.
         *
         * @param string        {@link String} which represents the name of the new {@link javax.swing.JButton}.
         */
        public ObserverButton(String string) {
            super(string);
            this.setName(this.getText());
            this.setText("");
        }

        /**
         * Update method of this observer. If the referencing {@link model.Field} changes it's state the
         * {@link view.MinePanel.ObserverButton} does it too.
         *
         * @param arg0          {@link java.util.Observable} observed observable object.
         * @param arg1          {@link java.lang.Object} optional parameter.
         *
         * @see java.util.Observer
         * @see java.util.Observable
         * @see java.lang.Object
         * @see model.Field
         */
        public void update(Observable arg0, Object arg1) {
            Field field = (Field) arg0;

            // Is referenced {@link model.Field} exploded?
            if(null == arg1) {
                // If {@link model.Field#isExploded} is true you lost the game
                if (field.isExploded()) {

                    // All {@link model.Field}s will be flipped
                    this.flipAllFields(false);

                    // Set {@link model.Game} state
                    game.setWon(false);
                    game.setEnded(true);

                // If {@link view.MinePanel.ObserverButton#checkVictory} is true you won the game
                } else if (this.checkVictory()) {

                    // All {@link model.Field}s will be flipped
                    this.flipAllFields(true);

                    // Set {@link model.Game} state
                    game.setWon(true);
                    game.setEnded(true);
                // If you did not won or lost the game either the referencing {@link model.Field} will just be flipped
                } else
                    // Flip {@link model.Field}
                    this.flipField(field);
            }
        }

        /**
         * Iterates trough each {@link model.Field} and checks if it's state. If all {@link model.Field}s pass this test
         * it will return true.
         *
         * @return          {@link} have you won the {@link model.Game}?
         *
         * @see model.Field#isMarked
         * @see model.Field#isOpen
         * @see model.Field#isExploded
         */
        private boolean checkVictory() {
            for (Field[] row : game.getFields()) {
                for (Field current : row) {
                    // If {@link model.Field#isMarked} and {@link model.Field#isOpen} return false or
                    // {@link model.Field#isExploded} returns true you haven't won the game and this method will return false
                    if (!current.isMarked() && !current.isOpen() || current.isExploded())
                        return false;
                }
            }
            return true;
        }

        /**
         * Iterates trough each {@link model.Field} of the current {@link model.Game} and flips it by using
         * {@link view.MinePanel.ObserverButton#flipField}. It will be used when you won or lost the {@link model.Game}.
         *
         * @param won           {@link boolean} have you won the {@link model.Game}?
         *
         * @see view.MinePanel.ObserverButton#flipField
         */
        private void flipAllFields(boolean won) {

            for (Field[] row : game.getFields()) {
                for (Field current : row) {
                    // If you won the unmarked {@link model.Field}s will not be flipped
                    if (!won)
                        current.unmark();
                    if (!current.isMarked())
                        current.setOpen(true);
                    // Delete {@link java.util.Observer}
                    current.deleteObservers();
                    // Flip the current {@link model.Field}
                    this.flipField(current);
                }
            }
        }

        /**
         * Flips the given {@link model.Field}.
         *
         * @param field         {@link model.Field} to flip.
         *
         * @see model.Field
         */
        private void flipField(Field field) {

            // Find referencing {@link view.MinePanel.ObserverButton}
            JButton btn = buttons[field.getX()][field.getY()];

            // Select right caption {@link java.lang.String}
            if (field.isExploded()) {
                btn.setName("boom");
            } else if (field.isMarked()) {
                btn.setName("!");
            } else if (field.isOpen()) {
                if (field.getAmountNeighborMines() > 0) {
                    btn.setName(Integer.toString(field.getAmountNeighborMines()));
                } else {
                    btn.setName("  ");
                }
            } else {
                btn.setName("?");
            }


            fitButtonCaption(btn);

            // If there is no {@link javax.swing.Icon} show caption {@link java.lang.String}
            if (null == btn.getIcon()) {
                btn.setText(btn.getName());
                setColor(btn);
            }
        }


    }

}

