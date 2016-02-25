package server;

import java.net.UnknownHostException;
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

public class MongoAccess {
	
	static MongoClient mc;
	static DB db;
	static Jongo jongo;
	static MongoCollection collec;
	
	public static void connect(){
		
		//LoadConfig.loadSettings();
	
		try {
//			MongoClientURI uri  = new MongoClientURI(String.format("mongodb://%s:%s@%s:%s/%s", 
//					                                 Settings.getLogin(),
//					                                 Settings.getPass(),
//					                                 Settings.getAdresse(),
//					                                 Settings.getPort(),
//					                                 Settings.getBase())); 
			MongoClientURI uri  = new MongoClientURI("mongodb://192.168.0.201/test_VDM_stock"); 
			MongoClient client = new MongoClient(uri);
			db = client.getDB(uri.getDatabase());	
			jongo = new Jongo(db);
			
			
		}
		catch (UnknownHostException UHE){
					System.out.println("erreur " + UHE);
		}
	}
	
    public static Find request(String table) {	
		
		Find find = null;
		collec = jongo.getCollection(table);
		find = collec.find();

		return find;
	}
    
    public static Find request(String table, ObjectId id) {	
		
		Find find = null;
		collec = jongo.getCollection(table);
		find = collec.find("{_id :  #}", id);

		return find;
	}
    
    public static Find request(String table, String field, ObjectId objectId) {	
    	
    	System.out.println("table : " + table);
    	System.out.println("field : " + field);
    	System.out.println("objectId : " + objectId);
		
		Find find = null;
		collec = jongo.getCollection(table);
		find = collec.find("{# :  #}", field, objectId);

		return find;
	}
    
    
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
    
    public static Find request(String table, String field, ObjectId objectId, String object) {	
		
		Find find = null;
		collec = jongo.getCollection(table);
		find = collec.find("{# :  #}", field, objectId).projection("{# : 1, _id : 0", object);

		return find;
	}
    
    public static FindOne request(String table, String field1, String value1, String field2, String value2) {	
		
		FindOne findOne = null;
		collec = jongo.getCollection(table);
		findOne = collec.findOne("{# :  #, # :  #}", field1, value1, field2, value2);

		return findOne;
	}

	
	public static FindOne request(String table, String field, String valeur) {	
		
		FindOne one = null;
		collec = jongo.getCollection(table);
		one = collec.findOne(String.format("{\"%s\" : \"%s\"}", field, valeur));

		return one;
	}
	
    public static FindOne requestExistPartiel(String table, String field, String value, String field1, String field2) {	
		
		FindOne one = null;
		collec = jongo.getCollection(table);
		one = collec.findOne(String.format("{\"%s\" : \"%s\", \"%s\" : { $exists: true }, \"%s\" :{ $exists: false }}", field, value, field1, field2));

		return one;
	}
	
    public static Find requestAll(String table, String field, String valeur) {	
		
		Find all = null;
		collec = jongo.getCollection(table);
		all = collec.find(String.format("{\"%s\" : \"%s\"}", field, valeur));

		return all;
	}
	
	
    public static Find request(String table, String field, String valeur, boolean regex) {	
		
		Find all = null;
		collec = jongo.getCollection(table);

		String query = String.format("{%s : #}", field); 
		//String reg = String.format("^%s", valeur);
		String reg = String.format("%s", valeur);
		
		all = collec.find(query, Pattern.compile(reg, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE));

		return all;
	}
	
	public static void insert (String table, Object m) {
		collec = jongo.getCollection(table);
		collec.insert(m);
		
	}
	
	public static void save (String table, Object m) {
		collec = jongo.getCollection(table);
		collec.save(m);
		
	}
	
	public static void drop() {
		collec.drop();
		
	}

	public static void update(String table, ObjectId id, String c) {
		
		if (! "{}".equals(c)){
			collec = jongo.getCollection(table);	
			String mod = String.format("{$set : %s}",c);
		
			System.out.println(mod);
			
			collec.update("{_id : #}", id).with(mod);
		}
	}
	
	public static long count(){
		
		collec = jongo.getCollection("materiel");
		return collec.count();	
	}


}
