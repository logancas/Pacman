

import java.awt.Color;
import java.util.Set;
/**
 * This is a subtype of the food class.  If this food is eaten, it makes all the ghosts temporarily
 * "edible" such that pacman can eat them, causing them to return to their original starting points.
 */
public class Special extends Food {
	protected static final int SIZE = 10;
	boolean isEaten = false;
	
	public Special(int courtW, int courtH, int xPos, int yPos) {
		super(courtW, courtH, xPos, yPos);
	}

	@Override
	public void eat(Set<Ghost> ghosts) {
		isEaten = true;
		for (Ghost g : ghosts) {
			if (g.getState().equals(State.NORMAL)) {
				g.changeState();
			}
		}
	}

	@Override
	protected Color getColor() {
		return Color.WHITE;
	}

	@Override
	public int getSize() {
		return SIZE;
	}
}
