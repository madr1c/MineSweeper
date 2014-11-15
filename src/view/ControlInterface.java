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

import controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Mark Alexander Dietrich on 06/11/14.
 *
 * File: ControlInterface.java
 *
 * Class: ControlInterface
 * This {@link javax.swing.JPanel} represents the control panel of minesweeper. It contains a game clock based on a
 * {@link javax.swing.Timer} and several {@link javax.swing.JButton}s which allow you to reset, save or load the
 * {@link model.Game} or change its properties.
 *
 * @author Mark Alexander Dietrich
 *
 * @see javax.swing.JPanel
 * @see javax.swing.JButton
 * @see model.Game
 * @see javax.swing.Timer
 *
 */
public class ControlInterface extends JPanel {

    /**
     * Game clock.
     */
    private Timer timer;
    private JLabel timerLabel;

    /**
     * {@link javax.swing.JLabel} which shows the {@link model.Game} time.
     */
    private JLabel time;

    // Buttons
    private JButton resetButton;
    private JButton loadButton;
    private JButton saveButton;
    private JButton propertyButton;

    /**
     * {@link javax.swing.Timer} parameters.
     */
    private int hours;
    private int minutes;
    private int seconds;

    /**
     * Class constructor.
     *
     * You have to initialize its {@link java.awt.Component}s otherwise it is only an empty {@link javax.swing.JPanel}.
     *
     * @see view.ControlInterface#initComponents
     */
    public ControlInterface() {}

    /**
     * Initializes {@link java.awt.Component}s to be added to this {@link javax.swing.JPanel}.
     *
     * @param controller            {@link controller.Controller} to work with.
     */
    public void initComponents(final Controller controller) {

        // Init {@link javax.swing.Timer} logic
        // Every second it should trigger the following {@link java.awt.event.ActionListener}
        int timerDelay = 1000;

        // What to do every second
        ActionListener timeActionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                // Converts a second at the current time
                if (seconds == 59) {
                    seconds = 0;
                    if (minutes == 59) {
                        minutes = 0;
                        if (hours == 99)
                            hours = 0;
                        else
                            hours++;
                    } else
                        minutes++;
                } else
                    seconds++;

                // Build {@link java.lang.String} to show
                StringBuilder sb = new StringBuilder();
                if (hours < 10)
                    sb.append("0");
                sb.append(hours);
                sb.append(":");
                if (minutes < 10)
                    sb.append("0");
                sb.append(minutes);
                sb.append(":");
                if (seconds < 10)
                    sb.append("0");
                sb.append(seconds);
                time.setText(sb.toString());
            }
        };

        // Initialize game clock
        timer = new Timer(timerDelay, timeActionListener);
        timerLabel = new JLabel("Time:");
        time = new JLabel("Time goes here!");

        // Initialize control {@link javax.swing.JButton}s
        resetButton = new JButton("Reset");
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                // Reset {@link model.Game}
                controller.resetGame();
            }
        });
        loadButton = new JButton("Load");
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                // Load saved {@link model.Game}
                controller.loadGame();
            }
        });
        saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                // Saved current {@link model.Game}
                controller.saveGame();
            }
        });

        propertyButton = new JButton("Properties");
        // Prepare {@link javax.swing.JOptionPane}s which allow you to change the {@link model.Game} properties
        propertyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                final String[] size = { "49", "64", "81", "100", "121", "144", "169", "196" };
                final String[] pMine = {"10%", "15%", "25%", "40%", "60%", "85%"};

                String gameSize = (String) JOptionPane.showInputDialog(null,
                        "How many fields should the new game contain?",
                        "Select game size!",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        size,
                        size[0]);

                String probabilityMine = (String) JOptionPane.showInputDialog(null,
                        "How big should be the chance that you will hit a mine?",
                        "Select mine probability!",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        pMine,
                        pMine[0]);

                // Calculate size of the new pitch
                int fieldSize = (int) Math.pow(controller.getGame().getHeight(), 2);

                if(null != gameSize)
                   fieldSize = Integer.parseInt(gameSize);

                // Calculate probability to hit a mine for the new {@link model.GAme}
                double probability = controller.getGame().getpMine();

                if (null != probabilityMine) {
                    probabilityMine = cropString(probabilityMine);
                    probability = (double) Integer.parseInt(probabilityMine) / 100;
                }

                // Replace properties of the {@link model.Game} with the entered ones
                controller.changeGamePorperties(fieldSize, probability);
            }
        });

        // Add initialized {@link java.awt.Component}s to this {@link javax.swing.JPanel}
        this.initPanel();
    }

    /**
     * Crop the {@link java.lang.String} entered in the {@link javax.swing.JOptionPane} that you can calculate with it.
     *
     * @param param         {@link java.lang.String} to crop.
     * @return              Cropped {@link java.lang.String}.
     */
    public String cropString(String param) {
        // Return everything before the "%"
        return param.substring(0,param.indexOf('%'));
    }

    /**
     * Adds necessary {@link java.awt.Component}s to this {@link javax.swing.JPanel}.
     */
    private void initPanel() {
        JPanel timerPanel = new JPanel(new GridLayout(1, 2));
        timerPanel.add(timerLabel);
        timerPanel.add(time);
        this.add(timerPanel);
        this.add(resetButton);
        this.add(loadButton);
        this.add(saveButton);
        this.add(propertyButton);
    }

    /**
     * Sets the {@link javax.swing.Timer} parameters depending on the given initialTIme and starts the game clock.
     *
     * @param initialTime       {@link int[]} containing the start parameters for the {@link javax.swing.Timer}.
     */
    public void startTimer(int[] initialTime) {

        // Should avoid errors
        if (timer.isRunning())
            stopTimer();

        // Set {@link javax.swing.Timer} parameters
        hours = initialTime[0];
        minutes = initialTime[1];
        seconds = initialTime[2];

        // Set delay
        timer.setInitialDelay(0);

        // Start {@link javax.swing.Timer}
        timer.start();
    }

    /**
     * Stops the game clock.
     */
    public void stopTimer() {

        if (timer.isRunning())
            // Stop {@link javax.swing.Timer}
            timer.stop();
    }

    /**
     * Resumes the game clock.
     */
    public void resumeTimer() {

        if (!timer.isRunning())
            // Start {@link javax.swing.Timer}
            timer.start();
    }

    public int[] getTime() {
        return new int[]{this.hours, this.minutes, this.seconds};
    }
}
