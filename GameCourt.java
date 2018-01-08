/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.0, Mar 2013
 */

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

import javax.imageio.ImageIO;
import javax.swing.*;

public class GameCourt extends JPanel {
	//variable needed for JPanel extension
	private static final long serialVersionUID = 0;
	
	// Game constants
	private static final File BACKGROUND= new File("maze.png");
	private static final File BORDER_BACKGROUND= new File ("maze_background.png");
	private static final int INTERVAL = 35;
	private static final int COURT_WIDTH = 600;
	private static final int COURT_HEIGHT = 520;
	private static final int PACMAN_VELOCITY = 4;

	// the state of the game logic
	private boolean playing = true; 
	private int timePassed = 0;
	private JLabel status; 
	private JLabel scoreLabel; 
	private JPanel lifePanel;

	//The game objects
	private Pacman pac;
	private Ghost blinky;
	private Ghost pinky;
	private Ghost inky;
	private Ghost clyde;
	private Ghost casper;
	private Ghost ghosty;
	
	private Set<Ghost> ghosts;
	private Set<Food> foods;
	private Set<Integer> specialIndices;
	private Set<Integer> fruitIndices;
	
	private boolean[][] borders;
	private Image img;
	private Image borderImg;
	
	//file to read info from for each level, and variables representing that info
	private static BufferedReader levelBr;
	private int level;
	private int numGhosts;
	private int numSpecial;
	
	//info on the number of lives a player has
	private int numLives;
	private boolean lost = false;
	private int score;
	
	//instance variables for the "special" food that makes the ghosts temporarily edible
	private boolean specialEaten = false;
	private int specialStartTime;
	private static final int SPECIALTIME = 6000;
	

	public GameCourt(JLabel status, JLabel scoreLabel, JPanel lifePanel, String levelFile){
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		setFocusable(true);
		startTimer();
		addKeyListener(getKeyAdapter());

		this.status = status;
		this.scoreLabel = scoreLabel;
		this.lifePanel = lifePanel;

		reset(new File(levelFile));
	}

