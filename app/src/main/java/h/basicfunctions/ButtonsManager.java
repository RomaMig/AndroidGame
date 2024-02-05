package h.basicfunctions;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;

import roma.illusionofdugeon.Game;
import roma.illusionofdugeon.GameObject;
import roma.illusionofdugeon.GameScreen;

/**
 * Created by Роман on 30.05.2017.
 */

public class ButtonsManager extends GameObject {

    private boolean isPressed;
    private boolean pressUp;
    private boolean beforePressed = false;
    private boolean action;
    private int imageX;
    private int imageY;
    private int imageWidth;
    private int imageHeight;
    private Paint paint;
    private Bitmap[] imageButton = new Bitmap[2];
    private Bitmap[] imagesOnBitmap;
    private int frame;
    private int time = 0;
    private String text;
    private int id = -1;
    private int sound = -1;
    private Point p = new Point();

    public ButtonsManager(Bitmap image, int x, int y) {
        this.isPressed = false;
        this.x = x;
        this.y = y;
        this.width = (int) ((image.getWidth() >> 1) * GameScreen.scaleX);
        this.height = (int) (image.getHeight() * GameScreen.scaleY);
        imagesOnBitmap = null;
        this.rect = new RectF(x, y, x + width, y + height);
        if (image != null) {
            imageX = (int) (x / GameScreen.scaleX);
            imageY = (int) (y / GameScreen.scaleY);
            imageWidth = image.getWidth() >> 1;
            imageHeight = image.getHeight();
            for (int i = 0; i < 2; i++) {
                this.imageButton[i] = Bitmap.createBitmap(image, i * imageWidth, 0, imageWidth, imageHeight);
            }
        }
    }

    public ButtonsManager(int x, int y, int width, int height, Bitmap image) {
        this.isPressed = false;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        imageX = (int) (x / GameScreen.scaleX);
        imageY = (int) (y / GameScreen.scaleY);
        imageWidth = (int) (width / GameScreen.scaleX);
        imageHeight = (int) (height / GameScreen.scaleY);
        for (int i = 0; i < 2; i++) {
            this.imageButton[i] = Bitmap.createBitmap(image, i * imageWidth, 0, imageWidth, imageHeight);
        }
        imagesOnBitmap = null;
        this.rect = new RectF(x, y, x + width, y + height);
    }

    public void setTextBitmap(Bitmap[] image, int frames) {
        imagesOnBitmap = new Bitmap[frames];
        imagesOnBitmap = image;
    }

    public void setText(String str) {
        text = str;
    }

    private Paint textPaint;

    public void setTextPaint(float size, int color, boolean antiAlias) {
        textPaint = new Paint();
        textPaint.setTextSize(size);
        textPaint.setColor(color);
        textPaint.setAntiAlias(antiAlias);
    }

    /**
     * Проверяет была ли нажата кнопка,
     * иначе при совпадении id касания
     * автоматически отпускает кнопку
     * @param id касания
     * @param x
     * @param y
     * @return true если произошло нажатие на кнопку
     */
    public boolean Pressing(int id, float x, float y) {
        if (rect.contains(x, y)) {
            this.id = id;
            p.set((int) x, (int) y);
            if (!isPressed) {
                isPressed = true;
                frame = 1;
                onPress();
                if (paint != null) {
                    paint.setColor(Color.DKGRAY);
                }
            }
            return true;
        } else {
            if (this.id == id) {
                pressUp = true;
                isPressed = false;
                id = -1;
                frame = 0;
                onUp();
                if (paint != null) {
                    paint.setColor(Color.WHITE);
                }
            }
        }
        return false;
    }

    /**
     * Проверяет была ли отпущена кнопка
     * @param id
     * @return true если id кнопки
     * и id касания совпадают
     */
    public boolean noPressing(int id) {
        if (isPressed && this.id == id && rect.contains(p.x, p.y)) {
            pressUp = true;
            isPressed = false;
            id = -1;
            frame = 0;
            if (sound != -1 && Game.effectsSound){
                Sounds.soundPool.play(sound, 1f, 1f, 0, 0, 1);
            }
            onClickListener();
            if (paint != null) {
                paint.setColor(Color.WHITE);
            }
            return true;
        }
        return false;
    }

    /**
     * Принудительное отпускание кнопки
     */
    public void noPressing() {
        pressUp = true;
        isPressed = false;
        id = -1;
        frame = 0;
        onClickListener();
        if (paint != null) {
            paint.setColor(Color.WHITE);
        }
    }

    public void update() {
        if (isPressed) {
            onHold();
        }
        if (pressUp && !isPressed) {
            action = true;
            pressUp = false;
            id = -1;
        } else {
            action = false;
        }
    }

    /**
     * Действия при нажатии кнопки
     */
    public void onPress() {
    }

    /**
     * Действия при удержании кнопки
     */
    public void onHold() {
    }

    /**
     * Действия при полном клике по кнопке
     */
    public void onClickListener() {
    }

    /**
     * Действия при отпускании кнопки
     */
    public void onUp() {
    }

    public void setSound(int sound){
        this.sound = sound;
    }

    public void buttonDraw(Canvas canvas) {
        if (paint != null) {
            canvas.drawRect(imageX, imageY, imageX + imageWidth, imageY + imageHeight, paint);
        }
        if (imageButton != null) {
            canvas.drawBitmap(imageButton[frame], imageX, imageY, null);
            if (imagesOnBitmap != null) {
                for (int i = 0; i < imagesOnBitmap.length; i++) {
                    canvas.drawBitmap(imagesOnBitmap[i], imageX + imagesOnBitmap[i].getWidth() * i + 5, imageY + 15, null);
                }
            }
        }
        if (text != null) {
            canvas.drawText(text, imageX + 8 * GameScreen.RATIO_WIDTHS, imageY + 50 * GameScreen.RATIO_HEIGHTS, textPaint);
        }
    }

    /**
     * Создание кисти для текста
     */
    public void createPaint() {
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setTextSize(30 * GameScreen.RATIO_WIDTHS);
    }

    public void setPress(boolean b) {
        isPressed = b;
    }

    public boolean getPress() {
        return isPressed;
    }

    public boolean getAction() {
        return action;
    }

    public int getId() {
        return id;
    }
}

