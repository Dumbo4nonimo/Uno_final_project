package org.example.eiscuno.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.example.eiscuno.model.unoenum.EISCUnoEnum;

import java.io.IOException;

/**
 * Represents the main stage of the Uno game application.
 * This stage displays the game interface to the user.
 */
public class GameUnoStage extends Stage {

    /**
     * Constructs a new instance of GameUnoStage.
     *
     * @throws IOException if an error occurs while loading the FXML file for the game interface.
     */
    public GameUnoStage() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/eiscuno/game-uno-view.fxml"));
        Parent root;
        try {
            root = loader.load();
        } catch (IOException e) {
            // Re-throwing the caught IOException
            throw new IOException("Error while loading FXML file", e);
        }
        Scene scene = new Scene(root);

        // Configuring the stage
        setTitle("EISC Uno"); // Sets the title of the stage
        setScene(scene); // Sets the scene for the stage
        setResizable(false); // Disallows resizing of the stage
        Image back = new Image(getClass().getResourceAsStream(EISCUnoEnum.BACKGROUND_UNO.getFilePath()));
        ImageView background = new ImageView(back);
        getIcons().add(new Image(String.valueOf(getClass().getResource(EISCUnoEnum.FAVICON.getFilePath()))));
        ((Pane) root).getChildren().add(0, background);


        show(); // Displays the stage
    }

    /**
     * Closes the instance of GameUnoStage.
     * This method is used to clean up resources when the game stage is no longer needed.
     */
    public static void deleteInstance() {
        GameUnoStageHolder.INSTANCE.close();
        GameUnoStageHolder.INSTANCE = null;
    }

    /**
     * Retrieves the singleton instance of GameUnoStage.
     *
     * @return the singleton instance of GameUnoStage.
     * @throws IOException if an error occurs while creating the instance.
     */
    public static GameUnoStage getInstance() throws IOException {
        return GameUnoStageHolder.INSTANCE != null ?
                GameUnoStageHolder.INSTANCE :
                (GameUnoStageHolder.INSTANCE = new GameUnoStage());
    }

    /**
     * Holder class for the singleton instance of GameUnoStage.
     * This class ensures lazy initialization of the singleton instance.
     */
    private static class GameUnoStageHolder {
        private static GameUnoStage INSTANCE;
    }
}
