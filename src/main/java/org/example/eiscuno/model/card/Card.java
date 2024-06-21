package org.example.eiscuno.model.card;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Represents a card in the Uno game.
 */
public class Card {
    private String path;
    private String value;
    private String color;
    private boolean isSpecial;
    private Image image;
    private ImageView cardImageView;

    /**
     * Constructs a Card with the specified image URL and name.
     *
     * @param path the URL of the card image
     * @param value of the card
     */
    public Card(String path, String value, String color, boolean isSpecial) {
        this.path = path;
        this.value = value;
        this.color = color;
        this.isSpecial = isSpecial;
        this.image = new Image(String.valueOf(getClass().getResource(path)));
        this.cardImageView = createCardImageView();
    }

    /**
     * Creates and configures the ImageView for the card.
     *
     * @return the configured ImageView of the card
     */
    private ImageView createCardImageView() {
        ImageView card = new ImageView(this.image);
        card.setY(16);
        card.setFitHeight(90);
        card.setFitWidth(70);
        return card;
    }



    /**
     * Gets the ImageView representation of the card.
     *
     * @return the ImageView of the card
     */
    public ImageView getCard() {
        return cardImageView;
    }

    /**
     * Gets the image of the card.
     *
     * @return the Image of the card
     */
    public Image getImage() {
        return image;
    }

    public String getValue() {
        return value;
    }

    public boolean getIsSpecial(){
        return isSpecial;
    }

    public String getColor() {
            return color;
    }

    public String getPath() {return path;}
}
