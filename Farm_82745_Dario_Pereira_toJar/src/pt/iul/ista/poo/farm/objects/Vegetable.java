package pt.iul.ista.poo.farm.objects;

import pt.iul.ista.poo.farm.Farm;
import pt.iul.ista.poo.gui.ImageMatrixGUI;
import pt.iul.ista.poo.utils.Point2D;

public abstract class Vegetable extends FarmObject implements Updatable, Interactable {

	private VegetableState estado;


	public Vegetable(Point2D p) {
		super(p);
		estado = VegetableState.PEQUENO;
	}

	@Override
	public int getLayer() {
		return 1;
	}

	@Override
	public String getName() {
		return estado.nome() + this.getClass().getSimpleName().toLowerCase();
	}

	public void setEstado(VegetableState estado) {
		this.estado = estado;
	}

	public VegetableState getEstado() {
		return estado;
	}

	public abstract void cuidar();

	public void removerVegetal (){
		ImageMatrixGUI.getInstance().removeImage(this);
		Farm.getInstance().removeObject(new KeyFarmObject(this));
		Land l = (Land) Farm.getInstance().getObject(new KeyFarmObject(getPosition(),0));
		l.setEstado(LandState.LAND);	
	}

	@Override
	public void interagir() {
		int pontos = 0;
		if(this instanceof Tomato)
			pontos = 3;
		else pontos = 2;
		switch(getEstado()){
		case PEQUENO: cuidar();
		break;
		case GRANDE:{
			Farm.getInstance().somarPontos(pontos);
			removerVegetal();	
			break;
		}
		default: removerVegetal();
		break;
		}

	}

	@Override
	public String toFile() {
		return super.toFile() + ";" + getCiclos() + ";" + estado;
	}

	public static Vegetable newVegetable(String[] tokens, Point2D pos){
		switch(tokens[0]){
		case "Cabbage": Cabbage c = new Cabbage(pos); c.setCiclos(Integer.parseInt(tokens[2]));
		c.setEstado(VegetableState.valueOf(tokens[3]));
		return c;
		case "Tomato":
			Tomato t = new Tomato(pos); t.setCiclos(Integer.parseInt(tokens[2]));
			t.setEstado(VegetableState.valueOf(tokens[3])); t.setInteragiu(Boolean.getBoolean(tokens[4]));
			return t;
		default: throw new IllegalStateException();
		}

	}
}
