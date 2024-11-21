package tetris;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.*;
import gui.TetrisApp;

/**
 * The Board class represents the game board in Tetris, which is a grid of cells that can be filled by Tetrominos.
 */
public class Board {
	/**
	 * A private inner class representing a cell in the board, which can be filled with a Tetromino and has a color for drawing.
	 */
	private class Cell {
		/**
		 * A boolean flag indicating whether the cell is filled with a Tetromino.
		 */
		public boolean filled;

		/**
		 * The color of the cell, which is the background color if the cell is empty,
		 * and the color of the Tetromino that filled it if not.
		 */
		public Color color;
		
		/**
		 * Constructs a new empty cell with the background color.
		 */
		public Cell () {
			filled = false;
			color = TetrisApp.BACKGROUND_COLOR.brighter();
		}
	}

	/**
	 * The width of the board in cells.
	 */
	private int width;
	
	/**
	 * The height of the board in cells, including 4 hidden buffer rows at the top.
	 */
	private int height;
	
	/**
	 * A list of lists of cells representing the board, with the first index being the y-coordinate
	 * in ascending order and the second the x-coordinate from left to right.
	 */
	private List<List<Cell>> lines;
	
	/**
	 * Constructs a new Board object with the default width and height, 10x24 as in the original Tetris game.
	 * Adds 4 hidden rows at the top of the board to allow the spawning of new Tetrominos and for death to occur.
	 */
	protected Board () {
		width = 10;
		height = 24+4;
		initEmptyBoard();
	}
	
	/**
	 * Returns the width of the board in cells.
	 * @return the width of the board
	 */
	protected int getWidth() {
		return width;
	}
	
	/**
	 * Returns the height of the board in cells, excluding the 4 hidden lines at the top.
	 * @return the playable height of the board
	 */
	protected int getHeight() {
		return height-4;
	}
	
	/**
	 * Checks if the given Tetromino piece collides with the current state of the board
	 * at the given (x, y) origin position, using the relative coordinates of its minos.
	 * @param piece the Tetromino piece to check for collision
	 * @param x the x-coordinate of the origin for the Tetromino's position
	 * @param y the y-coordinate of the origin for the Tetromino's position
	 * @return true if the piece collides with the board or is out of bounds, false otherwise
	 */
	protected boolean collides(Tetromino piece, int x, int y) {
		for (Point mino : piece.getMinos()) {
			if (y + mino.y < 0 || x + mino.x < 0 || x + mino.x >= width) return true;
			if (lines.get(y+mino.y).get(x+mino.x).filled) return true;
		}
		return false;
	}
	
	/**
	 * Places the given Tetromino piece on the board at the given (x, y) origin position,
	 * using the relative coordinates of its minos to fill the cells with the Tetromino's color.
	 * This method does not check for collisions, it should only be called after checking with collides().
	 * @param piece the Tetromino piece to place on the board
	 * @param x the x-coordinate of the origin for the Tetromino's position
	 * @param y the y-coordinate of the origin for the Tetromino's position
	 */
	protected void placePiece(Tetromino piece, int x, int y) {
		for (Point mino : piece.getMinos()) {
			lines.get(y+mino.y).get(x+mino.x).filled = true;
			lines.get(y+mino.y).get(x+mino.x).color = piece.getColor();
		}
	}
	
	/**
	 * Clears all lines that are completely filled with Tetrominos, removing them from the board
	 * which results in the pieces above them being dropped down, and adding new empty lines
	 * at the top to keep the height of the board constant.
	 * @return the number of lines that were cleared
	 */
	protected int clearFilledLines() {
		int cleared = 0;
		for (int y = getHeight(); y >= 0; y--) {
			if (lines.get(y).stream().allMatch(cell -> cell.filled)) {
				removeLine(y);
				cleared++;
			}
		}
		return cleared;
	}
	
	/**
	 * Checks if any lines above the playable height of the board have any filled cells in them,
	 * which means the stack is too high and the game is over.
	 * @return true if the game is over, false otherwise
	 */
	protected boolean isDead() {
		for (int y = getHeight(); y < height; y++) {
			if (lines.get(y).stream().anyMatch(cell -> cell.filled)) return true;
		}
		return false;
	}
	
	/**
	 * Initializes the board with empty cells, filling the entire grid with empty cells.
	 */
	private void initEmptyBoard() {
		lines = new ArrayList<>();
		for (int i = 0; i < height; i++) {
			addEmptyLine();
		}
	}
	
	/**
	 * Removes the line at the given height from the board, shifting all lines above it down by one.
	 * Adds an empty line at the top of the board to keep the height constant.
	 * @param h the height of the line to remove
	 */
	private void removeLine(int h) {
		lines.remove(h);
		addEmptyLine();
	}
	
	/**
	 * Adds a new empty line at the top of the board, filling it with empty cells.
	 */
	private void addEmptyLine() {
		List<Cell> newRow = new ArrayList<>();
		for (int j = 0; j < width; j++) {
			newRow.add(new Cell());
		}
		lines.add(newRow);
	}
	
	/**
	 * Draws the board on the given Graphics object, using the given square size to scale the cells.
	 * Origin is at the top-left corner, with the x-axis increasing to the right and the y-axis increasing downwards.
	 * @param g
	 * @param squareSize
	 */
	public void drawBoard(Graphics g, int squareSize) {
		for (int y = 0; y < getHeight(); y++) {
			for (int x = 0; x < width; x++) {
				Tetromino.drawMino(g, x*squareSize, y*squareSize, lines.get(y).get(x).color, squareSize);
			}
		}
	}
}
