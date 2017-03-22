package io.github.toadsworthlp.toadsworthbot.commands;

import java.util.ArrayList;
import java.util.List;

import com.darichey.discord.api.Command;
import com.darichey.discord.api.CommandRegistry;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;
import io.github.toadsworthlp.toadsworthbot.Commander;
import io.github.toadsworthlp.toadsworthbot.Utils;
import io.github.toadsworthlp.toadsworthbot.audio.AudioPlayer;

public class AudioCommands {
	
	public static List<String> djnames = new ArrayList<String>();
	
	public static void loadDJs(){
		String[] djs = Utils.getContentFromURL("https://toadsworthlp.github.io/Files/djlist.txt", "Toadsworth").split("\\R", -1);
		if(!djnames.isEmpty()){
			djnames.clear();
		}
		for(int i = 0; i < djs.length; i++){
			djnames.add(djs[i]);
		}
		Commander.LogInfo("DJs registriert.");
	}
	
	public static void addAudioCommands(IDiscordClient client){
		
		Command play = new Command("play")
		        .withDescription("Spielt Musik ab. Verwendung: !play <YouTube Link>")
		        .onExecuted(context ->
		            {
		            	if(djnames.contains(context.getMessage().getAuthor().getName())){
		            		AudioPlayer.loadAndPlay(context.getMessage().getChannel(), context.getArgs()[0]);
		            	}else{
		            		Commander.LogWarn(context.getMessage().getAuthor().getName() + " wollte den Track " + context.getArgs()[0] + " abspielen, ist aber kein DJ!");
		            		try {
								context.getMessage().getChannel().sendMessage("Du bist kein registrierter DJ!");
							} catch (MissingPermissionsException | RateLimitException | DiscordException e) {
								e.printStackTrace();
							}
		            	}
					}
		        );
		CommandRegistry.getRegistryForClient(client).register(play);
		
		Command skip = new Command("skip")
		        .withDescription("Überspringt den aktuell laufenden Track")
		        .onExecuted(context ->
		            {
		            	if(djnames.contains(context.getMessage().getAuthor().getName())){
		            		AudioPlayer.skipTrack(context.getMessage().getChannel());
		            	}else{
		            		Commander.LogWarn(context.getMessage().getAuthor().getName() + " wollte den aktuellen Track überspringen, ist aber kein DJ!");
		            		try {
								context.getMessage().getChannel().sendMessage("Du bist kein registrierter DJ!");
							} catch (MissingPermissionsException | RateLimitException | DiscordException e) {
								e.printStackTrace();
							}
		            	}
					}
		        );
		CommandRegistry.getRegistryForClient(client).register(skip);
		
		Command last = new Command("last")
		        .withDescription("Springt zum zuletzt hinzugefügten Track der Warteschlange.")
		        .onExecuted(context ->
		            {
		            	if(djnames.contains(context.getMessage().getAuthor().getName())){
		            		AudioPlayer.skipToLast(context.getMessage().getChannel());
		            	}else{
		            		Commander.LogWarn(context.getMessage().getAuthor().getName() + " wollte zum letzten Track springen, ist aber kein DJ!");
		            		try {
								context.getMessage().getChannel().sendMessage("Du bist kein registrierter DJ!");
							} catch (MissingPermissionsException | RateLimitException | DiscordException e) {
								e.printStackTrace();
							}
		            	}
					}
		        );
		CommandRegistry.getRegistryForClient(client).register(last);
		
		Command channel = new Command("channel")
		        .withDescription("Verschiebt den Musikbot in den angegeben Voice-Channel.")
		        .onExecuted(context ->
		            {
		            	if(djnames.contains(context.getMessage().getAuthor().getName())){
		            	AudioPlayer.joinSpecificVoiceChannel(context.getMessage().getGuild().getAudioManager(),context.getMessage().getChannel() ,context.getArgs()[0]);;
		            	}else{
		            		Commander.LogWarn(context.getMessage().getAuthor().getName() + " wollte den Bot in den Voice-Channel " + context.getArgs()[0] + " verschieben, ist aber kein DJ!");
		            		try {
								context.getMessage().getChannel().sendMessage("Du bist kein registrierter DJ!");
							} catch (MissingPermissionsException | RateLimitException | DiscordException e) {
								e.printStackTrace();
							}
		            	}
		            }
		        );
		CommandRegistry.getRegistryForClient(client).register(channel);
		
		Command reloadDjs = new Command("reloadDjs")
		        .withDescription("Aktualisiert die Liste der DJs.")
		        .onExecuted(context ->
		            {
		            	if(djnames.contains(context.getMessage().getAuthor().getName())){
		            		loadDJs();
		            	}else{
		            		Commander.LogWarn(context.getMessage().getAuthor().getName() + " wollte den Bot in den Voice-Channel " + context.getArgs()[0] + " verschieben, ist aber kein DJ!");
		            		try {
								context.getMessage().getChannel().sendMessage("Du bist kein registrierter DJ!");
							} catch (MissingPermissionsException | RateLimitException | DiscordException e) {
								e.printStackTrace();
							}
		            	}
		            }
		        );
		CommandRegistry.getRegistryForClient(client).register(reloadDjs);
		
	}

}
