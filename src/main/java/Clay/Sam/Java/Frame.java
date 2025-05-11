package Clay.Sam.Java;

import javax.swing.*;
import java.awt.*;

//this is our window
public class Frame {

    private static JFrame frame;


    public void createFrame() {

        frame = new JFrame("Frame");


        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1280, 720);
        frame.setTitle("Frame");

        frame.getContentPane().setBackground(Color.CYAN);

        frame.setVisible(true);

        startMenu(frame);

    }

    public void startMenu(JFrame frame) {
        frame.setLayout(new BorderLayout(10, 5));

        // Create the button and add it
        JButton startButton = startButton();
        frame.add(startButton, BorderLayout.NORTH);

        frame.revalidate();
    }

    // Method to create and configure the button
    public JButton startButton() {
        JButton start = new JButton("Start");

        start.setForeground(Color.BLUE);
        start.setBackground(Color.LIGHT_GRAY);
        start.setFont(new Font("Arial", Font.BOLD, 14));

        start.addActionListener(e -> {
            System.out.println("Button clicked!");
            GameInstance gameInstance = GameInstance.getInstance();
            gameInstance.startGame();
        });

        return start;
    }

    public static void clearFrame() {
        frame.getContentPane().removeAll();

        // Update the UI
        frame.revalidate();
        frame.repaint();

    }

    public static JFrame getFrame() {
        return frame;
    }


}
