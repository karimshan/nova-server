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
import java.text.NumberFormat;
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
import org.nova.cache.ce.editors.ItemDefinitionEditor;
import org.nova.cache.definition.ItemDefinition;
import org.nova.cache.utility.CacheUtils;
import org.nova.kshan.utilities.KeyUtils;
import org.nova.kshan.utilities.StringUtils;

/**
 * The item definition editing tabbedPane panel
 * 
 * @author Karimshan Nawaz
 *
 */
public class ItemPanel extends JPanel {
	
	/**
	 * @serialField
	 */
	private static final long serialVersionUID = -8611194620181429330L;
	
	public ItemDefinition previousDefs = null;
	public ItemDefinition defs = null;
	
	public JList<ItemDefinition> itemsList;
	public DefaultListModel<ItemDefinition> itemsListModel;
	
	private JLabel itemIDLabel;
	private JTextArea clientScripts;
	private JTextField lentID;
	private JTextField stackData;
	private JTextField modelData;
	private JTextField modelDesignData;
	private JTextField modelSizeData;
	private JTextField itemName;
	private JTextField notedID;
	private JTextField optionsData;
	private JTextField price;
	private JTextField itemSearch;
	private JCheckBox members;
	
	@SuppressWarnings("rawtypes")
	private JComboBox optionsComboBox, stackComboBox, modelDesignComboBox, modelSizeComboBox, modelsComboBox;
	
	private boolean recommendSaving;
	private boolean canSaveData;
	private boolean isNewItem;

	private String previousStackData = "stackable";
	private String previousOptionsData = "inventory options";
	private String previousModelData = "inventory model";
	private String previousModelSizeData = "model zoom";
	private String previousModelDesignData = "model colors";

