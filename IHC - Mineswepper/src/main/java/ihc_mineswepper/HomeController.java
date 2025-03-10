package ihc_mineswepper;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;

import java.io.IOException;

public class HomeController {

    @FXML public void startGame(ActionEvent event) {
        loadGame(event, "/ihc_mineswepper/game.fxml");
    }
    @FXML public void startGame2(ActionEvent event) {
        loadGame(event, "/ihc_mineswepper/game2.fxml");
    }
    @FXML public void startGame3(ActionEvent event) {
        loadGame(event, "/ihc_mineswepper/game3.fxml");
    }

    private void loadGame(ActionEvent event, String fxmlPath) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
            Scene scene = new Scene(fxmlLoader.load());
            scene.getStylesheets().add(getClass().getResource("/ihc_mineswepper/styles.css").toExternalForm());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Minesweeper");
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
