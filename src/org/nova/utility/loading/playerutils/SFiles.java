package org.nova.utility.loading.playerutils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.nova.game.player.Player;
import org.nova.utility.loading.Logger;
import org.nova.utility.misc.Misc;


public class SFiles {

	private static final String PATH = "data/chars/saves/";
	private static final String BACKUP_PATH = "data/chars/backups/";

	public synchronized static final boolean containsPlayer(String username) {
		return new File(PATH + username + ".player").exists();
	}

	public synchronized static final File getPlayer(String username) {
		return new File(PATH + username + ".player");
	}
	
	public synchronized static Player loadPlayer(String username) {
		try {
			return (Player) loadSerializedFile(new File(PATH + username + ".player"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			Logger.log("SerializableFilesManager", "Recovering account: "
					+ username);
			return (Player) loadSerializedFile(new File(BACKUP_PATH + username
					+ ".player"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean createBackup(String username) {
		try {
			Misc.copyFile(new File(PATH + username + ".player"), new File(
					BACKUP_PATH + username + ".player"));
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public synchronized static void savePlayer(Player player) {
		try {
			storeSerializableClass(player, new File(PATH + player.getUsername() + ".player"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final Object loadSerializedFile(File f) throws IOException,
			ClassNotFoundException {
		if (!f.exists())
			return null;
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(f));
		Object object = in.readObject();
		in.close();
		return object;
	}

	public static final void storeSerializableClass(Serializable o, File f) throws IOException {
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(f));
		out.writeObject(o);
		out.close();
	}

	private SFiles() {

	}

}
