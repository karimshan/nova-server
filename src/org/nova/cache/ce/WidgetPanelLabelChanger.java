package org.nova.cache.ce;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

import org.nova.cache.loaders.WCDefinition;

/**
 * Changes the label for each component on a listModel for the widget panel
 * @author karim
 *
 */
public class WidgetPanelLabelChanger extends DefaultListCellRenderer {
	
	private static final long serialVersionUID = 3937143657787320357L;

	public Component getListCellRendererComponent(
	        JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus)
	    {
	        // I know DefaultListCellRenderer always returns a JLabel
	        // super setups up all the defaults
	        JLabel label = (JLabel)super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

	        // "value" is whatever object you put into the list, you can use it however you want here

	        // I'm going to prefix the label text to demonstrate the point
	        WCDefinition[] def = (WCDefinition[]) value;
	        int widgetId = def[0].getWidgetId();
	       label.setText(""+widgetId);

	       return label;

	    }

}
