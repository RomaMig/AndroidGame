package roma.illusionofdugeon;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * Created by Роман on 30.05.2017.
 */

public class MainThread extends Thread {

    private int FPS = 30;
    private double averageFPS;
    private SurfaceHolder surfaceHolder;
    private GameScreen mGameScreen;
    private boolean running;
    public static Canvas canvas;
    public static long waitTime;

    public MainThread(SurfaceHolder surfaceHolder, GameScreen gameScreen) {
        super();
        this.surfaceHolder = surfaceHolder;
        this.mGameScreen = gameScreen;
    }

    @Override
    public void run() {

        long startTime;
        long timeMillis;
        long totalTime = 0;
        int frameCount = 0;
        long targetTime = 1000 / FPS;

        while (running) {
            startTime = System.nanoTime();
            canvas = null;

            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    this.mGameScreen.update();
                    this.mGameScreen.draw(canvas);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            finally{
                if (canvas!=null){
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            timeMillis = (System.nanoTime() - startTime) / 1000000;
            waitTime = targetTime - timeMillis;

            try {
                this.sleep(waitTime);
            } catch (Exception e) {
            }

            totalTime += System.nanoTime() - startTime;
            frameCount++;
            if (frameCount == FPS) {
                averageFPS = 1000 / ((totalTime / frameCount) / 1000000);
                frameCount = 0;
                totalTime = 0;
                System.out.println(averageFPS);
            }
        }
    }

    public void setRunning(boolean b) {
        running = b;
    }
    public double getFps() {
        return averageFPS;
    }
}

