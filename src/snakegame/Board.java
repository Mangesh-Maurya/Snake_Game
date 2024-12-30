package snakegame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel implements ActionListener {

	private Image apple;
	private Image dot;
	private Image head;
	private int dots;
	private int score = 0; // live score field

	private final int ALL_DOTS = 900;
	private final int DOT_SIZE = 10;
	private final int RANDOM_POSITION = 29;

	private int apple_x;
	private int apple_y;

	private final int x[] = new int[ALL_DOTS];
	private final int y[] = new int[ALL_DOTS];

	private Boolean leftDirection = false;
	private Boolean rightDirection = true;
	private Boolean upDirection = false;
	private Boolean downDirection = false;

	private boolean inGame =true;
	
	private Timer timer;

	Board() {
		addKeyListener(new TAdapter());

		setBackground(Color.BLACK);
		setPreferredSize(new Dimension(300, 300));
		setFocusable(true);

		loadImages();
		initGame();

	}

	public void loadImages() {
		ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("snakegame/icons/apple.png"));
		apple = i1.getImage();
		ImageIcon i2 = new ImageIcon(ClassLoader.getSystemResource("snakegame/icons/dot.png"));
		dot = i2.getImage();
		ImageIcon i3 = new ImageIcon(ClassLoader.getSystemResource("snakegame/icons/head.png"));
		head = i3.getImage();

	}

	public void initGame() {
		dots = 3;

		for (int i = 0; i < dots; i++) {
			y[i] = 50;
			x[i] = 50 - i * DOT_SIZE;

		}

		locateApple();

		timer = new Timer(140, this);
		timer.start();
	}

	public void locateApple() {
		int r = (int) (Math.random() * RANDOM_POSITION);
		apple_x = r * DOT_SIZE;
		r = (int) (Math.random() * RANDOM_POSITION);
		apple_y = r * DOT_SIZE;

	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		draw(g);
	}

	public void draw(Graphics g) {
		if(inGame) {
			
			String msg = "Game Over!";
			Font font = new Font("SAN SERIF", Font.BOLD, 14);
			FontMetrics metrices = getFontMetrics(font);
			g.setColor(Color.WHITE);
			g.setFont(font);
			g.drawString("Score: " +score, 10, 20);//display score at the top
			g.drawImage(apple, apple_x, apple_y, this);
			
			for (int i = 0; i < dots; i++) {
				if (i == 0) {
					g.drawImage(head, x[i], y[i], this);
				} else {
					g.drawImage(dot, x[i], y[i], this);
				}
			}
	
			Toolkit.getDefaultToolkit().sync();
		}else {
			gameOver(g);
		}
	}
	
	public void gameOver(Graphics g) {
		String msg = "Game Over!";
		Font font = new Font("SAN SERIF", Font.BOLD, 14);
		FontMetrics metrices = getFontMetrics(font);
		g.setColor(Color.WHITE);
		g.setFont(font);
		g.drawString("Score: "+score, 10, 20);// display score at the top
		g.drawString(msg, (300 - metrices.stringWidth(msg))/2, 300/2);
	}

	public void move() {
		for (int i = dots; i > 0; i--) {
			x[i] = x[i - 1];
			y[i] = y[i - 1];
		}

		 if (leftDirection) {
			  x[0] = x[0] - DOT_SIZE;
		}
		 if (rightDirection) {
			 x[0] = x[0] + DOT_SIZE;
		 }
		 if (upDirection) {
			 y[0] = y[0] - DOT_SIZE;
		 }
		 if (downDirection) {
			 y[0] = y[0] + DOT_SIZE;
		 }


		//x[0] += DOT_SIZE;
		//y[0] += DOT_SIZE;
	}
	
	public void checkApple() {
		if((x[0] == apple_x)&&(y[0] == apple_y)) {
			dots++;
			score += 10; //increase score by 10 for each apple
			locateApple();
		}
	}
	
	public void checkCollision() {
		for(int i = dots; i > 0; i-- ) {
			if((i > 4)&&(x[0] == x[i]) &&(y[0] == y[i])) {
				inGame = false;
			}
		}
		if(y[0] >=300) {
			inGame = false;
		}
		if(x[0] >=300) {
			inGame = false;
		}
		if(y[0] <0) {
			inGame = false;
		}
		if(x[0] <0) {
			inGame = false;
		}
		
		if(!inGame) {
			timer.stop();
		}
		
	}
	
	public void actionPerformed(ActionEvent ae) {
		if(inGame) {
		checkApple();
		checkCollision();
		move();
		}

		repaint();
	}

	public class  TAdapter extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();

			if( key == KeyEvent.VK_LEFT &&(!rightDirection)) {
				leftDirection = true;
				upDirection = false;
				downDirection =false;
			}
			if( key == KeyEvent.VK_RIGHT &&(!leftDirection)) {
				rightDirection = true;
				upDirection = false;
				downDirection =false;
			}
			if( key == KeyEvent.VK_UP &&(!downDirection)) {
				leftDirection = false;
				upDirection = true;
				rightDirection =false;
			}
			if( key == KeyEvent.VK_DOWN &&(!upDirection)) {
				leftDirection = false;
				rightDirection = false;
				downDirection =true;
			}

		}

	}

}
