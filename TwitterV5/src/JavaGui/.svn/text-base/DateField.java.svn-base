package JavaGui;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JTextField;

public class DateField extends JTextField{
	
	public DateField(String t){
		setBorder(BorderFactory.createEmptyBorder());
		Font f = new Font("calibri", Font.BOLD, 9);
		setFont(f);
		setText("        "+t);
		setForeground(new Color(0.8f,0.8f,0.8f));
		setOpaque(false);
		setEditable(false);
	}
	
	public int getWidth(){
		return getPreferredSize().width;
	}
	
	public int getHeight(){
		return getPreferredSize().height;
	}
	
	public void addStuffingSpace(int size){
		int actualSize = getWidth();
		while(actualSize<size){
			setText(getText()+" ");
			actualSize = getWidth();
		}
	}
}
