package org.nova.kshan.script;

import java.io.File;

import org.nova.utility.loading.Logger;

/**
 * A class used to manage the loading of scripts.
 * @author 'Vexia
 * 
 */
public final class ScriptManager {

	/**
	 * The directory to load scripts from.
	 */
	private static final String DIRECTORY = "./scripts/";

	/**
	 * The amount of scripts loaded.
	 */
	private static int amount;

	/**
	 * Method used to load the script manager.
	 * @param args the arguments.
	 */
	public static void main(String...args) {
		load();
	}

	/**
	 * Runs the script and returns the current script context after executing.
	 * @param context The script to run.
	 * @param args The arguments.
	 * @return The last script context executed.
	 */
	public static ScriptContext run(ScriptContext context, Object...args) {
		ScriptContext ctx = context;
		do {
			ctx = ctx.run(args);
		}
		while (ctx != null && ctx.isInstant());
		return ctx;
	}
	
	/**
	 * Initiates the chain reaction of script loading.
	 */
	public static void load() {
		amount = 0;
		load(new File(DIRECTORY));
		Logger.log("Script Manager", "Parsed " + amount + " Arios script" + (amount == 1 ? "": "s") + "...");
	}

	/**
	 * Method used to load a script by its path.
	 * @param path the path.
	 */
	public static void load(final String path) {
		load(new File(path));
	}

	/**
	 * Loads scripts from a directory.
	 * @param directory the directory. 
	 * @throws Throwable the throwable.
	 */
	public static void load(final File directory)  {
		try {
			for (File file : directory.listFiles()) {
				if (file.isDirectory()) {
					load(file);
					continue;
				}
				ScriptContext context = ScriptCompiler.parseRaw(file);
				if (ScriptCompiler.getBuilder() != null) {
					ScriptCompiler.getBuilder().configureScript(context);
				}
				amount++;
			}
		} catch (Throwable e) {
			e.printStackTrace();
			Logger.log("Script Error", "Error loading at directory - " + directory + "!");
		}
	}


}
