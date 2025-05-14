package Clay.Sam.Java;


import Clay.Sam.Java.assets.GamePanel;
import javax.swing.*;
import java.awt.*;

public class GameInstance implements Runnable {

    private static GameInstance instance; //Making this a singleton so only one instance ever
    boolean gameRunning = false;
    boolean gamePaused = false;

    private int score = 0;

    private GamePanel gamePanel;


    private int paddle1X = 50;
    private static double paddle1Y = 100;
    private int paddle2X = 350;
    private static double paddle2Y = 100;

    private int ballX = 200;
    private int ballY = 200;
    private float ballVelocity = 300;
    private float ballAngleRadians = 5;

    private static double paddle1Velocity = 0;
    private static double paddle2Velocity = 0;
    private static final int paddleVelocityMax = 250;
    private static final double paddleAcceleration = 100;

    private static int paddle1Direction = 0;
    private static int paddle2Direction = 0;

    private static int roundNumber;

    private JFrame frame;


    public static GameInstance getInstance() {
        if (instance == null) {
            instance = new GameInstance();
        }
        return instance;
    }

    private GameInstance() {

        gameRunning = true;

        frame = Frame.getFrame();
        gamePanel = new GamePanel();

        frame.getContentPane().removeAll();
        gamePanel.setBounds(0, 0, frame.getWidth(), frame.getHeight());

        frame.setFocusable(true);
        gamePanel.setFocusable(true);
        gamePanel.requestFocus();

        paddle1X = (int) (frame.getWidth() * 0.1);
        paddle1Y = (int) (frame.getHeight() * 0.5);

        paddle2Y = (int) (frame.getHeight() * 0.5);
        paddle2X = (int) (frame.getWidth() * 0.9);


        KeyboardInputs keyboardInputs = new KeyboardInputs(this);
        keyboardInputs.inputs();

        frame.setLayout(new BorderLayout());
        frame.add(gamePanel, BorderLayout.CENTER);

        frame.setResizable(false);
        frame.revalidate();
        frame.repaint();


        //TODO: round management
        roundNumber = 0;


        //Start a new thread for game, this runs run()
        Thread gameThread = new Thread(this);
        gameThread.start();
        System.out.println("creating gamethread: " + gameThread.getName());
    }

    //TODO: refracted this into instance constructor... "private GameInstance()"
    private void startRound() {

        ballX = frame.getWidth() / 2;
        ballY = frame.getHeight() / 2;

        ballVelocity = 1;

    }

    private void endRound() {

        gamePaused = true;

        /// /TODO implement
        //       <team> wins
        // Press any key to continue

    }

    private void gameOver() {

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
                //System.out.println("Tick time " + elapsedTime / 1000);
                update(elapsedTime / 1000.0); // Pass elapsed time in seconds
                render();

                lastUpdateTime = currentTime;
            } else {
                try {
                // Sleep a bit to avoid CPU hogging
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


        // deltaTime is the time passed since last update in seconds
    private void update(double deltaTime) {

        //System.out.println("DeltaTime: " + deltaTime);

        if (paddle1Direction != 0) {
            paddle1Velocity += (paddleAcceleration * paddle1Direction) * deltaTime;

            if (Math.abs(paddle1Velocity) > paddleVelocityMax) {
                paddle1Velocity = Math.signum(paddle1Velocity) * paddleVelocityMax;
            }
        } else {
            paddle1Velocity *= 0.9; // 0.9 is the damping factor, adjust for stronger/weaker damping

            if (Math.abs(paddle1Velocity) < 0.1) {
                paddle1Velocity = 0;
            }
        }

        paddle1Y += paddle1Velocity * deltaTime;

        paddle1Y = Math.max(0, Math.min(paddle1Y, gamePanel.getHeight() - 100));
        paddle2Y = Math.max(0, Math.min(paddle2Y, gamePanel.getHeight() - 100));

        //System.out.println("BallX: " + ballX);

        //System.out.println("Ball Velocity: " + ballVelocity);
        ballCollision();
        updateBallPosition(deltaTime);

    }

    private void render() {
        gamePanel.updatePositions(paddle1X, (int) paddle1Y, paddle2X, (int) paddle2Y, ballX, ballY);
        gamePanel.repaint();
    }

    public void updateBallPosition(double deltaTime) {

        ballX += (int) (ballVelocity * Math.cos(ballAngleRadians) * deltaTime);
        ballY += (int) (ballVelocity * Math.sin(ballAngleRadians) * deltaTime);

        gamePanel.updateBallPosition(ballX, ballY);
    }


    private void ballCollision() {

        if(ballX < frame.getHeight() || ballX > frame.getHeight() - 10) {
            System.out.println("Ball hit wall");
            ballAngleRadians = (float) Math.sin(ballAngleRadians);
            ballVelocity = -ballVelocity;
        }

        if(ballX < paddle1X && (ballY > paddle1Y && ballY < paddle1Y - gamePanel.getPaddleHeight())) {
            ballVelocity++;
            ballVelocity = -ballVelocity;
        }

        if(ballX > paddle2X && (ballY > paddle2Y && ballY < paddle2Y - gamePanel.getPaddleHeight())) {
            ballVelocity--;
            ballVelocity = -ballVelocity;
        }


    }







    //PADDLES

    public static void movePaddle1(int direction) {
        paddle1Direction = direction;
    }

    public static void movePaddle2(int direction) {
        paddle2Direction = direction;
    }

    public static void stopPanel1() {
        paddle1Direction = 0;
    }

    public static void stopPanel2() {
        paddle2Direction = 0;
    }

}
