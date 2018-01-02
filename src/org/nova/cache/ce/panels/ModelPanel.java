package org.nova.cache.ce.panels;

import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.nova.cache.Cache;
import org.nova.cache.ce.MainFrame;
import org.nova.cache.ce.Startup;
import org.nova.cache.utility.CacheUtils;

/**
 * Packs, edits or dumps models from the cache.
 * 
 * @author Karimshan Nawaz
 *
 */
public class ModelPanel extends JPanel {

	/**
	 * @serialField 
	 */
	private static final long serialVersionUID = -4159677525156339159L;
	
	private JCheckBox checkBox;
	private File model;
	private JTextField modelDirectory;
	private JTextField modelLocation;
	private boolean showedNulledModels;
	private JTextArea textOutput;
	
	public ModelPanel() {
		super();
		setLayout(null);
		setBounds(0, 0, 764, 574);
		setLayout(null);
		checkForNulledModels(this, MainFrame.mainTabs);
		modelDirectory = new JTextField();
		modelDirectory.setEditable(false);
		modelDirectory.setBounds(36, 63, 209, 20);
		add(modelDirectory);
		modelDirectory.setColumns(10);
		JLabel labelmodelDirectory = new JLabel("Model Directory (Folder)");
		labelmodelDirectory.setBounds(82, 38, 153, 20);
		add(labelmodelDirectory);
		JButton directoryBrowse = new JButton("Browse");
		directoryBrowse.addActionListener(e -> {
			modelDirectoryBrowse();
		});
		directoryBrowse.setBounds(82, 94, 89, 23);
		add(directoryBrowse);
		JButton btnPackModelsIn = new JButton("Pack models in directory");
		btnPackModelsIn.addActionListener(e -> {
			packModelsInDirectory();
		});
		btnPackModelsIn.setBounds(47, 132, 178, 28);
		add(btnPackModelsIn);
		JLabel lblModelLocationsingle = new JLabel("Model Location (Single Model)");
		lblModelLocationsingle.setBounds(356, 38, 171, 20);
		add(lblModelLocationsingle);
		modelLocation = new JTextField();
		modelLocation.setEditable(false);
		modelLocation.setColumns(10);
		modelLocation.setBounds(324, 63, 209, 20);
		add(modelLocation);
		JButton singleModelBrowse = new JButton("Browse");	
		singleModelBrowse.addActionListener(e -> {
			singleModelBrowse();
		});
		singleModelBrowse.setBounds(380, 94, 89, 23);
		add(singleModelBrowse);

		JButton btnPackSingleModel = new JButton("Pack Single Model");
		btnPackSingleModel.addActionListener(e -> {
			packSingleModel();
		});
		btnPackSingleModel.setBounds(340, 132, 178, 28);
		add(btnPackSingleModel);
		JLabel lblOutput = new JLabel("Output");
		lblOutput.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblOutput.setBounds(261, 168, 95, 22);
		add(lblOutput);
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(36, 201, 526, 205);
		add(scrollPane);
		textOutput = new JTextArea();
		textOutput.setEditable(false);
		scrollPane.setViewportView(textOutput);
		checkBox = new JCheckBox("Make the Model IDs the name of the files?");
		checkBox.setBounds(6, 172, 256, 19);
		add(checkBox);
	}

	/**
	 * Checks for nulled models
	 * @param panel
	 * @param pane
	 */
	public void checkForNulledModels(JPanel panel, JTabbedPane pane) {
		pane.addChangeListener(e -> {
			if (!showedNulledModels && pane.getSelectedComponent() == panel) {
				JFrame toShow = new JFrame();
				JOptionPane.showMessageDialog(toShow, "Checking for any nulled models in the cache...", "Model Packer",
						JOptionPane.INFORMATION_MESSAGE);
				CacheUtils.checkModels();
				if (CacheUtils.getNulledModels().size() > 0) {
					JOptionPane.showMessageDialog(toShow,
							"There are " + CacheUtils.getNulledModels().size()
									+ " nulled models in your cache. Please check the console for details.",
							"Model Packer", JOptionPane.INFORMATION_MESSAGE);
					MainFrame.writeLog("Model Packer", "Model Packing revealed "
							+ CacheUtils.getNulledModels().size() + " nulled models in your cache.");
					MainFrame.writeLog("Model Packer",
							"The nulled models are: " + Arrays.toString(CacheUtils.getNulledModels().toArray()));
				} else
					JOptionPane.showMessageDialog(toShow, "No nulled models exist!", "Model Packer",
							JOptionPane.INFORMATION_MESSAGE);
				showedNulledModels = true;
			}
		});
	}
	
