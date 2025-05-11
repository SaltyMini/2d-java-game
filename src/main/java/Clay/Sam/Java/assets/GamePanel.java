package Clay.Sam.Java.assets;

import javax.swing.*;
import java.awt.*;

import static javax.swing.text.StyleConstants.setBackground;

public class GamePanel extends JPanel {
    private int paddle1X, paddle1Y;
    private int paddle2X, paddle2Y;
    private int ballX, ballY;

    private final int PADDLE_WIDTH = 30;
    private final int PADDLE_HEIGHT = 100;
    private final int BALL_DIAMETER = 20;

    public GamePanel() {
        setLayout(null);
        setBackground(Color.BLACK);
        setFocusable(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw paddles
        g2d.setColor(Color.BLUE);
        g2d.fillRect(paddle1X, paddle1Y, PADDLE_WIDTH, PADDLE_HEIGHT);
        g2d.fillRect(paddle2X, paddle2Y, PADDLE_WIDTH, PADDLE_HEIGHT);

        // Draw ball
        g2d.setColor(Color.RED);
        g2d.fillOval(ballX, ballY, BALL_DIAMETER, BALL_DIAMETER);
    }

    public void updatePositions(int p1x, int p1y, int p2x, int p2y, int bx, int by) {
        paddle1X = p1x;
        paddle1Y = p1y;
        paddle2X = p2x;
        paddle2Y = p2y;
        ballX = bx;
        ballY = by;
        repaint();
    }

    public int getPaddleHeight() {
        return PADDLE_HEIGHT;
    }
}