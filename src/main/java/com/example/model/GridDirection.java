package com.example.model;

import java.util.List;
import java.util.Locale;

public enum GridDirection {
    NORTH,
    EAST,
    SOUTH,
    WEST,
    NORTH_EAST,
    SOUTH_EAST,
    SOUTH_WEST,
    NORTH_WEST,
    CENTER;

    public int getX() {
        if (this == NORTH_EAST || this == EAST || this == SOUTH_EAST) {
            return 1;
        }
        if (this == NORTH_WEST || this == WEST || this == SOUTH_WEST) {
            return -1;
        }
        return 0;
    }

    public int getY() {
        if (this == SOUTH_WEST || this == SOUTH || this == SOUTH_EAST) {
            return 1;
        }
        if (this == NORTH_WEST || this == NORTH || this == NORTH_EAST) {
            return -1;
        }
        return 0;
    }

    public boolean isLeftOf(GridDirection other) {
        return nextDirectionTo(RotationDirection.RIGHT) == other;
    }

    public boolean isRightOf(GridDirection other) {
        return nextDirectionTo(RotationDirection.LEFT) == other;
    }

    public boolean isSmallerOrEquals(GridDirection other) {
        return ordinal() <= other.ordinal();
    }

    public GridDirection nextDirectionTo(RotationDirection side) {
        if (this == CENTER) {
            return this;
        }
        GridDirection[] cycle = {NORTH, NORTH_EAST, EAST, SOUTH_EAST, SOUTH, SOUTH_WEST, WEST, NORTH_WEST};
        int position = -2;
        for (int i = 0; i < cycle.length; i++) {
            if (cycle[i] == this) {
                position = i;
            }
        }
        return cycle[(cycle.length + position + side.getValue()) % cycle.length];
    }

    public GridDirection opposite() {
        if (ordinal() <= 3) {
            return values()[smallOpposite(ordinal())];
        }
        if (ordinal() <= 7) {
            return values()[bigOpposite(ordinal())];
        }
        return CENTER;
    }

    public String toReadableString() {
        return toString().toLowerCase(Locale.UK).replace('_', ' ');
    }

    private int bigOpposite(int ordinal) {
        return 4 + smallOpposite(ordinal - 4);
    }

    private int smallOpposite(int ordinal) {
        return (ordinal + 2) % 4;
    }

    public static List<GridDirection> directNeighbors() {
        return List.of(NORTH, EAST, SOUTH, WEST);
    }

    public static List<GridDirection> indirectNeighbors() {
        return List.of(NORTH_EAST, SOUTH_EAST, SOUTH_WEST, NORTH_WEST);
    }

    public static List<GridDirection> neighbors() {
        return List.of(NORTH, EAST, SOUTH, WEST, NORTH_EAST, SOUTH_EAST, SOUTH_WEST, NORTH_WEST);
    }

    public static List<GridDirection> tilePositions() {
        return List.of(NORTH, EAST, SOUTH, WEST, CENTER);
    }

    public static List<GridDirection> byRow() {
        return List.of(NORTH_WEST, NORTH, NORTH_EAST, WEST, CENTER, EAST, SOUTH_WEST, SOUTH, SOUTH_EAST);
    }

    public static GridDirection[][] values2D() {
        return new GridDirection[][] {{NORTH_WEST, WEST, SOUTH_WEST}, {NORTH, CENTER, SOUTH}, {NORTH_EAST, EAST, SOUTH_EAST}};
    }
}

