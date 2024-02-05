package gl.maincomponents;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Vibrator;

import java.util.ArrayList;

import gl.intefaces.GameInterface;
import h.basicfunctions.Animation;
import h.basicfunctions.ButtonsManager;
import h.basicfunctions.Sounds;
import obj.entities.Entity;
import roma.illusionofdugeon.Game;
import roma.illusionofdugeon.GameScreen;
import roma.illusionofdugeon.GlobalVariables;

/**
 * Created by Роман on 30.05.2017.
 */

public class Hero extends Entity {

    private Animation animation = new Animation();
    private Animation shield = new Animation();
    private int side;
    private Bitmap [][]shields;
    private Bitmap heart, emptyHeart;
    private long endAttack, timeAttack;
    private int imageX, imageY, imageWidth, imageHeight, radiusAttack, maxBlock = 1, maxHearts = 3, experience = 0, pointBlock, hearts, level, damage;
    private boolean attack = false, block = false, startAttack, dead;

    public Hero(Bitmap imageHero, Bitmap imageShield) {
        this.name = "Knight";
        imageWidth = imageHero.getWidth() / 12;
        imageHeight = imageHero.getHeight() / 16;
        imageX = (int) ((Game.sDisplayMetrics.widthPixels - imageWidth) / 2);
        imageY = (int) ((Game.sDisplayMetrics.heightPixels - imageHeight) / 3);
        width = (int) ((imageWidth * 0.5) * GameScreen.scaleX);
        height = (int) ((imageHeight * 0.3) * GameScreen.scaleY);
        x = (int) (imageX + ((imageWidth - width) >> 1));
        y = (int) (imageY + imageHeight - height);
        images = new Bitmap[16][8];
        try {
            for (int i = 0; i < 16; i++) {
                for (int j = 0; j < 8; j++) {
                    images[i][j] = Bitmap.createBitmap(imageHero, j * imageWidth, i * imageHeight, imageWidth, imageHeight);
                }
                actionImages.put(i, images[i]);
            }
            shields = new Bitmap[16][2];
            for (int i = 0; i < 16; i++) {
                for (int j = 0; j < 2; j++) {
                    shields[i][j] = Bitmap.createBitmap(imageShield, j * imageWidth, i * imageHeight, imageWidth, imageHeight);
                }
            }
        } catch (OutOfMemoryError e) {
            try {
                finalize();
            } catch (Throwable t) {

            }
        }
        damage = 50;
        radiusAttack = 20;
        rect = new RectF(x, y, x + width, y + height);
        radAttack = new RectF(rect.left - radiusAttack, rect.top - radiusAttack, rect.right + radiusAttack, rect.bottom + radiusAttack);
        heart = GlobalVariables.images.get(0);
        emptyHeart = GlobalVariables.images.get(5);
        animation.setFrames(actionImages.get(0));
        shield.setFrames(shields[0]);
        side = 0;
        shield.setDelay(1000);
        animation.setDelay(70);
    }

    public void update() {
        if ((side < 4 || (side > 7 && side < 12)) && (animation.getFrame() - 2) % 4 == 0 && Game.effectsSound) {
            Sounds.soundPool.play(Sounds.sounds[(int)(Math.random()*10)+10], 0.65f, 0.65f, 0, 0, 1);
        }
        if (timeAttack != 0) {
            long time = System.nanoTime();
            if ((time - timeAttack) / 1000000 >= 0) {
                attack = true;
                if (Game.effectsSound)
                    Sounds.soundPool.play(Sounds.sounds[(int) (Math.random() * 5 + 3)], 0.2f, 0.2f, 0, 0, 1);
                timeAttack += 850000000;
            } else attack = false;
        }
        animation.update();
        shield.update();
        if (hearts <= 0) {
            if (!dead && Game.effectsSound){
                Sounds.soundPool.play(Sounds.sounds[30], 0.5f, 0.5f, 0, 0, 1);
            }
            dead = true;
        }
    }

    private ArrayList<Message> msg = new ArrayList<>();

