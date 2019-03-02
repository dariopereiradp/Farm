package pt.iul.ista.poo.farm.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import pt.iul.ista.poo.farm.Farm;
import pt.iul.ista.poo.gui.ImageMatrixGUI;
import pt.iul.ista.poo.utils.Direction;
import pt.iul.ista.poo.utils.Point2D;

public abstract class Animal extends FarmObject implements Interactable, Updatable {

	public Animal(Point2D p) {
		super(p);
	}

	@Override
	public int getLayer() {
		return 3;
	}

	public Point2D novaPosicao(){
		List<Point2D> posicoesPossiveis = new ArrayList<>();
		for(Point2D p: Direction.getNeighbourhoodPoints(getPosition())){
			if(ImageMatrixGUI.getInstance().isWithinBounds(p) && Farm.getInstance().posicaoDisponivel(p))
				posicoesPossiveis.add(p);
		}
		if(posicoesPossiveis.size() == 0)
			return this.getPosition();
		else{
			Random r = new Random();
			int d = r.nextInt(posicoesPossiveis.size());
			return posicoesPossiveis.get(d);
		}
	}

	public void move(Point2D p){
		Farm.getInstance().removeObject(new KeyFarmObject (this));
		setPosition(p);
		Farm.getInstance().addObject(this);
	}

	@Override
	public String toFile() {
		return super.toFile() + ";" + getCiclos();
	}
	
	public static Animal newAnimal(String[] tokens, Point2D pos){
		switch(tokens[0]){
		case "Chicken": Chicken c = new Chicken(pos); c.setCiclos(Integer.parseInt(tokens[2]));
		return c;
		case "Sheep": Sheep s = new Sheep(pos); s.setCiclos(Integer.parseInt(tokens[2])); s.setEstado(SheepState.valueOf(tokens[3]));
		return s;
		case "Egg": Egg e = new Egg(pos); e.setCiclos(Integer.parseInt(tokens[2]));
		return e;
		default: throw new IllegalStateException();
		}
	}
	

}

