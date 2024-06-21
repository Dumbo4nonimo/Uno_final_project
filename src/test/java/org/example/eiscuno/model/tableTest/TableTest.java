package org.example.eiscuno.model.tableTest;

import javafx.embed.swing.JFXPanel;
import javafx.application.Platform;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TableTest {

    @BeforeAll
    public static void initJFX() {
        // Initialize JavaFX toolkit
        new JFXPanel();
        Platform.setImplicitExit(false);
    }

    @Test
    void firstCardAddedByMachine() {
        Platform.runLater(() -> {
            var humanPlayer = new Player("HUMAN_PLAYER");
            var machinePlayer = new Player("MACHINE_PLAYER");
            var deck = new Deck();
            var table = new Table();
            var gameUno = new GameUno(humanPlayer, machinePlayer, deck, table);
            gameUno.startGame();

            // Manually play a card
            gameUno.playCard(machinePlayer.getCard(0));

            // Unit test to ensure the card played is the one on the table
            assertEquals(machinePlayer.getCard(0), table.getCurrentCardOnTheTable());
        });

        // Wait for JavaFX Platform to complete the test execution
        try {
            Thread.sleep(1000); // Adjust the sleep time as needed
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    void secondCardAddedByHuman() {
        Platform.runLater(() -> {
            var humanPlayer = new Player("HUMAN_PLAYER");
            var machinePlayer = new Player("MACHINE_PLAYER");
            var deck = new Deck();
            var table = new Table();
            var gameUno = new GameUno(humanPlayer, machinePlayer, deck, table);
            gameUno.startGame();

            // Manually play a card
            gameUno.playCard(machinePlayer.getCard(0));
            table.addCardOnTheTable(humanPlayer.getCard(1));

            // Unit test to ensure the card played is the one on the table
            assertEquals(humanPlayer.getCard(1), table.getCurrentCardOnTheTable());
        });

        // Wait for JavaFX Platform to complete the test execution
        try {
            Thread.sleep(1000); // Adjust the sleep time as needed
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}