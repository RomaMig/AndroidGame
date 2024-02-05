package gl.intefaces;

/**
 * Created by Роман on 30.05.2017.
 */

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.io.File;

import h.basicfunctions.ButtonsManager;
import h.basicfunctions.FileWorker;
import h.basicfunctions.Points;
import h.basicfunctions.Sounds;
import roma.illusionofdugeon.Game;
import roma.illusionofdugeon.GameSave;
import roma.illusionofdugeon.GameScreen;
import roma.illusionofdugeon.GlobalVariables;
import roma.illusionofdugeon.SettingsActivity;

public class MenuInterface extends MainInterface {

    private Bitmap imageMenu, imageClouds;
    private float x;
    private static ButtonsManager startButton, resumButton, supportsButton;

    public MenuInterface(final Game game, Bitmap imageMenu, Bitmap imageClouds, Bitmap startBitmap, Bitmap resumBitmap, Bitmap supportsBitmap,
                         Bitmap mapmakerBitmap, Bitmap endlessModeBitmap) {
        this.imageMenu = imageMenu;
        this.imageClouds = imageClouds;
        mButtons = new ButtonsManager[5];
        mButtons[0] = new ButtonsManager(startBitmap, (int) (Game.sDisplayMetrics.widthPixels / 3.4f), (int) (Game.sDisplayMetrics.heightPixels / 5.3f)) {
            @Override
            public void onClickListener() {
                FileWorker fileWorker = new FileWorker();
                GlobalVariables.endlessMode = false;
                GameSave save = new GameSave(fileWorker.readFromAssets("SaveDefolt"));
                GameSave.Load(save);
                Game.gameInterface.setMaxMap(10);
                GameScreen.gameCondition("play");
                Game.gameInterface.setmAlpha(255);
                if (!GlobalVariables.playing) {
                    GlobalVariables.playing = true;
                }
            }
        };
        mButtons[1] = new ButtonsManager(resumBitmap, (int) (Game.sDisplayMetrics.widthPixels / 3.4f), (int) (Game.sDisplayMetrics.heightPixels / 2.9f)) {
            @Override
            public void onClickListener() {
                GameSave save;
                FileWorker fileWorker = new FileWorker();
                GlobalVariables.endlessMode = false;
                if (!GlobalVariables.playing) {
                    if (new File(GlobalVariables.context.getFilesDir() + "/Save1.txt").exists()) {
                        save = new GameSave(fileWorker.openFile("Save1.txt"));
                    } else save = new GameSave(fileWorker.readFromAssets("SaveDefolt"));
                    GameSave.Load(save);
                    Game.gameInterface.setmAlpha(255);
                    GlobalVariables.playing = true;
                    Game.gameInterface.setMaxMap(10);
                }
                GameScreen.gameCondition("play");
            }
        };
        mButtons[4] = new ButtonsManager(endlessModeBitmap, (int) (Game.sDisplayMetrics.widthPixels / 3.4f), (int) (Game.sDisplayMetrics.heightPixels / 2f)) {
            @Override
            public void onClickListener() {
                if (!GlobalVariables.endlessMode) {
                    FileWorker fileWorker = new FileWorker();
                    GlobalVariables.endlessMode = true;
                    GameSave save;
                    if (new File(GlobalVariables.context.getFilesDir() + "/Save1.txt").exists()) {
                        save = new GameSave(fileWorker.openFile("Save2.txt"));
                    } else save = new GameSave(fileWorker.readFromAssets("SaveDefolt"));
                    GameSave.Load(save);
                    Game.gameInterface.setMaxMap(99999);
                    Game.gameInterface.setmAlpha(255);
                }
                GlobalVariables.playing = false;
                GameScreen.gameCondition("play");
            }
        };
        mButtons[2] = new ButtonsManager(supportsBitmap, (int) (Game.sDisplayMetrics.widthPixels / 3.4f), (int) (Game.sDisplayMetrics.heightPixels / 1.52f)) {
            @Override
            public void onClickListener() {
                Intent intent = new Intent(game, SettingsActivity.class);
                GlobalVariables.context.startActivity(intent);
            }
        };
        mButtons[3] = new ButtonsManager(mapmakerBitmap, (int) (Game.sDisplayMetrics.widthPixels / 3.4f), (int) (Game.sDisplayMetrics.heightPixels / 1.23f)) {
            @Override
            public void onClickListener() {
                GameScreen.gameCondition("mapMaker");
            }
        };
        for (ButtonsManager b : mButtons) {
            b.setSound(Sounds.sounds[2]);
        }
    }

    @Override
    public void actionToFalse(int id) {
        for (ButtonsManager b : mButtons) {
            b.noPressing(id);
        }
    }

    @Override
    public void checkGestures(Points[] fingers) {
        for (Points point : fingers) {
            if (point != null) {
                for (ButtonsManager b : mButtons) {
                    b.Pressing(point.ID, point.Before.x, point.Before.y);
                }
            }
        }
    }

    @Override
    public void update() {
        x += 0.5f;
        if (x == imageClouds.getWidth()) {
            x = 0;
        }
        for (ButtonsManager b : mButtons) {
            b.update();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.scale(GameScreen.scaleX, GameScreen.scaleY);
        canvas.drawBitmap(imageMenu, 0, 0, null);
        canvas.drawBitmap(imageClouds, x - imageClouds.getWidth(), 0, null);
        canvas.drawBitmap(imageClouds, x, 0, null);
        for (ButtonsManager b : mButtons) {
            b.buttonDraw(canvas);
        }
    }
}

