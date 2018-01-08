

import java.awt.Color;
import java.awt.Graphics;
import java.util.Set;
/**
 * A basic game object that does not move and that can be eaten by pacman
 */
public class Food extends GameObj implements Comparable<Food> {
	public static final int SIZE = 5;
	private boolean isEaten = false;
	
	public Food(int courtWidth, int courtHeight, int xPos, int yPos) {
		super(0, 0, xPos, yPos, SIZE, courtWidth, courtHeight);
	}
	
	/**
	 * This function allows the food to be "eaten" by Pacman
	 * @param ghosts is needed for Food's subtype 'Special's eat function
	 */
	public void eat(Set<Ghost> ghosts) {
		isEaten = true;
	}

	protected Color getColor() {
		return Color.YELLOW;
	}

	public int getScore() {
		return 24;
	}
	
	@Override
	public void draw(Graphics g) {
		if (!isEaten) {
			g.setColor(getColor());
			g.fillOval(posX, posY, getSize(), getSize());
		} 
	}

	@Override
	public int compareTo(Food f){
		return 0;
	}
}

