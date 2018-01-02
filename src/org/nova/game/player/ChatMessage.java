package org.nova.game.player;

import org.nova.utility.Censor;
import org.nova.utility.misc.Misc;

public class ChatMessage {

    private String message;
    private String filteredMessage;

    public ChatMessage(String message) {
	if (!(this instanceof QuickChatMessage)) {
	    filteredMessage = Censor.filter(message);
	    this.message = Misc.fixChatMessage(message);
	} else
	    this.message = message;
    }

    public String getMessage(boolean filtered) {
	return filtered ? filteredMessage : message;
    }
}
