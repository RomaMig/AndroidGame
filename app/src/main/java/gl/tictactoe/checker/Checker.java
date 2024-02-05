package gl.tictactoe.checker;

import android.content.Context;
import android.widget.Toast;

import gl.tictactoe.matrix.Cell;
import gl.tictactoe.matrix.Matrix;

/**
 * Created by Роман on 30.05.2017.
 */

public class Checker {

    private Cell[][] cells;
    private Context mContext;

    public Checker(Matrix matrix, Context context) {
        mContext = context;
        cells = matrix.getCells();
    }

    public boolean chekWin(String player) {
        if (chekWinHorizontal(player) || chekWinVertical(player) || chekWinLeftDiagonal(player)
                || chekWinRightDiagonal(player)) {
            Toast.makeText(mContext, "player " + player + " win!", Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }

    private Cell[][] cellsClone;

    public int chekMax(char player, int x, int y) {
        cellsClone = Cell.copyOf(cells);
        cellsClone[y][x].setCell(player);
        win = false;
        return Math.max(Math.max(countHorizontal(player, x, y), countVertical(player, x, y)),
                Math.max(countRightDiagonal(player, x, y), countLeftDiagonal(player, x, y)));
    }

    private boolean chekWinHorizontal(String player) {
        String p = "";
        p += player + player + player + player;
        String str;
        for (int i = 0; i < cells.length; i++) {
            str = "";
            for (int j = 0; j < cells.length; j++) {
                str += cells[i][j].getCell();
            }
            if (str.contains(p)) {
                return true;
            }
        }
        return false;
    }

    private boolean chekWinVertical(String player) {
        String p = "";
        p += player + player + player + player;
        String str;
        for (int i = 0; i < cells.length; i++) {
            str = "";
            for (int j = 0; j < cells.length; j++) {
                str += cells[j][i].getCell();
            }
            if (str.contains(p)) {
                return true;
            }
        }
        return false;
    }

    private boolean chekWinLeftDiagonal(String player) {
        String p = "";
        p += player + player + player + player;
        String str;
        for (int i = 0; i < cells.length - 3; i++) {
            for (int j = 0; j < cells.length - 3; j++) {
                str = "";
                int m = j;
                for (int k = i; k < i + 4; k++) {
                    str += cells[k][m].getCell();
                    m++;
                }
                if (str.contains(p)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean chekWinRightDiagonal(String player) {
        String p = "";
        p += player + player + player + player;
        String str;
        for (int i = 0; i < cells.length - 3; i++) {
            for (int j = cells.length - 1; j > 2; j--) {
                str = "";
                int m = j;
                for (int k = i; k < i + 4; k++) {
                    str += cells[k][m].getCell();
                    m--;
                }
                if (str.contains(p)) {
                    return true;
                }
            }
        }
        return false;
    }

    private int countHorizontal(char player, int x, int y) {
        String str = "";
        int end = x + 4;
        int start = x - 3;
        for (; start < end && start < cellsClone.length; start++) {
            if (start >= 0) {
                str += cellsClone[y][start].getCell();
            }
        }
        return countLine(str, x, player);
    }

    private int countVertical(char player, int x, int y) {
        String str = "";
        int end = y + 4;
        int start = y - 3;
        for (; start < end && start < cellsClone.length; start++) {
            if (start >= 0) {
                str += cellsClone[start][x].getCell();
            }
        }
        return countLine(str, y, player);
    }

    private int countLeftDiagonal(char player, int x, int y) {
        String str = "";
        int endX = x + 4;
        int startX = x - 3;
        int endY = y + 4;
        int startY = y - 3;
        for (int i = 0; i < 7 && startX < endX && startY < endY && startX < cellsClone.length
                && startY < cellsClone.length; i++, startX++, startY++) {
            if (startX >= 0 && startY >= 0) {
                str += cellsClone[startY][startX].getCell();
            }
        }
        return countDiagonal(str, x, y, player);
    }

    private int countRightDiagonal(char player, int x, int y) {
        String str = "";
        int endX = x - 4;
        int startX = x + 3;
        int endY = y + 4;
        int startY = y - 3;
        for (int i = 0; i < 7 && startX >= 0 && startY < endY; i++, startX--, startY++) {
            if (startX > endX && startX < cellsClone.length && startY >= 0 && startY < cellsClone.length) {
                str += cellsClone[startY][startX].getCell();
            }
        }
        return countDiagonal(str, x, y, player);
    }

    public boolean win = false;

    private int countLine(String str, int n, char player) {
        int count = 0;
        int w = 0;
        if (player == 'O' && str.contains("OOOO")) {
            win = true;
        }
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == player) {
                count += 4;
                w++;
            } else {
                if (i < (n > 3 ? 3 : n) && str.charAt(i) != ' ') {
                    count = 0;
                    if (str.length() - i < 4) {
                        return -1;
                    }
                } else {
                    if (i > (n > 3 ? 3 : n)) {
                        if (str.charAt(i) == ' ') {
                            count++;
                        } else {
                            return (i > 3) ? count : -1;
                        }
                    } else {
                        if (str.charAt(i) == ' ') {
                            count++;
                        }
                    }
                }
            }
        }
        if (w > 3) win = true;
        return count;
    }

    private int countDiagonal(String str, int x, int y, char player) {
        int count = 0;
        int w = 0;
        if (player == 'O' && str.contains("OOOO")) {
            win = true;
        }
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == player) {
                count += 4;
                w++;
            } else {
                if (i < (y > 3 ? 3 : y) && i > (x < 4 ? 3 : x) && str.charAt(i) != ' ') {
                    count = 0;
                    if (str.length() - i < 4) {
                        return -1;
                    }
                } else {
                    if (i > (y > 3 ? 3 : y) && i < (x < 4 ? 3 : x)) {
                        if (str.charAt(i) == ' ') {
                            count++;
                        } else {
                            return (i > 3) ? count : -1;
                        }
                    } else {
                        if (str.charAt(i) == ' ') {
                            count++;
                        }
                    }
                }
            }
        }
        if (w > 3) win = true;
        return count;
    }

    public void setCell(Cell[][] cells) {
        this.cells = cells;
    }
}
