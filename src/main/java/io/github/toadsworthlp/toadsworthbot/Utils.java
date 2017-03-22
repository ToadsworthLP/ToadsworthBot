package io.github.toadsworthlp.toadsworthbot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class Utils {
	
	public static int randomNumber(int min, int max){
		int randomNum = ThreadLocalRandom.current().nextInt(min, max + 1);
		return randomNum;
	}
	
	public static String getContentFromURL(String site){
		URL url;
		try {
			url = new URL(site);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
			Commander.LogError("Ungültige URL: " + site);
			return null;
		}
		
		URLConnection conn;
		try {
			conn = url.openConnection();
		} catch (IOException e) {
			e.printStackTrace();
			Commander.LogError("Konnte sich nicht mit der URL " + url.toString() + " verbinden!");
			return null;
		}
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
		    return reader.lines().collect(Collectors.joining("\n"));
		} catch (IOException e) {
			e.printStackTrace();
			Commander.LogError("Beim Lesen der Website " + url.toString() + " ist ein Fehler aufgetreten.");
			return null;
		}
	}
	
	public static String getContentFromURL(String site, String defaultText){
		URL url;
		try {
			url = new URL(site);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
			Commander.LogError("Ungültige URL: " + site);
			return defaultText;
		}
		
		URLConnection conn;
		try {
			conn = url.openConnection();
		} catch (IOException e) {
			e.printStackTrace();
			Commander.LogError("Konnte sich nicht mit der URL " + url.toString() + " verbinden!");
			return defaultText;
		}
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
		    return reader.lines().collect(Collectors.joining("\n"));
		} catch (IOException e) {
			e.printStackTrace();
			Commander.LogError("Beim Lesen der Website " + url.toString() + " ist ein Fehler aufgetreten.");
			return defaultText;
		}
	}

}
