package org.nova.kshan.content.interfaces;

import org.nova.game.Game;
import org.nova.game.engine.GameTick;
import org.nova.game.player.Player;

/**
 * 
 * @author K-Shan
 *
 */
public class ScreenFade {
	
	/**
	 * {@link Player} object.
	 */
	private transient Player player;
	
	/**
	 * Default Constructor
	 */
	public ScreenFade(Player player) {
		this.player = player;
	}
	
	/**
	 * Fades the {@link Player}'s screen black
	 * @param runnable
	 * @param player
	 */
	public void fadeBlack(Runnable actionWhenFaded, Runnable actionAfter) {
		fadeScreen(actionWhenFaded, actionAfter, false, false, true);
	}
	
	/**
	 * Fades the {@link Player}'s screen white
	 * @param runnable
	 * @param player
	 */
	public void fadeWhite(Runnable actionWhenFaded, Runnable actionAfter) {
		fadeScreen(actionWhenFaded, actionAfter, true, false, true);
	}
	
	/**
	 * Fades the {@link Player}'s screen
	 * @param runnable
	 * @param player
	 * @param whiteFade
	 * @param isOverlay
	 */
	public void fadeScreen(Runnable actionWhenFaded, Runnable actionAfter, boolean whiteFade, boolean isOverlay, boolean blackOut) {
		if(blackOut)
			player.packets().sendBlackOut(2);
		if(!isOverlay) {
			if(!player.interfaces().isFullScreen())
				player.interfaces().sendInterface(whiteFade ? 122 : 120);
			else
				player.packets().sendWindowsPane(whiteFade ? 122 : 120, 0);
		} else {
			if(!player.interfaces().isFullScreen())
				player.packets().sendOverlay(whiteFade ? 122 : 120);
			else
				player.packets().sendWindowsPane(whiteFade ? 122 : 120, 0);
		}
		if(actionWhenFaded != null) {
			Game.submit(new GameTick(2) {

				@Override
				public void run() {
					stop();
					if(actionWhenFaded != null)
						actionWhenFaded.run();
				}
				
			});
		}
		Game.submit(new GameTick(player.interfaces().isFullScreen() ? 3.45 : 3.75) {
			int ticks = 0;
			@Override
			public void run() {
				if(ticks == 0) {
					if(!isOverlay) {
						if(!player.interfaces().isFullScreen())
							player.interfaces().sendInterface(170);
					} else {
						if(!player.interfaces().isFullScreen())
							player.packets().sendOverlay(170);
					}
					setDelay(player.interfaces().isFullScreen() ? .55 : 1.75);
				} else if(ticks == 1) {
					stop();
					if(actionAfter != null)
						actionAfter.run();
					if(player.interfaces().isFullScreen())
						player.interfaces().sendDefaultPane();
					else {
						if(!isOverlay)
							player.interfaces().closeScreenInterface();
						else
							player.packets().sendRemoveOverlay();
					}
					if(blackOut)
						player.packets().sendBlackOut(0);
				}
				ticks++;
			}
			
		});
	}

}
