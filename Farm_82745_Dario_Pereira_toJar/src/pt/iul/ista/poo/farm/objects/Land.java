package pt.iul.ista.poo.farm.objects;

import java.util.Random;

import pt.iul.ista.poo.farm.Farm;
import pt.iul.ista.poo.gui.ImageMatrixGUI;
import pt.iul.ista.poo.utils.Point2D;

public class Land extends FarmObject implements Interactable {

	private LandState estado;
	private static final double TERRA_ROCHOSA = 0.1;


	public Land(Point2D p) {
		super(p);
		Random r = new Random();
		if(r.nextDouble()>=TERRA_ROCHOSA)
			estado = LandState.LAND;
		else
			estado = LandState.ROCK;
	}


	@Override
	public String getName() {
		if(estado == LandState.PLANTED)
			return "plowed";
		return estado.name().toLowerCase();
	}

	public void novoVegetal(){
		if(Math.random()<0.5){
			Tomato t = new Tomato(this.getPosition());
			ImageMatrixGUI.getInstance().addImage(t);
			Farm.getInstance().addObject(t);
		}
		else{
			Cabbage c = new Cabbage(this.getPosition());
			ImageMatrixGUI.getInstance().addImage(c);
			Farm.getInstance().addObject(c);
		}
	}


	@Override
	public void interagir() {
		switch(estado){
		case LAND: estado = LandState.PLOWED;
		break;
		case PLOWED: estado = LandState.PLANTED;
		novoVegetal();
		break;
		default: break;
		}

	}

	public LandState getEstado() {
		return estado;
	}


	public void setEstado(LandState estado) {
		this.estado = estado;
	}


	@Override
	public String toFile() {
		return super.toFile() + ";" +  estado;
	}
	
	public static Land newLand (String[] tokens, Point2D pos){
		Land l = new Land(pos);
		l.setEstado(LandState.valueOf(tokens[2]));
		return l;
	}
	
	

}
