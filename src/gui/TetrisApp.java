package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TetrisApp extends JFrame {
	public static void main(String[] args) {
		new TetrisApp();
	}
	
	public TetrisApp () {
		setTitle("Tetris HBM");
		setSize(1600, 900);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		
		makeMenuButtons();
		
		setVisible(true);
	}
	
	private void startGame() {
		TetrisPanel tetris = new TetrisPanel();
		tetris.setPreferredSize(getSize());
		add(tetris);
		tetris.setVisible(true);
		tetris.requestFocusInWindow();
		pack();
		tetris.startGame();
	}

	private void makeMenuButtons() {
		JPanel menuButtons = new JPanel();
		menuButtons.setLayout(new BoxLayout(menuButtons, BoxLayout.Y_AXIS));
		menuButtons.setBackground(new Color(40, 42, 54).darker());
		
		JButton startButton = new JButton("Start Tetris");
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				menuButtons.setVisible(false);
				startGame();
			}
		});
		startButton.setAlignmentX(CENTER_ALIGNMENT);
		menuButtons.add(startButton);
		menuButtons.add(Box.createRigidArea(new Dimension(0, 30)));
		
		JButton rankingButton = new JButton("Leaderboards");
		rankingButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				menuButtons.setVisible(false);
			}
		});
		rankingButton.setAlignmentX(CENTER_ALIGNMENT);
		menuButtons.add(rankingButton);
		menuButtons.add(Box.createRigidArea(new Dimension(0, 30)));
		
		JButton exitButton = new JButton("Exit");
		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		exitButton.setAlignmentX(CENTER_ALIGNMENT);
		menuButtons.add(exitButton);
		
		add(menuButtons, BorderLayout.CENTER);
	}
}
