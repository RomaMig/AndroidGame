package h.basicfunctions;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import java.io.IOException;

import roma.illusionofdugeon.GlobalVariables;

/**
 * Created by Роман on 22.02.2017.
 */

public class Sounds {

    private static final String[] soundsName = {
            "enemy_attak_rat.ogg", "block.ogg",             //0
            "menu_select.mp3", "hero_attak1.wav",           //2
            "hero_attak2.wav", "hero_attak3.wav",           //4
            "hero_attak4.wav", "hero_attak5.wav",           //6
            "movement.ogg", "movement.ogg",                 //8
            "Footstep_Dirt_00.mp3", "Footstep_Dirt_01.mp3", //10
            "Footstep_Dirt_02.mp3", "Footstep_Dirt_03.mp3", //12
            "Footstep_Dirt_04.mp3", "Footstep_Dirt_05.mp3", //14
            "Footstep_Dirt_06.mp3", "Footstep_Dirt_07.mp3", //16
            "Footstep_Dirt_08.mp3", "Footstep_Dirt_09.mp3", //18
            "hit00.flac", "hit01.flac",                     //20
            "hit02.flac", "hit03.wav",                      //22
            "hit04.wav", "hit05.wav",                       //24
            "hit06.wav", "hit07.wav",                       //26
            "hit08.wav", "Jingle_Achievement.mp3",          //28
            "Jingle_Lose.mp3", "Jingle_Win.mp3",            //30
    };
    public static int []sounds = new int[soundsName.length]; //массив с id аудио
    public static SoundPool soundPool;

    /**
     * Декодирует аудиофайлы
     *
     * @throws java.io.FileNotFoundException
     */
    public static void loadSoundFiles(){
        soundPool = new SoundPool(15, AudioManager.STREAM_MUSIC, 0);
        try {
            AssetManager assetManager = GlobalVariables.context.getAssets();
            AssetFileDescriptor descriptor;

            for (int i = 0; i<sounds.length; i++) {
                sounds[i] = -1;
                descriptor = assetManager.openFd(soundsName[i]);
                sounds[i] = soundPool.load(descriptor, 0);
            }

        } catch (IOException e) {
            Log.e("Error", e.toString());
        }
    }
}
