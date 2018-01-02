package org.nova.utility.misc;

import java.lang.reflect.Method;

/**
 * 
 * @author K-Shan
 *
 */
public class TestMethod {
	
	public static void doStuff() {
		System.out.println("Hello cunt");
	}
	
	public static void doThis(String s) {
		System.out.println(s);
	}
	
	public static int getMethod() {
		int thisInteger = 443;
		System.out.println("This integer called from getMethod() = "+thisInteger);
		return thisInteger;	
	}
	
	public static void methodWithDelay(double delay, String... methods) {
		try {
			System.out.println("Sleeping for "+(delay)+" second(s)...");
			Thread.sleep((long) ((delay * 1000)));
			for(int i = 0; i < methods.length; i++) {
				System.out.println("Calling method: "+methods[i]);
				TestMethod.class.getDeclaredMethod(methods[i]).invoke(methods[i].getClass());
				System.out.println(methods[i]+" completed");
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void methodWithDelay(Method method, double delay) {
		try {
			System.out.println("Sleeping for "+(delay)+" second(s)...");
			Thread.sleep((long) ((delay * 1000)));
			System.out.println("Calling method: "+method.getName());
			method.invoke(TestMethod.class, "Hello");
			System.out.println(method.getName()+" completed");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]) {
		methodWithDelay(1.5, "doStuff", "getMethod");
		try {
			methodWithDelay(TestMethod.class.getDeclaredMethod("doThis", String.class), 1);
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
	}

}
