package scores;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HighScoreTest {

    @Test
    public void testEmpty() {
        HighScore h = new HighScore();
        assertEquals("<empty>", h.getName());
        assertEquals(0, h.getScore());
    }

    @Test
    public void testValid() {
        HighScore h = new HighScore("Mr. Teszt", 100);
        assertEquals("Mr. Teszt", h.getName());
        assertEquals(100, h.getScore());
    }

    @Test
    public void testEmptyName() {
        HighScore h = new HighScore("", 100);
        assertEquals("<empty>", h.getName());
        assertEquals(0, h.getScore());
    }

    @Test
    public void testZeroScore() {
        HighScore h = new HighScore("Sanyi", 0);
        assertEquals("<empty>", h.getName());
        assertEquals(0, h.getScore());
    }

    @Test
    public void testCompareTo() {
        HighScore h1 = new HighScore("Rossz Gamer", 100);
        HighScore h2 = new HighScore("Pro Gamer", 200);
        assertTrue(h1.compareTo(h2) < 0);
        assertTrue(h2.compareTo(h1) > 0);
        assertEquals(0, h1.compareTo(new HighScore("Rossz Gamer 2", 100)));
    }
}