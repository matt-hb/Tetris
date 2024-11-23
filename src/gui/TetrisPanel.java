package gui;

import tetris.Tetris;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import java.util.List;

/**
 * The TetrisPanel class is responsible for interfacing between the game and the user.
 * It runs the game itself through a Tetris object and collects inputs from the player.
 */
public class TetrisPanel extends JPanel implements ActionListener {
	/**
	 * The TetrisApp parent object that this panel is a part of.
	 */
	private TetrisApp frame;

	/**
	 * The Tetris instance that represents the game logic.
	 */
	private Tetris tetris;

	/**
	 * The size of each cell in the game board, also used to size other labels and panels.
	 * Calculated based on the size of the frame and the size of the game board.
	 */
	private int squareSize;

	/**
	 * The timer that controls the speed of the game.
	 */
	private Timer timer;

	/**
	 * Labels that display the current score, total lines cleared, lines until next level, and current level.
	 */
	private JLabel score, linesTotal, linesRemaining, level;
	
	/**
	 * Constructs a new TetrisPanel with the given parent TetrisApp.
	 * @param p the parent TetrisApp object
	 */
	public TetrisPanel(TetrisApp p) {
		frame = p;
		tetris = new Tetris();
		setFocusable(true);
		setLayout(new BorderLayout());
		setBackground(frame.getBackground());
		
		addKeyListener(new TetrisListener());
	}
	
	/**
	 * Calculates squareSize and initializes the game board and info panels using it.
	 * Starts the game timer and sets the focus to the panel to be able to receive key inputs.
	 * The ActionListener for the timer is this panel itself.
	 */
	public void startGame() {
		squareSize = Math.min((frame.getHeight() - frame.getInsets().top) / tetris.getBoardHeight(), frame.getWidth() / tetris.getBoardWidth());

		initBoardPanel();
		initInfoPanel();

		timer = new Timer(tetris.getDelayInMillis(), this);
		timer.start();

		requestFocusInWindow();
	}
	
	/**
	 * The main game loop that runs the game logic and updates the display.
	 * Tries moving the current piece down, if moveDown() returns false, we know the game is over,
	 * so it stops the timer and returns to the main menu with the TOPOUT flag.
	 * Updates the timer delay and all the info labels with the current game state each tick.
	 * Calls the repaint() method to make sure the board and next piece is updated.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// false value means the game is over
		if (!tetris.moveDown()) {
			timer.stop();
			frame.returnToMainMenu(TetrisApp.TOPOUT);
		}
		timer.setDelay(tetris.getDelayInMillis());
		score.setText("<html><b>Score: " + tetris.getScore() + "</b></html>");
		linesTotal.setText("<html><b>Lines cleared: " + tetris.getTotalLines() + "</b></html>");
		linesRemaining.setText("<html><b>Lines until next level: " + tetris.getLinesToNextLevel() + "</b></html>");
		level.setText("<html><b>Current level: " + tetris.getGameSpeed() + "</b></html>");
		repaint();
	}

	/**
	 * Returns the end results of the game.
	 * @return the score of the game
	 */
	public int getResults() {
		return tetris.getScore();
	}

	/**
	 * Initializes the game board panel and positions it on the left side of the panel.
	 */
	private void initBoardPanel() {
		BoardPanel boardPanel = new BoardPanel();
		boardPanel.setPreferredSize(new Dimension(tetris.getBoardWidth() * squareSize, tetris.getBoardHeight() * squareSize));
		boardPanel.setBackground(getBackground());
		add(boardPanel, BorderLayout.WEST);
	}

	/**
	 * Initializes the info panel on the right side of the panel.
	 * Contains labels for the score, total lines cleared, lines until next level, and current level,
	 * and a button to return to the main menu without finishing the game.
	 * Also contains a panel for the next piece to be displayed.
	 * The back button returns to the main menu with the QUIT_FROM_GAME flag.
	 */
	private void initInfoPanel(){
		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new GridBagLayout());
		infoPanel.setBackground(frame.getBackground());

