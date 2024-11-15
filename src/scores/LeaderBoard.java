package scores;

import java.util.*;

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
        lb.add(s);
        Collections.sort(lb);
    }

    public int size() {
        return lb.size();
    }

    public void readFromJSON(String filename) {
        //TODO
    }

    public void writeToJSON(String filename) {
        //TODO
    }
}
