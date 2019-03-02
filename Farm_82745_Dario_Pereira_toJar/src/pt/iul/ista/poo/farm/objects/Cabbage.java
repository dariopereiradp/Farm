package pt.iul.ista.poo.farm.objects;


import pt.iul.ista.poo.utils.Point2D;

public class Cabbage extends Vegetable {

	private static final int MADURO = 10;
	private static final int PODRE = 30;

	public Cabbage(Point2D p) {
		super(p);
	}

	@Override
	public void cuidar() {
		contarCiclos();
	}

	@Override
	public void update() {
		contarCiclos();
		if(getCiclos() == MADURO || getCiclos() == MADURO + 1)
			setEstado(VegetableState.GRANDE);
		if(getCiclos() == PODRE || getCiclos() == PODRE + 1)
			setEstado(VegetableState.ESTRAGADO);		
	}

}
