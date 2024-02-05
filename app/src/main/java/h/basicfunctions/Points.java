package h.basicfunctions;

/**
 * Created by Роман on 30.05.2017.
 */

import android.graphics.Point;

public class Points {
    public int ID;
    public static int size = 5;
    public Point Now;
    public Point Before;
    public int action;
    boolean enabled = false;

    public Points(int id, int x, int y){
        ID = id;
        Now = Before = new Point(x, y);
    }

    public void setNow(int x, int y){
        if(!enabled){
            enabled = true;
            Now = Before = new Point(x, y);
        }else{
            Before = Now;
            Now = new Point(x, y);
        }
    }

    public void setAction(int action){
        this.action = action;
    }
    public int getAction(){
        return action;
    }
    public static Points getDefoltFinger(){
        return new Points(-10, -1, -1);
    }

    @Override
    public String toString() {
        return "Points{" +
                "ID=" + ID +
                ", Now=" + Now +
                ", Before=" + Before +
                ", action=" + action +
                ", enabled=" + enabled +
                '}';
    }
}
