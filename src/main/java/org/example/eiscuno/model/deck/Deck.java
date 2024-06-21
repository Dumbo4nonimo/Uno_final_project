package org.example.eiscuno.model.deck;

import org.example.eiscuno.model.alert.AlertBox;
import org.example.eiscuno.model.unoenum.EISCUnoEnum;
import org.example.eiscuno.model.card.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

/**
 * Represents a deck of Uno cards.
 */
public class Deck {
    private Stack<Card> deckOfCards;
    private ArrayList<Card> ghostCards;
    private volatile boolean isThereCardsOnDeck;

    /**
     * Constructs a new deck of Uno cards and initializes it.
     */
    public Deck() {
        deckOfCards = new Stack<>();
        ghostCards = new ArrayList<>();
        isThereCardsOnDeck = true;
        initializeDeck();
    }

    /**
     * Initializes the deck with cards based on the EISCUnoEnum values.
     */
    private void initializeDeck() {
        for (EISCUnoEnum cardEnum : EISCUnoEnum.values()) {
            if (cardEnum.name().startsWith("GREEN_") ||
                    cardEnum.name().startsWith("YELLOW_") ||
                    cardEnum.name().startsWith("BLUE_") ||
                    cardEnum.name().startsWith("RED_") ||
                    cardEnum.name().startsWith("SKIP_") ||
                    cardEnum.name().startsWith("RESERVE_") ||
                    cardEnum.name().startsWith("TWO_WILD_DRAW_") ||
                    cardEnum.name().equals("FOUR_WILD_DRAW") ||
                    cardEnum.name().equals("WILD")) {
                Card card = new Card(cardEnum.getFilePath(), getCardValue(cardEnum.name()), getCardColor(cardEnum.name()), getIsSpecial(cardEnum.name()));
                deckOfCards.push(card);
                initGhostCards();
                // Print each card
                //System.out.println(card.getValue() + " " + card.getColor());
            }
        }
        Collections.shuffle(deckOfCards);
    }

    private boolean getIsSpecial(String name){
        if (name.endsWith("0")){
            return false;
        } else if (name.endsWith("1")){
            return false;
        } else if (name.endsWith("2")){
            return false;
        } else if (name.endsWith("3")){
            return false;
        } else if (name.endsWith("4")){
            return false;
        } else if (name.endsWith("5")){
            return false;
        } else if (name.endsWith("6")){
            return false;
        } else if (name.endsWith("7")){
            return false;
        } else if (name.endsWith("8")){
            return false;
        } else if (name.endsWith("9")){
            return false;
        } else if (name.contains("RESERVE")) {
            return true;
        } else if (name.contains("TWO_WILD_DRAW")) {
            return true;
        } else if (name.equals("FOUR_WILD_DRAW")) {
            return true;
        } else if (name.equals("WILD")) {
            return true;
        } else if (name.contains("SKIP")) {
            return true;
        } else {
            System.out.println("Checkout deck.java with isSpecial");
            return true;
        }

    }

    private String getCardValue(String name) {
        if (name.endsWith("0")){
            return "0";
        } else if (name.endsWith("1")){
            return "1";
        } else if (name.endsWith("2")){
            return "2";
        } else if (name.endsWith("3")){
            return "3";
        } else if (name.endsWith("4")){
            return "4";
        } else if (name.endsWith("5")){
            return "5";
        } else if (name.endsWith("6")){
            return "6";
        } else if (name.endsWith("7")){
            return "7";
        } else if (name.endsWith("8")){
            return "8";
        } else if (name.endsWith("9")){
            return "9";
        } else if (name.contains("RESERVE")) {
            return "RESERVE";
        } else if (name.contains("TWO_WILD_DRAW")) {
            return "+2";
        } else if (name.equals("FOUR_WILD_DRAW")) {
            return "+4";
        } else if (name.equals("WILD")) {
            return "WILD";
        } else if (name.contains("SKIP")) {
            return "SKIP";
        } else {
            return "NON_VALUE";
        }

    }

    private String getCardColor(String name){
        if(name.contains("GREEN")){
            return "GREEN";
        } else if(name.contains("YELLOW")){
            return "YELLOW";
        } else if(name.contains("BLUE")){
            return "BLUE";
        } else if(name.contains("RED")){
            return "RED";
        } else {
            return "NON_COLOR";
        }
    }

    /**
     * Takes a card from the top of the deck.
     *
     * @return the card from the top of the deck
     * @throws IllegalStateException if the deck is empty
     */
    public Card takeCard() {

        try{
            if (!deckOfCards.isEmpty()) {
                return deckOfCards.pop();
            }

            isThereCardsOnDeck = false;

        }catch (IllegalStateException e){
            isThereCardsOnDeck = false;
        }

        return null;

    }

    private void initGhostCards(){
        ghostCards.add(new Card("", "NON_VALUE", "YELLOW", false));
        ghostCards.add(new Card("", "NON_VALUE", "RED", false));
        ghostCards.add(new Card("", "NON_VALUE", "BLUE", false));
        ghostCards.add(new Card("", "NON_VALUE", "GREEN", false));
    }

    public ArrayList<Card> getGhostCards(){
        return ghostCards;
    }

    /**
     * Checks if the deck is empty.
     *
     * @return true if the deck is empty, false otherwise
     */
    public boolean isEmpty() {
        return deckOfCards.isEmpty();
    }

    public boolean getIsThereCardsOnDeck(){
        return isThereCardsOnDeck;
    }
}