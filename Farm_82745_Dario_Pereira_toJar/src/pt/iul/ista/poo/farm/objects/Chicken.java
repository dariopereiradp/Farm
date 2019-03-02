package pt.iul.ista.poo.farm.objects;

import pt.iul.ista.poo.farm.Farm;
import pt.iul.ista.poo.gui.ImageMatrixGUI;
import pt.iul.ista.poo.utils.Point2D;

public class Chicken extends Animal {

	private static final int POE_OVO = 10;

	public Chicken(Point2D p) {
		super(p);

	}

	@Override
	public void interagir() {
		Farm.getInstance().removeObject(new KeyFarmObject(this));
		ImageMatrixGUI.getInstance().removeImage(this);
		Farm.getInstance().somarPontos(2);

	}

	@Override
	public void update() {
		contarCiclos();
		if(getCiclos()%2 != 0){
			Point2D p = novaPosicao();
			if(Farm.getInstance().temPlanta(p)){
				Vegetable temp = (Vegetable) Farm.getInstance().getObject(new KeyFarmObject (p,1));
				if(temp instanceof Tomato)
					temp.removerVegetal();
				else move(p);
				
			}
			else
				move(p);
		}
		if(getCiclos()%POE_OVO == 0){
			if(!novaPosicao().equals(this.getPosition())){
				Egg ovo = new Egg (novaPosicao());
				Farm.getInstance().addObject(ovo);
				ImageMatrixGUI.getInstance().addImage(ovo);
			}	
		}

	}

}
