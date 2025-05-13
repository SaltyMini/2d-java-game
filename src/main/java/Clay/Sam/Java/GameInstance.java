package Clay.Sam.Java;


import Clay.Sam.Java.assets.GamePanel;
import javax.swing.*;
import java.awt.*;

public class GameInstance implements Runnable {

    private static GameInstance instance; //Making this a singleton so only one instance ever
    boolean gameRunning = false;

    private int score = 0;

    private GamePanel gamePanel;


    private int paddle1X = 50;
    private static double paddle1Y = 100;
    private int paddle2X = 350;
    private static double paddle2Y = 100;

    private int ballX = 200;
    private int ballY = 200;
    private float ballSpeedX = 5;
    private float ballSpeedY = 5;

    private static double paddle1Velocity = 0;
    private static double paddle2Velocity = 0;
    private static final int paddleVelocityMax = 100;
    private static final double paddleAcceleration = 100;

    private static int paddle1Direction = 0;
    private static int paddle2Direction = 0;

    private static int roundNumber;

    private JFrame frame;



    private GameInstance() {

        frame = Frame.getFrame();

        paddle1X = (int) (frame.getWidth() * 0.1);
        paddle1Y = (int) (frame.getHeight() * 0.5);

        paddle2Y = (int) (frame.getHeight() * 0.5);
        paddle2X = (int) (frame.getWidth() * 0.9);

        roundNumber = 0;
    }

    public static GameInstance getInstance() {
        if (instance == null) {
            instance = new GameInstance();
        }
        return instance;
    }


    public void startGame() {
        gameRunning = true;
        System.out.println("Game started");

        JFrame frame = Frame.getFrame();
        frame.getContentPane().removeAll();

        gamePanel = new GamePanel();


        gamePanel.setBounds(0, 0, frame.getWidth(), frame.getHeight());

        frame.setFocusable(true);
        gamePanel.setFocusable(true);
        gamePanel.requestFocus();

        KeyboardInputs keyboardInputs = new KeyboardInputs();
        keyboardInputs.inputs();

        frame.setLayout(new BorderLayout());
        frame.add(gamePanel, BorderLayout.CENTER);

        frame.setResizable(false);
        frame.revalidate();
        frame.repaint();


        //Start a new thread for game, this runs run()
        Thread gameThread = new Thread(this);
        gameThread.start();
    }

    public void roundStart() {

        ballX = frame.getWidth() / 2;
        ballY = frame.getHeight() / 2;

        ballSpeedX = 1;

    }

    @Override
    public void run() {
        gameRunning = true;

        // Target: 60 FPS = 16.67 milliseconds per frame
        final long targetFrameTime = 1000 / 60;
        long lastUpdateTime = System.currentTimeMillis();

        while (gameRunning) {
            long currentTime = System.currentTimeMillis();
            long elapsedTime = currentTime - lastUpdateTime;

            if (elapsedTime >= targetFrameTime) {
                update(elapsedTime / 1000.0); // Pass elapsed time in seconds
                render();

                lastUpdateTime = currentTime;
            } else {
                // Sleep a bit to avoid CPU hogging
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


        // deltaTime is the time passed since last update in seconds
    private void update(double deltaTime) {

        paddle1Velocity += (paddleAcceleration * paddle1Direction) * deltaTime;

        if (Math.abs(paddle1Velocity) > paddleVelocityMax) {
            paddle1Velocity = Math.signum(paddle1Velocity) * paddleVelocityMax;
        }

        paddle1Y += paddle1Velocity * deltaTime;



        System.out.println("Paddle Y: " + paddle1Y);
        System.out.println("velocity: " + paddle1Velocity);

        paddle1Y = Math.max(0, Math.min(paddle1Y, gamePanel.getHeight() - 100));
        paddle2Y = Math.max(0, Math.min(paddle2Y, gamePanel.getHeight() - 100));

        ballCollision();

    }

    private void render() {
        gamePanel.updatePositions(paddle1X, (int) paddle1Y, paddle2X, (int) paddle2Y, ballX, ballY);
        gamePanel.repaint();
    }


    private void ballCollision() {

        if(ballX < paddle1X && (ballY > paddle1Y && ballY < paddle1Y - gamePanel.getPaddleHeight())) {
            ballSpeedX++;
            ballSpeedX = -ballSpeedX;
        }

        if(ballX > paddle2X && (ballY > paddle2Y && ballY < paddle2Y - gamePanel.getPaddleHeight())) {
            ballSpeedX--;
            ballSpeedX = -ballSpeedX;
        }


    }







    //PADDELS

    public static void movePaddle1(int direction) {

        paddle1Direction = direction;

    }

    public static void movePaddle2(int direction) {
        paddle2Direction = direction;
    }

    public static void stopPanel1() {
        paddle1Direction = 0;
        paddle1Velocity = 0;
    }

    public static void stopPanel2() {
        paddle2Velocity = 0;
        paddle2Direction = 0;
    }

}
