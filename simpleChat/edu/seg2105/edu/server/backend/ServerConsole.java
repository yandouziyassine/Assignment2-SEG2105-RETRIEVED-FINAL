package edu.seg2105.edu.server.backend;
import java.util.Scanner;
import common.ChatIF;
 
public class ServerConsole implements ChatIF {
	EchoServer server;
	
	public ServerConsole(EchoServer server) {
		this.server = server;
		
	}
	
	public void display(String message) {
		System.out.println(message);
	}
	
	
	
	public void accept() {
		try  {
			Scanner fromConsole = new Scanner(System.in);
			String message;
			
			while(true) {
				 message  = fromConsole.nextLine();
				server.handleMessageFromServerUI(message ); 
			}
		}
		catch(Exception e) {System.out.println("Error while reading the message from console");}
	}
	
	
	//main already in echoServer no need to add in ServerConsole
}