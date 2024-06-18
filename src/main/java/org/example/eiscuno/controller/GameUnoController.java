package org.example.eiscuno.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Label;
import javafx.util.Duration;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.machine.ThreadPlayMachine;
import org.example.eiscuno.model.machine.ThreadSingUNOMachine;
import org.example.eiscuno.model.observer.Observer;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;
import org.example.eiscuno.model.unoenum.EISCUnoEnum;
import javafx.scene.control.Button;

import java.util.concurrent.atomic.AtomicBoolean;

public class GameUnoController {

    @FXML
    private GridPane gridPaneCardsMachine;

    @FXML
    private GridPane gridPaneCardsPlayer;

    @FXML
    private ImageView tableImageView;

    @FXML
    private Label logoEiscUno;
    @FXML
    private Label sayOne;

    @FXML
    private Button unoButton;

    @FXML
    private Button takeCardButton;

    @FXML
    private Label plusTwoPlusFourMessage;

    @FXML
    private Label attackMessage;

    private Player humanPlayer;
    private Player machinePlayer;
    private Deck deck;
    private Table table;
    private GameUno gameUno;
    private int posInitCardToShow;

    private ThreadSingUNOMachine threadSingUNOMachine;
    private ThreadPlayMachine threadPlayMachine;
    private boolean eat = true;

    private final AtomicBoolean unoButtonPressed = new AtomicBoolean(false);
    private int posInitCardToShow1;

