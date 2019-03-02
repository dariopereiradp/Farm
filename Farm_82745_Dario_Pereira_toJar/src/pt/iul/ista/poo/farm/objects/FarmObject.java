package pt.iul.ista.poo.farm.objects;

import pt.iul.ista.poo.gui.ImageTile;
import pt.iul.ista.poo.utils.Point2D;

public abstract class FarmObject implements ImageTile {

	private Point2D position;

	private int ciclos = 0;

	public FarmObject(Point2D p) {
		this.position = p;
	}

	@Override
	public String getName() {
		return getClass().getSimpleName().toLowerCase();
	}

	@Override
	public Point2D getPosition() {
		return position;
	}

	@Override
	public int getLayer() {
		return 0;
	}

	public void setPosition(Point2D position) {
		this.position = position;
	}

	public int getCiclos() {
		return ciclos;
	}

	public void setCiclos(int ciclos){
		this.ciclos = ciclos;
	}

	public void contarCiclos() {
		ciclos++;
	}

	public void reiniciarCiclos(){
		ciclos = 0;
	}

	public String toFile(){
		return getClass().getSimpleName() + ";" + getPosition();
	}

	public static FarmObject newFarmObject(String[] tokens){
		String[] tokens1 = tokens[1].substring(1, tokens[1].length()-1).replace(" ", "").split(",");
		int	x = Integer.parseInt(String.valueOf(tokens1[0]));
		int	y = Integer.parseInt(String.valueOf(tokens1[1]));
		Point2D pos = new Point2D(x,y);
		switch(tokens[0]){
		case "Farmer": return new Farmer(pos);
		case "Land": return Land.newLand(tokens,pos);
		case "Cabbage": return Vegetable.newVegetable(tokens, pos);
		case "Tomato": return Vegetable.newVegetable(tokens, pos);
		case "Chicken": return Animal.newAnimal(tokens, pos);
		case "Sheep" : return Animal.newAnimal(tokens, pos);
		case "Egg" : return Animal.newAnimal(tokens, pos);

		default: throw new IllegalStateException();
		}
	}



}