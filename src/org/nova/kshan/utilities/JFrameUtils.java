package org.nova.kshan.utilities;

import java.awt.Toolkit;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.jvnet.substance.SubstanceLookAndFeel;

/**
 * 
 * @author K-Shan
 *
 */
public class JFrameUtils {
	
	public static Object inputDialog(String title, Object message) {
		return JOptionPane.showInputDialog(new JFrame(), message, title, JOptionPane.INFORMATION_MESSAGE);
	}
	
	public static boolean confirmDialog(String title, Object message) {
		int result = JOptionPane.showConfirmDialog(new JFrame(), message, title, JOptionPane.WARNING_MESSAGE);
		return result == 0 ? true : false;
	}
	
	public static void showMessage(String title, Object message) {
		JOptionPane.showMessageDialog(new JFrame(), message, title, JOptionPane.INFORMATION_MESSAGE);
	}

	public static void setSubstanceSkin(String theme) {
		theme = theme.replace(" ", "");
		try {
			SubstanceLookAndFeel.setSkin("org.jvnet.substance.skin."+theme+"Skin");
			JFrame.setDefaultLookAndFeelDecorated(true);
			JDialog.setDefaultLookAndFeelDecorated(true);
		} catch (Exception e) {
			System.err.println("Substance error: " + e.getMessage());
		}
	}

	public static void setFrameIcon(JFrame frame, String path) {
		try {
			frame.setIconImage(Toolkit.getDefaultToolkit().getImage(path));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
