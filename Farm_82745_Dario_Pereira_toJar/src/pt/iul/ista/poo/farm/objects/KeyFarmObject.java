package pt.iul.ista.poo.farm.objects;

import pt.iul.ista.poo.utils.Point2D;

public class KeyFarmObject {

	private Point2D position;
	private int layer;
	
	
	public KeyFarmObject(Point2D position, int layer) {
		this.position = position;
		this.layer = layer;
	}
	
	public KeyFarmObject(FarmObject f){
		position = f.getPosition();
		layer = f.getLayer();
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + layer;
		result = prime * result + ((position == null) ? 0 : position.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		KeyFarmObject other = (KeyFarmObject) obj;
		if (layer != other.layer)
			return false;
		if (position == null) {
			if (other.position != null)
				return false;
		} else if (!position.equals(other.position))
			return false;
		return true;
	}
	
	
}
