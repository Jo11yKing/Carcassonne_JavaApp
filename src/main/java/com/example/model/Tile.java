package com.example.model;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.HashMap;
import java.util.Map;

public class Tile extends StackPane {

    private final TileType type;
    private final TileTerrain terrain;
    private GridSpot gridSpot;
    private Meeple meeple;
    private static final int CASTLE_THRESHOLD = 6;
    private Map<GridDirection, Boolean> meeplePlacements;
    private boolean hasMeeple; // Флаг наличия мипла на плитке
    private int rotation; // Угол поворота плитки

    public Tile(TileType type) {
        if (type == null) {
            throw new IllegalArgumentException("Tile type cannot be null");
        }
        meeplePlacements = new HashMap<>();
        for (GridDirection dir : GridDirection.values()) {
            meeplePlacements.put(dir, false); // Никакие миплы изначально не размещены
        }
        this.type = type;
        this.terrain = new TileTerrain(type);
        initializeTile();
        this.rotation = 0; // Инициализация с поворотом 0 градусов
    }

    private void initializeTile() {
        Rectangle base = new Rectangle(50, 50);
        base.setFill(Color.LIGHTGRAY);
        this.getChildren().add(base); // Визуальное представление тайла
    }

    // Метод для установки поворота плитки
    public void setRotation(int rotation) {
        this.rotation = rotation;
        this.setRotate(rotation); // Применяем поворот к визуальному элементу
    }


    public int getRotation() {
        return rotation;
    }
    public TileType getType() {
        return type;
    }

    public GridSpot getGridSpot() {
        return gridSpot;
    }

    public void setGridSpot(GridSpot gridSpot) {
        this.gridSpot = gridSpot;
    }

    public boolean hasMeeple() {
        return meeple != null;
    }

    public void removeMeeple() {
        this.meeple = null;
    }

    public Meeple getMeeple() {
        return meeple;
    }

    public void setMeeple(Meeple meeple) {
        this.meeple = meeple;
        this.getChildren().add(meeple); // Добавляем мипла на тайл
    }

    public TileTerrain getTerrain() {
        return terrain;
    }

    public TerrainType getTerrain(GridDirection direction) {
        return terrain.getTerrain(direction);
    }

    public boolean hasConnection(GridDirection from, GridDirection to) {
        return terrain.hasConnection(from, to);
    }

    public void setPosition(GridSpot spot) {
        if (spot == null) {
            throw new IllegalArgumentException("Position can't be null, tile cannot be removed.");
        }
        gridSpot = spot;
    }

    public final boolean hasEmblem() {
        int castleSize = 0;
        for (GridDirection direction : GridDirection.values()) {
            if (terrain.getTerrain(direction).equals(TerrainType.CASTLE)) {
                castleSize++;
            }
        }
        return castleSize >= CASTLE_THRESHOLD;
    }

    public boolean canConnectTo(GridDirection direction, Tile otherTile) {
        TerrainType thisTerrain = this.getTerrain(direction);
        TerrainType otherTerrain = otherTile.getTerrain(direction.opposite());
        return thisTerrain == otherTerrain;
    }

    public boolean canPlaceMeeple(GridDirection direction, Player player) {
        return !this.isMeeplePlaced(direction) && player.hasFreeMeeples();
    }

    public boolean canPlaceMeeple(Player player) {
        // Пример проверки, если хотя бы одно направление подходит для размещения мипла
        for (GridDirection direction : GridDirection.values()) {
            if (canPlaceMeeple(direction, player)) {
                return true;
            }
        }
        return false;
    }

    private boolean isTerritoryOccupied(GridDirection direction) {
        return meeplePlacements.get(direction);
    }

    public void placeMeeple(GridDirection direction, Player player) {
        if (canPlaceMeeple(direction, player)) {
            meeplePlacements.put(direction, true);
            Meeple meeple = new Meeple(player);
            this.setMeeple(meeple); // Визуальное добавление мипла на тайл
            player.placeMeeple();
        } else {
            System.out.println("Meeple cannot be placed in this direction by " + player.getName());
        }
    }

    public void removeMeeple(GridDirection direction) {
        meeplePlacements.put(direction, false);
        this.meeple = null;
    }

    public boolean isMeeplePlaced(GridDirection direction) {
        return meeplePlacements.getOrDefault(direction, false);
    }

    public boolean hasMeepleSpot(GridDirection direction) {
        return true;
    }

    public void setMeeple(Player player, GridDirection direction) {
        Meeple meeple = new Meeple(player);
        this.meeple = meeple;
        this.getChildren().add(meeple); // Визуальное добавление мипла на тайл
    }

    @Override
    public String toString() {
        return type + " tile";
    }
}
