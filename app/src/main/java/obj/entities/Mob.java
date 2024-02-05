package obj.entities;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import gl.maincomponents.Background;
import h.basicfunctions.Animation;
import m.mapmaker.Bricks;
import roma.illusionofdugeon.Game;
import roma.illusionofdugeon.GlobalVariables;

/**
 * Created by Роман on 30.05.2017.
 */

public class Mob extends Entity {

    private static final int LEFT_MASK = 0b0111;
    private static final int RIGHT_MASK = 0b1011;
    private static final int DOWN_MASK = 0b1101;
    private static final int UP_MASK = 0b1110;
    protected Bricks[][] bricks;
    private RectF[] rects;
    private int canCourse;
    protected int course = 0;
    private Paint mob = new Paint();
    private Background background;
    protected int forwardX;
    protected int forwardY;
    protected int n;
    protected int m;
    private int imageX;
    private int imageY;
    protected int radiusAttack;
    private long timeCourse = 10;
    private boolean crash = false;
    protected boolean dead = false;
    private int radius;
    public int level;
    protected int exp;

    /**
     * Конструктор, предназначенный для враждебных мобов
     * @param name
     * @param bg
     * @param mob
     * @param level
     */
    public Mob(String name, Background bg, Enemy.Enemies mob, int level) {
        radius = (int) (Math.random() * 10 + 2);
        this.name = name;
        this.level = level;
        this.bricks = bg.bricks;
        this.background = bg;
        this.width = (int)(mob.getSize() * Game.RATIO);
        this.height = (int)(mob.getSize() * Game.RATIO);
        this.images = GlobalVariables.mobsImages.get(name);
        rect = new RectF(x, y, (x + width), (y + height));
        radiusAttack = (int)(mob.getRadiusAttack() / 10 * Game.RATIO);
        liveLine = mob.getLiveLine() + 5 * (level + Game.difficult);
        rects = new RectF[Bricks.countEmpty(bricks)];
        speed = mob.getSpeed();
        exp = mob.getExp() + (int)(level / 3) * 2;
        for (int i = 0; i < images.length; i++) {
            actionImages.put(i, images[i]);
        }
        setLocation();
        try {
            radAttack = new RectF(rect.left - radiusAttack, rect.top - radiusAttack,
                    rect.right + radiusAttack, rect.bottom + radiusAttack);
        } catch (Exception e) {
            e.printStackTrace();
        }
        animation = new Animation();
        animation.setFrames(actionImages.get(0));
        int delay = mob.getTimeAttack() - 2 * (level + Game.difficult);
        animation.setDelay(delay < 50 ? 50 : delay);
        imageY = (int) (images[0][0].getHeight() - height);
        imageX = (int) (images[0][0].getWidth() - width) / 2;
    }

    /**
     * Конструктор, предназначенный для мирных мобов
     * @param name
     * @param bg
     */
    public Mob(String name, Background bg) {
        Enemy.Enemies mob = Enemy.Enemies.valueOf(name);
        radius = (int) (Math.random() * 10 + 2);
        this.name = name;
        this.bricks = bg.bricks;
        this.background = bg;
        this.width = (int)(mob.getSize() * Game.RATIO);
        this.height = (int)(mob.getSize() * Game.RATIO);
        this.images = GlobalVariables.mobsImages.get(name);
        rects = new RectF[Bricks.countEmpty(bricks)];
        speed = mob.getSpeed();
        liveLine = mob.getLiveLine();
        exp = mob.getExp();
        for (int i = 0; i < images.length; i++) {
            actionImages.put(i, images[i]);
        }
        setLocation();
        try {
            rect = new RectF(x, y, (x + width), (y + height));
        } catch (Exception e) {
            e.printStackTrace();
        }
        animation = new Animation();
        animation.setFrames(actionImages.get(0));
        animation.setDelay(mob.getTimeAttack());
        imageY = (int) ((images[0][0].getHeight() - height));
        imageX = (int) ((images[0][0].getWidth() - width) / 2);
    }

    public boolean isDead() {
        return dead;
    }

    /**
     * Определяет место респавна
     */
    protected void setLocation() {
        int r = 0;
        for (int i = 0; i < Bricks.NUM_BRICKS_HEIGHT; i++) {
            for (int j = 0; j < Bricks.NUM_BRICKS_WIDTH; j++) {
                if (!bricks[i][j].getWall()) {
                    rects[r] = bricks[i][j].getRect();
                    r++;
                }
            }
        }
        int l = (int) (Math.random() * rects.length);
        x = (int) rects[l].left + 3;
        y = (int) rects[l].top + 3;
        n = x - (int) bricks[0][0].getRect().left;
        m = y - (int) bricks[0][0].getRect().top;
    }

    protected boolean attacked;
    public void update() {
        offset();
        offsetRect();
        if (Game.gameInterface.getmBackground().getRect().contains(rect)) {
            animation.update();
            if (!dead) {
                walk();
                offsetRect();
                if (Game.gameInterface.getHero().isAttack()) {
                    if (RectF.intersects(Game.gameInterface.getHero().getRadAttack(), rect)) {
                        attacked();
                        attacked = true;
                        if (liveLine < 1) {
                            dead = true;
                            animation.setFrames(actionImages.get(4));
                            Game.gameInterface.getHero().addExperience(exp);
                        }
                    }
                }else {
                    attacked = false;
                }
            } else {
                if (attacked){
                    attacked = false;
                }
            }
        }
    }

