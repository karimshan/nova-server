package org.nova.cache.ce.panels;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import org.nova.cache.Cache;
import org.nova.cache.ce.MainFrame;
import org.nova.cache.ce.editors.ObjectDefinitionEditor;
import org.nova.cache.definition.ObjectDefinition;
import org.nova.cache.utility.CacheUtils;

/**
 * Recycled my ItemPanel class once again
 * 
 * @author Karimshan Nawaz
 *
 */
public class ObjectPanel extends JPanel {	
	
	/**
	 * @serialField 
	 */
	private static final long serialVersionUID = 2892662641577178228L;
	
	public DefaultListModel<ObjectDefinition> objectsListModel;
	public JList<ObjectDefinition> objectsList;
	private JTextField objectSearch;
	
	public ObjectPanel() {
		super();
		objectsListModel = new DefaultListModel<ObjectDefinition>();
		setLayout(null);
		setLayout(null);
		setBounds(0, 0, 764, 574);
		JScrollPane objectsScrollPane = new JScrollPane();
		objectsScrollPane.setBounds(15, 15, 335, 490);
		add(objectsScrollPane);
		objectsList = new JList<ObjectDefinition>(objectsListModel);
		objectsScrollPane.setViewportView(objectsList);
		objectSearch = new JTextField();
		objectSearch.setColumns(10);
		objectSearch.setBounds(78, 522, 272, 27);
		objectSearch.addKeyListener(getKeyListener(objectSearch));
		objectsList.addMouseListener(getMouseListener());
		add(objectSearch);
		JLabel label_1 = new JLabel("Search:");
		label_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		label_1.setBounds(15, 522, 62, 27);
		add(label_1);
		objectsList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		objectsList.setLayoutOrientation(JList.VERTICAL);
		objectsList.setVisibleRowCount(-1);
		addAllObjects();
	}
	
	/**
	 * Adds the objects to the list.
	 */
	public void addAllObjects() {
		for (int id = 0; id < CacheUtils.getObjectsSize(Cache.INSTANCE); id++)
			addObjectListModel(ObjectDefinition.getDef(id, Cache.INSTANCE));
	}