    /**
     * Initializes the controller.
     */
    @FXML
    public void initialize() {
        initVariables();
        setImages();
        this.gameUno.addObserver((this);
        this.gameUno.startGame();

        printCardsHumanPlayer();
        printCardsMachinePlayer();

        threadSingUNOMachine = new ThreadSingUNOMachine(this.humanPlayer.getCardsPlayer(), this);
        Thread t = new Thread(threadSingUNOMachine, "ThreadSingUNO");
        t.start();

        threadPlayMachine = new ThreadPlayMachine(this.table, this.machinePlayer, this.tableImageView);
        threadPlayMachine.start();
    }

    /**
     * Initializes the variables for the game.
     */
    private void initVariables() {
        this.humanPlayer = new Player("HUMAN_PLAYER");
        this.machinePlayer = new Player("MACHINE_PLAYER");
        this.deck = new Deck();
        this.table = new Table();
        this.gameUno = new GameUno(this.humanPlayer, this.machinePlayer, this.deck, this.table);
        this.posInitCardToShow = 0;
        this.posInitCardToShow1=0;
    }

    public void setImages() {
        Image imageUno = new Image(getClass().getResourceAsStream(EISCUnoEnum.UNO.getFilePath()));
        ImageView imageViewUno = new ImageView(imageUno);
        imageViewUno.setFitWidth(logoEiscUno.getWidth());
        imageViewUno.setFitHeight(logoEiscUno.getHeight());
        logoEiscUno.setGraphic(imageViewUno);

        Image imageDeckOfCards = new Image(getClass().getResourceAsStream(EISCUnoEnum.DECK_OF_CARDS.getFilePath()));
        ImageView imageViewDeckOfCards = new ImageView(imageDeckOfCards);
        imageViewDeckOfCards.setFitWidth(takeCardButton.getWidth());
        imageViewDeckOfCards.setFitHeight(takeCardButton.getHeight());
        takeCardButton.setGraphic(imageViewDeckOfCards);

        Image imageUnoButton = new Image(getClass().getResourceAsStream(EISCUnoEnum.BUTTON_UNO.getFilePath()));
        ImageView imageViewUnoButton = new ImageView(imageUnoButton);
        imageViewUnoButton.setFitWidth(unoButton.getWidth());
        imageViewUnoButton.setFitHeight(unoButton.getHeight());
        unoButton.setGraphic(imageViewUnoButton);
    }

    /**
     * Prints the human player's cards on the grid pane.
     */
    private boolean findCardMachine(String path){
        for(Card card : machinePlayer.getCardsPlayer()){
            System.out.println(card.getPath());
            if(card.getPath().equals(path)){
                System.out.println("found");
                return true;
            }
        }
        System.out.println("not found");
        return false;
    }

    private void printCardsHumanPlayer() {
        this.gridPaneCardsPlayer.getChildren().clear();
        Card[] currentVisibleCardsHumanPlayer = this.gameUno.getCurrentVisibleCardsHumanPlayer(this.posInitCardToShow);

        for (int i = 0; i < currentVisibleCardsHumanPlayer.length; i++) {
            Card card = currentVisibleCardsHumanPlayer[i];
            ImageView cardImageView = card.getCard();

            cardImageView.setOnMouseClicked((MouseEvent event) -> {
                // Verify if the card can be played on the table
                plusTwoPlusFourMessage.setVisible(false);
                attackMessage.setVisible(false);

                if (table.isEmpty()) {
                    printCardsHumanByCases(card);
                    hasPlayerPlayed(true);

                } else if (card.getPath().contains("2_wild_draw")) {
                    if (table.getCurrentCardOnTheTable().getColor().equals(card.getColor())){
                        //Two cards are added to the machine´s array of cards
                        if(findCardMachine(card.getPath())){
                            printCardsHumanByCases(card);
                            changeMachinePlayer(card);
                            attackMessage.setText("La maquina ha contraatacado");
                            attackMessage.setVisible(true);
                            hasPlayerPlayed(false);

                        }else{
                            printCardsHumanByCases(card);
                            hasPlayerPlayed(true);
                            plusTwoPlusFourMessage.setText("Maquina: +2");
                            plusTwoPlusFourMessage.setVisible(true);
                            gameUno.eatCard(machinePlayer, 2);
                        }


                    }

                } else if (card.getPath().contains("4_wild_draw")) {
                    //Four cards are added to the machine´s array of cards
                    printCardsHumanByCases(card);
                    hasPlayerPlayed(true);
                    plusTwoPlusFourMessage.setText("Maquina: +4");
                    plusTwoPlusFourMessage.setVisible(true);
                    //gameUno.eatCard(machinePlayer, 4);

                } else if (card.getPath().contains("reserve_")) {
                    if (table.getCurrentCardOnTheTable().getColor().equals(card.getColor())) {
                        //Give the turn to machine on reverse
                        printCardsHumanByCases(card);
                        hasPlayerPlayed(true);
                    }

                } else if (card.getPath().contains("skip_")) {
                    if (table.getCurrentCardOnTheTable().getColor().equals(card.getColor())) {
                        //Skip turn to machine - stop
                        printCardsHumanByCases(card);
                        attackMessage.setText("Oponente Bloqueado\n       Vuelve a tirar");
                        attackMessage.setVisible(true);
                        hasPlayerPlayed(false);
                    }

                } else if (card.getPath().contains("wild")) {
                    printCardsHumanByCases(card);
                    hasPlayerPlayed(true);

                } else if (table.getCurrentCardOnTheTable().getColor().equals(card.getColor()) || table.getCurrentCardOnTheTable().getValue().equals(card.getValue())) {
                    printCardsHumanByCases(card);
                    hasPlayerPlayed(true);
                }

            });

            this.gridPaneCardsPlayer.add(cardImageView, i, 0);
        }
    }

    public void printCardsHumanByCases(Card card) {
        gameUno.playCard(card);
        tableImageView.setImage(card.getImage());
        humanPlayer.removeCard(findPosCardsHumanPlayer(card));
        printCardsHumanPlayer();
    }
r() {
    public void hasPlayerPlayed(boolean bool){
        threadPlayMachine.setHasPlayerPlayed(bool);
    }

    public void changeMachinePlayer(Card card){
        ThreadPlayMachine.putCardOnTableByPath(card);
    }


        private void printCardsMachinePlaye   Platform.runLater(() -> {
            this.gridPaneCardsMachine.getChildren().clear();
            Card[] currentVisibleCardsMachinePlayer = this.gameUno.getCurrentVisibleCardsMachinePlayer(this.posInitCardToShow1);

            for (int i = 0; i < currentVisibleCardsMachinePlayer.length; i++) {
                Card card = currentVisibleCardsMachinePlayer[i];
                Image cardMachine = new Image(getClass().getResourceAsStream(EISCUnoEnum.CARD_UNO.getFilePath()));
                ImageView cardImageView = new ImageView(cardMachine);

            cardImageView.setY(16);
            cardImageView.setFitHeight(90);
            cardImageView.setFitWidth(70);

            this.gridPaneCardsMachine.add(cardImageView, i, 0);
        }
    });
}
    /**
     * Finds the position of a specific card in the human player's hand.
     *
     * @param card the card to find
     * @return the position of the card, or -1 if not found
     */
    private Integer findPosCardsHumanPlayer(Card card) {
        for (int i = 0; i < this.humanPlayer.getCardsPlayer().size(); i++) {
            if (this.humanPlayer.getCardsPlayer().get(i).equals(card)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Handles the "Back" button action to show the previous set of cards.
     *
     * @param event the action event
     */
    @FXML
    void onHandleBack(ActionEvent event) {
        if (this.posInitCardToShow > 0) {
            this.posInitCardToShow--;
            this.gameUno.notifyObservers();
        }
    }

    @FXML
    void onHandleButtonExit(ActionEvent event) {
        System.exit(0);
    }

    /**
     * Handles the "Next" button action to show the next set of cards.
     *
     * @param event the action event
     */
    @FXML
    void onHandleNext(ActionEvent event) {
        if (this.posInitCardToShow < this.humanPlayer.getCardsPlayer().size() - 4) {
            this.posInitCardToShow++;
            this.gameUno.notifyObservers();
        }
    }

    /**
     * Handles the action of taking a card.
     *
     * @param event the action event
     */
    @FXML
    void onHandleTakeCard(ActionEvent event) {
        gameUno.eatCard(humanPlayer, 1);
        this.threadSingUNOMachine.setEat(true);
        this.gameUno.notifyObservers();
    }

    /**
     * Handles the action of saying "Uno".
     *
     * @param event the action event
     */
    @FXML
    void onHandleUno(ActionEvent event) {
        int uno = this.humanPlayer.getCardsPlayer().size();
        int unoMachine = this.machinePlayer.getCardsPlayer().size();
        if (uno == 1) {
            this.threadSingUNOMachine.setEat(false);
            unoButtonPressed.set(true);


        } else if(unoMachine==1) {
            gameUno.eatCard(machinePlayer, 1);
        }
        this.gameUno.notifyObservers();
    }

    public void showSayOneLabel() {
        Platform.runLater(() -> {
            sayOne.setVisible(true);
            unoButtonPressed.set(false);  // Reset the Uno button state

            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), evt -> {
                if (!unoButtonPressed.get()) {
                    System.out.println("The player did not press Uno in 3 seconds");
                }
                sayOne.setVisible(false);
                this.threadSingUNOMachine.setEat(true);
            }));
            timeline.setCycleCount(1);
            timeline.play();
        });
    }

    public void singUnoMachine() {
        Platform.runLater(() -> {
            gameUno.eatCard(humanPlayer, 1);
            this.threadSingUNOMachine.setEat(true);
        });
    }

    @Override
    public void update() {
        Platform.runLater(() -> {
            printCardsHumanPlayer();
            printCardsMachinePlayer();
        });
    }

    public void incrementPosInitCardToShow1() {
        Platform.runLater(() -> {
            this.posInitCardToShow1++;
            printCardsMachinePlayer();
        });
}
}
