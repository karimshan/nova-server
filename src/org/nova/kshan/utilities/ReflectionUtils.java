package org.nova.kshan.utilities;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * 
 * @author K-Shan
 *
 */
public class ReflectionUtils {
	
	public static boolean isFinal(Field f) {
		return Modifier.isFinal(f.getModifiers());
	}
	
	public static boolean isTransient(Field f) {
		return Modifier.isTransient(f.getModifiers());
	}
	
	public static boolean isAbstract(Field f) {
		return Modifier.isAbstract(f.getModifiers());
	}
	
	public static boolean isPrivate(Field f) {
		return Modifier.isPrivate(f.getModifiers());
	}
	
	public static boolean isPublic(Field f) {
		return Modifier.isPublic(f.getModifiers());
	}
	
	public static boolean isStatic(Field f) {
		return Modifier.isStatic(f.getModifiers());
	}

}
