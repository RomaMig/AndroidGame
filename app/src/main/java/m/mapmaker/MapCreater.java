package m.mapmaker;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;

import gl.intefaces.MapInterface;
import roma.illusionofdugeon.GameObject;

/**
 * Created by Роман on 30.05.2017.
 */

public class MapCreater extends GameObject {

    private Bitmap map;
    public Bricks[] bricks;

    public MapCreater() {
        x = 0;
        y = 0;
        width = Bricks.WIDTH * Bricks.NUM_BRICKS_WIDTH;
        height = Bricks.HEIGHT * Bricks.NUM_BRICKS_HEIGHT;
        bricks = new Bricks[Bricks.NUM_BRICKS];
        for (int i = 0; i < Bricks.NUM_BRICKS_HEIGHT; i++) {
            for (int j = 0; j < Bricks.NUM_BRICKS_WIDTH; j++) {
                bricks[i * Bricks.NUM_BRICKS_WIDTH + j] = new Bricks(i, j);
            }
        }
    }

    /**
     * Устанавливает новую плитку на кирпич
     *
     * @param x
     * @param y
     * @return возращает true, если плитка была установлена
     */
    public boolean setBrickVisible(float x, float y) {
        if (x < width && y < height) {
            for (int i = 0; i < bricks.length; i++) {
                if (bricks[i].getRect().contains(x, y)) {
                    bricks[i].setWall((char) (MapInterface.numTile + 1));
                    return true;
                }
            }
        }
        return false;
    }

    public void setBricks(String bricks) {
        if (bricks.length() < Bricks.NUM_BRICKS) {
            while (bricks.length() != Bricks.NUM_BRICKS) {
                bricks += (char) 1;
            }
        }
        for (int i = 0; i < Bricks.NUM_BRICKS; i++) {
            if (bricks != null) {
                this.bricks[i].setWall(bricks.charAt(i));
            }
        }
    }

    public String getBricks() {
        String msg = "";
        for (int i = 0; i < bricks.length; i++) {
            msg += bricks[i].id;
        }
        return msg;
    }

    public void update() {
        int forwardX = MapInterface.sJoystick.forwardX;
        int forwardY = MapInterface.sJoystick.forwardY;
        x -= forwardX;
        y -= forwardY;
        for (int i = 0; i < bricks.length; i++) {
            if (bricks[i] != null)
                bricks[i].setRect(forwardX, forwardY);
        }
    }

    public void draw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);
        int numBrW = (int) (x / -Bricks.WIDTH);
        int numBrH = (int) (y / -Bricks.HEIGHT);
        for (int i = numBrH - 1; i < numBrH + 15; i++) {
            for (int j = numBrW - 1; j < numBrW + 25; j++) {
                if (i > -1 && i < Bricks.NUM_BRICKS_HEIGHT && j > -1 && j < Bricks.NUM_BRICKS_WIDTH) {
                    int ind = i * Bricks.NUM_BRICKS_WIDTH + j;
                    if (bricks[ind] != null && bricks[ind].image != null) {
                        canvas.drawBitmap(bricks[ind].image, bricks[ind].getRect().left, bricks[ind].getRect().top, null);
                    }
                }
            }
        }
    }

    public void print(){
        for (int i = 0; i < bricks.length; i++) {
            System.out.print(bricks[i].id);
        }
    }
}

