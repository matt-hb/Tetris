package tetris;

import java.awt.Graphics;
import java.util.*;

/**
 * The Tetris class represents the game logic for a Tetris game, including the board, falling piece, and scoring.
 */
public class Tetris {
	/**
	 * The Board object representing the game board.
	 */
	private Board board;

	/**
	 * The Tetromino object that the player is currently controlling.
	 */
	private Tetromino piece;

	/**
	 * The Tetromino object that will be the next one to fall, after the current one lands.
	 */
	private Tetromino nextPiece;

	/**
	 * The origin coordinates of the current falling piece.
	 */
	private int pieceX, pieceY;

	/**
	 * The metrics for how well the player is doing. Score will be the result at the end of the game,
	 * the linesCleared values represent the number of lines cleared in the current level and total game.
	 */
	private int score, linesClearedOnLevel, linesClearedTotal;

	/**
	 * The current game speed, which determines how fast the pieces fall.
	 * There are 6 total levels of speed, this increases as the player clears lines.
	 */
	private int gameSpeed;

	/**
	 * A list of Tetromino shapes that will be used to determine the next piece to fall.
	 * The list is shuffled and cycled through once it's empty to provide a random sequence of pieces.
	 * This is used over just generating a random piece to ensure that the player
	 * does not experience long droughts of any specific piece.
	 */
	private List<Tetromino.Shape> pieceBag = new ArrayList<>();

	/**
	 * A Random object used to shuffle the pieceBag.
	 * This is used to allow for a seed to be set for testing purposes.
	 */
	private Random randomSource;

	/**
	 * Constructs a new Tetris object with a new empty Board. Sets all performance metrics to 0 and the game speed to 1.
	 * Initializes the current and next pieces with the first two pieces from the pieceBag.
	 * Sets the random source to a built in Random object.
	 */
	public Tetris() {
		board = new Board();
		score = linesClearedTotal = linesClearedOnLevel = 0;
		gameSpeed = 1;
		randomSource = new Random();
		initPieceBag();
		nextPiece = new Tetromino(pullFromPieceBag());
		newPiece();
	}

	/**
	 * Constructs a new Tetris object with a new empty Board. Sets all performance metrics to 0 and the game speed to 1.
	 * Initializes the current and next pieces with the first two pieces from the pieceBag.
	 * Sets the random source to a given Random object.
	 * Protected scope because this is used for testing.
	 */
	protected Tetris(Random r) {
		board = new Board();
		score = linesClearedTotal = linesClearedOnLevel = 0;
		gameSpeed = 1;
		randomSource = r;
		initPieceBag();
		nextPiece = new Tetromino(pullFromPieceBag());
		newPiece();
	}
	
	/**
	 * Attempts to move the current piece down by one cell.
	 * If the piece cannot move down, it will put the piece on the board, add points, and generate a new piece.
	 * @return true if the piece was successfully moved or if it landed and the game continues, false if the game is over
	 */
	public boolean moveDown() {
		if (tryMove(pieceX, pieceY-1)) {
			return true;
		}
		return finalizePiece(0);
	}
	
	/**
	 * Attempts to move the current piece left by one cell. This can not result in a piece landing, so no finalization is needed.
	 */
	public void moveLeft() {
		tryMove(pieceX-1, pieceY);
	}
	
	/**
	 * Attempts to move the current piece right by one cell. This can not result in a piece landing, so no finalization is needed.
	 */
	public void moveRight() {
		tryMove(pieceX+1, pieceY);
	}
	
	/**
	 * Attempts to rotate the current piece left (counter-clockwise) by 90 degrees.
	 * This can not result in a piece landing, so no finalization is needed.
	 */
	public void rotateLeft () {
		if (!board.collides(piece.rotatedLeft(), pieceX, pieceY)) piece = piece.rotatedLeft();
	}
	
	/**
	 * Attempts to rotate the current piece right (clockwise) by 90 degrees.
	 * This can not result in a piece landing, so no finalization is needed.
	 */
	public void rotateRight () {
		if (!board.collides(piece.rotatedRight(), pieceX, pieceY)) piece = piece.rotatedRight();
	}
	
	/**
	 * Performs a "hard-drop", which moves the current piece down as far as it can go without colliding with the board.
	 * This is guaranteed to result in a piece landing, so it will finalize the piece and generate a new one.
	 * Extra points are awarded for the height of the drop, up to 5 cells.
	 */
	public void drop() {
		int drop = 0;
		while(tryMove(pieceX, pieceY-1)) {
			drop++;
		}
		finalizePiece(drop >= 5 ? 5 : drop);
	}
	
	/**
	 * Returns the delay in milliseconds between the steps of automatic falling, based on the current game speed.
	 * The delay is given by the time it takes for a piece to drop from its spawning point to the bottom of the board.
	 * This time for each level is: 10s, 6s, 3s, 1.5s, 1s, 0.4s.
	 * @return the delay in milliseconds between steps of falling
	 */
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
	
