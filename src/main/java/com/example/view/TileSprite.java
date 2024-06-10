package com.example.view;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class TileSprite {
    public static Shape getRoadSprite() {
        Rectangle road = new Rectangle(70, 10);
        road.setFill(Color.GRAY);
        return road;
    }

    public static Shape getCitySprite() {
        Rectangle city = new Rectangle(70, 70);
        city.setFill(Color.DARKRED);
        return city;
    }

    public static Shape getFieldSprite() {
        Polygon field = new Polygon();
        field.getPoints().addAll(0.0, 0.0, 70.0, 35.0, 0.0, 70.0);
        field.setFill(Color.GREEN);
        return field;
    }

    public static Shape getMonasterySprite() {
        Circle monastery = new Circle(35);
        monastery.setFill(Color.SANDYBROWN);
        return monastery;
    }
}


