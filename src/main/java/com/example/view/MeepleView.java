package com.example.view;

import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;

import com.example.control.ControllerFacade;
import com.example.model.Player;
import com.example.model.GridDirection;
import com.example.model.Tile;
import com.example.model.TileType; 
import com.example.model.TerrainType;
import com.example.util.ImageLoadingUtil;

public class MeepleView extends Stage {
    private GridPane gridPane = new GridPane();
    private Tile tile;
    private ControllerFacade controller;
    private Player currentPlayer;
    private java.util.Map<GridDirection, Button> meepleButtons = new java.util.HashMap<>();
    private Color defaultButtonColor = Color.WHITE;

    public MeepleView(ControllerFacade controller, MainView ui) {
        super();
        this.controller = controller;
        setTitle("Meeple Placement");
        setScene(new Scene(gridPane));
        buildButtonSkip();
        buildButtonGrid();
    }

    public void setTile(Tile tile, Player currentPlayer) {
        this.tile = tile;
        this.currentPlayer = currentPlayer;
        updatePlacementButtons();
        show();
    }

    private void buildButtonGrid() {
        int index = 0;
        for (GridDirection direction : GridDirection.values()) {
            Button button = new Button("Place Meeple " + direction.toReadableString());
            button.setOnAction(e -> controller.placeMeeple(tile, direction, currentPlayer));
            GridPane.setConstraints(button, index % 3, index / 3 + 1);
            gridPane.add(button, index % 3, index / 3 + 1);
            meepleButtons.put(direction, button);
            index++;
        }
    }

    private void buildButtonSkip() {
        Button buttonSkip = new Button("Skip");
        buttonSkip.setOnAction(e -> controller.requestSkip());
        gridPane.add(buttonSkip, 0, 0, 3, 1);
    }

    private void updatePlacementButtons() {
        for (GridDirection direction : GridDirection.values()) {
            TerrainType terrain = tile.getTerrain(direction);
            boolean placeable = tile.hasMeepleSpot(direction) && controller.canPlaceMeeple(terrain);
            Button button = meepleButtons.get(direction);
            if (placeable) {
                button.setGraphic(new ImageView(ImageLoadingUtil.loadImage(getTileImagePath(tile.getType()))));
            } else {
                button.setGraphic(new ImageView(ImageLoadingUtil.loadImage(getTileImagePath(TileType.Null))));
            }
            button.setDisable(!placeable);
            button.setStyle("-fx-background-color: " + (placeable ? defaultButtonColor.toString() : currentPlayer.getLightColor().toString()));
        }
    }

    private String getTileImagePath(TileType tileType) {
        // Return the path to the image for the given tile type
        return "/com/example/java_fx_12_6_3/tiles/" + tileType.name().toLowerCase() + "0.png";
    }
}