    public void draw(Canvas canvas) {
        if (!dead) {
            if ((side - 3) % 4 == 0) {
                canvas.drawBitmap(animation.getImage(), imageX, imageY, null);
                canvas.drawBitmap(shield.getImage(), imageX, imageY, null);
            } else {
                canvas.drawBitmap(shield.getImage(), imageX, imageY, null);
                canvas.drawBitmap(animation.getImage(), imageX, imageY, null);
            }
            if (levelUp) {
                if (Game.effectsSound){
                    Sounds.soundPool.play(Sounds.sounds[29], 0.5f, 0.5f, 0, 0, 1);
                }
                levelUp = false;
                msg.add(new Message(GlobalVariables.images.get(7)));
            }
            if (!msg.isEmpty()) {
                for (int i = 0; i < msg.size(); i++) {
                    msg.get(i).draw(canvas);
                    if (msg.get(i).getY() == imageY + 20) {
                        msg.remove(i);
                    }
                }
            }
        }
        for (int i = 0; i < maxHearts; i++) {
            canvas.drawBitmap(emptyHeart, (float)(heart.getWidth() * i * 0.67 + Game.gameInterface.getButtons()[2].getWidth() * 0.7), 0, null);
            if (i < hearts) {
                canvas.drawBitmap(heart, (float)(heart.getWidth() * i * 0.67 + Game.gameInterface.getButtons()[2].getWidth() * 0.7), 0, null);
            }
        }
    }

    private static class Message {

        private final int x = Game.gameInterface.getHero().imageX;
        private int y = Game.gameInterface.getHero().imageY + 20;
        private int alpha = 0;
        private Paint paint = new Paint();
        private Bitmap msg;

        public Message(Bitmap msg) {
            this.msg = msg;
        }

        public int getY() {
            return y;
        }

        public void restart() {
            y = Game.gameInterface.getHero().imageY + 20;
        }

        public void draw(Canvas canvas) {
            y--;
            if (alpha < 255 && y > Game.gameInterface.getHero().imageY + 4) {
                alpha += 17;
            }
            if (alpha > 0 && y < Game.gameInterface.getHero().imageY - 15) {
                alpha -= 17;
            }
            paint.setAlpha(alpha);
            canvas.drawBitmap(msg, this.x, this.y, this.paint);
            if (y == Game.gameInterface.getHero().imageY - 30) {
                restart();
            }
        }
    }

    public void attacked() {
        if (pointBlock < 1 && hearts > 0) {
            hearts--;
        }
        if (block && pointBlock > 0) {
            if (Game.effectsSound) Sounds.soundPool.play(Sounds.sounds[1], 0.2f, 0.2f, 0, 0, 1);
            pointBlock--;
        }
        if (Game.vibrate) {
            Vibrator vibrator = (Vibrator)
                    GlobalVariables.context.getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(100L);
        }
    }

    public void setSide(int n) {
        side = n;
        animation.setFrames(actionImages.get(n));
    }
    public void setSideShield(int n) {
        shield.setFrames(shields[n]);
    }
    public boolean isDead() {
        return dead;
    }

    public void revival() {
        hearts = maxHearts;
        dead = false;
    }

    private boolean levelUp;

    public void addExperience(int e) {
        experience += e;
        int befLevel = level;
        level = experience / 100;
        if (level != befLevel) {
            hearts = maxHearts;
            levelUp = true;
            Game.gameInterface.getButtons()[2].setText("" + level);
        }
    }

    public void setRadiusAttack(int r) {
        radiusAttack = r;
        radAttack = new RectF(rect.left - radiusAttack, rect.top - radiusAttack, rect.right + radiusAttack, rect.bottom + radiusAttack);
    }

    public int getImageX() {
        return imageX;
    }

    public void setImageX(int imageX) {
        this.imageX = imageX;
    }

    public int getImageY() {
        return imageY;
    }

    public void setImageY(int imageY) {
        this.imageY = imageY;
    }

    public int getRadiusAttack() {
        return radiusAttack;
    }

    public void setExperience(int e) {
        experience = e;
    }

    public int getExperience() {
        return experience;
    }

