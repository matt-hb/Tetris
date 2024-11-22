package scores;

import java.io.*;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * The LeaderBoard class represents a leaderboard with a list of high scores.
 */
public class LeaderBoard {
    /**
     * A list of high scores in descending order.
     */
    private List<HighScore> lb;

    /**
     * Constructs a new LeaderBoard object with an empty list of high scores.
     */
    public LeaderBoard() {
        lb = new ArrayList<>();
    }

    /**
     * Constructs a new LeaderBoard object with the specified high scores, sorting them in descending order.
     * @param scores the high scores to add to the leaderboard
     */
    public LeaderBoard(HighScore... scores) {
        lb = new ArrayList<>(List.of(scores));
        Collections.sort(lb);
        Collections.reverse(lb);
    }

    /**
     * Returns the high score at the specified index in the leaderboard.
     * If the given index is higher than the number of high scores, returns an empty high score.
     * @param index the index of the high score to return
     * @return the high score at the specified index
     */
    public HighScore get(int index) {
        if (index >= lb.size()) return new HighScore();
        return lb.get(index);
    }

    /**
     * Adds a new high score to the leaderboard, sorting the list in descending order.
     * If the score is invalid, it is not added.
     * @param s the high score to add
     */
    public void add(HighScore s) {
        if (s.getScore() != 0) {
            lb.add(s);
            Collections.sort(lb);
            Collections.reverse(lb);
        }
    }

    /**
     * Returns the position the given score would have in the leaderboard, if added to it.
     * @param score the score to check
     * @return the position the score would have in the leaderboard
     */
    public int positionIfAdded(int score) {
        int pos = 0;
        while (pos < lb.size() && lb.get(pos).getScore() >= score) {
            pos++;
        }
        return (pos+1);
    }

    /**
     * Reads high scores from a JSON file and adds them to the leaderboard.
     * The file should only contain valid high scores, in descending order.
     * @param filename the name of the file to read from
     */
    public void readFromJSON(String filename) {
        Gson gson = new Gson();
        try  {
            FileReader reader = new FileReader(filename);
            HighScore[] t = gson.fromJson(reader, HighScore[].class);
            reader.close();
            if (t == null) return;
            lb = new ArrayList<>(Arrays.asList(t));
        } catch (IOException e) {
            System.err.println("Error reading scores");
        }
    }

    /**
     * Writes the high scores to a JSON file.
     * The file will contain the high scores in descending order.
     * @param filename the name of the file to write to
     */
    public void writeToJSON(String filename) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            FileWriter writer = new FileWriter(filename);
            writer.write(gson.toJson(lb));
            writer.close();
        } catch (IOException e) {
            System.err.println("Error saving scores");
        }
    }
}