    /**
     * Что делать мобу, если его атакуют?
     * Наверное, притвориться мертвым
     */
    protected  void attacked(){
        liveLine -= Game.gameInterface.getHero().getDamage();
    }

    protected void offsetRect(){
        rect.set(x, y, x + width, y + height);
        if (radAttack != null) {
            radAttack.set(x - radiusAttack, y - radiusAttack, rect.right + radiusAttack, rect.bottom + radiusAttack);
        }
    }

    protected void offset() {
        x -= background.getForwardX();
        y -= background.getForwardY();
    }

    /**
     * Передвижение моба
     */
    private void walk() {
        Collision();
        switch (course) {
            case 0:
                if (canCourse < 8) crash = true;
                break;
            case 1:
                if (canCourse < 4 || (canCourse > 7 && canCourse < 12)) crash = true;
                break;
            case 2:
                if (canCourse % 4 == 0 || (canCourse - 1) % 4 == 0) crash = true;
                break;
            case 3:
                if (canCourse % 2 == 0) crash = true;
                break;
        }
        if (System.nanoTime() / 1000000000 - timeCourse == 10 || crash)
            setCourse(-1);
        switch (course) {
            case 0:
                forwardX = -speed;
                forwardY = 0;
                break;
            case 1:
                forwardX = speed;
                forwardY = 0;
                break;
            case 2:
                forwardX = 0;
                forwardY = speed;
                break;
            case 3:
                forwardX = 0;
                forwardY = -speed;
                break;
        }
        x += forwardX;
        y += forwardY;
    }

    private StringBuilder mChoiceCourse;
    private static final String[] INSERT = {"0000", "000", "00", "0", ""};

    /**
     * Устанавливает курс
     * @param c
     */
    protected void setCourse(int c) {
        if (c == -1) {
            timeCourse = System.nanoTime() / 1000000000;
            mChoiceCourse = new StringBuilder(Integer.toBinaryString(canCourse));
            mChoiceCourse.insert(0, INSERT[mChoiceCourse.length()]);
            do {
                course = (int) (Math.random() * 4);
            } while (mChoiceCourse.charAt(course) == '0');
            crash = false;
        } else {
            course = c;
        }
        animation.setFrames(actionImages.get(course));
    }

    /**
     * Обработка столкновений
     */
    protected void Collision() {
        canCourse = 0b1111;
        int numBrW = (int) (n / Bricks.WIDTH);
        int numBrH = (int) (m / Bricks.HEIGHT);
        for (int i = numBrH - 4; i < numBrH + 6; i++) {
            for (int j = numBrW - 4; j < numBrW + 6; j++) {
                if (i >= 0 && i < Bricks.NUM_BRICKS_HEIGHT && j >= 0 && j < Bricks.NUM_BRICKS_WIDTH) {
                    if (bricks[i][j] != null) {
                        if (bricks[i][j].getWall()) {
                            if (bricks[i][j].getRect().contains(rect.left - radius, rect.bottom) || bricks[i][j].getRect().contains(rect.left - radius, rect.top)) {
                                canCourse &= LEFT_MASK;
                                if (bricks[i][j].getRect().contains(rect.left + forwardX, rect.bottom) || bricks[i][j].getRect().contains(rect.left + forwardX, rect.top)) {
                                    forwardX = 0;
                                }
                            }
                            if (bricks[i][j].getRect().contains(rect.right + radius, rect.top) || bricks[i][j].getRect().contains(rect.right + radius, rect.bottom)) {
                                canCourse &= RIGHT_MASK;
                                if (bricks[i][j].getRect().contains(rect.right + forwardX, rect.top) || bricks[i][j].getRect().contains(rect.right + forwardX, rect.bottom)) {
                                    forwardX = 0;
                                }
                            }
                            if (bricks[i][j].getRect().contains(rect.centerX(), rect.bottom + radius) || bricks[i][j].getRect().contains(rect.right, rect.bottom + radius) || bricks[i][j].getRect().contains(rect.left, rect.bottom + radius)) {
                                canCourse &= DOWN_MASK;
                                if (bricks[i][j].getRect().contains(rect.centerX(), rect.bottom + forwardY) || bricks[i][j].getRect().contains(rect.right, rect.bottom + forwardY) || bricks[i][j].getRect().contains(rect.left, rect.bottom + forwardY)) {
                                    forwardY = 0;
                                }
                            }
                            if (bricks[i][j].getRect().contains(rect.centerX(), rect.top - radius) || bricks[i][j].getRect().contains(rect.right, rect.top - radius) || bricks[i][j].getRect().contains(rect.left, rect.top - radius)) {
                                canCourse &= UP_MASK;
                                if (bricks[i][j].getRect().contains(rect.centerX(), rect.top + forwardY) || bricks[i][j].getRect().contains(rect.right, rect.top + forwardY) || bricks[i][j].getRect().contains(rect.left, rect.top + forwardY)) {
                                    forwardY = 0;
                                }
                            }
                        }
                    }
                }
            }
        }
        n += forwardX;
        m += forwardY;
    }

    public void draw(Canvas canvas) {
        if (Game.gameInterface.getmBackground().getRect().contains(rect)) {
            canvas.drawBitmap(animation.getImage(), x - imageX, y - imageY, null);
        }
    }

    @Override
    public String toString() {
        return "Mob{" +
                "course=" + course +
                ", forwardX=" + forwardX +
                ", forwardY=" + forwardY +
                ", dead=" + dead +
                ", radiusAttack=" + radiusAttack +
                ", exp=" + exp +
                ", level=" + level +
                '}';
    }
}

