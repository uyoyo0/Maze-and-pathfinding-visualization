
/**
 * Class to handle all logic needed such as generating or solving a maze
 * @author Uyiosa Iyekekpolor
 * @version 2021-01-10
 */
import java.util.*;

public class MazeLogic {

	/** VARIABLES FOR STEPS IN BUILDING / SOLVING THE MAZE **/
	private ArrayList<CellInfo> path = new ArrayList<CellInfo>();
	private ArrayList<CellInfo> maze = new ArrayList<CellInfo>();
	private boolean pathMade;

	/** VARIABLES FOR SOLVING MAZE **/
	private boolean found = false;
	private int[] dirx = { 0, -1, 1, 0 };
	private int[] diry = { -1, 0, 0, 1 };
	Queue<CellInfo> q = new LinkedList<CellInfo>();

	/** VARIABLES FOR BUILDING MAZE **/
	private boolean[][] wallNotHere = new boolean[35][35];

	/** GENERAL UTILITIES **/
	private CellInfo pointer;
	private Random num = new Random();
	private MazeSaver m = new MazeSaver("savedMazes.txt");

	/**
	 * Method that will find the cell that the user clicked
	 * 
	 * @param maze The maze that you are finding the cell on
	 * @param x    coordinate representing where you clicked
	 * @param y    coordinate representing where you clicked
	 * @return the cell that corresponds to where you clicked
	 */
	public CellInfo findCell(CellInfo[][] maze, int x, int y) {

		for (int i = 0; i < maze.length; i++) {
			for (int j = 0; j < maze[i].length; j++) {

				pointer = maze[i][j];
				/**
				 * Check whether or not the x and y variable the user clicked was inside of a
				 * cell I have to add roughly 60 to their x and y values because thats what
				 * their dimensions are (roughly)
				 */
				if (x > pointer.getX() && x < pointer.getX() + 20 && y > pointer.getY() && y < pointer.getY() + 40) {
					return pointer;
				}
			}
		}
		return null;

	}

	/**
	 * Method that will generate maze using recursive backtracking
	 * 
	 * @param maze: where the maze will be generated
	 * @param row:  row at which you will start generating maze
	 * @param col:  column at which you will start generating maze
	 */
	public void buildMaze(CellInfo[][] maze, int row, int col) {

		int direction;

		for (int i = 0; i < maze.length; i++) {
			direction = num.nextInt(4) + 1;// 1 == north, 2 == east, 3 == south, 4 == west
			switch (direction) {
			case 1: // North
				// Whether 2 cells up is out or not
				if (row - 2 < 0) {
					break;
				} else {
					pointer = maze[row - 2][col];
					if (!(wallNotHere[row - 2][col])) {// if the cell two rows above is a wall
						wallNotHere[row - 2][col] = true;
						wallNotHere[row - 1][col] = true;
						this.maze.add(pointer);
						pointer = maze[row - 1][col];
						this.maze.add(pointer);
						buildMaze(maze, row - 2, col);
					}
				}
				break;
			case 2:// east
					// Whether 2 cells to the right is out or not
				if (col + 2 > maze.length - 1) {
					break;
				} else {
					pointer = maze[row][col + 2];
					if (!(wallNotHere[row][col + 2])) {
						wallNotHere[row][col + 2] = true;
						//
						wallNotHere[row][col + 1] = true;
						this.maze.add(pointer);
						pointer = maze[row][col + 1];
						this.maze.add(pointer);
						buildMaze(maze, row, col + 2);
					}
				}
				break;
			case 3: // south
				// Whether 2 cells down is out or not
				if (row + 2 > maze.length - 1) {
					break;
				} else {
					pointer = maze[row + 2][col];
					if (!(wallNotHere[row + 2][col])) {
						wallNotHere[row + 2][col] = true;
						wallNotHere[row + 1][col] = true;
						this.maze.add(pointer);
						pointer = maze[row + 1][col];
						this.maze.add(pointer);
						buildMaze(maze, row + 2, col);
					}
				}
				break;
			case 4: // west
				// Whether 2 cells to the left is out or not
				if (col - 2 < 0) {
					break;
				} else {
					pointer = maze[row][col - 2];
					if (!(wallNotHere[row][col - 2])) {

						wallNotHere[row][col - 2] = true;
						wallNotHere[row][col - 1] = true;
						this.maze.add(pointer);
						pointer = maze[row][col - 1];
						this.maze.add(pointer);
						buildMaze(maze, row, col - 2);
					}
				}
				break;
			}
		}
	}

	/**
	 * Depth first search method to find the end of the maze
	 * 
	 * @param maze:  Maze that will be solved
	 * @param row:   start search from here
	 * @param col:   start search from here
	 * @param target the cell that is being searched for
	 */

