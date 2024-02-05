package gl.intefaces;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import gl.maincomponents.Joystick;
import h.basicfunctions.ButtonsManager;
import h.basicfunctions.FileWorker;
import h.basicfunctions.Font;
import h.basicfunctions.Points;
import m.mapmaker.Bricks;
import m.mapmaker.MapCreater;
import roma.illusionofdugeon.Game;
import roma.illusionofdugeon.GameSave;
import roma.illusionofdugeon.GameScreen;
import roma.illusionofdugeon.GlobalVariables;

/**
 * Created by Роман on 30.05.2017.
 */


public class MapInterface extends MainInterface {

    private MapCreater m;
    public static int numMap;
    private ButtonsManager plus10, loadButton, changeMapButton, saveButton, menuButton, plusButton, minusButton;
    public static Joystick sJoystick;
    public static int numTile = 0;
    private FileWorker files;

    public MapInterface(final Game game, Bitmap imageButton) {
        sJoystick = new Joystick();
        numMap = 0;
        files = new FileWorker();
        m = new MapCreater();
        mButtons = new ButtonsManager[9];
        mButtons[0] = new ButtonsManager(imageButton, (int)(Game.sDisplayMetrics.widthPixels / 1.14f), 50) {
            @Override
            public void onClickListener() {
                m.setBricks(files.openFile("map" + numMap + ".txt"));
            }
        };
        mButtons[0].setTextBitmap(Font.getImageText("L", 1), 1);
        mButtons[1] = new ButtonsManager(imageButton, (int)(Game.sDisplayMetrics.widthPixels / 1.33f), 50) {
            @Override
            public void onClickListener() {
                numMap++;
                if (numMap == 11) {
                    numMap = 0;
                }
            }
        };
        mButtons[1].setTextBitmap(Font.getImageText("C", 1), 1);
        mButtons[2] = new ButtonsManager(imageButton, (int)(Game.sDisplayMetrics.widthPixels / 1.6f), 50) {
            @Override
            public void onClickListener() {
                m.print();
                Bricks.len(m.getBricks());
                files.saveFile("map" + numMap + ".txt", m.getBricks());
            }
        };
        mButtons[2].setTextBitmap(Font.getImageText("S", 1), 1);
        mButtons[3] = new ButtonsManager(imageButton, (int)(Game.sDisplayMetrics.widthPixels / 2f), 50) {
            @Override
            public void onClickListener() {
                GameScreen.gameCondition("menu");
            }
        };
        mButtons[3].setTextBitmap(Font.getImageText("M", 1), 1);
        mButtons[4] = new ButtonsManager(imageButton, (int)(Game.sDisplayMetrics.widthPixels / 4f), 50) {
            @Override
            public void onClickListener() {
                numTile++;
                if (numTile == Bricks.sprite.length) numTile = 0;
            }
        };
        mButtons[5] = new ButtonsManager(imageButton, 0, 50) {
            @Override
            public void onClickListener() {
                numTile--;
                if (numTile == -1) numTile = Bricks.sprite.length - 1;
            }
        };
        mButtons[6] = new ButtonsManager(imageButton, (int)(Game.sDisplayMetrics.widthPixels / 2.66f), 50) {
            @Override
            public void onClickListener() {
                numTile += 10;
                if (numTile >= Bricks.sprite.length) numTile = 0;
            }
        };
        mButtons[7] = new ButtonsManager(imageButton, (int)(Game.sDisplayMetrics.widthPixels / 1.14f), (int)(60 + imageButton.getHeight() * GameScreen.scaleY)) {
            @Override
            public void onClickListener() {
                FileWorker fileWorker = new FileWorker();
                GlobalVariables.endlessMode = false;
                GlobalVariables.playing = false;
                GlobalVariables.mapsFromMaker.clear();
                for (int i = 0; i<11; i++) {
                    game.loadMap(i);
                }
                Game.gameInterface.setMapMakerMod(true);
                GameSave.Load(new GameSave("0*"+numMap+"*3*0*0*0*0"));
                Game.gameInterface.setMaxMap(11);
                GameScreen.gameCondition("play");
                Game.gameInterface.setmAlpha(255);
            }
        };
        mButtons[7].setTextBitmap(Font.getImageText("P", 1), 1);
        mButtons[8] = new ButtonsManager(imageButton, (int)(Game.sDisplayMetrics.widthPixels / 1.14f), (int)(70 + imageButton.getHeight() * 2 * GameScreen.scaleY)) {
            @Override
            public void onClickListener() {
                m.setBricks(files.readFromAssets("map1"));
            }
        };
        mButtons[8].setTextBitmap(Font.getImageText("D", 1), 1);
    }

    public void actionToFalse(int id) {
        if (sJoystick.noPressing(id)) {
        } else {
            for (ButtonsManager b : mButtons) {
                b.noPressing(id);
            }
        }
    }

    public void checkGestures(Points[] fingers) {
        boolean press = false;
        for (Points point : fingers) {
            if (point != null) {
                for (ButtonsManager b : mButtons) {
                    if (b.Pressing(point.ID, point.Before.x, point.Before.y)) {
                        press = true;
                    }
                }
                if (sJoystick.Pressing(point.ID, point.Before.x, point.Before.y)) {
                    press = true;
                } else {
                    if (!press) {
                        m.setBrickVisible(point.Now.x, point.Now.y);
                    }
                }
            }
        }
    }

    public void update() {
        sJoystick.joystickUpdate();
        m.update();
        for (ButtonsManager b : mButtons) {
            b.update();
        }
    }

    public void draw(Canvas canvas) {
        m.draw(canvas);
        canvas.scale(GameScreen.scaleX, GameScreen.scaleY);
        for (ButtonsManager b : mButtons) {
            b.buttonDraw(canvas);
        }
        if (Bricks.sprite[numTile] != null) {
            canvas.drawBitmap(Bricks.sprite[numTile], 90, 50, null);
        }
        canvas.scale(1/GameScreen.scaleX, 1/GameScreen.scaleY);
        sJoystick.joystickDraw(canvas);
    }
}
