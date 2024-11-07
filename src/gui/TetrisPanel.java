package gui;

import tetris.Tetris;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

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
		
		score = new JLabel("Score:");
		linesTotal = new JLabel("Lines cleared:");
		linesRemaining = new JLabel("Lines until next level:");
		score.setForeground(Color.WHITE);
		linesTotal.setForeground(Color.WHITE);
		linesRemaining.setForeground(Color.WHITE);
		add(score);
		add(linesTotal);
		add(linesRemaining);
		
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
		squareSize = Math.min(this.getHeight() / tetris.getBoard().getHeight(), this.getWidth() / tetris.getBoard().getWidth());
		
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
		score.setText("Score: " + tetris.getScore());
		linesTotal.setText("Lines cleared: " + tetris.getTotalLines());
		linesRemaining.setText("Lines until next level: " + tetris.getLinesToNextLevel());
		repaint(0,0,getWidth(),getHeight());
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
}
