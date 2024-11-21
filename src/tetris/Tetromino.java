package tetris;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.*;

/**
 * The Tetromino class represents a Tetris piece with a specific shape, color, and set of minos (blocks).
 * It provides methods to rotate the piece, draw it on a graphics context, and access its properties.
 */
public class Tetromino {
	/**
	 * The Shape enum represents the different types of tetromino shapes
	 * used in the Tetris game. Each shape is represented by a single
	 * letter corresponding to its common name: I, J, L, S, Z, T, O.
	 */
	protected enum Shape {
		I, J, L, S, Z, T, O
	}
	/**
	 * A static map that stores the color of each shape.
	 */
	private static EnumMap<Shape, Color> shapeColors = new EnumMap<>(Shape.class);
	/**
	 * A static map that stores the minos of each shape.
	 */
	private static EnumMap<Shape, Set<Point>> shapeMinos = new EnumMap<>(Shape.class);

	/**
	 * A set of points (relative coordinates) to represent the minos that make up the Tetromino.
	 */
	private Set<Point> minos;

	/**
	 * The color of the Tetromino.
	 */
	private Color color;

	/**
	 * The shape of the Tetromino.
	 */
	private Shape shape;
	
	/**
	 * Constructs a new Tetromino object with the specified shape, setting the minos and color accordingly.
	 * @param shape the shape of the Tetromino
	 */
	protected Tetromino(Shape shape) {
		minos = shapeMinos.get(shape);
		color = shapeColors.get(shape);
		this.shape = shape;
	}
	
	/**
	 * Returns the minos of the Tetromino.
	 * @return a Set of points representing the minos
	 */
	protected Set<Point> getMinos(){
		return minos;
	}
	
	/**
	 * Sets the minos of the Tetromino to the given set of points.
	 * @param newMinos the new set of minos
	 */
	protected void setMinos(Set<Point> newMinos) {
		minos = newMinos;
	}
	
	/**
	 * Returns the color of the Tetromino.
	 * @return the color of the Tetromino
	 */
	protected Color getColor() {
		return color;
	}

	/**
	 * Checks if the Tetromino has the given shape.
	 * @param s the shape to check against
	 * @return true if the Tetromino has the given shape, false otherwise
	 */
	protected boolean isShape(Shape s) {
		return shape == s;
	}
	
	/**
	 * Rotates the Tetromino 90 degrees to the left (counter-clockwise).
	 * @return a new Tetromino object representing the rotated piece
	 */
	protected Tetromino rotatedLeft() {
		if (shape == Shape.O) return this;
		Tetromino out = new Tetromino(shape);
		Set<Point> newMinos = new HashSet<>();
		for (Point mino : minos) {
			newMinos.add(new Point(-1*mino.y, mino.x));
		}
		out.setMinos(newMinos);
		return out;
	}
	
	/**
	 * Rotates the Tetromino 90 degrees to the right (clockwise).
	 * @return a new Tetromino object representing the rotated piece
	 */
	protected Tetromino rotatedRight() {
		if (shape == Shape.O) return this;
		Tetromino out = new Tetromino(shape);
		Set<Point> newMinos = new HashSet<>();
		for (Point mino : minos) {
			newMinos.add(new Point(mino.y, -1*mino.x));
		}
		out.setMinos(newMinos);
		return out;
	}
	
	/**
	 * Draws the Tetromino on the given graphics context at the specified location and size.
	 * Assumes the minos are relative to the top-left corner of the piece, increasing height downwards.
	 * @param g the graphics context to draw on
	 * @param x the x-coordinate of the top-left corner of the piece
	 * @param y the y-coordinate of the top-left corner of the piece
	 * @param squareSize the size of each square in pixels
	 */
	protected void drawPiece(Graphics g, int x, int y, int squareSize) {
		for (Point mino : minos) {
			drawMino(g, x + mino.x*squareSize, y + mino.y*squareSize, this.color, squareSize);
		}
	}
	
	/**
	 * Draws a single mino on the graphics context at the specified location and size.
	 * Draws a darker border around the mino.
	 * @param g the graphics context to draw on
	 * @param x the x-coordinate of the top-left corner of the mino
	 * @param y the y-coordinate of the top-left corner of the mino
	 * @param c the color of the mino
	 * @param size the size of the mino in pixels
	 */
	protected static void drawMino(Graphics g, int x, int y, Color c, int size) {
		int borderWidth = Math.floorDiv(size, 10);
		if (borderWidth == 0) borderWidth = 1;
		
		g.setColor(c.darker());
		g.fillRect(x, y, size, size);
		
		g.setColor(c);
		g.fillRect(x + borderWidth, y + borderWidth, size - 2*borderWidth, size - 2*borderWidth);
	}
	
	/**
	 * Initializes the static maps with the colors and minos for each shape.
	 */
	static {
		shapeColors.put(Shape.I, new Color(139, 233, 253));
		shapeColors.put(Shape.J, new Color(98, 114, 164));
		shapeColors.put(Shape.L, new Color(255, 184, 108));
		shapeColors.put(Shape.S, new Color(80, 250, 123));
		shapeColors.put(Shape.Z, new Color(255, 85, 85));
		shapeColors.put(Shape.T, new Color(255, 121, 198));
		shapeColors.put(Shape.O, new Color(241, 250, 140));
				
		shapeMinos.put(Shape.I, new HashSet<>(Set.of(new Point(-1,0), new Point(0,0), new Point(1,0), new Point(2,0))));
		shapeMinos.put(Shape.J, new HashSet<>(Set.of(new Point(-1,1), new Point(-1,0), new Point(0,0), new Point(1,0))));
		shapeMinos.put(Shape.L, new HashSet<>(Set.of(new Point(-1,0), new Point(0,0), new Point(1,0), new Point(1,1))));
		shapeMinos.put(Shape.S, new HashSet<>(Set.of(new Point(-1,0), new Point(0,0), new Point(0,1), new Point(1,1))));
		shapeMinos.put(Shape.Z, new HashSet<>(Set.of(new Point(-1,1), new Point(0,1), new Point(0,0), new Point(1,0))));
		shapeMinos.put(Shape.T, new HashSet<>(Set.of(new Point(-1,0), new Point(0,0), new Point(1,0), new Point(0,1))));
		shapeMinos.put(Shape.O, new HashSet<>(Set.of(new Point(0,0), new Point(1,0), new Point(0,1), new Point(1,1))));
	}
}
