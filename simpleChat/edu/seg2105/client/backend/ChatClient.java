// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package edu.seg2105.client.backend;

import ocsf.client.*;
import java.io.*;

import common.ChatIF;

/*
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 */
public class ChatClient extends AbstractClient{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 
  

  private boolean isConnected;
  private String loginId;
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String host, int port, ChatIF clientUI,String loginId) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    this.loginId = loginId;
    this.isConnected = false;
    
    
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
  /*
   * handle user command
   */
  public void handleCommand(String command) throws IOException{
	  String[] com = command.split(" "); //split string
	  
	  switch(com[0]) {  //first string == commands
	  
	  //#quit Causes the client to terminate gracefully. Make sure the connection to the server is terminated before exiting the program.
	  	case "#quit":  //command #quit terminate
	  		quit();
	  		break;
	  		
	  		
	  		//#logoff Causes the client to disconnect from the server, but not quit
	  	case "#logoff":      //coommand logoff 
	  		try {closeConnection(); }
	  		catch(IOException e){clientUI.display("Error when trying to disconnect from the server ");}
	  		break;
	  		
	  		
	     //#sethost <host> Calls the setHost method in the client. Only allowed if the client is logged off; displays an error message otherwise.
	  	case "#sethost":    // command set host
	  		if(isConnected() == false) {
	  			if(com.length >1) {
	  				setHost(com[1]);  
	  				 clientUI.display("The host is set to: " + getHost());
	  			}
	  			else {clientUI.display("#sethost <host>");}
	  		} 
	  		else { throw new IOException("Error, please log off before setting host.");}
	  		break;
	  	
	  		
	  		//#setport <port> Calls the setPort method in the client, with the same constraints as #sethost.
	  	case "#setport":     //command set port
	  		if(isConnected == false) {
	  			if(com.length > 1) {
	  				
	  				try {
	  					int port = Integer.parseInt(com[1]);
	  					setPort(port);
	  					clientUI.display("The port is set to: " + getPort());
	  				}
	 
	  				catch(NumberFormatException e) {clientUI.display("Invalid port, the port needs to be an integer.");}
	  			} else {clientUI.display("#setport <port>");}
	  		} 
	  		else {throw new IOException("Error, please log off before setting port");}
	  		break;
	  		
	  		
	  	//#login Causes the client to connect to the server. Only allowed if the client is not already connected; displays an error message otherwise.
	  	case "#login":    //command login
	  		if(isConnected == false) {
	  			try {
	  				openConnection();
	  			} catch(IOException e) {clientUI.display("Could not connect to the server.");}
	  			
	  		} else {clientUI.display("You are already connected to the server");}
	  		break;
	  		
	  		//#gethost Displays the current host name
	  	case "#gethost":    //command to get the current host 
	  		clientUI.display("The current host is: " + getHost());
	  		break;
	  		
	  	//#getport Displays the current port number.
	  	case "#getport":     //command to get the current port
	  		clientUI.display("The current port is: " + getPort());
	  		break;
	  		
	    default : //if command not in the above 
	    	clientUI.display("Command is unknown: " + command);
	  }
  }

  /**
   * This method handles all data coming from the UI and commands            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message){
	  
    try
    {
      if (message.startsWith("#") ) {
    	  
    	  handleCommand(message);
    	  
      } else {
    	  try {sendToServer(message);}
    	  catch(IOException e) {clientUI.display("Message could not be sent to server. Shutting down client");
    	  	quit();
    	  }
    	  
      }
    }
    catch(IOException e)
    {
      clientUI.display
        ("Could not send message to server.  Terminating client.");
      quit();
    }
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


/**
 * Hook method called each time an exception is thrown by the client's
 * thread that is waiting for messages from the server. The method may be
 * overridden by subclasses.
 * 
 * @param exception
 *            the exception raised.
 */
  	@Override
	protected void connectionException(Exception exception) {
  	    isConnected = false;
		clientUI.display("The server is shut down");
		System.exit(0);
	}
  	
	/**
	 * Hook method called after the connection has been closed. The default
	 * implementation does nothing. The method may be overriden by subclasses to
	 * perform special processing such as cleaning up and terminating, or
	 * attempting to reconnect.
	 */
  	@Override
	protected void connectionClosed() {
  	    isConnected = false;
  		clientUI.display("Connection is closed");
	}
  	
	/**
	 * Hook method called after a connection has been established. The default
	 * implementation does nothing. It may be overridden by subclasses to do
	 * anything they wish.
	 */
  	@Override
	protected void connectionEstablished() {
  	    isConnected = true;
  		clientUI.display("The client is connected to the server");
  		
  		
  		if(loginId != null && !loginId.isEmpty()) {
  			try {
  			    
  				sendToServer("#login " + loginId);  //the #login is sent to the server 
  			}
  			catch(IOException e) { 
  				clientUI.display("The loginId could not be sent to the server"); 
  			}
  		} else {
  			
  			clientUI.display("Error: No login ID available");
  		}
	}

}
//End of ChatClient class