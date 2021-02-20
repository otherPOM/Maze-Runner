package maze;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Maze implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Cell[][] cells;
    private final Cell[] exits;

    private Maze(int height, int width) {
        cells = new Cell[height][width];
        exits = new Cell[2];
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[0].length; j++) {
                cells[i][j] = new Cell(i, j);
            }
        }
    }

    public static Maze deserialize(String fileName) {
        try (var is = new ObjectInputStream(
                new BufferedInputStream(
                        Files.newInputStream(Path.of(fileName))))) {
            return (Maze) is.readObject();
        } catch (IOException e) {
            System.out.println("The file " + fileName + " does not exist");
        } catch (ClassNotFoundException e) {
            System.out.println("Cannot load the maze. It has an invalid format");
        }
        return null;
    }

    public static Maze generate(int height, int width) {
        var rand = new Random();
        var maze = new Maze(height, width);
        var cells = maze.cells;
        cells[1][1].makePassage();
        var list = new ArrayList<>(cells[1][1].getNeighboringWalls());
        while (!list.isEmpty()) {
            var randomNeighbor = list.get(rand.nextInt(list.size()));
            if (randomNeighbor.isGood()) {
                randomNeighbor.makePassage();
                list.addAll(randomNeighbor.getNeighboringWalls());
            }
            list.remove(randomNeighbor);
        }
        while (true) {
            var i = rand.nextInt(cells.length);
            if (!cells[i][1].isWall) {
                cells[i][0].makePassage();
                maze.exits[0] = cells[i][0];
                break;
            }
        }
        while (true) {
            var i = rand.nextInt(cells.length);
            if (!cells[i][cells[0].length - 2].isWall) {
                cells[i][cells[0].length - 1].makePassage();
                maze.exits[1] = cells[i][cells[0].length - 1];
                break;
            }
        }
        return maze;
    }

    public void serialize(String fileName) {
        try (var os = new ObjectOutputStream(
                new BufferedOutputStream(
                        Files.newOutputStream(Path.of(fileName))))) {
            os.writeObject(this);
        } catch (IOException e) {
            System.out.println("Error occurred while working with file:\n"
                    + fileName);
            e.printStackTrace();
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

    public void escape() {
        var current = new HashSet<Cell>();
        current.add(exits[0]);
        var prev = new HashSet<Cell>();

        int i = 0;
        while (!current.isEmpty()) {
            for (Cell cell : current) {
                cell.distance = i;
            }
            i++;
            var next = current.stream()
                    .flatMap(cell -> cell.getNeighboringCells().stream())
                    .filter(Predicate.not(prev::contains))
                    .collect(Collectors.toSet());
            prev.clear();
            prev.addAll(current);
            current.clear();
            current.addAll(next);
        }

        var path = new HashSet<Cell>();
        var c = exits[1];
        path.add(c);
        while (!c.equals(exits[0])) {
            c = c.getNeighboringCells().stream().min(Comparator.comparingInt(Cell::getDistance)).orElseThrow();
            path.add(c);
        }

        for (Cell[] row : cells) {
            for (Cell cell : row) {
                System.out.print(cell.isWall() ? "\u2588\u2588" : path.contains(cell) ? "//" : "  ");
            }
            System.out.println();
        }
    }

    private class Cell implements Serializable {
        private static final long serialVersionUID = 2L;

        private final int x;
        private final int y;
        private boolean isWall;
        private int distance;

        private Cell(int x, int y) {
            this.x = x;
            this.y = y;
            isWall = true;
            distance = 100_000;
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

        private List<Cell> getNeighboringWalls() {
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

        private List<Cell> getNeighboringCells() {
            var list = new ArrayList<Cell>();
            if (x - 1 >= 0) {
                list.add(cells[x - 1][y]);
            }
            if (y - 1 >= 0) {
                list.add(cells[x][y - 1]);
            }
            if (x + 1 <= cells.length - 1) {
                list.add(cells[x + 1][y]);
            }
            if (y + 1 <= cells[0].length - 1) {
                list.add(cells[x][y + 1]);
            }
            list.removeIf(Cell::isWall);
            return list;
        }

        public boolean isWall() {
            return isWall;
        }

        public int getDistance() {
            return distance;
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
