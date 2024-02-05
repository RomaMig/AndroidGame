package obj.entities;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.RectF;

import gl.maincomponents.Background;
import h.basicfunctions.Sounds;
import roma.illusionofdugeon.Game;
import roma.illusionofdugeon.GameScreen;

/**
 * Created by Роман on 30.05.2017.
 */

public class Enemy extends Mob {

    public boolean detected;
    private RectF hero;
    public FieldOfView view;
    private boolean attack;
    private double qx;
    private double qy;
    private double c;
    private long timeAttack = 0;
    private int timePause;
    protected int walk;
    protected int run;
    private Enemy.Enemies mob;
    private int sound;

    public Enemy(String name, Background bg, RectF hero, int level) {
        super(name, bg, Enemies.valueOf(name), level);
        mob = Enemies.valueOf(name);
        sound = mob.getSoundHit();
        this.hero = hero;
        view = new FieldOfView();
        view.setRadius((int)(mob.getRadiusAttack() * Game.RATIO));
        timePause = mob.getTimeAttack();
        walk = mob.getWalk();
        run = mob.getRun();
    }

    private int beforCourse = -1;

    public void update() {
        if (!Game.gameInterface.getmBackground().getRect().contains(rect)) {
            offset();
            offsetRect();
        } else {
            view.search();
            if (!detected) {
                super.update();
                detected = attacked;
            } else {
                offset();
                offsetRect();
                animation.update();
                if (!dead) {
                    beforCourse = course;
                    if (RectF.intersects(radAttack, hero)) {
                        if (course < 5) {
                            course += 5;
                            animation.setFrames(actionImages.get(course));
                        }
                        if (course > 4 && animation.getFrame() == 1 && !attack) {
                            attack = true;
                        }
                        if (course > 4 && animation.getFrame() == 2 && attack) {
                            attack();
                        }
                    } else {
                        if (course < 5 || (course > 4 && animation.getFrame() == 3)) {
                            pursuit();
                        }
                    }
                    offsetRect();
                    if (Game.gameInterface.getHero().isAttack()) {
                        if (RectF.intersects(Game.gameInterface.getHero().getRadAttack(), rect)) {
                            liveLine -= Game.gameInterface.getHero().getDamage();
                            if (liveLine < 1) {
                                dead = true;
                                animation.setFrames(actionImages.get(4));
                                Game.gameInterface.getHero().addExperience(exp);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Атака
     */
    private void attack() {
        if (Game.effectsSound)
            Sounds.soundPool.play(Sounds.sounds[(int) (Math.random() * 3) + sound], 0.5f, 0.5f, 0, 0, 1);
        Game.gameInterface.getHero().attacked();
        attack = false;
    }

    /**
     * Преследование
     */
    private void pursuit() {
        if (forwardX < 0 && Math.abs(forwardX) > Math.abs(forwardY)) {
            course = 0;
        } else {
            if (forwardX > 0 && Math.abs(forwardX) > Math.abs(forwardY)) {
                course = 1;
            } else {
                if (forwardY > 0 && Math.abs(forwardX) < Math.abs(forwardY)) {
                    course = 2;
                } else {
                    if (forwardY < 0 && Math.abs(forwardX) < Math.abs(forwardY)) {
                        course = 3;
                    }
                }
            }
        }
        if (beforCourse != course) {
            super.setCourse(course);
        }
        qx = hero.centerX() - rect.centerX();
        qy = hero.centerY() - rect.centerY();
        c = Math.sqrt(qx * qx + qy * qy);
        if (c > ((int) radAttack.width() >> 1)) {
            forwardX = (int) (qx / c * speed);
            forwardY = (int) (qy / c * speed);
            if (c > rect.width() / 2 + 10) {
                Collision();
                x += forwardX;
                y += forwardY;
            }
        }
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

    /**
     * Класс описывающий поле зрения враждебного моба
     */
    private class FieldOfView {

        private Point[] heroP = new Point[4];
        private int CenterX;
        private int CenterY;
        private int radius;

        public FieldOfView() {
            for (int i = 0; i < heroP.length; i++) {
                heroP[i] = new Point();
            }
        }

        /**
         * Поиск игрока, осуществляемый в пределах поля зрения
         */
        public void search() {
            CenterX = (int) (rect.centerX());
            CenterY = (int) (rect.centerY());
            heroP[0].x = (int) (hero.left - CenterX);
            heroP[0].y = (int) (hero.top - CenterY);
            heroP[1].x = (int) (hero.right - CenterX);
            heroP[1].y = (int) (hero.top - CenterY);
            heroP[2].x = (int) (hero.right - CenterX);
            heroP[2].y = (int) (hero.bottom - CenterY);
            heroP[3].x = (int) (hero.left - CenterX);
            heroP[3].y = (int) (hero.bottom - CenterY);
            for (int i = 0; i < heroP.length; i++) {
                if (heroP[i].x * heroP[i].x + heroP[i].y * heroP[i].y <= radius * radius) {
                    if (!detected) {
                        switch (course) {
                            case 0:
                                if (heroP[i].y >= heroP[i].x && heroP[i].y <= -heroP[i].x) {
                                    detected = true;
                                }
                                break;
                            case 1:
                                if (heroP[i].y <= heroP[i].x && heroP[i].y >= -heroP[i].x) {
                                    detected = true;
                                }
                                break;
                            case 2:
                                if (heroP[i].y >= heroP[i].x && heroP[i].y >= -heroP[i].x) {
                                    detected = true;
                                }
                                break;
                            case 3:
                                if (heroP[i].y <= heroP[i].x && heroP[i].y <= -heroP[i].x) {
                                    detected = true;
                                }
                                break;
                        }
                    } else {
                        detected = true;
                        speed = run;
                        break;
                    }
                } else {
                    detected = false;
                    speed = walk;
                }
            }
        }

        public void setRadius(int radius) {
            this.radius = radius;
        }
    }

    /**
     * Класс описывающий всех мобов
     */
    public static class Enemies {

        public static Enemies[] values() {
            return VALUES;
        }

        public static Enemies valueOf(String name) {
            for (Enemies enemy : VALUES) {
                if (enemy.name.compareToIgnoreCase(name) == 0) {
                    return enemy;
                }
            }
            return null;
        }

        public static final Enemies RAT;
        public static final Enemies DRIFTER;
        public static final Enemies SKELETON;
        public static final Enemies HAMSTER;
        public static final Enemies OGR;

        private static final Enemies VALUES[];
        public static final String[] enemiesName = (new String[]{"RAT", "HAMSTER", "SKELETON", "OGR", "DRIFTER"});
        private int liveLine;
        private int speed;
        private int timeAttack;
        private int size;
        private int walk;
        private int run;
        private int radiusAttack;
        private int exp;
        private int soundHit;

        static {
            RAT = new Enemies("RAT", 70, 150, 200, 3, 8, 35, 3, 5, 26);
            DRIFTER = new Enemies("DRIFTER", 100, 150, 0, 2, 8, 50, 1, 0, 0);
            SKELETON = new Enemies("SKELETON", 160, 230, 500, 1, 6, 40, 1, 15, 23);
            HAMSTER = new Enemies("HAMSTER", 5000, 350, 500, 2, 3, 100, 2, 100, 20);
            OGR = new Enemies("OGR", 9000, 380, 520, 3, 4, 190, 3, 190, 20);

            VALUES = (new Enemies[]{RAT, HAMSTER, DRIFTER, SKELETON, OGR});
        }

        private String name;

        private Enemies(String name, int liveLine, int timeAttack,
                        int radiusAttack, int walk, int run, int size,
                        int speed, int exp, int soundHit) {
            this.name = name;
            this.liveLine = liveLine;
            this.timeAttack = timeAttack;
            this.size = size;
            this.walk = walk;
            this.run = run;
            this.radiusAttack = radiusAttack;
            this.speed = speed;
            this.exp = exp;
            this.soundHit = soundHit;
        }

        public int getLiveLine() {
            return liveLine;
        }

        public int getTimeAttack() {
            return timeAttack;
        }

        public int getSize() {
            return size;
        }

        public int getWalk() {
            return walk;
        }

        public int getRun() {
            return run;
        }

        public int getRadiusAttack() {
            return radiusAttack;
        }

        public int getSpeed() {
            return speed;
        }

        public int getExp() {
            return exp;
        }

        public int getSoundHit() {
            return soundHit;
        }
    }
}


