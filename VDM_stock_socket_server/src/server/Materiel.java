package server;


import java.util.ArrayList;

/**
 * Modèle pour la conversion des éléments issus de la collection "materiel" dans la base MongoDB
 */
public class Materiel extends Enregistrable {


	private ArrayList<String> tags;

	/**
	 * Retourne la liste des tags valides de l'instance
	 * @return la liste des tags valides de l'instance
	 */
	public ArrayList<String> getTags() {
		return tags;
	}

	/**
	 * Modifie la liste des tags valides de l'instance
	 * @param tags la liste des tags valides de l'instance
	 */
	public void setTags(ArrayList<String> tags) {
		this.tags = tags;
	}
	
	
    
}
