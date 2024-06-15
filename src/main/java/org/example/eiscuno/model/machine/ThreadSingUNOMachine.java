package org.example.eiscuno.model.machine;

import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.deck.Deck;

import java.util.ArrayList;

public class ThreadSingUNOMachine implements Runnable{
    private ArrayList<Card> cardsPlayer;
    private Deck deck;
    private boolean eat=true;
    private boolean b;

    public ThreadSingUNOMachine(ArrayList<Card> cardsPlayer){
        this.cardsPlayer = cardsPlayer;
    }

    @Override
    public void run(){
        while (eat){
            try {
                Thread.sleep((long) (Math.random() * 5000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            hasOneCardTheHumanPlayer();
        }
    }

    private void hasOneCardTheHumanPlayer(){
        if(cardsPlayer.size() == 1){
            System.out.println("UNO");

        }
    }
    public void setEat(boolean b){
        eat=this.b;
    }
}
