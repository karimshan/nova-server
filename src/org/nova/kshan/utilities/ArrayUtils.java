package org.nova.kshan.utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Stores methods for functions involving arrays.
 * 
 * @author K-Shan
 *
 */
public class ArrayUtils {

	/**
	 * Turns an array [] into an {@code ArrayList}.
	 * @param obj
	 * @return
	 */
	public static <T> ArrayList<T> asList(T[] obj) {
		ArrayList<T> list = new ArrayList<T>();
		if(obj == null)
			return list;
		for(T o : obj) {
			if(o == null)
				continue;
			if(!list.contains(o))
				list.add(o);
		}
		return list;
	}
	
	/**
	 * Turns a list into an array [].
	 * @param from
	 * @return
	 */
	public static <T> T[] asArray(List<T> from) {
		@SuppressWarnings("unchecked")
		T[] to = (T[]) new Object[from.size()];
		int count = 0;
		for(T o : from) {
			to[count] = o;
			count++;
		}
		return to;
	}
	
	/**
	 * Converts a list populated with strings into an array.
	 * 
	 * @param from
	 * @return
	 */
	public static String[] asStringArray(List<String> from) {
		String[] array = new String[from.size()];
		for(int i = 0; i < array.length; i++)
			array[i] = from.get(i);
		return array;
	}
	
	/**
	 * Converts the specified {@code int[] array} 
	 * to its wrapper class {@code Integer[]} 
	 * @param primitive
	 * @return
	 */
	public static Integer[] toObject(int[] primitive) {
		Integer[] integerObject = new Integer[primitive.length];
		for (int i = 0; i < primitive.length; i++)
			integerObject[i] = Integer.valueOf(primitive[i]);
		return integerObject;
	}
	
	/**
	 * Converts the specified {@code Integer[] array} 
	 * to its primitive {@code int[]} 
	 * @param integerObject
	 * @return
	 */
	public static int[] toPrimitive(Integer[] integerObject) {
		int[] primitive = new int[integerObject.length];
		for (int i = 0; i < integerObject.length; i++)
			primitive[i] = integerObject[i].intValue();
		return primitive;
	}
	
	/**
	 * Sorts all of the elements of this 1D array.
	 * @param sorting
	 * @return
	 */
	public static <T> T[] sort(T[] sorting) {
		Arrays.sort(sorting);
		return sorting;
	}
	
	/**
	 * Sorts all of the elements of this List<T>
	 * @param sorting
	 * @return
	 */
	public static <T> List<T> sort(List<T> sorting) {
		@SuppressWarnings("unchecked")
		T[] toArray = (T[]) new Object[sorting.size()];
		int count = 0;
		for(T s : sorting) {
			toArray[count] = s;
			count++;
		}
		Arrays.sort(toArray);
		count = 0;
		sorting.clear();
		for(T s : toArray)
			sorting.add(s);
		return sorting;
	}
	
	/**
	 * Returns the specified index of an array's element.
	 * @param array
	 * @param element
	 * @return
	 */
	public static int indexOf(int[] array, int element) {
		for(int i = 0; i < array.length; i++)
			if(array[i] == element)
				return i;
		return -1;
	}
	
	/**
	 * Returns the specified index of an array's element.
	 * @param array
	 * @param element
	 * @return
	 */
	public static int specialIndexOf(Object[] array, Object element) {
		for(int i = 0; i < array.length; i++)
			if(array[i].equals(element))
				return i;
		return -1;
	}
	
	/**
	 * Deposits a new element in the specified array at the specified index
	 * @param array
	 * @param index
	 * @param element
	 * @return
	 */
	public static int[] depositElement(int[] array, int index, int element) {
		int[] newArray = Arrays.copyOf(array, array.length + 1);
		System.arraycopy(newArray, index, newArray, index + 1, array.length - index);
		newArray[index] = element;
		return newArray;
	}
	
	/**
	 * Destroys the element at the specified index.
	 * @param array
	 * @param index
	 * @return
	 */
	public static int[] destroyElement(int[] array, int index) {
		int[] newArray = new int[array.length - 1];
		System.arraycopy(array, 0, newArray, 0, index);
		System.arraycopy(array, index + 1, newArray, index, array.length - index - 1);
		return newArray;
	}
	
	/**
	 * Destroys the element at the specified index.
	 * @param array
	 * @param index
	 * @return
	 */
	public static Object[] destroySpecialElement(Object[] array, int index) {
		Object[] newArray = new Object[array.length - 1];
		System.arraycopy(array, 0, newArray, 0, index);
		System.arraycopy(array, index + 1, newArray, index, array.length - index - 1);
		return newArray;
	}
	
	/**
	 * Destroys the element at the specified index.
	 * @param array
	 * @param index
	 * @return
	 */
	public static char[] destroyElement(char[] array, int index) {
		char[] newArray = new char[array.length - 1];
		System.arraycopy(array, 0, newArray, 0, index);
		System.arraycopy(array, index + 1, newArray, index, array.length - index - 1);
		return newArray;
	}
	
	/**
	 * Adds an element to the specified array.
	 * @param array
	 * @param element
	 * @return
	 */
	public static int[] addElement(int[] array, int element) {
		int[] newArray = Arrays.copyOf(array, array.length + 1);
		newArray[newArray.length - 1] = element;
		return newArray;
	}
	
	/**
	 * Adds an element to the specified array.
	 * @param array
	 * @param element
	 * @return
	 */
	public static Object[] addSpecialElement(Object[] array, Object element) {
		Object[] newArray = Arrays.copyOf(array, array.length + 1);
		newArray[newArray.length - 1] = element;
		return newArray;
	}
	
	/**
	 * Reverses the elements of the {@code array}
	 * @param array
	 * @return
	 */
	public static int[] reversedElements(int[] array) {
		int[] newArray = new int[array.length];
		for (int i = array.length - 1; i >= 0; i--)
			newArray[i] = array[array.length - 1 - i];
		return newArray;
	}
	
	/**
	 * Deposits all of the elements of the {@code fromArray} into
	 * the {@code toArray} at the specified {@code element}'s index.
	 * Removes the current {@code element}.
	 * @param fromArray
	 * @param toArray
	 * @param element
	 * @return
	 */
	public static int[] depositElements(int[] fromArray, int[] toArray, int element) {
		if(indexOf(toArray, element) == -1)
			return toArray;
		for(int i = 0; i < fromArray.length; i++)
			toArray = depositElement(toArray, indexOf(toArray, element) + i, fromArray[i]);
		toArray = destroyElement(toArray, indexOf(toArray, element));
		return toArray;
	}


}
