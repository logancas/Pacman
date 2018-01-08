

/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.0, Mar 2013
 */

import java.awt.Graphics;

public abstract class GameObj {

	protected int posX; 
	protected int posY;
	protected int size;
	protected int vX;
	protected int vY;
	protected int maxX;
	protected int maxY;


	public GameObj(int vX, int vY, int posX, int posY, 
		int size, int court_width, int court_height){
		this.vX = vX;
		this.vY = vY;
		this.posX = posX;
		this.posY = posY;
		this.size = size;
		this.maxX = court_width - size;
		this.maxY = court_height - size;
	}

	public void move(boolean[][] borders, Pacman p) {
		//defaults to immovable object
	}
	
	Direction getDirection() {
		if (vX > 0) {
			return Direction.LEFT;
		} else if (vX < 0) {
			return Direction.RIGHT;
		} else if (vY > 0) {
			return Direction.DOWN;
		} else if (vY < 0) {
			return Direction.UP;
		} else {
			return null;
		}
	}

	public int getSize() {
		return size;
	}
	
	public int getXPos() {
		return posX;
	}
	
	public int getYPos() {
		return posY;
	}
	
	public void setXPos(int x) {
		posX = x;
	}
	
	public void setYPos(int y) {
		posY = y;
	}
	
	public void setXVel(int x) {
		vX = x;
	}
	
	public void setYVel(int y) {
		vY = y;
	}
	
	public int getYVel() {
		return vY;
	}
	
	public int getXVel() {
		return vY;
	}

	/**
	 * Prevents the object from going outside of the bounds of the area 
	 * designated for the object. (i.e. Object cannot go outside of the 
	 * active area the user defines for it).
	 */ 
	public void clip(){
		if (posX < 0) {
			posX = 0;
		}
		else if (posX > maxX) {
			posX = maxX;
		}

		if (posY < 0) { 
			posY = 0;
		}
		else if (posY > maxY) { 
			posY = maxY;
		} 
	}

	/**
	 * Determine whether this game object is currently intersecting
	 * another object.
	 * 
	 * @param obj : other object
	 * @return whether this object intersects the other object.
	 */
	public boolean intersects(GameObj obj){
		return (posX + size >= obj.posX
				&& posY + size >= obj.posY
				&& obj.posX + obj.size >= posX 
				&& obj.posY + obj.size >= posY);
	}

	
	/** Update the velocity of the object in response to hitting
	 *  an obstacle in the given direction. If the direction is
	 *  null, this method has no effect on the object. */
	public void bounce(Direction dir) {
		switch (dir) {
			case UP:    vY = Math.abs(vY); break;  
			case DOWN:  vY = -Math.abs(vY); break;
			case LEFT:  vX = Math.abs(vX); break;
			case RIGHT: vX = -Math.abs(vX); break;
			case NONE: return;
		}
	}

	/**
	 * Default draw method that provides how the object should be drawn 
	 * in the GUI. This method does not draw anything. Subclass should 
	 * override this method based on how their object should appear.
	 * 
	 * @param g 
	 *	The <code>Graphics</code> context used for drawing the object.
	 * 	Remember graphics contexts that we used in OCaml, it gives the 
	 *  context in which the object should be drawn (a canvas, a frame, 
	 *  etc.)
	 */
	public abstract void draw(Graphics g);
	
}
