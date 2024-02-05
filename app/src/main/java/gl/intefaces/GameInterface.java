package gl.intefaces;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;

import java.util.LinkedList;
import java.util.Stack;

import gl.maincomponents.Background;
import gl.maincomponents.Dialog;
import gl.maincomponents.Hero;
import gl.maincomponents.HeroMenu;
import gl.maincomponents.Joystick;
import h.basicfunctions.ButtonsManager;
import h.basicfunctions.Points;
import h.basicfunctions.Sounds;
import m.mapmaker.Bricks;
import obj.entities.Boss;
import obj.entities.Drifter;
import obj.entities.Enemy;
import obj.entities.Mob;
import roma.illusionofdugeon.Game;
import roma.illusionofdugeon.GameSave;
import roma.illusionofdugeon.GameScreen;
import roma.illusionofdugeon.GlobalVariables;

/**
 * Created by Роман on 30.05.2017.
 */

public class GameInterface extends MainInterface {

    private HeroMenu menu;
    private Dialog dialog;
    private Joystick mJoystick;
    private Hero hero;
    private Background mBackground;
    private Mob[] mMobs = new Enemy[45];
    private Mob mBoss;
    private Drifter mDrifter;
    private int thisMap, maxMap;
    private Bricks[][] bricks = new Bricks[Bricks.NUM_BRICKS_HEIGHT][Bricks.NUM_BRICKS_WIDTH];
    private Paint blackout = new Paint();
    private Paint text = new Paint();
    private boolean upLevel;
    private boolean downLevel;
    private int mAlpha = 0;
    private boolean mapMakerMod;
    private MainInterface mainInterface;

    {
        blackout.setColor(Color.BLACK);
        text.setColor(Color.WHITE);
        text.setTextSize(50 * GameScreen.RATIO_WIDTHS);
        text.setAntiAlias(true);
    }

    public GameInterface(final Game game,
                         Bitmap imageMeButton, Bitmap imageAcButton,
                         Bitmap imageHero, Bitmap imageShieldHero) {
        hero = new Hero(imageHero, imageShieldHero);
        mJoystick = new Joystick();
        mButtons = new ButtonsManager[4];
        maxMap = 10;
        mButtons[0] = new ButtonsManager((int) (Game.sDisplayMetrics.widthPixels / 1.22), (int) (Game.sDisplayMetrics.heightPixels / 1.4), (int) (80 * GameScreen.RATIO_WIDTHS), (int) (80 * GameScreen.RATIO_HEIGHTS), GlobalVariables.images.get(11)) {

            @Override
            public void onPress() {
                hero.setAttack(true);
            }

            @Override
            public void onHold() {
                hero.setAttack(true);
            }

            @Override
            public void onClickListener() {
                hero.setAttack(false);
            }

            @Override
            public void onUp() {
                hero.setAttack(false);
            }
        };
        mButtons[1] = new ButtonsManager((int) (Game.sDisplayMetrics.widthPixels / 1.4), (int) (Game.sDisplayMetrics.heightPixels / 1.4), (int) (80 * GameScreen.RATIO_WIDTHS), (int) (80 * GameScreen.RATIO_HEIGHTS), GlobalVariables.images.get(10)) {

            @Override
            public void onPress() {
                hero.setBlock(true);
            }

            @Override
            public void onHold() {
                hero.setBlock(true);
            }

            @Override
            public void onClickListener() {
                hero.setBlock(false);
            }

            @Override
            public void onUp() {
                hero.setBlock(false);
            }
        };
        mButtons[0].createPaint();
        mButtons[1].createPaint();
        mButtons[2] = new ButtonsManager(GlobalVariables.images.get(6), 0, 0) {
            @Override
            public void onClickListener() {
                if (mainInterface != null) {
                    mainInterface = null;
                } else {
                    mJoystick.revival();
                    mainInterface = menu;
                    Hero.Perks.fullPoints = 0;
                    for (int i = 0; i < menu.getPerks().length; i++) {
                        Hero.Perks.fullPoints += menu.getPerks()[i].getPoints();
                    }
                }
            }
        };
        mButtons[2].setTextPaint(50 * GameScreen.RATIO_WIDTHS, Color.WHITE, true);
        mButtons[3] = new ButtonsManager(imageMeButton, (int) (Game.sDisplayMetrics.widthPixels / 1.12), (int) (Game.sDisplayMetrics.heightPixels / 60)) {
            @Override
            public void onClickListener() {
                mJoystick.revival();
                if (!mapMakerMod) {
                    GameScreen.gameCondition("menu");
                    GameSave.sSaved = GameSave.Save(GlobalVariables.endlessMode ? 2 : 1);
                }else {
                    GameScreen.gameCondition("mapMaker");
                    mapMakerMod = false;
                }
            }
        };
        mButtons[2].setSound(Sounds.sounds[2]);
        mButtons[3].setSound(Sounds.sounds[2]);
        menu = new HeroMenu(hero);
        dialog = new Dialog(game);
    }

