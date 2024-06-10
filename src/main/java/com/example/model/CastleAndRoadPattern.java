package com.example.model;

import java.util.LinkedList;
import java.util.List;

public class CastleAndRoadPattern extends GridPattern {

    private final List<GridSpot> openEnds;

    public CastleAndRoadPattern(GridSpot startingSpot, GridDirection startingDirection, TerrainType terrainType) {
        super(terrainType, terrainType == TerrainType.CASTLE ? 2 : 1);
        openEnds = new LinkedList<>();
        checkArgs(startingSpot, startingDirection);
        buildPattern(startingSpot, startingDirection);
        complete = openEnds.isEmpty();
    }

    private void buildPattern(GridSpot spot, GridDirection direction) {
        if (spot == null || spot.isFree() || spot.isIndirectlyTagged(direction)) {
            return;
        }
        add(spot);
        openEnds.add(spot);
        spot.setTag(direction, this);

        for (GridDirection neighborDirection : GridDirection.directNeighbors()) {
            GridSpot neighbor = spot.getGrid().getNeighbor(spot, neighborDirection);
            if (neighbor != null && neighbor.getTile().getTerrain(neighborDirection.opposite()) == patternType) {
                buildPattern(neighbor, neighborDirection.opposite());
            }
        }
        openEnds.remove(spot);
    }
}
