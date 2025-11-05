package edu.seg2105.edu.server.backend;
// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 


import java.io.IOException;

import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 */
public class EchoServer extends AbstractServer 
{
  //Class variables *************************************************
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  
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
  public void handleMessageFromClient (Object msg, ConnectionToClient client) {
	  
	 String message = msg.toString();
	 
	 if(message.startsWith("#login ")) {
		 System.out.println("Message received: " +message + " from "+ client.getInfo("loginId"));  //test case 2004
		 if(client.getInfo("loginId") != null) { //clients already has a loginId
			 try {
				 client.sendToClient("Error: login command already used. The loginId command cannot be implemented more than once. Terminating client immediately");
				 client.close();
				}
			 catch(IOException e) {}
			 return;
		 } 
		 
		 String[] com = message.split(" ", 2);   //  /2 the String
		 if  (com.length<2) {
			 try {
			client.sendToClient("Error:  missing the loginID");
			 client.close();
			 }
			 catch(IOException e) {}
			 return;
		 }
		 
		 String loginId = com[1];  //get value of loginId
		 client.setInfo("loginId", loginId); 
		 System.out.println(loginId+ " has logged on.");             // server see client is logged in
		 this.sendToAllClients(loginId + " has logged on.");       // server send to all client that the client is logged in
	 } 
	 else {  //else == not a logni message received == terminate connection
		 
		 if( client.getInfo("loginId") == null) {
			 try {
				 client.sendToClient("Error: you nee to login in first");
				 client.close();
				 
			 }
			 catch(IOException e) {}
			 return;
			 
		 }
		 
		 String loginId = (String) client.getInfo("loginId");
		 System.out.println("Message received: " + message + " from  " + loginId);    //prefixed by the ID
		 this.sendToAllClients(loginId + "-> "+ message );
	 }
	 
  } 
  //handle message from ServerConsole
  public void  handleMessageFromServerUI(String message) throws IOException {
	  if (message.startsWith("#")){
		   handleServerCommand(message);
		   
	  } else {      // Any message originating from the end-user of the server should be prefixed by the string "SERVER MSG>
		  System.out.println("SERVER MSG-> " + message);
		  this.sendToAllClients( "SERVER MSG-> " +  message);
	  }
  }
  
  
  
  
  //commands
  private void handleServerCommand(String command) throws IOException {
	  String[] com = command.split(" ");  //split the string
	  
	  switch(com[0]) {
	  
	  //#quit Causes the server to quit gracefully.
	  	case "#quit":  //command #quit terminate
	  		try{close();}
	  		catch(IOException e){   }
	  		System.exit(0);
	  		break;
	  		
	  	//#stop Causes the server to stop listening for new clients.
	  	case "#stop":      //coommand stop
	  		stopListening();
	  		break;
	  		
	  	//#close Causes the server not only to stop listening for new clients, but also to disconnect all existing clients.
	  	case "#close":    // command close
	  		try {close();}
	  		catch(IOException e) {System.out.println("Error while trying to close the server:  "+ e.getMessage());}
	  		break;
	  	//#setport <port> Calls the setPort method in the server. Only allowed if the server is closed.
	  	case "#setport":     //command set port
	  		if(isListening() == false) {
	  			
	  				try {
	  					int port = Integer.parseInt(com[1]);
	  					setPort(port);
	  					System.out.println("The port is set to:  " + getPort());
	  				}
	  				catch(NumberFormatException  e) {System.out.println("Invalid port, the port needs to be an integer.");}
	  				
	  			
	  		} else {
	  			System.out.println( "The server is still open. The server must be close before setting a port");
	  		}
	  		
	  		break;
	  		
	  	//#start Causes the server to start listening for new clients. Only valid if the server is stopped.
	  	case "#start":    //command start
	  		
	  		if(isListening() == false) {
	  			try {listen();}
	  			catch(IOException e) {System.out.println("Error trying to start the server: " + e.getMessage());}
	  		}else {
	  			System.out.println("The server is already listening");
	  		}
	  		break;
	  		
	  	//#getport Displays the current port number.
	  	case "#getport":     //command to get the current port
	  		System.out.println("The current port is: " + getPort());
	  		break;
	  	
	  		
	    default : //if command not in the above 
	    	System.out.println("Command is unknown" );
	    
	  }
  }
    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  @Override
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  @Override
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
  }
  
  @Override
	protected void clientConnected(ConnectionToClient client) {
		System.out.println("A new client is connected to the server");
	}
	
	 @Override
	synchronized protected void clientDisconnected(ConnectionToClient client) {
		// Since we don't track which ID belongs to this client directly,
		// remove by value.
		String loginId = (String) client.getInfo("loginId"); 
		if(loginId != null) {
			System.out.println(loginId + " has disconnected");
			this.sendToAllClients(loginId + " has disconnected");
		} else {
			 System.out.println("A client has disconnected");
		}
		
	}
	
	
	/* 
	 */
	@Override
	synchronized protected void clientException(ConnectionToClient client, Throwable exception) {
		
		if(client.getInfo("loginId") != null) { System.out.println("Exception for " +client.getInfo("loginId") +  ": "+ exception.getMessage()); }
		else {System.out.println("Client exception: " + exception.getMessage() ); }
	}
		
	
	
	
	@Override
	protected void listeningException(Throwable exception) {
		System.out.println("Listen exception: "  + exception.getMessage());
	}
  
  
  //Class methods ***************************************************
  
	
	
	
	
	
	
	
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */
  public static void main(String[] args) 
  {
    int port = 0; //Port to listen on

    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t){
    	
      port = DEFAULT_PORT; //Set port to 5555
    }
	
    EchoServer sv = new EchoServer(port);
   
    try 
    {
      sv.listen(); //Start listening for connections
    } 
    catch (Exception ex) {
    	
      System.out.println("ERROR - Could not listen for clients!");
    }
    
    //start server console
    ServerConsole console = new ServerConsole(sv);
    console.accept();
  }
}
//End of EchoServer class