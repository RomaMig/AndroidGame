package roma.illusionofdugeon;

import gl.maincomponents.HeroMenu;
import h.basicfunctions.FileWorker;

/**
 * Created by Роман on 30.05.2017.
 */

public class GameSave {

    public static final int NUM_SAVES = 7;
    public int []saves = new int[NUM_SAVES];
    public boolean exist;
    public static boolean sSaved;

    private String []str = new String[NUM_SAVES];
    {
        for (int i = 0; i<str.length; i++){
            str[i] = "";
        }
    }

    public GameSave(String save){
        if (save != null) {
            int j = 0;
            for (int i = 0; i < save.length(); i++) {
                if (save.charAt(i) == '*') {
                    j++;
                } else {
                    if (j < saves.length) {
                        if (save.charAt(i) > 47 && save.charAt(i) < 58) {
                            str[j] += save.charAt(i);
                        }else{
                            if (str[j] == null) {
                                str[j] = "0";
                            }
                        }
                    }
                }
            }
            if (saves.length > 0) {
                try {
                    for (int i = 0; i < saves.length; i++) {
                        saves[i] = Integer.parseInt(str[i]);
                    }
                }catch (Exception e){}
            }
            exist = true;
        } else{
            exist = false;
        }
    }

    public static boolean Save(int i){
        FileWorker fileWorker = new FileWorker();
        fileWorker.saveFile("Save"+i+".txt", ""+Game.gameInterface.getHero().getExperience()+"*"+
                Game.gameInterface.getThisMap()+"*"+Game.gameInterface.getHero().getHearts()+"*"+HeroMenu.perks[0].getPoints()+"*"+
                HeroMenu.perks[1].getPoints()+"*"+HeroMenu.perks[2].getPoints()+"*"+HeroMenu.perks[3].getPoints());
        return true;
    }

    public static void Load(GameSave save){
        Game.gameInterface.getHero().setExperience(save.saves[0]);
        Game.gameInterface.getHero().setLevel(save.saves[0]/100);
        Game.gameInterface.getHero().setHearts(save.saves[2]);
        for (int i = 0; i<HeroMenu.perks.length; i++) {
            HeroMenu.perks[i].setPoints(save.saves[i+3]);
            Game.gameInterface.resume();
        }
        Game.gameInterface.setMap(save.saves[1]);
    }

    public String toString(){
        String s = "";
        for (int i:saves){
            s += i+", ";
        }
        return s;
    }
}

