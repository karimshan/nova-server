package org.nova.cache.ce;

import java.awt.BorderLayout;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;

import org.jvnet.substance.SubstanceLookAndFeel;
import org.nova.cache.Cache;
import org.nova.cache.ce.panels.ItemPanel;
import org.nova.cache.ce.panels.ModelPanel;
import org.nova.cache.ce.panels.NPCPanel;
import org.nova.cache.ce.panels.ObjectPanel;
import org.nova.cache.ce.panels.WidgetPanel;
import org.nova.cache.utility.CacheUtils;

/**
 * 
 * @author K-Shan
 *
 */
public class MainFrame extends JFrame {

	private static final long serialVersionUID = -6714622612437634427L;
	
	public static ItemPanel itemPanel;
	public static NPCPanel npcPanel;
	public static ObjectPanel objectPanel;
	public static ModelPanel modelPanel;
	public static WidgetPanel widgetPanel;

	public static JTabbedPane mainTabs;

	/**
	 * Initialize the contents of the frame.
	 */
	public MainFrame() {
		super("K-Shan's Cache Editor [ALPHA 1.0] - "+Cache.INSTANCE.getPath());
		writeLog("Cache Editor", "Started up the Cache Editor.");
		setSize(805, 656);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		mainTabs = new JTabbedPane(JTabbedPane.TOP);
		getContentPane().add(mainTabs, BorderLayout.CENTER);
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		JMenu FileMenu = new JMenu("File");
		menuBar.add(FileMenu);
		JMenuItem exitMenuItem = new JMenuItem("Exit");
		FileMenu.add(exitMenuItem);
		JMenu themesMenu = new JMenu("Themes");
		menuBar.add(themesMenu);
		JMenuItem[] themes = { 
			new JMenuItem("Raven"),
			new JMenuItem("Nebula"),
			new JMenuItem("Business"),
			new JMenuItem("Business Blue Steel"), 
			new JMenuItem("Business Black Steel"),
			new JMenuItem("Raven Graphite"),
			new JMenuItem("Challenger Deep"), 
			new JMenuItem("Magma"), 
			new JMenuItem("Emerald Dusk"),
			new JMenuItem("Mist Aqua"),
			new JMenuItem("Green Magic"),
			new JMenuItem("Finding Nemo"),
			new JMenuItem("Mango"),
			new JMenuItem("Autumn"),
			new JMenuItem("Office Blue 2007")
		};
		int themeIndex = 0;
		for(JMenuItem jmi : themes) {
			themesMenu.add(jmi);
			jmi.setActionCommand("Theme_"+themeIndex+"");
			jmi.addActionListener(e -> {
				setSubstanceSkin(jmi.getText());
			});
			themeIndex++;
		}
		exitMenuItem.addActionListener(e -> {
			int opcode = JOptionPane.showOptionDialog(mainTabs,
				"Are you sure you want to exit?", "Exit",
				JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE,
				null, new String[] { "Yes", "No" }, "No");
			if (opcode == JOptionPane.YES_OPTION)
				System.exit(1);
		});
		
		// Start of ItemListPanel Components
		itemPanel = new ItemPanel();
		mainTabs.addTab("Item Editing", null, itemPanel, "Edit the definitions of the items in your cache.");
		
		// Start of NPCListPanel Components
		npcPanel = new NPCPanel();
		mainTabs.addTab("NPC Editing", null, npcPanel, "Edit the definitions of the npcs in your cache.");
		
		// Start of ObjectListPanel Components
		objectPanel = new ObjectPanel();
		mainTabs.addTab("Object Editing", null, objectPanel, "Edit the definitions of the objects in your cache.");
		
		// Start of ModelPanel Components
		modelPanel = new ModelPanel();
		mainTabs.addTab("Model Related", null, modelPanel, "Pack, remove, or dump models from your cache.");
		
		// Start of WidgetPanel Components
		widgetPanel = new WidgetPanel();
		mainTabs.addTab("Widget Editing", null, widgetPanel, "Edit the definitions of widgets in your cache.");
	
	}

	/**
	 * Returns a new ID for the specified definition type
	 * @param type
	 * @return
	 */
	public static int getNewDefinitionID(String type) {
		try {
			JFrame frame = new JFrame();
			Object result = JOptionPane.showInputDialog(frame, type.equals("obj") ? 
				"New Object ID:" : type.equals("npc") ? "New NPC ID:" : "New Item ID:");
			int id = Integer.parseInt(result.toString());
			if((id > CacheUtils.getItemsSize() && type.equals("item")) || 
				(id > CacheUtils.getNPCsSize() && type.equals("npc")) ||
					(id > CacheUtils.getObjectsSize() && type.equals("obj")) || id < 0)
				return -1;
			return id;
		} catch (Exception e) {
			JFrame fr = new JFrame();
			JOptionPane.showMessageDialog(fr,
					"The number entered was not valid.");
		}
		return -1;
	}

	/**
	 * Prints out text to a file and the console.
	 * @param header
	 * @param toAppend
	 */
	public static void writeLog(String header, String toAppend) {
		System.out.println("[" + header + "] " + toAppend);
		try {
			DateFormat d = new SimpleDateFormat("MMMM dd, yyyy");
			File f = new File("data/ce/logs/"+d.format(new Date())+".txt");
			BufferedWriter writer = new BufferedWriter(new FileWriter(f, true));
			writer.write("[" + new Date() + "]: " + toAppend);
			writer.newLine();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Writes bytes to a file
	 * @param data
	 * @param fileName
	 * @throws IOException
	 */
	public static void writeDataToFile(byte[] data, String fileName) {
		try {
			OutputStream out = new FileOutputStream(fileName+".dat");
			out.write(data);
			out.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sets the skin for the frame.
	 * @param theme
	 */
	public static void setSubstanceSkin(String theme) {
		theme = theme.replace(" ", "");
		try {
			SubstanceLookAndFeel.setSkin("org.jvnet.substance.skin."+theme+"Skin");
			JFrame.setDefaultLookAndFeelDecorated(true);
			JDialog.setDefaultLookAndFeelDecorated(true);
			JPopupMenu.setDefaultLightWeightPopupEnabled(false);
		} catch (Exception e) {
			System.err.println("Substance error: " + e.getMessage());
		}
	}
}
