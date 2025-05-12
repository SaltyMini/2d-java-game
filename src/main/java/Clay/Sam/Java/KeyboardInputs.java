package Clay.Sam.Java;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class KeyboardInputs {

    public void inputs() {

        JFrame frame = Frame.getFrame();
        if (frame == null) {
            throw new IllegalStateException("Frame is not initialized.");
        }

        GameInstance gameInstance = GameInstance.getInstance();

        JPanel contentPane = (JPanel) frame.getContentPane();

        // Make sure the content pane can receive focus
        contentPane.setFocusable(true);

        // Set up key bindings for W key (pressed & released)
        setupKeyBinding(contentPane, KeyEvent.VK_W, "W_pressed", false, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gameInstance != null) {
                    GameInstance.movePaddle1(-1);
                }
            }
        });

        setupKeyBinding(contentPane, KeyEvent.VK_W, "W_released", true, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gameInstance != null) {
                    GameInstance.stopPanel1();
                }
            }
        });

        // Set up key bindings for S key (pressed & released)
        setupKeyBinding(contentPane, KeyEvent.VK_S, "S_pressed", false, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gameInstance != null) {
                    GameInstance.movePaddle1(1);
                }
            }
        });

        setupKeyBinding(contentPane, KeyEvent.VK_S, "S_released", true, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gameInstance != null) {
                    GameInstance.stopPanel1();
                }
            }
        });

        // Set up key bindings for UP key (pressed & released)
        setupKeyBinding(contentPane, KeyEvent.VK_UP, "UP_pressed", false, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gameInstance != null) {
                    GameInstance.movePaddle2(1);
                }
            }
        });

        setupKeyBinding(contentPane, KeyEvent.VK_UP, "UP_released", true, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gameInstance != null) {
                    GameInstance.stopPanel2();
                }
            }
        });

        // Set up key bindings for DOWN key (pressed & released)
        setupKeyBinding(contentPane, KeyEvent.VK_DOWN, "DOWN_pressed", false, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gameInstance != null) {
                    GameInstance.movePaddle2(-1);
                }
            }
        });

        setupKeyBinding(contentPane, KeyEvent.VK_DOWN, "DOWN_released", true, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gameInstance != null) {
                    GameInstance.stopPanel2();
                }
            }
        });

        // Request focus to the content pane
        if (contentPane.isVisible()) {
            contentPane.requestFocusInWindow();
        }
    }

    /**
     * Setup key bindings for a given component.
     *
     * @param component  The Swing component (e.g., JPanel) for key binding.
     * @param keyCode    The keyboard key code (e.g., KeyEvent.VK_W).
     * @param actionName The name of the binding (for action mapping).
     * @param onRelease  True for release action, false for press action.
     * @param action     The action to perform when triggered.
     */
    private void setupKeyBinding(JComponent component, int keyCode, String actionName, boolean onRelease, Action action) {
        // Use WHEN_IN_FOCUSED_WINDOW to receive keyboard events even when the component doesn't have focus
        int condition = JComponent.WHEN_IN_FOCUSED_WINDOW;

        // Get the input and action maps
        InputMap inputMap = component.getInputMap(condition);
        ActionMap actionMap = component.getActionMap();

        // Create the keystroke for the specified press or release action
        KeyStroke keyStroke = KeyStroke.getKeyStroke(keyCode, 0, onRelease);

        // Put the key binding into the input map
        inputMap.put(keyStroke, actionName);

        // Associate the action with the action name
        actionMap.put(actionName, action);
    }
}