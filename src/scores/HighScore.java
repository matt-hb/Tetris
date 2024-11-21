package scores;

/**
 * The HighScore class represents a high score with a name and a score.
 * It implements the Comparable<HighScore> interface to allow natural ordering by score.
 */
public class HighScore implements Comparable<HighScore> {
    /**
     * The name of the player who achieved the high score.
     */
    private String name;

    /**
     * The score achieved by the player.
     */
    private int score;

    /**
     * Constructs a new HighScore object with an empty name and a score of 0.
     */
    public HighScore(){
        name = "";
        score = 0;
    }
    
    /**
     * Constructs a new HighScore object with the specified name and score.
     * @param n the name of the player
     * @param s the score achieved by the player
     */
    public HighScore(String n, int s){
        name = n;
        score = s;
    }

    /**
     * Returns the name of the player who achieved the high score.
     * If the name is empty or the score is 0, returns "<empty>", as these scores don't need to be recorded.
     * @return the name of the player
     */
    public String getName() {
        if (name.length() == 0 || score == 0) return "<empty>";
        return name;
    }
    
    /**
     * Returns the score achieved by the player.
     * If the name is empty, returns 0, as these scores don't need to be recorded.
     * @return the score achieved by the player
     */
    public int getScore() {
        if (name.length() == 0) return 0;
        return score;
    }
    
    /**
     * Compares this HighScore object with another HighScore object by score.
     * @param h the HighScore object to compare to
     * @return a negative integer, zero, or a positive integer as this HighScore's score is less than, equal to, or greater than the specified HighScore's score
     */
    @Override
    public int compareTo(HighScore h) {
        return getScore() - h.getScore();
    }
}
