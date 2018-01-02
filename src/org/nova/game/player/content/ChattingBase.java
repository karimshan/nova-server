package org.nova.game.player.content;
/**
 * @author Fuzen Seth
 * 
 */
public enum ChattingBase {
	ON("<col=33FF00>On"),
	FRIENDS("<col=FFCC00>Friends"),
	OFF("<col=FF3300>Off"),
	HIDE("<col=33CCFF>Hide"),
	ALL("All");
        private String type;

		public String getType() {
			return type;
		}
	    private ChattingBase(String type) {
        	this.type = type;
        }
}