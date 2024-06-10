package com.example.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Grid {
    private static final TileType FOUNDATION_TYPE = TileType.CastleWallRoad;
    private final int width;
    private final int height;
    private final GridSpot[][] spots;
    private GridSpot foundation;
    private final boolean allowEnclaves;

    public Grid(int width, int height, boolean allowEnclaves) {
        this.width = width;
        this.height = height;
        this.allowEnclaves = allowEnclaves;
        spots = new GridSpot[width][height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                spots[x][y] = new GridSpot(this, x, y);
            }
        }
        placeFoundation(FOUNDATION_TYPE);
    }

    public Tile getTile(int x, int y) {
        if (isOnGrid(x, y)) {
            return spots[x][y].getTile();
        }
        return null;
    }
    

    public List<GridPattern> getAllPatterns() {
        List<GridPattern> patterns = new LinkedList<>();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (spots[x][y].isOccupied()) {
                    patterns.addAll(spots[x][y].createPatternList());
                }
            }
        }
        patterns.forEach(GridPattern::removeTileTags);
        return patterns;
    }

    public GridSpot getFoundation() {
        return foundation;
    }

    public int getHeight() {
        return height;
    }

    public Collection<GridPattern> getLocalPatterns(GridSpot spot) {
        Collection<GridPattern> gridPatterns = new ArrayList<>();
        if (spot.isOccupied()) {
            gridPatterns.addAll(spot.createPatternList());
        }
        for (GridSpot neighbor : getNeighbors(spot, false, GridDirection.directNeighbors())) {
            gridPatterns.addAll(neighbor.createPatternList());
        }
        gridPatterns.forEach(GridPattern::removeTileTags);
        return gridPatterns;
    }

    public Collection<GridPattern> getModifiedPatterns(GridSpot spot) {
        checkParameters(spot);
        if (spot.isFree()) {
            throw new IllegalArgumentException("Can't check for patterns on a free grid space");
        }
        Collection<GridPattern> modifiedPatterns = spot.createPatternList();
        modifiedPatterns.forEach(GridPattern::removeTileTags);
        return modifiedPatterns;
    }

    public GridSpot getNeighbor(GridSpot spot, GridDirection direction) {
        List<GridSpot> neighbors = getNeighbors(spot, false, direction);
        if (neighbors.isEmpty()) {
            return null;
        }
        return neighbors.get(0);
    }

    public List<GridSpot> getNeighbors(GridSpot spot, boolean allowEmptySpots, List<GridDirection> directions) {
        checkParameters(spot);
        ArrayList<GridSpot> neighbors = new ArrayList<>();
        for (GridDirection direction : directions) {
            int newX = direction.getX() + spot.getX();
            int newY = direction.getY() + spot.getY();
            if (isOnGrid(newX, newY) && (allowEmptySpots || spots[newX][newY].isOccupied())) {
                neighbors.add(spots[newX][newY]);
            }
        }
        return neighbors;
    }

    public boolean isClosingFreeSpotsOff(GridSpot spot, GridDirection direction) {
        boolean[][] visitedPositions = new boolean[width][height];
        visitedPositions[spot.getX()][spot.getY()] = true;
        return !findBoundary(spot, direction, visitedPositions);
    }

    public boolean isAllowingEnclaves() {
        return allowEnclaves;
    }

    public boolean isFull() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (spots[x][y].isFree()) {
                    return false;
                }
            }
        }
        return true;
    }

    public GridSpot getSpot(int x, int y) {
        checkParameters(x, y);
        return spots[x][y];
    }

    public boolean place(int x, int y, Tile tile) {
        checkParameters(x, y);
        checkParameters(tile);
        return spots[x][y].place(tile, allowEnclaves);
    }

    private void checkParameters(GridSpot spot) {
        if (spot == null) {
            throw new IllegalArgumentException("Spot can't be null!");
        }
        if (!spots[spot.getX()][spot.getY()].equals(spot)) {
            throw new IllegalArgumentException("Spot is not on the grid!");
        }
    }

    private void checkParameters(int x, int y) {
        if (!isOnGrid(x, y)) {
            throw new IllegalArgumentException("tile coordinates are out of grid: x=" + x + " & y=" + y);
        }
    }

    private void checkParameters(Tile tile) {
        if (tile == null) {
            throw new IllegalArgumentException("Tile can't be null.");
        }
        if (tile.getType() == TileType.Null) {
            throw new IllegalArgumentException("Tile from type TileType.Null can't be placed.");
        }
    }

    private boolean findBoundary(GridSpot spot, GridDirection direction, boolean[][] visitedPositions) {
        int newX = direction.getX() + spot.getX();
        int newY = direction.getY() + spot.getY();
        if (!isOnGrid(newX, newY)) {
            return true;
        }
        if (spots[newX][newY].isFree() && !visitedPositions[newX][newY]) {
            visitedPositions[newX][newY] = true;
            for (GridDirection newDirection : GridDirection.directNeighbors()) {
                if (findBoundary(spots[newX][newY], newDirection, visitedPositions)) {
                    return true;
                }
            }
        }
        return false;
    }

    private List<GridSpot> getNeighbors(GridSpot spot, boolean allowEmptySpots, GridDirection direction) {
        return getNeighbors(spot, allowEmptySpots, List.of(direction));
    }

    public boolean isOnGrid(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    private void placeFoundation(TileType tileType) {
        int centerX = (width - 1) / 2;
        int centerY = (height - 1) / 2;
        foundation = spots[centerX][centerY];
        foundation.forcePlacement(new Tile(tileType));
    }
}



