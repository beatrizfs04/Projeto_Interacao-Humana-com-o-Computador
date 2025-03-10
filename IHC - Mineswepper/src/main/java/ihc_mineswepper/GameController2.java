package ihc_mineswepper;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import javafx.util.Duration;


import java.io.IOException;
import java.util.Random;

public class GameController2 {

    @FXML private Button button00, button01, button02, button03, button04, button05, button06, button07;
    @FXML private Button button10, button11, button12, button13, button14, button15, button16, button17;
    @FXML private Button button20, button21, button22, button23, button24, button25, button26, button27;
    @FXML private Button button30, button31, button32, button33, button34, button35, button36, button37;
    @FXML private Button button40, button41, button42, button43, button44, button45, button46, button47;
    @FXML private Button button50, button51, button52, button53, button54, button55, button56, button57;
    @FXML private Button button60, button61, button62, button63, button64, button65, button66, button67;
    @FXML private Button button70, button71, button72, button73, button74, button75, button76, button77;
    @FXML private Label timeLabel;
    private Timeline timeline;
    private int timeInSeconds = 0;
    private boolean gameOver = false;
    private boolean gameWin = false;
    private boolean gameFlagOver = false;
    @FXML private Label minesLeftLabel;
    private int numberOfMines = 10;
    private int[][] mines;
    private Button[][] buttons;
    private final int rows = 8;
    private final int cols = 8;

    public void initialize() {
        buttons = new Button[][]{
                {button00, button01, button02, button03, button04, button05, button06, button07},
                {button10, button11, button12, button13, button14, button15, button16, button17},
                {button20, button21, button22, button23, button24, button25, button26, button27},
                {button30, button31, button32, button33, button34, button35, button36, button37},
                {button40, button41, button42, button43, button44, button45, button46, button47},
                {button50, button51, button52, button53, button54, button55, button56, button57},
                {button60, button61, button62, button63, button64, button65, button66, button67},
                {button70, button71, button72, button73, button74, button75, button76, button77}
        };
        createBoard();

        timeline = new Timeline(new KeyFrame(Duration.seconds(1), this::updateTimer));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    @FXML public void restartGame(ActionEvent event) {
        restartGame();
    }

    private void createBoard() {
        mines = new int[rows][cols];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Button button = buttons[row][col];
                final int r = row;
                final int c = col;

                button.setOnMouseClicked(event -> {
                    if (event.getButton() == MouseButton.SECONDARY) {
                        placeFlags(r, c);
                    } else {
                        handleButtonPress(r, c);
                    }
                });

                button.setOnAction(event -> handleButtonPress(r, c));
            }
        }
        placeMines();
        calculateAdjacentNumbers();
    }

    @FXML private void placeFlags(int row, int col) {
        Button button = buttons[row][col];

        if (numberOfMines > 0 || button.getText().equals("X")) {
            if (!button.getText().equals("X")) {
                if(mines[row][col] == -1){
                    button.setText("X");
                    numberOfMines--;
                    if(numberOfMines == 0){
                        gameWin();
                    }
                } else {
                    gameFlagOver();
                }
            } else {
                button.setText("");
                numberOfMines++;
            }
            minesLeftLabel.setText(String.valueOf(numberOfMines));
        }
    }


    private void placeMines() {
        Random random = new Random();
        int placedMines = 0;

        while (placedMines < numberOfMines) {
            int row = random.nextInt(rows);
            int col = random.nextInt(cols);


            if (mines[row][col] == 0) {
                mines[row][col] = -1;
                placedMines++;
            }
        }
    }

    private void calculateAdjacentNumbers() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (mines[row][col] == -1) {
                    continue;
                }
                int count = 0;
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        int newRow = row + i;
                        int newCol = col + j;
                        if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols && mines[newRow][newCol] == -1) {
                            count++;
                        }
                    }
                }
                mines[row][col] = count;
            }
        }
    }

    @FXML private void handleButtonPress(int row, int col) {
        if (mines[row][col] == -1) {
            buttons[row][col].setText("X");
            buttons[row][col].setStyle("-fx-background-color: red;");
            gameOver();
        } else if (mines[row][col] == 0) {
            revealZeros(row, col);
        } else {
            buttons[row][col].setText(String.valueOf(mines[row][col]));
            buttons[row][col].setDisable(true);
        }
    }

    @FXML public void handleButtonPress(javafx.event.ActionEvent event) {
        Button pressedButton = (Button) event.getSource();
        String id = pressedButton.getId();

        int row = Integer.parseInt(id.substring(6, 7));
        int col = Integer.parseInt(id.substring(7, 8));

        if (mines[row][col] == -1) {
            pressedButton.setText("X");
            pressedButton.setStyle("-fx-background-color: red;");
            gameOver();
        } else if (mines[row][col] == 0) {
            revealZeros(row, col);
        } else {
            pressedButton.setText(String.valueOf(mines[row][col]));
            pressedButton.setDisable(true);
            pressedButton.setStyle(null);
        }
    }

    private void revealZeros(int row, int col) {
        if (row < 0 || row >= rows || col < 0 || col >= cols || buttons[row][col].isDisabled()) {
            return;
        }

        buttons[row][col].setText("");
        buttons[row][col].setDisable(true);

        if (mines[row][col] != 0) {
            buttons[row][col].setText(String.valueOf(mines[row][col]));
            return;
        }

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i != 0 || j != 0) {
                    revealZeros(row + i, col + j);
                }
            }
        }
    }

    private void updateTimer(ActionEvent event) {
        if (!gameOver || !gameWin || !gameFlagOver) {
            timeInSeconds++;
            timeLabel.setText(String.format("%02d:%02d", timeInSeconds / 60, timeInSeconds % 60));
        }
    }

    private void gameWin() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Winner!");
        alert.setHeaderText(null);
        alert.setContentText("Parabéns! Ganhou o jogo.");
        alert.showAndWait();
        timeline.stop();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                buttons[row][col].setDisable(true);
                if (mines[row][col] == -1) {
                    buttons[row][col].setText("X");  // X para bomba
                    buttons[row][col].setStyle("-fx-background-color: green;");
                }
            }
        }
    }

    private void gameFlagOver() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Game Over!");
        alert.setHeaderText(null);
        alert.setContentText("Colocou mal uma bandeira! Fim do jogo.");
        alert.showAndWait();
        timeline.stop();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                buttons[row][col].setDisable(true);
                if (mines[row][col] == -1) {
                    buttons[row][col].setText("X");  // X para bomba
                    buttons[row][col].setStyle("-fx-background-color: red;");
                }
            }
        }
    }


    private void gameOver() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText(null);
        alert.setContentText("Clicou numa mina! Fim do jogo.");
        alert.showAndWait();
        timeline.stop();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                buttons[row][col].setDisable(true);
                if (mines[row][col] == -1) {
                    buttons[row][col].setText("X");  // X para bomba
                    buttons[row][col].setStyle("-fx-background-color: red;");
                }
            }
        }
    }

    @FXML public void restartGame() {
        numberOfMines = 10;
        minesLeftLabel.setText(String.valueOf(numberOfMines));
        timeInSeconds = 0;
        gameOver = false;
        gameWin = false;
        gameFlagOver = false;

        for (Button[] row : buttons) {
            for (Button button : row) {
                button.setText("");
                button.setDisable(false);
                button.setStyle(null);
            }
        }

        mines = new int[rows][cols];
        createBoard();

        timeLabel.setText("00:00");
        timeline.play();
    }

    @FXML public void backToHome(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("home.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Home");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
