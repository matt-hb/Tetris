package gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import javax.sound.sampled.*;

import com.formdev.flatlaf.FlatDarculaLaf;

public class TetrisApp extends JFrame {
	public static final Color BACKGROUND_COLOR = new Color(40, 42, 54).darker();
	public static final Color TEXT_COLOR = new Color(248, 248, 242);

	public static final int TOPOUT = 0;
	public static final int QUIT_FROM_GAME = 1;
	public static final int QUIT_FROM_LEADERBOARD = 2;

	private static final int WINDOW_WIDTH = 600;
	private static final int WINDOW_HEIGHT = 900;

	private TetrisPanel tetris;
	private LeaderBoardPanel leaderBoard;
	private Clip menuSong;
	private Clip gameSong;
	private Clip failSound;

	public static void main(String[] args) {
		System.setProperty("sun.java2d.opengl", "true");

		try {
			UIManager.setLookAndFeel(new FlatDarculaLaf());
			UIManager.put("OptionPane.background", BACKGROUND_COLOR);
			UIManager.put("TextField.background", BACKGROUND_COLOR.brighter());
			UIManager.put("Button.background", BACKGROUND_COLOR.brighter());
			UIManager.put("TextField.foreground", TEXT_COLOR);
			UIManager.put("Button.foreground", TEXT_COLOR);
			UIManager.put("Label.foreground", TEXT_COLOR);
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
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(new CardLayout());
		setBackground(BACKGROUND_COLOR);

		initSounds();
		
		makeMenuButtons();
		makeTetris();
		makeLeaderBoard();

		changeToPage("menu");

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				close();
			}
		});
		
		setVisible(true);
		menuSong.loop(Clip.LOOP_CONTINUOUSLY);
	}

	public void returnToMainMenu(int reason){
		if (reason == TOPOUT || reason == QUIT_FROM_GAME){
			if (gameSong.isRunning()) gameSong.stop();
			if (reason == TOPOUT) {
				failSound.setMicrosecondPosition(0);
				failSound.start();
				processResults();
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

	private void processResults(){
		int score = tetris.getResults();
		if (score != 0) {
			String text = "Your score is " + score + ", that puts you at #" + leaderBoard.positionIfAdded(score) + " on the leaderboard!\nIf you want to save your score, enter your name.";
			String name = JOptionPane.showInputDialog(this, text, "Game Over!", JOptionPane.PLAIN_MESSAGE);
			if (name == null) name = "";
			leaderBoard.addNewScore(name.trim(), score);
			leaderBoard.refresh();
		}
	}
	
	private void makeMenuButtons() {
		JPanel menuButtons = new JPanel();
		menuButtons.setLayout(new GridBagLayout());
		menuButtons.setBackground(this.getBackground());
		
		int buttonUnit = WINDOW_HEIGHT/20;
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.HORIZONTAL;

		gbc.weighty = 0.8;
		menuButtons.add(Box.createRigidArea(new Dimension()), gbc);
		
		gbc.weighty = 0.5;
		ImageIcon titleImage = new ImageIcon("asset/title.png");
		JLabel title = new JLabel( new ImageIcon(titleImage.getImage().getScaledInstance(5* buttonUnit, buttonUnit, Image.SCALE_SMOOTH)));
		title.setPreferredSize(new Dimension(5*buttonUnit, buttonUnit));
		menuButtons.add(title, gbc);

		gbc.weighty = 0.1;
		JButton startButton = new JButton("<html><h1>Start Tetris</h1></html>");
		startButton.addActionListener(e -> {
			changeToPage("game");
			tetris.startGame();
			if (menuSong.isRunning()) menuSong.stop();
			gameSong.setMicrosecondPosition(0);
			gameSong.loop(Clip.LOOP_CONTINUOUSLY);
		}
		);
		startButton.setPreferredSize(new Dimension(5*buttonUnit, buttonUnit));
		startButton.setBackground(getBackground().brighter());
		menuButtons.add(startButton, gbc);
		
		JButton rankingButton = new JButton("<html><h1>Leaderboards</h1></html>");
		rankingButton.addActionListener(e -> changeToPage("leaderboard"));
		rankingButton.setPreferredSize(new Dimension(5*buttonUnit, buttonUnit));
		rankingButton.setBackground(getBackground().brighter());
		menuButtons.add(rankingButton, gbc);
		
		JButton exitButton = new JButton("<html><h1>Exit</h1></html>");
		exitButton.addActionListener(e -> close());
		exitButton.setPreferredSize(new Dimension(5*buttonUnit, buttonUnit));
		exitButton.setBackground(getBackground().brighter());
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
		leaderBoard = new LeaderBoardPanel(this);
		leaderBoard.setPreferredSize(getSize());
		leaderBoard.importScores("localScores.json");
		leaderBoard.refresh();
		add(leaderBoard, "leaderboard");
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

	private void close() {
		menuSong.close();
		gameSong.close();
		failSound.close();
		leaderBoard.exportScores("localScores.json");
		System.exit(0);
	}
}
