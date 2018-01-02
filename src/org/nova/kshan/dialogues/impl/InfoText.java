package org.nova.kshan.dialogues.impl;

import org.nova.kshan.dialogues.Dialogue;

/**
 * 
 * @author K-Shan
 *
 */
public class InfoText extends Dialogue {
	
	String[] lines;
	boolean noContinue = false;
	
	@Override
	public void start() {
		lines = (String[]) data[0];
		if(data.length == 2)
			noContinue = (Boolean) data[1];
		sendLines(lines, noContinue);
	}

	@Override
	public void process(int interfaceId, int buttonId) {
		end();
	}

	@Override
	public void finish() {

	}

}
