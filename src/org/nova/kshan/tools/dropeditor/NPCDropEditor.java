package org.nova.kshan.tools.dropeditor;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

import org.jvnet.substance.SubstanceLookAndFeel;
import org.nova.cache.Cache;
import org.nova.game.map.Location;
import org.nova.game.npc.Drop;
import org.nova.game.npc.NPC;
import org.nova.kshan.utilities.JFrameUtils;
import org.nova.utility.loading.npcs.NPCDrops;

/**
 * 
 * @author K-Shan
 *
 */
@SuppressWarnings("serial")
public class NPCDropEditor extends JFrame {
	
	public static void main(String[] args) {
		try {
			Cache.load();
			NPCDrops.init();
		} catch(Exception e) {
			e.printStackTrace();
		}
		setSubstanceSkin("Raven");
		new NPCDropEditor().setVisible(true);
	}
	
	public static File dropsFile = new File("data/npcs/drops.txt");
	public static Map<Integer, Drop[]> drops;

	private JPanel contentPane;
	private static DefaultListModel<NPC> npcListModel;
	private JList<NPC> npcList;
	private JLabel lblDrops;
	private JScrollPane scrollPane_1;
	private JTextField search;
	private List<NPC> wholeList = new ArrayList<NPC>();
	private JList<Drop> dropsList;
	private static DefaultListModel<Drop> dropsListModel;

