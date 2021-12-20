
/**
 * Class to handle all of the graphics within the program
 * @author Uyiosa Iyekekpolor
 * @version 2021-01-20
 */

import javax.swing.*;
import javax.swing.event.MouseInputListener;

import com.sun.tools.classfile.Annotation.element_value;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class MazeGraphics extends JPanel implements ActionListener, KeyListener, MouseInputListener {

	/*
	 * The timer object is used to slow down the process of drawing the path taken
	 * in the DFS this allows the user to understand the path that the dfs algorithm
	 * will take. The argument "this" simply refers to the ActionListener that I
	 * implemented
	 */

	/** VARIABLES FOR ANIMATION **/
	private Timer timer = new Timer(80, this);
	private String message = "Click something";
	private boolean isGenerated = false;
	private int pathIndex = 0;// used when displaying path
	private int mazeIndex = 0;
	private boolean paused = false;

	/** VARIABLES FOR UTILITIES **/

	private int[] dirx = { 0, -1, 1, 0 };
	private int[] diry = { -1, 0, 0, 1 };
	private CellInfo pointer = null; // im using this as a temp var to point to each of my CellInfo objects since
	// they are stored in an 2d array
	private MazeLogic logic = new MazeLogic();
	private boolean changingTarget = false;
	private boolean changingStart = false;

	/** VARIABLES FOR JFRAME **/
	private JFrame frame;
	private int borderX = 40;
	private int borderY = 40;

	/** VARIABLES FOR MAZE **/
	private CellInfo[][] maze;
	private CellInfo startCell;// start cell
	private CellInfo targetCell;// target cell
	private int row;
	private int col;
	private int length = 14;
	private int width = 14;

	/**
	 * Constructor: Initialize maze and JFrame
	 */
	public MazeGraphics() {

		/** FRAME SETUP **/
		frame = new JFrame();
		frame.setTitle("Maze");
		frame.setSize(780, 680);
		frame.setResizable(true);
		frame.setLocationRelativeTo(null);// centers the window
		frame.add(this);
		frame.addKeyListener(this);
		frame.addMouseMotionListener(this);
		frame.addMouseListener(this);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		maze = new CellInfo[35][35];

		initMaze();

	}

	/**
	 * Initialize the maze with all its default variables and values
	 */
	private void initMaze() {

		for (int i = 0; i < maze.length; i++) {
			for (int j = 0; j < maze[i].length; j++) {

				maze[i][j] = new CellInfo(borderX + (15 * j), borderY + (15 * i), length, width, ID.PATH, i, j);
			}
		}

		for (int i = 0; i < maze.length; i++) {
			for (int j = 0; j < maze.length; j++) {
				pointer = maze[i][j];

				for (int k = 0; k < 4; k++) {
					int x = j + dirx[k];
					int y = i + diry[k];

					if (x > maze.length - 1 || x < 0 || y > maze.length - 1 || y < 0) {
						continue;
					} else {
						pointer.addCell(maze[y][x]);
					}
				}
			}

		}

		startCell = maze[0][0];
		col = 0;
		row = 0;
		startCell.setID(ID.START);
		targetCell = maze[maze.length - 1][maze.length - 1];
		targetCell.setID(ID.TARGET);

	}

	/**
	 * Method to paint all required components in the program
	 * 
	 * @param g: used to paint panel
	 */
	public void paint(Graphics g) {

		if (paused) {

		} else {

			if (isGenerated) {
				timer.start();
			}
			if (logic.isPathMade()) {
				timer.start();// This pauses the process of painting for a specified
								// time and runs the code in the "ActionPerformed method
			}
			/** BACKGROUND COLOR **/
			g.setColor(Color.DARK_GRAY);
			g.fillRect(0, 0, 780, 680);

			/** BUTTONS **/
			g.setColor(Color.gray);
			g.fillRect(50, 575, 80, 40);// solve
			g.fillRect(450, 575, 80, 40);// clear
			g.fillRect(240, 575, 100, 40);// generate
			g.fillRect(625, 115, 95, 30);// start
			g.fillRect(625, 165, 95, 30);// target
			g.fillRect(625, 235, 95, 30);// solve with DFS
			g.fillRect(625, 285, 95, 30);// solve with BFS

			g.fillRect(650, 530, 80, 30);// save
			g.fillRect(650, 580, 80, 30);// load

			g.setColor(Color.black);
			g.drawString("Solve", 75, 600);
			g.drawString("Reset Maze", 460, 600);
			g.drawString("Generate Maze", 250, 600);
			g.drawString("Change Start", 630, 135);
			g.drawString("Change Target", 630, 185);
			g.drawString("Depth First", 640, 255);
			g.drawString("Breadth First", 640, 305);

			g.drawString("Save maze", 660, 550);
			g.drawString("Load maze", 660, 600);

			/** MESSAGE **/
			g.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
			g.setColor(Color.white);
			g.drawString(message, 250, 25);

			g.setFont(new Font("Comic Sans MS", Font.BOLD, 12));
			g.drawString("Choose Target/Start Cell", 590, 100);
			g.drawString("Solve Maze Going...", 615, 220);

			/** DETERMINE THE COLOR OF EACH CELL BASED ON ITS ID **/
			for (int i = 0; i < maze.length; i++) {
				for (int j = 0; j < maze[i].length; j++) {

					pointer = maze[i][j];

					if (pointer.getID() == ID.WALL) {// wall

						g.setColor(new Color(30, 33, 43));// black-ish color
						g.fillRect(pointer.getX(), pointer.getY(), pointer.getWidth(), pointer.getHeight());

					} else if (pointer.getID() == ID.START) {// start cell
						g.setColor(Color.red);
						g.fillRect(pointer.getX(), pointer.getY(), pointer.getWidth(), pointer.getHeight());

					} else if (pointer.getID() == ID.TARGET) {// target cell
						g.setColor(Color.green);
						g.fillRect(pointer.getX(), pointer.getY(), pointer.getWidth(), pointer.getHeight());

					} else if (pointer.getID() == ID.VISIT) {// cell is a visited path
						g.setColor(Color.magenta);
						g.fillRect(pointer.getX(), pointer.getY(), pointer.getWidth(), pointer.getHeight());

					} else if (pointer.getID() == ID.CURRENT) {
						g.setColor(Color.white);
						g.fillRect(pointer.getX(), pointer.getY(), pointer.getWidth(), pointer.getHeight());

					} else {// cell is an open path

						g.setColor(new Color(51, 124, 160));// blue-ish color
//						g.setColor(Color.white);
						g.fillRect(pointer.getX(), pointer.getY(), pointer.getWidth(), pointer.getHeight());
					}
				}

			}
		}
	}

	/**
	 * Method to turn cells that are dragged on by the mouse to either paths or
	 * walls
	 * 
	 * @param e The Mouse event
	 */
	public void mouseDragged(MouseEvent e) {

		pointer = logic.findCell(maze, e.getX(), e.getY());
		if (pointer != null && pointer != startCell && pointer != targetCell) {
			if (SwingUtilities.isRightMouseButton(e)) {
				pointer.setID(ID.PATH);
				updateMaze();
			} else {
				pointer.setID(ID.WALL);
				updateMaze();
			}
		}
	}

	/**
	 * Method to detect clicks from the users mouse and to perform a variety of
	 * actions depending on where the user clicked
	 * 
	 * @param e The Mouse event
	 */
	public void mouseClicked(MouseEvent e) {

		int x = e.getX();
		int y = e.getY();
		System.out.println("X: " + x + " Y: " + y);
		pointer = logic.findCell(maze, x, y);

		if (changingTarget && pointer != null && pointer != startCell) {
			targetCell.setID(ID.PATH);
			targetCell = logic.findCell(maze, x, y);
			targetCell.setID(ID.TARGET);
			message = "Target Cell Chosen";
			updateMaze();
			changingTarget = false;
		} else if (changingStart && pointer != null && pointer != targetCell) {
			startCell.setID(ID.PATH);
			startCell = logic.findCell(maze, x, y);
			startCell.setID(ID.START);
			message = "Start Cell Chosen";
			updateMaze();
			changingStart = false;
			row = (startCell.getY() - borderY) / 15;
			col = (startCell.getX() - borderX) / 15; // I reverse the operations i used in the "init" method to get
														// these values
		} else {

			if (x > 50 && x < 130 && y > 595 && y < 645) {// If user clicks "solve"
				System.out.println("FINDING SHORTEST PATH");
				logic.clearPath(maze);
				logic.bfs(startCell, targetCell, false);
				updateMaze();

			} else if (x > 450 && x < 530 && y > 595 && y < 645) {// if user clicks "reset"
				message = "Click Something";
				initMaze();
				row = 0;// reset starting position of players
				col = 0;
				isGenerated = false;
				logic.clearPath(maze);
				logic.clearMaze();
				pathIndex = 0;
				mazeIndex = 0;
				updateMaze();
			} else if (x > 240 && x < 340 && y > 595 && y < 645) {// if user clicks "Generate maze"
				logic.clearPath(maze);
				clearMaze();
				logic.buildMaze(maze, 0, 0);
				isGenerated = true;
				updateMaze();

			} else if (x > 625 && x < 725 && y > 140 && y < 185) {// User wants to change the starting cell
				message = "Choose Start";
				updateMaze();
				changingStart = true;
			} else if (x > 625 && x < 725 && y > 195 && y < 230) {// user wants to change the target cell
				message = "Choose Target";
				updateMaze();
				changingTarget = true;

			} else if (x > 655 && x < 740 && y > 560 && y < 595) {// user saves maze
				message = "Maze Saved";
				updateMaze();
				logic.saveMaze(maze);
			} else if (x > 655 && x < 740 && y > 610 && y < 645) {// user loads in a maze
				logic.clearPath(maze);
				clearMaze();
				message = "Maze loaded";
				logic.loadMaze(maze);
				updateMaze();
			}else if (x > 625 && x < 725 && y > 265 && y < 290) {// User wishes to solve maze via a depth first search
				logic.clearPath(maze);
				logic.findEnd(maze, row, col, targetCell);
				updateMaze();
			}else if (x > 625 && x < 725 && y > 314 && y < 350){
				logic.clearPath(maze);
				logic.bfs(startCell, targetCell, true);
				updateMaze();
			}
			if (pointer != null) {
				pointer.swapID();
				updateMaze();// repaint the maze now that one of the cells has changed
			}
		}
	}

	/**
	 * If the game is paused, unpause it. If the game is not paused, pause it
	 */
	private void swapPaused() {
		if (paused) {
			paused = false;
		} else {
			paused = true;
		}
	}

	/*
	 * Repaint the panel
	 */
	private void updateMaze() {
		repaint();
	}

	/**
	 * Reset the maze to its default value and reset all needed variables
	 */
	private void clearMaze() {
		isGenerated = false;
		logic.clearPath(maze);
		logic.clearMaze();
		pathIndex = 0;
		mazeIndex = 0;
		message = "Click something";

		for (int i = 0; i < maze.length; i++) {
			for (int j = 0; j < maze.length; j++) {

				pointer = maze[i][j];

				if (pointer != targetCell && pointer != startCell) {
					pointer.setID(ID.WALL);
				}
			}
		}
		updateMaze();
	}

	/**
	 * Method that is called whenever the timer is started, this method paints the
	 * panel with a delay which results in animation
	 * 
	 * @param e The ActionEvent
	 */
	public void actionPerformed(ActionEvent e) {
		if (paused) {

		} else {

			if (logic.isPathMade()) {// If a path exists

				if (pathIndex < logic.getPathLength()) {// if the path has not been fully drawn
					pointer = logic.getPathCell(pathIndex);
					if (pointer != startCell && pointer != targetCell) {
						pointer.setID(ID.CURRENT);// the cell you are currently drawing
					}
					if (pathIndex >= 1) {
						pointer = logic.getPathCell(pathIndex - 1);
						if (pointer != startCell && pointer != targetCell) {
							pointer.setID(ID.VISIT);
						}
					}
					pathIndex++;
					message = "Finding path...";
					repaint();
				} else if (pathIndex == logic.getPathLength()) {
					if (logic.isFound()) {
						message = "Path Found";
					} else {
						message = "Path not found";
					}

					pathIndex++;
					repaint();
				}

			} else if (isGenerated) {// if the maze has been generated

				if (mazeIndex < logic.getMazeLength()) {// if the maze has not yet been drawn out
					pointer = logic.getMazeCell(mazeIndex);
					if (pointer != startCell && pointer != targetCell) {// I don't wanna set the start cell
																		// to a path
						pointer.setID(ID.CURRENT);
					}
					if (mazeIndex >= 1) {
						pointer = logic.getMazeCell(mazeIndex - 1);
						if (pointer != startCell && pointer != targetCell) {// I dont wanna set the start
																			// cell to a path
							pointer.setID(ID.PATH);
						}
					}

					mazeIndex++;
					message = "Building Maze...";
					updateMaze();

				} else if (mazeIndex == logic.getMazeLength()) {// if maze is complete, get rid of the "current" cell
					pointer = logic.getMazeCell(mazeIndex - 1);
					pointer.setID(ID.PATH);
					message = "Maze built";
					mazeIndex++;
					repaint();
				}
			}
		}
	}

	/**
	 * Method to perform a variety of actions depending on the key that is clicked
	 * 
	 * @param e The Key event
	 */
	public void keyPressed(KeyEvent e) {

		CellInfo prevCell;// used to set previous cell to a path after you move to a new cell
		switch (e.getKeyChar()) {

		case 'a':// move to your left
			if (logic.movePlayer(maze, row, col - 1)) {
				prevCell = maze[row][col];
				prevCell.setID(ID.PATH);
				col--;
				updateMaze();
			}
			break;
		case 'w':// move up
			if (logic.movePlayer(maze, row - 1, col)) {
				prevCell = maze[row][col];
				prevCell.setID(ID.PATH);
				row--;
				updateMaze();
			}
			break;
		case 's':// move down
			if (logic.movePlayer(maze, row + 1, col)) {
				prevCell = maze[row][col];
				prevCell.setID(ID.PATH);
				row++;
				updateMaze();
			}
			break;
		case 'd':// move to your right
			if (logic.movePlayer(maze, row, col + 1)) {
				prevCell = maze[row][col];
				prevCell.setID(ID.PATH);
				col++;
				updateMaze();
			}
			break;
		case 'p':// toggle pause
			swapPaused();
			break;
		case 'g':// toggle between grid view and plain view
			if (length == 14 && width == 14) {
				length = 15;
				width = 15;
				initMaze();// re-create the maze with its new dimensions
			} else {
				width = 14;
				length = 14;
				initMaze();// re-create the maze with its new dimensions
			}
			updateMaze();
			break;
		case '1':
			System.out.println("Speed: Slow");
			timer.setDelay(600);
			updateMaze();
			break;
		case '2':
			System.out.println("Speed: Normal");
			timer.setDelay(100);
			updateMaze();
			break;
		case '3':
			timer.setDelay(5);
			System.out.println("Speed: Fast");
			updateMaze();
			break;
		case 'b':
			System.out.println("USING BREADTH FIRST SEARCH");
			logic.clearPath(maze);
			logic.bfs(startCell, targetCell, true);
			updateMaze();
			break;
		case 'D':
			System.out.println("USING DEPTH FIRST SEARCH");
			logic.clearPath(maze);
			logic.findEnd(maze, row, col, targetCell);
			updateMaze();
			break;
		}

		if (row == (targetCell.getY() - borderY) / 15 && col == (targetCell.getX() - borderX) / 15) {// if the player
																										// has reached
																										// the end
			message = "You Win!";
			updateMaze();
		} else {
			message = "";
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {

	}

	@Override
	public void keyTyped(KeyEvent arg0) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {

	}
	
	public static void main(String[] args) {
		new MazeGraphics();
	}

}
