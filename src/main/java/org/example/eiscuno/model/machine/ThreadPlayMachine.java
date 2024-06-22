package org.example.eiscuno.model.machine;

import javafx.application.Platform;
import javafx.scene.image.ImageView;
import org.example.eiscuno.model.alert.AlertBox;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;
import org.example.eiscuno.controller.GameUnoController;

import java.util.ArrayList;
import java.util.Random;

/**
 * ThreadPlayMachine is a thread responsible for managing the actions of the machine player in a game of Uno.
 */
public class ThreadPlayMachine extends Thread {
    private static Table table;
    private static Player machinePlayer;
    private Deck deck;
    private static ImageView tableImageView;
    private volatile boolean hasPlayerPlayed;
    private GameUnoController gameUnoController;
    private GameUno gameUno;

    /**
     * Constructs a ThreadPlayMachine with specified table, machine player, table image view, game controller, deck, and game instance.
     *
     * @param table The table where the game is played.
     * @param machinePlayer The machine player participating in the game.
     * @param tableImageView The ImageView representing the current card on the table.
     * @param gameUnoController The controller managing the game UI and logic.
     * @param deck The deck of cards used in the game.
     * @param gameUno The instance of the game.
     */
    public ThreadPlayMachine(Table table, Player machinePlayer, ImageView tableImageView, GameUnoController gameUnoController, Deck deck, GameUno gameUno) {
        ThreadPlayMachine.table = table;
        ThreadPlayMachine.machinePlayer = machinePlayer;
        ThreadPlayMachine.tableImageView = tableImageView;
        this.deck = deck;
        this.gameUno = gameUno;
        this.hasPlayerPlayed = false;
        this.gameUnoController = gameUnoController;
    }

