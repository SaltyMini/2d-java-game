package Clay.Sam.Java;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class KeyboardInputs {

    public void inputs() {
        JFrame frame = Frame.getFrame();
        JPanel contentPane = (JPanel) frame.getContentPane();

        // Make sure the content pane can receive focus
        contentPane.setFocusable(true);

        // Set up key bindings for W key
        setupKeyBinding(contentPane, KeyEvent.VK_W, "W_pressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GameInstance.movePaddle1(1);
                System.out.println("W");
            }
        });

        // Set up key bindings for S key
        setupKeyBinding(contentPane, KeyEvent.VK_S, "S_pressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GameInstance.movePaddle1(-1);
            }
        });

        // Set up key bindings for arrow up
        setupKeyBinding(contentPane, KeyEvent.VK_UP, "UP_pressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GameInstance.movePaddle2(1);
            }
        });

        // Set up key bindings for arrow down
        setupKeyBinding(contentPane, KeyEvent.VK_DOWN, "DOWN_pressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GameInstance.movePaddle2(-1);
            }
        });

        // Request focus to the content pane
        contentPane.requestFocusInWindow();
    }

    private void setupKeyBinding(JComponent component, int keyCode, String actionName, Action action) {
        // Use WHEN_IN_FOCUSED_WINDOW to receive keyboard events even when the component doesn't have focus
        int condition = JComponent.WHEN_IN_FOCUSED_WINDOW;

        // Get the input and action maps
        InputMap inputMap = component.getInputMap(condition);
        ActionMap actionMap = component.getActionMap();

        // Create the key stroke for key press
        KeyStroke keyStroke = KeyStroke.getKeyStroke(keyCode, 0, false);

        // Put the key binding into the input map
        inputMap.put(keyStroke, actionName);

        // Associate the action with the action name
        actionMap.put(actionName, action);
    }
}