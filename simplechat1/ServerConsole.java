import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;
//**** Changed for E50
public class ServerConsole extends AbstractServer {
    private EchoServer server;

    public ServerConsole(EchoServer server) {
        super(server.getPort());
        this.server = server;
    }

    public void accept() {
        try {
            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
            String message;

            while (true) {
                message = console.readLine();

                if (message != null) {
                    if (message.startsWith("#")) {
                        handleServerCommand(message); 
                    } else {
                        sendMessageToServer(message); 
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading from console: " + e.getMessage());
        }
    }

    public void sendMessageToServer(String message) {
        System.out.println("Sending message to clients: " + message);
        server.sendToAllClients("Server msg> " + message); 
    }

    // **** Changed for E50, LS
    private void handleServerCommand(String command) {
        if (command.equals("#quit")) {
        	System.out.println("The server is shutting down.");
            server.stopListening();
            System.exit(0);
        } else if (command.equals("#stop")) {
            server.stopListening();
        } else if (command.equals("#close")) {
            server.stopListening();
        	Thread[] clientThreadList = server.getClientConnections();

        	// Iterate through the list and close each client connection
        	for (Thread clientThread : clientThreadList) {
        	    ConnectionToClient client = (ConnectionToClient)clientThread;
        	    try {
        	    	client.close();
				} catch (IOException e) {
				}
        	}
        	System.out.println("The server has successfully closed.");
        }
    	else if (command.startsWith("#setport ")) {
    		Thread[] clientThreadList = server.getClientConnections();

    		if (clientThreadList.length > 0) {
                System.out.println("Cannot change port while client is connected");
    		} else if (!server.isListening()) { 
                try {
                    int newPort = Integer.parseInt(command.substring(9));
                    server.close(); 
                    server.setPort(newPort);
                    System.out.println("Server port set to: " + newPort);
                    server.listen(); 
                } catch (NumberFormatException e) {
                    System.out.println("Invalid port number.");
                } catch (IOException e) {
                    System.out.println("Error starting the server: " + e.getMessage());
                }
            } else {
                System.out.println("Cannot change port while the server is listening.");
            }
    	}
        else if (command.equals("#start")) {
            if (!server.isListening()) {
                try {
                    server.listen();
                    System.out.println("Server has started listening for new clients.");
                } catch (IOException e) {
                    System.out.println("Error starting the server: " + e.getMessage());
                }
            } else {
                System.out.println("Server is already listening.");
            }
        } else if (command.equals("#getport")) {
            System.out.println("Current server port: " + server.getPort());
        } else {
            System.out.println("Unrecognized command: " + command);
        }
    }
    // **** Changed for E50, LS
    @Override
    protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
        System.out.println(client + " > " + msg); 
    }
}
