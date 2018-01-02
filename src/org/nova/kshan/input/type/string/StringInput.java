package org.nova.kshan.input.type.string;

import org.nova.kshan.content.keystroke.KeyStroke;
import org.nova.kshan.content.keystroke.KeyStrokeData;
import org.nova.kshan.input.type.InputEvent;

/**
 * 
 * @author K-Shan
 *
 */
public abstract class StringInput extends InputEvent {
	
	public abstract void process(String input);
	
	@Override
	public void sendScript(String message) {
		player.packets().runScript(110, new Object[] { message }, "s");
		player.getKeyStrokes().run(new KeyStroke() {

			@Override
			public void press(int key, char keyChar, boolean shiftHeld) {
				player.getInputEvent().current().whileTyping(key, keyChar, shiftHeld);
				if(KeyStrokeData.getKey(key).toLowerCase().contains("enter"))
					terminate();
			}
			
		});
	}
	
}
