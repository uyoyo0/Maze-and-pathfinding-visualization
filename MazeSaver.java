/**
 * Class to allow the user to save and load mazes
 * @author Uyiosa Iyekekpolor
 * @version 2021-01-20
 */
import java.io.*;
import java.util.Scanner;

//0 == path
//1 == wall
public class MazeSaver {
	private File file;
	private FileWriter out;
	private BufferedWriter write;
	private char[][] loadedMaze = new char[35][35];
	private Scanner scan;
	private CellInfo pointer;

	/**
	 * Constructor to create or find file that will be used
	 * @param fileName name of file that will be used in this classes methods 
	 */
	public MazeSaver(String fileName) {
		file = new File(fileName);

		
		if (file.exists()) {// If it exists, we don't need to do anything new
			
			
		} else {// If the file doesn't exist, try to create it
			try {
				file.createNewFile();

			} catch (IOException e) {
				e.printStackTrace();
				System.out.println(e.getMessage());
			}
		}

		try {
			out = new FileWriter(file, true);
		} catch (IOException e) {
			e.printStackTrace();
		}

		write = new BufferedWriter(out);
	}

	/**
	 * Method saves maze onto a text file
	 * @param maze: The maze that is to be copied into the file
	 */
	public void saveMaze(CellInfo[][] maze) {

		clearFile(file);
		for (int row = 0; row < maze.length; row++) {
			for (int col = 0; col < maze[row].length; col++) {

				try {
					pointer = maze[row][col];
					if (pointer.getID() != ID.WALL ) {
						write.write("0");
					} else {
						write.write("1");
					}

				} catch (IOException e) {
					System.out.println("Problem writing to file.");
					System.err.println("IOException: " + e.getMessage());
				}

			}
			try {
				write.newLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			write.close();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @return maze loaded from file
	 *
	 */
	public char[][] loadMaze() {
		int row = 0;

		try {
			scan = new Scanner(file);
			while (scan.hasNextLine()) {
				loadedMaze[row] = scan.nextLine().toCharArray();
				row++;
			}
		} catch (IOException e) {
			System.out.println("There was an error loading your file");
			System.out.println(e.getMessage());

		}
		return loadedMaze;
	}

	/**
	 * Method to clear the maze from the file
	 * @param file The file that will be cleared
	 *
	 */
	private void clearFile(File file) {
		try {

			out = new FileWriter(file, false); // <--- Because I set this to false it just clears the file.
			write = new BufferedWriter(out);

			write.write("");

		} catch (IOException e) {
			System.out.println("Problem writing to file.");
			System.err.println("IOException: " + e.getMessage());
		}
	}
}
