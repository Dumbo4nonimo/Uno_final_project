package org.example.eiscuno.model.machine;

import javafx.scene.image.ImageView;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;

import java.util.ArrayList;

public class ThreadPlayMachine extends Thread {
    private static Table table;
    private static Player machinePlayer;
    private static ImageView tableImageView;
    private volatile boolean hasPlayerPlayed;



    public ThreadPlayMachine(Table table, Player machinePlayer, ImageView tableImageView) {
        this.table = table;
        this.machinePlayer = machinePlayer;
        this.tableImageView = tableImageView;
        this.hasPlayerPlayed = false;
    }

    public void run() {
        int index = (int) (Math.random() * machinePlayer.getCardsPlayer().size());
        while (true){
            if(hasPlayerPlayed){
                try{
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // Aqui iria la logica de colocar la carta

                putCardOnTheTable();
                //removeCard(index);
                hasPlayerPlayed = false;
            }
        }
    }

    private void putCardOnTheTable(){
        int index = (int) (Math.random() * machinePlayer.getCardsPlayer().size());
        Card card = machinePlayer.getCard(index);
        table.addCardOnTheTable(card);
        tableImageView.setImage(card.getImage());
    }

    public static void putCardOnTableByPath(Card arg){
        int counter = 0;
        for(Card card : machinePlayer.getCardsPlayer()){
            if(card.getPath().equals(arg.getPath())){
                Card obj = machinePlayer.getCard(counter);
                table.addCardOnTheTable(obj);
                tableImageView.setImage(obj.getImage());
            }
            counter ++;
        }
    }

    public void setHasPlayerPlayed(boolean hasPlayerPlayed) {
        this.hasPlayerPlayed = hasPlayerPlayed;
    }
//    @Override
//    public void removeCard(int index) {
//        Card card = machinePlayer.getCard(index);
//        cardsMachine.remove(index);
//    }
}