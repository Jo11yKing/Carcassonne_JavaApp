package com.example.model;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Meeple extends Circle {
    private Player owner;
    private GridSpot location;
    private GridDirection position;

    public Meeple(Player owner) {
        super(10, Color.RED); 
        this.owner = owner;
        this.location = null;
        this.position = null;
    }

    public Player getOwner() {
        return owner;
    }

    public GridSpot getLocation() {
        return location;
    }

    public void setLocation(GridSpot location) {
        this.location = location;
    }

    public GridDirection getPosition() {
        return position;
    }

    public void setPosition(GridDirection position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "Meeple{owner=" + owner.getName() + ", location=(" + (location != null ? location.getX() + "," + location.getY() : "null") + "), position=" + position + "}";
    }
}
