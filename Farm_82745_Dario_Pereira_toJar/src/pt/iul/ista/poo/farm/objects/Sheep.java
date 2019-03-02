package pt.iul.ista.poo.farm.objects;

import pt.iul.ista.poo.farm.Farm;
import pt.iul.ista.poo.utils.Point2D;


public class Sheep extends Animal {

	private static final int COM_FOME = 10;
	private static final int FAMINTA = 20;
	private SheepState estado;

	public Sheep(Point2D p) {
		super(p);
		estado = SheepState.NORMAL;
	}

	@Override
	public String getName() {
		return estado.nome();
	}

	@Override
	public void interagir() {
		if(estado == SheepState.FAMINTA || estado == SheepState.COM_FOME)
			estado = SheepState.NORMAL;
		reiniciarCiclos();

	}

	@Override
	public void update() {
		contarCiclos();
		if(estado == SheepState.NORMAL)
			Farm.getInstance().somarPontos(1);
		if (getCiclos() == COM_FOME)
			estado = SheepState.COM_FOME;
		if(getCiclos() == FAMINTA)
			estado = SheepState.FAMINTA;
		if(estado == SheepState.COM_FOME){
			Point2D p = novaPosicao();
			if(Farm.getInstance().temPlanta(p)){
				Vegetable temp = (Vegetable) Farm.getInstance().getObject(new KeyFarmObject (p,1));
				temp.removerVegetal();
				estado = SheepState.NORMAL;
				reiniciarCiclos();
			}
			else
				move(p);
		}

	}

	@Override
	public String toFile() {
		return super.toFile() + ";" + estado;
	}

	public void setEstado(SheepState estado) {
		this.estado = estado;
	}
	
	
	
	

}
