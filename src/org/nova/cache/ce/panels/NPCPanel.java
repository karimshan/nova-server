package org.nova.cache.ce.panels;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.KeyEventPostProcessor;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import org.nova.cache.Cache;
import org.nova.cache.ce.MainFrame;
import org.nova.cache.ce.editors.NPCDefinitionEditor;
import org.nova.cache.definition.NPCDefinition;
import org.nova.cache.utility.CacheUtils;
import org.nova.kshan.utilities.KeyUtils;
import org.nova.kshan.utilities.StringUtils;

/**
 * Basically recycled my ItemPanel class
 * 
 * @author Karimshan Nawaz
 *
 */
public class NPCPanel extends JPanel {
	
	/**
	 * @serialField
	 */
	private static final long serialVersionUID = -2306195736861474011L;
	
	private DefaultListModel<NPCDefinition> npcsListModel;
	private JList<NPCDefinition> npcsList;
	
	public NPCDefinition previousDefs = null;
	public NPCDefinition defs = null;
	
	private JComboBox<?> sizeComboBox, optionsComboBox, modelDesignComboBox;
	
	private JLabel npcIDLabel;
	private JTextField npcSearch;
	
	private JTextField npcName;
	private JTextField headIcon;
	private JTextField combatLevel;
	private JTextField walkMask;
	private JTextField respawnDirection;
	private JTextField render;
	private JTextField models;
	private JTextField chatHeads;
	private JTextField sizeData;
	private JTextField optionsData;
	private JTextField modelDesignData;
	
	private JCheckBox visibleOnMap;
	private JTextArea clientScripts;
	
	private String previousOptionsData = "right-click options";
	private String previousSizeData = "size";
	private String previousModelDesignData = "model colors";
	
