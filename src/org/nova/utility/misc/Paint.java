package org.nova.utility.misc;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;

public class Paint extends Applet {
	
	private static final long serialVersionUID = 2636468321718495057L;
	
	public void paint(Graphics g) {
		try {
			setBackground(Color.WHITE);
			int circles = (int) (50 + (Math.random() * 951));
			for(int i = 0; i < circles; i++) {
				int red = 0 + (int) (Math.random() * 256);
				int green = 0 + (int) (Math.random() * 256);
				int blue = 0 + (int) (Math.random() * 256);
				g.setColor(new Color(red, green, blue));
				int rX = (int) (0 + (Math.random() * 801));
				int rY = (int) (0 + (Math.random() * 601));
				int bound = (int) (5 + (Math.random() * 196));
				int xToUse = rX + bound > 800 ? 800 : rX;
				int yToUse = rY + bound > 600 ? 600 : rY;
				Thread.sleep(50);
				if(bound % 2 == 0)
					g.fillOval(xToUse, yToUse, bound, bound);
				else
					g.drawOval(xToUse, yToUse, bound, bound);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
