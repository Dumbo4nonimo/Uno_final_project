package org.example.eiscuno.model.machine;

import javafx.application.Platform;
import org.example.eiscuno.controller.GameUnoController;
import org.example.eiscuno.model.card.Card;

import java.util.ArrayList;

/**
 * The ThreadSingUNOMachine class simulates the behavior of the machine player in an Uno game.
 * When the machine player has only one card left, it calls "UNO" and the corresponding game
 * logic is handled by the GameUnoController.
 */
public class ThreadSingUNOMachine implements Runnable {
    private ArrayList<Card> cardsPlayer;
    private boolean eat = true;
    private GameUnoController controller;

    /**
     * Constructs a ThreadSingUNOMachine with the specified player's cards and game controller.
     *
     * @param cardsPlayer The list of cards held by the player.
     * @param controller  The game controller handling the Uno game logic.
     */
    public ThreadSingUNOMachine(ArrayList<Card> cardsPlayer, GameUnoController controller) {
        this.cardsPlayer = cardsPlayer;
        this.controller = controller;
    }

    /**
     * Continuously runs the machine player's Uno logic in a separate thread.
     * Simulates a random waiting time before checking the player's cards.
     */
    @Override
    public void run() {
        while (true) {
            try {
                // Simulate a random waiting time
                Thread.sleep((long) (Math.random() * 5000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            hasOneCardTheHumanPlayer(eat);
        }
    }

    /**
     * Checks if the human player has only one card left and if so, executes the machine's Uno logic.
     *
     * @param singOne Flag indicating whether the machine should call "UNO".
     */
    private void hasOneCardTheHumanPlayer(boolean singOne) {
        if (singOne) {
            Platform.runLater(() -> {
                if (cardsPlayer.size() == 1) {
                    System.out.println("UNO");
                    // Call the controller method to show the "UNO" message
                    controller.showSayOneLabel();
                    // Call the controller method to handle the machine's "UNO" logic
                    controller.singUnoMachine();
                }
            });
        }
    }

    /**
     * Sets the flag indicating whether the machine should call "UNO".
     *
     * @param b The new value for the eat flag.
     */
    public void setEat(boolean b) {
        this.eat = b;
    }
}
