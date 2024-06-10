package com.example.model;

import java.util.EnumMap;
import java.util.Map;

public class TileTerrain {

    private final Map<GridDirection, TerrainType> terrainMap;

    public TileTerrain(TileType type) {
        terrainMap = new EnumMap<>(GridDirection.class);
        TerrainType[] terrainTypes = type.getTerrain();
        GridDirection[] directions = GridDirection.values();
        for (int i = 0; i < directions.length; i++) {
            terrainMap.put(directions[i], terrainTypes[i]);
        }
    }

    public TerrainType getTerrain(GridDirection direction) {
        return terrainMap.get(direction);
    }

    public boolean hasConnection(GridDirection from, GridDirection to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Directions cannot be null");
        }
        // Получает типы террена из карты для заданных направлений и сравнивает их.
        TerrainType fromTerrain = terrainMap.get(from);
        TerrainType toTerrain = terrainMap.get(to);

        // Возвращает true, если типы террена одинаковы, что означает наличие соединения между направлениями.
        return fromTerrain == toTerrain;
    }

}
