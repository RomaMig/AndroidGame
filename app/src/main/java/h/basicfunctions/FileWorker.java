package h.basicfunctions;

/**
 * Created by Роман on 30.05.2017.
 */

import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import roma.illusionofdugeon.Game;
import roma.illusionofdugeon.GlobalVariables;

public class FileWorker {

    private static StringBuilder builder;
    private BufferedReader reader;
    private InputStreamReader isr;
    private InputStream inputStream;
    private OutputStream outputStream;
    private OutputStreamWriter osw;
    private static String line;
    private Game game;

    /**
     * Создание файла
     * @param fileName
     */
    public void createFile(String fileName){
        String filePath = GlobalVariables.context.getFilesDir() +"/"+ fileName;

        File file = new File(filePath);
        try {
            if (file.createNewFile()) {
                Log.i("File", "file "+filePath + " is created");
            } else {
                Log.i("File", "file " + filePath + " already exists");
            }
        }catch (Exception e){
            Toast.makeText(GlobalVariables.context,
                    "Exception: " + e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Чтение файла
     * @param fileName
     * @return текст файла
     */
    public String openFile(String fileName) {
        try {
            inputStream = GlobalVariables.context.openFileInput(fileName);

            if (inputStream != null) {
                isr = new InputStreamReader(inputStream);
                reader = new BufferedReader(isr);
                builder = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    builder.append(line + "\n");
                }

                inputStream.close();
                reader.close();
                return builder.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

    /**
     * Сохраняет message в файл
     * @param fileName
     * @param msg
     */
    public void saveFile(String fileName, String msg) {
        try {
            outputStream = GlobalVariables.context.openFileOutput(fileName, 0);
            osw = new OutputStreamWriter(outputStream);
            osw.write(msg);
            osw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Читает файлы с asset'ов
     * @param fileName
     * @return
     */
    public String readFromAssets(String fileName) {
        try {
            isr = new InputStreamReader(GlobalVariables.context.getAssets().open(fileName));
            if (isr != null) {
                reader = new BufferedReader(isr);
                builder = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    builder.append(line + "\n");
                }
                reader.close();
                return builder.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

