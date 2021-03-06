package io.github.toadsworthlp.toadsworthbot;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.darichey.discord.api.Command;
import com.darichey.discord.api.CommandRegistry;

import io.github.toadsworthlp.toadsworthbot.commands.AdvancedCommandMaker;
import io.github.toadsworthlp.toadsworthbot.commands.AudioCommands;
import io.github.toadsworthlp.toadsworthbot.commands.CoreCommands;
import io.github.toadsworthlp.toadsworthbot.commands.SimpleCommandMaker;
import io.github.toadsworthlp.toadsworthbot.commands.UtilCommands;
import sx.blah.discord.api.IDiscordClient;

public class Commander {
	
	public static final String INFO_PREFIX = "[INFO]  ";
	public static final String WARN_PREFIX = "[WARN]  ";
	public static final String ERR_PREFIX =  "[ERROR] ";
	
	public static String configURL;
	public static List<String> botConfig;
	
	public static IDiscordClient botClient;
	public static List<Command> defaultCommands;
	public static CommandRegistry commandRegistry;
	
	public static void setupCommands(IDiscordClient client){
		botClient = client;
		defaultCommands = CommandRegistry.getRegistryForClient(client).getCommands(); //This is used to initialize the command registry when reloading it
		
		//Set up all  kinds of commands
		CoreCommands.addCommands(client);

		//Download the config file and split it into lines, if it's okay then set up config-based commands
		botConfig = new ArrayList<String>(Arrays.asList(Utils.getContentFromURL(configURL).split("\\R", -1)));
		if(botConfig != null){
			AudioCommands.loadDJs();
			AudioCommands.djsActive = true;
			SimpleCommandMaker.addSimpleCommands(client);
			AdvancedCommandMaker.addCommands(client);
		}else{
			AudioCommands.djsActive = false;
			Commander.LogError("Konnte toadsworthbot.cfg nicht laden!");
		}
		
		//Set up all audio-related commands
		AudioCommands.addCommands(client);
		
		//Add commands to get help and get a command list.
		UtilCommands.addCommands(client);
	}
	
	//Mostly the same as setupCommands(), allows for command reload
	public static void reloadConfig(IDiscordClient client){
		CommandRegistry.getRegistryForClient(client).getCommands().clear();
		botConfig.clear();
		
		CommandRegistry.getRegistryForClient(client).getCommands().addAll(defaultCommands);

		CoreCommands.addCommands(client);
		
		
		botConfig = new ArrayList<String>(Arrays.asList(Utils.getContentFromURL(configURL).split("\\R", -1)));
		if(botConfig != null){
			AudioCommands.loadDJs();
			AudioCommands.djsActive = true;
			SimpleCommandMaker.addSimpleCommands(client);
			AdvancedCommandMaker.addCommands(client);
		}else{
			AudioCommands.djsActive = false;
			Commander.LogError("Konnte toadsworthbot.cfg nicht laden!");
		}
		
		AudioCommands.addCommands(client);
		
		UtilCommands.addCommands(client);
		
		Commander.LogInfo("toadsworthbot.cfg neu geladen!");
	}
	
	//Some convenience functions for printing messages into the control panel
	public static void Log(String prefix, String message){
		if(Window.logArea.getLength() > 5000){
			Window.logArea.clear();
		}
		Window.logArea.appendText("[" + LocalTime.now() + "]" + prefix + message + System.lineSeparator());
	}
	
	public static void LogInfo(String message){
		Log(INFO_PREFIX, message);
	}
	
	public static void LogWarn(String message){
		Log(WARN_PREFIX, message);
	}
	
	public static void LogError(String message){
		Log(ERR_PREFIX, message);
	}

}