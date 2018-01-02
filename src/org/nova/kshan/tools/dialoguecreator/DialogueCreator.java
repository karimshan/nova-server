package org.nova.kshan.tools.dialoguecreator;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.nova.kshan.utilities.JFrameUtils;

/**
 * 
 * @author K-Shan
 *
 */
public class DialogueCreator extends JFrame {

	private static final long serialVersionUID = 1970212582856070748L;
	
	private JPanel contentPane;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JFrameUtils.setSubstanceSkin("Nebula");
					new DialogueCreator().setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	
	public DialogueCreator() {
		setTitle("Dialogue Creator - Made by: K-Shan");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JFrameUtils.setFrameIcon(this, "data/misc/images/Thought Bubble.png");
		setBounds(100, 100, 450, 300);
		setLocationRelativeTo(null);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
	}

}
