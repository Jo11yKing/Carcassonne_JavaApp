
package com.example.view;

import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import com.example.model.TileType;
import com.example.model.GameBoard;
import javafx.scene.transform.Rotate;

import java.util.Random;

public class StartGameDialog extends Stage {
    private TileType selectedTileType;
    private ImageView tileImageView;
    private GameBoard gameBoard;
    private int rotationAngle; // Новое поле для хранения текущего угла поворота

    public StartGameDialog(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
        this.rotationAngle = 0; // Изначально угол поворота равен 0
        setTitle("Начать игру");

        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        try {
            selectedTileType = getRandomTileType();

            String tilePath = "/com/example/java_fx_12_6_3/tiles/" + selectedTileType.name() + "0.png";
            Image tileImage = new Image(getClass().getResourceAsStream(tilePath));
            tileImageView = new ImageView(tileImage);
            tileImageView.setFitWidth(150);
            tileImageView.setFitHeight(150);

            String leftImagePath = "/com/example/java_fx_12_6_3/icons/left.png";
            Image leftImage = new Image(getClass().getResourceAsStream(leftImagePath));
            ImageView leftImageView = new ImageView(leftImage);
            leftImageView.setFitWidth(50);
            leftImageView.setFitHeight(50);
            Button leftButton = new Button();
            leftButton.setGraphic(leftImageView);
            leftButton.setOnAction(event -> rotateLeft()); // Назначаем действие для кнопки

            String cancelImagePath = "/com/example/java_fx_12_6_3/icons/skip.png";
            Image cancelImage = new Image(getClass().getResourceAsStream(cancelImagePath));
            ImageView cancelImageView = new ImageView(cancelImage);
            cancelImageView.setFitWidth(50);
            cancelImageView.setFitHeight(50);
            Button cancelButton = new Button();
            cancelButton.setGraphic(cancelImageView);
            cancelButton.setOnAction(event -> close());

            String rightImagePath = "/com/example/java_fx_12_6_3/icons/right.png";
            Image rightImage = new Image(getClass().getResourceAsStream(rightImagePath));
            ImageView rightImageView = new ImageView(rightImage);
            rightImageView.setFitWidth(50);
            rightImageView.setFitHeight(50);
            Button rightButton = new Button();
            rightButton.setGraphic(rightImageView);
            rightButton.setOnAction(event -> rotateRight()); // Назначаем действие для кнопки

            buttonBox.getChildren().addAll(leftButton, cancelButton, rightButton);

            root.getChildren().addAll(buttonBox, tileImageView);

            gameBoard.setSelectedTileType(selectedTileType);

        } catch (Exception e) {
            e.printStackTrace();
        }

        Scene scene = new Scene(root, 300, 300);
        setScene(scene);
        initModality(Modality.WINDOW_MODAL);
        setResizable(false);
    }

    public TileType getSelectedTileType() {
        return selectedTileType;
    }

    public void updateTile(int spriteCount) {
        selectedTileType = getRandomTileType();

        String tilePath = "/com/example/java_fx_12_6_3/tiles/" + selectedTileType.name() + "0.png";
        Image tileImage = new Image(getClass().getResourceAsStream(tilePath));
        tileImageView.setImage(tileImage);

        gameBoard.setSelectedTileType(selectedTileType);
        resetRotation(); // Сбрасываем поворот при обновлении плитки
    }

    private void rotateLeft() {
        rotationAngle = (rotationAngle - 90) % 360; // Уменьшаем угол поворота на 90 градусов
        tileImageView.setRotate(rotationAngle); // Применяем поворот
        System.out.println("Tile rotated to the left: " + rotationAngle + " degrees");
    }

    private void rotateRight() {
        rotationAngle = (rotationAngle + 90) % 360; // Увеличиваем угол поворота на 90 градусов
        tileImageView.setRotate(rotationAngle); // Применяем поворот
        System.out.println("Tile rotated to the right: " + rotationAngle + " degrees");
    }

    private void resetRotation() {
        rotationAngle = 0; // Сбрасываем угол поворота
        tileImageView.setRotate(rotationAngle); // Применяем сброс
    }

    public int getRotationAngle() {
        return rotationAngle;
    }

    private TileType getRandomTileType() {
        TileType[] tileTypes = TileType.values();
        Random random = new Random();
        return tileTypes[random.nextInt(tileTypes.length)];
    }
}
