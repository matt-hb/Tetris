package gui;

import tetris.Tetris;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;

import java.util.List;

public class TetrisPanel extends JPanel implements ActionListener {
	private TetrisApp parent;
	private Tetris tetris;
	private int squareSize;
	private Timer timer;
	private JLabel score;
	private JLabel linesTotal;
	private JLabel linesRemaining;
	
	public TetrisPanel(TetrisApp p) {
		parent = p;
		tetris = new Tetris();
		setFocusable(true);
		setLayout(new BorderLayout());
		squareSize = Math.min(parent.getHeight() / tetris.getBoard().getHeight(), parent.getWidth() / tetris.getBoard().getWidth());

		initInfoPanel();
		
		addKeyListener(new TetrisListener());
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		setBackground(new Color(40, 42, 54).darker());		
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.translate(0, this.getHeight());
		g2d.scale(1, -1);
		tetris.drawTetris(g2d, squareSize);
	}
	
	public void startGame() {		
		timer = new Timer(50, this);
		timer.setDelay(tetris.getDelayInMillis());
		timer.start();
		requestFocusInWindow();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (!tetris.moveDown()) {
			timer.stop();
			parent.returnToMainMenu(TetrisApp.TOPOUT);
		}
		timer.setDelay(tetris.getDelayInMillis());
		score.setText("<html><b>Score: " + tetris.getScore() + "</b></html>");
		linesTotal.setText("<html><b>Lines cleared: " + tetris.getTotalLines() + "</b></html>");
		linesRemaining.setText("<html><b>Lines until next level: " + tetris.getLinesToNextLevel() + "</b></html>");
		repaint(0,0,getWidth(),getHeight());
	}

	private void initInfoPanel(){
		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new GridBagLayout());
		infoPanel.setBackground(new Color(0,0,0,0));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weighty = 0.2;

		score = new JLabel("", SwingConstants.CENTER);
		linesTotal = new JLabel("", SwingConstants.CENTER);
		linesRemaining = new JLabel("", SwingConstants.CENTER);
		MatteBorder mb = BorderFactory.createMatteBorder(Math.floorDiv(squareSize, 10), Math.floorDiv(squareSize, 10), 3*Math.floorDiv(squareSize, 10), Math.floorDiv(squareSize, 10), Color.WHITE);
		for (JLabel label : List.of(score,linesTotal,linesRemaining)){
			label.setForeground(Color.WHITE);
			label.setPreferredSize(new Dimension(4*squareSize, 3*squareSize/2));
			label.setBorder(mb);
			infoPanel.add(label, gbc);
		}

		gbc.weighty = 10;
		NextPiecePanel next = new NextPiecePanel();
		next.setBackground(new Color(0,0,0,0));
		TitledBorder tb = BorderFactory.createTitledBorder(mb, "<html><b>Next Piece:</b></html>");
		tb.setTitleColor(Color.WHITE);
		next.setBorder(tb);
		next.setPreferredSize(new Dimension(4*squareSize, 4*squareSize));
		infoPanel.add(next, gbc);

		infoPanel.add(Box.createRigidArea(new Dimension()), gbc);

		gbc.weighty = 0.1;
		JButton backButton = new JButton("Back");
		backButton.addActionListener(e -> parent.returnToMainMenu(TetrisApp.QUIT));
		backButton.setPreferredSize(new Dimension(4*squareSize, 3*squareSize/2));
		infoPanel.add(backButton, gbc);

		add(infoPanel, BorderLayout.EAST);
	}
	
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

	private class NextPiecePanel extends JPanel {
		@Override
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			tetris.drawNextPiece(g, squareSize);
		}
	}
}