	/**
	 * The item definition components.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ItemPanel() {
		super();
		setLayout(null);
		setBounds(0, 0, 764, 550);
		itemsListModel = new DefaultListModel<ItemDefinition>();
		itemsList = new JList<ItemDefinition>(itemsListModel);
		itemsList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		itemsList.setLayoutOrientation(JList.VERTICAL);
		itemsList.setVisibleRowCount(-1);
		itemsList.addKeyListener(getKeyListener(null));
		itemsList.addMouseListener(getMouseListener());
		JScrollPane itemListscrollPane = new JScrollPane(itemsList);
		itemListscrollPane.setBounds(15, 15, 335, 490);
		add(itemListscrollPane);
		itemSearch = new JTextField();
		itemSearch.setBounds(78, 522, 272, 27);
		itemSearch.addKeyListener(getKeyListener(itemSearch));
		add(itemSearch);
		itemSearch.setColumns(10);
		JLabel lblSearch = new JLabel("Search:");
		lblSearch.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblSearch.setBounds(15, 522, 62, 27);
		add(lblSearch);
		optionsComboBox = new JComboBox();
		optionsComboBox.setModel(new DefaultComboBoxModel(new String[] { "Inventory Options", "Ground Options" }));
		optionsComboBox.setFont(new Font("Tahoma", Font.PLAIN, 13));
		optionsComboBox.setBounds(376, 178, 136, 27);
		optionsComboBox.addActionListener(e -> {
			handleComboBoxActions(e, "options");
		});
		add(optionsComboBox);
		optionsData = new JTextField();
		optionsData.setBounds(520, 178, 208, 27);
		add(optionsData);
		optionsData.setColumns(10);
		JLabel lblName = new JLabel("Name:");
		lblName.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblName.setBounds(376, 28, 50, 27);
		add(lblName);
		itemName = new JTextField();
		itemName.setColumns(10);
		itemName.setBounds(430, 28, 208, 27);
		add(itemName);
		itemIDLabel = new JLabel("Item: -1");
		itemIDLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		itemIDLabel.setBounds(496, 0, 90, 27);
		add(itemIDLabel);
		JLabel lblPrice = new JLabel("Price:");
		lblPrice.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblPrice.setBounds(376, 58, 50, 27);
		add(lblPrice);
		price = new JTextField();
		price.setColumns(10);
		price.setBounds(430, 58, 95, 27);
		add(price);
		stackComboBox = new JComboBox();
		stackComboBox.setModel(new DefaultComboBoxModel(new String[] { "Stackable", "Stack IDs", "Stack Amounts" }));
		stackComboBox.setFont(new Font("Tahoma", Font.PLAIN, 13));
		stackComboBox.setBounds(376, 148, 136, 27);
		stackComboBox.addActionListener(e -> {
			handleComboBoxActions(e, "stack");
		});
		add(stackComboBox);
		stackData = new JTextField();
		stackData.setColumns(10);
		stackData.setBounds(520, 148, 208, 27);
		add(stackData);
		JLabel lblNotedId = new JLabel("Noted ID:");
		lblNotedId.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblNotedId.setBounds(376, 88, 62, 27);
		add(lblNotedId);
		notedID = new JTextField();
		notedID.setColumns(10);
		notedID.setBounds(440, 88, 85, 27);
		add(notedID);
		lentID = new JTextField();
		lentID.setColumns(10);
		lentID.setBounds(440, 118, 85, 27);
		add(lentID);
		JLabel lblLentId = new JLabel("Lent ID:");
		lblLentId.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblLentId.setBounds(376, 118, 62, 27);
		add(lblLentId);
		members = new JCheckBox("Members");
		members.setFont(new Font("Tahoma", Font.PLAIN, 13));
		members.setBounds(376, 298, 90, 27);
		members.addActionListener(e -> {
			defs.setMembersOnly(members.isSelected());
		});
		add(members);
		modelsComboBox = new JComboBox();
		modelsComboBox
				.setModel(new DefaultComboBoxModel(new String[] { "Inventory Model", "Male Models", "Female Models" }));
		modelsComboBox.setFont(new Font("Tahoma", Font.PLAIN, 13));
		modelsComboBox.setBounds(376, 208, 136, 27);
		modelsComboBox.addActionListener(e -> {
			handleComboBoxActions(e, "models");
		});
		add(modelsComboBox);
		modelData = new JTextField();
		modelData.setColumns(10);
		modelData.setBounds(520, 208, 208, 27);
		add(modelData);
		modelSizeComboBox = new JComboBox();
		modelSizeComboBox
				.setModel(new DefaultComboBoxModel(new String[] { "Model Zoom", "Model Rotations", "Model Offsets" }));
		modelSizeComboBox.setFont(new Font("Tahoma", Font.PLAIN, 13));
		modelSizeComboBox.setBounds(376, 238, 136, 27);
		modelSizeComboBox.addActionListener(e -> {
			handleComboBoxActions(e, "model_size");
		});
		add(modelSizeComboBox);
		modelSizeData = new JTextField();
		modelSizeData.setColumns(10);
		modelSizeData.setBounds(520, 238, 208, 27);
		add(modelSizeData);
		modelDesignComboBox = new JComboBox();
		modelDesignComboBox.setModel(new DefaultComboBoxModel(new String[] { "Model Colors", "Model Textures" }));
		modelDesignComboBox.setFont(new Font("Tahoma", Font.PLAIN, 13));
		modelDesignComboBox.setBounds(376, 268, 136, 27);
		modelDesignComboBox.addActionListener(e -> {
			handleComboBoxActions(e, "model_design");
		});
		add(modelDesignComboBox);
		modelDesignData = new JTextField();
		modelDesignData.setColumns(10);
		modelDesignData.setBounds(520, 268, 208, 27);
		add(modelDesignData);
		JLabel lblClientScripts = new JLabel("Client Scripts:");
		lblClientScripts.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblClientScripts.setBounds(458, 328, 95, 27);
		add(lblClientScripts);
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(376, 363, 270, 172);
		add(scrollPane_1);
		clientScripts = new JTextArea();
		clientScripts.setFont(new Font("Courier New", Font.PLAIN, 15));
		clientScripts.addKeyListener(getKeyListener(clientScripts));
		scrollPane_1.setViewportView(clientScripts);
		
		setVisible(true);
		KeyUtils.updateKeyPostProcessor(getKeyPostProcessor());
		setKeyListeners();
		addAllItems();
	}

	/**
	 * Adds all the items to the panel.
	 */
	public void addAllItems() {
		for (int id = 0; id < CacheUtils.getItemsSize(); id++)
			addItemListModel(ItemDefinition.get(Cache.INSTANCE, id));
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
			case "options":
				switch(selectedComponent) {
					case "Inventory Options":
						optionsData.setText(getOptions("inventory"));
						break;
					case "Ground Options":
						optionsData.setText(getOptions("ground"));
						break;
				}
				break;
			case "stack":
				switch(selectedComponent) {
					case "Stackable":
						stackData.setText(defs.isStackable() ? "yes" : "no");
						break;
					case "Stack IDs":
						stackData.setText(defs.stackIds == null ? "" : 
							Arrays.toString(defs.stackIds).replace("[", "").replace("]", ""));
						break;
					case "Stack Amounts":
						stackData.setText(defs.stackAmounts == null ? "" : 
							Arrays.toString(defs.stackAmounts).replace("[", "").replace("]", ""));
						break;
				}
				break;
			case "models":
				switch(selectedComponent) {
					case "Inventory Model":
						modelData.setText("" + defs.invModelId);
						break;
					case "Male Models":
						modelData.setText(
							defs.maleEquipModelId1+", "+defs.maleEquipModelId2+", "+defs.maleEquipModelId3);
						break;
					case "Female Models":
						modelData.setText(
							defs.femaleEquipModelId1+", "+defs.femaleEquipModelId2+", "+defs.femaleEquipModelId3);
						break;
				}
				break;
			case "model_size":
				switch(selectedComponent) {
					case "Model Zoom":
						modelSizeData.setText("" + defs.invModelZoom);
						break;
					case "Model Rotations":
						modelSizeData.setText(defs.modelRotationX+", "+defs.modelRotationY);
						break;
					case "Model Offsets":
						modelSizeData.setText(defs.modelOffsetX+", "+defs.modelOffsetY);
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
	 *
	 * @param defs
	 */
	public void addItemListModel(final ItemDefinition defs) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				itemsListModel.addElement(defs);
			}
		});
	}

	/**
	 * The add button.
	 */
	public void addNewItem() {
		int opcode = JOptionPane.showOptionDialog(null, "Would you like to specify the Item ID?",
			"Item Definition Editor", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null,
				new String[] { "Yes", "No" }, "No");
		if (opcode == JOptionPane.YES_OPTION) {
			int id = MainFrame.getNewDefinitionID("item");
			if (id == -1) {
				JOptionPane.showMessageDialog(new JFrame(), "ID exceeds container size.");
				return;
			}
			defs = new ItemDefinition(Cache.INSTANCE, id, false);
		} else if (opcode == JOptionPane.NO_OPTION)
			defs = new ItemDefinition(Cache.INSTANCE, CacheUtils.getItemsSize(), false);
		if(opcode == JOptionPane.YES_OPTION || opcode == JOptionPane.NO_OPTION) {
			defs.setName("New Item: "+defs.id);
			itemsListModel.addElement(defs);
			itemsList.setSelectedIndex(itemsListModel.indexOf(defs));
			previousDefs = defs.clone();
			fillFields();
			isNewItem = true;
		}
	}

	/**
	 * Clones the definition.
	 */
	public void cloneItem() {
		ItemDefinition cloned = defs.clone();
		int opcode = JOptionPane.showOptionDialog(null, "Would you like to specify the Item ID?",
			"Item Definition Editor", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null,
				new String[] { "Yes", "No" }, "No");
		if (opcode == JOptionPane.YES_OPTION) {
			int id = MainFrame.getNewDefinitionID("item");
			if (id == -1) {
				JOptionPane.showMessageDialog(new JFrame(), "ID exceeds container size.");
				return;
			}
			cloned.id = id;
		} else if (opcode == JOptionPane.NO_OPTION)
			cloned.id = CacheUtils.getItemsSize();
		if (opcode == JOptionPane.YES_OPTION || opcode == JOptionPane.NO_OPTION) {
			cloned.setName(cloned.name + " - (From: "+defs.id+")");
			itemsListModel.addElement(cloned);
			itemsList.setSelectedIndex(itemsListModel.indexOf(cloned));
			defs = cloned.clone();
			previousDefs = defs.clone();
			fillFields();
			isNewItem = true;
		}
		MainFrame.writeLog("Item Definition Editor", "Cloned Item: "+defs.id+" - New Name: " + cloned.getName());
	}

	/**
	 * Deletes the def.
	 */
	public void deleteItem() {
		ItemDefinition defs = itemsList.getSelectedValue();
		if (defs == null)
			return;
		int itemId = defs.id;
		int index = itemsListModel.indexOf(defs);
		boolean lastItem = itemId == (CacheUtils.getItemsSize() - 1);
		int opcode = JOptionPane.showOptionDialog(null,
				"Are you sure you want to delete this item? " + defs.id + " - " + defs.name, "Delete the current item",
				JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, new String[] { "Yes", "No" }, "Yes");
		if (opcode == JOptionPane.YES_OPTION) {
			opcode = JOptionPane.showOptionDialog(null, "Delete the models for: " + defs.id + " - " + defs.name,
					"Delete Item Models", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null,
					new String[] { "Yes", "No" }, "No");
			if (opcode == JOptionPane.YES_OPTION) {
				ItemDefinitionEditor.deleteModels(defs.id);
				MainFrame.writeLog("Item Definition Editor", "Removed Item: " + defs.id + " - " + defs.getName());
				Cache.INSTANCE.getIndices()[19].removeFile(defs.getArchiveId(), defs.getFileId());
				ItemDefinition def = new ItemDefinition(Cache.INSTANCE, itemId);
				if (!lastItem)
					Cache.INSTANCE.getIndices()[19].putFile(def.getArchiveId(), def.getFileId(), def.encode());
				if (lastItem)
					removeItemListModel(defs);
				else
					replaceItemListModel(index, def);
			} else if (opcode == JOptionPane.NO_OPTION) {
				MainFrame.writeLog("Item Definition Editor", "Removed Item: " + defs.id + " - " + defs.getName());
				Cache.INSTANCE.getIndices()[19].removeFile(defs.getArchiveId(), defs.getFileId());
				ItemDefinition def = new ItemDefinition(Cache.INSTANCE, itemId);
				if (!lastItem)
					Cache.INSTANCE.getIndices()[19].putFile(def.getArchiveId(), def.getFileId(), def.encode());
				if (lastItem)
					removeItemListModel(defs);
				else
					replaceItemListModel(index, def);
			}
		}
	}

	/**
	 * Views the item defs.
	 */
	public void viewItem() {
		ItemDefinition def = itemsList.getSelectedValue();
		if (def == null)
			return;
		final ItemDefinition finalDefs = def;
		new Thread() {
			@Override
			public void run() {
				new ItemDefinitionEditor(finalDefs).setVisible(true);
			}
		}.start();
	}

	/**
	 * Fills the fields with the definition data
	 */
	public void fillFields() {
		int lastIndex = itemsListModel.indexOf(defs);
		if(isNewItem) {
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
		defs = itemsList.getSelectedValue();
		if (defs == null)
			return;
		previousDefs = defs.clone();
		itemIDLabel.setText("Item: " + defs.id);
		itemName.setText(defs.name);
		optionsData.setText(getOptions("inventory"));
		price.setText("" + NumberFormat.getInstance().format(defs.value));
		stackData.setText(defs.stackable == 1 ? "yes" : "no");
		notedID.setText("" + defs.notedItemId);
		lentID.setText("" + defs.lentItemId);
		modelData.setText("" + defs.invModelId);
		modelSizeData.setText("" + defs.invModelZoom);
		modelDesignData.setText(getModelDesign("colors"));
		members.setSelected(defs.membersOnly);
		clientScripts.setText(getClientScripts());
		canSaveData = false;
		optionsComboBox.setSelectedIndex(0);
		stackComboBox.setSelectedIndex(0);
		modelDesignComboBox.setSelectedIndex(0);
		modelSizeComboBox.setSelectedIndex(0);
		modelsComboBox.setSelectedIndex(0);
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
		if (defs.clientScriptData == null || defs.clientScriptData.isEmpty())
			return "";
		else {
			Iterator<Integer> keySet = defs.clientScriptData.keySet().iterator();
			Iterator<Object> values = defs.clientScriptData.values().iterator();
			int key = -1;
			Object value = null;
			for (int i = 0; i < defs.clientScriptData.values().size(); i++) {
				key = keySet.next();
				value = values.next();
				sb.append("[K: " + key + " : V: " + value + "]" + (i == defs.clientScriptData.size() - 1 ? "" : ",\n"));
			}
		}
		return sb.toString();
	}

	/**
	 * Returns the inventory options of an item.
	 *
	 * @param defs
	 * @return
	 */
	public String getInventoryOptions(ItemDefinition defs) {
		String text = "";
		for (final String option : defs.inventoryOptions)
			text = new StringBuilder().append(text).append(option == null ? "null" : option).append(";").toString();
		return text;
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
			
			boolean search = textField != null && textField.equals(itemSearch);

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
					if (textField.getText().equals("")) {
						try {
							itemsListModel.clear();
							addAllItems();
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						itemsListModel.clear();
						for (int i = 0; i < CacheUtils.getItemsSize(); i++) {
							ItemDefinition def = ItemDefinition.get(i);
							if (def.name.toLowerCase().contains(textField.getText().toLowerCase())) {
								itemsListModel.addElement(def);
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
	 * The mouse listener
	 *
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public MouseAdapter getMouseListener() {
		return new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2)
					viewItem();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger())
					showPopupMenu(e);
				fillFields();
			}

			private void showPopupMenu(MouseEvent e) {
				JPopupMenu menu = new JPopupMenu();
				JList<ItemDefinition> list = ((JList<ItemDefinition>) e.getSource());
				int row = list.locationToIndex(e.getPoint());
				list.setSelectedIndex(row);
				ItemDefinition def = list.getSelectedValue();
				String info = "[" + def.id + " - " + def.name + "]";
				JMenuItem[] options = { new JMenuItem("View: " + info), new JMenuItem("Clone: " + info),
						new JMenuItem("Delete: " + info), new JMenuItem("Add New Item") };
				for (JMenuItem i : options)
					menu.add(i);
				options[0].addActionListener(actionEvent -> {
					viewItem();
				});
				options[1].addActionListener(actionEvent -> {
					cloneItem();
				});
				options[2].addActionListener(actionEvent -> {
					deleteItem();
				});
				options[3].addActionListener(actionEvent -> {
					addNewItem();
				});
				menu.show(e.getComponent(), e.getX(), e.getY());
			}
		};
	}

	/**
	 * Returns the inventory or ground options of the item.
	 * 
	 * @param type
	 * @return
	 */
	public String getOptions(String type) {
		String options = "";
		if (type.equals("inventory"))
			options = Arrays.toString(defs.inventoryOptions).replace("[", "").replace("]", "");
		else
			options = Arrays.toString(defs.groundOptions).replace("[", "").replace("]", "");
		return options;
	}

	/**
	 * Prints the details of an item.
	 *
	 * @param defs
	 */
	public void printItemDetails(ItemDefinition defs) {
		String[] model = { "Name: " + defs.getName(), "Item ID: " + defs.id, "Inventory Model ID: " + defs.invModelId,
				"Worn Model Id: " + defs.maleEquipModelId1, "Price: " + NumberFormat.getInstance().format(defs.value),
				"Inventory Options: " + getInventoryOptions(defs) };
		String text = "";
		for (String element : model)
			text = new StringBuilder().append(text).append(element).append(", ").toString();
		MainFrame.writeLog("Item Details", text);
	}

	/**
	 * If the user has made changes, this suggests saving.
	 */
	private void recommendSaving(int index) {
		int choice = JOptionPane.showOptionDialog(modelsComboBox, "You have unsaved changes. Continue without saving?",
			"Item Definition Editor", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null,
				new String[] { "Yes", "No" }, "No");
		boolean yes = choice == JOptionPane.YES_OPTION;
		if(yes) {
			defs = previousDefs.clone();
			if(isNewItem) {
				itemsListModel.removeElementAt(index);
				isNewItem = false;
			} else
				replaceItemListModel(index, previousDefs);
			fillFields();
		} else
			itemsList.setSelectedIndex(index);
		recommendSaving = false;
	}

	/**
	 *
	 * @param defs
	 */
	public void removeItemListModel(final ItemDefinition defs) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				itemsListModel.removeElement(defs);
			}
		});
	}

	/**
	 *
	 * @param index
	 * @param def
	 */
	public void replaceItemListModel(int index, final ItemDefinition def) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				itemsListModel.set(index, def);
			}
		});
	}
	
	/**
	 * Saves the specified data in the cache.
	 * @param data
	 */
	private void saveData(String data) {
		if(!canSaveData)
			return;
		switch(data) {	
		
			// Integer/String definition values
			case "lentID":
				try {
					if(isEmpty(lentID.getText())) {
						defs.lentItemId = -1;
						return;
					}
					defs.lentItemId = Integer.parseInt(lentID.getText());
				} catch(NumberFormatException e) {
					return;
				}
				break;
			case "itemName":
				if(isEmpty(itemName.getText())) {
					defs.name = "null";
					return;
				}
				defs.name = itemName.getText();
				break;
			case "notedID":
				try {
					if(isEmpty(notedID.getText())) {
						defs.notedItemId = -1;
						return;
					}
					defs.notedItemId = Integer.parseInt(notedID.getText());
				} catch(NumberFormatException e) {
					return;
				}
				break;
			case "price":
				try {
					if(isEmpty(price.getText())) {
						defs.value = 0;
						return;
					}
					String text = price.getText().replaceAll(" ", "").replaceAll(",", "");
					defs.value = Integer.parseInt(text);
				} catch(NumberFormatException e) {
					return;
				}
				break;
		
			// These are the item options
			case "inventory options":
				if(previousOptionsData.equals("inventory options"))
					setOptions("inventory");
				previousOptionsData = data;
				break;
			case "ground options":
				if(previousOptionsData.equals("ground options"))
					setOptions("ground");
				previousOptionsData = data;
				break;
			case "optionsData":
				String selectedIndex = optionsComboBox.getSelectedItem().toString().toLowerCase();
				if(selectedIndex.equals("inventory options"))
					setOptions("inventory");
				else if(selectedIndex.equals("ground options"))
					setOptions("ground");
				break;
				
			// Item stacking options
			case "stackable":
				if(previousStackData.equals("stackable")) {
					if(isEmpty(stackData.getText())) {
						defs.stackable = 0;
						previousStackData = data;
						return;
					}
					defs.stackable = stackData.getText().toLowerCase().trim().contains("yes") ? 1 : 0;
				}
				previousStackData = data;
				break;
			case "stack ids":
				if(previousStackData.equals("stack ids"))
					setStack("ids");
				previousStackData = data;
				break;
			case "stack amounts":
				if(previousStackData.equals("stack amounts"))
					setStack("amounts");
				previousStackData = data;
				break;
			case "stackData":
				selectedIndex = stackComboBox.getSelectedItem().toString().toLowerCase();
				if(selectedIndex.equals("stackable")) {
					if(isEmpty(stackData.getText())) {
						defs.stackable = 0;
						return;
					}
					defs.stackable = stackData.getText().toLowerCase().trim().contains("yes") ? 1 : 0;
				}
				else if(selectedIndex.equals("stack ids"))
					setStack("ids");
				else if(selectedIndex.equals("stack amounts"))
					setStack("amounts");
				break;
				
			// Models
			case "inventory model":
				if(previousModelData.equals("inventory model")) {
					try {
						if(isEmpty(modelData.getText())) {
							defs.invModelId = 0;
							previousModelData = data;
							return;
						}
						defs.invModelId = Integer.parseInt(modelData.getText());
					} catch(NumberFormatException e) {
						return;
					}
				}
				previousModelData = data;
				break;
			case "male models":
				if(previousModelData.equals("male models"))
					setModels("male");
				previousModelData = data;
				break;
			case "female models":
				if(previousModelData.equals("female models"))
					setModels("female");
				previousModelData = data;
				break;
			case "modelData":
				selectedIndex = modelsComboBox.getSelectedItem().toString().toLowerCase();
				if(selectedIndex.equals("inventory model")) {
					try {
						if(isEmpty(modelData.getText())) {
							defs.invModelId = 0;
							return;
						}
						defs.invModelId = Integer.parseInt(modelData.getText());
					} catch(NumberFormatException e) {
						return;
					}
				} else if(selectedIndex.equals("male models"))
					setModels("male");
				else if(selectedIndex.equals("female models"))
					setModels("female");
				break;
				
			// Inventory Model Size
			case "model zoom":
				if(previousModelSizeData.equals("model zoom")) {
					try {
						if(isEmpty(modelSizeData.getText())) {
							defs.invModelZoom = 2000;
							previousModelSizeData = data;
							return;
						}
						defs.invModelZoom = Integer.parseInt(modelSizeData.getText());
					} catch(NumberFormatException e) {
						return;
					}
				}
				previousModelSizeData = data;
				break;
			case "model rotations":
				if(previousModelSizeData.equals("model rotations"))
					setModelPosition("rotations");
				previousModelSizeData = data;
				break;
			case "model offsets":
				if(previousModelSizeData.equals("model offsets"))
					setModelPosition("offsets");
				previousModelSizeData = data;
				break;
			case "modelSizeData":
				selectedIndex = modelSizeComboBox.getSelectedItem().toString().toLowerCase();
				if(selectedIndex.equals("model zoom")) {
					try {
						if(isEmpty(modelSizeData.getText())) {
							defs.invModelZoom = 2000;
							return;
						}
						defs.invModelZoom = Integer.parseInt(modelSizeData.getText());
					} catch(NumberFormatException e) {
						return;
					}
				} else if(selectedIndex.equals("model rotations"))
					setModelPosition("rotations");
				else if(selectedIndex.equals("model offsets"))
					setModelPosition("offsets");
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
	 * Lets all the textFields have key listeners.
	 */
	private void setKeyListeners() {
		try {
			Field[] fields = this.getClass().getDeclaredFields();
			for(Field f : fields) {
				f.setAccessible(true);
				if(f.getType() == JTextField.class && !f.getName().equals("itemSearch")) {
					((JTextField) f.get(this)).addKeyListener(getKeyListener((JTextField) f.get(this)));
					((JTextField) f.get(this)).setName(f.getName());
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
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
	 * 
	 * @param type
	 */
	public void setOptions(String type) {
		boolean ground = type.equals("ground");
		String[] intoArray = optionsData.getText().replaceAll(" ", "").replaceAll("_", " ").split(",");
		for(int i = 0; i < intoArray.length; i++) {
			if(intoArray[i].equals("null")) {
				intoArray[i] = null;
			}
		}
		if(optionsData.getText().equals("")) {
			if(ground)
				defs.groundOptions = new String[] { null, null, "take", null, null };
			else
				defs.inventoryOptions = new String[] { null, null, null, null, "drop" };
			return;
		}
		if(intoArray.length < 5) {
			if(ground)
				defs.groundOptions = new String[] { null, null, "take", null, null };
			else
				defs.inventoryOptions = new String[] { null, null, null, null, "drop" };
			return;
		}
		if(ground)
			defs.groundOptions = intoArray;
		else
			defs.inventoryOptions = intoArray;
		if(ground) {
			if(StringUtils.objectsMatch(defs.groundOptions, previousDefs.groundOptions))
				previousDefs.groundOptions = defs.groundOptions;
		} else {
			if(StringUtils.objectsMatch(defs.inventoryOptions, previousDefs.inventoryOptions))
				previousDefs.inventoryOptions = defs.inventoryOptions;
		}
	}
	
	/**
	 * 
	 * @param type
	 */
	public void setModelPosition(String type) {
		boolean offsets = type.equals("offsets");
		String[] intoArray = modelSizeData.getText().replaceAll(" ", "").split(",");
		if(modelSizeData.getText().equals("")) {
			if(offsets) {
				defs.modelOffsetX = 0;
				defs.modelOffsetY = 0;
			} else {
				defs.modelRotationX = 0;
				defs.modelRotationY = 0;
			}
			return;
		}
		if(intoArray.length < 2)
			return;
		for(int i = 0; i < intoArray.length; i++) {
			try {
				Integer.parseInt(intoArray[i]);
			} catch(NumberFormatException e) {
				return;
			}
		}
		int position1 = Integer.parseInt(intoArray[0]);
		int position2 = Integer.parseInt(intoArray[1]);
		if(offsets) {
			defs.modelOffsetX = position1;
			defs.modelOffsetY = position2;
		} else {
			defs.modelRotationX = position1;
			defs.modelRotationY = position2;
		}
	}
	
	/**
	 * Saves the item definition.
	 */
	public void save() {
		if(defs.matches(previousDefs) && !isNewItem)
			return;
		else if(!defs.matches(previousDefs) || isNewItem) {
			defs.write(Cache.INSTANCE);
			//JOptionPane.showMessageDialog(modelsComboBox, "Saved Item: "+defs.toString(), "Item Definition Editor", JOptionPane.INFORMATION_MESSAGE);
			printItemDetails(defs);
			previousDefs = defs.clone();
			isNewItem = false;
			price.setText("" + NumberFormat.getInstance().format(defs.value));
			clientScripts.setText(getClientScripts());
		}
	}
	
	/**
	 * 
	 * @param type
	 */
	public void setModels(String type) {
		boolean male = type.equals("male");
		String[] intoArray = modelData.getText().replaceAll(" ", "").split(",");
		if(modelData.getText().equals("")) {
			if(male) {
				defs.maleEquipModelId1 = -1;
				defs.maleEquipModelId2 = -1;
				defs.maleEquipModelId3 = -1;
			} else {
				defs.femaleEquipModelId1 = -1;
				defs.femaleEquipModelId2 = -1;
				defs.femaleEquipModelId3 = -1;
			}
			return;
		}
		if(intoArray.length < 3) 
			return;
		for(int i = 0; i < intoArray.length; i++) {
			try {
				Integer.parseInt(intoArray[i]);
			} catch(NumberFormatException e) {
				return;
			}
		}
		int model1 = Integer.parseInt(intoArray[0]);
		int model2 = Integer.parseInt(intoArray[1]);
		int model3 = Integer.parseInt(intoArray[2]);
		if(male)
			defs.maleEquipModelId1 = model1;
		else
			defs.femaleEquipModelId1 = model1;
		if(male)
			defs.maleEquipModelId2 = model2;
		else
			defs.femaleEquipModelId2 = model2;
		if(male)
			defs.maleEquipModelId3 = model3;
		else
			defs.femaleEquipModelId3 = model3;
	}
	
	/**
	 * 
	 * @param type
	 */
	public void setStack(String type) {
		boolean ids = type.equals("ids");
		String text = stackData.getText();
		if(text.equals("")) {
			if(ids) {
				defs.stackIds = null;
			} else {
				defs.stackAmounts = null;
			}
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
		if(ids)
			defs.stackIds = new int[stackArray.length];
		else
			defs.stackAmounts = new int[stackArray.length];
		for(int i = 0; i < stackArray.length; i++) {
			if(ids)
				defs.stackIds[i] = Integer.parseInt(stackArray[i]);
			else
				defs.stackAmounts[i] = Integer.parseInt(stackArray[i]);
		}
	}

	/**
	 *
	 * @param defs
	 */
	public void updateItemList(final ItemDefinition defs) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				int index = itemsListModel.indexOf(defs);
				if (index == -1)
					itemsListModel.addElement(defs);
				else
					itemsListModel.setElementAt(defs, index);
			}
		});
	}
	
	/**
	 * sets the client scripts!
	 */
	public void setClientScripts() {
		String text = clientScripts.getText();
		text = text.replace("K:", "").replace("V:", "").replace("[", "").replace("]", "").
			replace("\t", "").replace("\n", "").replaceAll(" ", "").trim();
		if(text.equals("")) {
			defs.clientScriptData = null;
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
		defs.clientScriptData = new HashMap<Integer, Object>();
		for(String values : tokens) {
			String[] splitValues = values.split(":");
			Integer key = Integer.valueOf(splitValues[0]);
			Object value = null;
			try {
				value = Integer.parseInt(splitValues[1]);
			} catch(NumberFormatException e) {
				value = splitValues[1];
			}
			defs.clientScriptData.remove(key);
			defs.clientScriptData.put(key, value);
		}
	}
	
	/**
	 * returns if a string is empty or not
	 * @param s
	 * @return
	 */
	public boolean isEmpty(String s) {
		return s.equals("");
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
