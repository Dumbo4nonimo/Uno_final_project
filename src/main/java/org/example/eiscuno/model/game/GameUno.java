package org.example.eiscuno.model.game;

import org.example.eiscuno.model.alert.AlertBox;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.observer.Observer;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;
import org.example.eiscuno.model.observer.Observable;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a game of Uno.
 * This class manages the game logic and interactions between players, deck, and the table.
 */
public class GameUno implements IGameUno,Observable {

    private Player humanPlayer;
    private Player machinePlayer;
    private Deck deck;
    private Table table;
    private List<Observer> observers;

    /**
     * Constructs a new GameUno instance.
     *
     * @param humanPlayer   The human player participating in the game.
     * @param machinePlayer The machine player participating in the game.
     * @param deck          The deck of cards used in the game.
     * @param table         The table where cards are placed during the game.
     *
     */
    public GameUno(Player humanPlayer, Player machinePlayer, Deck deck, Table table) {
        this.humanPlayer = humanPlayer;
        this.machinePlayer = machinePlayer;
        this.deck = deck;
        this.table = table;
        this.observers = new ArrayList<>();
    }
    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }

    /**
     * Starts the Uno game by distributing cards to players.
     * The human player and the machine player each receive 10 cards from the deck.
     */

    @Override
    public void startGame() {
        for (int i = 0; i < 10; i++) {
            if (i < 8) {
                humanPlayer.addCard(this.deck.takeCard());
            } else {
                machinePlayer.addCard(this.deck.takeCard());
            }
        }
        notifyObservers();  // Notificar a los observadores después de iniciar el juego
    }

    @Override
    public void eatCard(Player player, int numberOfCards) {
        Card card;
        for (int i = 0; i < numberOfCards; i++) {
            card = this.deck.takeCard();
            if(card != null){
                player.addCard(this.deck.takeCard());
            }else{
                AlertBox alertBox = new AlertBox();
                alertBox.showMessage("Alerta", "Ya no hay mas cartas sobre la baraja","Termina el juego con las que tienes");

            }
        }
        notifyObservers();  // Notificar a los observadores después de que el jugador coma una carta
    }
    @Override
    public void playCard(Card card) {
        this.table.addCardOnTheTable(card);
        notifyObservers();  // Notificar a los observadores después de jugar una carta
    }

    /**
     * Handles the scenario when a player shouts "Uno", forcing the other player to draw a card.
     *
     * @param playerWhoSang The player who shouted "Uno".
     */
    @Override
    public void haveSungOne(String playerWhoSang) {
        if (playerWhoSang.equals(humanPlayer.getTypePlayer())) {
            machinePlayer.addCard(this.deck.takeCard());
        } else {
            humanPlayer.addCard(this.deck.takeCard());
        }
    }

    /**
     * Retrieves the current visible cards of the human player starting from a specific position.
     *
     * @param posInitCardToShow The initial position of the cards to show.
     * @return An array of cards visible to the human player.
     */
    @Override
    public Card[] getCurrentVisibleCardsHumanPlayer(int posInitCardToShow) {
        int totalCards = this.humanPlayer.getCardsPlayer().size();
        int numVisibleCards = Math.min(4, totalCards - posInitCardToShow);
        Card[] cards = new Card[numVisibleCards];

        for (int i = 0; i < numVisibleCards; i++) {
            cards[i] = this.humanPlayer.getCard(posInitCardToShow + i);
        }

        return cards;
    }
    @Override
    public Card[] getCurrentVisibleCardsMachinePlayer(int posInitCardToShow) {
        int totalCards = this.machinePlayer.getCardsPlayer().size();
        int numVisibleCards = Math.min(4, totalCards - posInitCardToShow);
        Card[] cards = new Card[numVisibleCards];

        for (int i = 0; i < numVisibleCards; i++) {
            cards[i] = this.machinePlayer.getCard(posInitCardToShow + i);
        }

        return cards;
    }

    /**
     * Checks if the game is over.
     *
     * @return True if the deck is empty, indicating the game is over; otherwise, false.
     */
    @Override
    public Boolean isGameOver() {
        return null;
    }

    public Player getHumanPlayer() {
        return humanPlayer;
    }

}
