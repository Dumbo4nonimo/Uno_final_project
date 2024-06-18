package org.example.eiscuno.model.table;

import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.unoenum.EISCUnoEnum;
import org.junit.jupiter.api.Test;

import java.util.Stack;

import static org.junit.jupiter.api.Assertions.*;

class TableTest {
    private Player humanPlayer;
    private Player machinePlayer;
    private Deck deck;
    private Table table;
    private Card card3Red;

    // Stack to hold the cards
    private Stack<Card> cardStack;

    public TableTest() {
        // Initialize the stack
        cardStack = new Stack<>();

        Card card2Yellow = new Card(EISCUnoEnum.TWO_WILD_DRAW_YELLOW.getFilePath(), "1", "YELLOW");
        Card card5Red = new Card(EISCUnoEnum.RED_5.getFilePath(), "1", "RED");
        Card card1Blue = new Card(EISCUnoEnum.BLUE_1.getFilePath(), "1", "BLUE");
        Card card7Green = new Card(EISCUnoEnum.GREEN_7.getFilePath(), "1", "GREEN");
        Card card4Blue = new Card(EISCUnoEnum.BLUE_4.getFilePath(), "1", "BLUE");
        Card card3Red = new Card(EISCUnoEnum.RED_3.getFilePath(), "1", "RED");

        // Add cards to the stack
        cardStack.push(card2Yellow);
        cardStack.push(card5Red);
        cardStack.push(card1Blue);
        cardStack.push(card7Green);
        cardStack.push(card4Blue);
        cardStack.push(card3Red);

    }



    @Test
    void tableShouldHaveA3RedCard (){
        Table table = new Table();
        table.addCardOnTheTable(card3Red);

        // Verify the card added is the 3 Red card
        assertEquals(card3Red.getValue(), table.getCurrentCardOnTheTable().getValue());

    }

}