	private boolean recommendSaving;
	private boolean canSaveData;
	private boolean isNewNPC;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public NPCPanel() {
		super();
		npcsListModel = new DefaultListModel<NPCDefinition>();
		setLayout(null);
		setBounds(0, 0, 802, 574);
		JScrollPane npcsListscrollPane = new JScrollPane();
		npcsListscrollPane.setBounds(15, 15, 335, 490);
		add(npcsListscrollPane);
		npcsList = new JList<NPCDefinition>(npcsListModel);
		npcsList.addKeyListener(getKeyListener(null));
		npcsListscrollPane.setViewportView(npcsList);
		JLabel label = new JLabel("Search:");
		label.setFont(new Font("Tahoma", Font.PLAIN, 14));
		label.setBounds(15, 522, 62, 27);
		add(label);
		npcSearch = new JTextField();
		npcSearch.setColumns(10);
		npcSearch.setBounds(78, 522, 272, 27);
		npcSearch.addKeyListener(getKeyListener(npcSearch));
		npcsList.addMouseListener(getMouseListener());
		add(npcSearch);
		npcsList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		npcsList.setLayoutOrientation(JList.VERTICAL);
		npcsList.setVisibleRowCount(-1);
		npcIDLabel = new JLabel("NPC: -1");
		npcIDLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		npcIDLabel.setBounds(496, 0, 90, 27);
		add(npcIDLabel);
		
		JLabel labelName = new JLabel("Name:");
		labelName.setFont(new Font("Tahoma", Font.PLAIN, 13));
		labelName.setBounds(376, 28, 50, 27);
		add(labelName);
		
		npcName = new JTextField();
		npcName.setColumns(10);
		npcName.setBounds(430, 28, 208, 27);
		add(npcName);
		
		JLabel lblHeadIcon = new JLabel("Head Icon:");
		lblHeadIcon.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblHeadIcon.setBounds(376, 88, 67, 27);
		add(lblHeadIcon);
		
		headIcon = new JTextField();
		headIcon.setColumns(10);
		headIcon.setBounds(452, 88, 74, 27);
		add(headIcon);
		
		combatLevel = new JTextField();
		combatLevel.setColumns(10);
		combatLevel.setBounds(471, 58, 97, 27);
		add(combatLevel);
		
		JLabel lblCombatLevel = new JLabel("Combat Level:");
		lblCombatLevel.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblCombatLevel.setBounds(376, 58, 97, 27);
		add(lblCombatLevel);
		
		JLabel lblWalkMask = new JLabel("Walk Mask:");
		lblWalkMask.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblWalkMask.setBounds(376, 118, 81, 27);
		add(lblWalkMask);
		
		walkMask = new JTextField();
		walkMask.setColumns(10);
		walkMask.setBounds(452, 118, 74, 27);
		add(walkMask);
		
		JLabel lblRespawnDirection = new JLabel("Respawn Direction:");
		lblRespawnDirection.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblRespawnDirection.setBounds(376, 148, 128, 27);
		add(lblRespawnDirection);
		
		respawnDirection = new JTextField();
		respawnDirection.setColumns(10);
		respawnDirection.setBounds(494, 148, 92, 27);
		add(respawnDirection);
		
		JLabel lblRenderAnimation = new JLabel("Render Animation:");
		lblRenderAnimation.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblRenderAnimation.setBounds(376, 178, 120, 27);
		add(lblRenderAnimation);
		
		render = new JTextField();
		render.setColumns(10);
		render.setBounds(496, 178, 90, 27);
		add(render);
		
		JLabel lblModels = new JLabel("Models:");
		lblModels.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblModels.setBounds(376, 238, 56, 27);
		add(lblModels);
		
		models = new JTextField();
		models.setColumns(10);
		models.setBounds(428, 238, 351, 27);
		add(models);
		
		JLabel lblChatHeads = new JLabel("Chat Heads:");
		lblChatHeads.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblChatHeads.setBounds(376, 208, 90, 27);
		add(lblChatHeads);
		
		chatHeads = new JTextField();
		chatHeads.setColumns(10);
		chatHeads.setBounds(461, 208, 177, 27);
		add(chatHeads);
		
		sizeComboBox = new JComboBox();
		sizeComboBox.setModel(new DefaultComboBoxModel(new String[] {"Size", "Width", "Height"}));
		sizeComboBox.setFont(new Font("Tahoma", Font.PLAIN, 13));
		sizeComboBox.setBounds(376, 268, 67, 27);
		sizeComboBox.addActionListener(e -> {
			handleComboBoxActions(e, "size");
		});
		add(sizeComboBox);
		
		sizeData = new JTextField();
		sizeData.setColumns(10);
		sizeData.setBounds(461, 268, 120, 27);
		add(sizeData);
		
		optionsComboBox = new JComboBox();
		optionsComboBox.setModel(new DefaultComboBoxModel(new String[] {"Right-Click Options", "Tooltip Text (REMOVE)"}));
		optionsComboBox.setFont(new Font("Tahoma", Font.PLAIN, 13));
		optionsComboBox.setBounds(376, 298, 139, 27);
		optionsComboBox.addActionListener(e -> {
			handleComboBoxActions(e, "options");
		});
		add(optionsComboBox);
		
		optionsData = new JTextField();
		optionsData.setColumns(10);
		optionsData.setBounds(525, 298, 254, 27);
		add(optionsData);
		
		modelDesignComboBox = new JComboBox();
		modelDesignComboBox.setModel(new DefaultComboBoxModel(new String[] {"Model Colors", "Model Textures"}));
		modelDesignComboBox.setFont(new Font("Tahoma", Font.PLAIN, 13));
		modelDesignComboBox.setBounds(376, 328, 136, 27);
		modelDesignComboBox.addActionListener(e -> {
			handleComboBoxActions(e, "model_design");
		});
		add(modelDesignComboBox);
		modelDesignData = new JTextField();
		modelDesignData.setColumns(10);
		modelDesignData.setBounds(525, 328, 254, 27);
		add(modelDesignData);
		
		visibleOnMap = new JCheckBox("Visible On Map");
		visibleOnMap.setFont(new Font("Tahoma", Font.PLAIN, 13));
		visibleOnMap.setBounds(610, 362, 120, 27);
		visibleOnMap.addActionListener(e -> {
			defs.setVisibleOnMap(visibleOnMap.isSelected());
		});
		add(visibleOnMap);
		
		JLabel label_2 = new JLabel("Client Scripts:");
		label_2.setFont(new Font("Tahoma", Font.BOLD, 14));
		label_2.setBounds(459, 361, 95, 27);
		add(label_2);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(376, 393, 268, 170);
		add(scrollPane_1);
		clientScripts = new JTextArea();
		clientScripts.setFont(new Font("Courier New", Font.PLAIN, 15));
		clientScripts.addKeyListener(getKeyListener(clientScripts));
		scrollPane_1.setViewportView(clientScripts);
		
		setVisible(true);
		setKeyListeners();
		addAllNPCs();
		KeyUtils.updateKeyPostProcessor(getKeyPostProcessor());
	}
	
