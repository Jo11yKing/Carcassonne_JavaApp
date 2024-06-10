package com.example.model;

import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;
import com.example.control.ControllerFacade;
import com.example.view.MeeplePlacementView;
import com.example.view.StartGameDialog;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GameBoard extends Pane {

    private Tile[][] tiles; // Сетка тайлов
    private List<Meeple> meeples; // Список фигурок
    private final int rows;
    private final int cols;
    private double dragStartX;
    private double dragStartY;
    private final Scale scaleTransform;
    private int tileCount;
    private Set<ImageView> highlightedTiles;
    private Grid grid;
    private ControllerFacade controller;  // Добавляем controller как поле
    private Player currentPlayer;
    private TileType selectedTileType; // Новое поле для хранения выбранного типа плитки
    private StartGameDialog startGameDialog; // Новое поле для хранения ссылки на диалоговое окно
    private int spriteCount = 0;

    public GameBoard(int rows, int cols, ControllerFacade controller) {
        tiles = new Tile[cols][rows];
        meeples = new ArrayList<>();
        this.rows = rows;
        this.cols = cols;
        this.scaleTransform = new Scale(1, 1);
        this.getTransforms().add(scaleTransform);
        this.tileCount = 0;
        this.highlightedTiles = new HashSet<>();
        this.grid = new Grid(rows, cols, true);  // Инициализируем сетку
        this.controller = controller;  // Инициализируем controller
        setPrefSize(1000, 1000); // Установите предпочтительный размер панели
        setBackground(new Background(new BackgroundImage(loadImage("Null1.png"),
                BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
        drawBoard();
        placeInitialSprite();
    }

    public void setStartGameDialog(StartGameDialog startGameDialog) {
        this.startGameDialog = startGameDialog;
    }

    private Image loadTileImage(TileType tileType) {
        try {
            return new Image(new FileInputStream("src/main/resources/com/example/java_fx_12_6_3/tiles/" + tileType.name() + "0.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Image loadImage(String fileName) {
        try {
            return new Image(new FileInputStream("src/main/resources/com/example/java_fx_12_6_3/tiles/" + fileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setCurrentPlayer(Player player) {
        this.currentPlayer = player;
    }

    public void setSelectedTileType(TileType selectedTileType) {
        this.selectedTileType = selectedTileType;
    }

    public void handleTileClick(int x, int y) {
        double clickX = x * 50;
        double clickY = y * 50;
        Tile tile = tiles[x][y];
        ImageView imageView = findTile(clickX, clickY);

        System.out.println("Click at: (" + x + ", " + y + ")");
        System.out.println("Tile is null: " + (tile == null));
        System.out.println("ImageView is null: " + (imageView == null));
        System.out.println("Is highlighted: " + highlightedTiles.contains(imageView));
        System.out.println("Selected Tile Type: " + (selectedTileType == null ? "None" : selectedTileType.name()));

        if (highlightedTiles.contains(imageView)) {
            if (selectedTileType != null && selectedTileType != TileType.Null) {
                replaceTileWithSelectedType(clickX, clickY, imageView, selectedTileType);
            } else {
                System.out.println("No valid tile type selected for placement.");
            }
        }
    }

    private void replaceTileWithSelectedType(double clickX, double clickY, ImageView imageView, TileType selectedTileType) {
        // Удаление текущего изображения плитки
        getChildren().remove(imageView);
        // Создание нового изображения плитки
        imageView = createTile(selectedTileType, clickX, clickY);
        getChildren().add(imageView);

        // Расчет координат в сетке
        int gridX = (int) (clickX / 50);
        int gridY = (int) (clickY / 50);

        // Обновление сетки с новым типом плитки
        if (grid.getSpot(gridX, gridY) != null) {
            grid.getSpot(gridX, gridY).place(new Tile(selectedTileType), true);
        }
        // Создание и добавление нового объекта Tile в массив
        Tile newTile = new Tile(selectedTileType);
        tiles[gridX][gridY] = newTile;

        // Обновление спрайта в StartGameDialog и перевыбор типа плитки
        if (startGameDialog != null) {
            startGameDialog.updateTile(spriteCount++);
        }
    }

    public void drawBoard() {
        this.getChildren().clear(); // Очистка всех существующих элементов перед добавлением новых
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                final int finalCol = col; // Создание финальной переменной для колонки
                final int finalRow = row; // Создание финальной переменной для строки
                tiles[finalCol][finalRow] = new Tile(TileType.Null); // Убедитесь, что каждый тайл инициализирован
                ImageView imageView = createTile(TileType.Null, finalCol * 50, finalRow * 50);
                imageView.setOnMouseClicked(event -> handleTileClick(finalCol, finalRow));
                this.getChildren().add(imageView);
            }
        }
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    private void highlightInitialTiles() {
        int centerX = cols / 2;
        int centerY = rows / 2;

        // Подсветка только соседних плиток вокруг центральной
        highlightTileIfAbsent((centerX - 1) * 50, centerY * 50); // слева
        highlightTileIfAbsent((centerX + 1) * 50, centerY * 50); // справа
        highlightTileIfAbsent(centerX * 50, (centerY - 1) * 50); // сверху
        highlightTileIfAbsent(centerX * 50, (centerY + 1) * 50); // снизу
    }

    public void placeInitialSprite() {
        int centerX = (cols / 2) * 50;
        int centerY = (rows / 2) * 50;
        ImageView centerTile = createTile(TileType.CastleCenter, centerX, centerY);
        getChildren().add(centerTile);
        highlightInitialTiles(); // Проверьте, вызывается ли этот метод
    }

    private ImageView createTile(TileType tileType, double x, double y) {
        ImageView imageView = new ImageView(loadTileImage(tileType));
        imageView.setFitWidth(50);
        imageView.setFitHeight(50);
        imageView.setX(x);
        imageView.setY(y);
        tileCount++;

        int gridX = (int) x / 50;
        int gridY = (int) y / 50;

        // Создание объекта Tile и добавление его в массив tiles
        Tile newTile = new Tile(tileType);
        tiles[gridX][gridY] = newTile;

        if (tileType == TileType.Null) {
            imageView.setOnMouseClicked(event -> placeTileFromDialog(imageView));
        } else {
            addSurroundingTiles(x, y); // Добавление соседних плиток при создании нового тайла
        }

        System.out.println(String.format("Tile of type %s created at (%f, %f)", tileType.name(), x, y));
        return imageView;
    }

    private void addSurroundingTiles(double centerX, double centerY) {
        System.out.println("Adding surrounding tiles around (" + centerX + ", " + centerY + ")");
        highlightTileIfAbsent(centerX - 50, centerY);
        highlightTileIfAbsent(centerX + 50, centerY);
        highlightTileIfAbsent(centerX, centerY - 50);
        highlightTileIfAbsent(centerX, centerY + 50);
    }

    public void highlightTileIfAbsent(double x, double y) {
        ImageView tile = findTile(x, y);
        if (tile == null) {
            tile = createTile(TileType.Null, x, y);
            this.getChildren().add(tile);
        }
        applyHighlightEffect(tile); // Применение эффекта подсветки
        highlightedTiles.add(tile); // Добавляем плитку в подсвеченные
    }

    private ImageView findTile(double x, double y) {
        return this.getChildren().stream()
                .filter(node -> node instanceof ImageView && ((ImageView) node).getX() == x && ((ImageView) node).getY() == y)
                .map(node -> (ImageView) node)
                .findFirst()
                .orElse(null);
    }

    private void applyHighlightEffect(ImageView tile) {
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(15);
        dropShadow.setColor(Color.YELLOW);
        tile.setEffect(dropShadow);
    }

    private void highlightTile(ImageView tile) {
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(15);
        dropShadow.setColor(Color.YELLOW);
        tile.setEffect(dropShadow);
        highlightedTiles.add(tile);
        System.out.println(String.format("Tile at (%f, %f) highlighted", tile.getX(), tile.getY()));
    }

    private void removeHighlight(ImageView tile) {
        tile.setEffect(null);
        highlightedTiles.remove(tile);
        System.out.println(String.format("Highlight removed from tile at (%f, %f)", tile.getX(), tile.getY()));
    }

    public void placeTileFromDialog(ImageView targetImageView) {
        TileType selectedTileType = controller.getSelectedTileType();
        if (selectedTileType != null && highlightedTiles.contains(targetImageView)) {
            removeHighlight(targetImageView);
            double x = targetImageView.getX();
            double y = targetImageView.getY();
            int gridX = (int) x / 50;
            int gridY = (int) y / 50;
            if (grid.isOnGrid(gridX, gridY)) {
                ImageView newTile = createTile(selectedTileType, x, y);
                getChildren().remove(targetImageView);
                getChildren().add(newTile); // Добавляем новую плитку вместо старой

                GridSpot spot = grid.getSpot(gridX, gridY);
                spot.place(new Tile(selectedTileType), grid.isAllowingEnclaves());

                addSurroundingTiles(x, y);

                logAllTiles();

                // Обновление типа плитки и спрайта в StartGameDialog
                if (startGameDialog != null) {
                    startGameDialog.updateTile(spriteCount++);
                }
            } else {
                System.out.println("Coordinates out of grid: x=" + gridX + " & y=" + gridY);
            }
        }
    }

    public void reset() {
        this.getChildren().clear();
        this.grid = new Grid(rows, cols, true);  // Сброс сетки
        drawBoard();
    }

    public void startDrag(MouseEvent event) {
        dragStartX = event.getSceneX();
        dragStartY = event.getSceneY();
        System.out.println(String.format("Drag started at (%f, %f)", dragStartX, dragStartY));
    }

    public void drag(MouseEvent event) {
        double offsetX = event.getSceneX() - dragStartX;
        double offsetY = event.getSceneY() - dragStartY;

        this.setLayoutX(this.getLayoutX() + offsetX);
        this.setLayoutY(this.getLayoutY() + offsetY);

        dragStartX = event.getSceneX();
        dragStartY = event.getSceneY();

        ensureTiles();

        System.out.println(String.format("Dragged to new position (%f, %f)", this.getLayoutX(), this.getLayoutY()));
    }

    public void zoom(ScrollEvent event) {
        double scaleFactor = (event.getDeltaY() > 0) ? 1.1 : 0.9;
        scaleTransform.setX(scaleTransform.getX() * scaleFactor);
        scaleTransform.setY(scaleTransform.getY() * scaleFactor);
        System.out.println(String.format("Zoom event: scaleFactor=%f", scaleFactor));
    }

    private void ensureTiles() {
        double minX = getBoundsInParent().getMinX();
        double minY = getBoundsInParent().getMinY();
        double maxX = getBoundsInParent().getMaxX();
        double maxY = getBoundsInParent().getMaxY();

        if (minX > -100) {
            addColumnLeft();
        }
        if (minY > -100) {
            addRowTop();
        }
        if (maxX < getWidth() + 100) {
            addColumnRight();
        }
        if (maxY < getHeight() + 100) {
            addRowBottom();
        }

        System.out.println(String.format("ensureTiles: minX=%f, minY=%f, maxX=%f, maxY=%f", minX, minY, maxX, maxY));
    }

    private void addColumnLeft() {
        for (int row = 0; row < rows; row++) {
            highlightTileIfAbsent(getBoundsInParent().getMinX() - 50, row * 50);
        }
    }

    private void addColumnRight() {
        for (int row = 0; row < rows; row++) {
            highlightTileIfAbsent(getBoundsInParent().getMaxX(), row * 50);
        }
    }

    private void addRowTop() {
        for (int col = 0; col < cols; col++) {
            highlightTileIfAbsent(col * 50, getBoundsInParent().getMinY() - 50);
        }
    }

    private void addRowBottom() {
        for (int col = 0; col < cols; col++) {
            highlightTileIfAbsent(col * 50, getBoundsInParent().getMaxY());
        }
    }

    private void logAllTiles() {
        getChildren().forEach(node -> {
            if (node instanceof ImageView) {
                ImageView imageView = (ImageView) node;
                System.out.println(String.format("Tile at (%f, %f) with type %s", imageView.getX(), imageView.getY(), imageView.getImage().getUrl()));
            }
        });
    }

    public int getTileCount() {
        return tileCount;
    }

    public Grid getGrid() {
        return grid;
    }

    public int calculatePlayerScore(Player player) {
        int score = 0;
        // Логика подсчета очков игрока, например, на основе количества миплов и захваченных территорий
        for (GridPattern pattern : grid.getAllPatterns()) {
            if (pattern.getMeepleList().stream().anyMatch(meeple -> meeple.getOwner().equals(player))) {
                score += pattern.getPatternScore();
            }
        }
        return score;
    }
}
