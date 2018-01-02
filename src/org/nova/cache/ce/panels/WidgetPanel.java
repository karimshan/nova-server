package org.nova.cache.ce.panels;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;

import org.nova.cache.Cache;
import org.nova.cache.ce.MainFrame;
import org.nova.cache.ce.Startup;
import org.nova.cache.ce.WidgetPanelLabelChanger;
import org.nova.cache.loaders.WCDefinition;
import org.nova.cache.loaders.WidgetSettings;
import org.nova.utility.misc.Misc;
import javax.swing.JTabbedPane;

public class WidgetPanel extends JPanel {

	/**
	 * @serialField
	 */
	private static final long serialVersionUID = 3005487719679678603L;
	
	public JList<WCDefinition[]> widgetsList;
	public DefaultListModel<WCDefinition[]> widgetsListModel;

	/**
	 * Constructor
	 */
	public WidgetPanel() {
		super();
		setLayout(null);
		setBounds(0, 0, 800, 596);
		widgetsListModel = new DefaultListModel<WCDefinition[]>();
		widgetsList = new JList<WCDefinition[]>(widgetsListModel);
		widgetsList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		widgetsList.setLayoutOrientation(JList.VERTICAL);
		widgetsList.setVisibleRowCount(-1);
		widgetsList.setCellRenderer(new WidgetPanelLabelChanger()); // Changes the name to each widget ID.
		JScrollPane scrollPane = new JScrollPane(widgetsList);
		scrollPane.setBounds(15, 30, 50, 490);
		add(scrollPane);
		JButton btnRu = new JButton("Add Widget");
		btnRu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addWidget();
			}
		});
		btnRu.setBounds(15, 538, 97, 25);
		add(btnRu);
		
		JLabel lblWidgets = new JLabel("Widgets");
		lblWidgets.setBounds(15, 0, 69, 20);
		add(lblWidgets);
		
		JButton btnDeleteWidget = new JButton("Delete Widget");
		btnDeleteWidget.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(widgetsList.getSelectedValue() == null)
					return;
				deleteWidget();
			}
		});
		btnDeleteWidget.setBounds(120, 538, 97, 25);
		add(btnDeleteWidget);
		
		JButton btnBackupWidget = new JButton("Backup Widget");
		btnBackupWidget.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(widgetsList.getSelectedValue() == null)
					return;
				int widgetId = widgetsList.getSelectedValue()[0].getWidgetId();
				backupWidget(widgetId);
				String path = "data/ce/widgets/backups/"+(new Date())+"/"+widgetId;
				Startup.sendMessage("Backed up widget "+widgetId+" in: "+path);
			}
		});
		btnBackupWidget.setBounds(224, 538, 97, 25);
		add(btnBackupWidget);
		
		JButton btnCopyWidget = new JButton("Copy Widget");
		btnCopyWidget.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(widgetsList.getSelectedValue() == null)
					return;
				copyWidget();
			}
		});
		btnCopyWidget.setBounds(329, 538, 97, 25);
		add(btnCopyWidget);
		
		JButton btnPrintDetails = new JButton("Print Details");
		btnPrintDetails.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(widgetsList.getSelectedValue() == null)
					return;
				printWidgetDetails(widgetsList.getSelectedValue());
			}
		});
		btnPrintDetails.setBounds(434, 538, 97, 25);
		add(btnPrintDetails);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(77, 40, 667, 480);
		add(tabbedPane);
		addAllWidgets();
	}
	
	/**
	 * Copies the current widget onto a new widget
	 */
	private void copyWidget() {
		
	}
	
	/**
	 * Prints the widget's details
	 * @param id
	 */
	private void printWidgetDetails(WCDefinition[] defin) {
		try {
			int id = defin[0].getWidgetId();
			JFrame frame = new JFrame();
			frame.setTitle("Widget: "+id+" details");
			frame.setVisible(true);
			frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			frame.setSize(900, 700);
			frame.getContentPane().setLayout(null);
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setBounds(0, 0, 880, 670);
			frame.getContentPane().add(scrollPane);
			JTextArea area = new JTextArea();
			area.setEditable(false);
			scrollPane.setViewportView(area);
			area.setFont(new Font("Tahoma", Font.PLAIN, 14));
			Class<WCDefinition> c = WCDefinition.class;
			for(Field f : c.getDeclaredFields()) {
				f.setAccessible(true);
				area.append("["+f.getName()+"](");
				WCDefinition[] defs = WCDefinition.getComponents(id);
				for(int i = 0; i < defs.length; i++)
					area.append(i+""+(i == defs.length - 1 ? "" : ", ")+"");
					area.append("): ");
					for(WCDefinition def : defs) {
						Object o = f.get(def);
						if(o instanceof WCDefinition[][] || o instanceof WidgetSettings)
							continue;
						if(o instanceof int[]) {
							int[] intArray = (int[]) o;
							area.append(Arrays.toString(intArray)+", ");
						} else if(o instanceof String[]) {
							String[] array = (String[]) o;
							area.append(Arrays.toString(array)+", ");
						} else if(o instanceof Object[]) {
							Object[] array = (Object[]) o;
							area.append(Arrays.toString(array)+", ");
						} else if(o instanceof int[][]) {
							int[][] array = (int[][]) o;
							for(int i = 0; i < array.length; i++)
								area.append(Arrays.toString(array[i])+", ");
						} else if(o instanceof byte[]) {
							byte[] array = (byte[]) o;
							area.append(Arrays.toString(array)+", ");
						} else if(o instanceof HashMap) {
							@SuppressWarnings("unchecked")
							HashMap<Integer, Object> map = (HashMap<Integer, Object>) o;
							ArrayList<Integer> keys = new ArrayList<Integer>();
							ArrayList<Object> values = new ArrayList<Object>();
							for(Object k : map.values())
								values.add(k);
							for(Integer k : map.keySet())
								keys.add(k);
							for(int i = 0; i < values.size(); i++)
								area.append("["+keys.get(i)+", "+values.get(i)+", ");
						} else {
							area.append(f.get(def)+", ");
						}
					}
				area.append("\n");
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Deletes the selected widget
	 */
	private void deleteWidget() {
		int widgetId = widgetsList.getSelectedValue()[0].getWidgetId();
		int index = widgetsList.getSelectedIndex();
		int opcode = JOptionPane.showOptionDialog(null,
				"Are you sure you want to delete this widget? ("+widgetId+")", "Delete the current widget",
					JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, new String[] { "Yes", "No" }, "No");
		if(opcode == JOptionPane.YES_OPTION) {
			backupWidget(widgetId);
			String path = "data/ce/widgets/backups/"+(new Date())+"/"+widgetId;
			System.out.println("Deleted widget: "+widgetId+", saved a backup in: "+path);
			Cache.deleteArchive(3, widgetId);
			widgetsListModel.remove(index);
		}
	}
	
	/**
	 * Backs up a widget in case someone deletes it on accident.
	 * @param wId
	 * @return
	 */
	private void backupWidget(int wId) {
		DateFormat d = new SimpleDateFormat("EEE MMM dd, yyyy");
		File f = new File("data/ce/widgets/backups/"+d.format(new Date())+"/"+wId+"/");
		if(f.exists()) {
			Startup.sendMessage("There is already a widget backed up with the name: \""+wId+"\" in this folder.");
			return;
		}
		if(Cache.INSTANCE.getIndices()[3].getArchive(wId) == null) {
			Startup.sendMessage("Widget \""+wId+"\" is corrupt.");
			return;
		}
		if(!f.exists())
			f.mkdirs();
		int components = Cache.INSTANCE.getIndices()[3].getValidFilesCount(wId);
		for(int i = 0; i < components; i++) {
			byte[] data = Cache.INSTANCE.getIndices()[3].getFile(wId, i);
			if(data == null) {
				System.out.println("The data for widget "+wId+" is corrupt.");
				return;
			}
			MainFrame.writeDataToFile(data, f.getPath()+"/"+i);
		}
		MainFrame.writeLog("Widget Backup", "Backed up widget "+wId+" from the cache: "+Cache.INSTANCE.getPath());
	}
	
	/**
	 * Adds a new widget to the cache
	 */
	private void addWidget() {
		
	}

	/**
	 * Adds all the widgets to the panel.
	 */
	public void addAllWidgets() {
		for (int id = 0; id < Misc.getInterfaceDefinitionsSize(); id++) {
			if(Cache.INSTANCE.getIndices()[3].getArchive(id) != null) // Will only add interfaces that aren't corrupt
				addWidgetListModel(WCDefinition.getComponents(id));
		}
	}
	
	/**
	 *
	 * @param defs
	 */
	public void addWidgetListModel(final WCDefinition[] def) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				widgetsListModel.addElement(def);
			}
		});
	}
}
