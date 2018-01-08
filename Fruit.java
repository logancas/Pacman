

import java.awt.Graphics;
import java.awt.Image;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Fruit extends Food {
	private static final int SIZE = 20;
	public boolean isEaten = false;
	
	public Fruit(int courtW, int courtH, int xPos, int yPos) {
		super(courtW, courtH, xPos, yPos);
	}

	/**
	 * @return the score added to the game by eating this food object
	 */
	@Override
	public int getScore() {
		return 100;
	}

	@Override
	public void draw(Graphics g) {
		Image img = null;
		if (!isEaten) {
			try {
				File f = new File("cherry.png");
				img = ImageIO.read(f);
			} catch (IOException e) {
			}
			g.drawImage(img, posX - SIZE / 2, posY - SIZE / 2, SIZE, SIZE, null);
		} 
	}
	
}
