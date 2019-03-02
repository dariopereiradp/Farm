package pt.iul.ista.poo.utils;
//Added 27-Feb-2018 Changed 16-Mar-2018
public class Vector2D {

	private int x;
	private int y;

	public Vector2D(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}

	public static Vector2D movementVector(Point2D from, Point2D to) {
		Vector2D d = new Vector2D(to.getX() - from.getX(), to.getY() - from.getY());
		if (Math.abs(d.getX()) > Math.abs(d.getY())) {
			d = new Vector2D((int) Math.signum(d.getX()), 0);
		} else if (Math.abs(d.getX()) <= Math.abs(d.getY())) {
			d = new Vector2D(0, (int) Math.signum(d.getY()));
		}
		return d;
	}
	
}
