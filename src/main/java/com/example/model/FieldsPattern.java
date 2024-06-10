package com.example.model;

import java.util.LinkedList;
import java.util.List;

public class FieldsPattern extends GridPattern {
    private static final int POINTS_PER_CASTLE = 3;
    private final List<CastleAndRoadPattern> adjacentCastles;
    private final Grid grid;

    public FieldsPattern(GridSpot startingSpot, GridDirection startingDirection) {
        super(TerrainType.FIELDS, POINTS_PER_CASTLE);
        checkArgs(startingSpot, startingDirection);
        grid = startingSpot.getGrid();
        adjacentCastles = new LinkedList<>();
        startingSpot.setTag(startingDirection, this);
        add(startingSpot);
        buildPattern(startingSpot, startingDirection);
        adjacentCastles.forEach(CastleAndRoadPattern::removeOwnTags);
    }

    @Override
    public int getPatternScore() {
        return adjacentCastles.size() * scoreMultiplier;
    }

    private void addIfNotCastle(List<GridDirection> results, Tile tile, GridDirection next) {
        if (tile.getTerrain(next) != TerrainType.CASTLE) {
            results.add(next);
        }
    }

    private void buildPattern(GridSpot spot, GridDirection startingPoint) {
        List<GridDirection> fieldPositions = getFieldPositions(spot.getTile(), startingPoint);
        for (GridDirection position : fieldPositions) {
            countAdjacentCastles(spot, position);
            spot.setTag(position, this);
        }
        fieldPositions.forEach(it -> checkNeighbors(spot, it));
    }

    private void checkNeighbors(GridSpot spot, GridDirection position) {
        for (GridDirection connectionDirection : getFieldConnections(position, spot.getTile())) {
            GridSpot neighbor = grid.getNeighbor(spot, connectionDirection);
            GridDirection oppositeDirection = getFieldOpposite(position, connectionDirection);
            if (neighbor != null && !neighbor.isIndirectlyTagged(oppositeDirection)) {
                neighbor.setTag(oppositeDirection, this);
                add(neighbor);
                buildPattern(neighbor, oppositeDirection);
            }
        }
    }

    private void countAdjacentCastles(GridSpot spot, GridDirection position) {
        for (GridDirection neighbor : getAdjacentPositions(position)) {
            if (spot.getTile().getTerrain(neighbor) == TerrainType.CASTLE && isUntagged(spot, neighbor)) {
                CastleAndRoadPattern castle = new CastleAndRoadPattern(spot, neighbor, TerrainType.CASTLE);
                if (castle.isComplete()) {
                    adjacentCastles.add(castle);
                } else {
                    castle.removeOwnTags();
                }
            }
        }
    }

    private List<GridDirection> getAdjacentPositions(GridDirection position) {
        List<GridDirection> neighbors = new LinkedList<>();
        if (position.isSmallerOrEquals(GridDirection.WEST)) {
            neighbors.add(GridDirection.CENTER);
        }
        if (position.isSmallerOrEquals(GridDirection.NORTH_WEST)) {
            neighbors.add(position.nextDirectionTo(RotationDirection.LEFT));
            neighbors.add(position.nextDirectionTo(RotationDirection.RIGHT));
        } else {
            neighbors.addAll(GridDirection.directNeighbors());
        }
        return neighbors;
    }

    private List<GridDirection> getFieldConnections(GridDirection position, Tile tile) {
        List<GridDirection> results = new LinkedList<>();
        if (tile.getTerrain(position) == TerrainType.FIELDS) {
            if (position.isSmallerOrEquals(GridDirection.WEST)) {
                results.add(position);
            } else if (position.isSmallerOrEquals(GridDirection.NORTH_WEST)) {
                addIfNotCastle(results, tile, position.nextDirectionTo(RotationDirection.LEFT));
                addIfNotCastle(results, tile, position.nextDirectionTo(RotationDirection.RIGHT));
            }
        }
        return results;
    }

    private GridDirection getFieldOpposite(GridDirection position, GridDirection neighborDirection) {
        if (position.isSmallerOrEquals(GridDirection.WEST)) {
            return position.opposite();
        }
        if (position.isSmallerOrEquals(GridDirection.NORTH_WEST)) {
            if (neighborDirection.isLeftOf(position)) {
                return position.opposite().nextDirectionTo(RotationDirection.LEFT).nextDirectionTo(RotationDirection.LEFT);
            }
            return position.opposite().nextDirectionTo(RotationDirection.RIGHT).nextDirectionTo(RotationDirection.RIGHT);
        }
        return position;
    }

    private List<GridDirection> getFieldPositions(Tile tile, GridDirection startingPoint) {
        List<GridDirection> fieldPositions = new LinkedList<>();
        for (GridDirection position : GridDirection.values()) {
            if (tile.hasConnection(startingPoint, position)) {
                fieldPositions.add(position);
            }
        }
        return fieldPositions;
    }

    private boolean isUntagged(GridSpot spot, GridDirection position) {
        boolean tagged = false;
        for (CastleAndRoadPattern castle : adjacentCastles) {
            tagged |= spot.isIndirectlyTaggedBy(position, castle);
        }
        return !tagged;
    }
}

