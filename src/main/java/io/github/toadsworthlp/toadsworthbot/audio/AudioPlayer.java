package io.github.toadsworthlp.toadsworthbot.audio;

import java.util.HashMap;
import java.util.Map;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import io.github.toadsworthlp.toadsworthbot.Commander;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.audio.IAudioManager;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IVoiceChannel;
import sx.blah.discord.util.MissingPermissionsException;

public class AudioPlayer {

	private static AudioPlayerManager playerManager;
	private static Map<Long, GuildMusicManager> musicManagers;

	public static void setupAudio(IDiscordClient client){
		client.getDispatcher().registerListener(new AudioPlayer());
	}

	private AudioPlayer() {
		AudioPlayer.musicManagers = new HashMap<>();

		AudioPlayer.playerManager = new DefaultAudioPlayerManager();
		AudioSourceManagers.registerRemoteSources(playerManager);
		AudioSourceManagers.registerLocalSource(playerManager);
	}

	private synchronized static GuildMusicManager getGuildAudioPlayer(IGuild guild) {
		long guildId = Long.parseLong(guild.getID());
		GuildMusicManager musicManager = musicManagers.get(guildId);

		if (musicManager == null) {
			musicManager = new GuildMusicManager(playerManager);
			musicManagers.put(guildId, musicManager);
		}

		guild.getAudioManager().setAudioProvider(musicManager.getAudioProvider());

		return musicManager;
	}

	public static void loadAndPlay(final IChannel channel, final String trackUrl) {
		GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());

		musicManager.player.setVolume(10);

		playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
			@Override
			public void trackLoaded(AudioTrack track) {
				sendMessageToChannel(channel, "Zur Warteschlange hinzugefügt: " + track.getInfo().title);
				Commander.LogInfo("Zur Warteschlange hinzugefügt: " + track.getInfo().title);

				play(channel.getGuild(), musicManager, track);
			}

			@Override
			public void playlistLoaded(AudioPlaylist playlist) {
				AudioTrack firstTrack = playlist.getSelectedTrack();

				if (firstTrack == null) {
					firstTrack = playlist.getTracks().get(0);
				}

				sendMessageToChannel(channel, "Zur Warteschlange hinzugefügt: " + playlist.getName());
				Commander.LogInfo("Zur Warteschlange hinzugefügt: " + playlist.getName());

				for(AudioTrack track : playlist.getTracks()){
					play(channel.getGuild(), musicManager, track);
				}
			}

			@Override
			public void noMatches() {
				sendMessageToChannel(channel, "Kein Audio gefunden: " + trackUrl);
				Commander.LogWarn("Kein Audio gefunden: " + trackUrl);
			}

			@Override
			public void loadFailed(FriendlyException exception) {
				sendMessageToChannel(channel, "Konnte nicht abspielen: " + exception.getMessage());
				Commander.LogWarn("Konnte nicht abspielen: " + exception.getMessage());
			}
		});
	}

	public static void play(IGuild guild, GuildMusicManager musicManager, AudioTrack track) {
		musicManager.scheduler.queue(track);
	}

	public static void skipTrack(IChannel channel) {
		GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
		musicManager.scheduler.nextTrack();

		sendMessageToChannel(channel, "Track übersprungen.");
	}

	public static void skipToLast(IChannel channel) {
		GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
		int queueLength = musicManager.scheduler.getQueueLength();

		for(int i = 0; i < queueLength; i++){
			musicManager.scheduler.nextTrack();
		}

		sendMessageToChannel(channel, "Zum letzten Track gesprungen.");
	}

	public static void joinSpecificVoiceChannel(IAudioManager audioManager,IChannel channel , String name){
		if(audioManager.getGuild().getVoiceChannelsByName(name).size() > 0){
			for (IVoiceChannel voiceChannel : audioManager.getGuild().getVoiceChannelsByName(name)) {
				try {
					voiceChannel.join();
					Commander.LogInfo("Mit dem Voice-Channel " + name + " verbunden.");
				} catch (MissingPermissionsException e) {
					Commander.LogWarn("Konnte sich nicht mit dem Voice-Channel " + name + " verbinden.");
				}
			}
		}else{
			sendMessageToChannel(channel, "Voice-Channel mit dem Namen " + name +" existiert nicht. Ist der Channelname korrekt?");
		}
	}

	public static void sendMessageToChannel(IChannel channel, String message) {
		try {
			channel.sendMessage(message);
		} catch (Exception e) {
			Commander.LogWarn("Konnte die Nachricht " +message + " nicht zu " + channel.getName() + " senden!");
		}
	}

}
