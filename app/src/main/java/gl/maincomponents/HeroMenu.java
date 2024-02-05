package gl.maincomponents;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import gl.intefaces.MainInterface;
import h.basicfunctions.ButtonsManager;
import h.basicfunctions.Points;
import roma.illusionofdugeon.Game;
import roma.illusionofdugeon.GameScreen;
import roma.illusionofdugeon.GlobalVariables;

/**
 * Created by Роман on 08.02.2017.
 */

public class HeroMenu extends MainInterface {

    private Hero hero;
    private int x, y, experience, level;
    private Paint text = new Paint();
    private Bitmap menu;
    public static Hero.Perks[] perks = new Hero.Perks[4];
    public static boolean using;

    private int heartPerk = 0;
    private int damagePerk = 1;
    private int blockPerk = 2;

    public HeroMenu(Hero hero) {
        this.hero = hero;
        menu = GlobalVariables.images.get(1);
        x = 0;
        y = 0;
        text.setColor(Color.WHITE);
        text.setTextSize(50);
        text.setSubpixelText(true);
        perks[0] = new Hero.Perks(new ButtonsManager(GlobalVariables.images.get(3), (int) (Game.sDisplayMetrics.widthPixels / 16), (int) (Game.sDisplayMetrics.heightPixels / 3.2)) {
            @Override
            public void onClickListener() {
                if (perks[0].skillPoints > 0 && perks[0].getPoints() < perks[0].getRoof()) {
                    perks[0].setPoints(perks[0].getPoints() + 1);
                    perks[0].skillPoints--;
                }
            }
        }, 6);
        perks[1] = new Hero.Perks(new ButtonsManager(GlobalVariables.images.get(4), (int) (Game.sDisplayMetrics.widthPixels / 16), (int) (Game.sDisplayMetrics.heightPixels / 2.18)) {
            @Override
            public void onClickListener() {
                if (perks[1].skillPoints > 0 && perks[1].getPoints() < perks[1].getRoof()) {
                    perks[1].setPoints(perks[1].getPoints() + 1);
                    perks[1].skillPoints--;
                }
            }
        }, 25);
        perks[2] = new Hero.Perks(new ButtonsManager(GlobalVariables.images.get(2), (int) (Game.sDisplayMetrics.widthPixels / 16), (int) (Game.sDisplayMetrics.heightPixels / 1.65)) {
            @Override
            public void onClickListener() {
                if (perks[2].skillPoints > 0 && perks[2].getPoints() < perks[2].getRoof()) {
                    perks[2].setPoints(perks[2].getPoints() + 1);
                    perks[2].skillPoints--;
                }
            }
        }, 4);
        perks[3] = new Hero.Perks(new ButtonsManager(GlobalVariables.images.get(12), (int) (Game.sDisplayMetrics.widthPixels / 16), (int) (Game.sDisplayMetrics.heightPixels / 1.33)) {
            @Override
            public void onClickListener() {
                if (perks[3].skillPoints > 0 && perks[3].getPoints() < perks[3].getRoof()) {
                    perks[3].setPoints(perks[3].getPoints() + 1);
                    perks[3].skillPoints--;
                }
            }
        }, 6);
    }

    @Override
    public void actionToFalse(int id) {
        if (Game.gameInterface.getButtons()[2].noPressing(id)) {
        } else {
            for (int i = 0; i < perks.length; i++) {
                perks[i].buttonPerk.noPressing(id);
            }
        }
    }

    @Override
    public void checkGestures(Points[] fingers) {
        for (Points point : fingers) {
            if (point != null) {
                if (Game.gameInterface.getButtons()[2].Pressing(point.ID, point.Before.x, point.Before.y)) {
                } else {
                    for (int j = 0; j < perks.length; j++) {
                        perks[j].buttonPerk.Pressing(point.ID, point.Before.x,
                                point.Before.y);
                    }
                }
            }
        }
    }

    public void update() {
        Game.gameInterface.getButtons()[2].update();
        experience = hero.getExperience();
        level = hero.getLevel();
        for (int i = 0; i < perks.length; i++) {
            perks[i].update();
            Game.gameInterface.resume();
        }
    }

    public void draw(Canvas canvas) {
        canvas.scale(GameScreen.scaleX, GameScreen.scaleY);
        canvas.drawBitmap(menu, x, y, null);
        canvas.drawText(experience + " exp\n" + level + " level", 100 * GameScreen.RATIO_WIDTHS, 100 * GameScreen.RATIO_HEIGHTS, text);
        for (int i = 0; i < perks.length; i++) {
            perks[i].draw(canvas);
        }
        canvas.scale(1/GameScreen.scaleX, 1/GameScreen.scaleY);
        Game.gameInterface.getButtons()[2].buttonDraw(canvas);
    }

    public Hero.Perks[] getPerks() {
        return perks;
    }
}
