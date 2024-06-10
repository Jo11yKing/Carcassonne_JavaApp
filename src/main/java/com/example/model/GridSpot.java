package com.example.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GridSpot {
    private final Grid grid;
    private final Map<GridDirection, Set<GridPattern>> tagMap;
    private Tile tile;
    private final int x;
    private final int y;

    public GridSpot(Grid grid, int x, int y) {
        this.grid = grid;
        this.x = x;
        this.y = y;
        tagMap = new HashMap<>();
        for (GridDirection direction : GridDirection.values()) {
            tagMap.put(direction, new HashSet<>());
        }
    }

    public Collection<GridPattern> createPatternList() {
        if (isFree()) {
            throw new IllegalStateException("GridSpot is free, cannot create patterns");
        }
        List<GridPattern> results = new LinkedList<>();
        for (GridDirection direction : GridDirection.tilePositions()) {
            TerrainType terrain = tile.getTerrain(direction);
            if ((terrain == TerrainType.CASTLE || terrain == TerrainType.ROAD) && !isIndirectlyTagged(direction)) {
                results.add(new CastleAndRoadPattern(this, direction, terrain));
            }
        }
        for (GridDirection direction : GridDirection.values()) {
            TerrainType terrain = tile.getTerrain(direction);
            if (terrain == TerrainType.FIELDS && !isIndirectlyTagged(direction)) {
                results.add(new FieldsPattern(this, direction));
            }
        }
        addPatternIfMonastery(this, results);
        grid.getNeighbors(this, false, GridDirection.neighbors()).forEach(it -> addPatternIfMonastery(it, results));
        return results;
    }

    public void forcePlacement(Tile tile) {
        this.tile = tile;
        tile.setPosition(this);
    }

    public Tile getTile() {
        return tile;
    }

    public Grid getGrid() {
        return grid;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Boolean isIndirectlyTagged(GridDirection tilePosition) {
        for (GridDirection otherPosition : GridDirection.values()) {
            if (isTagged(otherPosition) && tile.hasConnection(tilePosition, otherPosition)) {
                return true;
            }
        }
        return false;
    }

    public Boolean isIndirectlyTaggedBy(GridDirection tilePosition, GridPattern tagger) {
        for (GridDirection otherPosition : GridDirection.values()) {
            if (tile.hasConnection(tilePosition, otherPosition) && tagMap.get(otherPosition).contains(tagger)) {
                return true;
            }
        }
        return false;
    }

    public boolean isFree() {
        return tile == null;
    }

    public boolean isOccupied() {
        return tile != null;
    }

    public boolean isPlaceable(Tile tile, boolean allowEnclaves) {
        if (isOccupied()) {
            return false;
        }
        int neighborCount = 0;
        for (GridDirection direction : GridDirection.directNeighbors()) {
            GridSpot neighbor = grid.getNeighbor(this, direction);
            if (neighbor == null) {
                if (!allowEnclaves && grid.isClosingFreeSpotsOff(this, direction)) {
                    return false;
                }
            } else {
                neighborCount++;
                if (!tile.canConnectTo(direction, neighbor.getTile())) {
                    return false;
                }
            }
        }
        return neighborCount > 0;
    }

    public void removeTags() {
        tagMap.values().forEach(Set::clear);
    }

    public void removeTagsFrom(GridPattern pattern) {
        tagMap.values().forEach(it -> it.remove(pattern));
    }

    public boolean place(Tile tile, boolean allowEnclaves) {
        if (isPlaceable(tile, allowEnclaves)) {
            tile.setPosition(this);
            this.tile = tile;
            return true;
        }
        return false;
    }

    public void removeTile() {
        if (tile != null) {
            tile.setPosition(null);
            tile = null;
        }
    }

    public void setTag(GridDirection direction, GridPattern tagger) {
        tagMap.get(direction).add(tagger);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[on: (" + x + "|" + y + "), Occupied:" + isOccupied() + "]";
    }

    private void addPatternIfMonastery(GridSpot spot, List<GridPattern> patternList) {
        if (spot.getTile().getTerrain(GridDirection.CENTER) == TerrainType.MONASTERY && !spot.isIndirectlyTagged(GridDirection.CENTER)) {
            patternList.add(new MonasteryPattern(spot));
        }
    }

    private Boolean isTagged(GridDirection direction) {
        return !tagMap.get(direction).isEmpty();
    }
}
