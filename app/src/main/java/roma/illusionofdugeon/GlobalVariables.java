package roma.illusionofdugeon;

/**
 * Created by Роман on 30.05.2017.
 */

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import m.mapmaker.Bricks;

public class GlobalVariables {
    public static boolean playing;
    public static boolean endlessMode = false;
    public static Context context;
    public static Context appContext;
    public static Resources resources;
    public static boolean loadBricks = false;
    public static int mapNumber;
    public static Map<String, Bitmap[][]> mobsImages = new HashMap<String, Bitmap[][]>();
    public static Map<Integer, Bricks[][]> bricksArray = new HashMap<Integer, Bricks[][]>();
    public static Map<Integer, Bricks[][]> mapsFromMaker = new HashMap<Integer, Bricks[][]>();
    public static ArrayList<Bitmap> images = new ArrayList<>();
}
