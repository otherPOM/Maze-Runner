package maze;

public class Maze {
    private final int[][] matrix;

    public Maze(int[][] matrix) {
        this.matrix = matrix;
    }

    public void print() {
        for (int[] ints : matrix) {
            for (int anInt : ints) {
                System.out.print(anInt == 0 ? "  " : "\u2588\u2588");
            }
            System.out.println();
        }
    }
}
