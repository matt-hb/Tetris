package tetris;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.*;
import gui.TetrisApp;

public class Board {
	private int width;
	private int height;
	
	private class Cell {
		public boolean filled;
		public Color color;
		
		public Cell () {
			filled = false;
			color = TetrisApp.BACKGROUND_COLOR.brighter();
		}
	}
	
	private List<List<Cell>> lines;
	
	protected Board () {
		width = 10;
		height = 24+4;
		initEmptyBoard();
	}
	
	protected Board (int w, int h) {
		width = w;
		height = h+4;
		initEmptyBoard();
	}
	
	protected int getWidth() {
		return width;
	}
	
	protected int getHeight() {
		return height-4;
	}
	
	protected boolean collides(Tetromino piece, int x, int y) {
		for (Point mino : piece.getMinos()) {
			if (y + mino.y < 0 || x + mino.x < 0 || x + mino.x >= width) return true;
			if (lines.get(y+mino.y).get(x+mino.x).filled) return true;
		}
		return false;
	}
	
	protected void placePiece(Tetromino piece, int x, int y) {
		for (Point mino : piece.getMinos()) {
			lines.get(y+mino.y).get(x+mino.x).filled = true;
			lines.get(y+mino.y).get(x+mino.x).color = piece.getColor();
		}
	}
	
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
	
	protected boolean isDead() {
		for (int y = getHeight(); y < height; y++) {
			if (lines.get(y).stream().anyMatch(cell -> cell.filled)) return true;
		}
		return false;
	}
	
	private void initEmptyBoard() {
		lines = new ArrayList<>();
		for (int i = 0; i < height; i++) {
			addEmptyLine();
		}
	}
	
	private void removeLine(int h) {
		lines.remove(h);
		addEmptyLine();
	}
	
	private void addEmptyLine() {
		List<Cell> newRow = new ArrayList<>();
		for (int j = 0; j < width; j++) {
			newRow.add(new Cell());
		}
		lines.add(newRow);
	}
	
	public void drawBoard(Graphics g, int squareSize) {
		for (int y = 0; y < getHeight(); y++) {
			for (int x = 0; x < width; x++) {
				Tetromino.drawMino(g, x*squareSize, y*squareSize, lines.get(y).get(x).color, squareSize);
			}
		}
	}
}
