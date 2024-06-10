
package com.example.control;

import com.example.view.MainView;
import com.example.model.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ControllerFacade {
    private MainView mainView;
    private GameBoard gameBoard;
    private int currentPlayerIndex;
    private List<Player> players;
    private Player currentPlayer;
    private TileType selectedTileType;

    public ControllerFacade(MainView mainView) {
        this.mainView = mainView;
        this.players = new ArrayList<>();
        players.add(new Player("Player 1", Color.RED));
        players.add(new Player("Player 2", Color.BLUE));
        currentPlayerIndex = 0;
        this.gameBoard = new GameBoard(20, 20, this); // Инициализация игрового поля
        mainView.setGameBoard(this.gameBoard); // Передача ссылки на gameBoard в mainView
    }

    public void requestSkip() {
        System.out.println("Skip requested");
        nextTurn();
    }

    public void placeMeeple(Tile tile, GridDirection direction, Player player) {
        System.out.println("Placing meeple at " + direction);
        if (tile.canPlaceMeeple(direction, player)) {
            tile.setMeeple(player, direction);  // Установить мипл на плитке
            System.out.println("Meeple placed on " + tile + " at " + direction);
            player.placeMeeple();
            // Обновить состояние игры
            updateGameState();
            // Подсчитать очки
            calculateScores();
        } else {
            System.out.println("Cannot place meeple: invalid move or position already taken.");
        }
    }

    public void startNewGame() {
        System.out.println("Starting new game...");
        gameBoard = new GameBoard(20, 20, this); // Инициализация нового игрового поля
        mainView.setGameBoard(gameBoard); // Установка нового игрового поля
        gameBoard.reset(); // Сброс состояния игрового поля
        gameBoard.placeInitialSprite(); // Убедитесь, что вызывается правильно

        currentPlayerIndex = 0;
        for (Player player : players) {
            player.resetScore(); // Сброс счета каждого игрока
        }
        mainView.updateView(); // Обновление представления
        highlightInitialTiles(); // Подсветка начальных ячеек
        System.out.println("New game started successfully.");
    }

    public void highlightInitialTiles() {
        // Получение координат центральной плитки
        int centerX = gameBoard.getCols() / 2;
        int centerY = gameBoard.getRows() / 2;

        // Подсветка только соседних плиток вокруг центральной плитки
        gameBoard.highlightTileIfAbsent(centerX * 50 - 50, centerY * 50); // слева
        gameBoard.highlightTileIfAbsent(centerX * 50 + 50, centerY * 50); // справа
        gameBoard.highlightTileIfAbsent(centerX * 50, centerY * 50 - 50); // сверху
        gameBoard.highlightTileIfAbsent(centerX * 50, centerY * 50 + 50); // снизу
    }

    public boolean canPlaceMeeple(TerrainType terrain) {
        // Здесь должна быть логика определения возможности размещения мипла
        return true; // Пример реализации
    }

    private void updateGameState() {
        // Логика обновления состояния игры
        for (GridPattern pattern : gameBoard.getGrid().getAllPatterns()) {
            if (pattern.isComplete()) {
                pattern.disburse(true);
            }
        }
        nextTurn();
    }

    private void nextTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        mainView.updateView();
        checkForGameEnd();
    }

    private void checkForGameEnd() {
        if (gameBoard.getGrid().isFull()) {
            endGame();
        }
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public void setCurrentPlayer(Player player) {
        this.currentPlayer = player;
    }

    public TileType getSelectedTileType() {
        return selectedTileType;
    }

    public void setSelectedTileType(TileType selectedTileType) {
        this.selectedTileType = selectedTileType;
    }

    public List<Player> getPlayers() {
        return players;
    }

    private void endGame() {
        System.out.println("Game over!");
        Player winner = determineWinner();
        System.out.println("The winner is: " + winner.getName());
        mainView.showWinner(winner);
    }

    private Player determineWinner() {
        return players.stream().max(Comparator.comparingInt(Player::getScore)).orElse(null);
    }

    private void calculateScores() {
        for (Player player : players) {
            int score = gameBoard.calculatePlayerScore(player);
            player.setScore(score);
        }
        mainView.updateScoreDisplay(players); // Добавляем метод обновления отображения очков
    }
}
