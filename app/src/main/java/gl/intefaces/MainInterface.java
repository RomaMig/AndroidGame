package gl.intefaces;

import android.graphics.Canvas;

import h.basicfunctions.ButtonsManager;
import h.basicfunctions.Points;

/**
 * Created by Роман on 30.05.2017.
 */

public abstract class MainInterface {

    public int forwardX;
    public int forwardY;
    protected ButtonsManager[]mButtons;

    public abstract void update();
    public abstract void draw(Canvas canvas);
    public abstract void actionToFalse(int id);
    public abstract void checkGestures(Points[]fingers);
    public ButtonsManager[] getButtons() {
        return mButtons;
    }
}

