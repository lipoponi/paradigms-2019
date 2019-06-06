import java.util.Random;

public class BoardConfiguration {
    private CellFilling[][] cells;
    private int boardHeight, boardWidth;
    private int freeCellsCount;
    private int lastPutX = -1, lastPutY = -1;

    public BoardConfiguration() {
        boardHeight = 8;
        boardWidth = 8;
        freeCellsCount = boardHeight * boardWidth;

        cells = new CellFilling[boardHeight][boardWidth];
    }

    public boolean isFilled(Point point) {
        return cells[point.getY()][point.getX()] != null;
    }

    public void put(Point point, CellFilling filling) {
        freeCellsCount--;
        cells[point.getY()][point.getX()] = filling;
        lastPutX = point.getX();
        lastPutY = point.getY();
    }

    public boolean lastPutWasWinning() {
        if (lastPutX == -1 && lastPutY == -1) return false;

        CellFilling good = cells[lastPutY][lastPutX];

        int cnt = 1;
        int p = lastPutX - 1;
        while (0 <= p && cells[lastPutY][p] == good) {
            p--;
            cnt++;
        }
        p = lastPutX + 1;
        while (p < boardWidth && cells[lastPutY][p] == good) {
            p++;
            cnt++;
        }

        if (5 <= cnt) return true;

        cnt = 1;
        p = lastPutY - 1;
        while (0 <= p && cells[p][lastPutX] == good) {
            p--;
            cnt++;
        }
        p = lastPutY + 1;
        while (p < boardWidth && cells[p][lastPutX] == good) {
            p++;
            cnt++;
        }

        if (5 <= cnt) return true;

        return false;
    }

    public int getBoardHeight() {
        return boardHeight;
    }

    public int getBoardWidth() {
        return boardWidth;
    }

    public boolean hasFreeCells() {
        return 0 < freeCellsCount;
    }
}
