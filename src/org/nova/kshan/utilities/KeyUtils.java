package org.nova.kshan.utilities;

import java.awt.DefaultKeyboardFocusManager;
import java.awt.KeyEventPostProcessor;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;

/**
 * 
 * @author Karimshan Nawaz
 *
 */
public class KeyUtils {
	
	/**
	 * ctrl + key
	 * @param e
	 * @param key
	 * @return
	 */
	public static boolean ctrlPressed(KeyEvent e, int key) {
		return e.isControlDown() && e.getKeyCode() == key;
	}
	
	/**
	 * 
	 * @param event
	 * @return
	 */
	public static boolean isCtrlPressed(KeyEvent event) {
		return event.getKeyCode() == KeyEvent.VK_CONTROL || event.isControlDown();
	}

	/**
	 * Updates the keyboard focus manager.
	 * @param keyPostProcessor
	 */
	public static void updateKeyPostProcessor(KeyEventPostProcessor keyPostProcessor) {
		KeyboardFocusManager.setCurrentKeyboardFocusManager(new DefaultKeyboardFocusManager());
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventPostProcessor(keyPostProcessor);
	}

}