	/**
	 * Adds the npcs to the list model.
	 */
	public void addAllNPCs() {
		for (int id = 0; id < CacheUtils.getNPCsSize(Cache.INSTANCE); id++) {
			addNPCListModel(NPCDefinition.getNPCDefinition(Cache.INSTANCE, id));
		}
	}

	/**
	 * 
	 * @param defs
	 */
	public void addNPCListModel(final NPCDefinition defs) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				npcsListModel.addElement(defs);
			}
		});
	}

	/**
	 * 
	 * @param defs
	 */
	public void updateNPCListModel(final NPCDefinition defs) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				int index = npcsListModel.indexOf(defs);
				if (index == -1)
					npcsListModel.addElement(defs);
				else
					npcsListModel.setElementAt(defs, index);
			}
		});
	}
	
	/**
	 * Replaces the element in the npc list model.
	 * @param index
	 * @param def
	 */
	public void replaceNPCListModel(int index, final NPCDefinition def) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				npcsListModel.set(index, def);
			}
		});
	}

	/**
	 * removes an element from the npc list model.
	 * @param defs
	 */
	public void removeNPCListModel(final NPCDefinition defs) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				npcsListModel.removeElement(defs);
			}
		});
	}
	
	/**
	 * Deletes the selected def
	 */
	public void deleteNPC() {
		NPCDefinition defs = (NPCDefinition) npcsList.getSelectedValue();
		int npcId = defs.id;
		int index = npcsListModel.indexOf(defs);
		boolean last = npcId == (CacheUtils.getNPCsSize() - 1);
		int opcode = JOptionPane.showOptionDialog(null,
				"Are you sure you want to delete this npc? " + defs.id
						+ " - " + defs.name, "Delete the current NPC",
				JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE,
				null, new String[] { "Yes", "No" }, "Yes");
		if (opcode == JOptionPane.YES_OPTION) {
			MainFrame.writeLog("NPC Definitions Editor", "Removed NPC: "
				+ defs.id + " - " + defs.name);
			Cache.INSTANCE.getIndices()[18].removeFile(defs.getArchiveId(), defs.getFileId());
			NPCDefinition def = new NPCDefinition(Cache.INSTANCE, npcId);
			if(!last)
				Cache.INSTANCE.getIndices()[18].putFile(def.getArchiveId(), def.getFileId(), def.encode());
			if(last)
				removeNPCListModel(defs);
			else
				replaceNPCListModel(index, def);
		}
	}
	
	/**
	 * The searching of definitions.
	 * 
	 * @param type
	 * @return
	 */
	public KeyListener getKeyListener(Object classType) {
		final JTextField textField = classType instanceof JTextField ? (JTextField) classType : null;
		final JTextArea csArea = classType instanceof JTextArea ? (JTextArea) classType : null;
		
		return new KeyListener() {
			
			boolean search = textField != null && textField.equals(npcSearch);

			@Override
			public void keyPressed(KeyEvent event) {
				boolean ctrlPressed = KeyUtils.isCtrlPressed(event);
				if(!recommendSaving && textField == null && !ctrlPressed && csArea == null)
					fillFields();
			}

			@Override
			public void keyReleased(KeyEvent event) {
				boolean ctrlPressed = KeyUtils.isCtrlPressed(event);
				if(textField != null && !search && !ctrlPressed) {
					if(event.getKeyCode() == KeyEvent.VK_LEFT || event.getKeyCode() == KeyEvent.VK_RIGHT)
						return;
					saveData(textField.getName());
				} if(textField == null && !search && !ctrlPressed && csArea != null) {
					if(event.getKeyCode() == KeyEvent.VK_LEFT || event.getKeyCode() == KeyEvent.VK_RIGHT)
						return;
					saveData("client scripts");
				} else if(textField == null && !ctrlPressed)
					fillFields();
				if(search && !ctrlPressed) {
					if(textField.getText().equals("")) {
						try {
							npcsListModel.clear();
							addAllNPCs();
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						npcsListModel.clear();
						for(int i = 0; i < CacheUtils.getNPCsSize(); i++) {
							NPCDefinition def = NPCDefinition.get(i);
							if(def.name.toLowerCase().contains(textField.getText().toLowerCase())) {
								npcsListModel.addElement(def);
							}
								
						}
					}
				}

			}

			@Override
			public void keyTyped(KeyEvent event) {

			}
		};
	}
	
	/**
	 * 
	 * @param e 
	 * @param string
	 */
	private void handleComboBoxActions(ActionEvent e, String type) {
		if(defs == null)
			return;
		String selectedComponent = (String) ((JComboBox<?>) e.getSource()).getSelectedItem();
		switch(type) {
			case "size":
				switch(selectedComponent) {
					case "Size":
						sizeData.setText("" + defs.size);
						break;
					case "Width":
						sizeData.setText(""+defs.width);
						break;
					case "Height":
						sizeData.setText(""+defs.height);
						break;
				}
				break;
			case "options":
				switch(selectedComponent) {
					case "Right-Click Options":
						optionsData.setText(getOptions("right-click"));
						break;
					case "Tooltip Text":
						optionsData.setText(getOptions("tooltip"));
						break;
				}
				break;
			case "model_design":
				switch(selectedComponent) {
					case "Model Colors":
						modelDesignData.setText(getModelDesign("colors"));
						break;
					case "Model Textures":
						modelDesignData.setText(getModelDesign("textures"));
						break;
				}
				break;
		}
		saveData(selectedComponent.toLowerCase());
	}
	
	/**
	 * Saves the specified data in the cache.
	 * @param data
	 */
	private void saveData(String data) {
		if(!canSaveData)
			return;
		switch(data) {	
		
			case "npcName":
				if(npcName.getText().isEmpty()) {
					defs.name = "null";
					return;
				}
				defs.name = npcName.getText();
				break;
			case "models":
				setModels();
				break;
			case "chatHeads":
				setChatHeads();
				break;
			case "combatLevel":
				putInt("combatLevel", combatLevel.getText());
				break;
			case "headIcon":
				putInt("headIcon", headIcon.getText());
				break;
			case "walkMask":
				putInt("walkMask", walkMask.getText());
				break;
			case "respawnDirection":
				putInt("respawnDirection", respawnDirection.getText());
				break;
			case "render":
				putInt("renderEmote", render.getText());
				break;
		
			// NPC options
			case "right-click options":
				if(previousOptionsData.equals("right-click options"))
					setOptions("right-click");
				previousOptionsData = data;
				break;
			case "tooltip text":
				if(previousOptionsData.equals("tooltip text"))
					setOptions("tooltip text");
				previousOptionsData = data;
				break;
			case "optionsData":
				String selectedIndex = optionsComboBox.getSelectedItem().toString().toLowerCase();
				if(selectedIndex.equals("right-click options"))
					setOptions("right-click");
				else if(selectedIndex.equals("tooltip text"))
					setOptions("tooltip text");
				break;
				
			// NPC Model size
			case "size":
				if(previousSizeData.equals("size"))
					putInt("size", sizeData.getText());
				previousSizeData = data;
				break;
			case "width":
				if(previousSizeData.equals("width"))
					putInt("width", sizeData.getText());
				previousSizeData = data;
				break;
			case "height":
				if(previousSizeData.equals("height"))
					putInt("height", sizeData.getText());
				previousSizeData = data;
				break;
			case "sizeData":
				selectedIndex = sizeComboBox.getSelectedItem().toString().toLowerCase();
				if(selectedIndex.equals("size")) {
					putInt("size", sizeData.getText());
				} else if(selectedIndex.equals("width"))
					putInt("width", sizeData.getText());
				else if(selectedIndex.equals("height"))
					putInt("height", sizeData.getText());
				break;
				
			// Model Design
			case "model colors":
				if(previousModelDesignData.equals("model colors"))
					setModelDesign("colors");
				previousModelDesignData = data;
				break;
			case "model textures":
				if(previousModelDesignData.equals("model textures"))
					setModelDesign("textures");
				previousModelDesignData = data;
				break;
			case "modelDesignData":
				selectedIndex = modelDesignComboBox.getSelectedItem().toString().toLowerCase();
				if(selectedIndex.equals("model colors"))
					setModelDesign("colors");
				else if(selectedIndex.equals("model textures"))
					setModelDesign("textures");
				break;
				
			// The client scripts
			case "client scripts":
				setClientScripts();
				break;
		}
	}
	
	/**
	 * 
	 * @param type
	 */
	public void setModels() {
		String text = models.getText();
		if(text.equals("")) {
			defs.modelIds = null;
			return;
		}
		String[] stackArray = text.replaceAll(" ", "").split(",");
		for(int i = 0; i < stackArray.length; i++) {
			try {
				Integer.parseInt(stackArray[i]);
			} catch(NumberFormatException e) {
				return;
			}
		}
		defs.modelIds = new int[stackArray.length];
		for(int i = 0; i < stackArray.length; i++)
			defs.modelIds[i] = Integer.parseInt(stackArray[i]);
		if(StringUtils.objectsMatch(defs.modelIds, previousDefs.modelIds))
			previousDefs.modelIds = defs.modelIds;
	}
	
	/**
	 * 
	 * @param type
	 */
	public void setChatHeads() {
		String text = chatHeads.getText();
		if(text.equals("")) {
			defs.chatHeads = null;
			return;
		}
		String[] stackArray = text.replaceAll(" ", "").split(",");
		for(int i = 0; i < stackArray.length; i++) {
			try {
				Integer.parseInt(stackArray[i]);
			} catch(NumberFormatException e) {
				return;
			}
		}
		defs.chatHeads = new int[stackArray.length];
		for(int i = 0; i < stackArray.length; i++)
			defs.chatHeads[i] = Integer.parseInt(stackArray[i]);
		if(StringUtils.objectsMatch(defs.chatHeads, previousDefs.chatHeads))
			previousDefs.chatHeads = defs.chatHeads;
	}
	
	/**
	 * 
	 * @param type
	 */
	public void setOptions(String type) {
		boolean tooltip = type.equals("tooltip text");
		String[] intoArray = optionsData.getText().replaceAll(" ", "").split(",");
		if(optionsData.getText().equals("") || (!tooltip && intoArray.length < 5)) {
			if(tooltip)
				setToolTip("");
			else
				defs.options = new String[] { null, null, null, null, null };
			return;
		}
		for(int i = 0; i < intoArray.length; i++)
			if(intoArray[i].equals("null"))
				intoArray[i] = null;
		if(tooltip)
			setToolTip(optionsData.getText());
		 else
			defs.options = intoArray;
		if(!tooltip && StringUtils.objectsMatch(defs.options, previousDefs.options))
			previousDefs.options = defs.options;
	}
	
	/**
	 * sets the client scripts!
	 */
	public void setClientScripts() {
		String text = clientScripts.getText();
		text = text.replace("K:", "").replace("V:", "").replace("[", "").replace("]", "").
			replace("\t", "").replace("\n", "").replaceAll(" ", "").trim();
		if(text.equals("")) {
			defs.clientScripts = null;
			return;
		}
		String[] tokens = text.split(",");
		for(String values : tokens) {
			String[] split = values.split(":");
			if(split.length < 2)
				return;
			try {
				Integer.parseInt(split[0]);
			} catch(NumberFormatException e) {
				return;
			}
		}
		defs.clientScripts = new HashMap<Integer, Object>();
		for(String values : tokens) {
			String[] splitValues = values.split(":");
			Integer key = Integer.valueOf(splitValues[0]);
			Object value = null;
			try {
				value = Integer.parseInt(splitValues[1]);
			} catch(NumberFormatException e) {
				value = splitValues[1];
			}
			defs.clientScripts.remove(key);
			defs.clientScripts.put(key, value);
		}
	}
	
	/**
	 * 
	 * @param type
	 */
	public void setModelDesign(String type) {
		boolean colors = type.equals("colors");
		final String[] intoArray = modelDesignData.getText().
			replace("[", "").replace("]", "").replaceAll(" ", "").split(",");
		if(modelDesignData.getText().equals("")) {
			if(colors)
				defs.resetModelColors();
			else
				defs.resetTextures();
			return;
		}
		boolean incorrectSize = false;
		for (final String designs : intoArray) {
			final String[] modifiedDesigns = designs.split("=");
			if(modifiedDesigns.length < 2)
				incorrectSize = true;
			for(String value : modifiedDesigns) {
				try {
					Integer.parseInt(value);
				} catch(NumberFormatException e) {
					return;
				}
			}
		}
		if(incorrectSize)
			return;
		if(colors)
			defs.resetModelColors();
		else
			defs.resetTextures();
		for (final String designs : intoArray) {
			final String[] modifiedDesigns = designs.split("=");
			if(colors)
				defs.changeModelColor(Integer.valueOf(modifiedDesigns[0]), Integer.valueOf(modifiedDesigns[1]));
			else
				defs.changeModelTexture(Integer.valueOf(modifiedDesigns[0]), Integer.valueOf(modifiedDesigns[1]));
		}
		if(colors) {
			if(StringUtils.objectsMatch(defs.originalModelColors, previousDefs.originalModelColors)) {
				previousDefs.originalModelColors = defs.originalModelColors;
			}
			if(StringUtils.objectsMatch(defs.modifiedModelColors, previousDefs.modifiedModelColors)) {
				previousDefs.modifiedModelColors = defs.modifiedModelColors;
			}
		} else {
			if(StringUtils.objectsMatch(defs.originalTextures, previousDefs.originalTextures)) {
				previousDefs.originalTextures = defs.originalTextures;
			}
			if(StringUtils.objectsMatch(defs.modifiedTextures, previousDefs.modifiedTextures)) {
				previousDefs.modifiedTextures = defs.modifiedTextures;
			}
		}
	}
	
	/**
	 * Lets all the textFields have key listeners.
	 */
	private void setKeyListeners() {
		try {
			Field[] fields = this.getClass().getDeclaredFields();
			for(Field f : fields) {
				f.setAccessible(true);
				if(f.getType() == JTextField.class && !f.getName().equals("npcSearch")) {
					((JTextField) f.get(this)).addKeyListener(getKeyListener((JTextField) f.get(this)));
					((JTextField) f.get(this)).setName(f.getName());
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Adds a new definition.
	 */
	public void addNewNPC() {
		NPCDefinition defs = null;
		int opcode = JOptionPane.showOptionDialog(null,
				"Would you like to specify the NPC ID?",
				"NPC Definitions Editor", JOptionPane.YES_NO_OPTION,
				JOptionPane.WARNING_MESSAGE, null, new String[] {
						"Yes", "No" }, "No");
		if(opcode == JOptionPane.YES_OPTION) {
			int id = MainFrame.getNewDefinitionID("npc");
			if(id == -1) {
				JOptionPane.showMessageDialog(new JFrame(), "ID exceeds container size.");
				return;
			}
			defs = new NPCDefinition(Cache.INSTANCE, id, false);
		} else if(opcode == JOptionPane.NO_OPTION)
			defs = new NPCDefinition(Cache.INSTANCE, CacheUtils
					.getNPCsSize(Cache.INSTANCE), false);
		final NPCDefinition finalDefs = defs;
		new Thread() {
			public void run() {
				new NPCDefinitionEditor(finalDefs).setVisible(true);
			}
		}.start();
	}
	
	/**
	 * Views the selected npc
	 */
	public void viewNPC() {
		NPCDefinition defs = (NPCDefinition) npcsList
				.getSelectedValue();
		if (defs == null) {
			return;
		}
		final NPCDefinition finalDefs = defs;
		new Thread() {
			public void run() {
				new NPCDefinitionEditor(finalDefs).setVisible(true);
			}
		}.start();
	}
	
	/**
	 * Clones the definition.
	 */
	public void cloneNPC() {
		NPCDefinition defs = npcsList.getSelectedValue();
		if (defs == null)
			return;
		defs = (NPCDefinition) defs.clone();
		if (defs == null)
			return;
		int opcode = JOptionPane.showOptionDialog(null,
				"Would you like to specify the NPC ID?",
				"NPC Definition Editor", JOptionPane.YES_NO_OPTION,
				JOptionPane.WARNING_MESSAGE, null, new String[] {
						"Yes", "No" }, "No");
		if(opcode == JOptionPane.YES_OPTION) {
			int id = MainFrame.getNewDefinitionID("npc");
			if(id == -1) {
				JOptionPane.showMessageDialog(new JFrame(), "ID exceeds container size.");
				return;
			}
			defs.id = id;
		} else if(opcode == JOptionPane.NO_OPTION)
			defs.id = CacheUtils.getNPCsSize();
		new NPCDefinitionEditor(defs);
		MainFrame.writeLog("NPC Definition Editor", "Cloned NPC: " + defs.id
				+ " - " + defs.getName());
	}

	/**
	 * The mouse listener
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public MouseAdapter getMouseListener() {
		return new MouseAdapter() {

			private void showPopupMenu(MouseEvent e) {
				JPopupMenu menu = new JPopupMenu();
				JList<NPCDefinition> list = ((JList<NPCDefinition>) e.getSource());
				int row = list.locationToIndex(e.getPoint());
				list.setSelectedIndex(row);
				NPCDefinition def = list.getSelectedValue();
				String info = "[" + def.id + " - " + def.name + "]";
				JMenuItem[] options = { 
					new JMenuItem("View: " + info), 
					new JMenuItem("Clone: " + info),
					new JMenuItem("Delete: " + info), 
					new JMenuItem("Add New Item") 
				};
				for (JMenuItem i : options)
					menu.add(i);
				options[0].addActionListener(actionEvent -> {
					viewNPC();
				});
				options[1].addActionListener(actionEvent -> {
					cloneNPC();
				});
				options[2].addActionListener(actionEvent -> {
					deleteNPC();
				});
				options[3].addActionListener(actionEvent -> {
					addNewNPC();
				});
				menu.show(e.getComponent(), e.getX(), e.getY());
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2)
					viewNPC();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger())
					showPopupMenu(e);
				fillFields();
			}
		};
	}
	
	/**
	 * Saves the item definition.
	 */
	public void save() {
		if(defs.matches(previousDefs) && !isNewNPC)
			return;
		else if(!defs.matches(previousDefs) || isNewNPC) {
			defs.changeDefinition(Cache.INSTANCE);
			printNPCDetails();
			previousDefs = defs.clone();
			isNewNPC = false;
			clientScripts.setText(getClientScripts());
		}
	}
	
	/**
	 * Fills the fields with the definition data
	 */
	public void fillFields() {
		int lastIndex = npcsListModel.indexOf(defs);
		if(isNewNPC) {
			recommendSaving(lastIndex);
			recommendSaving = true;
			return;
		}
		if (defs != null && previousDefs != null) {
			if (!defs.matches(previousDefs)) {
				recommendSaving(lastIndex);
				recommendSaving = true;
				return;
			}
		}
		defs = npcsList.getSelectedValue();
		if (defs == null)
			return;
		previousDefs = defs.clone();
		npcIDLabel.setText("NPC: " + defs.id);
		npcName.setText(defs.name);
		optionsData.setText(getOptions("right-click"));
		headIcon.setText(""+defs.headIcon);
		combatLevel.setText(""+defs.combatLevel);
		respawnDirection.setText(""+defs.respawnDirection);
		render.setText(""+defs.renderEmote);
		chatHeads.setText(getChatHeads());
		walkMask.setText(""+defs.walkMask);
		sizeData.setText(""+defs.size);
		models.setText(getModels());
		modelDesignData.setText(getModelDesign("colors"));
		visibleOnMap.setSelected(defs.visibleOnMap);
		clientScripts.setText(getClientScripts());
		canSaveData = false;
		optionsComboBox.setSelectedIndex(0);
		sizeComboBox.setSelectedIndex(0);
		modelDesignComboBox.setSelectedIndex(0);
		canSaveData = true;
		KeyUtils.updateKeyPostProcessor(getKeyPostProcessor());
	}
	
	/**
	 * Returns the clientScripts of the item.
	 * 
	 * @return
	 */
	public String getClientScripts() {
		StringBuilder sb = new StringBuilder();
		if (defs.clientScripts == null || defs.clientScripts.isEmpty())
			return "";
		else {
			Iterator<Integer> keySet = defs.clientScripts.keySet().iterator();
			Iterator<Object> values = defs.clientScripts.values().iterator();
			int key = -1;
			Object value = null;
			for (int i = 0; i < defs.clientScripts.values().size(); i++) {
				key = keySet.next();
				value = values.next();
				sb.append("[K: " + key + " : V: " + value + "]" + (i == defs.clientScripts.size() - 1 ? "" : ",\n"));
			}
		}
		return sb.toString();
	}
	
	/**
	 * 
	 * @param string
	 */
	public void setToolTip(String tooltip) {
		if(tooltip.equals("")) {
			if(defs.clientScripts != null && defs.clientScripts.get(1) != null) {
				defs.clientScripts.remove(1);
				if(defs.clientScripts.size() == 0)
					defs.clientScripts = null;
			}
			return;
		}
		Object[] keys = null;
		Object[] values = null;
		if(defs.clientScripts != null) {
			keys = defs.clientScripts.keySet().toArray();
			values = defs.clientScripts.values().toArray();
		}
		defs.clientScripts = new HashMap<Integer, Object>();
		if(keys != null && values != null) {
			for(int i = 0; i < keys.length; i++) {
				defs.clientScripts.put(Integer.valueOf(""+keys[i]), ""+values[i]);
			}
		}
		defs.clientScripts.put(1, tooltip);
	}
	
	/**
	 * 
	 * @return
	 */
	public String getChatHeads() {
		return defs.chatHeads != null ? Arrays.toString(defs.chatHeads).replace("[", "").replace("]", "") : "";
	}
	
	/**
	 * 
	 * @return
	 */
	public String getModels() {
		return defs.modelIds != null ? Arrays.toString(defs.modelIds).replace("[", "").replace("]", "") : "";
	}
	
	/**
	 * Returns the inventory or ground options of the item.
	 * 
	 * @param type
	 * @return
	 */
	public String getOptions(String type) {
		String options = "";
		if (type.equals("right-click"))
			options = Arrays.toString(defs.options).replace("[", "").replace("]", "");
		else
			options = defs.getToolTip();
		return options;
	}
	
	/**
	 *
	 * @param type
	 * @return
	 */
	public String getModelDesign(String type) {
		StringBuilder sb = new StringBuilder();
		if (type.equals("colors")) {
			int[] orig = defs.originalModelColors;
			int[] mod = defs.modifiedModelColors;
			if (orig == null || orig.length == 0)
				return "";
			for (int i = 0; i < orig.length; i++)
				sb.append("[" + orig[i] + "=" + mod[i] + "]" + (i == orig.length - 1 ? "" : ", "));
		} else {
			int[] orig = defs.originalTextures;
			int[] mod = defs.modifiedTextures;
			if (orig == null || orig.length == 0)
				return "";
			for (int i = 0; i < orig.length; i++)
				sb.append("[" + orig[i] + "=" + mod[i] + "]" + (i == orig.length - 1 ? "" : ", "));
		}
		return sb.toString();
	}
	
	/**
	 * If the user has made changes, this suggests saving.
	 */
	private void recommendSaving(int index) {
		int choice = JOptionPane.showOptionDialog(chatHeads, "You have unsaved changes. Continue without saving?",
			"NPC Definition Editor", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null,
				new String[] { "Yes", "No" }, "No");
		boolean yes = choice == JOptionPane.YES_OPTION;
		if(yes) {
			defs = previousDefs.clone();
			if(isNewNPC) {
				npcsListModel.removeElementAt(index);
				isNewNPC = false;
			} else
				replaceNPCListModel(index, previousDefs);
			fillFields();
		} else
			npcsList.setSelectedIndex(index);
		recommendSaving = false;
	}
	
	/**
	 * 
	 * @param i
	 * @param intValue
	 */
	private void putInt(String variable, String intValue) {
		try {
			Integer.parseInt(intValue);
		} catch(NumberFormatException e) {
			return;
		}
		try {
			Field[] fields = defs.getClass().getDeclaredFields();
			for(Field f : fields) {
				f.setAccessible(true);
				if(f.getName().equals(variable)) {
					if(f.get(defs) instanceof Byte)
						defs.getClass().getField(variable).setByte(defs, ((byte) Integer.parseInt(intValue)));
					else
						defs.getClass().getField(variable).setInt(defs, Integer.parseInt(intValue));
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param defs
	 */
	public void printNPCDetails() {
		String[] model = { 
				"Name: "+defs.name,  
				"Height: "+defs.height, 
				"Width: "+defs.width, 
				"Size: "+defs.size, 
				"Combat: "+defs.combatLevel,
				"Render: "+defs.renderEmote, 
				"Respawn Dir.: "+defs.respawnDirection};
		String text = "";
		for(int i = 0; i < model.length; i++)
		text = new StringBuilder().append(text).append(model[i]).append(", ").toString();
		System.out.println(text);
	}
	
	/**
	 * Basically handles what happens after the user types something.
	 */
	private KeyEventPostProcessor getKeyPostProcessor() {
		return new KeyEventPostProcessor() {

			@Override
			public boolean postProcessKeyEvent(KeyEvent e) {
				switch(e.getID()) {
					case KeyEvent.KEY_PRESSED:
						if(KeyUtils.ctrlPressed(e, KeyEvent.VK_S))
							save();
						return true;
					case KeyEvent.KEY_RELEASED:
						return true;
					case KeyEvent.KEY_TYPED:
						return true;
					default:
						return true;
				}
			}
		};
	}
}
