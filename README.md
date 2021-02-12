# Maze Runner https://hyperskill.org/projects/47

## Stage 1
Implement the maze as a two-dimensional array of integers. If the value of an element is 1, it is a wall. If the value is 0, it is a passage.

Your maze must be 10x10. Other rules are as follows:

1. There should be walls around the maze, except for two cells: entrance and exit.
2. Any empty cell should be accessible from the entrance or exit of the maze. It is impossible to move diagonally, only vertically or horizontally.
3. There has to be a path from the entrance to the exit. It doesn't matter what is considered an entrance and what is an exit as they are interchangeable.

## Stage 2
In this stage, you will develop an algorithm for creating a maze.

## Stage 3
The program should provide a menu with five options:

1. Generate a new maze.
2. Load a maze.
3. Save the maze.
4. Display the maze.
5. Exit.

After a maze is generated or loaded from a file, it becomes the current maze that can be saved or displayed.

If there is no current maze (generated or loaded), a user should not see the options save and display the maze.

You must always check the result of processing files and display user-friendly messages.

