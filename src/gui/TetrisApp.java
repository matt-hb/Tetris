package gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;

import com.formdev.flatlaf.FlatDarculaLaf;

/**
 * The TetrisApp class is the main class of the game.
 */
public class TetrisApp extends JFrame {
	/**
	 * The background color of the app.
	 */
	public static final Color BACKGROUND_COLOR = new Color(40, 42, 54).darker();

	/**
	 * The text color of the app.
	 */
	public static final Color TEXT_COLOR = new Color(248, 248, 242);

	/**
	 * The constant representing the player topping out (dying) in the game.
	 */
	public static final int TOPOUT = 0;

	/**
	 * The constant representing the player quitting from the game without dying.
	 */
	public static final int QUIT_FROM_GAME = 1;

	/**
	 * The constant representing the player returning from the leaderboard.
	 */
	public static final int QUIT_FROM_LEADERBOARD = 2;

	/**
	 * The width of the window.
	 */
	private static final int WINDOW_WIDTH = 600;

	/**
	 * The height of the window.
	 */
	private static final int WINDOW_HEIGHT = 900;

	/**
	 * The TetrisPanel object that allows the player to play the game.
	 */
	private TetrisPanel tetris;

	/**
	 * The LeaderBoardPanel object that displays the leaderboard.
	 */
	private LeaderBoardPanel leaderBoard;

	/**
	 * The Clip objects that play the menu music, game music, and game over sound.
	 */
	private Clip menuSong, gameSong, failSound;

	/**
	 * main method that initializes some UI settings and creates a new TetrisApp object.
	 * Look and feel is set to FlatLaf, from https://www.formdev.com/flatlaf/
	 */
	public static void main(String[] args) {
		// force enable hardware acceleration for smoother graphics
		// (fixes hitching on linux, enabled by default on windows)
		System.setProperty("sun.java2d.opengl", "true");

		// set the look and feel to FlatLaf
		// modify some UI settings to match the chosen background color and make text more legible
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
	
	/**
	 * Constructs a new TetrisApp object with the default window settings.
	 * Initializes the sounds used in the app.
	 * Creates the 3 main screens: the menu, the game, and the leaderboard,
	 * which are managed by a CardLayout, to allow easy switching based on their names.
	 * The default closing operation does nothing, so the app can handle it manually,
	 * which is done by using a WindowAdapter to call the close() method.
	 */
	public TetrisApp () {
		setTitle("Tetris HBM");
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		setResizable(false);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(new CardLayout());
		setBackground(BACKGROUND_COLOR);
		try {setIconImage(ImageIO.read(new File("asset/icon.png")));} catch (Exception e) {System.err.println("Error loading app icon");}

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

	/**
	 * Returns to the main menu screen with the given reason.
	 * TOPOUT: the player topped out (died) in the game.
	 * QUIT_FROM_GAME: the player quit from the game without dying.
	 * QUIT_FROM_LEADERBOARD: the player is returning from the leaderboard.
	 * @param reason the reason for returning to the main menu
	 */
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

	/**
	 * Changes the current screen to the one with the given name.
	 * @param name the name of the screen to change to
	 */
	private void changeToPage(String name){
		CardLayout cl = (CardLayout) getContentPane().getLayout();
		cl.show(getContentPane(), name);
	}

	/**
	 * If the result is valid, displays a dialog with the option to save the score to the leaderboard.
	 */
	private void processResults(){
		int score = tetris.getResults();
		if (score != 0) {
			String text = "Your score is " + score + ", that puts you at #" + leaderBoard.positionIfAdded(score) + " on the leaderboard!\nIf you want to save your score, enter your name.";
			String name = JOptionPane.showInputDialog(this, text, "Game Over!", JOptionPane.PLAIN_MESSAGE);
			if (name == null) name = "";
			leaderBoard.addNewScore(name.trim(), score);
		}
	}
	
	/**
	 * Creates the main menu screen with buttons to start the game, view the leaderboard, and exit the app.
	 * Also adds the title icon to the menu.
	 */
	private void makeMenuButtons() {
		JPanel menuButtons = new JPanel();
		menuButtons.setLayout(new GridBagLayout());
		menuButtons.setBackground(getBackground());
		
		// makes it easier to get consistent button sizes
		int buttonUnit = WINDOW_HEIGHT/20;
		
		// gridbagconstraints for the buttons, center and appear in a single column
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.HORIZONTAL;

		// add empty space above to make the title appear closer to the middle
		gbc.weighty = 0.8;
		menuButtons.add(Box.createRigidArea(new Dimension()), gbc);
		
		// add the title icon with the correct size
		// weight is set to 0.5 to make the title appear further from the buttons
		gbc.weighty = 0.5;
		ImageIcon titleImage = new ImageIcon("asset/title.png");
		JLabel title = new JLabel( new ImageIcon(titleImage.getImage().getScaledInstance(5* buttonUnit, buttonUnit, Image.SCALE_SMOOTH)));
		title.setPreferredSize(new Dimension(5*buttonUnit, buttonUnit));
		menuButtons.add(title, gbc);

		// add the start button, which starts the game and game music, and switches to the game screen
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
		menuButtons.add(startButton, gbc);
		
		// add the leaderboard button, which shows the leaderboard
		JButton rankingButton = new JButton("<html><h1>Leaderboards</h1></html>");
		rankingButton.addActionListener(e -> changeToPage("leaderboard"));
		rankingButton.setPreferredSize(new Dimension(5*buttonUnit, buttonUnit));
		menuButtons.add(rankingButton, gbc);
		
		// add the exit button, which closes the app
		JButton exitButton = new JButton("<html><h1>Exit</h1></html>");
		exitButton.addActionListener(e -> close());
		exitButton.setPreferredSize(new Dimension(5*buttonUnit, buttonUnit));
		menuButtons.add(exitButton, gbc);
		
		// add empty space below to make the buttons appear closer to the middle
		// weight is larger than the top spacer, so the menu is above the exact middle
		gbc.weighty = 1;
		menuButtons.add(Box.createRigidArea(new Dimension()), gbc);
		
		add(menuButtons, "menu");
	}

	/**
	 * Creates the game screen with the TetrisPanel that allows the player to play the game,
	 * and adds it to the layout manager.
	 */
	private void makeTetris() {
		tetris = new TetrisPanel(this);
		tetris.setPreferredSize(getSize());
		add(tetris, "game");
	}

	/**
	 * Creates the leaderboard screen with the LeaderBoardPanel that displays the leaderboard,
	 * and adds it to the layout manager.
	 */
	private void makeLeaderBoard() {
		leaderBoard = new LeaderBoardPanel(this);
		leaderBoard.setPreferredSize(getSize());
		leaderBoard.importScores("localScores.json");
		add(leaderBoard, "leaderboard");
	}

	/**
	 * Initializes the sounds used in the app.
	 */
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

	/**
	 * Stops (and frees resources for) the music clips, saves the leaderboard to a file, and exits the app.
	 */
	private void close() {
		menuSong.close();
		gameSong.close();
		failSound.close();
		leaderBoard.exportScores("localScores.json");
		System.exit(0);
	}
}
