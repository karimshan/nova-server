package org.nova.kshan.input.type.integer;

import org.nova.kshan.content.keystroke.KeyStroke;
import org.nova.kshan.content.keystroke.KeyStrokeData;
import org.nova.kshan.input.type.InputEvent;

/**
 * 
 * @author K-Shan
 *
 */
public abstract class IntegerInput extends InputEvent {

	public abstract void process(int input);
		
	@Override
	public void sendScript(String message) {
		player.packets().runScript(108, new Object[] { message }, "s");
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
