package gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.MatteBorder;

import scores.*;

/**
 * The LeaderBoardPanel class is responsible for displaying the leaderboard to the user.
 */
public class LeaderBoardPanel extends JPanel {
    /**
	 * The TetrisApp parent object that this panel is a part of.
	 */
    private TetrisApp frame;

    /**
     * The LeaderBoard object that stores the high scores to be shown in this panel.
     */
    private LeaderBoard lb;

    /**
     * Constructs a new LeaderBoardPanel with the given parent TetrisApp.
     * @param p the parent TetrisApp object
     */
    public LeaderBoardPanel(TetrisApp p) {
        frame = p;
        lb = new LeaderBoard();
        setLayout(new BorderLayout());
        setBackground(frame.getBackground());
    }

    /**
     * Imports high scores from a JSON file, and refreshes the panel to display them.
     * @param filename the name of the file to import from
     */
    public void importScores(String filename){
        lb.readFromJSON(filename);
        refresh();
    }

    /**
     * Exports high scores to a JSON file.
     * @param filename the name of the file to export to
     */
    public void exportScores(String filename){
        lb.writeToJSON(filename);
    }

    /**
     * Adds a new entry to the leaderboard, and refreshes the panel to display it.
     * @param name the name of the player
     * @param score the score of the player
     */
    public void addNewScore(String name, int score){
        lb.add(new HighScore(name, score));
        refresh();
    }

    /**
     * Refreshes the panel by reinitializing it.
     * Must be called whenever the entries are changed.
     */
    private void refresh(){
        removeAll();
        initPanel();
    }

    /**
     * Returns the position that the given score would be at if added to the leaderboard.
     * Interface for the LeaderBoard object.
     * @param score the score to check
     * @return the position the score would be at if added
     */
    public int positionIfAdded(int score){
        return lb.positionIfAdded(score);
    }

    /**
     * Initializes the panel with the current high scores.
     */
    private void initPanel() {
        JPanel entries = new JPanel();
        entries.setLayout(new GridBagLayout());
        entries.setBackground(getBackground());

        // width of each entry is 8/10 of the frame width ideally
        int entryWidth = frame.getWidth() * 8/10;
        // entries are rectangles with aspect ratio 10:1
        int entryHeight = entryWidth / 10;
        // amount of entries that fit in the panel, accounting for the back button
        int amountFits = (frame.getHeight() * 8/10) / entryHeight;

        // contraints the entries are added to the board with
        GridBagConstraints boardConstraints = new GridBagConstraints();
        boardConstraints.gridwidth = GridBagConstraints.REMAINDER;
		boardConstraints.anchor = GridBagConstraints.CENTER;
		boardConstraints.fill = GridBagConstraints.HORIZONTAL;
        
        // constraints the components of each entry are added with
        // each entry is a JPanel with 3 JLabels, rank, name and score
        GridBagConstraints entryConstraints = new GridBagConstraints();
        entryConstraints.gridwidth = 3;
        entryConstraints.gridheight = 1;
        entryConstraints.anchor = GridBagConstraints.CENTER;
        entryConstraints.fill = GridBagConstraints.BOTH;
        boardConstraints.weighty = 0.5;

        // border for the entries
        int bw = Math.ceilDiv(entryHeight, 20);
        MatteBorder mb = BorderFactory.createMatteBorder(bw, bw, 3*bw, bw, TetrisApp.TEXT_COLOR);

        // add each high score to the panel as an entry
        for (int i = 0; i < amountFits ; i++) {
            HighScore hs = lb.get(i);

            JPanel entry = new JPanel();
            entry.setLayout(new GridBagLayout());
            // rank, name, and score labels, weightX is set to make the name the largest
            entryConstraints.weightx = 1;
            entry.add(new JLabel("<html><h2>#" + (i+1) + "</h2></html>", SwingConstants.CENTER), entryConstraints);
            entryConstraints.weightx = 6;
            entry.add(new JLabel(hs.getName(), SwingConstants.CENTER), entryConstraints);
            entryConstraints.weightx = 3;
            entry.add(new JLabel(hs.getScore() + "     ", SwingConstants.TRAILING), entryConstraints);
            entry.setPreferredSize(new Dimension(10 * entryHeight, entryHeight));
            entry.setBorder(mb);
            entry.setBackground(getBackground().brighter());

            entries.add(entry, boardConstraints);
        }

        // back button to return to the main menu
        // weightY is increased so the button is spaced further from the entries
        boardConstraints.weighty = 1;
        boardConstraints.fill = GridBagConstraints.NONE;
        JButton backButton = new JButton("<html><h1>Back</h1></html>");
        backButton.addActionListener(e -> frame.returnToMainMenu(TetrisApp.QUIT_FROM_LEADERBOARD));
        entries.add(backButton, boardConstraints);

        add(entries, BorderLayout.CENTER);
    }
}