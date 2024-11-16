package scores;

import java.io.*;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class LeaderBoard {
    private List<HighScore> lb;

    public LeaderBoard() {
        lb = new ArrayList<>();
    }

    public LeaderBoard(HighScore... scores) {
        lb = new ArrayList<>(List.of(scores));
        Collections.sort(lb);
    }

    public HighScore get(int index) {
        return lb.get(index);
    }

    public void add(HighScore s) {
        if (s.getScore() != 0) {
            lb.add(s);
            Collections.sort(lb);
            Collections.reverse(lb);
        }
    }

    public int size() {
        return lb.size();
    }

    public int positionIfAdded(int score) {
        int pos = 0;
        while (pos < lb.size() && lb.get(pos).getScore() >= score) {
            pos++;
        }
        return (pos+1);
    }

    public void readFromJSON(String filename) {
        Gson gson = new Gson();
        try  {
            FileReader reader = new FileReader(filename);
            HighScore[] t = gson.fromJson(reader, HighScore[].class);
            lb = new ArrayList<>(Arrays.asList(t));
            reader.close();
        } catch (IOException e) {
            System.err.println("Error reading scores");
        }
    }

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
