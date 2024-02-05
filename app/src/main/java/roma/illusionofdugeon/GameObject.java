package roma.illusionofdugeon;

/**
 * Created by Роман on 30.05.2017.
 */

import android.graphics.RectF;

public abstract class GameObject {
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected RectF rect;

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public RectF getRect() {
        return rect;
    }
}

