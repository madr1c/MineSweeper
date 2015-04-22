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

package controller;

import model.Game;
import view.ControlInterface;
import view.MainFrame;
import view.MinePanel;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Mark Alexander Dietrich on 06/11/14.
 *
 * File: Controller.java
 *
 * Class: Controller
 * Controls the whole application.
 *
 * @author Mark Alexander Dietrich
 */
public class Controller {

    /**
     * Main {@link javax.swing.JFrame} of this application
     */
    private MainFrame containingFrame;

    /**
     * This {@link controller.Controller}. Necessary because the {@link controller.Controller.GameObserver} needs to
     * access it.
     */
    private Controller thisController;

    /**
     * Control panel of this application.
     */
    private ControlInterface controlInterface;

    /**
     * Pitch of this application.
     */
    private MinePanel mineField;

    /**
     * The {@link model.Game} behind the pitch.
     */
    private Game game;

    /**
     * Class constructor.
     *
     * @param containingFrame       {@link view.MainFrame} of this application.
     * @param mineField             {@link view.MinePanel} as pitch of this application.
     * @param controlInterface      {@link view.ControlInterface} as control panel of this application.
     * @param height                {@link int} Height of the pitch.
     * @param width                 {@link int} Width of the pitch.
     * @param pMine                 {@link double} Represents the probability that a {@link model.Field} is a mine.
     */
    public Controller(MainFrame containingFrame, MinePanel mineField,
                      ControlInterface controlInterface, int height, int width, double pMine) {

        // Initialize parameters
        this.containingFrame = containingFrame;
        this.mineField = mineField;
        this.controlInterface = controlInterface;
        this.thisController = this;

        // Create new {@link model.Game}
        this.newGame(height, width, pMine);
    }

    /**
     * Sets up a new {@link model.Game}.
     *
     * @param height        {@link int} Height of the pitch.
     * @param width         {@link int} Width of the pitch.
     * @param pMine         {@link double} Represents the probability that a {@link model.Field} is a mine.
     */
    public void newGame(int height, int width, double pMine) {
        this.game = new Game(height, width, pMine);
        this.game.addObserver(new GameObserver());
        this.mineField.init(this.game);
    }

    /**
     * Resumes the game clock of the {@link view.ControlInterface}.
     */
    public void resumeGame() {
        this.controlInterface.resumeTimer();
    }

    /**
     * Pauses the game clock of the {@link view.ControlInterface}.
     */
    public void pauseGame() {
        this.controlInterface.stopTimer();
    }

    /**
     * Starts the game clock of the {@link view.ControlInterface}.
     */
    public void startGame() {
        this.controlInterface.startTimer(this.game.getInitialTime());
    }

    /**
     * Reinitializes the current {@link model.Game} and starts the game clock of the {@link view.ControlInterface}.
     */
    public void resetGame() {
        this.controlInterface.stopTimer();
        this.mineField.clear();

        // Reinitialize {@link model.Game}
        this.game.reset();
        this.mineField.init(this.game);

        // Start game clock
        this.controlInterface.startTimer(this.game.getInitialTime());
    }

    /**
     * Initializes a new {@link model.Game} with the given parameters.
     *
     * @param fieldSize         {@link int} size of new pitch.
     * @param pMine             {@link double} representing the probability that a {@link model.Field} of the new
     *                          {@link model.Game} will be an mine.
     *
     * @see model.Field
     * @see model.Game
     */
    public void changeGamePorperties(int fieldSize, double pMine){

            // Calculates height of the new pitch
            fieldSize = (int) Math.sqrt((double) fieldSize);

            this.controlInterface.stopTimer();
            this.mineField.clear();

            this.newGame(fieldSize, fieldSize, pMine);

            this.controlInterface.startTimer(this.game.getInitialTime());
    }

