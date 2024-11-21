package scores;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.*;

import java.io.*;

/**
 * Unit tests for the LeaderBoard class. Since HighScore doesn't have a .equals() method, we use unique names to compare them.
 */
public class LeaderBoardTest {
    /**
     * Test the constructor of the LeaderBoard class with an empty list of high scores, this means the top score is "<empty>".
     */
    @Test
    public void testConstructorEmpty() {
        LeaderBoard lb = new LeaderBoard();
        assertEquals("<empty>", lb.get(0).getName());
    }

    /**
     * Test the constructor of the LeaderBoard class with a list of high scores.
     * This means the high scores are sorted in descending order.
     */
    @Test
    public void testConstructorWithScores() {
        HighScore h1 = new HighScore("A", 100);
        HighScore h2 = new HighScore("B", 200);
        HighScore h3 = new HighScore("C", 300);
        LeaderBoard lb = new LeaderBoard(h1, h2, h3);
        assertEquals(h3.getName(), lb.get(0).getName());
        assertEquals(h2.getName(), lb.get(1).getName());
        assertEquals(h1.getName(), lb.get(2).getName());
    }

    /**
     * Test the get method of the LeaderBoard class.
     * It returns the high score at the specified index in the leaderboard.
     * If the given index is higher than the number of high scores, it returns an empty high score.
     */
    @Test
    public void testGet() {
        HighScore h1 = new HighScore("Sanyi", 100);
        LeaderBoard lb = new LeaderBoard(h1);
        assertEquals(h1.getName(), lb.get(0).getName());
        assertEquals("<empty>", lb.get(1).getName());
        assertEquals(0, lb.get(1).getScore());
    }

    /**
     * Test the add method of the LeaderBoard class with valid HighScore objects.
     * It adds the new score to the leaderboard, sorting the list in descending order.
     */
    @Test
    public void testAdd() {
        HighScore h1 = new HighScore("A", 100);
        HighScore h2 = new HighScore("B", 200);
        HighScore h3 = new HighScore("C", 300);
        LeaderBoard lb = new LeaderBoard(h1);
        lb.add(h2);
        assertEquals(h2.getName(), lb.get(0).getName());
        assertEquals(h1.getName(), lb.get(1).getName());
        lb.add(h3);
        assertEquals(h3.getName(), lb.get(0).getName());
        assertEquals(h2.getName(), lb.get(1).getName());
        assertEquals(h1.getName(), lb.get(2).getName());
    }

    /**
     * Test the add method of the LeaderBoard class with an invalid HighScore object.
     * It doesn't get added to the leaderboard.
     */
    @Test
    public void testAddInvalidScore() {
        HighScore h1 = new HighScore("Mr. Teszt", 0);
        LeaderBoard lb = new LeaderBoard();
        lb.add(h1);
        assertEquals("<empty>", lb.get(0).getName());
    }

    /**
     * Test the positionIfAdded method of the LeaderBoard class.
     * It returns the position the given score would have in the leaderboard, if added to it.
     * If the score ties with another score, it gets placed below it.
     * Invalid scores are "placed" at the end of the leaderboard.
     */
    @Test
    public void testPositionIfAdded() {
        HighScore h1 = new HighScore("A", 100);
        HighScore h2 = new HighScore("B", 200);
        LeaderBoard lb = new LeaderBoard(h1, h2);
        assertEquals(1, lb.positionIfAdded(1000));
        assertEquals(2, lb.positionIfAdded(200));
        assertEquals(2, lb.positionIfAdded(150));
        assertEquals(3, lb.positionIfAdded(100));
        assertEquals(3, lb.positionIfAdded(50));
        assertEquals(3, lb.positionIfAdded(0));
    }

    /**
     * Test the readFromJSON method of the LeaderBoard class using a temporary file.
     * A hand-crafted JSON string is written to the file, then read back, resulting in a correct LeaderBoard object.
     */
    @Test
    public void testReadFromJSON(@TempDir File tempDir) throws IOException {
        String examplejson = "[{\"name\":\"B\",\"score\":200},{\"name\":\"A\",\"score\":100}]";
        
        File tempFile = new File(tempDir, "test_scores.json");
        OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(tempFile));
        osw.write(examplejson);
        osw.close();

        LeaderBoard lb = new LeaderBoard();
        lb.readFromJSON(tempFile.toString());
        assertEquals("B", lb.get(0).getName());
        assertEquals(200, lb.get(0).getScore());
        assertEquals("A", lb.get(1).getName());
        assertEquals(100, lb.get(1).getScore());
    }

    /**
     * Test the writeToJSON method of the LeaderBoard class using a temporary file.
     * A LeaderBoard object is created, written to the file, then read back,
     * resulting in a LeaderBoard object with names and scores that match the original.
     * There is obviously other score added after the read, so the last score is "<empty>".
     */
    @Test
    public void testWriteAndReadToJSON(@TempDir File tempDir){
        HighScore h1 = new HighScore("Alice", 100);
        HighScore h2 = new HighScore("Bob", 200);
        LeaderBoard lb1 = new LeaderBoard(h1, h2);

        File tempFile = new File(tempDir, "test_scores.json");
        lb1.writeToJSON(tempFile.getPath());

        LeaderBoard lb2 = new LeaderBoard();
        lb2.readFromJSON(tempFile.getPath());
        assertEquals(h2.getName(), lb2.get(0).getName());
        assertEquals(h2.getScore(), lb2.get(0).getScore());
        assertEquals(h1.getName(), lb2.get(1).getName());
        assertEquals(h1.getScore(), lb2.get(1).getScore());
        assertEquals("<empty>", lb2.get(2).getName());
    }
}