package pt.iul.ista.poo.farm.objects;

public enum SheepState {
	
	NORMAL, COM_FOME, FAMINTA;
	
	public String nome(){
		String s ="";
		switch(this){
		case NORMAL: s="sheep"; break;
		case COM_FOME: s="sheep"; break;
		default: s = "famished_sheep"; break;
		}
		return s;
	}

}
