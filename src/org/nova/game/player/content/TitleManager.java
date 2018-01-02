package org.nova.game.player.content;

import org.nova.game.player.Player;
/**
 * 
 * @author Fuzen Seth
 * @information Represents a title manager.
 * @since 22.6.2014
 */
public class TitleManager {
	
	private Player player;
	
	public TitleManager(Player player) {
		this.player = player;
	}
	/**
	 * Title instance.
	 */
	private Titles title;
	
	/**
	 * @author Fuzen Seth
	 * @information  Holds the title ids.
	 */
	public enum Titles {
		JR_CADET(1),SEARJANT(2),COMMMANDER(3),WAR_CHIEF(4),
		SIR(5),LORD(6),DUDERINO(7),LIONHEART(8),HELLRAISER(9),RUSADOR(10),
		DESPORADO(11),BARON(12),COUNT(13),OVERLORD(14),BANDITO(15),DUKE(16),
		KING(17),BIG_CHEESE(18),BIGWIG(19),WUNDERKIND(20),VYRELING(21),VYREGRUNT(22),
		VYREWATCH(23), VYRELORD(24), EMPEROR(26), PRINCE(27), WITCH_KING(28), ARCHON(29),
		JUSTICIAR(30), THE_AWESOME(31);
		
		private int titleId;
		
		private Titles(int titleId) {
		this.titleId = titleId;
		}

		public int getTitleId() {
			return titleId;
		}

		public void setTitleId(int titleId) {
			this.titleId = titleId;
		}
	}
	
	/**
	 * Get the titles.
	 * @return
	 */
	public Titles getTitle() {
		return title;
	}
	/**
	 * Removes the title from player.
	 * @param player
	 */
	public void removeTitle(Player player) {
		if (!(player.getAppearance().getTitle() > 0))  {
			player.getMatrixDialogues().startDialogue("SimpleMessage", "You don't have a title.");
			return;
		}
		player.getAppearance().setTitle(-1);
		player.getAppearance().getAppeareanceData();
		player.getMatrixDialogues().startDialogue("SimpleMessage", "Your title has been removed.");
}
	
	/**
	 * Adds player a title next to his username.
	 * @param player
	 * @param title
	 */
	public void setTitle(Player player, Titles title) {
		this.title = title;
		player.getAppearance().setTitle(title.getTitleId());
		player.getMatrixDialogues().startDialogue("SimpleMessage", "Your title has been changed.");
	}
	
}
