package scores;

public class HighScore implements Comparable<HighScore> {
    private String name;
    private int score;

    public HighScore(){
        name = "";
        score = 0;
    }
    
    public HighScore(String n, int s){
        name = n;
        score = s;
    }

    public String getName() {
        if (name.length() == 0 || score == 0) return "<empty>";
        return name;
    }
    
    public int getScore() {
        if (name.length() == 0) return 0;
        return score;
    }
    
    @Override
    public int compareTo(HighScore h) {
        return getScore() - h.getScore();
    }
}