	public void findEnd(CellInfo[][] maze, int row, int col, CellInfo target) {
		pathMade = true;
		if (found) {
			return;
		}

		if (row >= maze.length || col >= maze.length || row < 0 || col < 0) {
			return;
		}

		pointer = maze[row][col];
		if (pointer.getID() == ID.WALL || pointer.IsVisited()) {
			return;
		} else {

			if (pointer == target) {
				found = true;
				System.out.println("PATH SIZE: " + path.size());
				return;
			} else {
				pointer.setVisited(true);
				path.add(pointer);

					/** check if neighboring cells are the target cell (TARGET) **/
					findEnd(maze, row - 1, col, target);
					findEnd(maze, row, col + 1, target);
					findEnd(maze, row, col - 1, target);
					findEnd(maze, row + 1, col, target);
				
			}
		}
	}

	public void bfs(CellInfo start, CellInfo target, boolean showFullPath) {
		pathMade = true;
		CellInfo pointer = null;
		q.add(start);
		start.setVisited(true);
		while (!q.isEmpty() && !found) {

			pointer = q.poll();
			if (pointer == target) {
				found = true;
				break;
			}

			for (CellInfo cell : pointer.getCells()) {
				if (cell.IsVisited() || cell.getID() == ID.WALL) {

				} else {
					cell.setParent(pointer);
					if (showFullPath) {
						path.add(cell);
					}
					q.add(cell);
					cell.setVisited(true);
				}
			}
		}

		if (!showFullPath) {
			while (pointer != start && pointer.getParent() != null) {
				path.add(pointer);
				pointer = pointer.getParent();

			}
			Collections.reverse(path);
		}
		System.out.println("PATH SIZE: " + path.size());
		return;
	}

	/**
	 * Method that will change the position of the user as long as the users move is
	 * valid
	 * 
	 * @param maze: maze that the user is moving on
	 * @param row:  row that the user is attempting to move to
	 * @param col:  col that the user is attempting to move to
	 * @return return whether the move is valid or not
	 */
	public boolean movePlayer(CellInfo[][] maze, int row, int col) {
		if (row >= maze.length || col >= maze.length || row < 0 || col < 0) {// if out of bounds
			return false;
		}
		pointer = maze[row][col];

		if (pointer.getID() == ID.WALL) {
			return false;
		} else {
			pointer.setID(ID.CURRENT);
			return true;
		}
	}

	/**
	 * 
	 * @return whether the path has been made or not
	 */
	public boolean isPathMade() {
		return pathMade;
	}

	/**
	 * 
	 * @param index index of the arraylist
	 * @return the cell from the specified index of the path
	 */
	public CellInfo getPathCell(int index) {
		return path.get(index);
	}

	/**
	 * 
	 * @param index index of the arraylist
	 * @return the cell from the specified index
	 */
	public CellInfo getMazeCell(int index) {

		return maze.get(index);
	}

	/**
	 * @return found: return whether or not the end of the maze has been found
	 */
	public boolean isFound() {

		return found;
	}

	/**
	 * @return path.size(): return the size of the path
	 */
	public int getPathLength() {

		return path.size();
	}

	/**
	 * @return path.size(): return the size of the maze
	 */
	public int getMazeLength() {
		return maze.size();
	}

	/**
	 * Method to delete the path and mark all cells as unvisited
	 * 
	 * @param maze: Maze from which the path will be deleted
	 */
	public void clearPath(CellInfo[][] maze) {

		path.clear();
		pathMade = false;
		found = false;

		for (int i = 0; i < maze.length; i++) {
			for (int j = 0; j < maze[i].length; j++) {
				pointer = maze[i][j];
				pointer.setVisited(false);
				pointer.setParent(null);
			}
		}
	}

	/**
	 * Method to delete maze and set all requiered variables to their initial value
	 */
	public void clearMaze() {

		maze.clear();
		pathMade = false;
		found = false;
		q.clear();
		for (int i = 0; i < wallNotHere.length; i++) {
			for (int j = 0; j < wallNotHere[i].length; j++) {
				wallNotHere[i][j] = false;
			}
		}
	}

	/**
	 * Method to the maze to a text file
	 * 
	 * @param maze: maze that will be saved
	 */
	public void saveMaze(CellInfo[][] maze) {
		m.saveMaze(maze);
	}

	/**
	 * Method to load maze from text file
	 * 
	 * @param maze: maze in which the file will be loaded onto
	 */
	public void loadMaze(CellInfo[][] maze) {
		clearMaze();
		clearPath(maze);
		char[][] temp = m.loadMaze();

		for (int i = 0; i < wallNotHere.length; i++) {
			for (int j = 0; j < wallNotHere[i].length; j++) {
				if (temp[i][j] == '0') {
					pointer = maze[i][j];
					if (pointer.getID() != ID.START && pointer.getID() != ID.TARGET) {
						pointer.setID(ID.PATH);
					}
				}
			}
		}
	}
}