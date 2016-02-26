package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class VDM_stock_SocketServer extends Thread {

	
	private ServerSocket serverSocket;
    private int port;
    private boolean running = false;
    
    
    public VDM_stock_SocketServer(){
    	port = 44800;
    }

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

                System.out.println( "Listening for a connection" );

                // Call accept() to receive the next connection
                Socket socket = serverSocket.accept();

                // Pass the socket to the RequestHandler thread for processing
                RequestHandler requestHandler = new RequestHandler( socket );
                requestHandler.start();
                
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    

	public static void main(String[] args) {
		
		VDM_stock_SocketServer server = new VDM_stock_SocketServer();
		server.startServer();

	}

}
