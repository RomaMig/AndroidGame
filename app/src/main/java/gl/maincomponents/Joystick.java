package gl.maincomponents;

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


public class Joystick extends GameObject {

    private Paint bigCir = new Paint();
    private Paint smallCir = new Paint();
    private RectF area;
    private RectF bigArea;
    private static float cx;
    private static float cy;
    private static float CenterX;
    private static float CenterY;
    public int forwardX;
    public int forwardY;
    private boolean move;
    private boolean gr = true;
    private int radius;
    private int id = -1;
    private Point p = new Point();

    public Joystick(){
        this.x = (int) (Game.sDisplayMetrics.widthPixels >> 5);
        this.y = (int) (Game.sDisplayMetrics.heightPixels / 1.7);
        this.height = (int) (Game.sDisplayMetrics.heightPixels / 3.5);
        this.width = this.height;
        radius = width>>1;
        this.rect = new RectF(x, y, x + width, y + height);
        area = new RectF(0, y-100, (int)(width * 1.5), Game.sDisplayMetrics.heightPixels);
        bigArea = new RectF(0, 0, Game.sDisplayMetrics.widthPixels >>1, Game.sDisplayMetrics.heightPixels);
        bigCir.setColor(Color.WHITE);
        bigCir.setStyle(Paint.Style.STROKE);
        smallCir.setColor(Color.WHITE);
        CenterX = rect.centerX();
        CenterY = rect.centerY();
        cx = CenterX;
        cy = CenterY;
    }

    public boolean getMove(){return move;}

    public boolean Pressing(int id, float x, float y){
        float dx = x - CenterX;
        float dy = y - CenterY;
        if (bigArea.contains(x, y)) {
            this.id = id;
            p.set((int)x, (int)y);
            if (dx * dx + dy * dy <= radius * radius) {
                if (!move)
                    move = true;
                setJoyCoordinates(x, y);
                return true;
            } else {
                if (move) {
                    setJoyCoord(x, y);
                    return true;
                }
            }
        } else {
            if (!bigArea.contains(x, y) && move && this.id == id){
                move = false;
                id = -1;
            }
        }
        return false;
    }

    private void setJoyCoord(float x, float y){
        float dx = x - CenterX;
        float dy = y - CenterY;
        float R = (float)Math.sqrt(dx*dx+dy*dy)/radius;
        cx = dx / R + CenterX;
        cy = dy / R + CenterY;
    }

    public boolean noPressing(int id){
        if (move && bigArea.contains(p.x, p.y) && this.id == id) {
            move = false;
            id = -1;
            return true;
        }
        return false;
    }

    public void setJoyCoordinates(float x, float y){
        cx = x;
        cy = y;
    }

    public void joystickUpdate(){
        if (!move){
            cx = CenterX;
            cy = CenterY;
            forwardX = 0;
            forwardY = 0;
        } else {
            forwardX = (int)(cx - CenterX)>>3;
            forwardY = (int)(cy - CenterY)>>3;
        }
    }

    public void revival(){
        cx = CenterX;
        cy = CenterY;
        forwardX = 0;
        forwardY = 0;
        move = false;
    }

    public void joystickDraw(Canvas canvas){
        canvas.drawCircle(CenterX, CenterY, radius / GameScreen.scaleX, bigCir);
        canvas.drawCircle(cx, cy, width/6, smallCir);
    }
}

