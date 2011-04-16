package JavaGui;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JTextField;

public class FieldInput extends JTextField{
	public FieldInput(){
		super();
		Font f = new Font("calibri", 0, 15);
		setFont(f);
		setEditable(true);
		setMaximumSize(new Dimension(400,200));
		setPreferredSize(new Dimension(100,25));
	}
}
