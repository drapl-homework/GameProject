package com.base.game;

/**
 * Created by draplater on 16-6-2.
 * Map tile that represents for a type of GameObject.
 */
public class GameObjectTile extends MapTile {

    private Class<?> targetClass;

    public GameObjectTile(String tag, int colorCode, String className) throws ClassNotFoundException {
        super.tag = tag;
        super.colorCode = colorCode;
        targetClass = Class.forName(className);
    }

    /**
     * Class object of the GameObject.
     * @return
     */
    public Class<?> getTargetClass() {
        return targetClass;
    }

}