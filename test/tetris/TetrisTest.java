package tetris;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Random;

/**
 * This class contains tests for the Tetris class.
 * As this class is fairly complicated, the tests are very rudimentary.
 */
public class TetrisTest {
    /**
     * The Tetris object used for testing.
     */
    private Tetris tetris;

    /**
     * Creates the Tetris object with the specified random seed, which was hand-picked for these tests.
     * The piece order given by the specified seed is also shown.
     */
    @BeforeEach
    public void setUp() {
        tetris = new Tetris(new Random(2));
        // piece order:
        // [L, J, S, Z, O, I, T]
        // [J, O, Z, T, L, S, I]
    }

    /**
     * Tests the correct initialization of the Tetris object.
     */
    @Test
    public void testIntialization() {
        assertEquals(0, tetris.getScore());
        assertEquals(0, tetris.getTotalLines());
        assertEquals(10, tetris.getLinesToNextLevel());
        assertEquals(1, tetris.getGameSpeed());
        assertEquals(417, tetris.getDelayInMillis());
    }

    /**
     * Tests getting a line clear by moving pieces side to side and then dropping them.
     */
    @Test
    public void testFirstLineClearWithDrop() {
        // L piece gets placed lying flat
        tetris.moveLeft(); tetris.drop();

        // J piece gets placed lying flat to the right of the L piece
        tetris.moveRight(); tetris.moveRight(); tetris.drop();

        // S piece gets placed to fill the 2 block gap on the left of the L piece
        tetris.moveLeft(); tetris.moveLeft(); tetris.moveLeft(); tetris.drop();

        // Z piece gets placed to fill the 2 block gap on the right of the J piece
        tetris.moveRight(); tetris.moveRight(); tetris.moveRight(); tetris.moveRight(); tetris.drop();

        // line is full, so score is added with max drop -> score=40*(1+5)=240
        assertEquals(240, tetris.getScore());
        assertEquals(1, tetris.getTotalLines());
        assertEquals(9, tetris.getLinesToNextLevel());
        assertEquals(1, tetris.getGameSpeed());
    }

    /**
     * Tests getting a line clear by moving pieces side to side but without dropping the last one, which gives less points.
     */
    @Test
    public void testFirstLineClearWithoutDrop() {
        // L piece gets placed lying flat
        tetris.moveLeft(); tetris.drop();

        // J piece gets placed lying flat next to the L piece
        tetris.moveRight(); tetris.moveRight(); tetris.drop();

        // S piece gets placed to fill the 2 block gap on the left of the L piece
        tetris.moveLeft(); tetris.moveLeft(); tetris.moveLeft(); tetris.drop();

        // Z piece gets moved to the right, needs to be dropped 24 times to get to the bottom
        tetris.moveRight(); tetris.moveRight(); tetris.moveRight(); tetris.moveRight();
        for (int i = 0; i < 24; i++){
            assertTrue(tetris.moveDown());
        }

        // line has not been cleared yet
        assertEquals(0, tetris.getTotalLines());

        // try to move it down again, but it lands, so line is cleared, but game continues
        assertTrue(tetris.moveDown());

        // line is full, so score is added with 0 drop -> score=40*(1+0)=40
        assertEquals(40, tetris.getScore());
        assertEquals(1, tetris.getTotalLines());
        assertEquals(9, tetris.getLinesToNextLevel());
        assertEquals(1, tetris.getGameSpeed());
    }

    /**
     * Tests dying in a simple way.
     */
    @Test
    public void testToppingOut() {
        // dropping the first 13 pieces fills up 23 blocks of vertical space, the next dropping piece is an I piece
        for (int i = 0; i < 13; i++) {
            tetris.drop();
        }

        // I piece spawns in 1st hidden row
        assertTrue(tetris.moveDown()); // gets moved down succesfully
        assertTrue(tetris.moveDown()); // gets placed in the 24th row, but the game continues

        // new piece is spawned in 1st hidden row 
        assertFalse(tetris.moveDown()); // gets placed in the 1st hidden row, game is over
    }

    /**
     * Tests dying which involves rotating pieces left.
     * (Un)fortunately, rotations are symmetric, so if rotating left and right do the same thing, this test can pass in an unintended way.
     */
    @Test
    public void testRotateLeftWithTopOut() {
        // the first 8 pieces rotated left then dropped fills up the entire 24 rows
        for (int i = 0; i < 8; i++) {
            tetris.rotateLeft();
            tetris.drop();
        }
        // thus, moving the next piece down should kill the game
        assertFalse(tetris.moveDown());
    }

    /**
     * Tests dying which involves rotating pieces right.
     * (Un)fortunately, rotations are symmetric, so if rotating left and right do the same thing, this test can pass in an unintended way.
     */
    @Test
    public void testRotateRightWithTopOut() {
        // the first piece is dropped
        tetris.drop();

        // after this, the next 8 pieces rotated right will fill up the entire 24 rows
        for (int i = 0; i < 8; i++) {
            tetris.rotateLeft();
            tetris.drop();
        }

        // thus, moving the next piece down should kill the game
        assertFalse(tetris.moveDown());
    }

    /**
     * Tests getting a line clear in a way that also involves rotating pieces.
     * Also tests whether the game can handle cleaning lines which are not at the bottom.
     */
    @Test
    public void testLineClearWithRotations() {
        // L piece gets placed flat side up
        tetris.rotateLeft(); tetris.rotateLeft();
        tetris.moveLeft(); tetris.drop();

        // J piece gets placed flat side up to the right of the L piece
        tetris.rotateRight(); tetris.rotateRight();
        tetris.moveRight(); tetris.moveRight(); tetris.drop();

        // S piece gets placed to fill the 2 block gap on the left of the L piece
        tetris.moveLeft(); tetris.moveLeft(); tetris.moveLeft(); tetris.drop();

        // Z piece gets placed to fill the 2 block gap on the right of the J piece
        tetris.moveRight(); tetris.moveRight(); tetris.moveRight(); tetris.moveRight(); tetris.drop();

        // line is full, so score is added with max drop -> score=40*(1+5)=240
        assertEquals(240, tetris.getScore());
        assertEquals(1, tetris.getTotalLines());
        assertEquals(9, tetris.getLinesToNextLevel());
        assertEquals(1, tetris.getGameSpeed());
    }
}