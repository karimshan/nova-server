package org.nova.kshan.tools.itembonuseditor;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.nova.cache.Cache;
import org.nova.game.item.Item;
import org.nova.kshan.utilities.JFrameUtils;

/**
 * 
 * @author K-Shan
 *
 */
public class BonusesEditor extends JFrame {

	private static final long serialVersionUID = 1275160571142142115L;
	
	private static final File BONUSES_FILE = new File("./data/items/bonuses.txt");
	
	/**
	 * Starting the class
	 * @param args
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Cache.load();
					JFrameUtils.setSubstanceSkin("Magma");
					print("Editor", "Loaded the item bonus editor.");
					new BonusesEditor().setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	private JPanel contentPane;
	private JLabel lblCurrentlyEditingBonuses;
	private JTextField itemIdField;
	private JTextField aStab;
	private JTextField aSlash;
	private JTextField aCrush;
	private JTextField aMagic;
	private JTextField aRange;
	private JTextField dStab;
	private JTextField dSlash;
	private JTextField dCrush;
	private JTextField dMagic;
	private JTextField dRange;
	private JTextField dSummoning;
	private JTextField absorbMelee;
	private JTextField absorbMagic;
	private JTextField absorbRanged;
	private JTextField strength;
	private JTextField rangedStr;
	private JTextField prayer;
	private JTextField magicDamage;
	
	public String[] bonusesAsText = new String[18];

	public BonusesEditor() {
		setTitle("Item Bonuses Editor - Made by: K-Shan");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 548, 440);
		setLocationRelativeTo(null);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		lblCurrentlyEditingBonuses = new JLabel("Currently editing bonuses of: None selected!");
		lblCurrentlyEditingBonuses.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblCurrentlyEditingBonuses.setBounds(99, 11, 324, 27);
		contentPane.add(lblCurrentlyEditingBonuses);
		
		JButton btnLoadBonuses = new JButton("Load bonuses");
		btnLoadBonuses.addActionListener(new ActionListener() {
			@SuppressWarnings("resource")
			public void actionPerformed(ActionEvent arg0) {
				if(itemIdField.getText().equals("") 
					|| itemIdField.getText() == null 
						|| !(itemIdField.getText().matches("[0-9]+")) 
							|| itemIdField.getText().length() < 1
								|| itemIdField.getText().length() > 5) {
					sendWarningMessage("There was an error processing your request.");
					return;
				}
				int itemId = Integer.parseInt(itemIdField.getText());
				int[] bonuses = new int[18];
				boolean hasBonuses = false;
				try {
					BufferedReader r = new BufferedReader(new FileReader(BONUSES_FILE));
					while (true) {
						String line = r.readLine();
						if (line == null)
							break;
						if (line.startsWith("//") || line.equals(""))
							continue;
						String[] lineSplit = line.split(" - ");
						int item = Integer.parseInt(lineSplit[0]);
						String[] bonusesFromFile = lineSplit[1].split(", ");
						if(item == itemId) {
							if(bonusesFromFile.length < 18 || bonusesFromFile.length > 18)
								throw new RuntimeException("Error: the item bonus array's size can only be 18. Line: "+line);
							for(int i = 0; i < 18; i++)
								bonuses[i] = Integer.parseInt(bonusesFromFile[i]);
							hasBonuses = true;
						}
					}
					if(hasBonuses) {
						setTextFieldBonuses(itemId, bonuses);
					} else {
						if(sendConfirmMessage("Bonuses not found", 
								"Bonuses for the item: "+itemId+" were not found. Create new ones?")) {
							print("Item Bonuses Editor", itemId+" didn't have any bonuses. Created new ones.");
							BufferedWriter f = new BufferedWriter(new FileWriter(BONUSES_FILE, true));
							f.write(itemId+" - "+Arrays.toString(bonuses).replace("[", "").replace("]", ""));
							f.newLine();
							f.close();
							setDefaultTextFields();
						}
						else
							return;
					}
					r.close();
				} catch(Throwable e) {
					e.printStackTrace();
				}
			}
		});
		btnLoadBonuses.setBounds(273, 48, 116, 23);
		contentPane.add(btnLoadBonuses);
		
		itemIdField = new JTextField();
		itemIdField.setBounds(132, 49, 116, 20);
		contentPane.add(itemIdField);
		itemIdField.setColumns(10);
		
		JLabel lblItemId = new JLabel("Item ID:");
		lblItemId.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblItemId.setBounds(70, 51, 62, 14);
		contentPane.add(lblItemId);
		
		JLabel lblNewLabel = new JLabel("Stab:");
		lblNewLabel.setBounds(29, 108, 46, 17);
		contentPane.add(lblNewLabel);
		
		aStab = new JTextField();
		aStab.setText("");
		aStab.setBounds(62, 106, 70, 20);
		contentPane.add(aStab);
		aStab.setColumns(10);
		
		JLabel lblSlash = new JLabel("Slash:");
		lblSlash.setBounds(29, 150, 38, 25);
		contentPane.add(lblSlash);
		
		JLabel lblCrush = new JLabel("Crush:");
		lblCrush.setBounds(29, 186, 46, 27);
		contentPane.add(lblCrush);
		
		JLabel lblRange = new JLabel("Range:");
		lblRange.setBounds(29, 265, 46, 17);
		contentPane.add(lblRange);
		
		JLabel lblMagic = new JLabel("Magic:");
		lblMagic.setBounds(29, 228, 46, 20);
		contentPane.add(lblMagic);
		
		JLabel lblAttackBonuses = new JLabel("Attack Bonuses");
		lblAttackBonuses.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblAttackBonuses.setBounds(29, 83, 134, 14);
		contentPane.add(lblAttackBonuses);
		
		aSlash = new JTextField();
		aSlash.setText("");
		aSlash.setColumns(10);
		aSlash.setBounds(62, 147, 70, 20);
		contentPane.add(aSlash);
		
		aCrush = new JTextField();
		aCrush.setText("");
		aCrush.setColumns(10);
		aCrush.setBounds(70, 183, 70, 20);
		contentPane.add(aCrush);
		
		aMagic = new JTextField();
		aMagic.setText("");
		aMagic.setColumns(10);
		aMagic.setBounds(70, 225, 70, 20);
		contentPane.add(aMagic);
		
		aRange = new JTextField();
		aRange.setText("");
		aRange.setColumns(10);
		aRange.setBounds(70, 262, 70, 20);
		contentPane.add(aRange);
		
		JLabel lblDefenceBonuses = new JLabel("Defence Bonuses");
		lblDefenceBonuses.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblDefenceBonuses.setBounds(230, 82, 134, 14);
		contentPane.add(lblDefenceBonuses);
		
		JLabel label = new JLabel("Stab:");
		label.setBounds(165, 109, 46, 17);
		contentPane.add(label);
		
		dStab = new JTextField();
		dStab.setText("");
		dStab.setColumns(10);
		dStab.setBounds(205, 106, 70, 20);
		contentPane.add(dStab);
		
		JLabel label_1 = new JLabel("Slash:");
		label_1.setBounds(165, 147, 38, 25);
		contentPane.add(label_1);
		
		dSlash = new JTextField();
		dSlash.setText("");
		dSlash.setColumns(10);
		dSlash.setBounds(205, 147, 70, 20);
		contentPane.add(dSlash);
		
		JLabel label_2 = new JLabel("Crush:");
		label_2.setBounds(165, 183, 46, 27);
		contentPane.add(label_2);
		
		JLabel label_3 = new JLabel("Magic:");
		label_3.setBounds(165, 228, 46, 20);
		contentPane.add(label_3);
		
		JLabel label_4 = new JLabel("Range:");
		label_4.setBounds(165, 265, 46, 17);
		contentPane.add(label_4);
		
		dCrush = new JTextField();
		dCrush.setText("");
		dCrush.setColumns(10);
		dCrush.setBounds(205, 189, 70, 20);
		contentPane.add(dCrush);
		
		dMagic = new JTextField();
		dMagic.setText("");
		dMagic.setColumns(10);
		dMagic.setBounds(205, 228, 70, 20);
		contentPane.add(dMagic);
		
		dRange = new JTextField();
		dRange.setText("");
		dRange.setColumns(10);
		dRange.setBounds(205, 263, 70, 20);
		contentPane.add(dRange);
		
		JLabel lblSummoning = new JLabel("Summoning:");
		lblSummoning.setBounds(306, 108, 70, 17);
		contentPane.add(lblSummoning);
		
		dSummoning = new JTextField();
		dSummoning.setText("");
		dSummoning.setColumns(10);
		dSummoning.setBounds(386, 106, 70, 20);
		contentPane.add(dSummoning);
		
		JLabel lblAbsorbMelee = new JLabel("Absorb Melee:");
		lblAbsorbMelee.setBounds(306, 150, 83, 17);
		contentPane.add(lblAbsorbMelee);
		
		JLabel lblAbsorb = new JLabel("Absorb Magic:");
		lblAbsorb.setBounds(306, 191, 83, 17);
		contentPane.add(lblAbsorb);
		
		JLabel lblAbsorbRanged = new JLabel("Absorb Ranged:");
		lblAbsorbRanged.setBounds(306, 230, 89, 17);
		contentPane.add(lblAbsorbRanged);
		
		absorbMelee = new JTextField();
		absorbMelee.setText("");
		absorbMelee.setColumns(10);
		absorbMelee.setBounds(396, 147, 70, 20);
		contentPane.add(absorbMelee);
		
		absorbMagic = new JTextField();
		absorbMagic.setText("");
		absorbMagic.setColumns(10);
		absorbMagic.setBounds(395, 189, 70, 20);
		contentPane.add(absorbMagic);
		
		absorbRanged = new JTextField();
		absorbRanged.setText("");
		absorbRanged.setColumns(10);
		absorbRanged.setBounds(405, 228, 70, 20);
		contentPane.add(absorbRanged);
		
		JLabel lblOtherBonuses = new JLabel("Other Bonuses");
		lblOtherBonuses.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblOtherBonuses.setBounds(141, 296, 134, 14);
		contentPane.add(lblOtherBonuses);
		
		JLabel lblStrength = new JLabel("Strength:");
		lblStrength.setBounds(45, 323, 70, 17);
		contentPane.add(lblStrength);
		
		JLabel lblRangedStr = new JLabel("Ranged Str:");
		lblRangedStr.setBounds(45, 351, 70, 17);
		contentPane.add(lblRangedStr);
		
		strength = new JTextField();
		strength.setText("");
		strength.setColumns(10);
		strength.setBounds(99, 321, 70, 20);
		contentPane.add(strength);
		
		rangedStr = new JTextField();
		rangedStr.setText("");
		rangedStr.setColumns(10);
		rangedStr.setBounds(109, 349, 70, 20);
		contentPane.add(rangedStr);
		
		JLabel lblPrayer = new JLabel("Prayer");
		lblPrayer.setBounds(190, 321, 70, 17);
		contentPane.add(lblPrayer);
		
		JLabel lblMagicDamage = new JLabel("Magic damage:");
		lblMagicDamage.setBounds(189, 352, 86, 17);
		contentPane.add(lblMagicDamage);
		
		prayer = new JTextField();
		prayer.setText("");
		prayer.setColumns(10);
		prayer.setBounds(237, 321, 70, 20);
		contentPane.add(prayer);
		
		magicDamage = new JTextField();
		magicDamage.setText("");
		magicDamage.setColumns(10);
		magicDamage.setBounds(285, 349, 70, 20);
		contentPane.add(magicDamage);
		
		
		
		JButton btnNewButton = new JButton("Save changes");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(sendConfirmMessage("Saving", "Are you sure you want to make these changes?"))	
					saveChanges();
				else
					return;
			}
		});
		btnNewButton.setBounds(376, 296, 116, 23);
		contentPane.add(btnNewButton);
	}
	
	private void saveChanges() {
		bonusesAsText = new String[18];
		bonusesAsText[0] = aStab.getText();
		bonusesAsText[1] = aSlash.getText();
		bonusesAsText[2] = aCrush.getText();
		bonusesAsText[3] = aMagic.getText();
		bonusesAsText[4] = aRange.getText();
		bonusesAsText[5] = dStab.getText();
		bonusesAsText[6] = dSlash.getText();
		bonusesAsText[7] = dCrush.getText();
		bonusesAsText[8] = dMagic.getText();
		bonusesAsText[9] = dRange.getText();
		bonusesAsText[10] = dSummoning.getText();
		bonusesAsText[11] = absorbMelee.getText();
		bonusesAsText[12] = absorbMagic.getText();
		bonusesAsText[13] = absorbRanged.getText();
		bonusesAsText[14] = strength.getText();
		bonusesAsText[15] = rangedStr.getText();
		bonusesAsText[16] = prayer.getText();
		bonusesAsText[17] = magicDamage.getText();
		if(itemIdField.getText().equals("") 
			|| itemIdField.getText() == null 
			|| !(itemIdField.getText().matches("[0-9]+")) 
			|| itemIdField.getText().length() < 1
			|| itemIdField.getText().length() > 5) {
				sendWarningMessage("There was an error processing your request.");
				return;
		}
		for(String s : bonusesAsText) {
			if(s.equals("")) {
				sendWarningMessage("Error: all of the boxes must be filled.");
				return;
			} else {
				saveBonuses(Integer.parseInt(itemIdField.getText()));
				return;
			}
		}
	}
	
	public void saveBonuses(int itemId) {
		if(bonusesAsText.length < 18) {
			print("Item Bonuses", "Bonuses couldn't be updated due to a size mismatch of the item bonuses array.");
			return;
		}
		int[] bonusesToPut = new int[18];
		for(int i = 0; i < 18; i++)
			bonusesToPut[i] = Integer.parseInt(bonusesAsText[i]);
		boolean replace = false;
		try {
			File bonusesFile = BONUSES_FILE;
			BufferedReader r = new BufferedReader(new FileReader(bonusesFile));
			String line;
			StringBuilder code = new StringBuilder();
			while((line = r.readLine()) != null) {
				if(line.equals("") || line.startsWith("//"))
					continue;
				String[] split = line.split(" - ");
				int id = Integer.parseInt(split[0]);
				if(id == itemId) {
					line = line.replace(line, new String(itemId+" - "+Arrays.toString(bonusesToPut).replace("[", "").replace("]", "")));
					code.append(line+"\n");
					replace = true;
				} else {
					code.append(line+"\n");
					replace = true;
				}
				if(!code.toString().contains(""+itemId))
					replace = false;
				
			}
			if(replace) {
				BufferedWriter writer = new BufferedWriter(new FileWriter(bonusesFile));
				writer.write(code.toString());
				writer.close();
				print("Bonuses Editor", "Successfully edited the bonuses of: "+itemId+": "+Arrays.toString(bonusesToPut));
				sendWarningMessage("Successfully edited the bonuses of item: "+itemId);
				r.close();
			} else if(!replace) {
				r.close();
				print("Item Bonuses Editor", itemId+" didn't have any bonuses. Added new ones.");
				BufferedWriter f = new BufferedWriter(new FileWriter(bonusesFile, true));
				f.write(itemId+" - "+Arrays.toString(bonusesToPut).replace("[", "").replace("]", ""));
				f.newLine();
				f.close();
				sendWarningMessage(itemId+" didn't have any bonuses. Added new ones.");
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void setDefaultTextFields() {
		aStab.setText("0");
		aSlash.setText("0");
		aCrush.setText("0");
		aMagic.setText("0");
		aRange.setText("0");
		dStab.setText("0");
		dSlash.setText("0");
		dCrush.setText("0");
		dMagic.setText("0");
		dRange.setText("0");
		dSummoning.setText("0");
		absorbMelee.setText("0");
		absorbMagic.setText("0");
		absorbRanged.setText("0");
		strength.setText("0");
		rangedStr.setText("0");
		prayer.setText("0");
		magicDamage.setText("0");
	}
	
	private void setTextFieldBonuses(int itemId, int[] bonuses) {
		aStab.setText(""+bonuses[0]);
		aSlash.setText(""+bonuses[1]);
		aCrush.setText(""+bonuses[2]);
		aMagic.setText(""+bonuses[3]);
		aRange.setText(""+bonuses[4]);
		dStab.setText(""+bonuses[5]);
		dSlash.setText(""+bonuses[6]);
		dCrush.setText(""+bonuses[7]);
		dMagic.setText(""+bonuses[8]);
		dRange.setText(""+bonuses[9]);
		dSummoning.setText(""+bonuses[10]);
		absorbMelee.setText(""+bonuses[11]);
		absorbMagic.setText(""+bonuses[12]);
		absorbRanged.setText(""+bonuses[13]);
		strength.setText(""+bonuses[14]);
		rangedStr.setText(""+bonuses[15]);
		prayer.setText(""+bonuses[16]);
		magicDamage.setText(""+bonuses[17]);
		lblCurrentlyEditingBonuses.setText(new Item(itemId).toString());
		sendWarningMessage("Loaded bonuses of: "+(new Item(itemId)));
	}
	
	/**
	 * Prints out and logs the text printed out.
	 */
	public static void print(String header, Object text) {
		System.out.println("["+header+"] "+text);
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File("./src/org/nova/kshan/tools/itembonuseditor/logs.txt"), true));
			writer.write(new Date()+": "+text);
			writer.newLine();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void sendWarningMessage(String string) {
		JFrame frame = new JFrame();
		JOptionPane.showMessageDialog(frame, string, 
				"Item Bonuses Editor", JOptionPane.WARNING_MESSAGE);
	}
	
	public static boolean sendConfirmMessage(String header, String message) {
		JFrame f = new JFrame();
		int option = JOptionPane.showConfirmDialog(f, message, header, JOptionPane.WARNING_MESSAGE);
		return option == 0 ? true : false;
	}

}
