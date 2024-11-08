package scores;

public class HighScore implements Comparable<HighScore> {
    private String name;
    private int score;
    
    public HighScore(String n, int s){
        name = n;
        score = s;
    }

    public String getName() {
        return name;
    }
    
    public int getScore() {
        return score;
    }
    
    public String toString() {
        if (name.length() == 0) return "<empty>";
        return name + score;
    }

    @Override
    public int compareTo(HighScore h) {
        return score - h.score;
    }
}
