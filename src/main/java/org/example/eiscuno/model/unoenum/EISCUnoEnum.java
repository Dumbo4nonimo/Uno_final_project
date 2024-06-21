package org.example.eiscuno.model.unoenum;

/**
 * Enum EISCUnoEnum
 *
 * This enum represents the various file paths for the images used in the EISC Uno game.
 */
public enum EISCUnoEnum {
    FAVICON("favicon.png"),
    UNO("images/uno.png"),
    BACKGROUND_UNO("images/background_uno.png"),
    BUTTON_UNO("images/button_uno.png"),
    CARD_UNO("cards-uno/card_uno.png"),
    DECK_OF_CARDS("cards-uno/deck_of_cards.png"),
    WILD("cards-uno/wild_change.png"),
    TWO_WILD_DRAW_BLUE("cards-uno/2_wild_draw_blue.png"),
    TWO_WILD_DRAW_GREEN("cards-uno/2_wild_draw_green.png"),
    TWO_WILD_DRAW_YELLOW("cards-uno/2_wild_draw_yellow.png"),
    TWO_WILD_DRAW_RED("cards-uno/2_wild_draw_red.png"),
    FOUR_WILD_DRAW("cards-uno/4_wild_draw.png"),
    SKIP_BLUE("cards-uno/skip_blue.png"),
    SKIP_YELLOW("cards-uno/skip_yellow.png"),
    SKIP_GREEN("cards-uno/skip_green.png"),
    SKIP_RED("cards-uno/skip_red.png"),
    RESERVE_BLUE("cards-uno/reserve_blue.png"),
    RESERVE_YELLOW("cards-uno/reserve_yellow.png"),
    RESERVE_GREEN("cards-uno/reserve_green.png"),
    RESERVE_RED("cards-uno/reserve_red.png"),
    GREEN_0("cards-uno/0_green.png"),
    GREEN_1("cards-uno/1_green.png"),
    GREEN_2("cards-uno/2_green.png"),
    GREEN_3("cards-uno/3_green.png"),
    GREEN_4("cards-uno/4_green.png"),
    GREEN_5("cards-uno/5_green.png"),
    GREEN_6("cards-uno/6_green.png"),
    GREEN_7("cards-uno/7_green.png"),
    GREEN_8("cards-uno/8_green.png"),
    GREEN_9("cards-uno/9_green.png"),
    YELLOW_0("cards-uno/0_yellow.png"),
    YELLOW_1("cards-uno/1_yellow.png"),
    YELLOW_2("cards-uno/2_yellow.png"),
    YELLOW_3("cards-uno/3_yellow.png"),
    YELLOW_4("cards-uno/4_yellow.png"),
    YELLOW_5("cards-uno/5_yellow.png"),
    YELLOW_6("cards-uno/6_yellow.png"),
    YELLOW_7("cards-uno/7_yellow.png"),
    YELLOW_8("cards-uno/8_yellow.png"),
    YELLOW_9("cards-uno/9_yellow.png"),
    BLUE_0("cards-uno/0_blue.png"),
    BLUE_1("cards-uno/1_blue.png"),
    BLUE_2("cards-uno/2_blue.png"),
    BLUE_3("cards-uno/3_blue.png"),
    BLUE_4("cards-uno/4_blue.png"),
    BLUE_5("cards-uno/5_blue.png"),
    BLUE_6("cards-uno/6_blue.png"),
    BLUE_7("cards-uno/7_blue.png"),
    BLUE_8("cards-uno/8_blue.png"),
    BLUE_9("cards-uno/9_blue.png"),
    RED_0("cards-uno/0_red.png"),
    RED_1("cards-uno/1_red.png"),
    RED_2("cards-uno/2_red.png"),
    RED_3("cards-uno/3_red.png"),
    RED_4("cards-uno/4_red.png"),
    RED_5("cards-uno/5_red.png"),
    RED_6("cards-uno/6_red.png"),
    RED_7("cards-uno/7_red.png"),
    RED_8("cards-uno/8_red.png"),
    RED_9("cards-uno/9_red.png");


    private final String filePath;
    private static final String PATH = "/org/example/eiscuno/";

    /**
     * Constructor for the EISCUnoEnum enum.
     *
     * @param filePath the file path of the image relative to the base directory
     */
    EISCUnoEnum(String filePath) {
        this.filePath = PATH + filePath;
    }

    /**
     * Gets the full file path of the image.
     *
     * @return the full file path of the image
     */
    public String getFilePath() {
        return filePath;
    }
}
