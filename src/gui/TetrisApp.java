package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import com.formdev.flatlaf.FlatDarculaLaf;

public class TetrisApp extends JFrame {
	public static final int TOPOUT = 0;
	public static final int QUIT = 1;
	private static final int WINDOW_WIDTH = 600;
	private static final int WINDOW_HEIGHT = 900;
	private TetrisPanel tetris;

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(new FlatDarculaLaf());
		} catch (Exception e) { System.err.println("Failed to initialize FlatLaF"); }
		new TetrisApp();
	}
	
	public TetrisApp () {
		setTitle("Tetris HBM");
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		CardLayout cl = new CardLayout();
		this.setLayout(cl);
		
		makeMenuButtons();
		makeTetris();
		
		setVisible(true);
	}

	public void returnToMainMenu(int reason){
		if (reason == TOPOUT || reason == QUIT){
			if (reason == TOPOUT) {
				//TODO: Leaderboard processing
			}
			CardLayout cl = (CardLayout) getContentPane().getLayout();
			cl.show(getContentPane(), "menu");
			remove(tetris);
			makeTetris();
		}
	}
	
	private void makeTetris() {
		tetris = new TetrisPanel(this);
		tetris.setPreferredSize(getSize());
		add(tetris, "game");
		pack();
	}

	private void makeMenuButtons() {
		JPanel menuButtons = new JPanel();
		menuButtons.setLayout(new GridBagLayout());
		menuButtons.setBackground(new Color(40, 42, 54).darker());

		int buttonUnit = WINDOW_HEIGHT/20;
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		
		JButton startButton = new JButton("Start Tetris");
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CardLayout cl = (CardLayout) menuButtons.getParent().getLayout();
				cl.show(menuButtons.getParent(), "game");
				tetris.startGame();
			}
		});
		startButton.setAlignmentX(CENTER_ALIGNMENT);
		startButton.setPreferredSize(new Dimension(5*buttonUnit, buttonUnit));
		menuButtons.add(startButton, gbc);
		menuButtons.add(Box.createRigidArea(new Dimension(0, buttonUnit)), gbc);
		
		JButton rankingButton = new JButton("Leaderboards");
		rankingButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CardLayout cl = (CardLayout) menuButtons.getParent().getLayout();
				cl.show(menuButtons.getParent(), "leaderboard");
			}
		});
		rankingButton.setAlignmentX(CENTER_ALIGNMENT);
		rankingButton.setPreferredSize(new Dimension(5*buttonUnit, buttonUnit));
		menuButtons.add(rankingButton, gbc);
		menuButtons.add(Box.createRigidArea(new Dimension(0, buttonUnit)), gbc);
		
		JButton exitButton = new JButton("Exit");
		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		exitButton.setAlignmentX(CENTER_ALIGNMENT);
		exitButton.setPreferredSize(new Dimension(5*buttonUnit, buttonUnit));
		menuButtons.add(exitButton, gbc);
		
		add(menuButtons, "menu");
	}
}
