// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class EchoServer extends AbstractServer 
{
  //Class variables *************************************************
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 7199;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port) 
  {
    super(port);
  }

  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient(Object msg, ConnectionToClient client){
    System.out.println("Message received: " + msg + " from " + client);
    this.sendToAllClients(msg);
  }
    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  //**** Changed for E49
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
  }
  //**** Changed for E49
  synchronized protected void clientConnected(ConnectionToClient client) {
	    System.out.println("Client connected " + client);
	  }
  //**** Changed for E49
  @Override
  synchronized protected void clientDisconnected(ConnectionToClient client) {
	  System.out.println("Client disconnected ");
  }
  //**** Changed for E49
  @Override
  synchronized protected void clientException(ConnectionToClient client, Throwable exception) {
	  System.out.println("Client disconnected ");
  }
  
  //**** Changed for E49
  public void display(String message, int messageType) {
	  switch (messageType) {
	  case 0:
		  System.out.println(message);
		  break;
	  default:
		  System.out.println(message);
	  }
  }
  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */
  //**** Changed for E49 and E50, LS
  public static void main(String[] args) {
      int port = 0;

      try {
          // **** Changed for E49, LS
          port = Integer.parseInt(args[0]);
      } catch (Throwable t) {
          port = DEFAULT_PORT;
      }
      EchoServer server = new EchoServer(port);
       // **** Changed for E50, LS
      ServerConsole serverConsole = new ServerConsole(server);

      // **** Changed for E50, LS
      try {
          server.listen();
          serverConsole.accept();
      } catch (Exception ex) {
          System.out.println("ERROR - Could not listen for clients!");
      }
  }
 }
