package pt.iul.ista.poo.farm.objects;

import pt.iul.ista.poo.utils.Vector2D;
import pt.iul.ista.poo.farm.Farm;
import pt.iul.ista.poo.gui.ImageMatrixGUI;
import pt.iul.ista.poo.utils.Direction;
import pt.iul.ista.poo.utils.Point2D;

public class Farmer extends FarmObject {

	public Farmer(Point2D p) {
		super(p);
	}

	@Override
	public int getLayer() {
		return 4;
	}

	public Point2D novaPosicao(int key){
		if (Direction.isDirection(key)){
			Vector2D deslocamento = Direction.directionFor(key).asVector();
			return getPosition().plus(deslocamento);
		}
		else return getPosition();
	}

	public void move (int key){
		Point2D p= novaPosicao(key);
		if(ImageMatrixGUI.getInstance().isWithinBounds(p) && Farm.getInstance().posicaoDisponivel(p))
			setPosition(novaPosicao(key));
	}
}