	/**
	 * Packs a single model.
	 */
	public void packSingleModel() {
		if (model == null || !model.getPath().endsWith(".dat")) {
			JFrame frame = new JFrame();
			JOptionPane.showMessageDialog(frame, "Invalid model selected.", "Single Model Packing",
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		JFrame f = new JFrame();
		int picked = JOptionPane.showConfirmDialog(f,
				"Are you sure you want to pack the model: \"" + model.getName() + "\"?", "Confirm",
				JOptionPane.WARNING_MESSAGE);
		if (picked == 0) {
			f = new JFrame();
			picked = JOptionPane.showConfirmDialog(f, "Would you like to specify the Model ID?",
					"Specify Model ID", JOptionPane.WARNING_MESSAGE);
			if (picked == JOptionPane.OK_OPTION) {
				int output = getNewModelId();
				if (output < 0) {
					JOptionPane.showMessageDialog(null,
							"The model ID cannot be less than 0 or greater than the container amount.");
					textOutput.append("The model ID cannot be less than 0.\n");
					return;
				}
				if (model == null || !model.getPath().endsWith(".dat")) {
					System.out.println("File must end with a .dat extension.");
					return;
				}
				int modelId = 0;
				try {
					modelId = packCustomModel(CacheUtils.getBytesFromFile(model), output);
				} catch (IOException io) {
					io.printStackTrace();
				}
				MainFrame.writeLog("Model Packing", "The Model ID of the packed model from the file \""
						+ model.getName() + "\" is: " + modelId);
				textOutput.append("The Model ID of the packed model from the file \"" + model.getName()
						+ "\" is: " + modelId + "\n");
			} else if (picked == JOptionPane.CANCEL_OPTION) {
				if (model == null || !model.getPath().endsWith(".dat")) {
					System.out.println("File must end with a .dat extension.");
					return;
				}
				if (model.getPath().endsWith(".dat"))
					try {
						if (!CacheUtils.getNulledModels().isEmpty()) {
							int nulledId = CacheUtils.getNulledModels().get(0);
							int id = packCustomModel(CacheUtils.getBytesFromFile(model), nulledId);
							MainFrame.writeLog("Model Packing", "Packed new model from the file: \""
									+ model.getName() + "\" and replaced null model: " + id);
							textOutput.append("Packed new model from the file: \"" + model.getName()
									+ "\" and replaced null model: " + id + "\n");
							CacheUtils.getNulledModels().remove(0);
							return;
						}
						int modelId = packCustomModel(CacheUtils.getBytesFromFile(model));
						MainFrame.writeLog("Model Packing",
								"The Model ID of the packed model from the file \"" + model.getName()
										+ "\" is: " + modelId);
						textOutput.append("The Model ID of the packed model from the file \"" + model.getName()
								+ "\" is: " + modelId + "\n");
					} catch (Exception e) {
						e.printStackTrace();
					}
				else {
					JFrame frame = new JFrame();
					JOptionPane.showMessageDialog(frame, "The model selected is invalid.", "Model Selection",
							JOptionPane.WARNING_MESSAGE);
					return;
				}
			} else
				return;
		} else
			return;
	}
	
	/**
	 * Packs a model at the end of the list
	 * @param data
	 * @return
	 */
	public int packCustomModel(byte[] data) {
		Cache cache = Cache.INSTANCE;
		int archiveId = CacheUtils.getModelsSize(cache); // Inclusion of zero makes the need to put + 1 unnecessary
		if (cache.getIndices()[7].putFile(archiveId, 0, data)) {
			return archiveId;
		}
		MainFrame.writeLog("Model Packing", "Failed packing model " + archiveId);
		return -1;
	}

	/**
	 * Packs a model.
	 * @param data
	 * @param modelId
	 * @return
	 */
	public int packCustomModel(byte[] data, int modelId) {
		Cache cache = Cache.INSTANCE;
		int archiveId = modelId;
		if(archiveId > CacheUtils.getModelsSize(cache) || archiveId < 0) {
			MainFrame.writeLog("Model Packing", "Failed packing model: "+
				archiveId+", Model ID exceeds container amount.");
			return -1;
		} else if (cache.getIndices()[7].putFile(archiveId, 0, data)) {
			return archiveId;
		}
		MainFrame.writeLog("Model Packing", "Failed packing model " + archiveId);
		return -1;
	}
	
	/**
	 * Returns a new model id.
	 * 
	 * @return
	 */
	public int getNewModelId() {
		try {
			JFrame frame = new JFrame();
			Object result = JOptionPane.showInputDialog(frame, "Please enter in the new Model ID:");
			int id = Integer.parseInt(result.toString());
			if (id > CacheUtils.getModelsSize() || id < 0)
				return -1;
			return id;
		} catch (Exception e) {
			JFrame fr = new JFrame();
			JOptionPane.showMessageDialog(fr, "The number entered was not valid.");
		}
		return -1;
	}

	/**
	 * Browses for the single model.
	 */
	public void singleModelBrowse() {
		JFileChooser fc = new JFileChooser();
		File file = Startup.getFCPath();
		if (file != null)
			fc.setCurrentDirectory(file);
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		int val = fc.showOpenDialog(null);
		if (fc.getSelectedFile() == null)
			return;
		if (fc.getSelectedFile().getPath().endsWith(".dat") && val == 0) {
			Startup.writeFCDir(fc.getSelectedFile().getPath());
			model = fc.getSelectedFile();
			modelLocation.setText(fc.getSelectedFile().getPath());
		} else {
			if (fc.getSelectedFile() != null)
				Startup.writeFCDir(fc.getSelectedFile().getPath());
			JFrame frame = new JFrame();
			JOptionPane.showMessageDialog(frame, "The file must end with a \".dat\" extension.",
					"Single Model Selection", JOptionPane.WARNING_MESSAGE);
			model = null;
			modelLocation.setText("");
		}
	}

	/**
	 * Packs models in a dir
	 */
	public void packModelsInDirectory() {
		if (modelDirectory.getText() == null || modelDirectory.getText().equals("")) {
			JFrame frame = new JFrame();
			JOptionPane.showMessageDialog(frame, "No directory was selected!", "Multiple Model Packing",
					JOptionPane.WARNING_MESSAGE);
			modelDirectory.setText("");
			return;
		}
		File file = new File(modelDirectory.getText());
		File[] allFiles = file.listFiles();
		int fileCount = 0;
		List<File> modelsOnly = new ArrayList<File>();
		for (File allFile : allFiles)
			if (allFile.isFile() && allFile.getPath().endsWith(".dat")) {
				modelsOnly.add(allFile);
				fileCount++;
			}
		if (fileCount == 0) {
			JOptionPane.showMessageDialog(new JFrame(), "No models found in this directory.");
			return;
		}
		JFrame fr = new JFrame();
		int picked = JOptionPane.showConfirmDialog(fr,
				"Are you sure you want to pack all the models in this directory? (" + fileCount
						+ " total model(s))",
				"Confirm", JOptionPane.WARNING_MESSAGE);
		if (picked == 0) {
			boolean packWithCustomModelIDs = checkBox.isSelected();
			if (packWithCustomModelIDs) {
				for (int i = 0; i < modelsOnly.size(); i++) {
					File model = modelsOnly.get(i);
					String fileName = model.getName().replace(".dat", "");
					int modelId = -1;
					try {
						modelId = Integer.parseInt(fileName);
					} catch (Exception e) {
						JOptionPane.showMessageDialog(new JFrame(),
								"All of the model files in this directory must have numerical names.");
						textOutput.append(
								"All of the model files in this directory must have numerical names.\n");
						return;
					}
					if (modelId < 0 || modelId > CacheUtils.getModelsSize() - 1 + fileCount) {
						textOutput.append("Invalid file names found in this directory.\n");
						return;
					}
					try {
						int id = packCustomModel(CacheUtils.getBytesFromFile(model), modelId);
						MainFrame.writeLog("Model Packing", "Packed new model from the file: \""
								+ model.getName() + "\" and the new Model ID is: " + id);
						textOutput.append("Packed new model from the file: \"" + model.getName()
								+ "\" and the new Model ID is: " + id + "\n");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				MainFrame.writeLog("Model Packing",
						"Finished packing all the models in the directory: " + modelDirectory.getText());
				return;
			} else {
				try {
					if (!CacheUtils.getNulledModels().isEmpty()) {
						int nulledSize = CacheUtils.getNulledModels().size();
						int modelsSize = modelsOnly.size();
						for (int i = 0; i < (nulledSize > modelsSize ? modelsSize : nulledSize); i++) {
							File model = modelsOnly.get(i);
							int nulledId = CacheUtils.getNulledModels().get(i);
							int id = packCustomModel(CacheUtils.getBytesFromFile(model), nulledId);
							MainFrame.writeLog("Model Packing", "Packed new model from the file: \""
									+ model.getName() + "\" and replaced null model: " + id);
							textOutput.append("Packed new model from the file: \"" + model.getName()
									+ "\" and replaced null model: " + id + "\n");
						}
						for (int i = 0; i < (nulledSize > modelsSize ? modelsSize : nulledSize); i++) {
							CacheUtils.getNulledModels().remove(0);
							modelsOnly.remove(0);
						}
					}
					if (!modelsOnly.isEmpty())
						for (int i = 0; i < modelsOnly.size(); i++) {
							File model = modelsOnly.get(i);
							int id = packCustomModel(CacheUtils.getBytesFromFile(model));
							MainFrame.writeLog("Model Packing", "Packed new model from the file: \""
									+ model.getName() + "\" and the new Model ID is: " + id);
							textOutput.append("Packed new model from the file: \"" + model.getName()
									+ "\" and the new Model ID is: " + id + "\n");
						}
				} catch (Exception e) {
					e.printStackTrace();
				}
				MainFrame.writeLog("Model Packing",
						"Finished packing all the models in the directory: " + modelDirectory.getText());
				return;
			}
		} else
			return;
	}

	/**
	 * Browses for a dir.
	 */
	public void modelDirectoryBrowse() {
		final JFrame choose = new JFrame();
		final JFileChooser fc = new JFileChooser();
		File file = Startup.getFCPath();
		if (file != null)
			fc.setCurrentDirectory(file);
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		final int returnVal = fc.showOpenDialog(choose);
		if (returnVal == 0) {
			final File fi = fc.getSelectedFile();
			File fileChosen = fi;
			modelDirectory.setText(fileChosen.getPath() + "/");
			Startup.writeFCDir(fc.getSelectedFile().getPath());
		} else if (fc.getSelectedFile() != null)
			Startup.writeFCDir(fc.getSelectedFile().getPath());
	}
}
