package gui;

import javax.swing.*;
import java.awt.*;
import java.io.File;

import com.formdev.flatlaf.FlatDarculaLaf;
import javax.sound.sampled.*;

public class TetrisApp extends JFrame {
	public static final int TOPOUT = 0;
	public static final int QUIT_FROM_GAME = 1;
	private static final int WINDOW_WIDTH = 600;
	private static final int WINDOW_HEIGHT = 900;
	private TetrisPanel tetris;
	private Clip menuSong;
	private Clip gameSong;
	private Clip failSound;

	public static void main(String[] args) {
		System.setProperty("sun.java2d.opengl", "true");
		try {
			UIManager.setLookAndFeel(new FlatDarculaLaf());
		} catch (Exception e) { 
			System.err.println("Failed to initialize FlatLaF, using system default"); 
			try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) { /*ignored*/ }
		}
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
		makeLeaderBoard();
		
		initSounds();
		
		setVisible(true);
		menuSong.loop(Clip.LOOP_CONTINUOUSLY);
	}

	public void returnToMainMenu(int reason){
		if (reason == TOPOUT || reason == QUIT_FROM_GAME){
			if (gameSong.isRunning()) gameSong.stop();
			if (reason == TOPOUT) {
				failSound.setMicrosecondPosition(0);
				failSound.start();
				try { Thread.sleep(2000); } catch(InterruptedException e) { /*just wait it out*/ }
				//TODO
			}
			remove(tetris);
			makeTetris();
			menuSong.setMicrosecondPosition(0);
			menuSong.loop(Clip.LOOP_CONTINUOUSLY);
		}
		changeToPage("menu");
	}

	private void changeToPage(String name){
		CardLayout cl = (CardLayout) getContentPane().getLayout();
		cl.show(getContentPane(), name);
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
		gbc.weighty = 1;
		menuButtons.add(Box.createRigidArea(new Dimension()), gbc);
		
		gbc.weighty = 0.1;
		JButton startButton = new JButton("Start Tetris");
		startButton.addActionListener(e -> {
			changeToPage("game");
			tetris.startGame();
			if (menuSong.isRunning()) menuSong.stop();
			gameSong.setMicrosecondPosition(0);
			gameSong.loop(Clip.LOOP_CONTINUOUSLY);
		}
		);
		startButton.setAlignmentX(CENTER_ALIGNMENT);
		startButton.setPreferredSize(new Dimension(5*buttonUnit, buttonUnit));
		menuButtons.add(startButton, gbc);
		
		JButton rankingButton = new JButton("Leaderboards");
		rankingButton.addActionListener(e -> changeToPage("leaderboard"));
		rankingButton.setAlignmentX(CENTER_ALIGNMENT);
		rankingButton.setPreferredSize(new Dimension(5*buttonUnit, buttonUnit));
		menuButtons.add(rankingButton, gbc);
		
		JButton exitButton = new JButton("Exit");
		exitButton.addActionListener(e -> System.exit(0));
		exitButton.setAlignmentX(CENTER_ALIGNMENT);
		exitButton.setPreferredSize(new Dimension(5*buttonUnit, buttonUnit));
		menuButtons.add(exitButton, gbc);
		
		gbc.weighty = 1;
		menuButtons.add(Box.createRigidArea(new Dimension()), gbc);
		
		add(menuButtons, "menu");
	}

	private void makeTetris() {
		tetris = new TetrisPanel(this);
		tetris.setPreferredSize(getSize());
		add(tetris, "game");
		pack();
	}

	private void makeLeaderBoard() {
		//TODO
	}

	private void initSounds() {
		try {
			menuSong = AudioSystem.getClip();
			menuSong.open(AudioSystem.getAudioInputStream(new File("asset/menu.wav")));

			gameSong = AudioSystem.getClip();
			gameSong.open(AudioSystem.getAudioInputStream(new File("asset/ingame.wav")));

			failSound = AudioSystem.getClip();
			failSound.open(AudioSystem.getAudioInputStream(new File("asset/gameover.wav")));
		} catch (Exception e) {
			System.err.println("Error initializing music");
		}
	}
}
