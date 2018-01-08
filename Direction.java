/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.0, Mar 2013
 */

public enum Direction {
	UP, DOWN, LEFT, RIGHT, NONE;
	
	@Override
	public String toString() {
		if (this.equals(UP)) {
			return "up";
		} else if (this.equals(DOWN)) {
			return "down";
		} else if (this.equals(LEFT)) {
			return "left";
		} else if (this.equals(RIGHT)) {
			return "right";
		} else {
			return "none";
		}
	}
}