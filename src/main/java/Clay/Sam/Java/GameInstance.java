package Clay.Sam.Java;


import Clay.Sam.Java.assets.GamePanel;
import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

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

    private int ballX = 300;
    private int ballY = 700;
    private float ballVelocity = 300;
    private double ballAngle = 20; // in degrees
    private final int ballRadius;

    private static double paddle1Velocity = 0;
    private static double paddle2Velocity = 0;
    private static final int paddleVelocityMax = 100;
    private static double paddleAcceleration = 100;  //gets * by deltaTIme which is 0.016

    private int paddleWidth = 30;

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

        ballRadius = gamePanel.getBallRadius();
        paddleWidth = gamePanel.getPaddleWidth();

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



        //TESTING STUFF TO BE REMOVED
        ballAngle = ThreadLocalRandom.current().nextInt(10, 360 + 1);
        ballVelocity = 430;
        paddle1Velocity = 240;
        paddleAcceleration = 150;
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
                if(elapsedTime / 1000.0 > 0.02) {
                    System.out.println("Long tick: " + elapsedTime / 1000.0);
                }
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
            paddle1Velocity *= 0.8; // 0.9 is the damping factor, adjust for stronger/weaker damping

            if (Math.abs(paddle1Velocity) < 0.1) {
                paddle1Velocity = 0;
            }
        }

        if (paddle2Direction != 0) {
            paddle2Velocity += (paddleAcceleration * paddle2Direction) * deltaTime;

            if (Math.abs(paddle2Velocity) > paddleVelocityMax) {
                paddle2Velocity = Math.signum(paddle2Velocity) * paddleVelocityMax;
            }
        } else {
            paddle2Velocity *= 0.8; // 0.9 is the damping factor, adjust for stronger/weaker damping

            if (Math.abs(paddle2Velocity) < 0.1) {
                paddle2Velocity = 0;
            }
        }

        paddle1Y += paddle1Velocity * deltaTime;
        paddle2Y += paddle2Velocity * deltaTime;

        paddle1Y = Math.max(0, Math.min(paddle1Y, gamePanel.getHeight() - 100));
        paddle2Y = Math.max(0, Math.min(paddle2Y, gamePanel.getHeight() - 100));

        //System.out.println("BallX: " + ballX);

        //System.out.println("Ball Velocity: " + ballVelocity);
        updateBallPosition(deltaTime);
        ballAngleCheck();
        ballCollision();

    }

    private void render() {
        gamePanel.updatePositions(paddle1X, (int) paddle1Y, paddle2X, (int) paddle2Y, ballX, ballY);
        gamePanel.repaint();
    }

    public void updateBallPosition(double deltaTime) {

        ballX += (int) (ballVelocity * Math.cos(Math.toRadians(ballAngle)) * deltaTime);
        ballY += (int) (ballVelocity * Math.sin(Math.toRadians(ballAngle)) * deltaTime);

        gamePanel.updateBallPosition(ballX, ballY);
    }

    public void ballAngleCheck() {

        //adds veriation to each shot
        int randomNum = ThreadLocalRandom.current().nextInt(1, 20 + 1);

        //4/30 chance to be massivle angle change
        if(randomNum < 4) {
            randomNum = 30;
        }

        //checks for the near dead angle within +- 15 degrees
        if(30 <= ballAngle && ballAngle <= 60
                || 75 <= ballAngle && ballAngle <= 105
                || 165 <= ballAngle && ballAngle <= 195
                || 255 <= ballAngle && ballAngle <= 285) {
            int randomNum1 = ThreadLocalRandom.current().nextInt(10, 30 + 1);
            if(ballAngle > 0) {
                ballAngle = +randomNum1;
                return;
            } else if(ballAngle < 0) {
                ballAngle = -randomNum1;
                return;
            } else {
                ballAngle = randomNum1;
                return;
            }
        }
            //^^ achives the same as above, leaving both in as i don't understand this
        if(ballAngle > 0) {
            ballAngle = +randomNum;
            return;
        } else if(ballAngle < 0) {
            ballAngle = -randomNum;
            return;
        }


    }

    private void ballCollision() {

        //System.out.println("BallX: " + ballX);

        if(ballX < (0.1 * frame.getWidth()) || ballX > (0.9 * frame.getWidth())) {
            System.out.println("Ball hit wall1");
            ballAngle = Math.toDegrees(Math.PI - Math.toRadians(ballAngle));

        }

        if(ballY < (0.1 * frame.getHeight()) || ballY > (0.9 * frame.getHeight())) {
            System.out.println("Ball hit wall2");
            ballAngle = -ballAngle;


        }

        if (ballX - ballRadius <= paddle1X + paddleWidth &&
                ballX + ballRadius >= paddle1X &&
                ballY + ballRadius >= paddle1Y &&
                ballY - ballRadius <= paddle1Y + paddleWidth) {

            System.out.println("Ball hit paddle1");

            ballAngle = 180 - ballAngle;

            ballVelocity += 10;
        }

        if(ballX > paddle2X && (ballY > paddle2Y && ballY < paddle2Y - gamePanel.getPaddleHeight())) {
            System.out.println("Ball hit paddle2");
            ballAngle = Math.toDegrees(Math.PI - Math.toRadians(ballAngle));
            ballVelocity -= 10;
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
