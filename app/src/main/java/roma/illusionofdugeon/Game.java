package roma.illusionofdugeon;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.ProgressBar;

import gl.intefaces.GameInterface;
import gl.intefaces.MapInterface;
import gl.intefaces.MenuInterface;
import h.basicfunctions.FileWorker;
import h.basicfunctions.Font;
import h.basicfunctions.Sounds;
import m.mapmaker.Bricks;
import obj.entities.Enemy;


public class Game extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    ProgressBar progressBar;
    protected FileWorker fileWorker = new FileWorker();
    private final String DIFFICULT = "difficult";
    private final String MUSIC_SOUND = "music_sound";
    private final String EFFECTS_SOUND = "effects_sound";
    private final String VIBRATE = "vibrate";
    public static float RATIO;
    public static boolean vibrate;
    public static boolean musicSound;
    public static boolean effectsSound;
    public static int difficult;
    private static GameScreen mGameScreen;
    public static GameInterface gameInterface;
    public static MenuInterface menuInterface;
    public static MapInterface mapInterface;
    public static DisplayMetrics sDisplayMetrics;

    private int num = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        Context context = getApplicationContext();
        SharedPreferences prefs =
                PreferenceManager.getDefaultSharedPreferences(context);
        prefs.registerOnSharedPreferenceChangeListener(this);
        musicSound = prefs.getBoolean(MUSIC_SOUND, true);
        effectsSound = prefs.getBoolean(EFFECTS_SOUND, true);
        vibrate = prefs.getBoolean(VIBRATE, true);
        difficult = prefs.getInt(DIFFICULT, 0);
        GlobalVariables.context = this;
        GlobalVariables.resources = getResources();
        sDisplayMetrics = getResources().getDisplayMetrics();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        new LoadImage().execute();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        if (mGameScreen != null && musicSound) {
            mGameScreen.getPlayer().start();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        if (GlobalVariables.playing && !GlobalVariables.endlessMode) {
            GameSave.sSaved = GameSave.Save(1);
        } else {
            if (!GlobalVariables.playing && GlobalVariables.endlessMode) {
                GameSave.sSaved = GameSave.Save(2);
            }
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        GlobalVariables.playing = false;
        GlobalVariables.endlessMode = false;
        super.onDestroy();
    }

    public static GameScreen getGameScreen() {
        return mGameScreen;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key) {
            case MUSIC_SOUND:
                musicSound = sharedPreferences.getBoolean(key, true);
                break;
            case EFFECTS_SOUND:
                effectsSound = sharedPreferences.getBoolean(key, true);
                break;
            case VIBRATE:
                vibrate = sharedPreferences.getBoolean(key, true);
                break;
            case DIFFICULT:
                difficult = sharedPreferences.getInt(key, 0);
                break;
        }
    }

    public boolean loadMap(String fileName) {
        StringBuilder strBuilder = new StringBuilder(fileWorker.readFromAssets(fileName));
        Bricks[][] bricks;
        if (strBuilder.length() - 1 < Bricks.NUM_BRICKS) {
            while (strBuilder.length() != Bricks.NUM_BRICKS) {
                strBuilder.append((char) 1);
            }
        }
        bricks = new Bricks[Bricks.NUM_BRICKS_HEIGHT][Bricks.NUM_BRICKS_WIDTH];
        for (int i = 0; i < Bricks.NUM_BRICKS_HEIGHT; i++) {
            for (int j = 0; j < Bricks.NUM_BRICKS_WIDTH; j++) {
                if (strBuilder != null) {
                    bricks[i][j] = new Bricks(i, j);
                    bricks[i][j].setWall(strBuilder.charAt(i * Bricks.NUM_BRICKS_WIDTH + j));
                }
            }
        }

        if (bricks != null) {
            GlobalVariables.bricksArray.put(0, bricks);
        }
        return true;
    }

    public boolean loadMap(int num) {
        StringBuilder strBuilder = new StringBuilder(fileWorker.openFile("map" + num + ".txt"));
        Bricks[][] bricks;
        if (strBuilder.length() - 1 < Bricks.NUM_BRICKS) {
            while (strBuilder.length() != Bricks.NUM_BRICKS) {
                strBuilder.append((char) 1);
            }
        }
        bricks = new Bricks[Bricks.NUM_BRICKS_HEIGHT][Bricks.NUM_BRICKS_WIDTH];
        for (int i = 0; i < Bricks.NUM_BRICKS_HEIGHT; i++) {
            for (int j = 0; j < Bricks.NUM_BRICKS_WIDTH; j++) {
                if (strBuilder != null) {
                    bricks[i][j] = new Bricks(i, j);
                    bricks[i][j].setWall(strBuilder.charAt(i * Bricks.NUM_BRICKS_WIDTH + j));
                }
            }
        }
        if (bricks != null) {
            GlobalVariables.mapsFromMaker.put(num, bricks);
        }
        return true;
    }

    private class LoadImage extends AsyncTask<Void, Integer, Void> {

        private int[] imagesID = {
                R.drawable.heart3, R.drawable.fon, R.drawable.skill_up_final3,
                R.drawable.skill_up_final1, R.drawable.skill_up_final2,
                R.drawable.heart4, R.drawable.hero_menu, R.drawable.message_level_up,
                R.drawable.button_next, R.drawable.button_skip, R.drawable.shield,
                R.drawable.sword, R.drawable.skill_up_final4
        };

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Void doInBackground(Void... args) {
            try {
                Bitmap[] enemies = new Bitmap[5];
                enemies[0] = BitmapFactory.decodeResource(getResources(), R.drawable.enemies_final);
                enemies[1] = BitmapFactory.decodeResource(getResources(), R.drawable.humster2);
                enemies[2] = BitmapFactory.decodeResource(getResources(), R.drawable.skeleton);
                enemies[3] = BitmapFactory.decodeResource(getResources(), R.drawable.ogr);
                enemies[4] = BitmapFactory.decodeResource(getResources(), R.drawable.drifter);
                Bitmap[][][] images = new Bitmap[5][9][4];
                for (int n = 0; n < enemies.length; n++) {
                    int w = enemies[n].getWidth() / 4;
                    int num;
                    if (n != 4) {
                        num = 9;
                    } else {
                        num = 5;
                    }
                    int h = enemies[n].getHeight() / num;
                    for (int i = 0; i < num; i++) {
                        for (int j = 0; j < 4; j++) {
                            images[n][i][j] = Bitmap.createBitmap(enemies[n], j * w, i * h, w, h);
                            try {
                                finalize();
                            } catch (Throwable t) {

                            }
                        }
                    }
                    GlobalVariables.mobsImages.put(Enemy.Enemies.enemiesName[n], images[n]);
                }
                for (int i = 0; i < 11; i++) {
                    fileWorker.createFile("map" + i + ".txt");
                }
                fileWorker.createFile("Save1.txt");
                fileWorker.createFile("Save2.txt");
                for (int i = 0; i < imagesID.length; i++) {
                    GlobalVariables.images.add(BitmapFactory.decodeResource(getResources(), imagesID[i]));
                }

                RATIO = GlobalVariables.images.get(0).getWidth() / 90f;

                Sounds.loadSoundFiles();

                Font.font = BitmapFactory.decodeResource(getResources(), R.drawable.font);

                Bitmap Sprite_sheet = BitmapFactory.decodeResource(getResources(), R.drawable.sprite_sheet);
                Bricks.WIDTH = Sprite_sheet.getWidth();
                Bricks.HEIGHT = Sprite_sheet.getHeight() / Bricks.sprite.length;
                for (int i = 0; i < Bricks.sprite.length; i++) {
                    Bricks.sprite[i] = Bitmap.createBitmap(Sprite_sheet, 0, i * Bricks.HEIGHT, Bricks.WIDTH, Bricks.HEIGHT);
                }

                while (!GlobalVariables.loadBricks) {
                    if (loadMap("map0")) {
                        GlobalVariables.loadBricks = true;
                        publishProgress(num);
                    }
                }
            } catch (OutOfMemoryError e) {
            }
            try {
                finalize();
            } catch (Throwable t) {

            }
            publishProgress(100);
            return null;
        }

        protected void onPostExecute(Void image) {
            new LoadGame().execute();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.setProgress(values[0]);
        }
    }

    private class LoadGame extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Void doInBackground(Void... args) {

            try {
                gameInterface = new GameInterface(Game.this, BitmapFactory.decodeResource(getResources(),
                        R.drawable.menu_button), BitmapFactory.decodeResource(getResources(),
                        R.drawable.buttons), BitmapFactory.decodeResource(getResources(),
                        R.drawable.hero), BitmapFactory.decodeResource(getResources(),
                        R.drawable.hero_shield));
                menuInterface = new MenuInterface(Game.this, BitmapFactory.decodeResource(getResources(),
                        R.drawable.menu_background), BitmapFactory.decodeResource(getResources(),
                        R.drawable.clouds), BitmapFactory.decodeResource(getResources(),
                        R.drawable.button_newgame), BitmapFactory.decodeResource(getResources(),
                        R.drawable.button_continue), BitmapFactory.decodeResource(getResources(),
                        R.drawable.button_settings), BitmapFactory.decodeResource(getResources(),
                        R.drawable.button_mapmaker), BitmapFactory.decodeResource(getResources(),
                        R.drawable.button_endlessmode));
                mapInterface = new MapInterface(Game.this, BitmapFactory.decodeResource(getResources(),
                        R.drawable.buttons));
            } catch (OutOfMemoryError e) {
                try {
                    finalize();
                } catch (Throwable t) {

                }
            }
            publishProgress(100);
            return null;
        }

        protected void onPostExecute(Void image) {
            mGameScreen = new GameScreen(Game.this);
            setContentView(mGameScreen);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.setProgress(values[0]);
        }
    }
}

