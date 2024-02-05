package gl.tictactoe.matrix;

/**
 * Created by Роман on 30.05.2017.
 */

public class Cell {

    private char cell = ' ';
    private boolean empty;

    public Cell() {
        empty = true;
    }

    public void setCell(char c) {
        if (c == ' ') {
            empty = true;
        } else {
            cell = c;
            empty = false;
        }
    }

    public char getCell() {
        return cell;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(boolean b){
        empty = b;
    }

    public static Cell[][] copyOf(Cell[][]cells){
        Cell[][]copyCell = new Cell[cells.length][cells.length];
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells.length; j++) {
                copyCell[i][j] = new Cell();
                copyCell[i][j].setCell(cells[i][j].getCell());
            }
        }
        return copyCell;
    }
}

