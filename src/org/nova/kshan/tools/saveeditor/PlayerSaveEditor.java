package org.nova.kshan.tools.saveeditor;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.nova.game.player.Player;
import org.nova.kshan.utilities.StringUtils;
import org.nova.kshan.utilities.JFrameUtils;
import org.nova.kshan.utilities.ReflectionUtils;
import org.nova.utility.loading.playerutils.SFiles;

/**
 * 
 * @author K-Shan
 *
 */
public class PlayerSaveEditor extends JFrame {

	private static final long serialVersionUID = 580831508998370406L;
	
	private DefaultListModel<String> fields;
	private JPanel contentPane;
	private JList<String> list;
	private JTextField search;
	
	private List<String> allFields;
	public static Player currentPlayer;
	public static boolean frameOpen = false;
	public static JFrame editing;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JFrameUtils.setSubstanceSkin("FieldOfWheat");
					PlayerSaveEditor frame = new PlayerSaveEditor();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public PlayerSaveEditor() {
		setResizable(false);
		setTitle("Player Save Editor");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fields = new DefaultListModel<String>();
		allFields = new ArrayList<String>();
		setBounds(100, 100, 407, 453);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnLoadPlayer = new JButton("Load Player");
		btnLoadPlayer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFrame frame = new JFrame();
				Object input = JOptionPane.showInputDialog(frame, "Enter in the player name:");
				String name = (String) input;
				if(!SFiles.containsPlayer(name)) {
					sendMessage("That player doesn't exist.");
					search.setEnabled(false);
					fields.clear();
					allFields.clear();
					currentPlayer = null;
					setTitle("Player Save Editor");
					return;
				}
				fields.clear();
				allFields.clear();
				Player p = SFiles.loadPlayer(name);
				p.setUsername(name);
				currentPlayer = p;
				addFields(currentPlayer);
				setTitle("Currently editing: "+p.getDisplayName());
			}
		});
		btnLoadPlayer.setBounds(286, 38, 109, 23);
		contentPane.add(btnLoadPlayer);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(19, 28, 257, 335);
		contentPane.add(scrollPane);
		
		list = new JList<String>(fields);
		scrollPane.setViewportView(list);
		list.addMouseListener(fieldListListener);
		
		search = new JTextField();
		search.setFont(new Font("Tahoma", Font.PLAIN, 15));
		search.setBounds(88, 374, 257, 32);
		contentPane.add(search);
		search.setColumns(10);
		search.setEnabled(false);
		
		search.addKeyListener(searchListener);
		
		JLabel lblSearch = new JLabel("Search:");
		lblSearch.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblSearch.setBounds(19, 381, 68, 17);
		contentPane.add(lblSearch);
		
		btnSavePlayer = new JButton("Save Player");
		btnSavePlayer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(currentPlayer == null) {
					sendMessage("There is no loaded player.");
					return;
				}
				SFiles.savePlayer(currentPlayer);
				sendMessage(currentPlayer.getDisplayName()+" has been saved.");
			}
		});
		btnSavePlayer.setBounds(286, 72, 109, 23);
		contentPane.add(btnSavePlayer);
	}
	
	private void addFields(Player p) {
		try {
			fields.clear();
			allFields.clear();
			for(Field f : p.getClass().getDeclaredFields())
				if(f != null && !ReflectionUtils.isFinal(f) && !ReflectionUtils.isTransient(f)) {
					if(isValidType(f.getType())) {
						f.setAccessible(true);
						String fieldName = f.getName();
						allFields.add(fieldName);
						fields.addElement(fieldName);//+" = "+f.get(p));
					}
				}
			search.setEnabled(true);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void sendMessage(String string) {
		JFrame frame = new JFrame();
		JOptionPane.showMessageDialog(frame, string, 
				"Player Save Editor", JOptionPane.WARNING_MESSAGE);
	}
	
	public static boolean sendConfirm(String message) {
		JFrame f = new JFrame();
		int option = JOptionPane.showConfirmDialog(f, message, "Player Save Editor", JOptionPane.WARNING_MESSAGE);
		return option == 0 ? true : false;
	}
	
	private KeyListener searchListener = new KeyListener() {

		@Override
		public void keyPressed(KeyEvent event) {
			
		}

		@Override
		public void keyReleased(KeyEvent event) {
			fields.clear();
			for(int i = 0; i < allFields.size(); i++) {
				if(search.getText().equals("")) {
					try {
						Field f = currentPlayer.getClass().getDeclaredField(allFields.get(i));
						f.setAccessible(true);
						fields.addElement(allFields.get(i));//+" = "+f.get(currentPlayer));	
					} catch(Exception e) {
						e.printStackTrace();
					}
				} else if(StringUtils.contains(search.getText(), allFields.get(i))) {
					List<String> toAdd = new ArrayList<String>();
					toAdd.add(allFields.get(i));
					for(int j = 0; j < toAdd.size(); j++) {
						try {
							//Field f = currentPlayer.getClass().getDeclaredField(toAdd.get(j));
							//f.setAccessible(true);
							fields.addElement(toAdd.get(j));//+" = "+f.get(currentPlayer));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		}

		@Override
		public void keyTyped(KeyEvent event) {
			
		}

	};
	
	private MouseAdapter fieldListListener = new MouseAdapter() {
		
		@Override
		public void mouseClicked(MouseEvent evt) {
			try {
				@SuppressWarnings("unchecked")
				JList<String> list = (JList<String>) evt.getSource();
		        int clickCount = evt.getClickCount();
		        if(clickCount >= 2) {
		        	String value = list.getSelectedValue();
		        	for(int i = 0; i < allFields.size(); i++) {
		        		if(value.equals(allFields.get(i))) {
		        			if(frameOpen) {
		        				frameOpen = false;
		        				editing.dispose();
		        				EditValue frame = new EditValue("Editing: "+allFields.get(i));
		        				frame.setVisible(true);
		        				editing = frame;
		        				frameOpen = true;
		        			} else {
		        				EditValue frame = new EditValue("Editing: "+allFields.get(i));
		        				frame.setVisible(true);
		        				editing = frame;
			        			frameOpen = true;
		        			}
		        		}
		        	}
		        }
			} catch(Exception e) {
				e.printStackTrace();
			}
	    }
	};
	private JButton btnSavePlayer;
	
	private boolean isValidType(Class<?> type) {
		String[] validTypes = { "java.lang.String", "String", "int", "Integer", "long", "float", "Long",
				"double", "Boolean", "boolean", "char", "byte", "short", "class java.lang.String" };
		for(int i = 0; i < validTypes.length; i++)
			if(type.getName().startsWith(validTypes[i]))
				return true;
		return false;
	}
}
