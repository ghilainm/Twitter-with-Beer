package JavaGui;

import javax.swing.JFrame;
import javax.swing.JTextField;

public class MsgBox extends JFrame{
	public MsgBox(String msg){
		super("Attention");
		JTextField t =  new JTextField(msg);
		add(t);
		setVisible(true);
		pack();
		setResizable(false);
		t.setEditable(false);
		setLocationRelativeTo(null);
	}

}
