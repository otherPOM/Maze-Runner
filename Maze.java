package maze;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.function.Predicate;

public class Maze {
    private final Random rand = new Random();

    private final Cell[][] cells;

    public Maze(int height, int width) {
        cells = new Cell[height][width];
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[0].length; j++) {
                cells[i][j] = new Cell(i, j);
            }
        }
    }

    public void generate() {
        cells[1][1].makePassage();
        var list = new ArrayList<>(cells[1][1].getPotentialPassages());
        while (!list.isEmpty()) {
            var randomNeighbor = list.get(rand.nextInt(list.size()));
            if (randomNeighbor.isGood()) {
                randomNeighbor.makePassage();
                list.addAll(randomNeighbor.getPotentialPassages());
            }
            list.remove(randomNeighbor);
        }
        makeEntrances();
    }

    private void makeEntrances() {
        while (true) {
            var i = rand.nextInt(cells.length);
            if (!cells[i][1].isWall) {
                cells[i][0].makePassage();
                break;
            }
        }
        while (true) {
            var i = rand.nextInt(cells.length);
            if (!cells[i][cells[0].length - 2].isWall) {
                cells[i][cells[0].length - 1].makePassage();
                break;
            }
        }
    }

    public void print() {
        for (Cell[] row : cells) {
            for (Cell cell : row) {
                System.out.print(cell.isWall() ? "\u2588\u2588" : "  ");
            }
            System.out.println();
        }
    }


    private class Cell {
        private final int x;
        private final int y;
        private boolean isWall;

        private Cell(int x, int y) {
            this.x = x;
            this.y = y;
            isWall = true;
        }

        private boolean isGood() {
            var list = new ArrayList<Cell>();
            if (x - 1 != 0) {
                list.add(cells[x - 1][y]);
            }
            if (y - 1 != 0) {
                list.add(cells[x][y - 1]);
            }
            if (x + 1 != cells.length - 1) {
                list.add(cells[x + 1][y]);
            }
            if (y + 1 != cells[0].length - 1) {
                list.add(cells[x][y + 1]);
            }
            list.removeIf(Cell::isWall);
            if (list.size() != 1) {
                return false;
            }
            var dn = getDiagonalNeighbors();
            dn.removeIf(diaNei -> diaNei.x == list.get(0).x
            || diaNei.y == list.get(0).y);

            return dn.stream().allMatch(Cell::isWall);
        }

        private List<Cell> getDiagonalNeighbors() {
            var list = new ArrayList<Cell>();
            if (x - 1 != 0 && y - 1 != 0) {
                list.add(cells[x - 1][y - 1]);
            }
            if (x - 1 != 0 && y + 1 != cells[0].length - 1) {
                list.add(cells[x - 1][y + 1]);
            }
            if (x + 1 != cells.length - 1 && y - 1 != 0) {
                list.add(cells[x + 1][y - 1]);
            }
            if (x + 1 != cells.length - 1 && y + 1 != cells[0].length - 1) {
                list.add(cells[x + 1][y + 1]);
            }

            return list;
        }

        private List<Cell> getPotentialPassages() {
            var list = new ArrayList<Cell>();
            if (x - 1 != 0) {
                list.add(cells[x - 1][y]);
            }
            if (y - 1 != 0) {
                list.add(cells[x][y - 1]);
            }
            if (x + 1 != cells.length - 1) {
                list.add(cells[x + 1][y]);
            }
            if (y + 1 != cells[0].length - 1) {
                list.add(cells[x][y + 1]);
            }
            list.removeIf(Predicate.not(Cell::isWall));
            return list;
        }

        public boolean isWall() {
            return isWall;
        }

        private void makePassage() {
            isWall = false;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Cell cell = (Cell) o;
            return x == cell.x && y == cell.y && isWall == cell.isWall;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, isWall);
        }
    }
}