	/**
	 * Returns the current score of the game.
	 * @return the current score
	 */
	public int getScore() {
		return score;
	}
	
	/**
	 * Returns the number of lines cleared in the current level.
	 * @return the number of lines cleared in the current level
	 */
	public int getTotalLines() {
		return linesClearedTotal;
	}
	
	/**
	 * Returns the number of lines remaining to clear before the next level is reached.
	 * The number of lines to clear is 10 times the current game speed minus the number of lines cleared in the current level.
	 * The player can not progress further from level 6, so the number of lines to clear is always 1.
	 * @return the number of lines remaining to clear before the next level
	 */
	public int getLinesToNextLevel() {
		if (gameSpeed < 6) {
			return gameSpeed*10-linesClearedOnLevel;
		}
		else return 1;
	}

	/**
	 * Returns the current level.
	 * @return the current level
	 */
	public int getGameSpeed() {
		return gameSpeed;
	}
	
	/**
	 * Returns the height of the board in cells.
	 * @return the height of the board
	 */
	public int getBoardHeight() {
		return board.getHeight();
	}

	/**
	 * Returns the width of the board in cells.
	 * @return the width of the board
	 */
	public int getBoardWidth() {
		return board.getWidth();
	}
	
	/**
	 * Calls the draw functions of both the board and the current piece to draw the game state to the given Graphics object.
	 * @param g the Graphics object to draw on
	 * @param squareSize the size of each square in pixels
	 */
	public void drawTetris(Graphics g, int squareSize) {
		board.drawBoard(g, squareSize);
		piece.drawPiece(g, pieceX*squareSize, pieceY*squareSize, squareSize);		
	}

	/**
	 * Calls the draw function of the next piece, to draw it in a preview window.
	 * The preview window should be a 5x5 square, the drawing is done in a way where the piece is centered in the window.
	 * @param g the Graphics object to draw on
	 * @param squareSize the size of each square in pixels
	 */
	public void drawNextPiece(Graphics g, int squareSize) {
		if (nextPiece.isShape(Tetromino.Shape.O)) nextPiece.drawPiece(g, 3*squareSize/2, 3*squareSize/2, squareSize);
		else if (nextPiece.isShape(Tetromino.Shape.I)) nextPiece.drawPiece(g, 3*squareSize/2, 4*squareSize/2, squareSize);
		else nextPiece.drawPiece(g, 4*squareSize/2, 3*squareSize/2, squareSize);
	}
	
	/**
	 * Attempts to move the current piece to the given coordinates.
	 * If the piece can not be moved to the new position, nothing happens, and the function returns false.
	 * If the move is successful, the falling piece's origin coordinates are set to
	 * the given new origin coordinates, and the function returns true.
	 * @param newX the x-coordinate to move the piece to
	 * @param newY the y-coordinate to move the piece to
	 * @return true if the move was successful, false if the move can not be made
	 */
	private boolean tryMove(int newX, int newY) {
		if (board.collides(piece, newX, newY)) {
			return false;
		}
		pieceX = newX;
		pieceY = newY;
		return true;
	}
	
	/**
	 * Places the current piece on the board. If this results in the game ending, the function returns false.
	 * If the game continues, the function clears filled lines, then awards points based on how many lines were cleared at once,
	 * and whether or not the piece was dropped from a significant height.
	 * If the cleared lines result in the player reaching the next level, the function increments the level.
	 * Finally, the function generates a new piece to fall, and returns true.
	 * @param drop
	 * @return true if the game continues, false if the game is over
	 */
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
	
	/**
	 * Sets the current piece to the next piece, and pulls a new next piece from the pieceBag.
	 * The new piece is placed at the top of the board in the center. If the new piece can not start in this position
	 * because the stack is so high, then it is moved up into the hidden lines until this is no longer the case.
	 * This will take less than 4 iterations, as the piece is at most 4 cells tall, so 4 hidden lines are enough. 
	 */
	private void newPiece () {
		pieceX = board.getWidth()/2-1;
		pieceY = board.getHeight();
		piece = nextPiece;
		nextPiece = new Tetromino(pullFromPieceBag());
		
		while (board.collides(piece, pieceX, pieceY)) {
			pieceY++;
		}
	}

	/**
	 * Pulls the next piece from the pieceBag, creating a new shuffled order when all pieces have been pulled.
	 * @return the shape of the next piece to fall
	 */
	private Tetromino.Shape pullFromPieceBag () {
		if (pieceBag.isEmpty()) {
			initPieceBag();
		}
		return pieceBag.remove(0);
	}

	/**
	 * Initializes the pieceBag with a shuffled list of all Tetromino shapes.
	 */
	private void initPieceBag() {
		pieceBag = new ArrayList<>(List.of(Tetromino.Shape.values()));
		Collections.shuffle(pieceBag, randomSource);
	}
}
