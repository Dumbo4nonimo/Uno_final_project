package org.example.eiscuno.model.player;

import org.example.eiscuno.model.alert.AlertBox;
import org.example.eiscuno.model.card.Card;

import java.util.ArrayList;

/**
 * Represents a player in the Uno game.
 */
public class Player implements IPlayer {
    private ArrayList<Card> cardsPlayer;
    private String typePlayer;

    /**
     * Constructs a new Player object with an empty hand of cards.
     */
    public Player(String typePlayer){
        this.cardsPlayer = new ArrayList<Card>();
        this.typePlayer = typePlayer;
    };

    /**
     * Adds a card to the player's hand.
     *
     * @param card The card to be added to the player's hand.
     */
    @Override
    public void addCard(Card card){
        if(card != null){
            cardsPlayer.add(card);
        }else{
            AlertBox alertBox = new AlertBox();
            alertBox.showMessage("Alerta", "Ya no hay mas cartas sobre la baraja","Termina el juego con las que tienes.");
        }

    }

    /**
     * Retrieves all cards currently held by the player.
     *
     * @return An ArrayList containing all cards in the player's hand.
     */
    @Override
    public ArrayList<Card> getCardsPlayer() {
        return cardsPlayer;
    }

    /**
     * Removes a card from the player's hand based on its index.
     *
     * @param index The index of the card to remove.
     */
    @Override
    public void removeCard(int index) {
        try{
            cardsPlayer.remove(index);
        }catch (IndexOutOfBoundsException e){
            cardsPlayer.remove(index - 1);
            System.out.println("menos 1");
        }

    }

    /**
     * Retrieves a card from the player's hand based on its index.
     *
     * @param index The index of the card to retrieve.
     * @return The card at the specified index in the player's hand.
     */
    @Override
    public Card getCard(int index){
        return cardsPlayer.get(index);
    }

    public String getTypePlayer() {
        return typePlayer;
    }
}