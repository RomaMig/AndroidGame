package obj.entities;

/**
 * Created by Роман on 30.05.2017.
 */


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;

import java.util.HashMap;
import java.util.Map;

import roma.illusionofdugeon.GameObject;
import h.basicfunctions.Animation;

public abstract class Entity extends GameObject {

    protected String name;
    protected Map<Integer, Bitmap[]> actionImages = new HashMap<Integer, Bitmap[]>();
    protected Bitmap [][]images;
    protected Animation animation;

    public int getLiveLine() {
        return liveLine;
    }

    public int speed;
    protected int liveLine;
    protected RectF radAttack;

    public abstract void update();
    public abstract void draw(Canvas canvas);
    public String getName(){
        return name;
    }

    public RectF getRadAttack() {
        return radAttack;
    }
}

