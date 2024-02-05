package roma.illusionofdugeon;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import gl.intefaces.MainInterface;
import h.basicfunctions.Points;

/**
 * Created by Роман on 30.05.2017.
 */

public class GameScreen extends SurfaceView implements SurfaceHolder.Callback{

    public static final int WIDTH = 800;
    public static final int HEIGHT = 480;
    public static final float RATIO_WIDTHS = Game.sDisplayMetrics.widthPixels / (WIDTH*1f);
    public static final float RATIO_HEIGHTS = Game.sDisplayMetrics.heightPixels / (HEIGHT*1f);
    public static final float RATIO_DISPLAYS = RATIO_WIDTHS / RATIO_HEIGHTS;
    private MainThread thread;
    private MediaPlayer mPlayer = new MediaPlayer();
    private static MainInterface mainInterface;
    private Points[]fingers = new Points[5];
    public static int []music = {R.raw.music1, R.raw.music3};
    private Paint mPaint = new Paint();
    {
        mPaint.setColor(Color.WHITE);
        mPaint.setSubpixelText(true);
        mPaint.setTextSize(20);
    }

    public GameScreen(Context context) {
        super(context);

        getHolder().addCallback(this);

        setFocusable(true);

        mPlayer = MediaPlayer.create(GlobalVariables.context, R.raw.music1);
        mPlayer.setLooping(true);
        if (Game.musicSound) mPlayer.start();

        GameSave.sSaved = false;
    }
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){}

    @Override
    public void surfaceDestroyed(SurfaceHolder holder){
        mPlayer.stop();
        try {
            mPlayer.prepare();
            mPlayer.seekTo(0);
        }catch (Throwable t) {}
        boolean retry = true;
        while(retry)
        {
            try{thread.setRunning(false);
                thread.join();

            }catch(InterruptedException e){e.printStackTrace();}
            retry = false;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder){
        thread = new MainThread(getHolder(), this);
        if (GlobalVariables.loadBricks) {
            GameScreen.gameCondition("menu");
            thread.setRunning(true);
            thread.start();
            return;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        synchronized (getHolder()) {
            int id = event.getPointerId(event.getActionIndex());
            if (event.getActionMasked() == MotionEvent.ACTION_DOWN || event.getActionMasked() == MotionEvent.ACTION_POINTER_DOWN) {
                fingers[event.getPointerId(event.getActionIndex())] = new Points(id, (int) event.getX(), (int) event.getY());
                for (int n = 0; n < event.getPointerCount(); n++) {
                    fingers[event.getPointerId(n)].setNow((int) event.getX(n), (int) event.getY(n));
                }
                mainInterface.checkGestures(fingers);
                return true;
            }
            if (event.getActionMasked() == MotionEvent.ACTION_UP || event.getActionMasked() == MotionEvent.ACTION_POINTER_UP) {
                mainInterface.actionToFalse(event.getPointerId(event.getActionIndex()));
                fingers[event.getPointerId(event.getActionIndex())] = null;
            }
            if (event.getActionMasked() == MotionEvent.ACTION_MOVE) {
                for (int n = 0; n < event.getPointerCount(); n++) {
                    fingers[event.getPointerId(n)].setNow((int) event.getX(n), (int) event.getY(n));
                }
                mainInterface.checkGestures(fingers);
            }
        }
        return super.onTouchEvent(event);
    }

    public void update() {
        mainInterface.update();
    }

    public static final float scaleX = Game.sDisplayMetrics.widthPixels / (GlobalVariables.images.get(1).getWidth()*1f);
    public static final float scaleY = Game.sDisplayMetrics.heightPixels / (GlobalVariables.images.get(1).getHeight()*1f);

    @Override
    public void draw(Canvas canvas) {
        if(canvas!=null) {
            final int savedState = canvas.save();
            mainInterface.draw(canvas);
            canvas.restoreToCount(savedState);
        }
    }

    public MediaPlayer getPlayer() {
        if (mPlayer == null) {
            mPlayer = MediaPlayer.create(GlobalVariables.context, R.raw.music1);
            mPlayer.setLooping(true);
        }
        return mPlayer;
    }

    public void setPlayer(MediaPlayer player) {
        mPlayer = player;
    }
    public void setMusic(int music){
        mPlayer.stop();
        mPlayer = MediaPlayer.create(GlobalVariables.context, music);
        mPlayer.setLooping(true);
        if (Game.musicSound) {
            mPlayer.start();
        }
    }

    public static void gameCondition(String condition){
        switch (condition) {
            case "menu":
                Game.getGameScreen().setMusic(music[1]);
                mainInterface = Game.menuInterface;
                break;
            case "play":
                Game.getGameScreen().setMusic(music[0]);
                mainInterface = Game.gameInterface;
                break;
            case "mapMaker":
                mainInterface = Game.mapInterface;
                break;
        }
    }
}

