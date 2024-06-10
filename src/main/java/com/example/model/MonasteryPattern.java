package com.example.model;

import java.util.List;

public class MonasteryPattern extends GridPattern {
    private boolean surrounded;

    public MonasteryPattern(GridSpot spot) {
        super(TerrainType.MONASTERY, 1);
        if (spot.getTile().getTerrain(GridDirection.CENTER) != TerrainType.MONASTERY) {
            throw new IllegalArgumentException("Can't create monastery pattern from non-monastery tile");
        }
        buildPattern(spot);
    }

    private void buildPattern(GridSpot monasterySpot) {
        List<GridSpot> neighbors = monasterySpot.getGrid().getNeighbors(monasterySpot, false, GridDirection.neighbors());
        add(monasterySpot);
        monasterySpot.setTag(GridDirection.CENTER, this);
        neighbors.forEach(this::add);
        if (neighbors.size() == GridDirection.neighbors().size()) {
            complete = true;
            checkSurrounding(neighbors);
        }
    }

    private void checkSurrounding(List<GridSpot> neighbors) {
        surrounded = neighbors.stream().allMatch(GridSpot::isOccupied);
    }

    @Override
    public int getPatternScore() {
        int baseScore = super.getPatternScore();
        return surrounded ? baseScore + 3 : baseScore;
    }
}