    @Override
    public void actionToFalse(int id) {
        if (mainInterface != null) {
            mainInterface.actionToFalse(id);
        } else {
            for (ButtonsManager b : mButtons) {
                b.noPressing(id);
            }
            mJoystick.noPressing(id);
        }
    }

    @Override
    public void checkGestures(Points[] fingers) {
        for (Points point : fingers) {
            if (point != null) {
                if (mainInterface != null) {
                    mainInterface.checkGestures(fingers);
                } else {
                    for (ButtonsManager b : mButtons) {
                        b.Pressing(point.ID, point.Before.x, point.Before.y);
                    }
                    mJoystick.Pressing(point.ID, point.Before.x, point.Before.y);
                    if (mDrifter != null) mDrifter.startDialog(point.Before.x, point.Before.y);
                    if (mBoss instanceof Drifter)
                        ((Drifter) mBoss).startDialog(point.Before.x, point.Before.y);
                }
            }
        }
    }

    @Override
    public void update() {
        if (mainInterface != null) {
            mainInterface.update();
        } else {
            mBackground.update();
            hero.update();
            mJoystick.joystickUpdate();
            for (ButtonsManager b : mButtons) {
                b.update();
            }
            for (Mob m : mMobs) {
                m.update();
            }
            if (mBoss != null) mBoss.update();
            if (mDrifter != null) mDrifter.update();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if (mainInterface != null) {
            mainInterface.draw(canvas);
        } else {
            mBackground.draw(canvas);
            if (mBoss != null) mBoss.draw(canvas);
            if (mDrifter != null) mDrifter.draw(canvas);
            for (Mob m : mMobs) {
                m.draw(canvas);
            }
            hero.draw(canvas);
            mJoystick.joystickDraw(canvas);
            mButtons[2].buttonDraw(canvas);
            canvas.scale(GameScreen.scaleX, GameScreen.scaleY);
            for (int i = 0; i < mButtons.length; i++) {
                if (i != 2) {
                    mButtons[i].buttonDraw(canvas);
                }
            }
            changeScene();
            if (mAlpha > 0) {
                blackout.setAlpha(mAlpha);
                text.setAlpha(mAlpha);
                canvas.scale(1/GameScreen.scaleX, 1/GameScreen.scaleY);
                canvas.drawPaint(blackout);
                canvas.drawText((thisMap + 1) + " Подземелье, " + hero.getLevel() + " Уровень", Game.sDisplayMetrics.widthPixels / 16, Game.sDisplayMetrics.heightPixels / 2 - 20, text);
            }
        }
    }

    private void changeScene() {
        if (upLevel || downLevel || hero.isDead()) {
            int map = thisMap;
            if (mAlpha < 255) {
                mAlpha += 17;
            }
            if (mAlpha == 255) {
                if (downLevel) {
                    if (++map < maxMap) {
                        setMap(map);
                        downLevel = false;
                    }
                } else {
                    if (upLevel) {
                        if (--map >= -1) {
                            setMap(map);
                            upLevel = false;
                        }
                    } else {
                        if (hero.isDead()) {
                            if (GlobalVariables.endlessMode) {
                                GameSave.Load(new GameSave("0*0*3*0*0*0*0"));
                            } else {
                                setMap(thisMap);
                            }
                            hero.revival();
                        }
                    }
                }
            }
        } else {
            if (mAlpha > 0) {
                mAlpha -= 17;
            }
        }
    }

    public void setMap(int n) {
        if (n == -1) {
            GameScreen.gameCondition("menu");
            return;
        }
        thisMap = n;
        int[] code = null;
        if (!mapMakerMod) {
            if (n % 3 == 0 && n != 0 && !GlobalVariables.endlessMode) {
                bricks = GlobalVariables.bricksArray.get(0);
            } else {
                bricks = new Bricks[Bricks.NUM_BRICKS_HEIGHT][Bricks.NUM_BRICKS_WIDTH];
                Level level = new Level(21, Bricks.NUM_BRICKS_WIDTH);
                level.generateLevel();
                code = level.getCodeLevel();
            }
        } else {
            bricks = GlobalVariables.mapsFromMaker.get(n);
        }
        Point point = new Point();
        for (int i = 0; i < Bricks.NUM_BRICKS_HEIGHT; i++) {
            for (int j = 0; j < Bricks.NUM_BRICKS_WIDTH; j++) {
                if ((n % 3 != 0 || n == 0 || GlobalVariables.endlessMode) && !mapMakerMod) {
                    this.bricks[i][j] = new Bricks(i, j);
                    this.bricks[i][j].setWall(code[i * Bricks.NUM_BRICKS_WIDTH + j]);
                }
                if (upLevel) {
                    if (this.bricks[i][j].id == 87) {
                        point.x = (int) (bricks[i][j].getRect().left - hero.getRect().left + 5);
                        point.y = (int) (bricks[i][j].getRect().bottom - hero.getRect().top + 15);
                    }
                } else {
                    if (bricks[i][j].id == 85) {
                        point.x = (int) (bricks[i][j].getRect().left - hero.getRect().left + 5);
                        point.y = (int) (bricks[i][j].getRect().bottom - hero.getRect().top + 15);
                    }
                }
            }
        }
        for (int i = 0; i < Bricks.NUM_BRICKS_HEIGHT; i++) {
            for (int j = 0; j < Bricks.NUM_BRICKS_WIDTH; j++) {
                bricks[i][j].setRect(point.x, point.y);
            }
        }
        mBackground = new Background(hero.getRect(), bricks);
        if (n % 3 == 0 && n != 0 && !GlobalVariables.endlessMode && !mapMakerMod) {
            if (n != 9) {
                mBoss = new Boss(Boss.bosses[n / 3 - 1], mBackground, hero.getRect(), n);
                mBackground.setBoss((Enemy) mBoss);
            } else {
                mBoss = new Drifter("DRIFTER", mBackground, hero.getRect(), (int) (n / 2.25));
            }
        } else {
            mBoss = null;
        }
        if (n != 9 && !GlobalVariables.endlessMode && !mapMakerMod) {
            mDrifter = new Drifter("DRIFTER", mBackground, hero.getRect(), (int) (n / 2.25));
        } else {
            mDrifter = null;
        }
        String name = "RAT";
        for (int i = 0; i < mMobs.length; i++) {
            if (i > 30) {
                name = "SKELETON";
            }
            mMobs[i] = new Enemy(name, mBackground, hero.getRect(), n);
        }
        try {
            finalize();
        } catch (Throwable t) {
            Log.e("Error", t.toString());
        }
    }

    public void resume() {
        hero.setMaxHearts(menu.getPerks()[0].getLevel() + 3);
        hero.setDamage(menu.getPerks()[1].getLevel() * 10 + 50);
        hero.setMaxBlock(menu.getPerks()[2].getLevel() + 1);
        hero.setRadiusAttack(menu.getPerks()[3].getLevel() * 2 + 20);
    }

    public Background getmBackground() {
        return mBackground;
    }

    public void setmBackground(Background mBackground) {
        this.mBackground = mBackground;
    }

    public Mob[] getmMobs() {
        return mMobs;
    }

    public void setmMobs(Mob[] mMobs) {
        this.mMobs = mMobs;
    }

    public Mob getmBoss() {
        return mBoss;
    }

    public void setmBoss(Mob mBoss) {
        this.mBoss = mBoss;
    }

    public Drifter getmDrifter() {
        return mDrifter;
    }

    public void setmDrifter(Drifter mDrifter) {
        this.mDrifter = mDrifter;
    }

    public Joystick getmJoystick() {
        return mJoystick;
    }

    public Hero getHero() {
        return hero;
    }

    public int getThisMap() {
        return thisMap;
    }

    public void setThisMap(int thisMap) {
        this.thisMap = thisMap;
    }

    public Bricks[][] getBricks() {
        return bricks;
    }

    public void setBricks(Bricks[][] bricks) {
        this.bricks = bricks;
    }

    public boolean isUpLevel() {
        return upLevel;
    }

    public void setUpLevel(boolean upLevel) {
        this.upLevel = upLevel;
    }

    public boolean isDownLevel() {
        return downLevel;
    }

    public void setDownLevel(boolean downLevel) {
        this.downLevel = downLevel;
    }

    public int getmAlpha() {
        return mAlpha;
    }

    public void setmAlpha(int mAlpha) {
        this.mAlpha = mAlpha;
    }

    public MainInterface getMainInterface() {
        return mainInterface;
    }

    public void setMainInterface(MainInterface mainInterface) {
        this.mainInterface = mainInterface;
    }

    public Dialog getDialog() {
        return dialog;
    }

    public HeroMenu getMenu() {
        return menu;
    }

    public void setMaxMap(int m) {
        maxMap = m;
    }

    public void setMapMakerMod(boolean mapMakerMod) {
        this.mapMakerMod = mapMakerMod;
    }

    public class Level {

        private int height;
        private int width;
        private Cell[][] level;
        private LinkedList<Cell> endLevel = new LinkedList<>();
        private LinkedList<Cell[]> levelLine = new LinkedList<>();
        private Stack<Cell> stack = new Stack<>();

        public Level(int height, int width) {
            this.height = height;
            this.width = width;
            level = new Cell[height][width];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if ((i % 2 != 0 && j % 2 != 0) && (i < height - 1 && j < width - 1))
                        level[i][j] = new Cell(i, j, 0);
                    else
                        level[i][j] = new Cell(i, j, 1);
                    if (i == 0 || j == 0 || i == height - 1 || j == width - 1) {
                        level[i][j].setVisited();
                    }
                }
            }
        }

