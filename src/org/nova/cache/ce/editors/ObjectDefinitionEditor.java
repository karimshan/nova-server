package org.nova.cache.ce.editors;

import java.awt.DefaultKeyboardFocusManager;
import java.awt.Font;
import java.awt.KeyEventPostProcessor;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Arrays;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.nova.cache.Cache;
import org.nova.cache.ce.MainFrame;
import org.nova.cache.definition.ObjectDefinition;
import org.nova.kshan.utilities.KeyUtils;

/**
 * 
 * @author K-Shan
 *
 */
public class ObjectDefinitionEditor extends JFrame {

	private static final long serialVersionUID = -1328736259940173098L;

	private ObjectDefinition defs;
	
	private JPanel contentPane;
	private JTextField name;
	private JTextField modelIds;
	private JTextField options;
	private JTextField animation;
	private JCheckBox checkboxClipped;
	private JTextField sizeX;
	private JTextField sizeY;
	private JTextField width;
	private JTextField height;
	private JTextField length;
	private JCheckBox clickable;
	private JTextField cursor;
	private JTextField modelColors;
	private JTextField terrainTypes;

	public ObjectDefinitionEditor(ObjectDefinition defs) {
		this.defs = defs;
		setTitle("Editing Object: ["+defs.id+" - "+defs.name+"]");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setBounds(100, 100, 1043, 569);
		
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnOptions = new JMenu("Options");
		menuBar.add(mnOptions);
		
		JMenuItem mntmSave = new JMenuItem("Save");
		mntmSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				save();
				setTitle("Editing Object: ["+defs.id+" - "+defs.name+"]");
			}
		});
		mnOptions.add(mntmSave);
		
		JMenuItem mntmReload = new JMenuItem("Reload");
		mntmReload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fillTextFields();
			}
		});
		mnOptions.add(mntmReload);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblName = new JLabel("Name:");
		lblName.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 24));
		lblName.setBounds(16, 38, 104, 29);
		contentPane.add(lblName);
		
		name = new JTextField();
		name.setFont(new Font("Tahoma", Font.BOLD, 20));
		name.setBounds(142, 33, 348, 39);
		contentPane.add(name);
		name.setColumns(10);
		
		JLabel lblModelIds = new JLabel("Model IDs:");
		lblModelIds.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 19));
		lblModelIds.setBounds(16, 96, 130, 39);
		contentPane.add(lblModelIds);
		
		modelIds = new JTextField();
		modelIds.setFont(new Font("Tahoma", Font.BOLD, 20));
		modelIds.setColumns(10);
		modelIds.setBounds(142, 96, 348, 39);
		contentPane.add(modelIds);
		
		JLabel lblOptions = new JLabel("Options:");
		lblOptions.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 19));
		lblOptions.setBounds(16, 161, 118, 39);
		contentPane.add(lblOptions);
		
		options = new JTextField();
		options.setFont(new Font("Tahoma", Font.BOLD, 20));
		options.setColumns(10);
		options.setBounds(142, 159, 348, 39);
		contentPane.add(options);
		
		JLabel lblAnimation = new JLabel("Animation:");
		lblAnimation.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 19));
		lblAnimation.setBounds(16, 221, 130, 39);
		contentPane.add(lblAnimation);
		
		animation = new JTextField();
		animation.setFont(new Font("Tahoma", Font.BOLD, 20));
		animation.setColumns(10);
		animation.setBounds(142, 221, 348, 39);
		contentPane.add(animation);
		
		checkboxClipped = new JCheckBox("Clipped?");
		checkboxClipped.setFont(new Font("Tahoma", Font.BOLD, 17));
		checkboxClipped.setBounds(16, 267, 142, 39);
		contentPane.add(checkboxClipped);
		
		JLabel lblSizeX = new JLabel("Size X:");
		lblSizeX.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 19));
		lblSizeX.setBounds(168, 278, 89, 39);
		contentPane.add(lblSizeX);
		
		sizeX = new JTextField();
		sizeX.setFont(new Font("Tahoma", Font.BOLD, 20));
		sizeX.setColumns(10);
		sizeX.setBounds(252, 279, 263, 39);
		contentPane.add(sizeX);
		
		JLabel sizeYLabel = new JLabel("Size Y:");
		sizeYLabel.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 19));
		sizeYLabel.setBounds(168, 328, 89, 39);
		contentPane.add(sizeYLabel);
		
		sizeY = new JTextField();
		sizeY.setFont(new Font("Tahoma", Font.BOLD, 20));
		sizeY.setColumns(10);
		sizeY.setBounds(252, 328, 263, 39);
		contentPane.add(sizeY);
		
		JLabel lblWidth = new JLabel("Width:");
		lblWidth.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 19));
		lblWidth.setBounds(284, 385, 89, 39);
		contentPane.add(lblWidth);
		
		width = new JTextField();
		width.setFont(new Font("Tahoma", Font.BOLD, 20));
		width.setColumns(10);
		width.setBounds(383, 385, 118, 39);
		contentPane.add(width);
		
		JLabel lblHeight = new JLabel("Height:");
		lblHeight.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 19));
		lblHeight.setBounds(144, 445, 89, 39);
		contentPane.add(lblHeight);
		
		height = new JTextField();
		height.setFont(new Font("Tahoma", Font.BOLD, 20));
		height.setColumns(10);
		height.setBounds(233, 445, 118, 39);
		contentPane.add(height);
		
		JLabel lbllength = new JLabel("Length:");
		lbllength.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 19));
		lbllength.setBounds(31, 385, 89, 39);
		contentPane.add(lbllength);
		
		length = new JTextField();
		length.setFont(new Font("Tahoma", Font.BOLD, 20));
		length.setColumns(10);
		length.setBounds(134, 385, 118, 39);
		contentPane.add(length);
		
		clickable = new JCheckBox("Clickable?");
		clickable.setFont(new Font("Tahoma", Font.BOLD, 15));
		clickable.setBounds(16, 298, 130, 58);
		contentPane.add(clickable);
		
		JLabel lblCursor = new JLabel("Cursor:");
		lblCursor.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 19));
		lblCursor.setBounds(511, 35, 89, 39);
		contentPane.add(lblCursor);
		
		cursor = new JTextField();
		cursor.setFont(new Font("Tahoma", Font.BOLD, 20));
		cursor.setColumns(10);
		cursor.setBounds(600, 35, 118, 39);
		contentPane.add(cursor);
		
		JLabel lblObjectColors = new JLabel("Model colors:");
		lblObjectColors.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 19));
		lblObjectColors.setBounds(511, 96, 142, 39);
		contentPane.add(lblObjectColors);
		
		modelColors = new JTextField();
		modelColors.setFont(new Font("Tahoma", Font.BOLD, 20));
		modelColors.setColumns(10);
		modelColors.setBounds(653, 96, 348, 39);
		contentPane.add(modelColors);
		
		JLabel lblTerrainTypes = new JLabel("Terrain types:");
		lblTerrainTypes.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 19));
		lblTerrainTypes.setBounds(511, 161, 142, 39);
		contentPane.add(lblTerrainTypes);
		
		terrainTypes = new JTextField();
		terrainTypes.setFont(new Font("Tahoma", Font.BOLD, 20));
		terrainTypes.setColumns(10);
		terrainTypes.setBounds(653, 161, 348, 39);
		contentPane.add(terrainTypes);
		
		fillTextFields();
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
	
	private void fillTextFields() {
		name.setText(defs.name);
		animation.setText(defs.objectAnimation+"");
		options.setText(getOptions());
		modelIds.setText(getModelIds());
		sizeX.setText(""+defs.sizeX);
		sizeY.setText(""+defs.sizeY);
		checkboxClipped.setSelected(defs.isProjectileClipped());
		width.setText(""+defs.width);
		height.setText(""+defs.height);
		length.setText(""+defs.length);
		clickable.setSelected(defs.isClickable());
		cursor.setText(""+defs.cursor);
		modelColors.setText(getModelColors());
		terrainTypes.setText(getTerrainTypes());
	}
	
	public String getOptions() {
		return defs.options != null ? 
			Arrays.toString(defs.options).replace("[", "").replace("]", "") : "";
	}
	
	public String getTerrainTypes() {
		return defs.getTerrainTypes() != null ? 
			Arrays.toString(defs.getTerrainTypes()).replace("[", "").replace("]", "") : "";
	}
	
	public String getModelIds() {
		return defs.modelIds != null ? 
			Arrays.toString(defs.modelIds[0]).replace("[", "").replace("]", "") : "";
	}
	
	public String getModelColors() {
		String color = "";
		if(defs.originalColors != null)
			for(int i = 0; i < defs.originalColors.length; i++)
				color = new StringBuilder().append(color).append(defs.originalColors[i]).append(" = ").
					append(defs.modifiedColors[i]).append(i == defs.originalColors.length - 1 ? "" : ", ").toString();
		return color;
	}
	
	public void save() {
		defs.name = name.getText();
		defs.objectAnimation = Integer.parseInt(animation.getText());
		defs.sizeX = Integer.parseInt(sizeX.getText());
		defs.sizeY = Integer.parseInt(sizeY.getText());
		defs.height = Integer.parseInt(height.getText());
		defs.length = Integer.parseInt(length.getText());
		defs.width = Integer.parseInt(width.getText());
		defs.setClickable(clickable.isSelected());
		defs.cursor = Integer.parseInt(cursor.getText());
		if(!modelIds.getText().equals("")) {
			String[] modelIdsAsText = modelIds.getText().split(", ");
			defs.modelIds = null;
			defs.modelIds = new int[1][];
			defs.modelIds[0] = new int[modelIdsAsText.length == 0 ? 1 : modelIdsAsText.length];
			if(modelIdsAsText.length > 0)
				for(int i = 0; i < modelIdsAsText.length; i++)
					defs.modelIds[0][i] = Integer.parseInt(modelIdsAsText[i]);
		}
		if(!modelIds.getText().equals("")) {
			String[] terrain = terrainTypes.getText().split(", ");
			defs.terrainTypes = null;
			defs.terrainTypes = new byte[terrain.length];
			for(int i = 0; i < terrain.length; i++)
				defs.terrainTypes[i] = (byte) Integer.parseInt(terrain[i]);
		}
		if(!modelColors.getText().equals("")) {
			defs.originalColors = null;
			defs.modifiedColors = null;
			String[] modelColorsAsText = modelColors.getText().split(", ");
			defs.originalColors = new int[modelColorsAsText.length];
			defs.modifiedColors = new int[modelColorsAsText.length];
			for(int index = 0; index < modelColorsAsText.length; index++) {
				String[] successfulSplit = modelColorsAsText[index].split(" = ");
				defs.originalColors[index] = Integer.parseInt(successfulSplit[0]);
				defs.modifiedColors[index] = Integer.parseInt(successfulSplit[1]);
			}
		}
		defs.setProjectileClipped(checkboxClipped.isSelected());
		final String[] options = this.options.getText().split(", ");
		if(defs.options == null)
			defs.options = new String[options.length];
		for (int i = 0; i < defs.options.length; i++)
			defs.options[i] = (options[i].equals("null") ? null : options[i]);
		MainFrame.writeLog("Object Definition Editor", "Saved Game Object: ["+defs.id+" - "+defs.name+"] "
			+ "Options: "+Arrays.toString(defs.options)+", Model IDs: "+Arrays.toString(defs.modelIds[0]));
		MainFrame.objectPanel.updateObjectList(defs);
		defs.write(Cache.INSTANCE);
	}
	
}
