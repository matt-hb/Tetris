package tetris;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.awt.Point;
import java.util.List;

/**
 * This class contains tests for the Board class.
 */
public class BoardTest {

    /**
     * The Board object that will be used for testing purposes.
     */
    private Board board;

    /**
     * A Tetromino piece which only consists of one mino, at its origin, used for testing.
     */
    private Tetromino singleMino;

    /*
     * Set up the objects before each test.
     */
    @BeforeEach
    public void setUp() {
        board = new Board();
        singleMino = new Tetromino(Tetromino.Shape.O);
        singleMino.setMinos(List.of(new Point(0, 0)));
    }

    /*
     * Test collision on every cell when the board is empty, should never collide.
     */
    @Test
    public void testCollisionOnEmptyBoard() {
        for (int y = 0; y < board.getHeight(); y++) {
            for (int x = 0; x < board.getWidth(); x++) {
                assertFalse(board.collides(singleMino, x, y));
            }
        }
    }

    /*
     * Test collision on the edges of the board, should always collide.
     * Top edge of the board is a special case, as it is extended to allow for pieces to spawn and board death to occur.
     */
    @Test
    public void testCollisionOnEdges() {
        for (int y = 0; y < board.getHeight(); y++) {
            assertTrue(board.collides(singleMino, -1, y));
            assertTrue(board.collides(singleMino, board.getWidth(), y));
        }
        for (int x = 0; x < board.getWidth(); x++) {
            assertTrue(board.collides(singleMino, x, -1));
        }
    }

    /*
     * I shaped piece's minos have relative coordinates of (-1,0), (0,0), (1,0), (2,0)
     * Place an I shaped piece on the board, then test collision on every cell of it, expecting true.
     * Also test collision on every cell around it, expecting false.
     */
    @Test
    public void testPlacement() {
        board.placePiece(new Tetromino(Tetromino.Shape.I), 2, 1);
        for (int i = 1; i <= 4; i++) {
            assertTrue(board.collides(singleMino, i, 1));
            assertFalse(board.collides(singleMino, i, 0));
            assertFalse(board.collides(singleMino, i, 2));
        }
        assertFalse(board.collides(singleMino, 0, 1));
        assertFalse(board.collides(singleMino, 5, 1));
    }

    /*
     * Test if the board is empty by default, then fill every cell of the board with single cells.
     * clearFilledLines() should clear as many lines as the height of the board.
     * Test if the board height changed, and if the board is empty again.
     */
    @Test
    public void testClearLines() {
        assertEquals(0, board.clearFilledLines());
        int initialHeight = board.getHeight();
        for (int y = 0; y < board.getHeight(); y++) {
            for (int x = 0; x < board.getWidth(); x++) {
                board.placePiece(singleMino, x, y);
            }
        }
        assertEquals(initialHeight, board.clearFilledLines());

        assertEquals(initialHeight, board.getHeight());
        assertEquals(0, board.clearFilledLines());
    }

    /*
     * Test if the board is dead by default, expecting false.
     * Place a piece on the top row of the board, then test if the board is dead, expecting true.
     */
    @Test
    public void testDeath() {
        assertFalse(board.isDead());
        board.placePiece(new Tetromino(Tetromino.Shape.O), 1, board.getHeight());
        assertTrue(board.isDead());
    }


}