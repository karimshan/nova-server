package org.nova.utility;

import java.io.File;
import java.util.HashMap;

import org.python.util.PythonInterpreter;

public final class ScriptManager {
	
	/**
	 * Represents the {@code ScriptManager}.
	 */
	private static final ScriptManager SCRIPT_MANAGER = new ScriptManager();

	/**
	 * The pyCache map used for storing the python instances
	 */
	private HashMap<String, String> pyCache = new HashMap<String, String>();

	/**
	 * The {@code PythonInterpreter} singleton.
	 */
	private static final PythonInterpreter pythonInterpreter = new PythonInterpreter();

	/**
	 * @return the scriptsCache
	 */
	public HashMap<String, String> getScriptsCache() {
		return pyCache;
	}

	/**
	 * Initiates all of the scripts.
	 */
	public void initialize() {
		for (File file : new File("./data/scripts/").listFiles()) {
			if (file == null)
				continue;
			if (file.getName().endsWith(".py")) {
				pyCache.put(file.getName(), file.getPath());
			}
		}
	}

	/**
	 * Executes a given script.
	 * 
	 * @param script
	 *            The Script name to find.
	 * @return if it executed fine.
	 */
	public boolean executeScript(String script) {
		String file = pyCache.get(script);
		if (file != null) {
			this.getPythonInterpreter().execfile("./data/scripts/"+script);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Executes a script with the given arguments.
	 * 
	 * @param script
	 *            The script to execute.
	 * @param args
	 *            The arguments to pass.
	 * @return if we can execute it.
	 */
	public boolean executeScript(String script, Object[]... args) {
		String file = pyCache.get(script);
		if (file != null) {
			for (Object[] object : args) {
				this.getPythonInterpreter().set((String) object[0], object[1]);
			}
			this.getPythonInterpreter().execfile("./data/scripts/"+script);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @return the pythonInterpreter
	 */
	public PythonInterpreter getPythonInterpreter() {
		return pythonInterpreter;
	}

	/**
	 * @return the scriptManager
	 */
	public static ScriptManager getScriptManager() {
		return SCRIPT_MANAGER;
	}
}