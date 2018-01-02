package org.nova.cache.ce.editors;

import java.awt.BorderLayout;
import java.awt.DefaultKeyboardFocusManager;
import java.awt.Font;
import java.awt.KeyEventPostProcessor;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.nova.cache.Cache;
import org.nova.cache.ce.MainFrame;
import org.nova.cache.ce.Startup;
import org.nova.cache.definition.ItemDefinition;
import org.nova.cache.definition.NPCDefinition;
import org.nova.kshan.utilities.KeyUtils;

/**
 *
 * @author K-Shan
 *
 */
public class NPCDefinitionEditor extends JFrame {

	/**
	 * @serialField
	 */
	private static final long serialVersionUID = 8465373446597572205L;

	private final NPCDefinition defs;
	private final JTextField npcNameTextField;
	private final JTextField npcWidthTextField;
	private final JTextField npcHeightTextField;
	private final JTextField npcSizeTextField;
	private final JTextField walkMaskTextField;
	private final JTextField combatLevelTextField;
	private final JTextField respawnDirectionTextField;
	private final JTextField renderAnimationTextField;
	private final JTextField chatHeads;
	private final JTextField rightClickOptions;
	private final JCheckBox visibleOnMap;
	private JTextField headIcon;
	private JTextField modelColors;
	private JTextField textures;
	private JTextField modelIds;
	private JTextField itemsToBeEquipped;
	private JTextField cursor;

