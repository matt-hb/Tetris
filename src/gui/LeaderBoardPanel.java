package gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.MatteBorder;

import scores.*;

public class LeaderBoardPanel extends JPanel {
    private TetrisApp frame;
    private LeaderBoard lb;

    public LeaderBoardPanel(TetrisApp p) {
        frame = p;
        lb = new LeaderBoard();
        setLayout(new BorderLayout());
        setBackground(frame.getBackground());
        initPanel();
    }

    public void addNewScore(String name, int score){
        lb.add(new HighScore(name, score));
    }

    public void refresh(){
        removeAll();
        initPanel();
    }

    public int positionIfAdded(int score){
        return lb.positionIfAdded(score);
    }

    private void initPanel() {
        JPanel entries = new JPanel();
        entries.setLayout(new GridBagLayout());
        entries.setBackground(getBackground());
        int entryHeight = frame.getWidth() * 8/10 / 10; // height of each entry
        int amountFits = (frame.getHeight() * 8/10) / entryHeight; // amount of entries that fit in the panel

        GridBagConstraints boardConstraints = new GridBagConstraints();
        boardConstraints.gridwidth = GridBagConstraints.REMAINDER;
		boardConstraints.anchor = GridBagConstraints.CENTER;
		boardConstraints.fill = GridBagConstraints.HORIZONTAL;

        int bw = Math.ceilDiv(entryHeight, 20);
        MatteBorder mb = BorderFactory.createMatteBorder(bw, bw, 3*bw, bw, Color.WHITE);
        
        GridBagConstraints entryConstraints = new GridBagConstraints();
        entryConstraints.gridwidth = 3;
        entryConstraints.gridheight = 1;
        entryConstraints.anchor = GridBagConstraints.CENTER;
        entryConstraints.fill = GridBagConstraints.BOTH;

        boardConstraints.weighty = 0.5;
        for (int i = 0; i < amountFits ; i++) {
            HighScore hs = i < lb.size() ? lb.get(i) : new HighScore();

            JPanel entry = new JPanel();
            entry.setLayout(new GridBagLayout());
            entryConstraints.weightx = 1;
            entry.add(new JLabel("<html><h2>#" + (i+1) + "</h2></html>", SwingConstants.CENTER), entryConstraints);
            entryConstraints.weightx = 6;
            entry.add(new JLabel(hs.getName(), SwingConstants.CENTER), entryConstraints);
            entryConstraints.weightx = 3;
            entry.add(new JLabel("" + hs.getScore(), SwingConstants.CENTER), entryConstraints);
            entry.setPreferredSize(new Dimension(10 * entryHeight, entryHeight));
            entry.setBorder(mb);
            entry.setBackground(getBackground().brighter());

            entries.add(entry, boardConstraints);
        }

        boardConstraints.weighty = 1;
        boardConstraints.fill = GridBagConstraints.NONE;
        JButton backButton = new JButton("<html><h1>Back</h1></html>");
        backButton.addActionListener(e -> frame.returnToMainMenu(TetrisApp.QUIT_FROM_LEADERBOARD));
        backButton.setBackground(getBackground().brighter());
        entries.add(backButton, boardConstraints);

        add(entries, BorderLayout.CENTER);
    }
}