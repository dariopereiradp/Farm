package pt.iul.ista.poo.farm.objects;

import pt.iul.ista.poo.utils.Point2D;

public class Tomato extends Vegetable{


	private static final int MADURO = 15;
	private static final int PODRE = 25;
//	private int quantosCiclosDesdeQueCuidou = 0;
//	private static final int PARA_DE_AMADURECER = 10;
	private boolean interagiu = false;

	public Tomato(Point2D p) {
		super(p);
	}

	@Override
	public void cuidar() {
//		quantosCiclosDesdeQueCuidou = 0;
		interagiu = true;	
	}
	
	public void setInteragiu(boolean interagiu) {
		this.interagiu = interagiu;
	}

	@Override
	public void update() {
		contarCiclos();
//		if(quantosCiclosDesdeQueCuidou == PARA_DE_AMADURECER)
//			interagiu=false;
//		if(getCiclos() < MADURO)
//			quantosCiclosDesdeQueCuidou++;
		if(getCiclos()>MADURO && getCiclos()<PODRE && interagiu)
			setEstado(VegetableState.GRANDE);
		if (getCiclos()==PODRE)
			setEstado(VegetableState.ESTRAGADO);
	}



	@Override
	public String toFile() {
		return super.toFile() + ";" + interagiu;
	}
	
	

}
