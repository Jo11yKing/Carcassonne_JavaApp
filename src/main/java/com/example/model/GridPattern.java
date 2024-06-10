package com.example.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GridPattern {
    private boolean disbursed;
    protected boolean complete;
    private final Map<Player, Integer> involvedPlayers;
    private final List<Meeple> meepleList;
    protected final TerrainType patternType;
    protected int scoreMultiplier;
    protected List<GridSpot> containedSpots;

    protected GridPattern(TerrainType patternType, int scoreMultiplier) {
        this.patternType = patternType;
        this.scoreMultiplier = scoreMultiplier;
        containedSpots = new LinkedList<>();
        meepleList = new LinkedList<>();
        involvedPlayers = new HashMap<>();
    }

    public void disburse(boolean splitScore) {
        if (complete) {
            distributePatternScore(splitScore);
            meepleList.forEach(it -> it.getLocation().getTile().removeMeeple());
            involvedPlayers.clear();
        }
    }

    public void forceDisburse(boolean splitScore) {
        if (!complete) {
            distributePatternScore(splitScore);
        }
    }

    public List<Player> getDominantPlayers() {
        if (involvedPlayers.isEmpty()) {
            return Collections.emptyList();
        }
        int maximum = Collections.max(involvedPlayers.values());
        return involvedPlayers.keySet().stream().filter(player -> involvedPlayers.get(player) == maximum).toList();
    }

    public List<Meeple> getMeepleList() {
        return meepleList;
    }

    public int getPatternScore() {
        return containedSpots.size() * scoreMultiplier;
    }

    public TerrainType getType() {
        return patternType;
    }

    public int getScoreFor(Player player) {
        List<Player> dominantPlayers = getDominantPlayers();
        if (dominantPlayers.contains(player)) {
            return divideScore(getPatternScore(), dominantPlayers);
        }
        return 0;
    }

    public int getSize() {
        return containedSpots.size();
    }

    public boolean isComplete() {
        return complete;
    }

    public boolean isNotOccupied() {
        return involvedPlayers.isEmpty();
    }

    public boolean isOccupiedBy(Player player) {
        return involvedPlayers.containsKey(player);
    }

    public void removeOwnTags() {
        containedSpots.forEach(it -> it.removeTagsFrom(this));
    }

    public void removeTileTags() {
        containedSpots.forEach(GridSpot::removeTags);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("GridPattern[type: ");
        builder.append(patternType).append(", size: ").append(getSize()).append(", complete: ").append(complete);
        builder.append(", disbursed: ").append(disbursed).append(", meeples: ").append(meepleList).append(", on: ");
        builder.append(containedSpots.stream().map(it -> "(" + it.getX() + "|" + it.getY() + ")").toList());
        return builder.toString();
    }

    private void distributePatternScore(boolean splitScore) {
        if (!disbursed && !involvedPlayers.isEmpty()) {
            List<Player> dominantPlayers = getDominantPlayers();
            int stake = splitScore ? divideScore(getPatternScore(), dominantPlayers) : getPatternScore();
            for (Player player : dominantPlayers) {
                player.addPoints(stake, patternType);
            }
            disbursed = true;
        }
    }

    private void addMeepleFrom(GridSpot spot) {
        assert !disbursed;
        Meeple meeple = spot.getTile().getMeeple();
        if (!meepleList.contains(meeple) && isPartOfPattern(spot, meeple.getPosition())) {
            Player player = meeple.getOwner();
            involvedPlayers.put(player, involvedPlayers.getOrDefault(player, 0) + 1);
            meepleList.add(meeple);
        }
    }

    private int divideScore(int score, List<Player> dominantPlayers) {
        return (int) Math.ceil(score / (double) dominantPlayers.size());
    }

    private boolean isPartOfPattern(GridSpot spot, GridDirection position) {
        boolean onCorrectTerrain = spot.getTile().getTerrain(position) == patternType;
        boolean onPattern = spot.isIndirectlyTaggedBy(position, this) || patternType == TerrainType.MONASTERY;
        return onCorrectTerrain && onPattern;
    }

    protected void add(GridSpot spot) {
        containedSpots.add(spot);
        if (spot.getTile().hasMeeple()) {
            addMeepleFrom(spot);
        }
    }

    protected void checkArgs(GridSpot spot, GridDirection direction) {
        if (spot == null || direction == null) {
            throw new IllegalArgumentException("Arguments can't be null");
        }
    }
}
