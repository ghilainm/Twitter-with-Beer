package JavaGui;

import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class TextField extends JTextArea {
	public TextField(String t){
		setBorder(BorderFactory.createEmptyBorder());
		Font f = DisplayConstant.tweetPolice;
		setFont(f);
		setForeground(DisplayConstant.tweetColor);
		setText(t);
		setEditable(false);
		setOpaque(false);
	}
	
	public int getWidth(){
		return getPreferredSize().width;
	}
	
	public int getHeight(){
		return getPreferredSize().height;
	}
	
}
