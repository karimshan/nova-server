package org.nova.cache.ce.editors;

import java.awt.BorderLayout;
import java.awt.DefaultKeyboardFocusManager;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.KeyEventPostProcessor;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import org.nova.cache.Cache;
import org.nova.cache.ce.MainFrame;
import org.nova.cache.ce.Startup;
import org.nova.cache.definition.ItemDefinition;
import org.nova.cache.files.Index;
import org.nova.cache.utility.CacheConstants;
import org.nova.kshan.utilities.KeyUtils;

/**
 * This is the old GUI which was used to edit items.
 * 
 * @author K-Shan
 *
 */
public class ItemDefinitionEditor extends JFrame {
	/**
	 * @serialField
	 */
	private static final long serialVersionUID = -7003371661220473036L;

	private final ItemDefinition defs;

	private final JTextField csk1;
	private final JTextField csk2;
	private final JTextField csk3;
	private final JTextField csk4;
	private final JTextField csk5;
	private final JTextField csk6;
	private final JTextField csk7;
	private final JTextField csv1;
	private final JTextField csv2;
	private final JTextField csv3;
	private final JTextField csv4;
	private final JTextField csv5;
	private final JTextField csv6;
	private final JTextField csv7;
	private final JTextArea clientScriptTextArea;
	private final JTextField itemName;
	private final JTextField stackable;
	private final JTextField itemPrice;
	private final JTextField team;
	private final JTextField switchLendItemId;
	private final JTextField lentId;
	private final JTextField switchNoteItemId;
	private final JTextField notedItemId;
	private final JTextField groundOptions;
	private final JTextField inventoryOptions;
	private final JTextField modelColors;
	private final JTextField textureColors;
	private final JTextField inventoryModelId;
	private final JTextField inventoryModelZoom;
	private final JTextField modelRotations;
	private final JTextField modelOffsets;
	private final JTextField maleEquips;
	private final JTextField femaleEquips;
	private final JCheckBox membersOnlyToggle;
	private final JCheckBox unNotedToggle;
	//private JTextField invModelID;
	//private JTextField maleModelID;
	//private JCheckBox packCustomModelsToggle;
	//private JTextArea outputModelsTArea;
	public int inv, maleWorn, femaleWorn;
	public File invModel, maleWornModel, femaleWornModel;
	//private JTextField femaleModelID;
	private JTextField stackIdsTextField;
	private JTextField stackAmounts;
	private JTextField bonusesTF;

