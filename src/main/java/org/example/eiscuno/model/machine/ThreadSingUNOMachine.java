package org.example.eiscuno.model.machine;

import javafx.application.Platform;
import org.example.eiscuno.controller.GameUnoController;
import org.example.eiscuno.model.card.Card;

import java.util.ArrayList;

public class ThreadSingUNOMachine implements Runnable {
    private ArrayList<Card> cardsPlayer;
    private boolean eat=true;
    private GameUnoController controller;

    public ThreadSingUNOMachine(ArrayList<Card> cardsPlayer, GameUnoController controller) {
        this.cardsPlayer = cardsPlayer;
        this.controller = controller;
    }

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

    public void setEat(boolean b) {
        this.eat = b;
    }

}
