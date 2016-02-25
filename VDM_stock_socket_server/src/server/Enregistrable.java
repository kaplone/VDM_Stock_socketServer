package server;

public class Enregistrable {
	
	private String nom;
	
	public Enregistrable(String nom) {
		this.nom = nom;
	}

	public Enregistrable(){
		this("");
	}

	public String getNom(){
		return nom;
	}

}
