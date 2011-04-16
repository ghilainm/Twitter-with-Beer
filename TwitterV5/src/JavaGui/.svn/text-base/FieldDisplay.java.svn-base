package JavaGui;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JTextField;

public class FieldDisplay extends JTextField{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FieldDisplay(String string){
		super(string);
		setBorder(BorderFactory.createEmptyBorder());
		Font f = new Font("calibri", Font.BOLD, 15);
		setFont(f);
		setMaximumSize(new Dimension(string.length()*20,10));
		setEditable(false);
	}
}
