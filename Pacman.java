

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * The user-controlled game object depicted as the image pacman.png
 */
public class Pacman extends GameObj {
	private static final String imgFile = "pacman";
	private static BufferedImage img;
	
	private static final int SIZE = 26;
	private Direction currentDirection;

	public Pacman(int courtWidth, int courtHeight) {
		super(0, 0, 300, 282, SIZE, courtWidth, courtHeight);
		currentDirection = Direction.RIGHT;
		setImage();
	}

	private void setImage() {
		try {
			img = ImageIO.read(new File(imgFile + currentDirection.toString() + ".png"));
		} catch (IOException e) {
		}
	}
	
	public void setDirection(Direction dir) {
		currentDirection = dir;
		setImage();
	}

	/**
	 * @return the direction in which this Pacman is going
	 */
	public Direction getDirection() {
		return currentDirection;
	}

	/**
	 * Draws the image for Pacman
	 */
	@Override
	public void draw(Graphics g) {
		g.drawImage(img, posX, posY, size, size, null);
	}
	
	/**
	 * Moves the object by its velocity.  Ensures that the Pacman does
	 * not go outside its bounds by clipping.
	 * 
	 * @param borders - the barriers that Pacman is unable to cross in the GameCourt it is in
	 */
	@Override
	public void move(boolean[][] borders, Pacman p) {
		boolean isOkayToMove = true;
		boolean isSwitchingSides = false;
		
		if (isAtPortal(borders)) {
			isSwitchingSides = true;
		} else if (isAtBorder(borders)) {
			isOkayToMove = false;
		} 
		updatePositionsAndVelocities(isOkayToMove, isSwitchingSides);
	}

	private void updatePositionsAndVelocities(boolean isOkayToMove, boolean isSwitchingSides) {
		if (!isOkayToMove && !isSwitchingSides) {
			vX = 0;
			vY = 0;
			bounce(getDirection());
		} else if (!isSwitchingSides) {
			posX += vX;
			posY += vY;
			clip();
		} else {
			if (posX >= 300) {
				posX = 0;
			} else {
				posX = maxX - SIZE;
			}
			clip();
		}
	}

	private boolean isAtPortal(boolean[][] borders) {
		return posY >= 230 && posY <= 255 && (posX + SIZE + vX >= maxX || posX + vX <= 0);
	}

	private boolean isAtBorder(boolean[][] borders) {
		return borders[posX + vX][posY + vY] || borders[posX + SIZE + vX][posY + vY] ||
			   borders[posX + SIZE + vX][posY + SIZE + vY] || borders[posX + vX][posY + SIZE + vY];
	}		
}