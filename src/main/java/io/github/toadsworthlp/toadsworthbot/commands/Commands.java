package io.github.toadsworthlp.toadsworthbot.commands;

import com.darichey.discord.api.Command;
import com.darichey.discord.api.CommandRegistry;

import io.github.toadsworthlp.toadsworthbot.Commander;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

public class Commands {
	
	//Actual commands
	public static void setupCommands(IDiscordClient client){
		
		Command debug = new Command("debug")
		        .withDescription("Gibt Debuginformationen aus.")
		        .onExecuted(context ->
		            {
		            	try {
							context.getMessage().getChannel().sendMessage(
							"ClientName: " + client.getOurUser().getName()+System.lineSeparator()+
							"ClientID: " + client.getOurUser().getID()+System.lineSeparator()+
							"Sender ClientName: " + context.getMessage().getAuthor().getName()+System.lineSeparator()+
							"Sender ClientID: " + context.getMessage().getAuthor().getID()+System.lineSeparator()+
							"Sender Avatar URL: " + context.getMessage().getAuthor().getAvatarURL());
							//"Sender Roles: " + context.getMessage().getAuthor().getRolesForGuild(context.getMessage().getGuild()).toString());
						} catch (RateLimitException | MissingPermissionsException | DiscordException e) {
							e.printStackTrace();
						}
					}
		        );
		CommandRegistry.getRegistryForClient(client).register(debug);
		
		Command reloadConfig = new Command("reloadConfig")
		        .withDescription("Lädt die Befehlliste neu.")
		        .onExecuted(context ->
		            {	
		            	Commander.reloadConfig(client);
					}
		        );
		CommandRegistry.getRegistryForClient(client).register(reloadConfig);

	}

}