	public NPCDropEditor() {
		JFrameUtils.setFrameIcon(this, "data/misc/images/Jad.png");
		setTitle("NPC Drop Editor - Made by: K-Shan");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 772, 637);
		setLocationRelativeTo(null);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		npcListModel = new DefaultListModel<NPC>();
		dropsListModel = new DefaultListModel<Drop>();
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 43, 278, 492);
		contentPane.add(scrollPane);
		
		npcList = new JList<NPC>(npcListModel);
		scrollPane.setViewportView(npcList);
		npcList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		npcList.setLayoutOrientation(JList.VERTICAL);
		npcList.setVisibleRowCount(-1);
		
		JLabel lblCachedNpcs = new JLabel("Cached NPCs");
		lblCachedNpcs.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblCachedNpcs.setBounds(72, 11, 134, 32);
		contentPane.add(lblCachedNpcs);
		
		lblDrops = new JLabel("Drops");
		lblDrops.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblDrops.setBounds(352, 11, 70, 32);
		contentPane.add(lblDrops);
		
		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(318, 43, 414, 457);
		contentPane.add(scrollPane_1);
		
		dropsList = new JList<Drop>(dropsListModel);
		scrollPane_1.setViewportView(dropsList);
		dropsList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		dropsList.setLayoutOrientation(JList.VERTICAL);
		dropsList.setVisibleRowCount(-1);
		
		JLabel lblSearch = new JLabel("Search:");
		lblSearch.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblSearch.setBounds(43, 555, 70, 32);
		contentPane.add(lblSearch);
		
		search = new JTextField();
		search.setFont(new Font("Tahoma", Font.PLAIN, 16));
		search.setBounds(123, 555, 345, 28);
		contentPane.add(search);
		search.setColumns(10);
		
		JButton btnAddNewDrop = new JButton("Add new drop");
		btnAddNewDrop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addNewDropAction();
			}
		});
		btnAddNewDrop.setBounds(328, 511, 113, 23);
		contentPane.add(btnAddNewDrop);
		
		JButton btnEditDrop = new JButton("Reload drops");
		btnEditDrop.setBounds(591, 511, 105, 23);
		contentPane.add(btnEditDrop);
		btnEditDrop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				reloadDropsAction();
			}
		});
		
		JButton btnDeleteDrop = new JButton("Delete drop");
		btnDeleteDrop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteDropAction();
			}
		});
		btnDeleteDrop.setBounds(466, 511, 89, 23);
		contentPane.add(btnDeleteDrop);
		
		JLabel lblitemNameDrop = new JLabel("(Item Name - (Min amount-Max amount), rate)");
		lblitemNameDrop.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblitemNameDrop.setBounds(407, 11, 325, 32);
		contentPane.add(lblitemNameDrop);
		
		JButton btnAddNewNpc = new JButton("Add new NPC Drop");
		btnAddNewNpc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newNPCDropAction();
			}
		});
		btnAddNewNpc.setBounds(493, 555, 131, 23);
		contentPane.add(btnAddNewNpc);
		
		search.addKeyListener(searchListener);
		addList();
		for(int i = 0; i < npcListModel.size(); i++)
			wholeList.add(npcListModel.get(i));
		
		npcList.addMouseListener(npcListMouseListener);
		npcList.addKeyListener(npcListKeyListener);
	}
	
	private void addDrops(int id) {
		dropsListModel.clear();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				for(Drop d : getDrops().get(id))
					dropsListModel.addElement(d);
			}
		});
	}
	
	public void addList() {
		npcListModel.clear();
		try {
			BufferedReader r = new BufferedReader(new FileReader(dropsFile));
			String line;
			while((line = r.readLine()) != null) {
				if(line.startsWith("//") || line.equals(""))
					continue;
				String[] tokens = line.split(" - ");
				int npcId = Integer.parseInt(tokens[0]);
				addToList(new NPC(npcId, new Location(0, 0, 0), false, false, null, null));
			}
			r.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addToList(final NPC n) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				npcListModel.addElement(n);
			}
		});
	}

	public void updateList(final NPC n) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				int index = npcListModel.indexOf(n);
				if (index == -1)
					npcListModel.addElement(n);
				else
					npcListModel.setElementAt(n, index);
			}
		});
	}

	public void removeFromList(final NPC n) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				npcListModel.removeElement(n);
			}
		});
	}
	
	private KeyListener searchListener = new KeyListener() {

		@Override
		public void keyPressed(KeyEvent event) {
			
		}

		@Override
		public void keyReleased(KeyEvent event) {
			if(!dropsListModel.isEmpty())
				dropsListModel.clear();
			if(search.getText().equals("")) {
				try {
					addList();
				} catch(Exception e) {
					e.printStackTrace();
				}
			} else {
				npcListModel.clear();
				for(int i = 0; i < wholeList.size(); i++) {
					if(wholeList.get(i).getName().toLowerCase().contains(search.getText().toLowerCase())) {
						List<NPC> toAdd = new ArrayList<NPC>();
						toAdd.add(wholeList.get(i));
						for(int j = 0; j < toAdd.size(); j++) {
							try {
								npcListModel.addElement(toAdd.get(j));
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		}
		

		@Override
		public void keyTyped(KeyEvent event) {
			
		}

	};
	
	private KeyListener npcListKeyListener = new KeyListener() {

		@Override
		public void keyPressed(KeyEvent event) {
			
		}

		@Override
		public void keyReleased(KeyEvent event) {
			dropsListModel.clear();
			for(Drop d : getDrops().get(npcList.getSelectedValue().getId()))
				dropsListModel.addElement(d);
		}
		

		@Override
		public void keyTyped(KeyEvent event) {
			
		}

	};
	
	private MouseAdapter npcListMouseListener = new MouseAdapter() {
		
		@Override
		public void mouseClicked(MouseEvent evt) {
			try {
				@SuppressWarnings("unchecked")
				JList<NPC> list = (JList<NPC>) evt.getSource();
		        int clickCount = evt.getClickCount();
		        if(clickCount >= 1) {
		        	NPC value = list.getSelectedValue();
		        	addDrops(value.getId());
		        }
			} catch(Exception e) {
				e.printStackTrace();
			}
	    }
	};
	
	public static Map<Integer, Drop[]> getDrops() {
		if(drops == null) {
			drops = new HashMap<Integer, Drop[]>();
			try {
				BufferedReader r = new BufferedReader(new FileReader(dropsFile));
				String line;
				while((line = r.readLine()) != null) {
					if(line.startsWith("//") || line.equals(""))
						continue;
					String[] tokens = line.split(" - ");
					int npcId = Integer.parseInt(tokens[0]);
					Drop[] toUse = new Drop[tokens.length - 1];
					int count = 0;
					for(int i = 1; i < tokens.length; i++) {
						String[] dropTokens = tokens[i].split(", ");
						int itemId = Integer.parseInt(dropTokens[0]);
						double rate = Double.parseDouble(dropTokens[1]);
						int minAmount = Integer.parseInt(dropTokens[2]);
						int maxAmount = Integer.parseInt(dropTokens[3]);
						boolean rare = Boolean.parseBoolean(dropTokens[4]);
						toUse[count] = new Drop(itemId, rate, minAmount, maxAmount, rare);
						drops.put(npcId, toUse);
						count++;
					}
				}
				r.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return drops;
	}
	
	private void addNewDropAction() {
		if(npcList.getSelectedValue() == null) {
			JFrameUtils.showMessage("NPC Drop Editor", "You need to have an NPC selected.");
			return;
		}
		Object input = JFrameUtils.inputDialog(
			"New drop for: \""+npcList.getSelectedValue().getName()+"\"",
			"Syntax: item id,min,max,rate");
		int npcId = npcList.getSelectedValue().getId();
		if(input != null) {
			String toString = (String) input;
			String[] splitString = toString.split(",");
			int itemId = Integer.parseInt(splitString[0]);
			int minAmount = Integer.parseInt(splitString[1]);
			int maxAmount = Integer.parseInt(splitString[2]);
			double rate = Double.parseDouble(splitString[3]);
			Drop drop = new Drop(itemId, rate, minAmount, maxAmount, false);
			dropsListModel.addElement(drop);
			try {
				File file = dropsFile;
				BufferedReader r = new BufferedReader(new FileReader(file));
				String line;
				StringBuilder code = new StringBuilder();
				while((line = r.readLine()) != null) {
					if(line.startsWith("//") || line.equals(""))
						continue;
					String[] tokens = line.split(" - ");
					int idFromFile = Integer.parseInt(tokens[0]);
					if(npcId == idFromFile) {
						code.append(line+" - "+itemId+", "+rate+", "+minAmount+", "+maxAmount+", false\n");
					} else
						code.append(line+"\n");
				}
				BufferedWriter w = new BufferedWriter(new FileWriter(file));
				w.write(code.toString());
				r.close();
				w.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
			Drop[] oldDrop = drops.get(npcId);
			int length = oldDrop.length;
			Drop destination[] = new Drop[length + 1];
			System.arraycopy(oldDrop, 0, destination, 0, length);
			destination[length] = drop;
			drops.put(npcId, destination);
		}
	}
	
	private void reloadDropsAction() {
		drops.clear();
		try {
			BufferedReader r = new BufferedReader(new FileReader(dropsFile));
			String line;
			while((line = r.readLine()) != null) {
				if(line.startsWith("//") || line.equals(""))
					continue;
				String[] tokens = line.split(" - ");
				int npcId = Integer.parseInt(tokens[0]);
				Drop[] toUse = new Drop[tokens.length - 1];
				int count = 0;
				for(int i = 1; i < tokens.length; i++) {
					String[] dropTokens = tokens[i].split(", ");
					int itemId = Integer.parseInt(dropTokens[0]);
					double rate = Double.parseDouble(dropTokens[1]);
					int minAmount = Integer.parseInt(dropTokens[2]);
					int maxAmount = Integer.parseInt(dropTokens[3]);
					boolean rare = Boolean.parseBoolean(dropTokens[4]);
					toUse[count] = new Drop(itemId, rate, minAmount, maxAmount, rare);
					drops.put(npcId, toUse);
					count++;
				}
			}
			r.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		int current = npcList.getSelectedValue().getId();
		dropsListModel.clear();
		for(Drop d : drops.get(current))
			dropsListModel.addElement(d);
	}
	
	private void deleteDropAction() {
		if(dropsList.getSelectedValue() == null) {
			JFrameUtils.showMessage("NPC Drop Editor", "You need to have a drop selected.");
			return;
		}
		int npcId = npcList.getSelectedValue().getId();
		Drop drop = dropsList.getSelectedValue();
		System.out.println("Deleted drop: "+drop);
		try {
			File file = dropsFile;
			BufferedReader r = new BufferedReader(new FileReader(file));
			String line;
			StringBuilder lines = new StringBuilder();
			StringBuilder newDropsLine = new StringBuilder();
			int count = 0;
			Drop[] newDrops = new Drop[dropsListModel.size() - 1];
			for(int i = 0; i < dropsListModel.size(); i++) {
				Drop from = dropsListModel.getElementAt(i);
				if(drop != from) {
					newDrops[count] = from;
					count++;
				}
			}
			drops.put(npcId, newDrops);
			count = 0;
			newDropsLine.append(npcId+" - ");
			for(Drop d : newDrops) {
				newDropsLine.append(d.getItemId()+", "+d.getRate()+", "+d.getMinAmount()+", "+d.getMaxAmount()+", false"+(
					count == newDrops.length - 1 ? "" : " - "));
				count++;
			}
			while((line = r.readLine()) != null) {
				if(line.startsWith("//") || line.equals(""))
					continue;
				String[] tokens = line.split(" - ");
				int idFromFile = Integer.parseInt(tokens[0]);
				if(npcId == idFromFile)
					lines.append(newDropsLine.toString()+"\n");
				else
					lines.append(line+"\n");
			}
			BufferedWriter w = new BufferedWriter(new FileWriter(file));
			w.write(lines.toString());
			r.close();
			w.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		dropsListModel.removeElement(dropsList.getSelectedValue());
	}
	
	private void newNPCDropAction() {
		int npcId = Integer.parseInt((String) JFrameUtils.inputDialog("New NPC Drop", "Enter in the NPC's ID:"));
		try {
			@SuppressWarnings("resource")
			BufferedReader r = new BufferedReader(new FileReader(dropsFile));
			String line;
			Drop bones = new Drop(526, 100.0, 1, 1, false);
			while((line = r.readLine()) != null) {
				if(line.startsWith("//") || line.equals(""))
					continue;
				String[] tokens = line.split(" - ");
				int idFromFile = Integer.parseInt(tokens[0]);
				if(npcId == idFromFile) {
					JFrameUtils.showMessage("NPC Drop Editor", "That NPC already has a drop table. Try searching for it.");
					return;
				}
			}
			BufferedWriter w = new BufferedWriter(new FileWriter(dropsFile, true));
			w.write(npcId+" - "+bones.getItemId()+", "+bones.getRate()+", "+bones.getMinAmount()+", "+bones.getMaxAmount()+", false");
			w.newLine();
			w.close();
			r.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		npcListModel.addElement(NPC.createBlankNPC(npcId));
		JFrameUtils.showMessage("NPC Drop Editor", "Added new drop table for: ["+NPC.createBlankNPC(npcId).getName()+" - "+npcId+"]");
	}
	
	/**
	 * Sets the theme
	 * @param theme
	 */
	private static void setSubstanceSkin(String theme) {
		theme = theme.replace(" ", "");
		try {
			SubstanceLookAndFeel.setSkin("org.jvnet.substance.skin."+theme+"Skin");
			JFrame.setDefaultLookAndFeelDecorated(true);
			JDialog.setDefaultLookAndFeelDecorated(true);
		} catch (Exception e) {
			System.err.println("Substance error: " + e.getMessage());
		}
	}
}