    /**
     * Loads a saved {@link model.Game} by using an {@link java.io.ObjectInputStream}.
     *
     * @see java.io.ObjectInputStream
     * @see javax.swing.JFileChooser
     */
    public void loadGame() {
        this.pauseGame();
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {

            try {
                File f = chooser.getSelectedFile();
                FileInputStream fis;
                fis = new FileInputStream(f);
                ObjectInputStream ois = new ObjectInputStream(fis);

                this.mineField = (MinePanel) ois.readObject();

                ois.close();
                fis.close();
                this.game = this.mineField.getGame();
                this.game.addObserver(new GameObserver());
                this.mineField.clear();
                this.mineField.init(this.game);
                this.containingFrame.clear();
                this.containingFrame.setMineField(this.mineField);
                this.containingFrame.initComponents();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        this.startGame();

    }

    /**
     * Saves the current {@link model.Game} by using an {@link java.io.ObjectOutputStream}.
     *
     * @see java.io.ObjectOutputStream
     * @see javax.swing.JFileChooser
     */
    public void saveGame() {
        this.pauseGame();
        this.game.setInitialTime(this.controlInterface.getTime());
        JFileChooser chooser = new JFileChooser();
        if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {

            try {
                File f = chooser.getSelectedFile();
                FileOutputStream fos;
                fos = new FileOutputStream(f);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(this.mineField);
                oos.close();
                fos.close();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        this.resumeGame();
    }

    public Game getGame() {
        return game;
    }

    /**
     * Class: GameObserver
     * Implements an {@link java.util.Observer} which should observe a {@link model.Game}. If it is the first turn of
     * the {@link model.Game} it makes you invulnerable against mines.
     *
     * @see java.util.Observer
     * @see model.Game
     */
    class GameObserver implements Observer {

        /**
         * Class constructor.
         */
        public GameObserver() {
            super();
        }

        /**
         * Update method of this observer. Observes a {@link model.Game}. If its over is shows you a
         * {@link javax.swing.JOptionPane} which asks the user if he wants to play agian.
         *
         * @param arg0          {@link java.util.Observable} observed observable object.
         * @param arg1          {@link java.lang.Object} optional parameter.
         *
         * @see java.util.Observer
         * @see java.util.Observable
         * @see java.lang.Object
         * @see model.Game
         * @see javax.swing.JOptionPane
         */
        public void update(Observable arg0, Object arg1) {
            Game game = (Game) arg0;
            int reply;

            if (game.isEnded()) {

                // Stop current {@link model.Game}
                thisController.pauseGame();

                if (!game.isWon()) {
                    URL url = this.getClass().getResource("/" + "defeat.png");
                    ImageIcon icon = new ImageIcon(url);
                    icon.setImage(icon.getImage().getScaledInstance(80, 80, Image.SCALE_DEFAULT));
                    reply = JOptionPane.showConfirmDialog(null, "Try again?", "You lose", JOptionPane.YES_NO_OPTION, 2, icon);

                } else {
                    URL url = this.getClass().getResource("/" + "defeat.png");
                    ImageIcon icon = new ImageIcon(url);
                    icon.setImage(icon.getImage().getScaledInstance(80, 80, Image.SCALE_DEFAULT));
                    reply = JOptionPane.showConfirmDialog(null, "Play again?", "You win", JOptionPane.YES_NO_OPTION, 2, icon);
                }

                // If user wants to play a new {@link model.Game} it will reset
                if (reply == JOptionPane.YES_OPTION) {
                    thisController.resetGame();
                    Window[] windows = Window.getWindows();
                    for (Window window : windows) {
                        if (window instanceof MainFrame) {
                            MainFrame mainFrame = (MainFrame) window;
                            if (mainFrame.getContentPane().getComponentCount() == 1
                                    && mainFrame.getContentPane().getComponent(0) instanceof JOptionPane) {
                                mainFrame.dispose();
                            }
                        }
                    }
                // If the user does not wants to play a new {@link model.Game} the application will close.
                } else {
                    Window[] windows = Window.getWindows();
                    for (Window window : windows) {
                        if (window instanceof MainFrame) {
                            window.dispose();
                        }
                    }
                }
            }
        }

    }

}
