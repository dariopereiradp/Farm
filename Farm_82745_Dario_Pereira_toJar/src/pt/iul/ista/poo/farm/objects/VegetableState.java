package pt.iul.ista.poo.farm.objects;

public enum VegetableState {
	PEQUENO, GRANDE, ESTRAGADO;
	
	public String nome(){
		String s = "";
		switch(this){
		case PEQUENO: s = "small_"; break;
		case GRANDE: s = ""; break;
		default: s = "bad_"; break;
		}
		return s;
		
	}

}
