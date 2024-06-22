package org.example.eiscuno.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Label;
import javafx.util.Duration;
import org.example.eiscuno.model.alert.AlertBox;
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

/**
 * Controller for the UNO game.
 * Implements the Observer interface to update the UI based on game state changes.
 */
public class GameUnoController implements Observer {

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
    public Label plusTwoPlusFourMessage;

    @FXML
    public Label attackMessage;

    private Player humanPlayer;
    private Player machinePlayer;
    private Deck deck;
    private Table table;
    private GameUno gameUno;
    private int posInitCardToShow;

    private ThreadSingUNOMachine threadSingUNOMachine;
    private ThreadPlayMachine threadPlayMachine;
    private boolean controlButton = false;

    private final AtomicBoolean unoButtonPressed = new AtomicBoolean(false);
    private int posInitCardToShow1;
    private int colorChosen; // 1.Yellow  2.Red  3.Blue  4.Green

    /**
     * Initializes the controller.
     */
    @FXML
    public void initialize() {
        initVariables();
        setImages();
        this.gameUno.addObserver(this);
        this.gameUno.startGame();

        printCardsHumanPlayer();
        printCardsMachinePlayer();

        threadSingUNOMachine = new ThreadSingUNOMachine(this.humanPlayer.getCardsPlayer(), this);
        Thread t = new Thread(threadSingUNOMachine, "ThreadSingUNO");
        t.start();

        threadPlayMachine = new ThreadPlayMachine(this.table, this.machinePlayer, this.tableImageView, this, deck, this.gameUno);
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
        this.posInitCardToShow1 = 0;
    }

