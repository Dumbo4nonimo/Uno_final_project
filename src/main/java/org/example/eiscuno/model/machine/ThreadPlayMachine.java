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

public class ThreadPlayMachine extends Thread {
    private static Table table;
    private static Player machinePlayer;
    private Deck deck;
    private static ImageView tableImageView;
    private volatile boolean hasPlayerPlayed;
    private GameUnoController gameUnoController;
    private GameUno gameUno;

    public ThreadPlayMachine(Table table, Player machinePlayer, ImageView tableImageView, GameUnoController gameUnoController, Deck deck, GameUno gameUno) {
        ThreadPlayMachine.table = table;
        ThreadPlayMachine.machinePlayer = machinePlayer;
        ThreadPlayMachine.tableImageView = tableImageView;
        this.deck = deck;
        this.gameUno = gameUno;
        this.hasPlayerPlayed = false;
        this.gameUnoController = gameUnoController;
    }

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

    private void putCardsOnTable(Card card, int index) {
        table.addCardOnTheTable(card);
        tableImageView.setImage(card.getImage());
        machinePlayer.removeCard(index);
        gameUnoController.showMachineCards();

    }

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

    private void wild(Card card, int index) {
        putCardsOnTable(card, index);
        Random random = new Random();
        AlertBox alertBox = new AlertBox();

        int num = random.nextInt(4) + 1;
        switch (num) {
            case 1:
                alertBox.showMessage("Color", "El oponente ha cambiado el color a:", "Amarillo");
                table.addCardOnTheTable(deck.getGhostCards().get(0));
                break;
            case 2:
                alertBox.showMessage("Color", "El oponente ha cambiado el color a:", "Rojo");
                table.addCardOnTheTable(deck.getGhostCards().get(1));
                break;
            case 3:
                alertBox.showMessage("Color", "El oponente ha cambiado el color a:", "Azul");
                table.addCardOnTheTable(deck.getGhostCards().get(2));
                break;
            case 4:
                alertBox.showMessage("Color", "El oponente ha cambiado el color a:", "Verde");
                table.addCardOnTheTable(deck.getGhostCards().get(3));
                break;
        }
    }

    private void skip(Card card, int index) {
        putCardsOnTable(card, index);
        gameUnoController.attackMessage.setText("Te bloquearon y pierdes turno");
        gameUnoController.attackMessage.setVisible(true);
        setHasPlayerPlayed(true);
    }

    private void reverse(Card card, int index) {
        putCardsOnTable(card, index);
        gameUnoController.attackMessage.setText("Oponente ha aplicado reversa");
        gameUnoController.attackMessage.setVisible(true);
        setHasPlayerPlayed(true);
    }

    private Card obtainLastAdeedCard() {
        int index = machinePlayer.getCardsPlayer().size() - 1;
        return machinePlayer.getCardsPlayer().get(index);
    }

    public void throughMachineCards() {
        int counter = 0;
        for (Card card : machinePlayer.getCardsPlayer()) {
            System.out.println(counter + ". " + card.getPath());
            counter++;
        }
        System.out.println();
    }

    public boolean colorOrValue(Card card) {
        return table.getCurrentCardOnTheTable().getColor().equals(card.getColor()) || table.getCurrentCardOnTheTable().getValue().equals(card.getValue());
    }

    public void setHasPlayerPlayed(boolean hasPlayerPlayed) {
        this.hasPlayerPlayed = hasPlayerPlayed;
    }

    public void passMessageLabel() {
        gameUnoController.attackMessage.setText("La maquina saltó turno");
        gameUnoController.attackMessage.setVisible(true);
    }
}
