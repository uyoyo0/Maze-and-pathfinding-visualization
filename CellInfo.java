import java.util.*;

/**
 * Class to outline all functionalities of a cell in the maze
 * 
 * @author Uyiosa Iyekekpolor
 * @version 2021-01-20
 */
public class CellInfo {
	private ID id = ID.PATH;
	private int width, height, x, y, row, col;
	private boolean visited = false;
	private ArrayList<CellInfo> cells = new ArrayList<CellInfo>();
	private CellInfo parent;

	/**
	 * Constructor to initialize values of a given cell
	 * 
	 * @param x:            x-coordinate of cell
	 * @param y:            y-coordinate of cell
	 * @param width:        width of cell
	 * @param height:height of cell
	 * @param id:           ID of cell
	 */
	public CellInfo(int x, int y, int width, int height, ID id, int row, int col) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.id = id;
		this.row = row;
		this.col = col;
	}

	public void addCell(CellInfo cell) {
		cells.add(cell);
//		cell.cells.add(this);
	}

	/**
	 * @return x: return x-coordinate of cell
	 */
	public int getX() {
		return x;
	}

	/**
	 * @return y: return y-coordinate of cell
	 */
	public int getY() {
		return y;
	}

	/**
	 * @return height: return height of cell
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @return width: return width of cell
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @return visited: return whether or not the cell has been visited
	 */
	public boolean IsVisited() {
		return visited;
	}

	/**
	 * @param visited: Value of what the boolean visited will be set to
	 */
	public void setVisited(boolean visited) {
		this.visited = visited;
	}

	/**
	 * @return ID of the given cell
	 */
	public ID getID() {
		return id;
	}

	/**
	 * @param id: The ID that the given cell will be set to
	 */
	public void setID(ID id) {

		this.id = id;
	}

	/**
	 * If the ID given is a wall, change it to a path, if the ID given is a path,
	 * switch it to a wall
	 */
	public void swapID() {
		if (id == ID.PATH) {
			id = ID.WALL;
		} else if (this.id == ID.WALL) {
			id = id.PATH;
		}
	}
	
	public void setParent(CellInfo parent) {
		this.parent = parent;
	}
	
	public CellInfo getParent() {
		return parent;
	}

	public ArrayList<CellInfo> getCells() {
		return cells;
	}

}