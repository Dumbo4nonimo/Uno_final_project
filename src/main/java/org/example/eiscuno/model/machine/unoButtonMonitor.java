package org.example.eiscuno.model.machine;

import org.example.eiscuno.controller.GameUnoController;
import org.example.eiscuno.model.player.Player;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The unoButtonMonitor class monitors the state of the Uno button for a machine player in a game of Uno.
 * It extends the Thread class and periodically checks if the machine player has only one card left and
 * whether the Uno button has been pressed within a specified timeout period.
 */
public class unoButtonMonitor extends Thread {
    private final Player machinePlayer;
    private GameUnoController gameUnoController;
    private final int TIMEOUT = 2000; // 2 seconds
    private AtomicBoolean unoButtonPressed;
    private AtomicBoolean alreadyChecked;

    /**
     * Constructs a unoButtonMonitor with the specified machine player and game controller.
     *
     * @param machinePlayer The machine player participating in the game.
     * @param gameUnoController The controller managing the game UI and logic.
     */
    public unoButtonMonitor(Player machinePlayer, GameUnoController gameUnoController) {
        this.machinePlayer = machinePlayer;
        this.gameUnoController = gameUnoController;
        this.unoButtonPressed = new AtomicBoolean(false);
        this.alreadyChecked = new AtomicBoolean(false);
    }

    /**
     * Runs the thread, continuously checking if the machine player has only one card left and
     * whether the Uno button has been pressed within the timeout period.
     */
    @Override
    public void run() {
        while (true) {
            try {
                // Check if the machine player has only one card left
                if (machinePlayer.getCardsPlayer().size() == 1) {
                    // Check if this card has already been verified
                    if (!alreadyChecked.get()) {
                        long startTime = System.currentTimeMillis();

                        // Reset the button state before checking
                        unoButtonPressed.set(false);

                        // Wait for TIMEOUT before checking the button state
                        while (System.currentTimeMillis() - startTime < TIMEOUT) {
                            Thread.sleep(100); // Sleep for a short time before checking again
                        }

                        // Check the button state after the waiting period
                        if (unoButtonPressed.get()) {
                            // Action if the button was not pressed within the timeout
                            gameUnoController.eatMachineCards();
                        }

                        // Mark as checked
                        alreadyChecked.set(true);
                    }
                } else {
                    // Reset the verification state when the player does not have only one card
                    alreadyChecked.set(false);
                }

                // Sleep for a while before checking again
                Thread.sleep(500); // Sleep for 0.5 seconds to reduce the checking frequency
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Sets the state indicating that the Uno button has been pressed.
     */
    public void setUnoButtonPressed() {
        unoButtonPressed.set(true);
    }
}
