package gl.tictactoe.matrix;

import android.content.Context;
import android.widget.Button;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import gl.tictactoe.checker.Checker;

/**
 * Created by Роман on 30.05.2017.
 */

public class Matrix {

    private Cell[][] cells = new Cell[6][6];
    private char[] c = {'X', 'O'};
    private int count;
    private boolean winnerExist;
    private Checker checker;

    public Matrix(Context context) {
        checker = new Checker(this, context);

        count = 0;
        winnerExist = false;
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells.length; j++) {
                cells[i][j] = new Cell();
            }
        }
    }

    public boolean setCell(int x, int y) {
        if (x >= 0 && x < cells.length && y >= 0 && y < cells.length) {
            if (cells[y][x].isEmpty()) {
                cells[y][x].setCell(c[count % 2]);
                winnerExist = checker.chekWin("" + c[count % 2]);
                count++;
                return true;
            }
        }
        return false;
    }

    public Move moveOpponent(int moves) {
        Move move = null;
        if (moves < 1) {
            int i, j;
            do {
                i = (int) (Math.random() * cells.length);
                j = (int) (Math.random() * cells.length);
            } while (!cells[i][j].isEmpty());
            checker.chekMax('O', j, i);
            setCell(j, i);
            move = new Move(j, i);
        } else {
            do {
                move = successMove(cells);
            } while (!setCell(move.getX(), move.getY()));
        }
        return move;
    }

    public Move successMove(Cell[][] cells) {
        HashMap<Move, MaxMove> moves = new HashMap<>();
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells.length; j++) {
                if (cells[i][j].isEmpty()) {
                    int price1 = checker.chekMax(c[0], j, i);
                    int price2 = checker.chekMax(c[1], j, i);

                    MaxMove m = new MaxMove(price1 > price2 ? price1 : price2, price1 > price2 ? 0 : 1);
                    m.isWinning = checker.win;

                    moves.put(new Move(j, i), m);
                }
            }
        }
        return getMaxMove(moves);
    }

    public Move getMaxMove(HashMap<Move, MaxMove> map) {
        int max = -100;
        int maxOp = max;
        Move m = null;
        LinkedList<Move> moves = new LinkedList<>();
        for (Map.Entry<Move, MaxMove> entry : map.entrySet()) {
            if (entry.getValue().isWinning && entry.getValue().whoMove == 1){
                return entry.getKey();
            }
            if (max <= entry.getValue().price) {
                max = entry.getValue().price;
                if (entry.getValue().whoMove == 1) {
                    m = entry.getKey();
                    maxOp = max;
                } else {
                    if (maxOp < max) {
                        moves.add(entry.getKey());
                        m = null;
                    }
                }
            }
        }
        return m != null ? m : moves.getLast();
    }

    private class MaxMove {

        int price;
        int whoMove;
        boolean isWinning;

        public MaxMove(int price, int whoMove) {
            this.price = price;
            this.whoMove = whoMove;
        }

        public String toString() {
            return price + " " + whoMove;
        }
    }

    public class Move {

        private int x, y;

        public Move(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public void print() {
            System.out.println("x: " + x + ", y: " + y);
        }

        public String toString() {
            return x + " " + y;
        }
    }

    public boolean restart(Button[][] buttons) {
        count = 0;
        winnerExist = false;
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells.length; j++) {
                cells[i][j] = new Cell();
                buttons[i][j].setText("");
            }
        }
        return true;
    }

    public boolean isEmpty() {
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells.length; j++) {
                if (cells[i][j].isEmpty()) {
                    return true;
                }
            }
        }
        return false;
    }

    public int countEmpty(Cell[][] cells) {
        int count = 0;
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells.length; j++) {
                if (cells[i][j].isEmpty()) {
                    count++;
                }
            }
        }
        return count;
    }

    public void printMatrix() {
        System.out.println("+-+-+-+-+-+-+");
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells.length; j++) {
                System.out.print("|" + cells[i][j].getCell());
            }
            System.out.println("|\n+-+-+-+-+-+-+");
        }
    }

    public static void printMatrix(Cell[][] cells, int offset) {
        String offsetStr = "";
        for (int i = 0; i < offset; i++) {
            offsetStr += "  ";
        }
        System.out.println(offsetStr + "+-+-+-+-+-+-+");
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells.length; j++) {
                if (j == 0) {
                    System.out.print(offsetStr);
                }
                System.out.print("|" + cells[i][j].getCell());
            }
            System.out.println("|\n" + offsetStr + "+-+-+-+-+-+-+");
        }
    }

    public Cell[][] getCells() {
        return cells;
    }

    public boolean getWinnerExist() {
        return winnerExist;
    }
}

