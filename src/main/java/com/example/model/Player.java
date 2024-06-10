
package com.example.model;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.paint.Color;

public class Player {
    private String name;
    private int score;
    private List<Meeple> meeples;
    private int availableMeeples;
    private Color color;

    public Player(String name, Color color) {
        this.name = name;
        this.score = 0;
        this.availableMeeples = 7;  // Примерное количество миплов
        this.color = color;
        this.meeples = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public void resetScore() {
        this.score = 0;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Color getColor() {
        return color;
    }

    public Color getLightColor() {
        return color.brighter();
    }

    public void addPoints(int points, TerrainType terrainType) {
        score += points;
        System.out.println(name + " получил " + points + " очков за " + terrainType);
    }

    public void addMeeple(Meeple meeple) {
        meeples.add(meeple);
    }

    public boolean hasFreeMeeples() {
        // Проверяем, есть ли у игрока свободные миплы
        return this.availableMeeples > 0;
    }

    public void removeMeeple(Meeple meeple) {
        meeples.remove(meeple);
    }

    @Override
    public String toString() {
        return "Player{name='" + name + "', score=" + score + ", meeples=" + meeples.size() + "}";
    }

    public boolean canPlaceMeeple(GridDirection direction, Tile tile) {
        // Предполагается, что игрок может размещать мипл, если у него есть свободные миплы
        return availableMeeples > 0;
    }

    public void placeMeeple() {
        if (availableMeeples > 0) {
            availableMeeples--;
        }
    }

    public void retrieveMeeple() {
        availableMeeples++;
    }
}
