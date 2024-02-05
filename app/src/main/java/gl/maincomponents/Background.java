package gl.maincomponents;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;

import m.mapmaker.Bricks;
import obj.entities.Enemy;
import roma.illusionofdugeon.Game;
import roma.illusionofdugeon.GameObject;
import roma.illusionofdugeon.GameScreen;

/**
 * Created by Роман on 30.05.2017.
 */


public class Background extends GameObject {

    private Bitmap bg, fullBg, mask;
    public static int course = 0;
    private static int forwardX;
    private static int forwardY;
    public Bricks[][]bricks;
    private RectF hero;
    private RectF boss;

    public Background(RectF hero, Bricks[][]bricks) {
        this.bricks = bricks;
        this.x = (int)bricks[0][0].getRect().left;
        this.y = (int)bricks[0][0].getRect().top;
        width = 720;
        height = 800;
        rect = new RectF(x, y, x+width, y+height);
        this.hero = hero;
        rect = new RectF(-100, -200, Game.sDisplayMetrics.widthPixels + 100, Game.sDisplayMetrics.heightPixels + 200);
    }

    public void update() {
        forwardX = Game.gameInterface.getmJoystick().forwardX;
        forwardY = Game.gameInterface.getmJoystick().forwardY;
        setCourse();
        if (forwardX != 0 || forwardY != 0) {
            Collision();
            for (int i = 0; i < Bricks.NUM_BRICKS_HEIGHT; i++) {
                for (int j = 0; j < Bricks.NUM_BRICKS_WIDTH; j++) {
                    if (bricks[i][j]!=null) {
                        bricks[i][j].getRect().left -= forwardX;
                        bricks[i][j].getRect().right -= forwardX;
                        bricks[i][j].getRect().top -= forwardY;
                        bricks[i][j].getRect().bottom -= forwardY;
                    }
                }
            }
            x -= forwardX;
            y -= forwardY;
        }
    }

