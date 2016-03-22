package server;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.bson.types.ObjectId;
import org.jongo.Distinct;
import org.jongo.Find;
import org.jongo.FindOne;
import org.jongo.Jongo;
import org.jongo.MongoCollection;


import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

/**
 * Classe pour la gestion des échanges avec la base de données MongoDB.
 * Elle fournit une collection de requètes découplées du driver choisi.
 * 
 * @see <a href="http://jongo.org">jongo</a>
 */
public class MongoAccess {
	
	static MongoClient mc;
	static DB db;
	static Jongo jongo;
	static MongoCollection collec;
	
	/**
	 * Etablit une connexion avec la base de données MongoDB spécifiée dans le code.
	 */
	public static void connect(){
		
		//LoadConfig.loadSettings();
	
		try {
//			MongoClientURI uri  = new MongoClientURI(String.format("mongodb://%s:%s@%s:%s/%s", 
//					                                 Settings.getLogin(),
//					                                 Settings.getPass(),
//					                                 Settings.getAdresse(),
//					                                 Settings.getPort(),
//					                                 Settings.getBase())); 
			MongoClientURI uri  = new MongoClientURI("mongodb://192.168.0.201/VDM_stock"); 
			MongoClient client = new MongoClient(uri);
			db = client.getDB(uri.getDatabase());	
			jongo = new Jongo(db);
			
			
		}
		catch (UnknownHostException UHE){
					System.out.println("erreur " + UHE);
		}
	}
	
	/**
	 * Retourne tous les éléments d'une table.
	 * @param table table interrogée
	 * @return une collection contenant tous les éléments de la table
	 */
    public static Find request(String table) {	
		
		Find find = null;
		collec = jongo.getCollection(table);
		find = collec.find();

		return find;
	}
    
    /**
     * Retourne une liste vide ou contenant 1 élément correspondant à l'identifiant passé en parametre.
     * @param table table interrogée
     * @param id Identifiant recherché
     * @return une liste vide ou contenant 1 élément correspondant à l'identifiant passé en parametre.
     */
    public static Find request(String table, ObjectId id) {	
		
		Find find = null;
		collec = jongo.getCollection(table);
		find = collec.find("{_id :  #}", id);

		return find;
	}
    
    /**
     * Retourne une liste des éléments d'une table dont la liste du champs "wrong_tags" contient au moins un élément de la liste "tags".
     * @param table table interrogée
     * @param tags liste des tags dont on vérifie la présence
     * @return une liste des éléments d'une table dont la liste du champs "wrong_tags" contient au moins un élément de la liste "tags".
     */
    public static Find requestIn(String table, List<String> tags) {	
    	
    	System.out.println("tags : " + tags.toString());
    	System.out.println("tags.size() : " + tags.size());
		
		Find find = null;
		collec = jongo.getCollection(table);
		find = collec.find("{wrong_tags : {$in :  #}}", tags);

		return find;
	}
    
    /**
     * Retourne une liste d'éléments dont le champ passé en paramètre a comme valeur l'identifiant passé en parametre.
     * Par exemple quand un objet référence un autre objet par son identifiant.
     * 
     * @param table table interrogée
     * @param field champ dans lequel on recherche l'identifiant
     * @param objectId Identifiant recherché
     * @return une liste d'éléments dont le champ passé en paramètre a comme valeur l'identifiant passé en parametre.
     */
    public static Find request(String table, String field, ObjectId objectId) {	
    	
    	System.out.println("table : " + table);
    	System.out.println("field : " + field);
    	System.out.println("objectId : " + objectId);
		
		Find find = null;
		collec = jongo.getCollection(table);
		find = collec.find("{# :  #}", field, objectId);

		return find;
	}
    
