package pt.iul.ista.poo.farm.objects;

import pt.iul.ista.poo.farm.Farm;
import pt.iul.ista.poo.gui.ImageMatrixGUI;
import pt.iul.ista.poo.utils.Point2D;

public class Egg extends Animal {
	
	private static final int NASCER_GALINHA = 20;
	
	public Egg(Point2D p) {
		super(p);
	}
	
	

	@Override
	public int getLayer() {
		return 2;
	}



	@Override
	public void update() {
		contarCiclos();
		if(getCiclos()==NASCER_GALINHA)
			if(!novaPosicao().equals(this.getPosition())){
				Chicken galinha = new Chicken(novaPosicao());
				Farm.getInstance().addObject(galinha);
				ImageMatrixGUI.getInstance().addImage(galinha);
				Farm.getInstance().removeObject(new KeyFarmObject(this));
				ImageMatrixGUI.getInstance().removeImage(this);
			}
		

	}

	@Override
	public void interagir() {
		Farm.getInstance().removeObject(new KeyFarmObject(this));
		ImageMatrixGUI.getInstance().removeImage(this);
		Farm.getInstance().somarPontos(1);

	}

}
