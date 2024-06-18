package org.example.eiscuno.model.machine;

import javafx.application.Platform;
import javafx.scene.image.ImageView;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;
import org.example.eiscuno.controller.GameUnoController;

import java.util.ArrayList;
public class ThreadPlayMachine extends Thread {
    private static Table table;
    private static Player machinePlayer;
    private static ImageView tableImageView;
    private volatile boolean hasPlayerPlayed;
    private GameUnoController gameUnoController;

    public ThreadPlayMachine(Table table, Player machinePlayer, ImageView tableImageView, GameUnoController gameUnoController) {
        this.table = table;
        this.machinePlayer = machinePlayer;
        this.tableImageView = tableImageView;
        this.hasPlayerPlayed = false;
        this.gameUnoController = gameUnoController;
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

                putCardOnTheTable();
                gameUnoController.incrementPosInitCardToShow1(); // Incrementa posInitCardToShow1 en el controlador
                hasPlayerPlayed = false;
            }
        }
    }

    private void putCardOnTheTable() {
        Platform.runLater(() -> {
            int index = (int) (Math.random() * machinePlayer.getCardsPlayer().size());
            Card card = machinePlayer.getCard(index);
            table.addCardOnTheTable(card);
            tableImageView.setImage(card.getImage());
        });
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
}