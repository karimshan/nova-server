package org.nova.kshan.script.context;

import java.util.Arrays;

import org.nova.game.npc.NPC;
import org.nova.game.player.Player;
import org.nova.kshan.dialogues.Dialogue;
import org.nova.kshan.script.ParamCall;
import org.nova.kshan.script.ScriptCompiler;
import org.nova.kshan.script.ScriptContext;

public final class NPCDialInstruction extends ScriptContext {

	private final String[] messages;
	private final int expression;
	private boolean hideContinue;

	public NPCDialInstruction() {
		this(null, -1);
	}
	
	public NPCDialInstruction(String[] messages, int expression) {
		super("npc");
		super.setInstant(false);
		this.messages = messages;
		this.expression = expression;
	}
	
	@Override
	public boolean execute(Object... args) {
		Player player = (Player) args[0];
		int npcId = -1;
		System.out.println(Arrays.toString(args));
		if (args[1] instanceof NPC) {
			npcId = ((NPC) args[1]).getId();
		} else {
			npcId = (Integer) args[1];
		}
		String[] messages = new String[this.messages.length];
		for (int i = 0; i < messages.length; i++) {
			String message = this.messages[i];
			if (message.contains(">playername<")) {
				message = message.replace(">playername<", player.getUsername());
			}
			messages[i] = message;
		}
		//player.getDialogue().getCurrent().sendEntityDialogue(1, expression, npcId, hideContinue, messages[0], messages);
		//player.getDialogueInterpreter().setDialogueStage(this);
		// TODO
		return true;
	}
	
	@Override
	public ScriptContext parse(Object... params) {
		String[] messages = new String[6];
		int messageIndex = 0;
		int expression = Dialogue.HAPPY_TALKING;
		boolean hide = false;
		for (int i = 0; i < params.length; i++) {
			Object o = params[i];
			if (o instanceof ParamCall) {
				String param = ((ParamCall) o).getParameter();
				if (param.startsWith("anim:")) {
					String expr = ScriptCompiler.formatArgument(param.substring("anim:".length())).toString();
//					FacialExpression exp = null;
//					for (FacialExpression e : FacialExpression.values()) {
//						if (e.name().equals(expr)) {
//							exp = e;
//							break;
//						}
//					} // TODO
//					if (exp != null) {
//						expression = exp.getAnimationId();
//					} else {
//						expression = Integer.parseInt(expr);
//					}
				}
				else if (param.startsWith("hidecont:")) {
					hide = Boolean.parseBoolean(ScriptCompiler.formatArgument(param.substring("hidecont:".length())).toString());
				}
			} else if (o instanceof String) {
				messages[messageIndex++] = (String) o;
			}
		}
		if (messageIndex != messages.length) {
			messages = Arrays.copyOf(messages, messageIndex);
		}
		NPCDialInstruction context = new NPCDialInstruction(messages, expression);
		context.hideContinue = hide;
		context.parameters = params;
		return context;
	}

}