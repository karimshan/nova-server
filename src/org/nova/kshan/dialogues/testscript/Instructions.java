package org.nova.kshan.dialogues.testscript;

/**
 * 
 * @author K-Shan
 *
 */
public enum Instructions {
	
	GO_TO("GO_TO->"),
	EXECUTE("~"),
	CONDITION("&"),
	END_BRACKET("}");
	
	private String type;
	
	private Instructions(String action) {
		this.type = action;
	}
	
	public String getType() {
		return type;
	}

}
