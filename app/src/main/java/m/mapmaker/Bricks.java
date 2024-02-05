package m.mapmaker;

import android.graphics.Bitmap;
import android.graphics.RectF;

/**
 * Created by Роман on 30.05.2017.
 */

public class Bricks {

    private RectF rect;
    private boolean isWall;
    public static Bitmap[] sprite = new Bitmap[144];
    public static int WIDTH;
    public static int HEIGHT;
    public static final int NUM_BRICKS_WIDTH = 41;
    public static final int NUM_BRICKS_HEIGHT = 41;
    public static final int NUM_BRICKS = 1681;
    public static final int []codeTiles = {0, 0445, 0744, 0744, 0515, 0, 0711, 0544, 0, 0447, 0511, 0, 0117, 0107, 0, 0444, 0407, 0, 0444, 0701, 0, 0444, 07040, 0, 0700, 07050, 0, 0700, 05450, 0, 07000, 05150, 0, 070, 05070, 0, 070, 04010, 070, 01040, 01110, 05010, 01110, 05040, 01110, 01050, 07470, 04050, 07550, 05050, 07170, 04000, 05570, 01000, 07070, 040, 07070, 010, 05550, 0500, 05550, 04040, 07570, 01010, 07450, 050, 07150, 05170, 05470, 03332, 03301, 07772, 07701, 07772, 07701, 07772, 07701, 07772, 07701, 06662, 06601, 02222, 02201, 077777, 077770, 066667, 066660, 07777, 0, 0, 0, 0, 0, 0, 0, 0, 0, 07337, 0, 03377, 0, 03337, 0, 04777, 0727, 01777, 02327, 0777, 02627, 07713, 02703, 07743, -1, 07703, -1, 07667, -1, 06677, -1, 06667, -1, 0303, -1, 0227, 02727, 0603, 03777, 02203, 06777, 0703, 07737, 0703, 07767, 02227, 02777, 02227, 03737, 0203, 06767, 0327, 07727, 0627, 02303, 02603};

    public char id = 250;
    public Bitmap image;

    public Bricks(int row, int column) {

        isWall = false;
        rect = new RectF(column * WIDTH,
                row * HEIGHT,
                column * WIDTH + WIDTH,
                row * HEIGHT + HEIGHT);
    }

    /**
     * Установить плитку, соответствующую коду
     * @param code
     */
    public void setWall(int code){
        for (int i = 0; i<codeTiles.length; i++){
            if (codeTiles[i] == code){
                image = null;
                image = sprite[i];
                id = toChar(i+1);
                if (Integer.toOctalString(code).endsWith("0")){
                    isWall = false;
                } else {
                    isWall = true;
                }
            }
        }
    }

    private char toChar(int n){
        for (char i = 1; i<=codeTiles.length; i++){
            if ((char) n == i){
                return i;
            }
        }
        return 1;
    }

    /**
     * Установить плитку, соответствующую номеру
     * @param i
     */
    public void setWall(char i) {
        if (i <= sprite.length && i > 0) {
            id = i;
            i--;
            int j;
            if (i < 69 || i == 84 || i == 86) {
                isWall = false;
            } else {
                isWall = true;
            }
            for (int n = 0; n < sprite.length; n++) {
                if ((char) n == i) {
                    image = null;
                    image = sprite[n];
                }
            }
        } else {
            id = 1;
            isWall = false;
            image = sprite[0];
        }
    }

    public static void printBricks(Bricks[][]bricks){
        String s = "";
        for (int i = 0; i < Bricks.NUM_BRICKS_HEIGHT; i++) {
            for (int j = 0; j < Bricks.NUM_BRICKS_WIDTH; j++) {
                s += bricks[i][j].id;
            }
        }
        len(s);
    }

    public static void len(String s) {
        System.out.println();
        for (int i = 0; i < 41; i++) {
            for (int j = 0; j < 41; j++) {
                System.out.print(s.charAt(i*41+j));
            }
            System.out.println();
        }
    }

    /**
     * Считает сколько свободных(не стен) плиток
     * @param bricks
     * @return количество свободных плиток
     */
    public static int countEmpty(Bricks[][] bricks) {
        int empty = 0;
        for (int i = 0; i < NUM_BRICKS_HEIGHT; i++) {
            for (int j = 0; j < NUM_BRICKS_WIDTH; j++) {
                if (!bricks[i][j].getWall()) {
                    empty++;
                }
            }
        }
        return empty;
    }

    public boolean getWall() {
        return isWall;
    }

    public RectF getRect() {
        return rect;
    }

    public void setRect(int x, int y) {
        rect.left -= x;
        rect.right -= x;
        rect.top -= y;
        rect.bottom -= y;
    }
}

