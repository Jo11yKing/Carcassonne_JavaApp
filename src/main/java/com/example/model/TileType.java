package com.example.model;

import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

public enum TileType {
    Null(0, TerrainType.OTHER, TerrainType.OTHER, TerrainType.OTHER, TerrainType.OTHER, TerrainType.OTHER, TerrainType.OTHER, TerrainType.OTHER, TerrainType.OTHER, TerrainType.OTHER),
    CastleCenter(1, TerrainType.CASTLE, TerrainType.CASTLE, TerrainType.CASTLE, TerrainType.CASTLE, TerrainType.CASTLE, TerrainType.CASTLE, TerrainType.CASTLE, TerrainType.CASTLE, TerrainType.CASTLE),
    CastleCenterEntry(3, TerrainType.CASTLE, TerrainType.CASTLE, TerrainType.ROAD, TerrainType.CASTLE, TerrainType.CASTLE, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.CASTLE, TerrainType.CASTLE),
    CastleCenterSide(4, TerrainType.CASTLE, TerrainType.CASTLE, TerrainType.FIELDS, TerrainType.CASTLE, TerrainType.CASTLE, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.CASTLE, TerrainType.CASTLE),
    CastleEdge(5, TerrainType.CASTLE, TerrainType.CASTLE, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.CASTLE, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.FIELDS),
    CastleEdgeRoad(4, TerrainType.CASTLE, TerrainType.CASTLE, TerrainType.ROAD, TerrainType.ROAD, TerrainType.CASTLE, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.ROAD),
    CastleSides(3, TerrainType.CASTLE, TerrainType.FIELDS, TerrainType.CASTLE, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.FIELDS),
    CastleSidesEdge(2, TerrainType.CASTLE, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.CASTLE, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.OTHER, TerrainType.FIELDS),
    CastleTube(2, TerrainType.FIELDS, TerrainType.CASTLE, TerrainType.FIELDS, TerrainType.CASTLE, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.CASTLE),
    CastleWall(5, TerrainType.CASTLE, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.FIELDS),
    CastleWallCurveLeft(3, TerrainType.CASTLE, TerrainType.FIELDS, TerrainType.ROAD, TerrainType.ROAD, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.ROAD),
    CastleWallCurveRight(3, TerrainType.CASTLE, TerrainType.ROAD, TerrainType.ROAD, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.ROAD),
    CastleWallJunction(3, TerrainType.CASTLE, TerrainType.ROAD, TerrainType.ROAD, TerrainType.ROAD, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.OTHER),
    CastleWallRoad(4, TerrainType.CASTLE, TerrainType.ROAD, TerrainType.FIELDS, TerrainType.ROAD, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.ROAD),
    Monastery(4, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.MONASTERY),
    MonasteryRoad(2, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.ROAD, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.MONASTERY),
    Road(7, TerrainType.ROAD, TerrainType.FIELDS, TerrainType.ROAD, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.ROAD),
    RoadCurve(8, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.ROAD, TerrainType.ROAD, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.ROAD),
    RoadJunctionLarge(1, TerrainType.ROAD, TerrainType.ROAD, TerrainType.ROAD, TerrainType.ROAD, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.OTHER),
    RoadJunctionSmall(3, TerrainType.FIELDS, TerrainType.ROAD, TerrainType.ROAD, TerrainType.ROAD, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.OTHER),
    RoadCrossLarge(0, TerrainType.ROAD, TerrainType.ROAD, TerrainType.ROAD, TerrainType.ROAD, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.ROAD),
    RoadCrossSmall(2, TerrainType.FIELDS, TerrainType.ROAD, TerrainType.ROAD, TerrainType.ROAD, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.ROAD),
    CastleTubeEntries(1, TerrainType.ROAD, TerrainType.CASTLE, TerrainType.ROAD, TerrainType.CASTLE, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.CASTLE),
    CastleTubeEntry(1, TerrainType.FIELDS, TerrainType.CASTLE, TerrainType.ROAD, TerrainType.CASTLE, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.CASTLE),
    MonasteryCastle(1, TerrainType.CASTLE, TerrainType.CASTLE, TerrainType.CASTLE, TerrainType.CASTLE, TerrainType.CASTLE, TerrainType.CASTLE, TerrainType.CASTLE, TerrainType.CASTLE, TerrainType.MONASTERY),
    MonasteryJunction(1, TerrainType.ROAD, TerrainType.ROAD, TerrainType.ROAD, TerrainType.ROAD, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.MONASTERY),
    CastleSidesRoad(2, TerrainType.CASTLE, TerrainType.ROAD, TerrainType.CASTLE, TerrainType.ROAD, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.ROAD),
    CastleSidesEdgeRoad(1, TerrainType.CASTLE, TerrainType.ROAD, TerrainType.ROAD, TerrainType.CASTLE, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.OTHER, TerrainType.ROAD),
    CastleSidesQuad(1, TerrainType.CASTLE, TerrainType.CASTLE, TerrainType.CASTLE, TerrainType.CASTLE, TerrainType.OTHER, TerrainType.OTHER, TerrainType.OTHER, TerrainType.OTHER, TerrainType.FIELDS),
    RoadEnd(1, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.ROAD, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.OTHER),
    CastleCenterSides(1, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.CASTLE, TerrainType.FIELDS, TerrainType.OTHER, TerrainType.OTHER, TerrainType.OTHER, TerrainType.OTHER, TerrainType.CASTLE),
    CastleMini(1, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.ROAD, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.CASTLE),
    CastleWallEntryLeft(1, TerrainType.CASTLE, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.ROAD, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.ROAD),
    CastleWallEntryRight(1, TerrainType.CASTLE, TerrainType.ROAD, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.ROAD),
    CastleWallEntry(2, TerrainType.CASTLE, TerrainType.FIELDS, TerrainType.ROAD, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.FIELDS, TerrainType.ROAD);

    private final TerrainType[] terrain;
    private final int amount;

    TileType(int amount, TerrainType... terrain) {
        this.amount = amount;
        this.terrain = terrain;
    }

    public int getAmount() {
        return amount;
    }

    public TerrainType[] getTerrain() {
        return terrain;
    }

    public TerrainType getTerrain(GridDirection direction) {
        return terrain[direction.ordinal()];
    }

    public String readableRepresentation() {
        return toString().replaceAll("([^_])([A-Z])", "$1 $2").toLowerCase(Locale.UK);
    }

    public static List<TileType> validTiles() {
        return Stream.of(values()).filter(it -> it != Null).toList();
    }

    public static List<TileType> enabledTiles() {
        return Stream.of(values()).filter(it -> it != Null && it.getAmount() > 0).toList();
    }
}