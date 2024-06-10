package com.example.view;

import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.Node;
import com.example.model.Player;
import com.example.model.Tile;
import com.example.control.ControllerFacade;
import com.example.model.GridDirection;

public class MeeplePlacementView extends GridPane {
    private ControllerFacade controller;
    private Tile tile;
    private Player currentPlayer;

    public MeeplePlacementView(ControllerFacade controller, Tile tile, Player player) {
        super();
        this.controller = controller;
        this.tile = tile;
        this.currentPlayer = player;

        initializeUI();
    }

    private void initializeUI() {
        int offset = 1; // Смещение, чтобы избежать отрицательных индексов
        for (GridDirection direction : GridDirection.values()) {
            Button btn = new Button("Place Meeple " + direction);
            btn.setUserData(direction);
            btn.setOnAction(e -> attemptToPlaceMeeple(direction));
            // Установка индексов с учетом смещения
            int colIndex = direction.getX() + offset;
            int rowIndex = direction.getY() + offset;
            this.add(btn, colIndex, rowIndex);
        }
    }
    

    public void updateTileAndPlayer(Tile tile, Player currentPlayer) {
        this.tile = tile;
        this.currentPlayer = currentPlayer;
        updateButtons();
    }

    private void attemptToPlaceMeeple(GridDirection direction) {
        if (tile.canPlaceMeeple(direction, currentPlayer)) {
            controller.placeMeeple(tile, direction, currentPlayer);
        } else {
            System.out.println("Cannot place meeple at " + direction);
        }
    }

    private void updateButtons() {
        for (Node node : this.getChildren()) {
            if (node instanceof Button) {
                Button button = (Button) node;
                GridDirection direction = (GridDirection) button.getUserData();
                button.setDisable(!tile.canPlaceMeeple(direction, currentPlayer));
            }
        }
    }
}
