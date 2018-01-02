package org.nova.kshan.dialogues.listoptions;

import java.util.ArrayList;

import org.nova.kshan.dialogues.Dialogue;

/**
 * This class is an extension of the {@link Dialogue} class.
 * It's used to send any number of options displaying
 * a list's elements in a string format.
 * Enables the user to choose any option from that list
 * and an action will occur according to the current
 * index of the list used.
 * Used in dialogue-type format.
 * The thing that makes this class special is the fact
 * that no matter how many elements a list has, this dialogue
 * will never have to be modified. It'll always
 * display the elements of the list, followed by
 * either a component that displays a previous or next
 * page option to allow easy readability of all of the elements in the list.
 * 
 * @author K-Shan
 *
 */
public abstract class ListOptionsDialogue extends Dialogue {

	/**
	 * The current index in the list.
	 */
	protected int currentIndex = 0;
	
	/**
	 * The last index of the list.
	 */
	protected int lastIndex = 3;
	
	/**
	 * The last interfaceId opened up by the dialogue.
	 */
	protected int lastInterfaceId = FIVE_OPTIONS;
	
	/**
	 * If any of the dialogues have stages that require special instructions,
	 * then this variable can be used to indicate those stages. Acts as a holder
	 * variable.
	 */
	protected boolean specialStage = false;
	
	/**
	 * The current page that is being viewed.
	 */
	protected int page = 1;
	
	/**
	 * The interface that is currently open in the chatbox.
	 */
	protected int interfaceId;
	
	/**
	 * The button id being pressed down on in the open interface.
	 */
	protected int buttonId;

	/**
	 * The array list that contains the values displayed 
	 * in the dialogue.
	 * @return
	 */
	public abstract ArrayList<?> getList();
	
	/**
	 * The title of the dialogue (In between the swords)
	 * @return
	 */
	public abstract String getTitle();
	
	/**
	 * Processes the action that occurs after clicking
	 * one of the options.
	 * @param s
	 */
	public abstract void processStage(int s);

	/**
	 * The default starting procedure that occurs
	 * after starting any ListOptionsDialogue.
	 */
	@Override
	public void start() {
		int currentSize = getList().size();
		int optionsFilled = 0;
		if (currentSize == 1) {
			sendOptions(getTitle(), "", 
				"1) "+getEachLine(getList().get(0)), "");
			player.packets().sendHideIComponent(THREE_OPTIONS, 2, true);
			player.packets().sendHideIComponent(THREE_OPTIONS, 4, true);
			stage = 1;
		} else if (currentSize <= 5 && currentSize > 1) {
			for (int i = 0; i < currentSize; i++)
				optionsFilled++;
			for (int i = 0; i < optionsFilled; i++) {
				player.packets().sendString((getList().indexOf(getList().get(i)) + 1)+") "+getEachLine(getList().get(i)),
					checkOptions(optionsFilled), checkOptions(optionsFilled) == THREE_OPTIONS ? i + 2 : i + 1);
			}
			player.interfaces().sendChatBoxInterface(checkOptions(optionsFilled));
			player.packets().sendString(getTitle(), checkOptions(optionsFilled),
				checkOptions(optionsFilled) == THREE_OPTIONS ? 1 : 0);
			stage = 2;
		} else if(currentSize > 5) {
			optionsFilled = 5;
			for(int i = 0; i < 4; i++)
				player.packets().sendString((getList().indexOf(getList().get(i)) + 1)+") "+getEachLine(getList().get(i)),
					checkOptions(5), i + 1);
			player.packets().sendString("<col=0000ff>Next Page ("+(page + 1)+")", checkOptions(5), 5);
			player.interfaces().sendChatBoxInterface(checkOptions(5));
			player.packets().sendString(getTitle(), checkOptions(5), 0);
			stage = 3;
		}
		checkTitleSwords(optionsFilled);
	}

