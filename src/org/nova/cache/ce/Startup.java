package org.nova.cache.ce;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.nova.Constants;
import org.nova.cache.Cache;
import org.nova.cache.definition.ItemDefinition;

/**
 * Starts up the initial process which asks the user what cache they want to edit.
 * 
 * @author Karimshan Nawaz
 *
 */
public class Startup extends JFrame {

	private static final long serialVersionUID = 6885523248518945139L;

	private JPanel contentPane;
	private JTextField browse;
	private static Properties p;
	
	private static final String DEFAULT_TEXT = "Browse for your cache.";
	public static final String PREF_PATH = "data/ce/preferences.ini";
	
	public static void main(String[] args) throws IOException {
		try {
			MainFrame.setSubstanceSkin("Raven");
			p = new Properties();
			try {
				p.load(new FileInputStream(PREF_PATH));
			} catch(Exception e) {
				e.printStackTrace();
			}
			boolean loadNormally = Boolean.parseBoolean(p.getProperty("load_normally"));
			if(loadNormally)
				new Startup().setVisible(true);
			else {
				Cache.setInstance(Constants.CACHE_PATH);
				new MainFrame().setVisible(true);
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
	
	public Startup() {
		setTitle("Choose where to edit your cache");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 373, 209);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setResizable(false);
		browse = new JTextField();
		browse.setText(p.getProperty("cache_path"));
		if(browse.getText() == null || browse.getText().equals(""))
			browse.setText(DEFAULT_TEXT);
		browse.setEditable(false);
		browse.setBounds(21, 11, 326, 20);
		contentPane.add(browse);
		browse.setColumns(10);
		
		JButton btnLaunchEditor = new JButton("Start Editing!");
		btnLaunchEditor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File f = new File(browse.getText());
				boolean sendMessage = false;
				if(f.isDirectory()) {
					for(File files : f.listFiles()) {
						if(!files.getName().toLowerCase().contains("main_file_cache.idx7") || browse.getText().equals("")) {
							sendMessage = true;
						} else {
							Cache.setInstance(browse.getText());
							//transferItems(); // This was a success!
							new MainFrame().setVisible(true);
							dispose();
							return;
						}
					}
				}
				if(sendMessage) {
					JFrame frame = new JFrame();
					JOptionPane.showMessageDialog(frame, "Please choose the cache path.", 
						"Cache Editor", JOptionPane.WARNING_MESSAGE);
					Cache.setInstance(null);
					browse.setText(DEFAULT_TEXT);
				}
			}
		});
		btnLaunchEditor.setBounds(95, 96, 161, 36);
		contentPane.add(btnLaunchEditor);
		
