package roma.illusionofdugeon;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

import roma.illusionofdugeon.R;
import gl.tictactoe.matrix.Matrix;

/**
 * Created by Роман on 07.05.2017.
 */

public class TicTacToeActivity extends AppCompatActivity {

    private TableLayout tablelayout;
    public Matrix matrix = new Matrix(this);
    public int moves = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tictactoe_activity);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        tablelayout = (TableLayout) findViewById(R.id.tictactoe_activity);
        buildGameField();
    }

    @Override
    protected void onDestroy() {
        GameScreen.gameCondition("play");
        super.onDestroy();
    }

    private Button[][] buttons = new Button[6][6];

    private void buildGameField() {
        DisplayMetrics dm = new DisplayMetrics();
        for (int i = 0; i < buttons.length; i++) {
            TableRow row = new TableRow(this); // создание строки таблицы
            for (int j = 0; j < buttons.length; j++) {
                Button button = new Button(this);
                buttons[i][j] = button;
                button.setOnClickListener(new Listener(i, j)); // установка слушателя, реагирующего на клик по кнопке
                row.addView(button, new TableRow.LayoutParams(70,
                        TableRow.LayoutParams.WRAP_CONTENT)); // добавление кнопки в строку таблицы
                button.setWidth((int)(dm.widthPixels / 7));
                button.setHeight(button.getWidth());
                button.setTextSize(20);
                button.setBackground(getDrawable(R.drawable.selector));
            }
            tablelayout.addView(row, new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,
                    TableLayout.LayoutParams.WRAP_CONTENT)); // добавление строки в таблицу
        }
    }

    public void restart() {
        matrix.restart(buttons);
        moves = 0;
    }

    public class Listener implements View.OnClickListener {
        private int x = 0;
        private int y = 0;

        public Listener(int y, int x) {
            this.x = x;
            this.y = y;
        }

        public void onClick(View view) {
            if (matrix.isEmpty() && !matrix.getWinnerExist()) {
                if (matrix.setCell(x, y)) {
                    buttons[y][x].setText("" + matrix.getCells()[y][x].getCell());

                    if (matrix.getWinnerExist()) {
                        return;
                    }

                    Matrix.Move move;
                    move = matrix.moveOpponent(++moves);

                    buttons[move.getY()][move.getX()].setText("" + matrix.getCells()[move.getY()][move.getX()].getCell());
                }
            } else {
                restart();
            }
        }
    }
}