	/**
	 * The default procedures that occur when a dialogue is active
	 */
	@Override
	public void process(int interfaceId, int buttonId) {
		this.interfaceId = interfaceId;
		this.buttonId = buttonId;
		if(getButton5Overrides() != null) {
			for(int special : getButton5Overrides()) {
				if(buttonId == 5 && stage != 2 && stage != special) {
					page++;
					sendPageOptions();
					return;
				}
			}
		} else if(buttonId == 5 && stage != 2) {
			page++;
			sendPageOptions();
			return;
		}
		switch(stage) {
			case 2:
				currentIndex = interfaceId == THREE_OPTIONS ? buttonId - 2 : buttonId - 1;
				processStage(stage);
				break;
			case 3:
				if(buttonId != 5) {
					currentIndex = interfaceId == THREE_OPTIONS ? buttonId - 2 : buttonId - 1;
					processStage(stage);
					break;
				}
				break;
			case 4:
				if(interfaceId == TWO_OPTIONS && buttonId != 2 || (interfaceId == FOUR_OPTIONS || interfaceId == THREE_OPTIONS) 
					&& buttonId != 4 || stage != 3 && buttonId != 5 && buttonId != 4 && interfaceId == FIVE_OPTIONS) {
					currentIndex = interfaceId == THREE_OPTIONS ? buttonId + lastIndex - 3 : interfaceId == TWO_OPTIONS ? 
						buttonId + lastIndex - 1 : buttonId + lastIndex - 3;
					lastInterfaceId = interfaceId;
					processStage(stage);
				} else if(interfaceId == TWO_OPTIONS && buttonId == 2) {
					page--;
					lastIndex = lastIndex - 4;
					sendPageOptions();
				} else if((interfaceId == THREE_OPTIONS || interfaceId == FOUR_OPTIONS || interfaceId == FIVE_OPTIONS) && buttonId == 4) {
					page--;
					if(page == 1) {
						currentIndex = 0;
						lastIndex = 3;
						start();
					} else {
						lastIndex = interfaceId == THREE_OPTIONS ? lastIndex - 5 : lastIndex - 6;
						sendPageOptions();
					}
				}
				break;
			default:
				processStage(stage);
				break;
		}
	}

	/**
	 * Default finishing procedure.
	 */
	@Override
	public void finish() {
		
	}
	
	/**
	 * The default is the string value of that class.
	 * This returns what each option will display on every
	 * different option line. This is displayed after
	 * the current index is displayed.
	 * @param value
	 * @return
	 */
	public <V> String getEachLine(V value) {
		return value.toString();
	}
	
	/**
	 * Returns the selected value of the list,
	 * specified by the current index.
	 * @return
	 */
	public Object getSelectedElement() {
		return getList().get(currentIndex);
	}
	
	/**
	 * Returns any special stages in which button id 5 will
	 * not complete its normal procedure and instead do what
	 * the processStage method tells it to do.
	 * @return
	 */
	public int[] getButton5Overrides() {
		return null;
	}
	
	/**
	 * Sends the default page options.
	 */
	protected void sendPageOptions() {
		int lastIndexInArray = getList().size();
		int count = 0;
		int trueCount = 0;
		for(int i = (lastIndex + 1); i < lastIndexInArray; i++) {
			count++;
			trueCount++;
		}
		if(count > 3)
			count = 3;
		for(int i = 0; i < count; i++) {
			player.packets().sendString((getList().indexOf(
				getList().get(lastIndex + i + 1)) + 1)+") "+getEachLine(getList().get(lastIndex + i + 1)), 
					checkOptions(trueCount > 3 ? count + 2 : count + 1), 
						checkOptions(trueCount > 3 ? count + 2 : count + 1) == THREE_OPTIONS ? i + 2 : i + 1);
		}
		player.interfaces().sendChatBoxInterface(checkOptions(trueCount > 3 ? count + 2 : count + 1));
		player.packets().sendString("<col=0000ff>Previous Page: ("+(page - 1)+")", checkOptions(trueCount > 3 ? count + 2 : count + 1), 
				checkOptions(trueCount > 3 ? count + 2 : count + 1) == THREE_OPTIONS ? count + 2 : count + 1);
		if(trueCount > 3)
			player.packets().sendString("<col=0000ff>Next Page: ("+(page + 1)+")", checkOptions(count + 2), count + 2);
		player.packets().sendString(getTitle(), checkOptions(trueCount > 3 ? count + 2 : count + 1), 
				checkOptions(trueCount > 3 ? count + 2 : count + 1) == THREE_OPTIONS ? 1 : 0);
		lastIndex = (lastIndex + count);
		stage = 4;
		checkTitleSwords(trueCount > 3 ? count + 2 : count + 1);
	}
}
