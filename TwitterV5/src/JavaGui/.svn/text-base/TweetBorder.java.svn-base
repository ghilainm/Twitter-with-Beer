package JavaGui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.border.AbstractBorder;

public class TweetBorder extends AbstractBorder{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int deep = 10;
	
	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height){
		g.setColor(new Color(0.5f,0f,0f));
		//for(int i=0; i < deep-1; i++)
			g.drawRoundRect(x, y, width-10, height-10, 10,10);
	}
	
	public Insets getBorderInsets(Component c,
            Insets newInsets){	
		newInsets.set(deep, deep, deep, deep);
		return newInsets;
	}
	public Insets getBorderInsets(Component c) {
		 return new Insets(deep, deep, deep, deep);
	}
	
	 public boolean isBorderOpaque() {
		 return true;
	 }
}
