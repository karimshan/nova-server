package org.nova.kshan.dialogues.testscript;

/**
 * Options Dialogue Script.
 * 
 * @author K-Shan
 *
 */
public class OptionsDialogueScript extends DialogueScript {
	
	private String title;
	
	public OptionsDialogueScript(String title, String[] options) {
		this.title = title;
		super.lines = options;
	}
	
	public String getTitle() {
		return title;
	}

}
