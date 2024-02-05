package gl.maincomponents;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;

import gl.intefaces.MainInterface;
import h.basicfunctions.ButtonsManager;
import h.basicfunctions.Points;
import roma.illusionofdugeon.Game;
import roma.illusionofdugeon.GameScreen;
import roma.illusionofdugeon.GlobalVariables;
import roma.illusionofdugeon.R;
import roma.illusionofdugeon.TicTacToeActivity;

/**
 * Created by Роман on 30.05.2017.
 */

public class Dialog extends MainInterface {

    private static Bitmap backgroundD = GlobalVariables.images.get(1), hero, drifter;
    public int numPhrase;
    public boolean dialog = false;
    private float xh = GameScreen.WIDTH * 0.75f;
    private Paint p = new Paint();
    public ButtonsManager skip, next;
    public ArrayList<Phrases> phrases = new ArrayList<>();

    {
        p.setColor(Color.WHITE);
        p.setSubpixelText(true);
        p.setTextSize(20 * GameScreen.RATIO_WIDTHS);
    }

    public Dialog(final Game game) {
        dialog = false;
        numPhrase = 0;
        skip = new ButtonsManager(GlobalVariables.images.get(9), (int) (Game.sDisplayMetrics.widthPixels / 1.16f), 10) {
            @Override
            public void onClickListener() {
                Game.gameInterface.setMainInterface(null);
                Game.gameInterface.getDialog().numPhrase = 0;
            }
        };
        next = new ButtonsManager(GlobalVariables.images.get(8), (int) (Game.sDisplayMetrics.widthPixels / 2.29f), (int) (Game.sDisplayMetrics.heightPixels / 1.16f)) {
            @Override
            public void onClickListener() {
                if (phrases.get(numPhrase).phrase.compareTo("Да!") == 0) {
                    Intent intent = new Intent(game, TicTacToeActivity.class);
                    GlobalVariables.context.startActivity(intent);
                }
                if (numPhrase < phrases.size() - 1) {
                    Game.gameInterface.getDialog().numPhrase++;
                }
            }
        };
    }

    public void buildDialog(int numDialog) {
        try {
            phrases.clear();
            XmlPullParser parser = GlobalVariables.context.getResources().getXml(R.xml.dialogs);
            boolean neededDialogExist = false;
            while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                String who = "";
                String emotion = "";
                while (!neededDialogExist) {
                    if (parser.getDepth() == 2 && parser.getAttributeCount() == 1) {
                        if (parser.getAttributeValue(0).compareToIgnoreCase("" + numDialog) == 0) {
                            neededDialogExist = true;
                        }
                    }
                    parser.next();
                }
                if (parser.getDepth() == 3 && parser.getAttributeCount() == 2) {
                    who = parser.getAttributeValue(0);
                    emotion = parser.getAttributeValue(1);
                    parser.next();
                    phrases.add(Game.gameInterface.getDialog().new Phrases(who, emotion, parser.getText()));
                }
                parser.next();
                if (parser.getDepth() == 2 && parser.getAttributeCount() == 1) {
                    if (parser.getAttributeValue(0).compareToIgnoreCase("" + (numDialog + 1)) == 0) {
                        return;
                    }
                }
            }
        } catch (Exception e) {
            Log.e("Error: ", e.toString());
        }
    }

    @Override
    public void actionToFalse(int id) {
        skip.noPressing(id);
        next.noPressing(id);
    }

    @Override
    public void checkGestures(Points[] fingers) {
        for (Points point : fingers) {
            if (point != null) {
                skip.Pressing(point.ID, point.Before.x, point.Before.y);
                next.Pressing(point.ID, point.Before.x, point.Before.y);
            }
        }
    }

    public void update() {
        skip.update();
        next.update();
    }

    public void draw(Canvas canvas) {
        canvas.scale(GameScreen.scaleX, GameScreen.scaleY);
        canvas.drawBitmap(backgroundD, 0, 0, null);
        skip.buttonDraw(canvas);
        next.buttonDraw(canvas);
        if (hero != null) {
            canvas.drawBitmap(hero, xh, 0, null);
        }
        if (drifter != null) {
            canvas.drawBitmap(drifter, 0, 0, null);
        }
        canvas.scale(1/GameScreen.scaleX, 1/GameScreen.scaleY);
        canvas.drawText(phrases.get(numPhrase).who + ": " + phrases.get(numPhrase).phrase, 10 * GameScreen.RATIO_WIDTHS, 100 * GameScreen.RATIO_HEIGHTS, p);
    }

    public class Phrases {

        public String who;
        public String emotion;
        public String phrase;

        public Phrases(String who, String emotion, String phrase) {
            this.who = who;
            this.emotion = emotion;
            this.phrase = phrase;
        }

        @Override
        public String toString() {
            return "Phrases{" +
                    "who='" + who + '\'' +
                    ", emotion='" + emotion + '\'' +
                    ", phrase='" + phrase + '\'' +
                    '}';
        }
    }
}