	private void startTimer() {
		Timer timer = new Timer(INTERVAL, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tick();
			}
		});
		timer.start(); 
	}

	private KeyAdapter getKeyAdapter() {
		return new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_LEFT) {
					pac.setXVel(-PACMAN_VELOCITY);
					pac.setYVel(0);
					pac.setDirection(Direction.LEFT);
				} if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					pac.setXVel(PACMAN_VELOCITY);
					pac.setYVel(0);
					pac.setDirection(Direction.RIGHT);
				}
				if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					pac.setYVel(PACMAN_VELOCITY);
					pac.setXVel(0);
					pac.setDirection(Direction.DOWN);
				}
				if (e.getKeyCode() == KeyEvent.VK_UP) {
					pac.setYVel(-PACMAN_VELOCITY);
					pac.setXVel(0);
					pac.setDirection(Direction.UP);
				}
			}
			public void keyReleased(KeyEvent e) {
				// pac.setXVel(0);
				// pac.setYVel(0);
			}
		};
	}

	/**
	 * (Re-)set the game to its initial state.
	 */
	public void reset(File f) {
		score = -24; //pacman starts out on a food, so this makes the initial score 0
		int numLivesLeft = numLives;
		numLives = 3;
		playing = true;
		lost = false;
		try {
			img = ImageIO.read(BACKGROUND);
			borderImg = ImageIO.read(BORDER_BACKGROUND);
			levelBr = new BufferedReader(new FileReader(f));
			level = Integer.parseInt(levelBr.readLine());
			numGhosts = Integer.parseInt(levelBr.readLine());
			numSpecial = Integer.parseInt(levelBr.readLine());
		} catch (IOException e) {
		}
		
		timePassed = 0;
		setSpecialIndices();
		setFruitIndices();
		resetPacman();
		resetGhosts();
		createBorders();
		setStatus();
		createfoodForCurrentLevel();
		drawLives(numLivesLeft);
		requestFocusInWindow();
	}
	
	private void setSpecialIndices() {
		this.specialIndices = new HashSet<>();
		this.specialIndices.add(11);
		this.specialIndices.add(165);
		this.specialIndices.add(0);
		this.specialIndices.add(172);
		this.specialIndices.add(4);
		this.specialIndices.add(6);
		this.specialIndices.add(161);
		this.specialIndices.add(167);
	}

	private void setFruitIndices() {
		this.fruitIndices = new HashSet<>();
		this.fruitIndices.add(52);
		this.fruitIndices.add(122);
	}

	private void setStatus() {
		if (level < 7) {
			status.setText("Playing level " + Integer.toString(level));
		} else {
			status.setText("Playing custom level");
		}
	}

	public void resetGhosts() {
		ghosts = new HashSet<>();
		blinky = new Ghost(COURT_WIDTH, COURT_HEIGHT, "blue");
		ghosts.add(blinky);
		if (numGhosts > 1) {
			pinky = new Ghost(COURT_WIDTH, COURT_HEIGHT, "pink");
			ghosts.add(pinky);
			if (numGhosts > 2) {
				inky = new Ghost(COURT_WIDTH, COURT_HEIGHT, "red");
				ghosts.add(inky);
				if (numGhosts > 3) {
					clyde = new Ghost(COURT_WIDTH, COURT_HEIGHT, "orange");
					ghosts.add(clyde);
					if (numGhosts > 4) {
						casper = new Ghost(COURT_WIDTH, COURT_HEIGHT, "purple");
						ghosts.add(casper);
						if (numGhosts > 5) {
							ghosty = new Ghost(COURT_WIDTH, COURT_HEIGHT, "green");
							ghosts.add(ghosty);
						}
					}
				}
			}
		}
	}

	private void resetPacman() {
		pac = new Pacman(COURT_WIDTH, COURT_HEIGHT);
	}
	
	private void drawLives(int numLivesLeft) {
		int numLivesLeftToDraw = numLives - numLivesLeft;
		while (numLivesLeftToDraw > 0) {
			JLabel life = new JLabel(new ImageIcon("smallpacmanright.png"));
			lifePanel.add(life);
			numLivesLeftToDraw--;
		}
	}

	/**
	 * This method is called every time the timer defined in the constructor
	 * triggers.
	 */

	public void createBorders() {
		BufferedImage bufferedImage = new BufferedImage(borderImg.getWidth(null), 
														borderImg.getHeight(null), 
														BufferedImage.TYPE_INT_RGB);
		 Graphics gr = bufferedImage.getGraphics();
	     gr.drawImage(borderImg, 0, 0, null);
	     WritableRaster raster = bufferedImage.getRaster();
		 boolean[][] borderArr = new boolean[COURT_WIDTH][COURT_HEIGHT];
		 
		 for (int i = 0; i < COURT_WIDTH; i++) {
			 for (int j = 0; j < COURT_HEIGHT; j++) {
				 int[] x = raster.getPixel(i, j, (int[]) null);
				 int pixelBlueComponent = x[2]; 
				 if (pixelBlueComponent >= 245) { 
					 borderArr[i][j] = true;
				 } else {
					 borderArr[i][j] = false;
				 }
			 }
			 borders = borderArr;
		 }
	}

	public void createfoodForCurrentLevel() {
		foods = new HashSet<>();
		
		int specialPlaced = 0;
		int fruitPlaced = 0;
		int numFruit = 2;
		for (int i = 45; i < COURT_WIDTH - Food.SIZE - 20; i += 32) {
			for (int j = 33; j < COURT_HEIGHT - Food.SIZE; j += 28) {
				if (!foodTouchesBorder(i, j, borders) && !foodInGhostBox(i, j, borders)) {
					if (specialIndices.contains(foods.size()) && specialPlaced < numSpecial) {
						foods.add(new Special(COURT_WIDTH, COURT_HEIGHT, i, j));
						specialPlaced++;
					} else if (fruitIndices.contains(foods.size()) && fruitPlaced < numFruit) {
						foods.add(new Fruit(COURT_WIDTH, COURT_HEIGHT, i, j));
						fruitPlaced++;
					} else {
						foods.add(new Food(COURT_WIDTH, COURT_HEIGHT, i, j));
					}
				} 
			}
		}
	}

	private boolean foodTouchesBorder(int i, int j, boolean[][] borders) {
		return borders[i][j] || borders[i + Food.SIZE][j + Food.SIZE];
	}

	private boolean foodInGhostBox(int i, int j, boolean[][] borders) {
		return i >= 226 && i <= 376 && j >= 208 && j <= 276;
	}
	
	void tick() {
		if (playing) {
			timePassed += INTERVAL;
			
			advanceMovableGameObjs();
			removeEatenFood();
			checkSpecial();
			updateScore();
			checkPacGhostIntersect();
			repaint();
		} else {
			if (!lost) {
				try {
					proceedToNextLevel();
				} catch (IOException e) {
				}
				timePassed = 0;  
				resetPacman();
				resetGhosts();
				createfoodForCurrentLevel();
			}
		}
	} 

	private void advanceMovableGameObjs() {
		pac.move(borders, pac);
		for (Ghost g : ghosts) {
			if (timePassed > g.getDelay()) {
				g.move(borders, pac);
			}
		}
	}

	private void removeEatenFood() {
		ArrayList<Food> foodToRemove = new ArrayList<>();
		for (Food f : foods) {
			if (pac.intersects(f)) {
				f.eat(ghosts);
				foodToRemove.add(f);
				score += f.getScore();
				if (f instanceof Special) {
					specialEaten = true;
					specialStartTime = timePassed;
				} 
			}
		}
		for (Food f : foodToRemove) {
			foods.remove(f);
		}
	}

	private void checkSpecial() {
		if (specialEaten && (specialStartTime + SPECIALTIME < timePassed)) {
			for (Ghost g : ghosts) {
				g.changeState();
			}
			specialEaten = false;
		}
	}

	private void updateScore() {
		scoreLabel.setText("Score: " + Integer.toString(score));
	}

	private void checkPacGhostIntersect() {
		for (Ghost g : ghosts) {
			if (pac.intersects(g)) {
				if (g.getState().equals(State.NORMAL)) {
					loseALife(); 
				} else {
					g.resetPosition();
				}
			} else if (foods.isEmpty()) {
				playing = false;
				status.setText("You won level " + Integer.toString(level) + "!"); 
			}
		}
	}

	private void loseALife() {
		numLives--;
		lifePanel.remove(numLives);
		if (numLives <= 0) {
			playing = false;
			lost = true;
			status.setText("You lose!"); 
		} else {
			pac = new Pacman(COURT_WIDTH, COURT_HEIGHT);
		}
	}

	private void proceedToNextLevel() throws IOException {
		String nextLine = levelBr.readLine();
		if (nextLevelExists(nextLine)) {
			level = Integer.parseInt(nextLine);
			numGhosts = Integer.parseInt(levelBr.readLine());
			numSpecial = Integer.parseInt(levelBr.readLine());
			playing = true;
			timePassed = 0;  
			resetPacman();
			resetGhosts();
			createfoodForCurrentLevel();
		} else { 
			playing = false;
			status.setText("You win!");
		}
	}

	private boolean nextLevelExists(String nextLine) {
		return nextLine != null;
	}
	
	public int getCurrentScore() {
		return score;
	}
	
	public int getNumLives() {
		return numLives;
	}
	
	public void setPlaying(boolean isPlaying) {
		playing = isPlaying;
	}
	
	public void setLost(boolean hasLost) {
		lost = hasLost;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (!playing) {
		}
		g.drawImage(img, 0, 0, COURT_WIDTH, COURT_HEIGHT, null);
		for (Food f : foods) {
			f.draw(g);
		}
		pac.draw(g);
		for (Ghost gh : ghosts) {
			gh.draw(g);
		}
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(COURT_WIDTH, COURT_HEIGHT);
	}
}
