package org.nova.kshan.utilities;

import java.lang.reflect.Method;

/**
 * 
 * @author K-Shan
 *
 */
public class TestUtils {
	
	public static void main(String[] args) throws NoSuchFieldException, SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException {
		TestUtils c = new TestUtils();
		c.setPoints();
		c.setPKPoints();
		c.setCurrency(c.getClass().getDeclaredMethod("getPoints"));
		System.out.println(c.getClass().getField("points").get(c));
	}
	
	private Method currency;

    private int pkPoints;
    
    public int getPKPoints() {
    	return pkPoints;
    }
    
    public void setPKPoints() {
    	pkPoints += 50;
    	System.out.println("PK Points are now: "+pkPoints);
    }
    
    public int points;
    
    public int getPoints() {
    	return points;
    }
    
    public void setPoints() {
    	points += 50;
    	System.out.println("Points are now: "+points);
    }
    
    public Method getCurrency() {
    	return currency;
    }
    
    public void setCurrency(Method method) {
    	this.currency = method;
    	System.out.println(method.getName());
    }

}