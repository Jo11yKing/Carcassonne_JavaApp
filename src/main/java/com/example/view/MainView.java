package com.example.view;

import com.example.model.GameBoard;
import com.example.model.Player;
import com.example.control.ControllerFacade;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class MainView extends Application {
    private Stage primaryStage;
    private ControllerFacade controller;
    private BorderPane root;
    private GameBoard gameBoard;
    private StartGameDialog startGameDialog;
    private Label scoreLabel; // Новый Label для отображения очков

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.root = new BorderPane();
        this.controller = new ControllerFacade(this);

        initializeUI();
    }

    private void initializeUI() {
        root.setTop(createMenuBar());
        scoreLabel = new Label(); // Инициализация нового Label
        updateScoreLabel();
        VBox topContainer = new VBox(createMenuBar(), scoreLabel); // Добавление Label в верхний контейнер
        root.setTop(topContainer);

        this.gameBoard = new GameBoard(20, 20, this.controller);
        setGameBoard(this.gameBoard);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Carcassonne Game");
        primaryStage.show();
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();
        Menu gameMenu = new Menu("Game");

        MenuItem newGameItem = new MenuItem("New Game");
        newGameItem.setOnAction(event -> controller.startNewGame());
        gameMenu.getItems().add(newGameItem);

        MenuItem startGameDialogItem = new MenuItem("Start Game Dialog");
        startGameDialogItem.setOnAction(event -> {
            if (startGameDialog == null || !startGameDialog.isShowing()) {
                startGameDialog = new StartGameDialog(gameBoard);
                startGameDialog.show();
            }
        });
        gameMenu.getItems().add(startGameDialogItem);

        MenuItem exitItem = new MenuItem("Exit");
        exitItem.setOnAction(event -> primaryStage.close());
        gameMenu.getItems().add(exitItem);

        menuBar.getMenus().add(gameMenu);
        return menuBar;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void updateView() {
        if (gameBoard != null) {
            gameBoard.drawBoard();
            updateScoreLabel(); // Обновление очков при обновлении представления
        } else {
            System.out.println("GameBoard is not initialized!");
        }
    }

    public void setGameBoard(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
        root.setCenter(gameBoard);
    }

    public void showWinner(Player winner) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText(null);
        alert.setContentText("The winner is: " + winner.getName());
        alert.showAndWait();
    }

    // Новый метод для обновления текста в Label с очками
    private void updateScoreLabel() {
        StringBuilder scores = new StringBuilder("Scores: ");
        for (Player player : controller.getPlayers()) {
            scores.append(player.getName()).append(": ").append(player.getScore()).append(" ");
        }
        scoreLabel.setText(scores.toString());
    }

    // Метод обновления отображения очков
    public void updateScoreDisplay(List<Player> players) {
        updateScoreLabel();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