    public int getLevel() {
        return level;
    }

    public boolean isAttack() {
        return attack;
    }

    public void setAttack(boolean attack) {
        if (!dead) {
            if (!this.startAttack && attack) {
                timeAttack = System.nanoTime();
                this.startAttack = attack;
            } else {
                if (!attack) {
                    timeAttack = 0;
                    endAttack = System.nanoTime();
                    this.startAttack = attack;
                    this.attack = attack;
                }
            }
        }
    }

    public boolean isBlock() {
        return block;
    }

    public void setBlock(boolean block) {
        if (!this.block && block) {
            pointBlock = maxBlock;
        }
        if (!block) {
            pointBlock = 0;
        }
        this.block = block;
    }

    public void setLevel(int level) {
        this.level = level;
        Game.gameInterface.getButtons()[2].setText("" + level);
    }

    public boolean isStartAttack() {
        return startAttack;
    }

    public void setStartAttack(boolean startAttack) {
        this.startAttack = startAttack;
    }

    public long getTimeAttack() {
        return timeAttack;
    }

    public void setTimeAttack(long timeAttack) {
        this.timeAttack = timeAttack;
    }

    public long getEndAttack() {
        return endAttack;
    }

    public void setEndAttack(long endAttack) {
        this.endAttack = endAttack;
    }

    public int getMaxBlock() {
        return maxBlock;
    }

    public void setMaxBlock(int maxBlock) {
        this.maxBlock = maxBlock;
    }

    public int getPointBlock() {
        return pointBlock;
    }

    public void setPointBlock(int pointBlock) {
        this.pointBlock = pointBlock;
    }

    public int getHearts() {
        return hearts;
    }

    public void setHearts(int hearts) {
        this.hearts = hearts;
    }

    public int getMaxHearts() {
        return maxHearts;
    }

    public void setMaxHearts(int maxHearts) {
        this.maxHearts = maxHearts;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public static class Perks {

        public static int fullPoints = 0;
        public static int skillPoints;
        public ButtonsManager buttonPerk;
        public String text;
        private Paint paint = new Paint();

        {
            paint.setColor(Color.WHITE);
            paint.setTextSize(30 * Game.sDisplayMetrics.widthPixels / (800f));
            paint.setSubpixelText(true);
        }

        private int points = 0;
        private int perkLevel = 0;
        private int roof;
        public static final int[] roofs = new int[]{0, 1, 2, 2, 3, 3, 4, 4, 4, 5, 5, 5, 6, 6, 6, 6, 7, 7, 7, 7, 8, 8, 8, 8, 8, 9, 9, 9, 9, 9, 10};
        public static final int[] needPoints = new int[]{1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 5};
        public static final int[] nestedPoints = new int[]{0, 0, 0, 1, 0, 1, 0, 1, 2, 0, 1, 2, 0, 1, 2, 3, 0, 1, 2, 3, 0, 1, 2, 3, 4, 0, 1, 2, 3, 4};

        public Perks(ButtonsManager button, int r) {
            buttonPerk = button;
            roof = r;
        }

        public void setPoints(int p) {
            points = p;
            fullPoints++;
            perkLevel = roofs[points];
        }

        public int getPoints() {
            return points;
        }

        public int getRoof() {
            return roof;
        }

        public int getLevel() {
            return perkLevel;
        }

        public void update() {
            skillPoints = Game.gameInterface.getHero().getLevel() - fullPoints;
            buttonPerk.update();
        }

        public void draw(Canvas canvas) {
            buttonPerk.buttonDraw(canvas);
            if (needPoints[perkLevel] > 1 && points < roof) {
                canvas.drawText("" + nestedPoints[points] + "/" + needPoints[perkLevel], buttonPerk.getRect().left / GameScreen.scaleX + 5, buttonPerk.getRect().top / GameScreen.scaleY + 60, paint);
            }
            canvas.drawText("" + perkLevel, buttonPerk.getRect().left / GameScreen.scaleX + 10, buttonPerk.getRect().top / GameScreen.scaleY + 30, paint);
        }
    }
}
