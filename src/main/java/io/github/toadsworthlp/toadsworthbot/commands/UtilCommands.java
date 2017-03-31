package io.github.toadsworthlp.toadsworthbot.commands;

import java.util.Optional;

import com.darichey.discord.api.Command;
import com.darichey.discord.api.CommandRegistry;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

public class UtilCommands {
	
	//List and Help
	public static void addCommands(IDiscordClient client){
		
		Command liste = new Command("liste")
		        .withDescription("Listet alle verfügbaren Commands auf.")
		        .onExecuted(context ->
		            {
		            	try {
		            		String message = "Liste aller verfügbaren Commands:" + System.lineSeparator();
		            		for(int i=0;i<CommandRegistry.getRegistryForClient(client).getCommands().size();i++){
		            			String name = CommandRegistry.getRegistryForClient(client).getCommands().get(i).getName();
		            			String desc = CommandRegistry.getRegistryForClient(client).getCommands().get(i).getDescription();
		            			message += name + " : "+ desc + System.lineSeparator();
		            		}
		            		context.getMessage().getChannel().sendMessage(message);
						} catch (RateLimitException | MissingPermissionsException | DiscordException e) {
							e.printStackTrace();
						}
					}
		        );
		CommandRegistry.getRegistryForClient(client).register(liste);
		
		
		Command hilfe = new Command("hilfe")
		        .withDescription("Zeigt Hilfe an.")
		        .onExecuted(context ->
		            {
		            	try {
		            		if(context.getArgs().length > 0){
			            		Optional<Command> cmdDesc = CommandRegistry.getRegistryForClient(client).getCommandByName(context.getArgs()[0], true);
			            		if(cmdDesc.isPresent()){
			            			context.getMessage().getChannel().sendMessage("Hilfe für Command: " + cmdDesc.get().getName());
			            			context.getMessage().getChannel().sendMessage(cmdDesc.get().getDescription());
			            		}else{
			            			context.getMessage().getChannel().sendMessage("Kein gültiger Command. Für eine Liste von verfügbaren Commands benutze !list.");
			            		}
		            		}else{
		            			context.getMessage().getChannel().sendMessage("Kein gültiger Command. Verwendung: !hilfe <Name des Commands>");
		            			context.getMessage().getChannel().sendMessage("Für eine Liste von verfügbaren Commands benutze !liste.");
		            		}
						} catch (RateLimitException | MissingPermissionsException | DiscordException e) {
							e.printStackTrace();
						}
					}
		        );
		CommandRegistry.getRegistryForClient(client).register(hilfe);
	}

}
