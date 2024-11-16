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
        //TODO
    }

    public void writeToJSON(String filename) {
        //TODO
    }
}
