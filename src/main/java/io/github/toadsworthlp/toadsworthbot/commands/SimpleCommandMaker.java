package io.github.toadsworthlp.toadsworthbot.commands;

import java.util.List;

import com.darichey.discord.api.Command;
import com.darichey.discord.api.CommandRegistry;

import io.github.toadsworthlp.toadsworthbot.Commander;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

public class SimpleCommandMaker {
	
	public static void addSimpleCommands(IDiscordClient client){
		int loaded = 0;		//Variable to keep track of the simple commands successfully loaded
		List<String> lines = Commander.botConfig;		//Get the config lines
		for(int i = 0; i < lines.size(); i++){		//For each line...
			if(!lines.get(i).endsWith(".js") && !lines.get(i).endsWith(".dj")){		//...if it isn't pointing to a JavaScript or DJ file...
				String cmdname;
				String desc;
				String out;
				String[] raw = lines.get(i).split(";");		//...split it and...
				
				if(raw.length==3){		//...store it in temporary variables.
					cmdname = raw[0];
					desc = raw[1];
					out = raw[2];
					
					Command cmd = new Command(cmdname)
					        .withDescription(desc)
					        .onExecuted(context ->		//Register the command based on the config
					            {
					            	try {
					            		String msg;
					            		//Set message to the loaded response
					            		msg = out;
					            		//If arguments exist, replace %a with the first argument
					            		if(context.getArgs().length > 0){msg = msg.replaceAll("%a", context.getArgs()[0]);}else{msg = msg.replaceAll("%a", "");}
					            		//Replace %u with the name of the sender
					            		msg = msg.replaceAll("%u", context.getMessage().getAuthor().getDisplayName(context.getMessage().getGuild()));
					            		//Replace %n with a line break
					            		msg = msg.replaceAll("%n", System.lineSeparator());
					            		//Post the message
					            		context.getMessage().getChannel().sendMessage(msg);
									} catch (RateLimitException | MissingPermissionsException | DiscordException e) {
										e.printStackTrace();
									}
								}
					        );
					CommandRegistry.getRegistryForClient(client).register(cmd);		//Actually registers the command
					loaded++;		//Increases the loaded counter
				}else{
					Commander.LogError("Fehler beim Lesen von Zeile "+i+" von toadsworthbot.cfg!");
				}
				
			}
			
		}
		
		Commander.LogInfo(loaded+" simple(n) Command(s) von "+Commander.configURL+" geladen.");

	}

}
