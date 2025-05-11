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
    private static int paddle1Y = 100;
    private int paddle2X = 350;
    private static int paddle2Y = 100;

    private int ballX = 200;
    private int ballY = 200;
    private float ballSpeedX = 5;
    private float ballSpeedY = 5;

    private static int paddle1Speed = 0;
    private static int paddle2Speed = 0;
    private static int paddleMaxSpeed = 3;

    private JFrame frame;



    private GameInstance() {

        frame = Frame.getFrame();

        KeyboardInputs keyboardInputs = new KeyboardInputs();
        keyboardInputs.inputs();

        paddle1X = (int) (frame.getWidth() * 0.1);
        paddle1Y = (int) (frame.getHeight() * 0.5);

        paddle2Y = (int) (frame.getHeight() * 0.5);
        paddle2X = (int) (frame.getWidth() * 0.9);
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
        gamePanel.requestFocusInWindow();

        frame.setLayout(new BorderLayout());
        frame.add(gamePanel, BorderLayout.CENTER);

        frame.setResizable(false);
        frame.revalidate();
        frame.repaint();


        //Start a new thread for game, this runs run()
        Thread gameThread = new Thread(this);
        gameThread.start();
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

        // stops padels leaving the screen TODO:refractor for readability
        int nextPaddle1Y = paddle1Y + paddle1Speed;
        int nextPaddle2Y = paddle2Y + paddle2Speed;

        // get game frame size
        JFrame frame = Frame.getFrame();
        int frameHeight = frame.getHeight();

        int paddleHeight = gamePanel.getPaddleHeight();

        // Check boundaries for paddle1
        if (nextPaddle1Y >= 0 && nextPaddle1Y + paddleHeight <= frameHeight) {
            paddle1Y = nextPaddle1Y;
        } else {
            // If we'd go out of bounds, stop at the boundary
            if (nextPaddle1Y < 0) {
                paddle1Y = 0;
            } else if (nextPaddle1Y + paddleHeight > frameHeight) {
                paddle1Y = frameHeight - paddleHeight;
            }
            paddle1Speed = 0;
        }

        // Check boundaries for paddle2
        if (nextPaddle2Y >= 0 && nextPaddle2Y + paddleHeight <= frameHeight) {
            paddle2Y = nextPaddle2Y;
        } else {
            // If we'd go out of bounds, stop at the boundary
            if (nextPaddle2Y < 0) {
                paddle2Y = 0;
            } else if (nextPaddle2Y + paddleHeight > frameHeight) {
                paddle2Y = frameHeight - paddleHeight;
            }
            paddle2Speed = 0;
        }

        // Ball movement logic would go here


        paddle1Y += paddle1Speed;
        paddle2Y += paddle2Speed;



    }

    private void render() {
        gamePanel.updatePositions(paddle1X, paddle1Y, paddle2X, paddle2Y, ballX, ballY);
        gamePanel.repaint();
    }

    public static void movePaddle1(int direction) {
        if(direction == 1) {
            if(paddle2Speed >= paddleMaxSpeed || paddle2Speed <= -paddleMaxSpeed) {
                return;
            }
            paddle1Speed++;
        } else {
            paddle1Speed--;
        }
    }

    public static void movePaddle2(int direction) {
        if(direction == 1) {
            if(paddle2Speed >= paddleMaxSpeed || paddle2Speed <= -paddleMaxSpeed) {
                return;
            }
            paddle2Speed++;
        } else {
            paddle2Speed--;
        }
    }

    public static void stopPanel1() {
        paddle1Speed = 0;
    }

    public static void stopPanel2() {
        paddle2Speed = 0;
    }

}