	/**
	 * Create the dialog.
	 */
	public ItemDefinitionEditor(final ItemDefinition defs) {
		super("Item Definitions Editor");
		setTitle("667+ Item Definition Editor; Currently Editing Definitions of Item: "+defs.id+" - "+defs.getName());
		this.defs = defs;
		setBounds(100, 100, 802, 560);	
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		
		final JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		final JTabbedPane tabbedBox = new JTabbedPane(SwingConstants.TOP);
		getContentPane().add(tabbedBox, BorderLayout.CENTER);

		final JPanel generalSettingsPanel = new JPanel();
		tabbedBox.addTab("General Settings", null, generalSettingsPanel, null);
		generalSettingsPanel.setLayout(null);

		final JLabel generalSettingsItemLabel = new JLabel(
				"Currently editing item definitions of item: " + defs.id
				+ " - " + defs.getName());
		generalSettingsItemLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		generalSettingsItemLabel.setBounds(144, 0, 612, 27);
		generalSettingsPanel.add(generalSettingsItemLabel);

		final JLabel lblItemName = new JLabel("Item Name:");
		lblItemName.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblItemName.setBounds(20, 40, 80, 27);
		generalSettingsPanel.add(lblItemName);

		itemName = new JTextField();
		itemName.setToolTipText("The current name of the name.");
		itemName.setBounds(115, 45, 203, 27);
		generalSettingsPanel.add(itemName);
		itemName.setColumns(10);
		itemName.setText("" + defs.getName());

		final JLabel lblStackable = new JLabel("Stackable:");
		lblStackable.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblStackable.setBounds(20, 78, 80, 27);
		generalSettingsPanel.add(lblStackable);

		stackable = new JTextField();
		stackable
		.setToolTipText("Represents if the item is stackable (1) or not.");
		stackable.setBounds(115, 83, 86, 20);
		generalSettingsPanel.add(stackable);
		stackable.setColumns(10);
		stackable.setText("" + defs.stackable);

		final JLabel lblPrice = new JLabel("Price:");
		lblPrice.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblPrice.setBounds(20, 116, 80, 27);
		generalSettingsPanel.add(lblPrice);

		itemPrice = new JTextField();
		itemPrice.setToolTipText("The price of the item.");
		itemPrice.setColumns(10);
		itemPrice.setBounds(115, 114, 160, 20);
		generalSettingsPanel.add(itemPrice);
		itemPrice.setText("" + defs.value);

		final JLabel lblTeamId = new JLabel("Team Id:");
		lblTeamId.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblTeamId.setBounds(20, 154, 80, 27);
		generalSettingsPanel.add(lblTeamId);

		team = new JTextField();
		team.setColumns(10);
		team.setBounds(115, 159, 86, 20);
		generalSettingsPanel.add(team);
		team.setText("" + defs.teamId);

		final JLabel lblSwitchLendItem = new JLabel("Switch Lend Item ID:");
		lblSwitchLendItem.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblSwitchLendItem.setBounds(20, 268, 135, 27);
		generalSettingsPanel.add(lblSwitchLendItem);

		final JLabel lblLentId = new JLabel("Lent ID:");
		lblLentId.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblLentId.setBounds(20, 306, 98, 27);
		generalSettingsPanel.add(lblLentId);

		switchLendItemId = new JTextField();
		switchLendItemId.setColumns(10);
		switchLendItemId.setBounds(163, 273, 86, 20);
		generalSettingsPanel.add(switchLendItemId);
		switchLendItemId.setText("" + defs.lentItemId);

		lentId = new JTextField();
		lentId.setToolTipText("The lent id of the item.");
		lentId.setColumns(10);
		lentId.setBounds(144, 306, 86, 20);
		generalSettingsPanel.add(lentId);
		lentId.setText("" + defs.notedModelId);

		membersOnlyToggle = new JCheckBox("Members Only?");
		membersOnlyToggle.setToolTipText("Check if the item is member's only.");
		membersOnlyToggle.setFont(new Font("Tahoma", Font.PLAIN, 35));
		membersOnlyToggle.setBounds(358, 45, 363, 73);
		generalSettingsPanel.add(membersOnlyToggle);
		membersOnlyToggle.setSelected(defs.membersOnly);

		unNotedToggle = new JCheckBox("Un-Noted?");
		unNotedToggle.setVerticalAlignment(SwingConstants.BOTTOM);
		unNotedToggle
		.setToolTipText("Check the box if the item is un-noted. Leave un-checked if its noted.");
		unNotedToggle.setFont(new Font("Tahoma", Font.PLAIN, 35));
		unNotedToggle.setBounds(358, 246, 223, 67);
		generalSettingsPanel.add(unNotedToggle);
		unNotedToggle.setSelected(defs.isUnnoted());

		final JLabel lblNoted = new JLabel("Switch Note Item ID:");
		lblNoted.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNoted.setBounds(20, 192, 128, 29);
		generalSettingsPanel.add(lblNoted);

		final JLabel lblNotedItemId = new JLabel("Noted Item ID:");
		lblNotedItemId.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNotedItemId.setBounds(20, 232, 128, 29);
		generalSettingsPanel.add(lblNotedItemId);

		switchNoteItemId = new JTextField();
		switchNoteItemId.setColumns(10);
		switchNoteItemId.setBounds(163, 198, 98, 20);
		generalSettingsPanel.add(switchNoteItemId);
		switchNoteItemId.setText("" + defs.notedItemId);

		notedItemId = new JTextField();
		notedItemId.setToolTipText("The noted id of the item.");
		notedItemId.setColumns(10);
		notedItemId.setBounds(163, 238, 104, 20);
		generalSettingsPanel.add(notedItemId);
		notedItemId.setText("" + defs.notedModelId);

		final JButton saveButton = new JButton("Save Item");
		saveButton
		.setToolTipText("Saves all of the values of the current item.");
		saveButton.setFont(new Font("Stencil", Font.PLAIN, 14));
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});
		saveButton.setBounds(358, 125, 140, 42);
		generalSettingsPanel.add(saveButton);

		final JButton reloadButton = new JButton("Reload");
		reloadButton
		.setToolTipText("Reloads the item (If you haven't saved, your work will be lost.)");
		reloadButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				reload();
			}
		});
		reloadButton.setFont(new Font("Stencil", Font.PLAIN, 14));
		reloadButton.setBounds(358, 192, 140, 42);
		generalSettingsPanel.add(reloadButton);

		final JButton closeButton = new JButton("Close");
		closeButton
		.setToolTipText("Closes out the box, but does NOT save the item.");
		closeButton.setFont(new Font("Stencil", Font.PLAIN, 14));
		closeButton.setBounds(539, 158, 140, 42);
		generalSettingsPanel.add(closeButton);

		JLabel lblStackIds = new JLabel("Stack IDs");
		lblStackIds.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblStackIds.setBounds(20, 344, 98, 27);
		generalSettingsPanel.add(lblStackIds);

		stackIdsTextField = new JTextField();
		stackIdsTextField.setToolTipText("The stack ids of this item");
		stackIdsTextField.setText(getStackIds());
		stackIdsTextField.setColumns(10);
		stackIdsTextField.setBounds(144, 349, 174, 20);
		generalSettingsPanel.add(stackIdsTextField);

		JLabel lblStackAmounts = new JLabel("Stack Amounts");
		lblStackAmounts.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblStackAmounts.setBounds(20, 382, 98, 27);
		generalSettingsPanel.add(lblStackAmounts);

		stackAmounts = new JTextField();
		stackAmounts.setToolTipText("The stack amounts");
		stackAmounts.setText(getStackAmounts());
		stackAmounts.setColumns(10);
		stackAmounts.setBounds(144, 387, 174, 20);
		generalSettingsPanel.add(stackAmounts);
		closeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		final JPanel itemOptionSettingsPanel = new JPanel();
		tabbedBox
		.addTab("Option Settings", null, itemOptionSettingsPanel, null);
		itemOptionSettingsPanel.setLayout(null);

		final JLabel optionSettingsItemLabel = new JLabel(
				"Currently editing item definitions of item: " + defs.id
				+ " - " + defs.getName());
		optionSettingsItemLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		optionSettingsItemLabel.setBounds(150, 0, 606, 27);
		itemOptionSettingsPanel.add(optionSettingsItemLabel);

		final JLabel lblGroundOptions = new JLabel("Ground Options:");
		lblGroundOptions.setFont(new Font("Tahoma", Font.ITALIC, 20));
		lblGroundOptions.setBounds(255, 25, 158, 41);
		itemOptionSettingsPanel.add(lblGroundOptions);

		groundOptions = new JTextField();
		groundOptions
		.setToolTipText("Displays the options when you hover over the item when its on the ground.");
		groundOptions.setText(getGroundOptions());
		groundOptions.setFont(new Font("Tahoma", Font.PLAIN, 20));
		groundOptions.setBounds(53, 63, 600, 41);
		itemOptionSettingsPanel.add(groundOptions);
		groundOptions.setColumns(10);

		final JLabel lblInventoryOptions = new JLabel("Inventory Options:");
		lblInventoryOptions.setFont(new Font("Tahoma", Font.ITALIC, 20));
		lblInventoryOptions.setBounds(255, 105, 176, 41);
		itemOptionSettingsPanel.add(lblInventoryOptions);

		inventoryOptions = new JTextField();
		inventoryOptions
		.setToolTipText("Options when item is right clicked in inventory.");
		inventoryOptions.setText(getInventoryOptions());
		inventoryOptions.setFont(new Font("Tahoma", Font.PLAIN, 20));
		inventoryOptions.setColumns(10);
		inventoryOptions.setBounds(53, 144, 600, 41);
		itemOptionSettingsPanel.add(inventoryOptions);

		final JLabel lblModelColors_1 = new JLabel("Model Colors:");
		lblModelColors_1.setFont(new Font("Tahoma", Font.ITALIC, 20));
		lblModelColors_1.setBounds(255, 191, 176, 41);
		itemOptionSettingsPanel.add(lblModelColors_1);

		modelColors = new JTextField();
		modelColors.setToolTipText("modified model colors");
		modelColors.setText(getChangedModelColors());
		modelColors.setFont(new Font("Tahoma", Font.PLAIN, 20));
		modelColors.setColumns(10);
		modelColors.setBounds(53, 232, 600, 41);
		itemOptionSettingsPanel.add(modelColors);

		final JLabel lblTextures = new JLabel("Textures:");
		lblTextures.setFont(new Font("Tahoma", Font.ITALIC, 20));
		lblTextures.setBounds(255, 271, 176, 41);
		itemOptionSettingsPanel.add(lblTextures);

		textureColors = new JTextField();
		textureColors.setToolTipText("Modified textures");
		textureColors.setText(getChangedTextureColors());
		textureColors.setFont(new Font("Tahoma", Font.PLAIN, 20));
		textureColors.setColumns(10);
		textureColors.setBounds(53, 311, 600, 41);
		itemOptionSettingsPanel.add(textureColors);
		
		JLabel lblBonuses = new JLabel("Bonuses");
		lblBonuses.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblBonuses.setBounds(255, 351, 122, 42);
		itemOptionSettingsPanel.add(lblBonuses);
		
		bonusesTF = new JTextField();
		bonusesTF.setToolTipText("The bonuses of the item");
		if(getBonuses(defs.getId()) != null)
			bonusesTF.setText(Arrays.toString(getBonuses(defs.getId())).replace("[", "").replace("]", ""));
		bonusesTF.setFont(new Font("Tahoma", Font.PLAIN, 20));
		bonusesTF.setColumns(10);
		bonusesTF.setBounds(53, 392, 600, 41);
		itemOptionSettingsPanel.add(bonusesTF);
		
		JButton btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				updateBonuses(defs.getId());
			}
		});
		btnUpdate.setBounds(667, 405, 89, 23);
		itemOptionSettingsPanel.add(btnUpdate);

		final JPanel itemModelSettingsPanel = new JPanel();
		tabbedBox.addTab("Model Settings", null, itemModelSettingsPanel, null);
		itemModelSettingsPanel.setLayout(null);

		final JLabel modelSettingsItemLabel = new JLabel(
				"Currently editing item definitions of item: " + defs.id
				+ " - " + defs.getName());
		modelSettingsItemLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		modelSettingsItemLabel.setBounds(146, 0, 610, 27);
		itemModelSettingsPanel.add(modelSettingsItemLabel);

		final JLabel lblInventoryItemModel = new JLabel(
				"Inventory Item Model ID:");
		lblInventoryItemModel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblInventoryItemModel.setBounds(10, 46, 235, 38);
		itemModelSettingsPanel.add(lblInventoryItemModel);

		inventoryModelId = new JTextField();
		inventoryModelId.setFont(new Font("Tahoma", Font.PLAIN, 20));
		inventoryModelId.setBounds(268, 49, 438, 33);
		itemModelSettingsPanel.add(inventoryModelId);
		inventoryModelId.setColumns(10);
		inventoryModelId.setText("" + defs.invModelId);

		final JLabel lblInventoryModelZoom = new JLabel("Inventory Model Zoom:");
		lblInventoryModelZoom.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblInventoryModelZoom.setBounds(10, 98, 215, 42);
		itemModelSettingsPanel.add(lblInventoryModelZoom);

		inventoryModelZoom = new JTextField();
		inventoryModelZoom.setFont(new Font("Tahoma", Font.PLAIN, 20));
		inventoryModelZoom.setColumns(10);
		inventoryModelZoom.setBounds(268, 103, 438, 33);
		itemModelSettingsPanel.add(inventoryModelZoom);
		inventoryModelZoom.setText("" + defs.invModelZoom);

		final JLabel lblModelRotations = new JLabel("Model Rotations (1:2):");
		lblModelRotations.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblModelRotations.setBounds(10, 151, 203, 42);
		itemModelSettingsPanel.add(lblModelRotations);

		final JLabel lblModelOffsets = new JLabel("Model Offsets (1:2):");
		lblModelOffsets.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblModelOffsets.setBounds(10, 204, 184, 42);
		itemModelSettingsPanel.add(lblModelOffsets);

		final JLabel lblMaleEquipIds = new JLabel("Male Equip Ids (1:2:3):");
		lblMaleEquipIds.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblMaleEquipIds.setBounds(10, 257, 215, 42);
		itemModelSettingsPanel.add(lblMaleEquipIds);

		final JLabel lblFemaleEquipIds = new JLabel("Female Equip Ids (1:2:3):");
		lblFemaleEquipIds.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblFemaleEquipIds.setBounds(10, 310, 235, 42);
		itemModelSettingsPanel.add(lblFemaleEquipIds);

		modelRotations = new JTextField();
		modelRotations.setFont(new Font("Tahoma", Font.PLAIN, 20));
		modelRotations.setColumns(10);
		modelRotations.setBounds(268, 156, 438, 33);
		itemModelSettingsPanel.add(modelRotations);
		modelRotations.setText(getModelRotations());

		modelOffsets = new JTextField();
		modelOffsets.setFont(new Font("Tahoma", Font.PLAIN, 20));
		modelOffsets.setColumns(10);
		modelOffsets.setBounds(268, 209, 438, 33);
		itemModelSettingsPanel.add(modelOffsets);
		modelOffsets.setText(getModelOffsets());

		maleEquips = new JTextField();
		maleEquips.setFont(new Font("Tahoma", Font.PLAIN, 20));
		maleEquips.setColumns(10);
		maleEquips.setBounds(268, 262, 438, 33);
		itemModelSettingsPanel.add(maleEquips);
		maleEquips.setText(getMaleEquipIds());

		femaleEquips = new JTextField();
		femaleEquips.setFont(new Font("Tahoma", Font.PLAIN, 20));
		femaleEquips.setColumns(10);
		femaleEquips.setBounds(268, 315, 438, 33);
		itemModelSettingsPanel.add(femaleEquips);
		femaleEquips.setText(getFemaleEquipIds());
		
		JButton dumpModelsBtn = new JButton("Dump Models");
		dumpModelsBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dumpModels();
			}
		});
		dumpModelsBtn.setFont(new Font("Tahoma", Font.PLAIN, 14));
		dumpModelsBtn.setBounds(305, 367, 171, 41);
		itemModelSettingsPanel.add(dumpModelsBtn);

		final JPanel clientScriptPanel = new JPanel();
		tabbedBox
		.addTab("ClientScript Settings", null, clientScriptPanel, null);
		clientScriptPanel.setLayout(null);

		clientScriptTextArea = new JTextArea();
		clientScriptTextArea.setText(getClientScriptData());
		clientScriptTextArea.setColumns(5);
		clientScriptTextArea.setBounds(24, 71, 274, 296);
		clientScriptPanel.add(clientScriptTextArea);

		final JLabel currentClientScriptValuesLabel = new JLabel(
				"Current ClientScript Values");
		currentClientScriptValuesLabel.setFont(new Font("Tahoma", Font.PLAIN,
				15));
		currentClientScriptValuesLabel.setBounds(64, 38, 195, 22);
		clientScriptPanel.add(currentClientScriptValuesLabel);

		final JLabel csItemLabel = new JLabel(
				"Currently editing item definitions of item: " + defs.id
				+ " - " + defs.getName());
		csItemLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		csItemLabel.setBounds(139, 0, 617, 27);
		clientScriptPanel.add(csItemLabel);

		final JLabel keysLabel = new JLabel("Modified Keys:");
		keysLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		keysLabel.setBounds(371, 26, 104, 27);
		clientScriptPanel.add(keysLabel);

		csk2 = new JTextField();
		csk2.setBounds(372, 109, 86, 20);
		clientScriptPanel.add(csk2);
		csk2.setColumns(10);

		csk3 = new JTextField();
		csk3.setBounds(372, 152, 86, 20);
		clientScriptPanel.add(csk3);
		csk3.setColumns(10);

		csk4 = new JTextField();
		csk4.setBounds(372, 200, 86, 20);
		clientScriptPanel.add(csk4);
		csk4.setColumns(10);

		csk5 = new JTextField();
		csk5.setBounds(372, 243, 86, 20);
		clientScriptPanel.add(csk5);
		csk5.setColumns(10);

		csk6 = new JTextField();
		csk6.setBounds(372, 288, 86, 20);
		clientScriptPanel.add(csk6);
		csk6.setColumns(10);

		csk7 = new JTextField();
		csk7.setBounds(372, 338, 86, 20);
		clientScriptPanel.add(csk7);
		csk7.setColumns(10);

		csk1 = new JTextField();
		csk1.setColumns(10);
		csk1.setBounds(372, 64, 86, 20);
		clientScriptPanel.add(csk1);

		final JLabel label = new JLabel("1).");
		label.setBounds(348, 67, 14, 14);
		clientScriptPanel.add(label);

		final JLabel label_1 = new JLabel("2).");
		label_1.setBounds(348, 112, 14, 14);
		clientScriptPanel.add(label_1);

		final JLabel label_2 = new JLabel("3).");
		label_2.setBounds(348, 155, 14, 14);
		clientScriptPanel.add(label_2);

		final JLabel label_3 = new JLabel("4).");
		label_3.setBounds(348, 203, 14, 14);
		clientScriptPanel.add(label_3);

		final JLabel label_4 = new JLabel("5).");
		label_4.setBounds(348, 246, 14, 14);
		clientScriptPanel.add(label_4);

		final JLabel label_5 = new JLabel("6).");
		label_5.setBounds(348, 291, 14, 14);
		clientScriptPanel.add(label_5);

		final JLabel label_6 = new JLabel("7).");
		label_6.setBounds(348, 344, 14, 14);
		clientScriptPanel.add(label_6);

		final JLabel lblModifiedValues = new JLabel("Modified Values:");
		lblModifiedValues.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblModifiedValues.setBounds(589, 26, 104, 27);
		clientScriptPanel.add(lblModifiedValues);

		csv1 = new JTextField();
		csv1.setColumns(10);
		csv1.setBounds(589, 64, 86, 20);
		clientScriptPanel.add(csv1);

		csv2 = new JTextField();
		csv2.setColumns(10);
		csv2.setBounds(589, 109, 86, 20);
		clientScriptPanel.add(csv2);

		csv3 = new JTextField();
		csv3.setColumns(10);
		csv3.setBounds(589, 152, 86, 20);
		clientScriptPanel.add(csv3);

		csv4 = new JTextField();
		csv4.setColumns(10);
		csv4.setBounds(589, 200, 86, 20);
		clientScriptPanel.add(csv4);

		csv5 = new JTextField();
		csv5.setColumns(10);
		csv5.setBounds(589, 243, 86, 20);
		clientScriptPanel.add(csv5);

		csv6 = new JTextField();
		csv6.setColumns(10);
		csv6.setBounds(589, 288, 86, 20);
		clientScriptPanel.add(csv6);

		csv7 = new JTextField();
		csv7.setColumns(10);
		csv7.setBounds(589, 338, 86, 20);
		clientScriptPanel.add(csv7);

		final JLabel label_7 = new JLabel("1).");
		label_7.setBounds(565, 67, 14, 14);
		clientScriptPanel.add(label_7);

		final JLabel label_8 = new JLabel("2).");
		label_8.setBounds(565, 112, 14, 14);
		clientScriptPanel.add(label_8);

		final JLabel label_9 = new JLabel("3).");
		label_9.setBounds(565, 155, 14, 14);
		clientScriptPanel.add(label_9);

		final JLabel label_10 = new JLabel("4).");
		label_10.setBounds(565, 203, 14, 14);
		clientScriptPanel.add(label_10);

		final JLabel label_11 = new JLabel("5).");
		label_11.setBounds(565, 246, 14, 14);
		clientScriptPanel.add(label_11);

		final JLabel label_12 = new JLabel("6).");
		label_12.setBounds(565, 291, 14, 14);
		clientScriptPanel.add(label_12);

		final JLabel label_13 = new JLabel("7).");
		label_13.setBounds(565, 341, 14, 14);
		clientScriptPanel.add(label_13);
		
		JButton clearClientscripts = new JButton("Clear ClientScripts");
		clearClientscripts.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				clearClientScripts();
			}
		});
		clearClientscripts.setFont(new Font("Tahoma", Font.PLAIN, 14));
		clearClientscripts.setBounds(445, 383, 154, 25);
		clientScriptPanel.add(clearClientscripts);

		final JPanel clientScriptValuesPanel = new JPanel();
		tabbedBox.addTab("ClientScript Values", null, clientScriptValuesPanel,
				null);
		clientScriptValuesPanel.setLayout(null);

		final JLabel csItemLabel2 = new JLabel(
				"Currently editing item definitions of item: " + defs.id
				+ " - " + defs.getName());
		csItemLabel2.setFont(new Font("Tahoma", Font.PLAIN, 16));
		csItemLabel2.setBounds(134, 0, 622, 27);
		clientScriptValuesPanel.add(csItemLabel2);

		final JScrollPane clientScriptAttackValueScrollPane = new JScrollPane();
		clientScriptAttackValueScrollPane.setBounds(10, 37, 362, 354);
		clientScriptValuesPanel.add(clientScriptAttackValueScrollPane);

		final JTextArea clientScriptAttackValuesTextArea = new JTextArea();
		clientScriptAttackValueScrollPane
		.setViewportView(clientScriptAttackValuesTextArea);
		clientScriptAttackValuesTextArea.setEditable(false);
		clientScriptAttackValuesTextArea
		.setText("ClientScript Attack values\r\n\r\nValue - Attack Style\r\n0 - Unarmed\r\n1 - Staff/Wand\r\n2 - Battleaxe/Hatchet\r\n3 - Spear - no combined attack\r\n4 - Pickaxe\r\n5 - Dagger/Rapier\r\n6 - Scimitar/Longsword/Teasing Stick\r\n7 - 2h/Godsword\r\n8 - Mace/Anchor\r\n9 - Claws\r\n10 - Warhammer/Maul\r\n11 - Abyssal Whip\r\n12 - Fun Weapons\r\n13 - Darts/Knives\r\n14 - Spear/Hasta/Noose Wand\r\n15 - Halberd\r\n16 - Shortbox/Longbow/Comp Bow\r\n17 - Crossbow\r\n18 - Dart/Knife/Thrownaxe/Javelin/Snowball\r\n19 - Chinchompa\r\n20 - Fixed Device\r\n21 - Salamander\r\n22 - Scythe\r\n23 - Spear\r\n24 - Sling,Chuck,Lob\r\n25 - Jab, Swipe, Fend");
		clientScriptAttackValuesTextArea.setRows(25);
		clientScriptAttackValuesTextArea.setColumns(25);

		final JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(393, 38, 363, 353);
		clientScriptValuesPanel.add(scrollPane);

		final JTextArea clientScriptValuesTextArea = new JTextArea();
		scrollPane.setViewportView(clientScriptValuesTextArea);
		clientScriptValuesTextArea
		.setText("Key, Object - Example\r\n\r\n0, Integer - Stab attack bonus\r\n1, Integer - Slash attack bonus\r\n2, Integer - Crush attack bonus\r\n3, Integer - Magic attack bonus\r\n4, Integer - Range attack bonus\r\n5, Integer - Stab defense bonus\r\n6, Integer - Slash defense bonus\r\n7, Integer - Crush defense bonus\r\n8, Integer - Magic defense bonus\r\n9, Integer - Range defense bonus\r\n\r\n11, Integer - Prayer bonus\r\n\r\n14, Integer - Attack speed\r\n\r\n23, Integer - Ranged level requirement ? ( 20<- /40)\r\n\r\n277, Integer - Maxed skill requirement\r\n\r\n417, Integer - Summoning defense bonus\r\n\r\n528, String - Equipment tab Option (ActionButtons2)\r\n529, String - Equipment tab Option (ActionButtons3)\r\n530, String - Equipment tab Option (ActionButtons4)\r\n531, String - Equipment tab Option (ActionButtons5)\r\n\r\n641, Integer - Melee Strength bonus\r\n642, Integer - Magic Strength bonus\r\n643, Integer - Ranged Strength bonus\r\n644, Integer - Render Animation Id\r\n\r\n685, Integer - Magic damage\r\n686, Integer - Attack Styles Tab\r\n687, Integer - Special bar (1)\r\n\r\n749, Integer - Skill Id Requirment\r\n750, Integer - Skill Level Requirment\r\n751, Integer - Skill Id Requirment\r\n752, Integer - Skill Level Requirment\r\n753, Integer - Skill Id Requirment\r\n754, Integer - Skill Level Requirment\r\n755, Integer - Skill Id Requirment\r\n756, Integer - Skill Level Requirment\r\n757,  Integer - Skill Id Requirment\r\n758,  Integer - Skill Level Requirment\r\n\r\n861, Integer - Quest Id Requirement\r\n\r\n967, Integer - Absorb melee bonus\r\n968, Integer - Absorb range bonus\r\n969, Integer - Absorb magic bonus");
		clientScriptValuesTextArea.setLineWrap(true);

		final JPanel otherValues = new JPanel();
		tabbedBox.addTab("Other Values", null, otherValues, null);
		otherValues.setLayout(null);

		final JLabel otherValuesItemLabel = new JLabel(
				"Currently editing item definitions of item: " + defs.id
				+ " - " + defs.getName());
		otherValuesItemLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		otherValuesItemLabel.setBounds(154, 0, 602, 27);
		otherValues.add(otherValuesItemLabel);

		final JLabel lblItemEquipTypes = new JLabel("Item Equip Types Ids");
		lblItemEquipTypes.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblItemEquipTypes.setBounds(40, 47, 171, 27);
		otherValues.add(lblItemEquipTypes);

		final JTextArea itemEquipTypesTextArea = new JTextArea();
		itemEquipTypesTextArea.setLineWrap(true);
		itemEquipTypesTextArea
		.setText("ID - Type of wear\r\n\r\n0 - As-is\r\n5 - 2 Handed Weapons\r\n6 - Full Torso Wear\r\n8 - Head Wear\r\n11 - Face Wear");
		itemEquipTypesTextArea.setBounds(28, 85, 207, 154);
		otherValues.add(itemEquipTypesTextArea);

		final JLabel lblModelColors = new JLabel("Model Colors");
		lblModelColors.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblModelColors.setBounds(438, 38, 104, 27);
		otherValues.add(lblModelColors);

		final JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(245, 63, 511, 328);
		otherValues.add(scrollPane_1);

		final JTextArea txtrItemModelColors = new JTextArea();
		scrollPane_1.setViewportView(txtrItemModelColors);
		txtrItemModelColors
		.setText("Item Model Colors:\r\n\r\nYellow: 8128\r\nWhite: 127\r\nPurple: 51136\r\nLime: 17350\r\nDark Red (Or Dragon): 926\r\nLava: 6073\r\n491770 = White With Tint Of Blue\r\n479770 = Camo Green\r\n476770 = Icing Green\r\n470770 = Neon Yellow With A Hint Of Lime Green\r\n461770 = Deep Orange\r\n428770 = Another Blue\r\n422770 = Yet Another Nice Green\r\n419770 = Nice Green\r\n380770 = Pink\r\n374770 = Shiny Purple\r\n359770 = Blue\r\n356770 = Dark Green\r\n353770 = White Green\r\n350770 = Forest Green\r\n347770 = Neon Green\r\n338770 = Another Nice Yellow\r\n332770 = Nice Orange Color\r\n311770 = Light Purple\r\n305770 = Crayon Blue\r\n302770 = Dark Blue\r\n296770 = Blue Darker Than Cyan By Abit\r\n290770 = Bluish Green\r\n266770 = Bronze\r\n257770 = Bright Pink\r\n246770 = Pinky Purple\r\n226770 = Aqua\r\n210770 = Lime Green\r\n34770 = Cyan\r\n76770 = Nice Yellow\r\n87770 = Nice Green\r\n92770 = Neon Greenish Color\r\n130770 = Pinky Red\r\n129770 = Chewing Gum Pink\r\n123770 = Pink\r\n933 = Red");
		txtrItemModelColors.setLineWrap(true);

	/*	JPanel customModels = new JPanel();
//		tabbedBox.addTab("Model Packing", null, customModels, null);
//		customModels.setLayout(null); // Only removed because I added a better way of adding new models.

		JLabel lblInvModelId = new JLabel("Inv. Model ID:");
		lblInvModelId.setBounds(34, 26, 89, 14);
		customModels.add(lblInvModelId);

		invModelID = new JTextField();
		invModelID.setEditable(false);
		invModelID.setBounds(128, 23, 195, 20);
		customModels.add(invModelID);
		invModelID.setColumns(10);

		JLabel lblWornModelId = new JLabel("Male Model ID:");
		lblWornModelId.setBounds(34, 60, 89, 14);
		customModels.add(lblWornModelId);

		maleModelID = new JTextField();
		maleModelID.setEditable(false);
		maleModelID.setColumns(10);
		maleModelID.setBounds(128, 54, 195, 20);
		customModels.add(maleModelID);

		final JButton invModelChooseButton = new JButton("Choose File");
		invModelChooseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int val = fc.showOpenDialog(invModelChooseButton);
				JFrame f = new JFrame();
				int kk = JOptionPane.showConfirmDialog(f, "Would you like to specify the Model ID?", 
						"Specify Model", JOptionPane.WARNING_MESSAGE);
				if(kk == 0) {
					int output = getNewModelId();
					if(fc.getSelectedFile().getPath().endsWith(".dat") && val == 0) {
						invModel = fc.getSelectedFile();
						invModelID.setText(""+invModel.getPath());
						try {
							inv = MainFrame.packCustomModel(MainFrame.STORE, MainFrame.getBytesFromFile(invModel), output);
						} catch (IOException e) {
							e.printStackTrace();
						}
						outputModelsTArea.append("Current Inv. Model ID of: "+defs.id+" - "+defs.invModelId+"\n");
						outputModelsTArea.append("If selected, New Inv. Model ID of: "+defs.id+" - "+inv+"\n");
					} else {
						JFrame frame = new JFrame();
						JOptionPane.showMessageDialog(frame, "The model selected is invalid.", "Model Selection", JOptionPane.WARNING_MESSAGE);
					}
				} else {
					if(fc.getSelectedFile().getPath().endsWith(".dat") && val == 0) {
						invModel = fc.getSelectedFile();
						invModelID.setText(""+invModel.getPath());
						try {
							inv = MainFrame.packCustomModel(MainFrame.STORE, MainFrame.getBytesFromFile(invModel));
						} catch (IOException e) {
							e.printStackTrace();
						}
						outputModelsTArea.append("Current Inv. Model ID of: "+defs.id+" - "+defs.invModelId+"\n");
						outputModelsTArea.append("If selected, New Inv. Model ID of: "+defs.id+" - "+inv+"\n");
					} else {
						JFrame frame = new JFrame();
						JOptionPane.showMessageDialog(frame, "The model selected is invalid.", "Model Selection", JOptionPane.WARNING_MESSAGE);
					}
				}
			}
		});
		invModelChooseButton.setBounds(343, 20, 89, 23);
		customModels.add(invModelChooseButton);

		outputModelsTArea = new JTextArea();
		outputModelsTArea.setEditable(false);
		outputModelsTArea.setBounds(34, 154, 378, 130);
		customModels.add(outputModelsTArea);

		JButton maleModelButton = new JButton("Choose File");
		maleModelButton.setBounds(343, 56, 89, 23);
		customModels.add(maleModelButton);
		maleModelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int val = fc.showOpenDialog(invModelChooseButton);
				JFrame f = new JFrame();
				int kk = JOptionPane.showConfirmDialog(f, "Would you like to specify the Model ID?", 
						"Specify Model", JOptionPane.WARNING_MESSAGE);
				if(kk == 0) {
					int output = getNewModelId();
					if(fc.getSelectedFile().getPath().endsWith(".dat") && val == 0) {
						maleWornModel = fc.getSelectedFile();
						maleModelID.setText(""+maleWornModel.getPath());
						try {
							maleWorn = MainFrame.packCustomModel(MainFrame.STORE, MainFrame.getBytesFromFile(maleWornModel), output);
						} catch (IOException e) {
							e.printStackTrace();
						}
						outputModelsTArea.append("Current Worn Model ID of: "+defs.id+" - "+defs.maleEquipModelId1+"\n");
						outputModelsTArea.append("If Selected, New Worn Model ID of: "+defs.id+" - "+maleWorn+"\n");
					} else {
						JFrame frame = new JFrame();
						JOptionPane.showMessageDialog(frame, "The model selected is invalid.", "Model Selection", JOptionPane.WARNING_MESSAGE);
					}

				} else {
					if(fc.getSelectedFile().getPath().endsWith(".dat") && val == 0) {
						maleWornModel = fc.getSelectedFile();
						maleModelID.setText(""+maleWornModel.getPath());
						try {
							maleWorn = MainFrame.packCustomModel(MainFrame.STORE, MainFrame.getBytesFromFile(maleWornModel));
						} catch (IOException e) {
							e.printStackTrace();
						}
						outputModelsTArea.append("Current Worn Model ID of: "+defs.id+" - "+defs.maleEquipModelId1+"\n");
						outputModelsTArea.append("If selected, New Worn Model ID of: "+defs.id+" - "+maleWorn+"\n");
					} else {
						JFrame frame = new JFrame();
						JOptionPane.showMessageDialog(frame, "The model selected is invalid.", "Model Selection", JOptionPane.WARNING_MESSAGE);
					}
				}
			}
		});

		packCustomModelsToggle = new JCheckBox("Use these models?");
		packCustomModelsToggle.setBounds(524, 71, 159, 23);
		customModels.add(packCustomModelsToggle);

		JLabel lblFemaleModelId = new JLabel("Female Model ID:");
		lblFemaleModelId.setBounds(34, 97, 89, 14);
		customModels.add(lblFemaleModelId);

		femaleModelID = new JTextField();
		femaleModelID.setEditable(false);
		femaleModelID.setColumns(10);
		femaleModelID.setBounds(128, 94, 195, 20);
		customModels.add(femaleModelID);

		JButton femaleModelButton = new JButton("Choose File");
		femaleModelButton.setBounds(343, 93, 89, 23);
		customModels.add(femaleModelButton);
		femaleModelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int val = fc.showOpenDialog(invModelChooseButton);
				JFrame f = new JFrame();
				int kk = JOptionPane.showConfirmDialog(f, "Would you like to specify the Model ID?", 
						"Specify Model", JOptionPane.WARNING_MESSAGE);
				if(kk == 0) {
					int output = getNewModelId();
					if(fc.getSelectedFile().getPath().endsWith(".dat") && val == 0) {
						femaleWornModel = fc.getSelectedFile();
						maleModelID.setText(""+femaleWornModel.getPath());
						try {
							femaleWorn = MainFrame.packCustomModel(MainFrame.STORE, MainFrame.getBytesFromFile(femaleWornModel), output);
						} catch (IOException e) {
							e.printStackTrace();
						}
						outputModelsTArea.append("Current Worn Model ID of: "+defs.id+" - "+defs.maleEquipModelId1+"\n");
						outputModelsTArea.append("If selected, New Worn Model ID of: "+defs.id+" - "+femaleWorn+"\n");
					} else {
						JFrame frame = new JFrame();
						JOptionPane.showMessageDialog(frame, "The model selected is invalid.", "Model Selection", JOptionPane.WARNING_MESSAGE);
					}

				} else {
					if(fc.getSelectedFile().getPath().endsWith(".dat") && val == 0) {
						femaleWornModel = fc.getSelectedFile();
						maleModelID.setText(""+femaleWornModel.getPath());
						try {
							femaleWorn = MainFrame.packCustomModel(MainFrame.STORE, MainFrame.getBytesFromFile(femaleWornModel));
						} catch (IOException e) {
							e.printStackTrace();
						}
						outputModelsTArea.append("Current Worn Model ID of: "+defs.id+" - "+defs.maleEquipModelId1+"\n");
						outputModelsTArea.append("If selected, New Worn Model ID of: "+defs.id+" - "+femaleWorn+"\n");
					} else {
						JFrame frame = new JFrame();
						JOptionPane.showMessageDialog(frame, "The model selected is invalid.", "Model Selection", JOptionPane.WARNING_MESSAGE);
					}
				}
			}
		});*/

		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		final JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		final JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		final JMenuItem mntmSave = new JMenuItem("Save");
		mntmSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				save();
			}
		});
		mnFile.add(mntmSave);

		final JMenuItem mntmReloadItem = new JMenuItem("Reload Item");
		mntmReloadItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				reload();
			}
		});
		mnFile.add(mntmReloadItem);
		
		
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

	@SuppressWarnings("unlikely-arg-type")
	public void save() {
		if (!this.stackIdsTextField.getText().equals(""))
		{
			String[] stackIds = this.stackIdsTextField.getText().split(";");
			for (int i = 0; i < this.defs.getStackIds().length; i++) {
				this.defs.getStackIds()[i] = Integer.parseInt(stackIds[i]);
			}
		}
		if (!this.stackAmounts.getText().equals(""))
		{
			String[] stackAmounts = this.stackAmounts.getText().split(";");
			for (int i = 0; i < this.defs.getStackAmounts().length; i++) {
				this.defs.getStackAmounts()[i] = Integer.parseInt(stackAmounts[i]);
			}
		}
		if ((!this.csk1.getText().equals(""))
				&& (!this.csv1.getText().equals(""))) {
			try {
				this.defs.clientScriptData.remove(this.csk1);
				this.defs.clientScriptData.put(
						Integer.valueOf(Integer.parseInt(this.csk1.getText())),
						Integer.valueOf(Integer.parseInt(this.csv1.getText())));
			} catch (final Exception e) {
				this.defs.clientScriptData.remove(this.csk1);
				this.defs.clientScriptData.put(
						Integer.valueOf(Integer.parseInt(this.csk1.getText())), this.csv1.getText().toString());
			}
		}
		if ((!this.csk2.getText().equals(""))
				&& (!this.csv2.getText().equals(""))) {
			try {
				this.defs.clientScriptData.remove(this.csk2);
				this.defs.clientScriptData.put(
						Integer.valueOf(Integer.parseInt(this.csk2.getText())),
						Integer.valueOf(Integer.parseInt(this.csv2.getText())));
			} catch (final Exception e) {
				this.defs.clientScriptData.remove(this.csk2);
				this.defs.clientScriptData.put(
						Integer.valueOf(Integer.parseInt(this.csk2.getText())),
						this.csv2.getText().toString());
			}
		}
		if ((!this.csk3.getText().equals(""))
				&& (!this.csv3.getText().equals(""))) {
			try {
				this.defs.clientScriptData.remove(this.csk3);
				this.defs.clientScriptData.put(
						Integer.valueOf(Integer.parseInt(this.csk3.getText())),
						Integer.valueOf(Integer.parseInt(this.csv3.getText())));
			} catch (final Exception e) {
				this.defs.clientScriptData.remove(this.csk3);
				this.defs.clientScriptData.put(
						Integer.valueOf(Integer.parseInt(this.csk3.getText())),
						this.csv3.getText().toString());
			}
		}
		if ((!this.csk4.getText().equals(""))
				&& (!this.csv4.getText().equals(""))) {
			try {
				this.defs.clientScriptData.remove(this.csk4);
				this.defs.clientScriptData.put(
						Integer.valueOf(Integer.parseInt(this.csk4.getText())),
						Integer.valueOf(Integer.parseInt(this.csv4.getText())));
			} catch (final Exception e) {
				this.defs.clientScriptData.remove(this.csk4);
				this.defs.clientScriptData.put(
						Integer.valueOf(Integer.parseInt(this.csk4.getText())),
						this.csv4.getText().toString());
			}
		}
		if ((!this.csk5.getText().equals(""))
				&& (!this.csv5.getText().equals(""))) {
			try {
				this.defs.clientScriptData.remove(this.csk5);
				this.defs.clientScriptData.put(
						Integer.valueOf(Integer.parseInt(this.csk5.getText())),
						Integer.valueOf(Integer.parseInt(this.csv5.getText())));
			} catch (final Exception e) {
				this.defs.clientScriptData.remove(this.csk5);
				this.defs.clientScriptData.put(
						Integer.valueOf(Integer.parseInt(this.csk5.getText())),
						this.csv5.getText().toString());
			}
		}
		if ((!this.csk6.getText().equals(""))
				&& (!this.csv6.getText().equals(""))) {
			try {
				this.defs.clientScriptData.remove(this.csk6);
				this.defs.clientScriptData.put(
						Integer.valueOf(Integer.parseInt(this.csk6.getText())),
						Integer.valueOf(Integer.parseInt(this.csv6.getText())));
			} catch (final Exception e) {
				this.defs.clientScriptData.remove(this.csk6);
				this.defs.clientScriptData.put(
						Integer.valueOf(Integer.parseInt(this.csk6.getText())),
						this.csv6.getText().toString());
			}
		}
		if ((!this.csk7.getText().equals(""))
				&& (!this.csv7.getText().equals("")))
			try {
				this.defs.clientScriptData.remove(this.csk7);
				this.defs.clientScriptData.put(
						Integer.valueOf(Integer.parseInt(this.csk7.getText())),
						Integer.valueOf(Integer.parseInt(this.csv7.getText())));
			} catch (final Exception e) {
				this.defs.clientScriptData.remove(this.csk7);
				this.defs.clientScriptData.put(
						Integer.valueOf(Integer.parseInt(this.csk7.getText())),
						this.csv7.getText().toString());
			}
		final String[] groundOption = groundOptions.getText().split(";");
		for (int i = 0; i < defs.getGroundOptions().length; i++)
			defs.getGroundOptions()[i] = groundOption[i].equals("null") ? null
					: groundOption[i];

		final String[] invOptions = inventoryOptions.getText().split(";");
		for (int i = 0; i < defs.getInventoryOptions().length; i++)
			defs.getInventoryOptions()[i] = invOptions[i].equals("null") ? null
					: invOptions[i];
		defs.resetModelColors();
		if (!modelColors.getText().equals("")) {
			final String[] splitedModelColorsTexts = modelColors.getText()
					.split(";");
			for (final String t : splitedModelColorsTexts) {
				final String[] editedColor = t.split("=");
				defs.changeModelColor(Integer.valueOf(editedColor[0]),
						Integer.valueOf(editedColor[1]));
			}
		}
		defs.resetTextures();
		if (!textureColors.getText().equals("")) {
			final String[] splitedTextureColorsTexts = textureColors.getText()
					.split(";");
			for (final String t : splitedTextureColorsTexts) {
				final String[] editedColor = t.split("=");
				defs.changeModelTexture(Integer.valueOf(editedColor[0]),
						Integer.valueOf(editedColor[1]));
			}
		}
		final String[] maleEquip = maleEquips.getText().split(";");
//		if(packCustomModelsToggle.isSelected() && maleWornModel != null)
//			defs.maleEquipModelId1 = maleWorn;
//		else
			defs.maleEquipModelId1 = Integer.parseInt(maleEquip[0]);
		defs.maleEquipModelId2 = Integer.parseInt(maleEquip[1]);
		defs.maleEquipModelId3 = Integer.parseInt(maleEquip[2]);
		final String[] femaleEquip = femaleEquips.getText().split(";");
//		if(packCustomModelsToggle.isSelected() && femaleWornModel != null)
//			defs.femaleEquipModelId1 = femaleWorn;
//		else
			defs.femaleEquipModelId1 = Integer.parseInt(femaleEquip[0]);
		defs.femaleEquipModelId2 = Integer.parseInt(femaleEquip[1]);
		defs.femaleEquipModelId3 = Integer.parseInt(femaleEquip[2]);
//		if(packCustomModelsToggle.isSelected() && invModel != null)
//			defs.invModelId = inv;
//		else
			defs.invModelId = Integer.valueOf(this.inventoryModelId.getText()).intValue();

		final String[] modelOffset = modelOffsets.getText().split(";");
		defs.modelOffsetX = Integer.parseInt(modelOffset[0]);
		defs.modelOffsetY = Integer.parseInt(modelOffset[1]);

		final String[] modelRotation = modelRotations.getText().split(";");
		defs.modelRotationX = Integer.parseInt(modelRotation[0]);
		defs.modelRotationY = Integer.parseInt(modelRotation[1]);

		defs.invModelZoom = Integer.valueOf(this.inventoryModelZoom.getText())
				.intValue();

		defs.notedModelId = Integer.valueOf(notedItemId.getText()).intValue();
		defs.notedItemId = Integer.valueOf(switchNoteItemId.getText())
				.intValue();
		defs.notedModelId = Integer.valueOf(lentId.getText()).intValue();
		defs.lentItemId = Integer.valueOf(switchLendItemId.getText())
				.intValue();
		defs.setUnNoted(unNotedToggle.isSelected());
		defs.membersOnly = membersOnlyToggle.isSelected();
		defs.setName(this.itemName.getText().toString());
		defs.value = (Integer.parseInt(this.itemPrice.getText().toString()));
		defs.teamId = (Integer.parseInt(this.team.getText().toString()));
		defs.stackable = (Integer.parseInt(this.stackable.getText().toString()));
		MainFrame.itemPanel.updateItemList(defs);
		defs.write(Cache.INSTANCE);
		MainFrame.itemPanel.printItemDetails(defs);
	}

	/**
	 * reloads the item
	 */
	public void reload() {
		clientScriptTextArea.setText(getClientScriptData());
		itemName.setText(new StringBuilder().append("")
				.append(this.defs.getName()).toString());
		itemPrice.setText(new StringBuilder().append("")
				.append(this.defs.value).toString());
		team.setText(new StringBuilder().append("").append(this.defs.teamId)
				.toString());
		//bonusesTF.setText(Arrays.toString(getBonuses(defs.getId())).replace("[", "").replace("]", ""));
		stackIdsTextField.setText(getStackIds());
		stackAmounts.setText(getStackAmounts());
		membersOnlyToggle.setSelected(this.defs.membersOnly);
		unNotedToggle.setSelected(this.defs.unnoted);
		stackable.setText(new StringBuilder().append("")
				.append(this.defs.stackable).toString());
		notedItemId.setText(new StringBuilder().append("")
				.append(this.defs.notedModelId).toString());
		switchNoteItemId.setText(new StringBuilder().append("")
				.append(this.defs.notedItemId).toString());
		lentId.setText(new StringBuilder().append("")
				.append(this.defs.notedModelId).toString());
		switchLendItemId.setText(new StringBuilder().append("")
				.append(this.defs.lentItemId).toString());
		modelColors.setText(new StringBuilder().append("")
				.append(getChangedModelColors()).toString());
		textureColors.setText(new StringBuilder().append("")
				.append(getChangedTextureColors()).toString());
		inventoryOptions.setText(new StringBuilder().append("")
				.append(getInventoryOptions()).toString());
		groundOptions.setText(new StringBuilder().append("")
				.append(getGroundOptions()).toString());
		maleEquips.setText(new StringBuilder().append("")
				.append(getMaleEquipIds()).toString());
		femaleEquips.setText(new StringBuilder().append("")
				.append(getFemaleEquipIds()).toString());
		modelRotations.setText(new StringBuilder().append("")
				.append(getModelRotations()).toString());
		modelOffsets.setText(new StringBuilder().append("")
				.append(getModelOffsets()).toString());
		inventoryModelId.setText(new StringBuilder().append("")
				.append(this.defs.invModelId).toString());
		inventoryModelZoom.setText(new StringBuilder().append("")
				.append(this.defs.invModelZoom).toString());
		MainFrame.itemPanel.printItemDetails(defs);
	}

	/**
	 * the model colors
	 * 
	 * @return
	 */
	public String getChangedModelColors() {
		String text = "";
		if (this.defs.originalModelColors != null) {
			for (int i = 0; i < this.defs.originalModelColors.length; i++) {
				text = new StringBuilder().append(text)
						.append(this.defs.originalModelColors[i]).append("=")
						.append(this.defs.modifiedModelColors[i]).append(";")
						.toString();
			}
		}
		return text;
	}

	/**
	 * the texture colors
	 * 
	 * @return
	 */
	public String getChangedTextureColors() {
		String text = "";
		if (this.defs.originalTextures != null) {
			for (int i = 0; i < this.defs.originalTextures.length; i++) {
				text = new StringBuilder().append(text)
						.append(this.defs.originalTextures[i]).append("=")
						.append(this.defs.modifiedTextures[i]).append(";")
						.toString();
			}
		}
		return text;
	}

	/**
	 * inventory options
	 * 
	 * @return
	 */
	public String getInventoryOptions() {
		String text = "";
		for (final String option : this.defs.getInventoryOptions()) {
			text = new StringBuilder().append(text)
					.append(option == null ? "null" : option).append(";")
					.toString();
		}
		return text;
	}

	/**
	 * ground options
	 * 
	 * @return
	 */
	public String getGroundOptions() {
		String text = "";
		for (final String option : this.defs.getGroundOptions()) {
			text = new StringBuilder().append(text)
					.append(option == null ? "null" : option).append(";")
					.toString();
		}
		return text;
	}

	/**
	 * the male equip ids.
	 * 
	 * @return
	 */
	public String getMaleEquipIds() {
		String text = "";
		final int[] equipArray = { defs.maleEquipModelId1,
				defs.maleEquipModelId2, defs.maleEquipModelId3 };
		if (equipArray != null) {
			for (int i = 0; i < equipArray.length; i++) {
				text = new StringBuilder().append(text).append(equipArray[i])
						.append(";").toString();
			}
		}
		return text;
	}

	/**
	 * the female equip ids.
	 * 
	 * @return
	 */
	public String getFemaleEquipIds() {
		String text = "";
		final int[] equipArray = { defs.femaleEquipModelId1,
				defs.femaleEquipModelId2, defs.femaleEquipModelId3 };
		if (equipArray != null) {
			for (int i = 0; i < equipArray.length; i++) {
				text = new StringBuilder().append(text).append(equipArray[i])
						.append(";").toString();
			}
		}
		return text;
	}

	/**
	 * the model rotations.
	 * 
	 * @return
	 */
	public String getModelRotations() {
		String text = "";
		final int[] equipArray = { defs.modelRotationX, defs.modelRotationY };
		if (equipArray != null) {
			for (int i = 0; i < equipArray.length; i++) {
				text = new StringBuilder().append(text).append(equipArray[i])
						.append(";").toString();
			}
		}
		return text;
	}

	/**
	 * the model offsets.
	 * 
	 * @return
	 */
	public String getModelOffsets() {
		String text = "";
		final int[] equipArray = { defs.modelOffsetX, defs.modelOffsetY };
		if (equipArray != null) {
			for (int i = 0; i < equipArray.length; i++) {
				text = new StringBuilder().append(text).append(equipArray[i])
						.append(";").toString();
			}
		}
		return text;
	}

	/**
	 * the new model id.
	 * @return
	 */
	public int getNewModelId() {
		try {
			JFrame frame = new JFrame();
			Object result = JOptionPane.showInputDialog(frame, "Please enter in the new Model ID:");
			return Integer.parseInt(result.toString());
		} catch (Exception e) {
			JFrame fr = new JFrame();
			JOptionPane.showMessageDialog(fr, "The number entered was not valid.");
		}
		return -1;
	}

	/**
	 * the client script data
	 * 
	 * @return
	 */
	public String getClientScriptData() {
		String text = "";
		final String lineSep = System.getProperty("line.separator");
		@SuppressWarnings("rawtypes")
		Iterator i$;
		if (this.defs.clientScriptData != null) {
			for (i$ = this.defs.clientScriptData.keySet().iterator(); i$
					.hasNext();) {
				final int key = ((Integer) i$.next()).intValue();
				final Object value = this.defs.clientScriptData.get(Integer
						.valueOf(key));
				text = new StringBuilder().append(text).append("Key: ")
						.append(key).append(", Value: ").append(value)
						.toString();
				text = new StringBuilder().append(text).append(lineSep)
						.toString();
			}
		}
		return text;
	}

	public String getStackIds()
	{
		String text = "";
		try
		{
			for (int index : this.defs.getStackIds()) {
				text = text + index + ";";
			}
		}
		catch (Exception e) {}
		return text;
	}


	public String getStackAmounts()
	{
		String text = "";
		try
		{
			for (int index : this.defs.getStackAmounts()) {
				text = text + index + ";";
			}
		}
		catch (Exception e) {}
		return text;
	}
	
	public int[] getBonuses(int itemId) {
		int[] bonuses = new int[18];
		try {
			@SuppressWarnings("resource")
			BufferedReader r = new BufferedReader(new FileReader(new File("data/items/bonuses.txt")));
			while(true) {
				String line = r.readLine();
				if(line == null)
					break;
				if(line.equals("") || line.startsWith("//"))
					continue;
				String[] split = line.split(" - ");
				int id = Integer.parseInt(split[0]);
				if(id == itemId) {
					String[] iB = split[1].split(", ");
					if(iB.length < 18 || iB.length > 18)
						throw new RuntimeException("Error: the item bonus array is too short or too long. Line: "+line);
					for(int i = 0; i < 18; i++)
						bonuses[i] = Integer.parseInt(iB[i]);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return bonuses;
	}
	
	public void updateBonuses(int itemId) {
		String[] bonusesText = bonusesTF.getText().split(", ");
		if(bonusesText.length < 18) {
			MainFrame.writeLog("Item Editor", "Bonuses couldn't be updated due to a size mismatch of the item bonuses array.");
			return;
		}
		int[] bonusesToPut = new int[18];
		for(int i = 0; i < 18; i++)
			bonusesToPut[i] = Integer.parseInt(bonusesText[i]);
		boolean replace = true;
		try {
			File bonusesFile = new File("data/items/bonuses.txt");
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
					replace = true;
					code.append(line+"\n");
				} 
				if(!(code.toString().contains(""+itemId)))
					replace = false;
			}
			if(replace) {
				BufferedWriter writer = new BufferedWriter(new FileWriter(bonusesFile));
				writer.write(code.toString());
				writer.close();
				r.close();
			} else if(!replace) {
				r.close();
				MainFrame.writeLog("Item Bonuses Editor", itemId+" didn't have any bonuses. Created new ones.");
				BufferedWriter f = new BufferedWriter(new FileWriter(new File("data/items/bonuses.txt"), true));
				f.write(itemId+" - "+Arrays.toString(bonusesToPut).replace("[", "").replace("]", ""));
				f.newLine();
				f.close();
			}
			MainFrame.writeLog("Bonuses Editor", "Successfully edited the bonuses of: "+itemId+": "+Arrays.toString(bonusesToPut));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void dumpModels() {
		Cache s = Cache.INSTANCE;
		int id = defs.id;
		int invId = defs.invModelId;
		int maleEquip1 = defs.maleEquipModelId1;
		int maleEquip2 = defs.maleEquipModelId2;
		int maleEquip3 = defs.maleEquipModelId3;
		int femaleEquip1 = defs.femaleEquipModelId1;
		int femaleEquip2 = defs.femaleEquipModelId2;
		int femaleEquip3 = defs.femaleEquipModelId3;
		if(invId > -1)
			Startup.saveModel(s, invId, ""+id+" - "+defs.name, ""+invId+" - Inv ID");
		if(maleEquip1 > -1)
			Startup.saveModel(s, maleEquip1, ""+id+" - "+defs.name, ""+maleEquip1+" - M1 ID");
		if(maleEquip2 > -1)
			Startup.saveModel(s, maleEquip2, ""+id+" - "+defs.name, ""+maleEquip2+" - M2 ID");
		if(maleEquip3 > -1)
			Startup.saveModel(s, maleEquip3, ""+id+" - "+defs.name, ""+maleEquip3+" - M3 ID");
		if(femaleEquip1 > -1)
			Startup.saveModel(s, femaleEquip1, ""+id+" - "+defs.name, ""+femaleEquip1+" - F1 ID");
		if(femaleEquip2 > -1)
			Startup.saveModel(s, femaleEquip2, ""+id+" - "+defs.name, ""+femaleEquip2+" - F2 ID");
		if(femaleEquip3 > -1)
			Startup.saveModel(s, femaleEquip3, ""+id+" - "+defs.name, ""+femaleEquip3+" - F3 ID");
	}
	
	public static void deleteModels(int id) {
		Cache store = Cache.INSTANCE;
		Index index = store.getIndices()[CacheConstants.MODELS_INDEX];
		ItemDefinition def = ItemDefinition.get(store, id);
		int invId = def.invModelId;
		int male1 = def.maleEquipModelId1;
		int male2 = def.maleEquipModelId2;
		int male3 = def.maleEquipModelId3;
		int female1 = def.femaleEquipModelId1;
		int female2 = def.femaleEquipModelId2;
		int female3 = def.femaleEquipModelId3;
		if(invId > -1) {
			Startup.saveModel(store, invId, ""+id+" - "+def.name, ""+invId+" - Inv ID");
			index.removeFile(invId, 0);
			MainFrame.writeLog("Model Deletion", 
				"Deleted model after making backup: \""+invId+"\" from item: ["+id+" - "+def.name+"]");
		}
		if(male1 > -1) {
			Startup.saveModel(store, male1, ""+id+" - "+def.name, ""+male1+" - M1 ID");
			index.removeFile(male1, 0);
			MainFrame.writeLog("Model Deletion", 
				"Deleted model after making backup: \""+male1+"\" from item: ["+id+" - "+def.name+"]");
		}
		if(male2 > -1) {
			Startup.saveModel(store, male2, ""+id+" - "+def.name, ""+male2+" - M2 ID");
			index.removeFile(male2, 0);
			MainFrame.writeLog("Model Deletion", 
				"Deleted model after making backup: \""+male2+"\" from item: ["+id+" - "+def.name+"]");
		}
		if(male3 > -1) {
			Startup.saveModel(store, male3, ""+id+" - "+def.name, ""+male3+" - M3 ID");
			index.removeFile(male3, 0);
			MainFrame.writeLog("Model Deletion", 
				"Deleted model after making backup: \""+male3+"\" from item: ["+id+" - "+def.name+"]");
		}
		if(female1 > -1 && male1 != female1) {
			Startup.saveModel(store, female1, ""+id+" - "+def.name, ""+female1+" - F1 ID");
			index.removeFile(female1, 0);
			MainFrame.writeLog("Model Deletion", 
				"Deleted model after making backup: \""+female1+"\" from item: ["+id+" - "+def.name+"]");
		}
		if(female2 > -1 && male2 != female2) {
			Startup.saveModel(store, female2, ""+id+" - "+def.name, ""+female2+" - F2 ID");
			index.removeFile(female2, 0);
			MainFrame.writeLog("Model Deletion", 
				"Deleted model after making backup: \""+female2+"\" from item: ["+id+" - "+def.name+"]");
		}
		if(female3 > -1 && male3 != female3) {
			Startup.saveModel(store, female3, ""+id+" - "+def.name, ""+female3+" - F3 ID");
			index.removeFile(female3, 0);
			MainFrame.writeLog("Model Deletion", 
				"Deleted model after making backup: \""+female3+"\" from item: ["+id+" - "+def.name+"]");
		}
	}
	
	private void clearClientScripts() {
		if(defs.clientScriptData == null || defs.clientScriptData.isEmpty()) {
			JOptionPane.showMessageDialog(null, "The ClientScripts for: ["+defs.id+" - "+defs.name+"] are empty.");
			return;
		}
		int opcode = JOptionPane.showConfirmDialog(null, "Remove the ClientScripts for: ["+defs.id+" - "+defs.name+"]?", 
			"Clear ClientScripts", JOptionPane.YES_NO_OPTION);
		if(opcode == JOptionPane.YES_OPTION) {
			defs.clientScriptData = null;
			clientScriptTextArea.setText(getClientScriptData());
			JOptionPane.showMessageDialog(null, "Cleared ClientScripts for: ["+defs.id+" - "+defs.name+"].");
		} else
			return;
	}
	
}
