package org.nova.utility.loading.playerutils;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;

import org.nova.game.player.DominionTower;
import org.nova.game.player.Player;
import org.nova.utility.loading.Logger;
import org.nova.utility.misc.Misc;


public final class DominionTowerRank implements Serializable {

	private static final long serialVersionUID = 5403480618483552509L;

	private String username;
	private long dominionFactor;
	private int mode;
	private String bossName;
	private int floorId;

	private static DominionTowerRank[] ranks;

	private static final String PATH = "data/playerdata/dtRanks.ser";

	public DominionTowerRank(Player player, int mode, String bossName) {
		this.username = player.getUsername();
		this.mode = mode;
		this.bossName = bossName;
		this.floorId = player.getDominionTower().getProgress();
		dominionFactor = player.getDominionTower().getTotalScore();
	}

	public static void showRanks(Player player) {
		player.interfaces().sendInterface(1158);
		int count = 0;
		for (DominionTowerRank rank : ranks) {
			if (rank == null)
				return;
			player.packets().sendIComponentText(1158, 9 + count * 5,
					Misc.formatPlayerNameForDisplay(rank.username));
			player.packets().sendIComponentText(
					1158,
					10 + count * 5,
					"On "
							+ (rank.mode == DominionTower.CLIMBER ? "climber"
									: "endurance") + ", reached floor "
							+ rank.floorId + ", killing: " + rank.bossName
							+ ".");
			player.packets().sendIComponentText(1158, 11 + count * 5,
					"DF:<br>" + rank.dominionFactor);
			count++;
		}
	}

	public static void init() {
		File file = new File(PATH);
		if (file.exists())
			try {
				ranks = (DominionTowerRank[]) SFiles
						.loadSerializedFile(file);
				return;
			} catch (Throwable e) {
				Logger.handle(e);
			}
		ranks = new DominionTowerRank[10];
	}

	public static final void save() {
		try {
			SFiles.storeSerializableClass(ranks, new File(
					PATH));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void sort() {
		Arrays.sort(ranks, new Comparator<DominionTowerRank>() {
			@Override
			public int compare(DominionTowerRank arg0, DominionTowerRank arg1) {
				if (arg0 == null)
					return 1;
				if (arg1 == null)
					return -1;
				if (arg0.dominionFactor < arg1.dominionFactor)
					return 1;
				else if (arg0.dominionFactor > arg1.dominionFactor)
					return -1;
				else
					return 0;
			}

		});
	}

	public static void checkRank(Player player, int mode, String boss) {
		long dominionFactor = player.getDominionTower().getTotalScore();
		for (int i = 0; i < ranks.length; i++) {
			DominionTowerRank rank = ranks[i];
			if (rank == null)
				break;
			if (rank.username.equalsIgnoreCase(player.getUsername())) {
				ranks[i] = new DominionTowerRank(player, mode, boss);
				sort();
				return;
			}
		}
		for (int i = 0; i < ranks.length; i++) {
			DominionTowerRank rank = ranks[i];
			if (rank == null) {
				ranks[i] = new DominionTowerRank(player, mode, boss);
				sort();
				return;
			}
		}
		for (int i = 0; i < ranks.length; i++) {
			if (ranks[i].dominionFactor < dominionFactor) {
				ranks[i] = new DominionTowerRank(player, mode, boss);
				sort();
				return;
			}
		}
	}

}
