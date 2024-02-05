package h.basicfunctions;

/**
 * Created by Роман on 30.05.2017.
 */

import android.graphics.Bitmap;

public class Font {
    private static String chars =""+"ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!?$%-.,()";
    private static Bitmap []imageText;
    public static Bitmap font;

    public static Bitmap[] getImageText(String text, int scale){
        imageText = new Bitmap[text.length()];
        for (int i = 0; i<text.length(); i++) {
            int charIndex = chars.indexOf(text.charAt(i));
            imageText[i] = Bitmap.createBitmap(font, charIndex * font.getWidth()/45, 0, font.getWidth()/45, font.getHeight());
        }
        return imageText;
    }
}