    /**
     * Runs the thread, continuously checking if the human player has played, and if so, executing the machine's turn.
     */
    public void run() {
        while (true) {
            if (hasPlayerPlayed) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                gameUnoController.showMachineCards();
                hasPlayerPlayed = false;
                logicCardsByCases();
            }
        }
    }

    /**
     * Executes the logic for the machine player to choose and play a card based on the current state of the game.
     */
    private void logicCardsByCases() {
        Platform.runLater(() -> {
            if (!table.isEmpty()) {
                if (!table.getCurrentCardOnTheTable().getIsSpecial()) {
                    validateBasicCards();
                    throughMachineCards();
                } else {
                    Card newCard;
                    ArrayList<Card> cardsPlayerTemporal = new ArrayList<>(machinePlayer.getCardsPlayer());
                    for (int i = 0; i < cardsPlayerTemporal.size(); i++) {
                        Card card = cardsPlayerTemporal.get(i);
                        if (card.getPath().contains("reserve_")) {
                            if (colorOrValue(card)) {
                                reverse(card, i);
                                break;
                            }
                            newCard = takeCardsFromDeck();
                            putCardsOnTable(newCard, i);
                        } else if (card.getPath().contains("2_wild_draw")) {
                            if (colorOrValue(card)) {
                                putCardsOnTable(card, i);
                                gameUnoController.plusTwoPlusFourMessage.setText("Tu: +2");
                                gameUnoController.plusTwoPlusFourMessage.setVisible(true);
                                gameUno.eatCard(gameUno.getHumanPlayer(), 2);
                            }
                        } else if (card.getPath().contains("4_wild_draw")) {
                            putCardsOnTable(card, i);
                            gameUnoController.plusTwoPlusFourMessage.setText("Tu: +4");
                            gameUnoController.plusTwoPlusFourMessage.setVisible(true);
                            gameUno.eatCard(gameUno.getHumanPlayer(), 4);
                        } else if (card.getPath().contains("skip_")) {
                            if (colorOrValue(card)) {
                                skip(card, i);
                                break;
                            }
                            newCard = takeCardsFromDeck();
                            putCardsOnTable(newCard, i);
                        } else if (card.getPath().contains("wild_change")) {
                            wild(card, i);
                            break;
                        } else if (colorOrValue(card)) {
                            putCardsOnTable(card, i);
                            break;
                        }
                    }
                }
            } else {
                int index = (int) (Math.random() * machinePlayer.getCardsPlayer().size());
                Card card = machinePlayer.getCard(index);
                table.addCardOnTheTable(card);
                tableImageView.setImage(card.getImage());
                throughMachineCards();
            }
        });
    }

    /**
     * Validates and plays basic cards for the machine player.
     */
    private void validateBasicCards() {
        boolean flag = false;
        int index = 0;
        ArrayList<Card> cardsPlayerTemporal = machinePlayer.getCardsPlayer();
        for (Card card : cardsPlayerTemporal) {
            if (!card.getIsSpecial() && (colorOrValue(card))) {
                putCardsOnTable(card, index);
                flag = true;
                break;
            } else if (card.getPath().contains("skip_")) {
                if (colorOrValue(card)) {
                    skip(card, index);
                    flag = true;
                    break;
                }
            } else if (card.getPath().contains("reserve_")) {
                if (colorOrValue(card)) {
                    reverse(card, index);
                    flag = true;
                    break;
                }
            } else if (card.getPath().contains("wild_change")) {
                wild(card, index);
                flag = true;
                break;
            } else if (card.getPath().contains("2_wild_draw")) {
                if (colorOrValue(card)) {
                    putCardsOnTable(card, index);
                    gameUnoController.plusTwoPlusFourMessage.setText("Tu: +2");
                    gameUnoController.plusTwoPlusFourMessage.setVisible(true);
                    gameUno.eatCard(gameUno.getHumanPlayer(), 2);
                }
            } else if (card.getPath().contains("4_wild_draw")) {
                putCardsOnTable(card, index);
                gameUnoController.plusTwoPlusFourMessage.setText("Tu: +4");
                gameUnoController.plusTwoPlusFourMessage.setVisible(true);
                gameUno.eatCard(gameUno.getHumanPlayer(), 4);
            }
            index++;
        }

        if (!flag) {
            Card card = takeCardsFromDeck();
            if (card != null) {
                putCardsOnTable(card, index);
            }
        }
    }

    /**
     * Puts a card on the table and updates the table image view.
     *
     * @param card The card to be placed on the table.
     * @param index The index of the card in the player's hand.
     */
    private void putCardsOnTable(Card card, int index) {
        table.addCardOnTheTable(card);
        tableImageView.setImage(card.getImage());
        machinePlayer.removeCard(index);
        gameUnoController.showMachineCards();
    }

    /**
     * Takes a card from the deck.
     *
     * @return The card taken from the deck.
     */
    private Card takeCardsFromDeck() {
        while (deck.getIsThereCardsOnDeck()) {
            System.out.println(deck.getIsThereCardsOnDeck());
            machinePlayer.addCard(deck.takeCard());
            Card newCard = obtainLastAdeedCard();

            if (!newCard.getIsSpecial()) {
                if (colorOrValue(newCard)) {
                    machinePlayer.removeCard(machinePlayer.getCardsPlayer().size() - 1);
                    return newCard;
                }
            }
        }
        passMessageLabel();
        return null;
    }

    /**
     * Plays a wild card and changes the color.
     *
     * @param card The wild card to be played.
     * @param index The index of the card in the player's hand.
     */
    private void wild(Card card, int index) {
        putCardsOnTable(card, index);
        Random random = new Random();
        AlertBox alertBox = new AlertBox();

        int num = random.nextInt(4) + 1;
        switch (num) {
            case 1:
                alertBox.showMessage("Color", "The opponent has changed the color to:", "Yellow");
                table.addCardOnTheTable(deck.getGhostCards().get(0));
                break;
            case 2:
                alertBox.showMessage("Color", "The opponent has changed the color to:", "Red");
                table.addCardOnTheTable(deck.getGhostCards().get(1));
                break;
            case 3:
                alertBox.showMessage("Color", "The opponent has changed the color to:", "Blue");
                table.addCardOnTheTable(deck.getGhostCards().get(2));
                break;
            case 4:
                alertBox.showMessage("Color", "The opponent has changed the color to:", "Green");
                table.addCardOnTheTable(deck.getGhostCards().get(3));
                break;
        }
    }

    /**
     * Plays a skip card, preventing the human player from taking a turn.
     *
     * @param card The skip card to be played.
     * @param index The index of the card in the player's hand.
     */
    private void skip(Card card, int index) {
        putCardsOnTable(card, index);
        gameUnoController.attackMessage.setText("You are blocked and lose a turn");
        gameUnoController.attackMessage.setVisible(true);
        setHasPlayerPlayed(true);
    }

    /**
     * Plays a reverse card, reversing the turn order.
     *
     * @param card The reverse card to be played.
     * @param index The index of the card in the player's hand.
     */
    private void reverse(Card card, int index) {
        putCardsOnTable(card, index);
        gameUnoController.attackMessage.setText("Opponent applied reverse");
        gameUnoController.attackMessage.setVisible(true);
        setHasPlayerPlayed(true);
    }

    /**
     * Obtains the last card added to the machine player's hand.
     *
     * @return The last card added to the machine player's hand.
     */
    private Card obtainLastAdeedCard() {
        int index = machinePlayer.getCardsPlayer().size() - 1;
        return machinePlayer.getCardsPlayer().get(index);
    }

    /**
     * Prints the machine player's cards to the console.
     */
    public void throughMachineCards() {
        int counter = 0;
        for (Card card : machinePlayer.getCardsPlayer()) {
            System.out.println(counter + ". " + card.getPath());
            counter++;
        }
        System.out.println();
    }

    /**
     * Checks if a card matches the color or value of the current card on the table.
     *
     * @param card The card to be checked.
     * @return True if the card matches the color or value, false otherwise.
     */
    public boolean colorOrValue(Card card) {
        return table.getCurrentCardOnTheTable().getColor().equals(card.getColor()) || table.getCurrentCardOnTheTable().getValue().equals(card.getValue());
    }

    /**
     * Sets the flag indicating if the human player has played.
     *
     * @param hasPlayerPlayed The flag to be set.
     */
    public void setHasPlayerPlayed(boolean hasPlayerPlayed) {
        this.hasPlayerPlayed = hasPlayerPlayed;
    }

    /**
     * Displays a message indicating that the machine player skipped a turn.
     */
    public void passMessageLabel() {
        gameUnoController.attackMessage.setText("The machine skipped a turn");
        gameUnoController.attackMessage.setVisible(true);
    }
}
