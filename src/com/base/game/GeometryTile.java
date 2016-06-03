package com.base.game;

import com.base.engine.Image;

/**
 * Created by Chen Yufei on 16-6-2.
 * Map tile that represents for a type of geometry block.
 */
public class GeometryTile extends MapTile {
    // accessibility enum
    static final int ACCESSIBLE = 0;
    static final int INACCESSIBLE = 1;
    static final int DANGEROUS = 2;

    /**
     * get the accessibility of this type of geometry block.
     * @return accessibility enum
     */
    public int getAccessibility() {
        return accessibility;
    }

    int accessibility;

    /**
     * get the image of this type of geometry block.
     * @return
     */
    public Image getImage() {
        return image;
    }

    Image image;

    public GeometryTile(String tag, int colorCode, String imagePath, int accessibility) {
        super.tag = tag;
        super.colorCode = colorCode;
        this.image = new Image(imagePath);
        this.accessibility = accessibility;
    }
}
