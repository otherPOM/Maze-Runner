package maze;

import java.util.Scanner;

public class Main {
    private static final Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Please, enter the size of maze");
        var height = scan.nextInt();
        var width = scan.nextInt();

        var maze = new Maze(height, width);
        maze.generate();
        maze.print();
    }
}