	public NPCDefinitionEditor(NPCDefinition defs) {
		setTitle("Editing definition of: " + defs.id + " - "
				+ defs.name);
		this.defs = defs;
		setBounds(100, 100, 756, 603);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		final JTabbedPane tabbedPane = new JTabbedPane(SwingConstants.TOP);
		getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		final JPanel generalSettingsPanel = new JPanel();
		tabbedPane.addTab("General Settings", null, generalSettingsPanel, null);
		generalSettingsPanel.setLayout(null);

		final JLabel nameLabel = new JLabel("Name:");
		nameLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		nameLabel.setBounds(28, 11, 53, 24);
		generalSettingsPanel.add(nameLabel);

		npcNameTextField = new JTextField();
		npcNameTextField.setFont(new Font("Tahoma", Font.PLAIN, 13));
		npcNameTextField.setText(defs.name);
		npcNameTextField.setBounds(91, 11, 250, 24);
		generalSettingsPanel.add(npcNameTextField);
		npcNameTextField.setColumns(10);

		final JLabel lblNpcWidth = new JLabel("Width:");
		lblNpcWidth.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblNpcWidth.setBounds(28, 83, 53, 24);
		generalSettingsPanel.add(lblNpcWidth);

		npcWidthTextField = new JTextField();
		npcWidthTextField.setFont(new Font("Tahoma", Font.PLAIN, 13));
		npcWidthTextField.setText("" + defs.width);
		npcWidthTextField.setColumns(10);
		npcWidthTextField.setBounds(91, 85, 167, 24);
		generalSettingsPanel.add(npcWidthTextField);

		final JLabel lblNpcHeight = new JLabel("Height:");
		lblNpcHeight.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblNpcHeight.setBounds(28, 133, 53, 24);
		generalSettingsPanel.add(lblNpcHeight);

		npcHeightTextField = new JTextField();
		npcHeightTextField.setFont(new Font("Tahoma", Font.PLAIN, 13));
		npcHeightTextField.setText("" + defs.height);
		npcHeightTextField.setColumns(10);
		npcHeightTextField.setBounds(91, 133, 146, 24);
		generalSettingsPanel.add(npcHeightTextField);

		npcSizeTextField = new JTextField();
		npcSizeTextField.setFont(new Font("Tahoma", Font.PLAIN, 13));
		npcSizeTextField.setText("" + defs.size);
		npcSizeTextField.setColumns(10);
		npcSizeTextField.setBounds(91, 278, 110, 24);
		generalSettingsPanel.add(npcSizeTextField);

		final JLabel lblNpcSize = new JLabel("Size:");
		lblNpcSize.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblNpcSize.setBounds(28, 278, 43, 24);
		generalSettingsPanel.add(lblNpcSize);

		walkMaskTextField = new JTextField();
		walkMaskTextField.setFont(new Font("Tahoma", Font.PLAIN, 13));
		walkMaskTextField.setText("" + defs.walkMask);
		walkMaskTextField.setColumns(10);
		walkMaskTextField.setBounds(123, 179, 175, 24);
		generalSettingsPanel.add(walkMaskTextField);

		final JLabel lblWalkMask = new JLabel("Walk Mask:");
		lblWalkMask.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblWalkMask.setBounds(28, 179, 91, 24);
		generalSettingsPanel.add(lblWalkMask);

		final JLabel lblCombatLevel = new JLabel("Combat Level:");
		lblCombatLevel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblCombatLevel.setBounds(28, 48, 103, 24);
		generalSettingsPanel.add(lblCombatLevel);

		combatLevelTextField = new JTextField();
		combatLevelTextField.setFont(new Font("Tahoma", Font.PLAIN, 13));
		combatLevelTextField.setText("" + defs.combatLevel);
		combatLevelTextField.setColumns(10);
		combatLevelTextField.setBounds(149, 50, 155, 24);
		generalSettingsPanel.add(combatLevelTextField);

		final JLabel lblRespawnDirection = new JLabel("Respawn Direction:");
		lblRespawnDirection.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblRespawnDirection.setBounds(28, 225, 136, 24);
		generalSettingsPanel.add(lblRespawnDirection);

		respawnDirectionTextField = new JTextField();
		respawnDirectionTextField.setFont(new Font("Tahoma", Font.PLAIN, 13));
		respawnDirectionTextField.setText("" + defs.respawnDirection);
		respawnDirectionTextField.setColumns(10);
		respawnDirectionTextField.setBounds(174, 229, 117, 24);
		generalSettingsPanel.add(respawnDirectionTextField);

		final JLabel lblRenderAnimation = new JLabel("Render Animation:");
		lblRenderAnimation.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblRenderAnimation.setBounds(28, 327, 136, 24);
		generalSettingsPanel.add(lblRenderAnimation);

		renderAnimationTextField = new JTextField();
		renderAnimationTextField.setFont(new Font("Tahoma", Font.PLAIN, 13));
		renderAnimationTextField.setText("" + defs.renderEmote);
		renderAnimationTextField.setColumns(10);
		renderAnimationTextField.setBounds(174, 331, 110, 24);
		generalSettingsPanel.add(renderAnimationTextField);

		visibleOnMap = new JCheckBox("Visible on Map?");
		visibleOnMap.setFont(new Font("Dialog", Font.PLAIN, 14));
		visibleOnMap.setBounds(317, 117, 146, 57);
		visibleOnMap.setSelected(defs.isVisibleOnMap() ? true : false);
		generalSettingsPanel.add(visibleOnMap);
		
		JButton saveDefsButton = new JButton("Save");
		saveDefsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				save();
			}
		});
		saveDefsButton.setFont(new Font("Tahoma", Font.ITALIC, 11));
		saveDefsButton.setBounds(346, 225, 89, 23);
		generalSettingsPanel.add(saveDefsButton);
		
		JButton reloadDefsButton = new JButton("Reload");
		reloadDefsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				reload();
			}
		});
		reloadDefsButton.setFont(new Font("Tahoma", Font.ITALIC, 11));
		reloadDefsButton.setBounds(465, 225, 89, 23);
		generalSettingsPanel.add(reloadDefsButton);
		
		JButton closeDefsButton = new JButton("Close");
		closeDefsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				close();
			}
		});
		closeDefsButton.setFont(new Font("Tahoma", Font.ITALIC, 11));
		closeDefsButton.setBounds(407, 262, 89, 23);
		generalSettingsPanel.add(closeDefsButton);
		
		JLabel lblChatHeads_1 = new JLabel("Head Icon:");
		lblChatHeads_1.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblChatHeads_1.setBounds(353, 11, 103, 24);
		generalSettingsPanel.add(lblChatHeads_1);
		
		headIcon = new JTextField();
		headIcon.setText(""+defs.headIcon);
		headIcon.setFont(new Font("Tahoma", Font.PLAIN, 13));
		headIcon.setColumns(10);
		headIcon.setBounds(444, 11, 91, 24);
		generalSettingsPanel.add(headIcon);
		
		JLabel lblRightClickOptions = new JLabel("Right Click Options:");
		lblRightClickOptions.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblRightClickOptions.setBounds(28, 382, 146, 24);
		generalSettingsPanel.add(lblRightClickOptions);
		
		JLabel lblChatHeads_2 = new JLabel("Chat Heads:");
		lblChatHeads_2.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblChatHeads_2.setBounds(28, 426, 136, 24);
		generalSettingsPanel.add(lblChatHeads_2);
		
		rightClickOptions = new JTextField();
		rightClickOptions.setText(rightClickOptions());
		rightClickOptions.setFont(new Font("Tahoma", Font.PLAIN, 13));
		rightClickOptions.setColumns(10);
		rightClickOptions.setBounds(174, 386, 402, 24);
		generalSettingsPanel.add(rightClickOptions);
		
		chatHeads = new JTextField();
		chatHeads.setText(chatHeads());
		chatHeads.setFont(new Font("Tahoma", Font.PLAIN, 13));
		chatHeads.setColumns(10);
		chatHeads.setBounds(149, 430, 402, 24);
		generalSettingsPanel.add(chatHeads);
		
		JLabel lblCursorId = new JLabel("Cursor ID:");
		lblCursorId.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblCursorId.setBounds(331, 55, 103, 24);
		generalSettingsPanel.add(lblCursorId);
		
		cursor = new JTextField();
		cursor.setText(defs.unknownInt6+"");
		cursor.setFont(new Font("Tahoma", Font.PLAIN, 13));
		cursor.setColumns(10);
		cursor.setBounds(444, 52, 91, 24);
		generalSettingsPanel.add(cursor);

		JPanel panel = new JPanel();
		tabbedPane.addTab("Model Related", null, panel, null);
		panel.setLayout(null);
		
		JLabel lblChangingTheModels = new JLabel("Changing the Model IDs");
		lblChangingTheModels.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblChangingTheModels.setBounds(222, 22, 181, 37);
		panel.add(lblChangingTheModels);
		
		modelIds = new JTextField();
		modelIds.setFont(new Font("Tahoma", Font.PLAIN, 15));
		modelIds.setText(modelIds());
		modelIds.setColumns(10);
		modelIds.setBounds(154, 70, 517, 37);
		panel.add(modelIds);
		
		JLabel lblModelIds = new JLabel("Model IDs:");
		lblModelIds.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblModelIds.setBounds(52, 79, 71, 19);
		panel.add(lblModelIds);
		
		JLabel lblChangingTheModel = new JLabel("Changing the Model Colors");
		lblChangingTheModel.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblChangingTheModel.setBounds(222, 121, 207, 37);
		panel.add(lblChangingTheModel);
		
		modelColors = new JTextField();
		modelColors.setFont(new Font("Tahoma", Font.PLAIN, 15));
		modelColors.setText(modelColors());
		modelColors.setColumns(10);
		modelColors.setBounds(154, 169, 517, 37);
		panel.add(modelColors);
		
		JLabel lblChangingTheTexture = new JLabel("Changing the Texture Colors");
		lblChangingTheTexture.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblChangingTheTexture.setBounds(222, 217, 207, 37);
		panel.add(lblChangingTheTexture);
		
		textures = new JTextField();
		textures.setFont(new Font("Tahoma", Font.PLAIN, 15));
		textures.setText(textureColors());
		textures.setColumns(10);
		textures.setBounds(154, 265, 517, 37);
		panel.add(textures);
		
		JLabel lblModelColors = new JLabel("Model Colors:");
		lblModelColors.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblModelColors.setBounds(38, 178, 106, 19);
		panel.add(lblModelColors);
		
		JLabel lblTextureColors = new JLabel("Texture Colors:");
		lblTextureColors.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblTextureColors.setBounds(38, 274, 118, 19);
		panel.add(lblTextureColors);
		
		JLabel lblItemsToBe = new JLabel("Items to be equipped:");
		lblItemsToBe.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblItemsToBe.setBounds(38, 381, 156, 19);
		panel.add(lblItemsToBe);
		
		itemsToBeEquipped = new JTextField();
		itemsToBeEquipped.setText((String) null);
		itemsToBeEquipped.setFont(new Font("Tahoma", Font.PLAIN, 15));
		itemsToBeEquipped.setColumns(10);
		itemsToBeEquipped.setBounds(191, 372, 517, 37);
		panel.add(itemsToBeEquipped);
		
		JLabel lblEquipItems = new JLabel("Equip Items");
		lblEquipItems.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblEquipItems.setBounds(265, 324, 106, 37);
		panel.add(lblEquipItems);
		
		JCheckBox useItems = new JCheckBox("Use these items' model ids for the custom equipment?");
		useItems.setBounds(68, 436, 328, 37);
		panel.add(useItems);
		
		JButton btnEquipItems = new JButton("Equip items");
		btnEquipItems.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(itemsToBeEquipped.getText().equals("") || !useItems.isSelected()) {
					Startup.sendMessage("There was an error processing your request.");
					return;
				}
				String[] items = itemsToBeEquipped.getText().split(", ");
				List<Integer> originalModelColorsToUse = new ArrayList<Integer>();
				List<Integer> modifiedModelColorsToUse = new ArrayList<Integer>();
				List<Integer> originalTextureColorsToUse = new ArrayList<Integer>();
				List<Integer> modifiedTextureColorsToUse = new ArrayList<Integer>();
				List<Integer> newModels = new ArrayList<Integer>();
				defs.clientScripts = null;
				defs.clientScripts = new HashMap<Integer, Object>();
				newModels.add(215); // Default models in: head models.
				newModels.add(246); // Default models to set: head models.
				for(int i = 0; i < items.length; i++) {
					ItemDefinition itemDef = new ItemDefinition(Cache.INSTANCE, Integer.parseInt(items[i]));
					if(itemDef.hasModifiedColors()) {
						if(Startup.sendConfirm("["+itemDef.getName()+" - "+itemDef.getId()+"] has modified model colors. Use these?")) {
							for(int j = 0; j < itemDef.originalModelColors.length; j++) {
								originalModelColorsToUse.add(itemDef.originalModelColors[j]);
								modifiedModelColorsToUse.add(itemDef.modifiedModelColors[j]);
							}
						}
					}
					if(itemDef.hasModifiedTextureColors()) {
						if(Startup.sendConfirm("["+itemDef.getName()+" - "+itemDef.getId()+"] has modified textures colors. Use these?")) {
							for(int j = 0; j < itemDef.originalTextures.length; j++) {
								originalTextureColorsToUse.add(itemDef.originalTextures[j]);
								modifiedTextureColorsToUse.add(itemDef.modifiedTextures[j]);
							}
						}
					}
					if(itemDef.maleEquipModelId1 > -1) {
						newModels.add(itemDef.maleEquipModelId1);
						if(itemDef.maleEquipModelId2 > -1)
							newModels.add(itemDef.maleEquipModelId2);
						if(itemDef.maleEquipModelId3 > -1)
							newModels.add(itemDef.maleEquipModelId3);
							
					}
				}
				
				defs.originalModelColors = null;
				defs.modifiedModelColors = null;
				defs.originalTextures = null;
				defs.modifiedTextures = null;
				defs.modelIds = null;
				
				for(int i = 0; i < items.length; i++)
					defs.clientScripts.put(i, Integer.parseInt(items[i]));
				defs.originalModelColors = new int[originalModelColorsToUse.size()];
				for(int i = 0; i < originalModelColorsToUse.size(); i++)
					defs.originalModelColors[i] = originalModelColorsToUse.get(i);
				defs.modifiedModelColors = new int[modifiedModelColorsToUse.size()];
				for(int i = 0; i < modifiedModelColorsToUse.size(); i++)
					defs.modifiedModelColors[i] = modifiedModelColorsToUse.get(i);
				
				defs.originalTextures = new int[originalTextureColorsToUse.size()];
				for(int i = 0; i < originalTextureColorsToUse.size(); i++)
					defs.originalTextures[i] = originalTextureColorsToUse.get(i);
				defs.modifiedTextures = new int[modifiedTextureColorsToUse.size()];
				for(int i = 0; i < modifiedTextureColorsToUse.size(); i++)
					defs.modifiedTextures[i] = modifiedTextureColorsToUse.get(i);
				
				defs.modelIds = new int[newModels.size()];
				for(int i = 0; i < newModels.size(); i++)
					defs.modelIds[i] = newModels.get(i);
				System.out.println(Arrays.toString(defs.modelIds));
				Startup.sendMessage("The items have been set.\nTheir models will now be the models of the npc.\nPress the save button to save the changes made, \notherwise your edits will be lost.");
			}
		});
		btnEquipItems.setBounds(421, 443, 89, 23);
		panel.add(btnEquipItems);
		
		JButton btnNewButton = new JButton("View equipped items");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(defs.clientScripts == null)
					return;
				if(defs.clientScripts.isEmpty()) {
					Startup.sendMessage("["+defs.getName()+" - "+defs.getId()+"] has no equipped items!");
					return;
				}
				StringBuilder message = new StringBuilder("");
				for(int i = 0; i < defs.clientScripts.size(); i++) {
					message.append("["+(ItemDefinition.get(Cache.INSTANCE, (Integer) defs.clientScripts.get(i)).getName())+" "
						+ "- "+defs.clientScripts.get(i)+"]\n");
				}
				Startup.sendMessage(message.toString());
			}
		});
		btnNewButton.setBounds(530, 443, 129, 23);
		panel.add(btnNewButton);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem saveNPCDefinitionsMenuItem = new JMenuItem("Save NPC Definitions");
		saveNPCDefinitionsMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});
		mnFile.add(saveNPCDefinitionsMenuItem);
		
		JMenuItem reloadNpcMenuItem = new JMenuItem("Reload NPC");
		reloadNpcMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				reload();
			}
		});
		mnFile.add(reloadNpcMenuItem);
		
		JMenuItem closeNpcMenuItem = new JMenuItem("Close");
		closeNpcMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				close();
			}
		});
		mnFile.add(closeNpcMenuItem);
		setVisible(true);
		KeyboardFocusManager.setCurrentKeyboardFocusManager(new DefaultKeyboardFocusManager());
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventPostProcessor(keyPostProcessor);
	}
	
	private KeyEventPostProcessor keyPostProcessor = new KeyEventPostProcessor() {

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

	/**
	 * saves the values.
	 */
	public void save() {
		defs.name = npcNameTextField.getText();
		defs.width = Integer.parseInt(npcWidthTextField.getText());
		defs.height = Integer.parseInt(npcHeightTextField.getText());
		defs.size = Integer.parseInt(npcSizeTextField.getText());
		defs.combatLevel = Integer.parseInt(combatLevelTextField.getText());
		defs.renderEmote = Integer.parseInt(renderAnimationTextField.getText());
		defs.respawnDirection = Byte.parseByte(respawnDirectionTextField.getText());
		defs.walkMask = Byte.parseByte(walkMaskTextField.getText());
		defs.setVisibleOnMap(visibleOnMap.isSelected() ? true : false);
		defs.headIcon = Integer.parseInt(headIcon.getText());
		defs.unknownInt6 = Integer.parseInt(cursor.getText());
		
		if(!modelIds.getText().equals("")) {
			String[] modelIdsAsText = modelIds.getText().split(", ");
			defs.modelIds = null;	
			defs.modelIds = new int[modelIdsAsText.length];
			for(int i = 0; i < modelIdsAsText.length; i++)
				defs.modelIds[i] = Integer.parseInt(modelIdsAsText[i]);
		}
		
		if (!chatHeads.getText().equals("")) {
			final String[] chatHeadIds = chatHeads.getText().split(", ");
			defs.chatHeads = null;
			defs.chatHeads = new int[chatHeadIds.length];
			for(int i = 0; i < chatHeadIds.length; i++)
				defs.chatHeads[i] = Integer.parseInt(chatHeadIds[i]);
		}
		
		final String[] options = this.rightClickOptions.getText().split(", ");
		for (int i = 0; i < defs.options.length; i++)
			defs.options[i] = (options[i].equals("null") ? null : options[i]);

		if(!modelColors.getText().equals("")) {
			defs.originalModelColors = null;
			defs.modifiedModelColors = null;
			String[] modelColorsAsText = modelColors.getText().split(", ");
			defs.originalModelColors = new int[modelColorsAsText.length];
			defs.modifiedModelColors = new int[modelColorsAsText.length];
			int index = 0;
			for(String splitModelColorsText : modelColorsAsText) {
				String[] successfulSplit = splitModelColorsText.split(" = ");
				index++;
				defs.originalModelColors[index - 1] = Integer.parseInt(successfulSplit[0]);
				defs.modifiedModelColors[index - 1] = Integer.parseInt(successfulSplit[1]);
			}
		} else {
			defs.originalModelColors = null;
			defs.modifiedModelColors = null;
		}
		
		if(!textures.getText().equals("")) {
			defs.originalTextures = null;
			defs.modifiedTextures = null;
			String[] textureColorsAsText = textures.getText().split(", ");
			defs.originalTextures = new int[textureColorsAsText.length];
			defs.modifiedTextures = new int[textureColorsAsText.length];
			int index = 0;
			for(String splitTextureColorsText : textureColorsAsText) {
				String[] successfulSplit = splitTextureColorsText.split(" = ");
				index++;
				defs.originalTextures[index - 1] = Integer.parseInt(successfulSplit[0]);
				defs.modifiedTextures[index - 1] = Integer.parseInt(successfulSplit[1]);
			}
		} else {
			defs.originalTextures = null;
			defs.modifiedTextures = null;
		}
		MainFrame.writeLog("NPC Definitions Editor", "Saved NPC: "+defs.id+" - "+defs.name);
		MainFrame.npcPanel.updateNPCListModel(defs);
		defs.changeDefinition(Cache.INSTANCE);
		printNPCDetails(defs);
	}
	
	public void printNPCDetails(NPCDefinition defs) {
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

	
	private void reload() {
		npcNameTextField.setText(defs.getName());
		npcWidthTextField.setText("" + defs.width);
		npcHeightTextField.setText("" + defs.height);
		npcSizeTextField.setText("" + defs.size);
		combatLevelTextField.setText("" + defs.combatLevel);
		renderAnimationTextField.setText("" + defs.renderEmote);
		respawnDirectionTextField.setText("" + defs.respawnDirection);
		walkMaskTextField.setText("" + defs.walkMask);
		headIcon.setText(defs.headIcon+"");
		cursor.setText(defs.unknownInt6+"");
		visibleOnMap.setSelected(defs.visibleOnMap);
		chatHeads.setText(chatHeads());
		modelColors.setText(modelColors());
		textures.setText(textureColors());
		rightClickOptions.setText(rightClickOptions());
		modelIds.setText(modelIds());
		MainFrame.writeLog("NPC Definitions Editor", "Reloaded NPC: "+defs.id+" - "+defs.name);
		printNPCDetails(defs);
	}
	
	public String chatHeads() {
		return defs.chatHeads != null ? 
			Arrays.toString(defs.chatHeads).replace("[", "").replace("]", "") : "";
	}
	
	public String modelColors() {
		String color = "";
		if(defs.originalModelColors != null)
			for(int i = 0; i < defs.originalModelColors.length; i++)
				color = new StringBuilder().append(color).append(defs.originalModelColors[i]).append(" = ").
					append(defs.modifiedModelColors[i]).append(i == defs.originalModelColors.length - 1 ? "" : ", ").toString();
		return color;
	}
	
	public String textureColors() {
		String color = "";
		if(defs.originalTextures != null)
			for(int i = 0; i < defs.originalTextures.length; i++)
				color = new StringBuilder().append(color).append(defs.originalTextures[i]).append(" = ").
					append(defs.modifiedTextures[i]).append(i == defs.originalTextures.length - 1 ? "" : ", ").toString();
		return color;
	}
	
	
	public String modelIds() {
		return defs.modelIds != null ? 
			Arrays.toString(defs.modelIds).replace("[", "").replace("]", "") : "";
	}
	
	public String rightClickOptions() {
		return defs.options != null ? 
			Arrays.toString(defs.options).replace("[", "").replace("]", "") : "null, null, null, null, null";
	}

	private void close() {
		printNPCDetails(defs);
		dispose();
	}
	
}
