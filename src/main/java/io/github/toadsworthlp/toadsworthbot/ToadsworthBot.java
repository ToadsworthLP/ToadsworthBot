package io.github.toadsworthlp.toadsworthbot;

import io.github.toadsworthlp.toadsworthbot.audio.AudioPlayer;
import javafx.application.Application;
import javafx.stage.Stage;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.util.DiscordException;

public class ToadsworthBot extends Application {
	
	public static IDiscordClient client;
	public static String[] autoconnectInfo;
	
	public static void main(String[] args){
		//Set autoconnect stuff
		if(args.length > 1){
			autoconnectInfo = args;
		}
		//Kick off everything
		Application.launch(args);
	}
	
	public static void createAndLogin(String token){
		//Create the client and log it in
		client = ToadsworthBot.createClient(token, true);
		
		//Setup commands and audio stuff
		Commander.setupCommands(client);
		AudioPlayer.setupAudio(client);
	}
	
	public static IDiscordClient createClient(String token, boolean login) { // Returns a new instance of the Discord client
	    ClientBuilder clientBuilder = new ClientBuilder(); // Creates the ClientBuilder instance
	    clientBuilder.withToken(token); // Adds the login info to the builder
	    clientBuilder.setDaemon(true);
	    try {
	        if (login) {
	            return clientBuilder.login(); // Creates the client instance and logs the client in
	        } else {
	            return clientBuilder.build(); // Creates the client instance but it doesn't log the client in yet, you would have to call client.login() yourself
	        }
	    } catch (DiscordException e) { // This is thrown if there was a problem building the client
	        e.printStackTrace();
	        return null;
	    }
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		//Create the Control Panel window and show it, wait for user to press "Start!"
		Window window = new Window(primaryStage);
		window.showControls();		//For some reason this doesn't work statically
	}
	
	//Stopping everything when closing the window
	@Override
	public void stop(){
	    System.exit(0);
	}
}
