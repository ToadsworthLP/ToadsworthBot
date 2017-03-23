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
	
	public static List<String> djIds = new ArrayList<String>();
	public static boolean djsActive = true;
	
	public static void loadDJs(){
		if(!djIds.isEmpty()){
			djIds.clear();
		}
		
		List<String> djfiles = new ArrayList<String>();
		for(int i = 0; i < Commander.botConfig.size(); i++){
			if(Commander.botConfig.get(i).endsWith(".dj")){
				String file = Utils.getContentFromURL(Commander.configURL.replaceAll("toadsworthbot.cfg", "") + Commander.botConfig.get(i).split(";")[1]);
				djfiles.add(file);
			}
		}
		
		if(djfiles.size() > 0){
			for(int i = 0; i < djfiles.size(); i++){
				String[] ids = djfiles.get(i).split("\\R", -1);
				for(int j = 0; j < ids.length; j++){
					djIds.add(ids[j]);
				}
			}
		Commander.LogInfo("DJs registriert.");
		}else{
			Commander.LogWarn("Keine DJs eingetragen! DJ-Funktionen deaktiviert.");
			djsActive = false;
		}
	}
	
	public static void addAudioCommands(IDiscordClient client){
		
		Command play = new Command("play")
		        .withDescription("Spielt Musik ab. Verwendung: !play <YouTube Link>")
		        .onExecuted(context ->
		            {
		            	if(djIds.contains(context.getMessage().getAuthor().getID()) || !djsActive){
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
		            	if(djIds.contains(context.getMessage().getAuthor().getID()) || !djsActive){
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
		            	if(djIds.contains(context.getMessage().getAuthor().getID()) || !djsActive){
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
		            	if(djIds.contains(context.getMessage().getAuthor().getID()) || !djsActive){
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
		            	if(djIds.contains(context.getMessage().getAuthor().getID()) || !djsActive){
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
