package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Classe principale de l'application
 */
public class VDM_stock_SocketServer extends Thread {
	
	private ServerSocket serverSocket;
    private int port;
    private boolean running = false;
    
    private ExecutorService executor = null;
    
    /**
     * Constructeur sans paramètre
     *  (définition du numéro du port)
     */
    public VDM_stock_SocketServer(){
    	port = 44800;
    }

    /**
     * Initialisation du socket coté serveur
     */
    public void startServer()
    {
        try
        {
            serverSocket = new ServerSocket( port );
            this.start();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Arret du socket coté serveur
     */
    public void stopServer()
    {
        running = false;
        this.interrupt();
    }

    @Override
    public void run()
    {
        running = true;
        while( running )
        {
            try
            {   
            	
            	MongoAccess.connect();

                System.out.println( "Listening for connections" );

                // Call accept() to receive the next connection
                Socket socket = serverSocket.accept();
                executor = Executors.newFixedThreadPool(10);

                // Pass the socket to the RequestHandler thread for processing
                RequestHandler requestHandler = new RequestHandler( socket );
                requestHandler.start();
                
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            finally {
            	if (executor != null){
            		executor.shutdown();
            	}
            }
        }
    }

	public static void main(String[] args) {
		
		VDM_stock_SocketServer server = new VDM_stock_SocketServer();
		server.startServer();

	}

}