    /**
     * Retourne une liste d'éléments distincts dont le champ passé en paramètre a comme valeur l'identifiant passé en parametre.
     * @param table table interrogée
     * @param distinct nom du champ dont les valeurs doivent etre distinctes
     * @param field champ dans lequel on recherche l'identifiant
     * @param objectId Identifiant recherché
     * @return une liste d'éléments distincts dont le champ passé en paramètre a comme valeur l'identifiant passé en parametre.
     */
    public static Distinct distinct(String table, String distinct, String field, ObjectId objectId) {	
    	
    	System.out.println("table : " + table);
    	System.out.println("field : " + field);
    	System.out.println("objectId : " + objectId);
		
		Distinct find = null;
		collec = jongo.getCollection(table);
		find = collec.distinct(distinct).query("{# :  #}", field, objectId);
		
		//System.out.println();

		return find;
	}
    
    /**
     * Retourne une liste d'éléments dont le champ passé en paramètre a comme valeur l'identifiant passé en parametre.
     * Cette liste ne contient que la projection du champ 'objet' passé en parametre et exclue la valeur de l'identifiant.
     * 
     * @param table table table interrogée
     * @param field champ dans lequel on recherche l'identifiant
     * @param objectId Identifiant recherché
     * @param object champ conservé dans la projection
     * @return une liste d'éléments dont le champ passé en paramètre a comme valeur l'identifiant passé en parametre.
     */
    public static Find request(String table, String field, ObjectId objectId, String object) {	
		
		Find find = null;
		collec = jongo.getCollection(table);
		find = collec.find("{# :  #}", field, objectId).projection("{# : 1, _id : 0", object);

		return find;
	}
    
    /**
     * Retourne une liste d'éléments dont les champs passés en paramètre ont comme valeurs celles passées en parametre.
     * @param table table table interrogée
     * @param field1 premier champ (interrogé avec la  première valeur)
     * @param value1 première valeur (paramètre du premier champ)
     * @param field2 deuxième champ (interrogé avec la  deuxième valeur)
     * @param value2 deuxième valeur (paramètre du deuxième champ)
     * @return une liste d'éléments dont les champs passés en paramètre ont comme valeurs celles passées en parametre.
     */
    public static FindOne request(String table, String field1, String value1, String field2, String value2) {	
		
		FindOne findOne = null;
		collec = jongo.getCollection(table);
		findOne = collec.findOne("{# :  #, # :  #}", field1, value1, field2, value2);

		return findOne;
	}

    /**
	 * Retourne un seul élément dont le champ passé en paramètre a la valeur passée en paramètre.
	 * @param table table table interrogée
	 * @param field champ concerné
	 * @param valeur valeur recherchée
	 * @return un élément unique dont le champ passé en paramètre a la valeur passée en paramètre.
	 */
	public static FindOne request(String table, String field, String valeur) {	
		
		FindOne one = null;
		collec = jongo.getCollection(table);
		one = collec.findOne(String.format("{\"%s\" : \"%s\"}", field, valeur));

		return one;
	}
	
	/**
	 * Retourne un seul élément dont le premier champ a la valeur passée en paramètre et dont le deuxième champ existe et le troisième n'existe pas.
	 * @param table table table interrogée
	 * @param field champ concerné
	 * @param value valeur recherchée
	 * @param field1 champ devant exister
	 * @param field2 champ ne devant pas exister
	 * @return un seul élément dont le premier champ a la valeur passée en paramètre et dont le deuxième champ existe et le troisième n'existe pas.
	 */
    public static FindOne requestExistPartiel(String table, String field, String value, String field1, String field2) {	
		
		FindOne one = null;
		collec = jongo.getCollection(table);
		one = collec.findOne(String.format("{\"%s\" : \"%s\", \"%s\" : { $exists: true }, \"%s\" :{ $exists: false }}", field, value, field1, field2));

		return one;
	}
	
