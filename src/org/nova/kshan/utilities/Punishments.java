package org.nova.kshan.utilities;

import java.io.File;
import java.util.concurrent.CopyOnWriteArrayList;

import org.nova.game.Game;
import org.nova.game.player.Player;
import org.nova.utility.loading.playerutils.SFiles;
import org.nova.utility.misc.Misc;

/**
 * Represents punishments.
 * 
 * @author Karimshan Nawaz
 *
 */
public class Punishments {
	
	/**
	 * The three permanent punishment arrays.
	 */
	private static CopyOnWriteArrayList<String> bannedIPs, bannedMACAddresses, mutedIPs;
	
	/**
	 * The path where the files exist.
	 */
	private static final String PATH = "data/playerdata/";
	
	/**
	 * returns whether the files have been modified or not.
	 */
	private static boolean modifiedMACBans, modifiedIPBans, modifiedIPMutes;
	
	/**
	 * Loads the permanent punishments.
	 */
	@SuppressWarnings("unchecked")
	public static void loadPermanentPunishments() {
		try {
			File bannedIPsPath = new File(PATH + "IPBans.ser");
			File bannedMACsPath = new File(PATH + "MACBans.ser");
			File mutedIPsPath = new File(PATH + "IPMutes.ser");
			if(!bannedIPsPath.exists())
				bannedIPsPath.createNewFile();
			if(!bannedMACsPath.exists())
				bannedMACsPath.createNewFile();
			if(!mutedIPsPath.exists())
				mutedIPsPath.createNewFile();
			if(bannedIPsPath.exists() && bannedIPsPath.length() > 0)
				bannedIPs = (CopyOnWriteArrayList<String>) SFiles.loadSerializedFile(bannedIPsPath);
			if(bannedMACsPath.exists() && bannedMACsPath.length() > 0)
				bannedMACAddresses = (CopyOnWriteArrayList<String>) SFiles.loadSerializedFile(bannedMACsPath);
			if(mutedIPsPath.exists() && mutedIPsPath.length() > 0)
				mutedIPs = (CopyOnWriteArrayList<String>) SFiles.loadSerializedFile(mutedIPsPath);
			if(bannedIPs == null)
				bannedIPs = new CopyOnWriteArrayList<String>();
			if(bannedMACAddresses == null)
				bannedMACAddresses = new CopyOnWriteArrayList<String>();
			if(mutedIPs == null)
				mutedIPs = new CopyOnWriteArrayList<String>();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Saves all of the files.
	 */
	public static final void saveFiles() {
		try {
			if(modifiedMACBans)
				SFiles.storeSerializableClass(bannedMACAddresses, new File(PATH + "MACBans.ser"));
			if(modifiedIPBans)
				SFiles.storeSerializableClass(bannedIPs, new File(PATH + "IPBans.ser"));
			if(modifiedIPMutes)
				SFiles.storeSerializableClass(mutedIPs, new File(PATH + "IPMutes.ser"));
			modifiedMACBans = false;
			modifiedIPBans = false;
			modifiedIPMutes = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param player
	 */
	public static void applyIPBan(Player player) {
		boolean sessionExists = player.getSession() != null;
		String ip = sessionExists ? player.getIP() : player.getLastIP();
		player.setPermBanned(true);
		if(!bannedIPs.contains(ip))
			bannedIPs.add(ip);
		if(sessionExists)
			for(Player p : Game.getPlayers())
				if(p != null)
					if(p.getIP().equals(ip))
						p.getSession().getChannel().disconnect();
		SFiles.savePlayer(player);
		modifiedIPBans = true;
	}
	
	/**
	 * 
	 * @param player
	 */
	public static void applyMACBan(Player player) {
		boolean sessionExists = player.getSession() != null;
		player.setPermBanned(true);
		if(!bannedMACAddresses.contains(player.getMACAddress()))
			bannedMACAddresses.add(player.getMACAddress());
		if(sessionExists)
			for(Player p : Game.getPlayers())
				if(p != null)
					if(p.getMACAddress().equals(player.getMACAddress()))
						p.getSession().getChannel().disconnect();
		SFiles.savePlayer(player);
		modifiedMACBans = true;
	}
	
	/**
	 * 
	 * @param player
	 */
	public static void applyIPMute(Player player) {
		player.setMuted(Long.MAX_VALUE);
		boolean sessionExists = player.getSession() != null;
		String ip = sessionExists ? player.getIP() : player.getLastIP();
		if(!mutedIPs.contains(ip))
			mutedIPs.add(ip);
		SFiles.savePlayer(player);
		modifiedIPMutes = true;
	}

	/**
	 * 
	 * @param player
	 */
	public static void removeIPBan(Player player) {
		bannedIPs.remove(player.getLastIP());
		player.setPermBanned(false);
		modifiedIPBans = true;
		saveFiles();
	}
	
	/**
	 * 
	 * @param player
	 */
	public static void removeBan(Player player) {
		player.setPermBanned(false);
		player.setBannedDelay(0);
	}
	
	/**
	 * 
	 * @param player
	 */
	public static void removeIPMute(Player player) {
		boolean sessionExists = player.getSession() != null;
		String ip = sessionExists ? player.getIP() : player.getLastIP();
		player.setMuted(0);
		mutedIPs.remove(ip);
		modifiedIPMutes = true;
		saveFiles();
	}
	
	/**
	 * 
	 * @param player
	 */
	public static void removeMACBan(Player player) {
		player.setPermBanned(false);
		player.setBannedDelay(0);
		bannedMACAddresses.remove(player.getMACAddress());
		modifiedMACBans = true;
		saveFiles();
	}
	
	/**
	 * 
	 * @param ip
	 * @return
	 */
	public static boolean isIPBanned(String ip) {
		if(bannedIPs == null || bannedIPs.isEmpty())
			return false;
		else
			return bannedIPs.contains(ip);
	}
	
	/**
	 * 
	 * @param player
	 * @return
	 */
	public static boolean isPermMuted(Player player) {
		return player.getMutedDelay() > (Misc.currentTimeMillis() + (TimeUtils.getDays(7)));
	}
	
	/**
	 * 
	 * @param macAddress
	 * @return
	 */
	public static boolean isMACBanned(String macAddress) {
		if(bannedMACAddresses == null || bannedMACAddresses.isEmpty())
			return false;
		else
			return bannedMACAddresses.contains(macAddress);
	}
	
	/**
	 * 
	 * @param ip
	 * @return
	 */
	public static boolean isIPMuted(String ip) {
		if(mutedIPs == null || mutedIPs.isEmpty())
			return false;
		else
			return mutedIPs.contains(ip);
	}
	
	/**
	 * 
	 * @return
	 */
	public static CopyOnWriteArrayList<String> getBannedIPs() {
		return bannedIPs;
	}
	
	/**
	 * 
	 * @return
	 */
	public static CopyOnWriteArrayList<String> getBannedMACAddresses() {
		return bannedMACAddresses;
	}
	
	/**
	 * 
	 * @return
	 */
	public static CopyOnWriteArrayList<String> getMutedIPs() {
		return mutedIPs;
	}

}
