package server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.jongo.MongoCursor;

/**
 * Ecouteur du socket coté serveur.
 * Accepte 3 formes de requètes :
 * <ul>
 * <li>préfixée par "00" : de la forme 00=table&field&valeur </li>
 * {@link MongoAccess#requestPerfectMatch(String table, String field, String valeur, boolean regex)}
 * <li>préfixée par "0"  : de la forme  0=table&field&valeur  </li>
 * {@link MongoAccess#requestAnyMatch(String table, String field, String valeur, boolean regex)}
 * <li>préfixée par "1"  : de la forme  1=nom_du_materiel. A partir du nom du matériel, une première requète est exécutée pour obtenir la liste des tags.</li>
 * {@link MongoAccess#requestIn(String table, List<String> tags)}
 * </ul>
 * 
 */
class RequestHandler extends Thread{

	private Socket socket;

	/**
	 * Constructeur canonique
	 * @param socket le socket à écouter
	 */
	RequestHandler( Socket socket )	{
		this.socket = socket;
	}

	@Override
	public void run()	{
		try
		{
			System.out.println( "Received a connection" );

			// Get input and output streams
			BufferedReader in = new BufferedReader( new InputStreamReader( socket.getInputStream() ) );
			PrintWriter out = new PrintWriter( socket.getOutputStream() );

			// Echo lines back to the client until the client closes the connection or we receive an empty line
			String line = in.readLine();


			while( line != null && line.length() > 0 )
			{

				System.out.println(line);

				String c = line.split("=")[0];
				line = line.split("=")[1];

				switch (c){

				case "00" : if (line.split("&").length == 3 && !line.split("&")[2].equals("")){

					ArrayList<String> retour_array = new ArrayList<>();
					String retour;

					MongoCursor<Enregistrable> cursor = MongoAccess.requestPerfectMatch(line.split("&")[0], line.split("&")[1], line.split("&")[2], true).as(Enregistrable.class);


					while (cursor.hasNext()){
						retour_array.add(cursor.next().getNom());
					}

					retour = retour_array.stream().sorted().collect(Collectors.joining("&"));

					System.out.println("retour : " + retour);

					out.println(retour);
					out.flush();
				}   

				else{

					ArrayList<String> retour_array = new ArrayList<>();
					String retour;

					MongoCursor<Enregistrable> cursor = MongoAccess.request(line.split("&")[0]).as(Enregistrable.class);


					while (cursor.hasNext()){
						retour_array.add(cursor.next().getNom());
					}

					retour = retour_array.stream().sorted().collect(Collectors.joining("&"));

					System.out.println("retour : " + retour);

					out.println(retour);
					out.flush();
				} 
				break;

				case "0" : if (line.split("&").length == 3 && !line.split("&")[2].equals("")){

					ArrayList<String> retour_array = new ArrayList<>();
					String retour;

					MongoCursor<Enregistrable> cursor = MongoAccess.requestAnyMatch(line.split("&")[0], line.split("&")[1], line.split("&")[2], true).as(Enregistrable.class);


					while (cursor.hasNext()){
						retour_array.add(cursor.next().getNom());
					}

					retour = retour_array.stream().sorted().collect(Collectors.joining("&"));

					System.out.println("retour : " + retour);

					out.println(retour);
					out.flush();
				}   

				else{

					ArrayList<String> retour_array = new ArrayList<>();
					String retour;

					MongoCursor<Enregistrable> cursor = MongoAccess.request(line.split("&")[0]).as(Enregistrable.class);


					while (cursor.hasNext()){
						retour_array.add(cursor.next().getNom());
					}

					retour = retour_array.stream().sorted().collect(Collectors.joining("&"));

					System.out.println("retour : " + retour);

					out.println(retour);
					out.flush();
				} 
				break;


				case "1" :
					ArrayList<String> retour_array = new ArrayList<>();
					String retour;

					Materiel materiel = MongoAccess.request("materiel", "nom", line).as(Materiel.class);

					MongoCursor<Destinataire> cursor = MongoAccess.requestIn("destinataire", materiel.getTags()).as(Destinataire.class);


					while (cursor.hasNext()){
						Destinataire d = cursor.next();
						System.out.println("destinataire.getNom() : " + d.getNom());
						retour_array.add(d.getNom());
					}

					retour = retour_array.stream().collect(Collectors.joining("&"));

					System.out.println("retour : " + retour);

					out.println(retour);
					out.flush();

					break;
				}



				line = in.readLine();
			}

			// Close our connection
			in.close();
			out.close();
			socket.close();

			System.out.println( "Connection closed" );
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}
}