        public boolean unvisitedExist() {
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (!level[i][j].getVisited()) {
                        unvis = level[i][j];
                        return true;
                    }
                }
            }
            return false;
        }

        public void neighboursUnvisited(int x, int y) {
            if (y - 2 > -1) {
                if (!level[x][y - 2].getVisited()) {
                    unvisited.add(level[x][y - 2]);
                }
            }
            if (y + 2 < width) {
                if (!level[x][y + 2].getVisited()) {
                    unvisited.add(level[x][y + 2]);
                }
            }
            if (x - 2 > -1) {
                if (!level[x - 2][y].getVisited()) {
                    unvisited.add(level[x - 2][y]);
                }
            }
            if (x + 2 < height) {
                if (!level[x + 2][y].getVisited()) {
                    unvisited.add(level[x + 2][y]);
                }
            }
        }

        LinkedList<Cell> unvisited = new LinkedList<>();
        Cell unvis;

        public void generateLevel() {
            Cell currentBrick = level[1][1];
            currentBrick.setVisited();
            stack.add(currentBrick);
            Cell startBrick;
            do {
                unvisited.clear();
                neighboursUnvisited(currentBrick.getX(), currentBrick.getY());
                if (!unvisited.isEmpty()) {
                    stack.add(currentBrick);
                    startBrick = currentBrick;
                    currentBrick = unvisited.get((int) (Math.random() * unvisited.size()));
                    stack.add(currentBrick);
                    currentBrick.setVisited();
                    removeWall(startBrick, currentBrick);
                } else {
                    if (!stack.isEmpty()) {
                        currentBrick = stack.pop();
                    } else {
                        currentBrick = unvis;
                    }
                }
            } while (unvisitedExist());
            boolean wall;
            for (int i = 0; i < height; i++) {
                wall = false;
                for (int j = 0; j < width; j++) {
                    if (i + 1 < height) {
                        if (level[i][j].getWall() == 1 && level[i + 1][j].getWall() == 0) {
                            wall = true;
                        }
                    }
                }
                levelLine.add(level[i]);
                if (wall) {
                    levelLine.add(level[i]);
                    levelLine.add(level[i]);
                }
            }
        }

        public void removeWall(Cell start, Cell current) {
            int offsetX = (start.getX() - current.getX()) >> 1;
            int offsetY = (start.getY() - current.getY()) >> 1;
            level[current.getX() + offsetX][current.getY() + offsetY].setWall(0);
        }

        public int[] getCodeLevel() {
            StringBuilder[][] str = new StringBuilder[levelLine.size()][levelLine.get(0).length];
            int[] code = new int[levelLine.size() * levelLine.get(0).length];
            for (int i = 0; i < levelLine.size(); i++) {
                for (int j = 0; j < levelLine.get(0).length; j++) {
                    str[i][j] = new StringBuilder();
                    if (i - 1 > -1) {
                        if (j - 1 > -1) {
                            str[i][j].insert(str[i][j].length(), levelLine.get(i - 1)[j - 1].getWall());
                        } else {
                            str[i][j].insert(str[i][j].length(), "1");
                        }
                        str[i][j].insert(str[i][j].length(), levelLine.get(i - 1)[j].getWall());
                        if (j + 1 < levelLine.get(0).length) {
                            str[i][j].insert(str[i][j].length(), levelLine.get(i - 1)[j + 1].getWall());
                        } else {
                            str[i][j].insert(str[i][j].length(), "1");
                        }
                    } else {
                        str[i][j].insert(str[i][j].length(), "111");
                    }
                    if (j - 1 > -1) {
                        str[i][j].insert(str[i][j].length(), levelLine.get(i)[j - 1].getWall());
                    } else {
                        str[i][j].insert(str[i][j].length(), "1");
                    }
                    str[i][j].insert(str[i][j].length(), levelLine.get(i)[j].getWall());
                    if (j + 1 < levelLine.get(0).length) {
                        str[i][j].insert(str[i][j].length(), levelLine.get(i)[j + 1].getWall());
                    } else {
                        str[i][j].insert(str[i][j].length(), "1");
                    }
                    if (i + 1 < levelLine.size()) {
                        if (j - 1 > -1) {
                            str[i][j].insert(str[i][j].length(), levelLine.get(i + 1)[j - 1].getWall());
                        } else {
                            str[i][j].insert(str[i][j].length(), "1");
                        }
                        str[i][j].insert(str[i][j].length(), levelLine.get(i + 1)[j].getWall());
                        if (j + 1 < levelLine.get(0).length) {
                            str[i][j].insert(str[i][j].length(), levelLine.get(i + 1)[j + 1].getWall());
                        } else {
                            str[i][j].insert(str[i][j].length(), "1");
                        }
                    } else {
                        str[i][j].insert(str[i][j].length(), "111");
                    }
                }
            }
            for (int i = levelLine.size() - 1; i >= 0; i--) {
                for (int j = levelLine.get(0).length - 1; j >= 0; j--) {
                    if (str[i][j].charAt(4) == '1') {
                        if (i == levelLine.size() - 1) {
                            str[i][j].insert(str[i][j].length(), "111");
                        } else {
                            if (i * width + j + width < code.length) {
                                if ((code[i * width + j + width] & 0b000000000111) == 0) {
                                    str[i][j].insert(str[i][j].length(), "001");
                                } else {
                                    if ((code[i * width + j + width] & 0b000000000111) == 1) {
                                        str[i][j].insert(str[i][j].length(), "010");
                                    } else {
                                        if ((code[i * width + j + width] & 0b000000000111) == 2) {
                                            str[i][j].insert(str[i][j].length(), "011");
                                        } else {
                                            if ((code[i * width + j + width] & 0b000000000111) == 3 || (code[i * width + j + width] & 0b000000000111) == 7) {
                                                str[i][j].insert(str[i][j].length(), "111");
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        str[i][j].insert(str[i][j].length(), "000");
                    }
                    if (str[i][j].toString().endsWith("000")) {
                        if (str[i][j].charAt(1) == '1' && str[i][j].charAt(7) == '1' && str[i][j].toString().regionMatches(3, "000", 0, 3)) {
                            str[i][j] = new StringBuilder("111000111000");
                        } else {
                            if (str[i][j].charAt(1) == '0' && str[i][j].charAt(7) == '0' && str[i][j].toString().regionMatches(3, "101", 0, 3)) {
                                str[i][j] = new StringBuilder("101101101000");
                            }
                        }
                    }
                    if (str[i][j].toString().endsWith("001")) {
                        if (str[i][j].charAt(1) == '1' && str[i][j].charAt(7) == '0' && str[i][j].toString().regionMatches(3, "111", 0, 3)) {
                            str[i][j] = new StringBuilder("111111000001");
                        }
                    }
                    code[i * width + j] = Integer.parseInt(str[i][j].toString(), 2);
                }
            }
            for (int i = 0; i < levelLine.size(); i++) {
                for (int j = 0; j < levelLine.get(0).length; j++) {
                    StringBuilder a = new StringBuilder("");
                    if (Integer.toBinaryString(code[i * width + j]).endsWith("111") || Integer.toBinaryString(code[i * width + j]).endsWith("011")) {
                        if (i - 1 > -1) {
                            if (j - 1 > -1) {
                                if (Integer.toBinaryString(code[(i - 1) * width + j - 1]).endsWith("111") || Integer.toBinaryString(code[(i - 1) * width + j - 1]).endsWith("011"))
                                    a.insert(a.length(), levelLine.get(i - 1)[j - 1].getWall());
                                else
                                    a.insert(a.length(), "0");
                            } else {
                                a.insert(a.length(), "1");
                            }
                            if (Integer.toBinaryString(code[(i - 1) * width + j]).endsWith("111") || Integer.toBinaryString(code[(i - 1) * width + j]).endsWith("011"))
                                a.insert(a.length(), levelLine.get(i - 1)[j].getWall());
                            else
                                a.insert(a.length(), "0");
                            if (j + 1 < levelLine.get(0).length) {
                                if (Integer.toBinaryString(code[(i - 1) * width + j + 1]).endsWith("111") || Integer.toBinaryString(code[(i - 1) * width + j + 1]).endsWith("011"))
                                    a.insert(a.length(), levelLine.get(i - 1)[j + 1].getWall());
                                else
                                    a.insert(a.length(), "0");
                            } else {
                                a.insert(a.length(), "1");
                            }
                        } else {
                            a.insert(a.length(), "111");
                        }
                        if (j - 1 > -1) {
                            if (Integer.toBinaryString(code[i * width + j - 1]).endsWith("111") || Integer.toBinaryString(code[i * width + j - 1]).endsWith("011"))
                                a.insert(a.length(), levelLine.get(i)[j - 1].getWall());
                            else
                                a.insert(a.length(), "0");
                        } else {
                            a.insert(a.length(), "1");
                        }
                        if (Integer.toBinaryString(code[i * width + j]).endsWith("111") || Integer.toBinaryString(code[i * width + j]).endsWith("011"))
                            a.insert(a.length(), levelLine.get(i)[j].getWall());
                        else
                            a.insert(a.length(), "0");
                        if (j + 1 < levelLine.get(0).length) {
                            if (Integer.toBinaryString(code[i * width + j + 1]).endsWith("111") || Integer.toBinaryString(code[i * width + j + 1]).endsWith("011"))
                                a.insert(a.length(), levelLine.get(i)[j + 1].getWall());
                            else
                                a.insert(a.length(), "0");
                        } else {
                            a.insert(a.length(), "1");
                        }
                        if (i + 1 < levelLine.size()) {
                            if (j - 1 > -1) {
                                if (Integer.toBinaryString(code[(i + 1) * width + j - 1]).endsWith("111") || Integer.toBinaryString(code[(i + 1) * width + j - 1]).endsWith("011"))
                                    a.insert(a.length(), levelLine.get(i + 1)[j - 1].getWall());
                                else
                                    a.insert(a.length(), "0");
                            } else {
                                a.insert(a.length(), "1");
                            }
                            if (Integer.toBinaryString(code[(i + 1) * width + j]).endsWith("111") || Integer.toBinaryString(code[(i + 1) * width + j]).endsWith("011"))
                                a.insert(a.length(), levelLine.get(i + 1)[j].getWall());
                            else
                                a.insert(a.length(), "0");
                            if (j + 1 < levelLine.get(0).length) {
                                if (Integer.toBinaryString(code[(i + 1) * width + j + 1]).endsWith("111") || Integer.toBinaryString(code[(i + 1) * width + j + 1]).endsWith("011"))
                                    a.insert(a.length(), levelLine.get(i + 1)[j + 1].getWall());
                                else
                                    a.insert(a.length(), "0");
                            } else {
                                a.insert(a.length(), "1");
                            }
                        } else {
                            a.insert(a.length(), "111");
                        }
                        if (Integer.toBinaryString(code[i * width + j]).endsWith("111")) {
                            a.insert(a.length(), "111");
                            String s = a.toString();
                            if (s.regionMatches(3, "010", 0, 3) && s.charAt(1) == '1' && s.charAt(7) == '1') {
                                a = new StringBuilder("010010010111");
                            }
                            if (s.regionMatches(3, "110", 0, 3) && s.regionMatches(0, "11", 0, 2) && s.regionMatches(6, "11", 0, 2)) {
                                a = new StringBuilder("110110110111");
                            }
                            if (s.regionMatches(3, "111", 0, 3) && s.charAt(1) == '0' && s.regionMatches(6, "111", 0, 3)) {
                                a = new StringBuilder("000111111111");
                            }
                            if (s.regionMatches(3, "011", 0, 3) && s.regionMatches(1, "11", 0, 2) && s.regionMatches(7, "11", 0, 2)) {
                                a = new StringBuilder("011011011111");
                            }

                        }
                        if (Integer.toBinaryString(code[i * width + j]).endsWith("011")) {
                            a.insert(a.length(), "011");
                            String s = a.toString();
                            if (s.charAt(1) == '0' && s.regionMatches(3, "111", 0, 3) && s.charAt(7) == '0') {
                                a = new StringBuilder("000111000011");
                            }
                            if (s.regionMatches(0, "111111", 0, 6)) {
                                a = new StringBuilder("111111000011");
                            }
                        }
                        code[i * width + j] = Integer.parseInt(a.toString(), 2);
                    }
                }
            }
            for (int i = levelLine.size() - 3; i > levelLine.size() - 4; i--) {
                for (int j = levelLine.get(0).length - 1; j >= 0; j--) {
                    if (Integer.toOctalString(code[i * width + j]).equals("7701")) {
                        code[i * width + j] = 066660;
                        code[(i - 1) * width + j] = 066667;
                        break;
                    }
                }
            }
            for (int i = 2; i < 3; i++) {
                for (int j = 1; j < levelLine.get(0).length; j++) {
                    if (Integer.toOctalString(code[i * width + j]).equals("7701")) {
                        code[i * width + j] = 077770;
                        code[(i - 1) * width + j] = 077777;
                        break;
                    }
                }
            }
            try {
                finalize();
            } catch (Throwable throwable) {
                Log.e("Error", throwable.toString());
            }
            return code;
        }
    }

    public class Cell {

        private int x;
        private int y;
        private int isWall;
        private boolean isVisited;

        public Cell(int x, int y, int wall) {
            this.x = x;
            this.y = y;
            this.isWall = wall;
            if (wall == 1) {
                isVisited = true;
            } else {
                isVisited = false;
            }
        }

        public void setVisited() {
            isVisited = true;
        }

        public boolean getVisited() {
            return isVisited;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getWall() {
            return isWall;
        }

        public void setWall(int b) {
            isWall = b;
            isVisited = true;
        }
    }
}

