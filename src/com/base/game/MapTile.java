package com.base.game;

/**
 * Created by draplater on 16-6-2.
 * Representing for a tile in map.
 * Geometry block or GameObject can placed in this tile.
 */
public abstract class MapTile {
    int colorCode;
    String tag;

    /**
     * Get color code with which we represent the tile in .png file.
     * @return color code in ARGB.
     */
    public int getColorCode() {
        return colorCode;
    }

    /**
     * get the tag of tile.
     * @return tag string
     */
    public String getTag() {
        return tag;
    }
}