		JButton btnBrowse = new JButton("Browse");
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final JFrame choose = new JFrame();
				final JFileChooser fc = new JFileChooser();
				File file = Startup.getFCPath();
				if(file != null)
					fc.setCurrentDirectory(file);
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				final int returnVal = fc.showOpenDialog(choose);
				if (returnVal == 0) {
					Startup.writeFCDir(fc.getSelectedFile().getPath());
					try {
						browse.setText(fc.getSelectedFile().getPath());
						p.load(new FileInputStream(PREF_PATH));
						p.replace("cache_path", fc.getSelectedFile().getPath()+"/");
						p.store(new FileOutputStream(PREF_PATH), "Cache Editor Preferences");
					} catch(Exception exception) {
						exception.printStackTrace();
					}
				} else {
					if(fc.getSelectedFile() != null)
						Startup.writeFCDir(fc.getSelectedFile().getPath());
					return;
				}
			}
		});
		btnBrowse.setBounds(95, 52, 161, 23);
		contentPane.add(btnBrowse);

	}
	
	/**
	 * Transfers items from one cache to another and changes the model ids.
	 */
	@SuppressWarnings("unused")
	private static void transferItems() {
		try {
			Cache from = new Cache("data/tizen/");
			Cache to = new Cache("C:\\Users\\Karimshan\\Google Drive\\RSPS\\Nova 667\\Nova Core\\data\\cache//");
			BufferedReader r = new BufferedReader(new FileReader(new File("data/transfer.txt")));
			String line;
			List<Integer> items = new ArrayList<Integer>();
			while((line = r.readLine()) != null) {
				if(line.startsWith("//") || line.equals(""))
					continue;
				String[] tokens = line.split(" - ");
				if(tokens.length == 1)
					items.add(Integer.parseInt(tokens[0]));
				else {
					int fromId = Integer.parseInt(tokens[0]);
					int toId = Integer.parseInt(tokens[1]);
					for(int i = fromId; i <= toId; i++)
						items.add(i);
				}
			}
			r.close();

			// This is just to make the old model ids into the new ones and dump them to a folder
			List<Integer> modelIdsFromCache = new ArrayList<Integer>();
			List<Integer> newModelIds = new ArrayList<Integer>();
			int modelId = 65684;
			for(int i : items) {
				ItemDefinition defs = ItemDefinition.get(from, i);
				modelIdsFromCache.add(defs.invModelId);
				newModelIds.add(modelId);
				if(defs.maleEquipModelId1 > -1) {
					modelIdsFromCache.add(defs.maleEquipModelId1);
					modelId++;
					newModelIds.add(modelId);
				}
				if(defs.maleEquipModelId2 > -1) {
					modelIdsFromCache.add(defs.maleEquipModelId2);
					modelId++;
					newModelIds.add(modelId);
				}
				modelId++;
			}
			// This saves the new models with new ids.
//			for(int i = 0; i < newModelIds.size(); i++)
//				saveModel(from, modelIdsFromCache.get(i), ""+newModelIds.get(i));
			
			// This is to make sure the old item ids get the new models put into them and
			// the defs get updated with new model and item ids.
			List<ItemDefinition> newDefs = new ArrayList<ItemDefinition>();
			int newItemId = 22531;
			modelId = 65684;
			for(int i : items) {
				ItemDefinition defs = ItemDefinition.get(from, i);
				ArrayList<Integer> newItemModels = new ArrayList<Integer>();
				newItemModels.add(modelId);
				defs.invModelId = modelId;
				if(defs.maleEquipModelId1 > -1) {
					modelId++;
					newItemModels.add(modelId);
					defs.maleEquipModelId1 = modelId;
					defs.femaleEquipModelId1 = modelId;
				}
				if(defs.maleEquipModelId2 > -1) {
					modelId++;
					newItemModels.add(modelId);
					defs.maleEquipModelId2 = modelId;
					defs.femaleEquipModelId2 = modelId;
				}
				modelId++;
				defs.id = newItemId;
				newDefs.add(defs);
				newItemId++;
			}
			
			// SUCCESS!!!
//			for(ItemDefinition def : newDefs) {
//				System.out.println(def.id+" - "+def.name+" - "+def.invModelId+""+
//				(def.maleEquipModelId1 > -1 ? ", "+def.maleEquipModelId1 : "")+""+
//				(def.maleEquipModelId2 > -1 ? ", "+def.maleEquipModelId2 : ""));
//				def.unnoted = true;
//				def.membersOnly = true;
//				//def.write(to);
//			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Saves a model from a cache without specifying the name of the file.
	 * @param store
	 * @param modelId
	 */
	public static void saveModel(Cache store, int modelId) {
		saveModel(store, modelId, null, null);
	}
	
	/**
	 * Saves a model from a cache and asks for the path without specifying name of files.
	 * @param store
	 * @param modelId
	 */
	public static void saveModel(Cache store, int modelId, String pathName) {
		saveModel(store, modelId, pathName, null);
	}
	
	/**
	 * Saves a model from a cache and specifies the name of the file.
	 * @param store
	 * @param modelId
	 * @param fileName
	 */
	public static void saveModel(Cache store, int modelId, String pathName, String fileName) {
		DateFormat d = new SimpleDateFormat("EEE MMM DD, yyyy");
		File f = new File("data/ce/models/"+d.format(new Date())+"/"+(pathName == null ? "" : pathName)+"/");
		File modelPath = new File(f.getPath() + "/"+(fileName == null ? ""+modelId : fileName)+".dat");
		boolean wasNull = false;
		if(fileName == null) {
			fileName = ""+modelId;
			wasNull = true;
		}
		byte[] data = store.getIndices()[7].getFile(modelId, 0);
		if(modelPath.exists()) {
			Startup.sendMessage("There is already a model file with the name: \""+fileName+"\" in this folder.");
			return;
		}
		if(data == null) {
			Startup.sendMessage("The model ID: \""+modelId+"\" is corrupt.");
			return;
		}
		if(!f.exists())
			f.mkdirs();
		try {
			OutputStream s = new FileOutputStream(modelPath.getPath());
			s.write(data);
			s.close();
			MainFrame.writeLog("Model Saver", "Saved model: "+(!wasNull ? ""+modelId+" (\""+fileName+"\")" :
				fileName)+" from the cache: "+store.getPath());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param string
	 */
	public static void sendMessage(String string) {
		JFrame frame = new JFrame();
		JOptionPane.showMessageDialog(frame, string, 
				"Editor", JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**
	 * 
	 * @param message
	 * @return
	 */
	public static boolean sendConfirm(String message) {
		JFrame f = new JFrame();
		int option = JOptionPane.showConfirmDialog(f, message, "Editor", JOptionPane.WARNING_MESSAGE);
		return option == 0 ? true : false;
	}
	
	/**
	 * Writes the last directory that was opened in the file chooser.
	 * For easier access of previously used files
	 * 
	 * @param path
	 */
	public static void writeFCDir(String path) {
		try {
			p.load(new FileInputStream(PREF_PATH));
			p.put("file_chooser_dir", path+"/");
			p.store(new FileOutputStream(PREF_PATH), "Cache Editor Preferences");
		} catch(Exception exception) {
			exception.printStackTrace();
		}
	}
	
	/**
	 * Returns the file chooser's current path file
	 * @return
	 */
	public static File getFCPath() {
		try {
			p.load(new FileInputStream(PREF_PATH));
			File f = new File(p.getProperty("file_chooser_dir"));
			if(f != null)
				return f;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}


}