		// GridBagConstraints that ensure the components are in a single column with equal width.
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weighty = 0.2;

		score = new JLabel("", SwingConstants.CENTER);
		linesTotal = new JLabel("", SwingConstants.CENTER);
		linesRemaining = new JLabel("", SwingConstants.CENTER);
		level = new JLabel("", SwingConstants.CENTER);

		// create border for labels
		int bw = Math.floorDiv(squareSize, 10);
		MatteBorder mb = BorderFactory.createMatteBorder(bw, bw, 3*bw, bw, TetrisApp.TEXT_COLOR);

		// set preferred size and border for each label
		for (JLabel label : List.of(score, linesTotal, linesRemaining, level)){
			label.setPreferredSize(new Dimension(4*squareSize, 3*squareSize/2));
			label.setBorder(mb);
			infoPanel.add(label, gbc);
		}

		// add a panel for the next piece, with a titled border and larger weightY to take up more vertical space
		gbc.weighty = 10;
		NextPiecePanel next = new NextPiecePanel();
		next.setBackground(new Color(0,0,0,0));
		TitledBorder tb = BorderFactory.createTitledBorder(mb, "<html><b>Next Piece:</b></html>");
		tb.setTitleColor(TetrisApp.TEXT_COLOR);
		next.setBorder(tb);
		next.setPreferredSize(new Dimension(5*squareSize, 5*squareSize));
		infoPanel.add(next, gbc);

		// add rigid area to push the back button to the bottom
		infoPanel.add(Box.createRigidArea(new Dimension()), gbc);

		// add back button to return to main menu with really small weightY to make sure it's at the bottom
		gbc.weighty = 0.1;
		JButton backButton = new JButton("Back");
		backButton.addActionListener(e -> frame.returnToMainMenu(TetrisApp.QUIT_FROM_GAME));
		backButton.setPreferredSize(new Dimension(4*squareSize, 3*squareSize/2));
		infoPanel.add(backButton, gbc);

		add(infoPanel, BorderLayout.EAST);
	}
	
	/**
	 * The TetrisListener class is responsible for listening to key inputs from the player.
	 * It listens for the arrow keys to move the piece left and right, the A and D keys to rotate the piece,
	 * and the space key to drop the piece to the bottom of the board.
	 * Each key press calls the corresponding method in the Tetris object and repaints the panel,
	 * so the movements feel responsive and not delayed.
	 */
	private class TetrisListener extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT -> tetris.moveLeft();
			case KeyEvent.VK_RIGHT -> tetris.moveRight();
			case KeyEvent.VK_A -> tetris.rotateLeft();
			case KeyEvent.VK_D -> tetris.rotateRight();
			case KeyEvent.VK_SPACE -> tetris.drop();
			}
			repaint();
		}
	}

	/**
	 * The NextPiecePanel class is responsible for displaying the next piece to be played.
	 * It draws the next piece in the center of the panel, scaled to fit the panel size.
	 * Ideal size for the panel is 5x5 squares, so the piece is drawn in the center of the panel.
	 */
	private class NextPiecePanel extends JPanel {
		@Override
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g.create();
			// flip the y-axis to draw the piece correctly
			g2d.translate(0, this.getHeight());
			g2d.scale(1, -1);
			tetris.drawNextPiece(g2d, squareSize);
		}
	}

	/**
	 * The BoardPanel class is responsible for displaying the game board and falling piece.
	 * It also draws a background-colored rectangle to hide pieces falling in from the hidden lines above the board.
	 */
	private class BoardPanel extends JPanel {
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g.create();
			// flip the y-axis to draw the board correctly
			g2d.translate(0, this.getHeight());
			g2d.scale(1, -1);
			tetris.drawTetris(g2d, squareSize);
			
			// draw a background-colored rectangle to hide pieces falling in from the hidden lines above the board
			g2d.setColor(getBackground());
			g2d.fillRect(0, tetris.getBoardHeight()*squareSize, tetris.getBoardWidth() * squareSize, getHeight() - tetris.getBoardHeight()*squareSize);
		}
	}
}
