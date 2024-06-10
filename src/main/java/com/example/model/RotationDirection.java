package com.example.model;

public enum RotationDirection {
    LEFT(-1),
    RIGHT(1);

    int value;

    RotationDirection(int value) {
        this.value = value;
    }

    /**
     * Returns the numeric value of the rotation direction.
     * @return -1 or 1.
     */
    public int getValue() {
        return value;
    }
}
