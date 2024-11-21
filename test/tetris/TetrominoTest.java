package tetris;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.awt.Color;
import java.awt.Point;
import java.util.List;

/**
 * This class contains tests for the creation and rotation of Tetromino shapes.
 * Each test verifies the color and the positions of the minos after the manipulation.
 */
public class TetrominoTest {

    /**
     * Test the creation of a piece, with the correct shape and color
     */
    @Test
    public void testTetrominoCreation() {
        Tetromino t = new Tetromino(Tetromino.Shape.L);
        assertTrue(t.isShape(Tetromino.Shape.L));
        assertEquals(new Color(255, 184, 108), t.getColor());
        assertEquals(List.of(new Point(-1,0), new Point(0,0), new Point(1,0), new Point(1,1)), t.getMinos());
    }

    /**
     * Mino locations:
     * [(-1,0) -> (0,-1)]
     * [(0,0) -> (0,0)]
     * [(1,0) -> (0,1)]
     * [(2,0) -> (0,2)]
     */
    @Test
    public void testTetrominoRotationLeftIShape() {
        Tetromino t = new Tetromino(Tetromino.Shape.I);
        Tetromino rotated = t.rotatedLeft();
        assertEquals(t.getColor(), rotated.getColor());
        assertEquals(List.of(new Point(0,-1), new Point(0,0), new Point(0,1), new Point(0,2)), rotated.getMinos());
    }

    /**
     * Mino locations:
     * [(-1,0) -> (0,1)]
     * [(0,0) -> (0,0)]
     * [(1,0) -> (0,-1)]
     * [(2,0) -> (0,-2)]
     */
    @Test
    public void testTetrominoRotationRightIShape() {
        Tetromino t = new Tetromino(Tetromino.Shape.I);
        Tetromino rotated = t.rotatedRight();
        assertEquals(t.getColor(), rotated.getColor());
        assertEquals(List.of(new Point(0,1), new Point(0,0), new Point(0,-1), new Point(0,-2)), rotated.getMinos());
    }

    /**
     * Mino locations:
     * [(-1,1) -> (-1,-1)]
     * [(-1,0) -> (0,-1)]
     * [(0,0) -> (0,0)]
     * [(1,0) -> (0,1)]
     */
    @Test
    public void testTetrominoRotationLeftJShape() {
        Tetromino t = new Tetromino(Tetromino.Shape.J);
        Tetromino rotated = t.rotatedLeft();
        assertEquals(t.getColor(), rotated.getColor());
        assertEquals(List.of(new Point(-1,-1), new Point(0,-1), new Point(0,0), new Point(0,1)), rotated.getMinos());
    }

    /**
     * Mino locations:
     * [(-1,1) -> (1,1)]
     * [(-1,0) -> (0,1)]
     * [(0,0) -> (0,0)]
     * [(1,0) -> (0,-1)]
     */
    @Test
    public void testTetrominoRotationRightJShape() {
        Tetromino t = new Tetromino(Tetromino.Shape.J);
        Tetromino rotated = t.rotatedRight();
        assertEquals(t.getColor(), rotated.getColor());
        assertEquals(List.of(new Point(1,1), new Point(0,1), new Point(0,0), new Point(0,-1)), rotated.getMinos());
    }

    /**
     * Mino locations:
     * [(-1,1) -> (-1,-1)]
     * [(0,1) -> (-1,0)]
     * [(0,0) -> (0,0)]
     * [(1,0) -> (0,1)]
     */
    @Test
    public void testTetrominoRotationLeftZShape() {
        Tetromino t = new Tetromino(Tetromino.Shape.Z);
        Tetromino rotated = t.rotatedLeft();
        assertEquals(t.getColor(), rotated.getColor());
        assertEquals(List.of(new Point(-1,-1), new Point(-1,0), new Point(0,0), new Point(0,1)), rotated.getMinos());
    }

    /**
     * Mino locations:
     * [(-1,1) -> (1,1)]
     * [(0,1) -> (1,0)]
     * [(0,0) -> (0,0)]
     * [(1,0) -> (0,-1)]
     */
    @Test
    public void testTetrominoRotationRightZShape() {
        Tetromino t = new Tetromino(Tetromino.Shape.Z);
        Tetromino rotated = t.rotatedRight();
        assertEquals(t.getColor(), rotated.getColor());
        assertEquals(List.of(new Point(1,1), new Point(1,0), new Point(0,-0), new Point(0,-1)), rotated.getMinos());
    }

    /**
     * If the shape of a Tetromino is O, 
     * rotatedLeft() and rotatedRight() should return the same Tetromino
     */
    @Test
    public void testTetrominoRotationLeftOShape() {
        Tetromino t = new Tetromino(Tetromino.Shape.O);
        Tetromino rotated = t.rotatedLeft();
        assertEquals(t.getColor(), rotated.getColor());
        assertEquals(t.getMinos(), rotated.getMinos());
    }

    /**
     * If the shape of a Tetromino is O, 
     * rotatedLeft() and rotatedRight() should return the same Tetromino
     */
    @Test
    public void testTetrominoRotationRightOShape() {
        Tetromino t = new Tetromino(Tetromino.Shape.O);
        Tetromino rotated = t.rotatedRight();
        assertEquals(t.getColor(), rotated.getColor());
        assertEquals(t.getMinos(), rotated.getMinos());
    }
}