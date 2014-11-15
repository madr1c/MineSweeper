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

/**
 * Created by Mark Alexander Dietrich on 06/11/14.
 *
 * File: MainFrame.java
 *
 * Class: MainFrame
 * It is the main {@link javax.swing.JFrame} of Minesweeper, which should contain the {@link view.MinePanel} and the
 * {@link view.ControlInterface}.
 *
 *
 * @author Mark Alexander Dietrich
 *
 * @see javax.swing.JFrame
 * @see view.MinePanel
 * @see view.ControlInterface
 */
public class MainFrame extends JFrame {

    /**
     * The {@link view.MinePanel} represents the minefield.
     */
    private MinePanel mineField;

    /**
     * The {@link view.ControlInterface} allow the user to control the game.
     */
    private ControlInterface controlInterface;

    /**
     * Class constructor.
     *
     * @param height        {@link int} Height of the minefield.
     * @param width         {@link int} Width of the minefield.
     * @param pMine         {@link double} Represents the probability that a field is a mine.
     */
    public MainFrame(int height, int width, double pMine) {

        Controller controller;
        Dimension screenSize;

        // Calculate screensize
        screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        screenSize.setSize(screenSize.getHeight() -200, screenSize.getHeight() - 400);

        // Create components
        this.mineField = new MinePanel(screenSize);
        this.controlInterface = new ControlInterface();
        controller = new Controller(this, this.mineField, this.controlInterface, height, width, pMine);

        // Initialize {@link controller.Controller}
        this.controlInterface.initComponents(controller);

        // Initialize GUI elements
        this.initComponents();

        // Start game
        controller.startGame();

    }

    /**
     * Initializes GUI elements.
     */
    public void initComponents() {
        // Customize FrameLayout
        this.setTitle("Minesweeper");
        this.setLayout(new BorderLayout());

        // Add GUI elements
        this.add(this.controlInterface, BorderLayout.SOUTH);
        this.add(this.mineField, BorderLayout.CENTER);

        // Places and resizes the {@link javax.swing.JFrame}
        this.pack();
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.repaint();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Remove all {@link java.awt.Component}s from this {@link javax.swing.JFrame}.
     */
    public void clear() {
        this.getContentPane().removeAll();
    }

    public void setMineField(MinePanel mineField) {
        this.mineField = mineField;
    }

}
