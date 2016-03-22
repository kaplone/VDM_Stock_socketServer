package server;

import java.util.ArrayList;

/**
 * Modèle pour la conversion des éléments issus de la base MongoDB.
 */
public class Enregistrable {
	
	private String nom;
	
	/**
	 * Constructeur canonique
	 * @param nom le nom affiché de l'élément.
	 */
	public Enregistrable(String nom) {
		this.nom = nom;
	}

	/**
	 * Constructeur vide.
	 */
	public Enregistrable(){
		this("");
	}

	/**
	 * Retourne le nom affiché
	 * @return le nom affiché
	 */
	public String getNom(){
		return nom;
	}

}