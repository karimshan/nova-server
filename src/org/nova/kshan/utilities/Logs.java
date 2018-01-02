package org.nova.kshan.utilities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Date;

import org.nova.game.player.Player;
import org.nova.kshan.bot.Bot;

/**
 * Handles server logs.
 * 
 * @author Karimshan Nawaz
 *
 */
public class Logs {
	
	/**
	 * The path for the logger to write to.
	 */
	private String path;
	
	/**
	 * The constructor.
	 * 
	 * @param path
	 */
	public Logs(String path) {
		this.path = path;
	}
	
	/**
	 * Writes a player's action to their own log folder (Optional global log)
	 * @param msg
	 * @param type
	 * @param p
	 */
	public static void write(String msg, String type, Player p, boolean global) {
		if(p instanceof Bot) {
			return;
		}
		new Thread() {
			@Override
			public void run() {
				File folder = new File("data/logs/players/"+p.getUsername());
				if(!folder.exists())
					folder.mkdirs();
				File logType = new File(folder.getPath()+"/"+type+".txt");
				Logs log = new Logs(logType.getPath());
				log.addLog("["+new Date()+", "+p.getSession().getIP()+"] "+msg);
				if(global) {
					folder = new File("data/logs/global");
					if(!folder.exists())
						folder.mkdirs();
					logType = new File(folder.getPath()+"/"+type+".txt");
					log = new Logs(logType.getPath());
					log.addLog("["+new Date()+"] "+msg);
				}
			}
			
		}.start();
	}
	
	/**
	 * Writes a player's action to their own log folder (No global option)
	 * @param msg
	 * @param type
	 * @param p
	 */
	public static void write(String msg, String type, Player p) {
		write(msg, type, p, false);
	}
	
	/**
	 * Writes global logs.
	 * @param msg
	 * @param type
	 */
	public static void writeGL(String msg, String type) {
		new Thread() {
			@Override
			public void run() {
				File folder = new File("data/logs/global");
				if(!folder.exists())
					folder.mkdirs();
				File logType = new File(folder.getPath()+"/"+type+".txt");
				Logs log = new Logs(logType.getPath());
				log.addLog("["+new Date()+"] "+msg);
			}
			
		}.start();
	}
	
	/**
	 * Adds a log to the file.
	 * @param message
	 */
	public void addLog(String message) {
		try {
			BufferedWriter buffer = new BufferedWriter(new FileWriter(path, true));
			buffer.write(message+"\n");
			buffer.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
