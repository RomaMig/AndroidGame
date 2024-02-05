package obj.entities;

import android.graphics.RectF;

import gl.maincomponents.Background;
import h.basicfunctions.Sounds;
import m.mapmaker.Bricks;
import roma.illusionofdugeon.Game;

/**
 * Created by Роман on 17.02.2017.
 */

public class Boss extends Enemy {

    public static final String[] bosses = {"HAMSTER", "OGR", "OGR"};
    private boolean soundWin = false;

    public Boss(String name, Background bg, RectF hero, int level) {
        super(name, bg, hero, level);
    }

    /**
     * Определяет место респавна
     */
    @Override
    protected void setLocation() {
        for (int i = 0; i < Bricks.NUM_BRICKS_HEIGHT; i++) {
            for (int j = 0; j < Bricks.NUM_BRICKS_WIDTH; j++) {
                if (bricks[i][j].id == 6) {
                    x = (int) bricks[i][j].getRect().left + 3;
                    y = (int) bricks[i][j].getRect().top + 3;
                    n = x - (int) bricks[0][0].getRect().left;
                    m = y - (int) bricks[0][0].getRect().top;
                    break;
                }
            }
        }
    }

    @Override
    public void update() {
        super.update();
        if (dead && Game.effectsSound && !soundWin) {
            soundWin = true;
            Sounds.soundPool.play(Sounds.sounds[31], 0.5f, 0.5f, 0, 0, 1);
        }
    }
}
