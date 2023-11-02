// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import ocsf.client.*;
import common.*;
import java.io.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String host, int port, ChatIF clientUI) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    openConnection();
  }

  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  //**** Changed for E49
  public void handleMessageFromClientUI(String message) {
	    try {
	        if (message.startsWith("#")) {
	            processCommand(message);
	        } else {
	            sendToServer(message);
	        }
	    } catch (IOException e) {
	        clientUI.display("Could not send message to server. Terminating client.");
	        quit();
	    }
	}

    // **** Changed for E49
	private void processCommand(String command) throws IOException {
	    String[] parts = command.split(" ");
	    String cmd = parts[0];

	    switch (cmd) {
	        case "#quit":
	            quit();
	            break;
	        case "#logoff":
	            closeConnection();
	            clientUI.display("Logged off.");
	            break;
	        case "#sethost":
	            if (!isConnected()) {
	                if (parts.length == 2) {
	                    String host = parts[1];
	                    setHost(host);
	                    clientUI.display("Host set to " + host);
	                } else {
	                    clientUI.display("Usage: #sethost <host>");
	                }
	            } else {
	                clientUI.display("You must log off before changing the host.");
	            }
	            break;
	        case "#setport":
	            if (!isConnected()) {
	                if (parts.length == 2) {
	                    int port = Integer.parseInt(parts[1]);
	                    setPort(port);
	                    clientUI.display("Port set to " + port);
	                } else {
	                    clientUI.display("Usage: #setport <port>");
	                }
	            } else {
	                clientUI.display("You must log off before changing the port.");
	            }
	            break;
	        case "#login":
	            if (!isConnected()) {
	                openConnection();
	                clientUI.display("Connected to the server.");
	            } else {
	                clientUI.display("Already connected to the server.");
	            }
	            break;
	        case "#gethost":
	            clientUI.display("Current host: " + getHost());
	            break;
	        case "#getport":
	            clientUI.display("Current port: " + getPort());
	            break;
	        default:
	            clientUI.display("Unrecognized command: " + cmd);
	            break;
	    }
	}

  // **** Changed for E49
  @Override
  public void connectionClosed() {
      System.out.println("Connection Closed to Server.");
  }

  /**
   * Handles errors in connection
   */
  //**** Changed for E49
  @Override
  public void connectionException(Exception exception) {
      System.out.println("The server has stopped listening for connections. Disconnecting");
      quit();
  }
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }
}
//End of ChatClient class
