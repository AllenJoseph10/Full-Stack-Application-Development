package templates;/*
 * BooksDatabaseServer.java
 *
 * The server main class.
 * This server provides a service to access the Books database.
 *
 * author: <2549623>
 *
 */

import java.io.OutputStream;
import java.net.Socket;
import java.net.ServerSocket;
import java.net.InetAddress;

import static java.lang.System.in;


public class BooksDatabaseServer {

    private int thePort = 0;
    private String theIPAddress = null;
    private ServerSocket serverSocket =  null;

    //Support for closing the server
    //private boolean keypressedFlag = false;


    //Class constructor
    public BooksDatabaseServer(){
        //Initialize the TCP socket
        thePort = Credentials.PORT;
        theIPAddress = Credentials.HOST;

        //Initialize the socket and runs the service loop
        System.out.println("Server: Initializing server socket at " + theIPAddress + " with listening port " + thePort);
        System.out.println("Server: Exit server application by pressing Ctrl+C (Windows or Linux) or Opt-Cmd-Shift-Esc (Mac OSX)." );//Initialize the socket
        try {
            // create a Socket object
            serverSocket = new ServerSocket(thePort);

            // set socket options if necessary
            System.out.println("Server: Server at " + theIPAddress + " is listening on port : " + thePort);
            System.out.println("Socket initialized successfully");

            // use the socket...
            // clientSocket.getInputStream(); //to read from the socket
            // clientSocket.getOutputStream(); //to write to the socket

            // close the socket when finished

        }
        //TO BE COMPLETED
        catch (Exception e){
            //The creation of the server socket can cause several exceptions;
            //See https://docs.oracle.com/javase/7/docs/api/java/net/ServerSocket.html
            System.out.println(e);
            System.exit(1);
        }
    }

    //Runs the service loop
    public void executeServiceLoop()
    {
        System.out.println("Server: Start service loop.");
        try {
            //Service loop
            //TO BE COMPLETED
            while (true) {
                new BooksDatabaseService(serverSocket.accept());

            }

        } catch (Exception e){
            //The creation of the server socket can cause several exceptions;
            //See https://docs.oracle.com/javase/7/docs/api/java/net/ServerSocket.html
            System.out.println(e);
        }
        System.out.println("Server: Finished service loop.");
    }

/*
	@Override
	protected void finalize() {
		//If this server has to be killed by the launcher with destroyForcibly
		//make sure we also kill the service threads.
		System.exit(0);
	}
*/

    public static void main(String[] args){
        //Run the server
        BooksDatabaseServer server=new BooksDatabaseServer(); //inc. Initializing the socket
        server.executeServiceLoop();
        System.out.println("Server: Finished.");
        System.exit(0);
    }


}
