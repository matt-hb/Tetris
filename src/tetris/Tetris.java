package tetris;

import java.awt.Graphics;
import java.util.*;

public class Tetris {
	private Board board;
	private Tetromino piece;
	private Tetromino nextPiece;
	private int pieceX, pieceY;
	private int score, linesClearedOnLevel, linesClearedTotal;
	private int gameSpeed;
	private static List<Tetromino.Shape> pieceBag;
	private static int pieceBagIndex;
		
	public Tetris() {
		board = new Board();
		score = linesClearedTotal = linesClearedOnLevel = 0;
		gameSpeed = 1;
		nextPiece = new Tetromino(pullFromPieceBag());
		newPiece();
	}
	
	public boolean moveDown() {
		if (tryMove(pieceX, pieceY-1)) {
			return true;
		}
		return finalizePiece(0);
	}
	
	public void moveLeft() {
		tryMove(pieceX-1, pieceY);
	}
	
	public void moveRight() {
		tryMove(pieceX+1, pieceY);
	}
	
	public void rotateLeft () {
		if (!board.collides(piece.rotatedLeft(), pieceX, pieceY)) piece = piece.rotatedLeft();
	}
	
	public void rotateRight () {
		if (!board.collides(piece.rotatedRight(), pieceX, pieceY)) piece = piece.rotatedRight();
	}
	
	public void drop() {
		int drop = 0;
		while(tryMove(pieceX, pieceY-1)) {
			drop++;
		}
		finalizePiece(drop >= 5 ? 5 : drop);
	}
	
	public int getDelayInMillis() {
		switch(gameSpeed) {
		case 1:
			return (int) Math.round(10000.0/board.getHeight());
		case 2:
			return (int) Math.round(6000.0/board.getHeight());
		case 3:
			return (int) Math.round(3000.0/board.getHeight());
		case 4:
			return (int) Math.round(1500.0/board.getHeight());
		case 5:
			return (int) Math.round(1000.0/board.getHeight());
		case 6:
			return (int) Math.round(400.0/board.getHeight());
		default:
			return 0;
		}
	}
	
	public int getScore() {
		return score;
	}
	
	public int getTotalLines() {
		return linesClearedTotal;
	}
	
	public int getLinesToNextLevel() {
		if (gameSpeed < 6) {
			return gameSpeed*10-linesClearedOnLevel;
		}
		else return 1;
	}

	public int getGameSpeed() {
		return gameSpeed;
	}
	
	public Board getBoard() {
		return board;
	}
	
	public void drawTetris(Graphics g, int squareSize) {
		board.drawBoard(g, squareSize);
		piece.drawPiece(g, pieceX*squareSize, pieceY*squareSize, squareSize);		
	}

	public void drawNextPiece(Graphics g, int squareSize) {
		if (nextPiece.isShape(Tetromino.Shape.O)) nextPiece.drawPiece(g, 3*squareSize/2, 3*squareSize/2, squareSize);
		else if (nextPiece.isShape(Tetromino.Shape.I)) nextPiece.drawPiece(g, 3*squareSize/2, 4*squareSize/2, squareSize);
		else nextPiece.drawPiece(g, 4*squareSize/2, 3*squareSize/2, squareSize);
	}
	
	private boolean tryMove(int newX, int newY) {
		if (board.collides(piece, newX, newY)) {
			return false;
		}
		pieceX = newX;
		pieceY = newY;
		return true;
	}
	
	private boolean finalizePiece(int drop) {
		board.placePiece(piece, pieceX, pieceY);
		if (board.isDead()) return false;
		int lines = board.clearFilledLines();
		switch(lines) {
		case 0:
			break;
		case 1:
			score += 40*(gameSpeed*gameSpeed+drop);
			break;
		case 2:
			score += 100*(gameSpeed*gameSpeed+drop);
			break;
		case 3:
			score += 300*(gameSpeed*gameSpeed+drop);
			break;
		case 4:
			score += 1200*(gameSpeed*gameSpeed+drop);
			break;
		}
		linesClearedOnLevel+=lines;
		linesClearedTotal+=lines;
		newPiece();
		if (getLinesToNextLevel() <= 0) {
			linesClearedOnLevel = 0;
			gameSpeed++;
		}
		return true;
	}
	
	private void newPiece () {
		pieceX = board.getWidth()/2-1;
		pieceY = board.getHeight();
		piece = nextPiece;
		nextPiece = new Tetromino(pullFromPieceBag());
		
		while (board.collides(piece, pieceX, pieceY)) {
			pieceY++;
		}
	}

	private static Tetromino.Shape pullFromPieceBag () {
		Tetromino.Shape out = pieceBag.get(pieceBagIndex++);
		if (pieceBagIndex == Tetromino.Shape.values().length) {
			pieceBagIndex = 0;
			Collections.shuffle(pieceBag);
		}
		return out;
	}

	static {
		pieceBag = new ArrayList<>(List.of(Tetromino.Shape.values()));
		Collections.shuffle(pieceBag);
		pieceBagIndex = 0;
	}
}
