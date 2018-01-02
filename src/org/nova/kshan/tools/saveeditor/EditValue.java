package org.nova.kshan.tools.saveeditor;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.Field;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.nova.utility.loading.playerutils.SFiles;

/**
 * 
 * @author K-Shan
 *
 */
public class EditValue extends JFrame {

	private static final long serialVersionUID = -8485720810090992052L;
	
	private JPanel contentPane;
	private JFrame frame;
	private JFormattedTextField textField;

	public EditValue(String title) {
		super(title);
		try {
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame = this;
			setBounds(100, 100, 375, 176);
			setLocationRelativeTo(null);
			contentPane = new JPanel();
			contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			setContentPane(contentPane);
			contentPane.setLayout(null);
		
			JLabel lblValueOf = new JLabel("Value: ");
			lblValueOf.setFont(new Font("Tahoma", Font.PLAIN, 16));
			lblValueOf.setBounds(10, 36, 67, 20);
			contentPane.add(lblValueOf);
			final Field f = PlayerSaveEditor.currentPlayer.getClass().getDeclaredField(title.replace("Editing: ", ""));
			f.setAccessible(true);
			
			setTitle("Editing: "+f.getName()+" Type: "+f.getType());
			
			JButton btnSave = new JButton("Save");
			btnSave.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if(!textField.equals("")) {
						try {
							Object value = textField.getValue();
							if(value != null) {
								f.set(PlayerSaveEditor.currentPlayer, value);
								SFiles.savePlayer(PlayerSaveEditor.currentPlayer);
								PlayerSaveEditor.sendMessage("Saved value for "+f.getName()+": "+f.get(PlayerSaveEditor.currentPlayer));
							}
						} catch(Exception e) {
							e.printStackTrace();
						}
						
					}
				}
			});
			btnSave.setBounds(119, 92, 89, 23);
			contentPane.add(btnSave);
			
			textField = new JFormattedTextField();
			textField.setFont(new Font("Tahoma", Font.PLAIN, 14));
			textField.setBounds(72, 31, 264, 31);
			textField.setValue(f.get(PlayerSaveEditor.currentPlayer));
			
			contentPane.add(textField);
			frame.addWindowListener(new WindowAdapter() {
				
			    @Override
			    public void windowClosing(WindowEvent windowEvent) {
			        PlayerSaveEditor.frameOpen = false;
			    }
			});
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