	/**
	 * 
	 * @param defs
	 */
	public void addObjectListModel(final ObjectDefinition defs) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				objectsListModel.addElement(defs);
			}
		});
	}
	
	/**
	 * 
	 * @param index
	 * @param def
	 */
	public void replaceObjectListModel(int index, final ObjectDefinition def) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				objectsListModel.set(index, def);
			}
		});
	}

	/**
	 * 
	 * @param defs
	 */
	public void updateObjectList(final ObjectDefinition defs) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				int index = objectsListModel.indexOf(defs);
				if (index == -1)
					objectsListModel.addElement(defs);
				else
					objectsListModel.setElementAt(defs, index);
			}
		});
	}

	/**
	 * 
	 * @param defs
	 */
	public void removeObjectListModel(final ObjectDefinition defs) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				objectsListModel.removeElement(defs);
			}
		});
	}
	
	/**
	 * Deletes the def.
	 */
	public void deleteObject() {
		ObjectDefinition defs = (ObjectDefinition) objectsList.getSelectedValue();
		int objectId = defs.id;
		int index = objectsListModel.indexOf(defs);
		boolean last = objectId == (CacheUtils.getObjectsSize() - 1);
		int opcode = JOptionPane.showOptionDialog(null,
				"Are you sure you want to delete this object? " + defs.id
						+ " - " + defs.name, "Delete the current object",
				JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE,
				null, new String[] { "Yes", "No" }, "Yes");
		if (opcode == JOptionPane.YES_OPTION) {
			MainFrame.writeLog("Object Definition Editor", "Removed Object: "
					+ defs.id + " - " + defs.name);
			Cache.INSTANCE.getIndices()[16].removeFile(defs.getArchiveId(), defs.getFileId());
			ObjectDefinition def = new ObjectDefinition(Cache.INSTANCE, objectId, true);
			if(!last)
				Cache.INSTANCE.getIndices()[16].putFile(def.getArchiveId(), def.getFileId(), def.getData());
			if(last)
				removeObjectListModel(defs);
			else
				replaceObjectListModel(index, def);
		}
	}
	
	/**
	 * Adds a new object.
	 */
	public void addNewObject() {
		ObjectDefinition defs = null;
		int opcode = JOptionPane.showOptionDialog(null,
				"Would you like to specify the Object's ID?",
				"Object Definition Editor", JOptionPane.YES_NO_OPTION,
				JOptionPane.WARNING_MESSAGE, null, new String[] {
						"Yes", "No" }, "No");
		if(opcode == JOptionPane.YES_OPTION) {
			int id = MainFrame.getNewDefinitionID("obj");
			if(id == -1) {
				JOptionPane.showMessageDialog(new JFrame(), "ID exceeds container size.");
				return;
			}
			defs = new ObjectDefinition(Cache.INSTANCE, id, false);
		} else if(opcode == JOptionPane.NO_OPTION)
			defs = new ObjectDefinition(Cache.INSTANCE, CacheUtils.getObjectsSize(Cache.INSTANCE), false);
		final ObjectDefinition finalDefs = defs;
		new Thread() {
			public void run() {
				new ObjectDefinitionEditor(finalDefs).setVisible(true);
			}
		}.start();
	}
	
	/**
	 * The searching of definitions.
	 * 
	 * @param type
	 * @return
	 */
	public KeyListener getKeyListener(JTextField search) {
		return new KeyListener() {

			@Override
			public void keyPressed(KeyEvent event) {
			}

			@Override
			public void keyReleased(KeyEvent event) {
				if(search.getText().equals("")) {
					try {
						objectsListModel.clear();
						addAllObjects();
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					objectsListModel.clear();
					for(int i = 0; i < CacheUtils.getObjectsSize(); i++) {
						ObjectDefinition def = ObjectDefinition.get(i);
						if(def.name.toLowerCase().contains(search.getText().toLowerCase())) {
							objectsListModel.addElement(def);
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
	 * The mouse listener
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public MouseAdapter getMouseListener() {
		return new MouseAdapter() {

			private void showPopupMenu(MouseEvent e) {
				JPopupMenu menu = new JPopupMenu();
				JList<ObjectDefinition> list = ((JList<ObjectDefinition>) e.getSource());
				int row = list.locationToIndex(e.getPoint());
				list.setSelectedIndex(row);
				ObjectDefinition def = list.getSelectedValue();
				String info = "[" + def.id + " - " + def.name + "]";
				JMenuItem[] options = { 
					new JMenuItem("Edit: " + info), 
					new JMenuItem("Clone: " + info),
					new JMenuItem("Delete: " + info), 
					new JMenuItem("Add New Item") 
				};
				for (JMenuItem i : options)
					menu.add(i);
				options[0].addActionListener(actionEvent -> {
					editObject();
				});
				options[1].addActionListener(actionEvent -> {
					cloneObject();
				});
				options[2].addActionListener(actionEvent -> {
					deleteObject();
				});
				options[3].addActionListener(actionEvent -> {
					addNewObject();
				});
				menu.show(e.getComponent(), e.getX(), e.getY());
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				JList<ObjectDefinition> list = ((JList<ObjectDefinition>) e.getSource());
				ObjectDefinition def = list.getSelectedValue();
				if (e.getClickCount() == 2)
					new ObjectDefinitionEditor(def);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger())
					showPopupMenu(e);
			}
		};
	}
	
	/**
	 * Edits the objectDef
	 */
	public void editObject() {
		ObjectDefinition defs = (ObjectDefinition) objectsList.getSelectedValue();
		if (defs == null)
			return;
		final ObjectDefinition finalDefs = defs;
		new Thread() {
			public void run() {
				new ObjectDefinitionEditor(finalDefs).setVisible(true);
			}
		}.start();
	}
	
	/**
	 * clones the def.
	 */
	public void cloneObject() {
		ObjectDefinition defs = (ObjectDefinition) objectsList.getSelectedValue();
		if (defs == null)
			return;
		defs = (ObjectDefinition) defs.clone();
		if (defs == null)
			return;
		int opcode = JOptionPane.showOptionDialog(null,
				"Would you like to specify the Object ID?",
				"Object Definition Editor", JOptionPane.YES_NO_OPTION,
				JOptionPane.WARNING_MESSAGE, null, new String[] {
						"Yes", "No" }, "No");
		if(opcode == JOptionPane.YES_OPTION) {
			int id = MainFrame.getNewDefinitionID("obj");
			if(id == -1) {
				JOptionPane.showMessageDialog(new JFrame(), "ID exceeds container size.");
				return;
			}
			defs.id = id;
		} else if(opcode == JOptionPane.NO_OPTION)
			defs.id = CacheUtils.getObjectsSize(Cache.INSTANCE);
		final ObjectDefinition finalDefs = defs;
		new Thread() {
			public void run() {
				new ObjectDefinitionEditor(finalDefs).setVisible(true);
			}
		}.start();
	}

}
