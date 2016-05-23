package com.base.game;

import java.util.ArrayList;

public class Physics {
    private static ArrayList<GameObject> go = new ArrayList<GameObject>();

    public static void update() {
        for (int i = 0; i < go.size(); i++)
            for (int j = i + 1; j < go.size(); j++) {
                // collision between rectangle & rectangle
                // see doc/w
                float _2x1 = go.get(i).getUpperLeft().getX() +
                        go.get(i).getLowerRight().getX();
                float _2y1 = go.get(i).getUpperLeft().getY() +
                        go.get(i).getLowerRight().getY();
                float _2x2 = go.get(j).getUpperLeft().getX() +
                        go.get(j).getLowerRight().getX();
                float _2y2 = go.get(j).getUpperLeft().getY() +
                        go.get(j).getLowerRight().getY();
                float w1 = go.get(i).getLowerRight().getX() -
                        go.get(i).getUpperLeft().getX();
                float h1 = go.get(i).getLowerRight().getY() -
                        go.get(i).getUpperLeft().getY();
                float w2 = go.get(j).getLowerRight().getX() -
                        go.get(i).getUpperLeft().getX();
                float h2 = go.get(j).getLowerRight().getY() -
                        go.get(i).getUpperLeft().getY();
                if (Math.abs(_2x1 - _2x2) <= (w1 + w2) && // collision horizonally
                        Math.abs(_2y1 - _2y2) <= (h1 + h2) ) { // collision vertically
                    go.get(i).collide(go.get(j));
                    go.get(j).collide(go.get(i));
                }
            }

    go.clear();
}

    public static void addObject(GameObject gameObject) {
        go.add(gameObject);
    }
}