    /**
     * Sets the images for the UI components.
     */
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
     * Finds the index of a card in the machine player's hand based on its path.
     *
     * @param path The path of the card.
     * @return The index of the card in the machine player's hand, or -1 if not found.
     */
    private int findCardMachine(String path) {
        Card card;
        for (int i = 0; i < machinePlayer.getCardsPlayer().size(); i++) {
            card = machinePlayer.getCardsPlayer().get(i);
            if (card.getPath().equals(path)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Prints the human player's cards on the grid pane.
     */

    private void printCardsHumanPlayer() {

        this.gridPaneCardsPlayer.getChildren().clear();
        Card[] currentVisibleCardsHumanPlayer = this.gameUno.getCurrentVisibleCardsHumanPlayer(this.posInitCardToShow);

        // Check if the human player has won the game
        if (currentVisibleCardsHumanPlayer.length == 0) {
            AlertBox alertBox = new AlertBox();
            alertBox.showMessage("Congratulations!", "You've Won the Game!", "You're Awesome");
        }

        // Iterate through each visible card of the human player
        for (int i = 0; i < currentVisibleCardsHumanPlayer.length; i++) {
            Card card = currentVisibleCardsHumanPlayer[i];
            ImageView cardImageView = card.getCard();

            // Handle mouse click event on the card
            cardImageView.setOnMouseClicked((MouseEvent event) -> {
                // Clear any displayed messages
                plusTwoPlusFourMessage.setVisible(false);
                attackMessage.setVisible(false);

                if (table.isEmpty()) {
                    // If the table is empty, play the card directly
                    printCardsHumanByCases(card);
                    hasPlayerPlayed(true);
                    controlButton = true;

                } else if (card.getPath().contains("2_wild_draw")) {
                    // If the card is a +2, check if it matches the current table card color
                    if (table.getCurrentCardOnTheTable().getColor().equals(card.getColor())) {
                        int index = findCardMachine(card.getPath());
                        if (index != -1) {
                            // Handle the situation where the machine player can counterattack
                            printCardsHumanByCases(card);
                            machinePlayer.removeCard(index);
                            attackMessage.setText("The machine has counterattacked");
                            plusTwoPlusFourMessage.setText("You: +4");
                            plusTwoPlusFourMessage.setVisible(true);
                            attackMessage.setVisible(true);
                            gameUno.eatCard(humanPlayer, 4);
                            hasPlayerPlayed(false);

                        } else {
                            // Handle the situation where the card is played normally
                            printCardsHumanByCases(card);
                            plusTwoPlusFourMessage.setText("Machine: +2");
                            plusTwoPlusFourMessage.setVisible(true);
                            gameUno.eatCard(machinePlayer, 2);
                            hasPlayerPlayed(true);
                        }
                    }

                } else if (card.getPath().contains("4_wild_draw")) {
                    // If the card is a +4, add 4 cards to the machine player's deck
                    printCardsHumanByCases(card);
                    plusTwoPlusFourMessage.setText("Machine: +4");
                    plusTwoPlusFourMessage.setVisible(true);
                    gameUno.eatCard(machinePlayer, 4);
                    hasPlayerPlayed(true);

                } else if (card.getPath().contains("reserve_")) {
                    // Handle reserve card logic
                    if (table.getCurrentCardOnTheTable().getColor().equals(card.getColor())) {
                        printCardsHumanByCases(card);
                        hasPlayerPlayed(true);
                    }

                } else if (card.getPath().contains("skip_")) {
                    // Handle skip card logic
                    if (table.getCurrentCardOnTheTable().getColor().equals(card.getColor())) {
                        printCardsHumanByCases(card);
                        attackMessage.setText("Opponent Blocked\n       Turn Skipped");
                        attackMessage.setVisible(true);
                        hasPlayerPlayed(false);
                    }

                } else if (card.getPath().contains("wild_change")) {
                    // Handle wild card logic for color selection
                    showColorChoiceDialog();
                    gameUno.playCard(deck.getGhostCards().get(colorChosen));
                    tableImageView.setImage(card.getImage());
                    humanPlayer.removeCard(findPosCardsPlayer(humanPlayer, card));
                    printCardsHumanPlayer();
                    hasPlayerPlayed(true);

                } else if (table.getCurrentCardOnTheTable().getPath().contains("4_wild_draw")) {
                    // Handle playing a regular card on a +4 card
                    printCardsHumanByCases(card);
                    hasPlayerPlayed(true);
                } else if (table.getCurrentCardOnTheTable().getColor().equals(card.getColor()) ||
                        table.getCurrentCardOnTheTable().getValue().equals(card.getValue())) {
                    // Handle playing a matching color or value card
                    printCardsHumanByCases(card);
                    hasPlayerPlayed(true);
                }
            });

            // Add the card image view to the grid pane
            this.gridPaneCardsPlayer.add(cardImageView, i, 0);
        }
    }

    /**
     * Plays the specified card by the human player, updates the game state, and refreshes the displayed cards.
     *
     * @param card The card to be played by the human player.
     */
    public void printCardsHumanByCases(Card card) {
        gameUno.playCard(card); // Puts the user's card on the table's array
        tableImageView.setImage(card.getImage()); // Updates the table GUI with the card image
        humanPlayer.removeCard(findPosCardsPlayer(humanPlayer, card)); // Removes the card from the human player's hand
        printCardsHumanPlayer(); // Refreshes the displayed cards for the human player
    }

    /**
     * Sets whether the human player has made a move, affecting the gameplay flow.
     *
     * @param bool Boolean indicating if the player has made a move.
     */
    public void hasPlayerPlayed(boolean bool) {
        threadPlayMachine.setHasPlayerPlayed(bool);
    }

    /**
     * Displays Uno type cards on the machine player's deck in the GUI.
     */
    private void printCardsMachinePlayer() {
        Platform.runLater(() -> {
            this.gridPaneCardsMachine.getChildren().clear();
            Card[] currentVisibleCardsMachinePlayer = this.gameUno.getCurrentVisibleCardsMachinePlayer(this.posInitCardToShow1);

            // Iterate through each visible card of the machine player
            for (int i = 0; i < currentVisibleCardsMachinePlayer.length; i++) {
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
    private Integer findPosCardsPlayer(Player player, Card card) {
        for (int i = 0; i < player.getCardsPlayer().size(); i++) {
            if (player.getCardsPlayer().get(i).equals(card)) {
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


        } else if (unoMachine == 1) {
            gameUno.eatCard(machinePlayer, 1);
        }
        this.gameUno.notifyObservers();
    }

    /**
     * Displays a dialog box for the human player to choose a color.
     * Sets the chosen color index based on the player's selection.
     */
    private void showColorChoiceDialog() {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("Choose a color");
        alert.setHeaderText("Choose a color to continue:");

        ButtonType redButton = new ButtonType("Red");
        ButtonType yellowButton = new ButtonType("Yellow");
        ButtonType greenButton = new ButtonType("Green");
        ButtonType blueButton = new ButtonType("Blue");

        alert.getButtonTypes().setAll(redButton, yellowButton, greenButton, blueButton);

        alert.showAndWait().ifPresent(response -> {
            if (response == yellowButton) {
                System.out.println("Yellow chosen");
                colorChosen = 0; // Yellow color index
            } else if (response == redButton) {
                System.out.println("Red chosen");
                colorChosen = 1; // Red color index
            } else if (response == blueButton) {
                System.out.println("Blue chosen");
                colorChosen = 2; // Blue color index
            } else if (response == greenButton) {
                System.out.println("Green chosen");
                colorChosen = 3; // Green color index
            }
            // Close the alert dialog
            alert.close();
        });
    }

    /**
     * Handles the action when the "Skip Turn" button is clicked.
     *
     * @param event The action event triggered by clicking the "Skip Turn" button.
     */
    @FXML
    void skipTurnOnAction(ActionEvent event) {
        if(controlButton){
            // If control button is true, mark that the player has played and show skip message
            hasPlayerPlayed(true);
            attackMessage.setText("Turn skipped");
            attackMessage.setVisible(true);
        } else {
            // If control button is false, show an alert indicating it's too early to skip turn
            AlertBox alertBox = new AlertBox();
            alertBox.showMessage("Oops!","Don't hurry","It's too early to skip turn");
        }
    }

    /**
     * Shows a label for a specific duration and performs actions based on player interaction with the game.
     * It resets the Uno button state if not pressed within a set time frame.
     */
    public void showSayOneLabel() {
        Platform.runLater(() -> {
            sayOne.setVisible(true); // Display the label indicating "Say One"
            unoButtonPressed.set(false); // Reset the Uno button state

            // Create a timeline to hide the label after 3 seconds if Uno button is not pressed
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), evt -> {
                if (!unoButtonPressed.get()) {
                    System.out.println("The player did not press Uno in 3 seconds");
                }
                sayOne.setVisible(false); // Hide the "Say One" label
                this.threadSingUNOMachine.setEat(true); // Set the machine's turn to eat
            }));
            timeline.setCycleCount(1);
            timeline.play(); // Start the timeline
        });
    }

    /**
     * Makes the machine player say "Uno" and takes appropriate actions.
     */
    public void singUnoMachine() {
        Platform.runLater(() -> {
            gameUno.eatCard(humanPlayer, 1); // Make the machine player eat 1 card
            this.threadSingUNOMachine.setEat(true); // Set the machine's turn to eat
        });
    }

    /**
     * Updates the UI by refreshing the displayed cards for both human and machine players.
     */
    @Override
    public void update() {
        Platform.runLater(() -> {
            printCardsHumanPlayer(); // Refresh human player's cards
            printCardsMachinePlayer(); // Refresh machine player's cards
        });
    }

    /**
     * Increments the initial position of cards to show for the machine player and updates the UI.
     */
    public void incrementPosInitCardToShow1() {
        Platform.runLater(() -> {
            //this.posInitCardToShow1++;
            printCardsMachinePlayer(); // Refresh machine player's cards
        });
    }

    /**
     * Updates the UI to show the machine player's cards.
     */
    public void showMachineCards() {
        printCardsMachinePlayer(); // Refresh machine player's cards
    }

    /**
     * Makes the machine player eat 1 card and updates the UI accordingly.
     */
    public void eatMachineCards() {
        Platform.runLater(() -> {
            gameUno.eatCard(machinePlayer, 1); // Make the machine player eat 1 card
            printCardsMachinePlayer(); // Refresh machine player's cards
        });
    }
}
