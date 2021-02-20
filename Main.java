package maze;

import java.util.Scanner;

public class Main {
    private static final Scanner scan = new Scanner(System.in);
    private static final String SHORT_MENU = "=== Menu ===\n" +
            "1. Generate a new maze\n" +
            "2. Load a maze\n" +
            "0. Exit";
    private static final String LONG_MENU = "=== Menu ===\n" +
            "1. Generate a new maze\n" +
            "2. Load a maze\n" +
            "3. Save the maze\n" +
            "4. Display the maze\n" +
            "5. Find the escape\n" +
            "0. Exit";

    private static Maze currentMaze;

    public static void main(String[] args) {
        while (true) {
            System.out.println(currentMaze == null ? SHORT_MENU : LONG_MENU);
            var choice = scan.nextLine();
            switch (choice) {
                case "1":
                    String sizeInput;
                    do {
                        System.out.println("Enter the size of a new maze");
                    } while (!isValid(sizeInput = scan.nextLine().strip()));

                    var size = parseSize(sizeInput);
                    currentMaze = Maze.generate(size[0], size[1]);
                    currentMaze.print();
                    break;
                case "2":
                    System.out.println("Enter file to load a maze from");
                    currentMaze = Maze.deserialize(scan.nextLine());
                    break;
                case "3":
                    if (currentMaze != null) {
                        System.out.println("Enter file to save a maze to");
                        currentMaze.serialize(scan.nextLine());
                        break;
                    }
                case "4":
                    if (currentMaze != null) {
                        currentMaze.print();
                        break;
                    }
                case "5":
                    if (currentMaze != null) {
                        currentMaze.escape();
                        break;
                    }
                case "0":
                    return;
                default:
                    System.out.println("Incorrect option. Please try again");
            }
        }
    }

    private static int[] parseSize(String line) {
        var x = line.split("\\s+");
        if (x.length == 1) {
            return new int[]{Integer.parseInt(x[0]), Integer.parseInt(x[0])};
        } else {
            return new int[]{Integer.parseInt(x[0]), Integer.parseInt(x[1])};
        }
    }

    private static boolean isValid(String size) {
        return size.matches("[0-9]+")
                || size.matches("[0-9]+\\s+[0-9]+");
    }
}
