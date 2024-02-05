package obj.entities;

import android.graphics.Canvas;
import android.graphics.RectF;

import gl.maincomponents.Background;
import roma.illusionofdugeon.Game;

/**
 * Created by Роман on 23.02.2017.
 */

public class Drifter extends Mob {

    private RectF hero;

    public Drifter(String name, Background bg, RectF hero, int numD) {
        super(name, bg);
        this.hero = hero;
        Game.gameInterface.getDialog().buildDialog(numD);
    }

    /**
     * Начинает диалог по касанию
     *
     * @param x
     * @param y
     */
    public void startDialog(float x, float y) {
        if (rect.contains(x, y) && rect.intersect(hero) && !dead) {
            attacked();
        }
    }

    /**
     * Начинает диалог по атаке
     */
    @Override
    public void attacked() {
        Game.gameInterface.setMainInterface(Game.gameInterface.getDialog());
        Game.gameInterface.getmJoystick().revival();
        Game.gameInterface.getButtons()[0].noPressing();
    }

    public void update() {
        super.update();
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
    }
}