    /**
     * Retourne une liste des éléments dont le champ passé en paramètre a la valeur passée en paramètre.
     * @param table table table interrogée
     * @param field champ concerné
     * @param valeur valeur recherchée
     * @return une liste des éléments dont le champ passé en paramètre a la valeur passée en paramètre.
     */
    public static Find requestAll(String table, String field, String valeur) {	
		
		Find all = null;
		collec = jongo.getCollection(table);
		all = collec.find(String.format("{\"%s\" : \"%s\"}", field, valeur));

		return all;
	}
	
    /**
	 * Retourne une liste des éléments dont le champ passé en paramètre a la valeur passée en paramètre
	 * La recherche se fait avec les filtres REGEX suivants :
	 * <ul>
	 * <li>/valeur/</li>
	 * <li>UNICODE_CASE</li>
	 * <li>CASE_INSENSITIVE</li>
	 * </ul>
	 * 
	 * @param table table interrogée
	 * @param field champ concerné
	 * @param valeur valeur recherchée
	 * @param regex modifie la signature de la méthode (paramètre non- utilisé)
	 * @return une liste des éléments (filtrés avec une REGEX) dont le champ passé en paramètre a la valeur passée en paramètre
	 */
    public static Find requestAnyMatch(String table, String field, String valeur, boolean regex) {	
		
		Find all = null;
		collec = jongo.getCollection(table);

		String query = String.format("{%s : #}", field); 
		//String reg = String.format("^%s", valeur);
		String reg = String.format("%s", valeur);
		
		all = collec.find(query, Pattern.compile(reg, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE));

		return all;
	}
    
    /**
	 * Retourne une liste des éléments dont le champ passé en paramètre a la valeur passée en paramètre
	 * La recherche se fait avec les filtres REGEX suivants :
	 * <ul>
	 * <li>/^valeur$/</li>
	 * <li>UNICODE_CASE</li>
	 * <li>CASE_INSENSITIVE</li>
	 * </ul>
	 * 
	 * @param table table interrogée
	 * @param field champ concerné
	 * @param valeur valeur recherchée
	 * @param regex modifie la signature de la méthode (paramètre non- utilisé)
	 * @return une liste des éléments (filtrés avec une REGEX) dont le champ passé en paramètre a la valeur passée en paramètre
	 */
    public static Find requestPerfectMatch(String table, String field, String valeur, boolean regex) {	
		
		Find all = null;
		collec = jongo.getCollection(table);

		String query = String.format("{%s : #}", field); 
		//String reg = String.format("^%s", valeur);
		String reg = String.format("^%s$", valeur);
		
		all = collec.find(query, Pattern.compile(reg, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE));

		return all;
	}
	
    /**
     * Insère un objet dans une table
     * @param table table accédée
     * @param m objet à insérer
     */
	public static void insert (String table, Object m) {
		collec = jongo.getCollection(table);
		collec.insert(m);
		
	}
	
	/**
	 * Sauvegarde un objet dans une table
	 * @param table table accédée
	 * @param m objet à enregistrer
	 */
	public static void save (String table, Object m) {
		collec = jongo.getCollection(table);
		collec.save(m);
		
	}
	
	/**
	 * Suppression de la collection renseignée à la connexion.
	 * @see #connect 
	 */
	public static void drop() {
		collec.drop();
		
	}

	/**
	 * Mise à jour d'un unique champ de l'objet.
	 * @param table table accédée
	 * @param id identifiant de l'élément à mettre à jour
	 * @param c champ mis à jour ou rajouté (sous la forme "{field : value}") 
	 */
	public static void update(String table, ObjectId id, String c) {
		
		if (! "{}".equals(c)){
			collec = jongo.getCollection(table);	
			String mod = String.format("{$set : %s}",c);
		
			System.out.println(mod);
			
			collec.update("{_id : #}", id).with(mod);
		}
	}
	
	/**
	 * Retourne le nombre d'éléments dans la collection "materiel"
	 * @return le nombre d'éléments dans la collection "materiel"
	 */
	public static long count(){
		
		collec = jongo.getCollection("materiel");
		return collec.count();	
	}


}
