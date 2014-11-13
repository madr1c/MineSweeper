import com.sun.codemodel.internal.JFieldRef;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by alexander on 10/11/14.
 */
public class ControlInterface extends JPanel {

    private MainFrame containingFrame;
    private Timer timer;
    private JLabel timerLabel;
    private JLabel time;
    private JButton resetButton;
    private JButton loadButton;
    private JButton saveButton;
    private int hours;
    private int minutes;
    private int seconds;

    public ControlInterface(final MainFrame containingFrame, final Controller controller){

        this.containingFrame = containingFrame;
        int timerDelay = 1000;
        ActionListener timeActionListener =new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                if(seconds == 59) {
                    seconds = 0;
                    if(minutes == 59) {
                        minutes = 0;
                        if(hours == 99)
                            hours = 0;
                        else
                            hours ++;
                    } else
                        minutes ++;
                } else
                    seconds ++;

                StringBuilder sb = new StringBuilder();
                if( hours < 10 )
                    sb.append("0");
                sb.append(hours);
                sb.append(":");
                if( minutes < 10 )
                    sb.append("0");
                sb.append(minutes);
                sb.append(":");
                if( seconds < 10 )
                    sb.append("0");
                sb.append(seconds);
                time.setText(sb.toString());
            }
        };

        timer = new Timer(timerDelay, timeActionListener);
        timerLabel = new JLabel("Time:");
        time = new JLabel("Time goes here!");

        resetButton = new JButton("Reset");
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                controller.resetGame();
            }
        });
        loadButton = new JButton("Load");
        saveButton = new JButton("Save");
        this.initPanel();
    }

    private void initPanel(){
        JPanel timerPanel = new JPanel(new GridLayout(1, 2));
        timerPanel.add(timerLabel);
        timerPanel.add(time);
        this.add(timerPanel);
        this.add(resetButton);
        this.add(loadButton);
        this.add(saveButton);
    }

    public void resetTimer() {

        if(timer.isRunning())
            timer.stop();
        timer.restart();

        hours = 0;
        minutes = 0;
        seconds = 0;

        StringBuilder sb = new StringBuilder();
        sb.append(hours);
        sb.append(minutes);
        sb.append(seconds);

        time.setText(sb.toString());
    }

    public void startTimer(int[] initialTime) {

        stopTimer();

        hours = initialTime[0];
        minutes = initialTime[1];
        seconds = initialTime[2];

        timer.setInitialDelay(0);

        timer.start();
    }

    public void stopTimer() {

        if (timer.isRunning())
            timer.stop();
    }

    public void resumeTimer() {

        if(!timer.isRunning())
            timer.start();
    }

    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getSeconds() {
        return seconds;
    }
}
