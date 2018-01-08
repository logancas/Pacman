

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

public class Ghost extends GameObj implements Comparable<Ghost> {

	//ghost images
	private static String baseFile = "ghost"; 
	private BufferedImage img;
	
	//initial location, size, velocity, direction, state, and color
	private static final int SIZE = 26;
	private static final int VEL = 2;
	private int delay;
	private int initPosX;
	private int initPosY;
	private State state;
	private String color = "";
	private Direction currentDir;

	/**
	 * @param courtWidth - width of the GameCourt
	 * @param courtHeight - height of the GameCourt
	 * @param color - color of this ghost, as a string to easily access images
	 * @param velocity - velocity of ghost
	 */
	public Ghost(int courtWidth, int courtHeight, String color) {
		super(0, -VEL, (int) (courtWidth / 2.0) - 10, (int) (courtHeight / 2.0) - SIZE, SIZE,
				courtWidth, courtHeight);
		this.color = color;
		this.initPosX = posX;
		this.initPosY = posY;
		this.state = State.NORMAL;

		delay = computeDelay(color);
		currentDir = Direction.UP;
		setImage();
	}

	private int computeDelay(String color) {
		if (color.equals("blue")) {
			return 3000;
		} else if (color.equals("pink")) {
			return 8000;
		} else if (color.equals("red")) {
			return 1300;
		} else if (color.equals("orange")){
			return 18000;
		} else {
			return 23000;
		}
	}
	
	public void resetPosition() {
		posX = initPosX;
		posY = initPosY;
		vX = 0;
		vY = -VEL;
	}

	 /**
	  * Sets the image used for this ghost to that where the ghost is facing the new direction
	  * @param dir - the direction in which the ghost is now traveling
	  */
	private void setDirection(Direction dir) {
		currentDir = dir;
		setImage();
	}
	
	@Override
	public void draw(Graphics g) {
		g.drawImage(img, posX, posY, size, size, null);
	}
	
	@Override
	public void move(boolean[][] borders, Pacman p) {
		boolean isOkayToMove = true;
		boolean isSwitchingSides = false;
		
		for (int i = posX + vX; i < posX + SIZE + vX; i++) {
			for (int j = posY + vY; j < posY + SIZE + vY; j++) {
				if (borders[i][j]) {
					isOkayToMove = false;
				} else if (j >= 230 && j <= 255 && (i >= maxX || i <= 0)) {
					isSwitchingSides = true;
				}
			}
		}
		
		updatePositionsAndVelocities(isOkayToMove, isSwitchingSides, borders);
	}

	private void updatePositionsAndVelocities(boolean isOkayToMove, boolean isSwitchingSides, boolean[][] borders) {
		if (!isOkayToMove && !isSwitchingSides) {
			Direction dir = chooseDir(borders);
			setDirection(dir);
			if (dir.equals(Direction.RIGHT)) {
				vX = VEL;
				vY = 0;
			} else if (dir.equals(Direction.LEFT)) {
				vX = -VEL;
				vY = 0;
			} else if (dir.equals(Direction.DOWN)) {
				vX = 0;
				vY = VEL;
			} else {
				vX = 0;
				vY = -VEL;
			}
		} else if (!isSwitchingSides) {
			posX += vX;
			posY += vY;
			clip();
		} else {
			if (posX >= 300) {
				posX = 0;
			} else {
				posX = maxX - SIZE - vX;
			}
			clip();
		}
	}
	
	/**
	 * 
	 * @param borders - the barriers in the game court that this ghost is not able to cross
	 * @return A List of all possible directions in which this ghost is able to move
	 */
	private List<Direction> getPossibleDirections(boolean[][] borders) {
		List<Direction> options = getAllDirections();			
		if (vX != 0) { 
			options.remove(Direction.LEFT);
			options.remove(Direction.RIGHT);
			if (vX > 0) { 
				if(!canMoveDown(borders)) {
					options.remove(Direction.DOWN);
				}
			} else {
				if(!canMoveUp(borders)) {
					options.remove(Direction.UP);
				}
			}
		} if (vY != 0) { 
			options.remove(Direction.UP);
			options.remove(Direction.DOWN);
			if (vY > 0) {
				if(!canMoveRight(borders)) {
					options.remove(Direction.RIGHT);
				}
			} else {
				if(!canMoveLeft(borders)) {
					options.remove(Direction.LEFT);
				}
			}
		} 
		return options;
	}

	private List<Direction> getAllDirections() {
		List<Direction> options = new ArrayList<>();
		options.add(Direction.UP);
		options.add(Direction.DOWN);
		options.add(Direction.LEFT);
		options.add(Direction.RIGHT);
		return options;
	}

	private boolean canMoveDown(boolean[][] borders) {
		for (int i = 0; i < vX + SIZE; i++) {
			if (borders[posX][posY + i]) {
				return false;
			}
		}
		return true;
	}

	private boolean canMoveUp(boolean[][] borders) {
		for (int i = 0; i > vX - SIZE; i--) {
			if (posY + i >= maxY || posY + i <= 0) {
				break;
			}
			if (borders[posX][posY + i]) {
				return false;
			}
		}
		return true;
	}

	private boolean canMoveRight(boolean[][] borders) {
		if (vY > 0) { 
			for (int i = 0; i < vY + 5; i++) {
				if (borders[posX + i][posY]) {
					return false;
				}
			}
		}
		return true;
	}

	private boolean canMoveLeft(boolean[][] borders) {
		for (int i = 0; i > vY - 5; i--) {
			if (borders[posX + i][posY]) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Chooses a direction from the possible ones in which this ghost can move
	 * @param borders - the barriers in the game court which this ghost cannot cross; 
	 * borders[i][j] is true if there is a border at the coordinates (i, j), false otherwise
	 * @return a direction chosen from those returned by getPossibleDirections(borders)
	 */
	private Direction chooseDir(boolean[][] borders) {
		List<Direction> possibleDirections = getPossibleDirections(borders);
		int n = possibleDirections.size();
		int random = (int) (n * Math.random());
		int index = (int) Math.ceil(random);
		return possibleDirections.get(index);
	}
	
	/**
	 * Changes the ghost's state from normal to edible or edible to normal
	 */
	public void changeState() {
		if (state.equals(State.NORMAL)) {
			state = State.EDIBLE;
		} else {
			state = State.NORMAL;
		}
		setImage();
	}

	private void setImage() {
		try {
			if (state.equals(State.EDIBLE)) {
					img = ImageIO.read(new File("edibleghost.png"));
			} else {
				img = ImageIO.read(new File(color + baseFile + currentDir.toString() + ".png"));
			}
		} catch (IOException e) {
		}
	}
	
	@Override
	public int compareTo(Ghost g){
		return 0;
	}

	public State getState() {
		return state;
	}

	public int getDelay() {
		return delay;
	}

}

