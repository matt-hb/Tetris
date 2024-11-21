package tetris;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.*;

public class Tetromino {
	protected enum Shape {
		I, J, L, S, Z, T, O
	}
	private static EnumMap<Shape, Color> shapeColors = new EnumMap<>(Shape.class);
	private static EnumMap<Shape, List<Point>> shapeMinos = new EnumMap<>(Shape.class);

	private List<Point> minos;
	private Color color;
	private Shape shape;
		
	protected Tetromino(Shape shape) {
		minos = shapeMinos.get(shape);
		color = shapeColors.get(shape);
		this.shape = shape;
	}
		
	protected List<Point> getMinos(){
		return minos;
	}
	
	protected void setMinos(List<Point> newMinos) {
		minos = newMinos;
	}
	
	protected Color getColor() {
		return color;
	}

	protected boolean isShape(Shape s) {
		return shape == s;
	}
	
	protected Tetromino rotatedLeft() {
		if (shape == Shape.O) return this;
		Tetromino out = new Tetromino(shape);
		List<Point> newMinos = new ArrayList<>();
		for (int i = 0; i < minos.size(); i++) {
			newMinos.add(new Point(-1*minos.get(i).y, minos.get(i).x));
		}
		out.setMinos(newMinos);
		return out;
	}
	
	protected Tetromino rotatedRight() {
		if (shape == Shape.O) return this;
		Tetromino out = new Tetromino(shape);
		List<Point> newMinos = new ArrayList<>();
		for (int i = 0; i < minos.size(); i++) {
			newMinos.add(new Point(minos.get(i).y, -1*minos.get(i).x));
		}
		out.setMinos(newMinos);
		return out;
	}
	
	protected void drawPiece(Graphics g, int x, int y, int squareSize) {
		for (Point mino : minos) {
			drawMino(g, x + mino.x*squareSize, y + mino.y*squareSize, this.color, squareSize);
		}
	}
	
	protected static void drawMino(Graphics g, int x, int y, Color c, int size) {
		int borderWidth = Math.floorDiv(size, 10);
		if (borderWidth == 0) borderWidth = 1;
		
		g.setColor(c.darker());
		g.fillRect(x, y, size, size);
		
		g.setColor(c);
		g.fillRect(x + borderWidth, y + borderWidth, size - 2*borderWidth, size - 2*borderWidth);		
	}
	
	static {
		shapeColors.put(Shape.I, new Color(139, 233, 253));
		shapeColors.put(Shape.J, new Color(98, 114, 164));
		shapeColors.put(Shape.L, new Color(255, 184, 108));
		shapeColors.put(Shape.S, new Color(80, 250, 123));
		shapeColors.put(Shape.Z, new Color(255, 85, 85));
		shapeColors.put(Shape.T, new Color(255, 121, 198));
		shapeColors.put(Shape.O, new Color(241, 250, 140));
				
		shapeMinos.put(Shape.I, new ArrayList<>(List.of(new Point(-1,0), new Point(0,0), new Point(1,0), new Point(2,0))));		
		shapeMinos.put(Shape.J, new ArrayList<>(List.of(new Point(-1,1), new Point(-1,0), new Point(0,0), new Point(1,0))));
		shapeMinos.put(Shape.L, new ArrayList<>(List.of(new Point(-1,0), new Point(0,0), new Point(1,0), new Point(1,1))));
		shapeMinos.put(Shape.S, new ArrayList<>(List.of(new Point(-1,0), new Point(0,0), new Point(0,1), new Point(1,1))));
		shapeMinos.put(Shape.Z, new ArrayList<>(List.of(new Point(-1,1), new Point(0,1), new Point(0,0), new Point(1,0))));
		shapeMinos.put(Shape.T, new ArrayList<>(List.of(new Point(-1,0), new Point(0,0), new Point(1,0), new Point(0,1))));
		shapeMinos.put(Shape.O, new ArrayList<>(List.of(new Point(0,0), new Point(1,0), new Point(0,1), new Point(1,1))));
	}
}
