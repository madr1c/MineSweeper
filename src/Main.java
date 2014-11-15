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

import view.MainFrame;

/**
 * Created by Mark Alexander Dietrich on 06/11/14.
 *
 * File: Main.java
 *
 * Class: Main
 * The main class.
 *
 * @author Mark Alexander Dietrich
 */
public class Main {

    /**
     * The main method which starts the application.
     *
     * @param args {@link String[]} which contains the following commands after JVM invokes after this method.
     */
    public static void main(String[] args) {

        MainFrame mainFrame = new MainFrame(10, 10, 0.25);

        mainFrame.setVisible(true);

    }
}
