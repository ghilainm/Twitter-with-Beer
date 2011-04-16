package JavaGui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AuthorField extends JTextField{
	private static final long serialVersionUID = 1L;

	public AuthorField(String t){
		setBorder(BorderFactory.createEmptyBorder());
		Font f = new Font("calibri", Font.BOLD, 12);
		setFont(f);
		setForeground(new Color(1f,1f,1f));
		setText(t);
		setColumns(t.length());
		setOpaque(false);
		setEditable(false);
	}
	
	public int getWidth(){
		return getColumns()*getColumnWidth();
	}
	
	public int getHeight(){
		return getPreferredSize().height+10;
	}
	
	public Dimension getPreferredSize(){
		return new Dimension(getWidth(), super.getPreferredSize().height);
	}
	
	public Dimension getMinimumSize(){
		return getPreferredSize();
	}
	
	public void addStuffingSpace(int size){
		int actualSize = getWidth();
		while(actualSize<size){
			setText(getText()+" ");
			actualSize = getWidth();
		}
	}

}