    public void draw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);
        int numBrW = (int)(x / -Bricks.WIDTH);
        int numBrH = (int)(y / -Bricks.HEIGHT);
        for (int i = numBrH-2; i < numBrH+15; i++) {
            for (int j = numBrW-2; j < numBrW+25; j++) {
                if (i>-1&&i< Bricks.NUM_BRICKS_HEIGHT&&j>-1&&j< Bricks.NUM_BRICKS_WIDTH) {
                    if (bricks[i][j]!=null && bricks[i][j].image!=null) {
                        canvas.drawBitmap(bricks[i][j].image, bricks[i][j].getRect().left, bricks[i][j].getRect().top, null);
                    }
                }
            }
        }
    }

    private void Collision(){
        boolean upM = false;
        boolean downM = false;
        if (boss!=null) {
            if (boss.right > 0 || boss.left < GameScreen.WIDTH || boss.top < GameScreen.HEIGHT || boss.bottom < 0) {
                if (boss.contains(hero.left + forwardX, hero.bottom) || boss.contains(hero.left + forwardX, hero.top) || boss.contains(hero.left + forwardX, hero.centerY())) {
                    forwardX = (int) (boss.right - hero.left);
                }
                if (boss.contains(hero.right + forwardX, hero.top) || boss.contains(hero.right + forwardX, hero.bottom) || boss.contains(hero.right + forwardX, hero.centerY())) {
                    forwardX = (int) (boss.left - hero.right) - 1;
                }
                if (boss.contains(hero.centerX(), hero.top + forwardY) || boss.contains(hero.right, hero.top + forwardY) || boss.contains(hero.left, hero.top + forwardY)) {
                    forwardY = (int) (boss.bottom - hero.top);
                }
                if (boss.contains(hero.centerX(), hero.bottom + forwardY) || boss.contains(hero.right, hero.bottom + forwardY) || boss.contains(hero.left, hero.bottom + forwardY)) {
                    forwardY = (int) (boss.top - hero.bottom) - 1;
                }
            }
        }
        int numBrW = (int)(x - hero.left) / -Bricks.WIDTH;
        int numBrH = (int)(y - hero.top) / -Bricks.HEIGHT;
        for (int i = numBrH-2; i<numBrH + 5; i++){
            for (int j = numBrW-2; j<numBrW + 5; j++) {
                if (i >=0 && i < Bricks.NUM_BRICKS_HEIGHT && j>=0 && j < Bricks.NUM_BRICKS_WIDTH) {
                    if (bricks[i][j]!=null) {
                        if (bricks[i][j].getWall()) {
                            if (bricks[i][j].getRect().contains(hero.left + forwardX, hero.bottom) || bricks[i][j].getRect().contains(hero.left + forwardX, hero.top) || bricks[i][j].getRect().contains(hero.left + forwardX, hero.centerY())) {
                                forwardX = (int)(bricks[i][j].getRect().right - hero.left);
                            } else {
                                if (bricks[i][j].getRect().contains(hero.right + forwardX, hero.top) || bricks[i][j].getRect().contains(hero.right + forwardX, hero.bottom) || bricks[i][j].getRect().contains(hero.right + forwardX, hero.centerY())) {
                                    forwardX = (int) (bricks[i][j].getRect().left - hero.right) - 1;
                                }
                            }
                            if (bricks[i][j].getRect().contains(hero.centerX(), hero.top + forwardY) || bricks[i][j].getRect().contains(hero.right, hero.top + forwardY) || bricks[i][j].getRect().contains(hero.left, hero.top + forwardY)) {
                                forwardY = (int)(bricks[i][j].getRect().bottom - hero.top);
                            }else {
                                if (bricks[i][j].getRect().contains(hero.centerX(), hero.bottom + forwardY) || bricks[i][j].getRect().contains(hero.right, hero.bottom + forwardY) || bricks[i][j].getRect().contains(hero.left, hero.bottom + forwardY)) {
                                    forwardY = (int) (bricks[i][j].getRect().top - hero.bottom) - 1;
                                }
                            }
                        } else {
                            if (bricks[i][j].id == 85 && RectF.intersects(bricks[i][j].getRect(), hero)){
                                Game.gameInterface.setUpLevel(true);
                                upM = true;
                            } else {
                                if (!upM) {
                                    Game.gameInterface.setUpLevel(false);
                                }
                                if (bricks[i][j].id == 87 && RectF.intersects(bricks[i][j].getRect(), hero)) {
                                    Game.gameInterface.setDownLevel(true);
                                    downM = true;
                                } else {
                                    if (!downM) {
                                        Game.gameInterface.setDownLevel(false);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private static void setCourse(){
        int beforCourse = course;
        if (forwardX < 0 && Math.abs(forwardX)>=Math.abs(forwardY)) {
            course = 0;
        } else {
            if (forwardX > 0 && Math.abs(forwardX)>=Math.abs(forwardY)) {
                course = 1;
            } else {
                if (forwardY < 0 && Math.abs(forwardX)<=Math.abs(forwardY)) {
                    course = 2;
                } else {
                    if (forwardY > 0 && Math.abs(forwardX)<=Math.abs(forwardY)) {
                        course = 3;
                    } else {
                        if (beforCourse + 4 < 8)
                            course = beforCourse + 4;
                    }
                }
            }
        }
        if (Game.gameInterface.getHero().isBlock() && course + 8 < 16){
            Game.gameInterface.getHero().setSideShield(course + 8);
        } else {
            Game.gameInterface.getHero().setSideShield(course);
        }
        if (Game.gameInterface.getHero().isStartAttack() || (!Game.gameInterface.getHero().isStartAttack() && (System.nanoTime() - Game.gameInterface.getHero().getEndAttack())/1000000 < 320)) {
            if (course + 8 < 16) {
                course += 8;
            }
        } else {
            if (course > 7){
                course -= 8;
            }
        }
        if (beforCourse != course) {
            Game.gameInterface.getHero().setSide(course);
        }
    }
    public int getForwardX(){return forwardX;}
    public int getForwardY(){return forwardY;}
    public void setBoss(Enemy boss){this.boss = boss.getRect();}
}

