package io.github.toadsworthlp.toadsworthbot.commands;

import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.darichey.discord.api.Command;
import com.darichey.discord.api.CommandRegistry;

import io.github.toadsworthlp.toadsworthbot.Commander;
import io.github.toadsworthlp.toadsworthbot.Utils;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

public class AdvancedCommandMaker {
	
	//Mostly the same as SimpleCommandMaker.addSimpleCommands
	public static void addCommands(IDiscordClient client){
		int loaded = 0;
		List<String> lines = Commander.botConfig;
		for(int i = 0; i < lines.size(); i++){
			if(lines.get(i).endsWith(".js")){
				
				String cmdname;
				String desc;
				String script;
				String[] raw = lines.get(i).split(";");
				
				if(raw.length==3){
					cmdname = raw[0];
					desc = raw[1];
					script = Utils.getContentFromURL(Commander.configURL.replaceAll("toadsworthbot.cfg", "")+raw[2]);
					
					Command cmd = new Command(cmdname)
					        .withDescription(desc)
					        .onExecuted(context ->
					            {
					            	//Creates a new ScriptEngineManager
					            	ScriptEngineManager factory = new ScriptEngineManager();
				                    //Create a JavaScript engine
				                    ScriptEngine engine = factory.getEngineByName("JavaScript");
				                    //Evaluate JavaScript code from String
				                    try {
				                    	engine.put("context", context);		//Allow accessing the context using JavaScript
										engine.eval(script);
									} catch (ScriptException e) {
										Commander.LogWarn("Fehler beim Ausführen des Scripts: "+e.toString());
										try {
											context.getMessage().getChannel().sendMessage("Fehler beim Ausführen des Commands.");
										} catch (MissingPermissionsException | RateLimitException | DiscordException e1) {
											e1.printStackTrace();
										}
									}
								}
					        );
					CommandRegistry.getRegistryForClient(client).register(cmd);
					loaded++;
				}else{
					Commander.LogError("Fehler beim Lesen von Zeile "+i+" von toadsworthbot.cfg!");
				}
				
			}
			
		}
		
		Commander.LogInfo(loaded+" Script-Command(s) von "+Commander.configURL+" geladen.");

	}

}
