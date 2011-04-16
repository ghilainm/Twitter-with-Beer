package JavaGui;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class TextFieldPanel extends JPanel {
	private int marginHOut = 7;
	private int xSize;
	private int ySize;
	
	public TextFieldPanel(String txt){
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		add(Box.createHorizontalStrut(marginHOut));
		TextField t = new TextField(txt);
		add(t);
		add(Box.createHorizontalStrut(marginHOut));
		setOpaque(false);
		initSize(t.getWidth(), t.getHeight());
	}

	private void initSize(int width, int height) {
		xSize = width + 2*marginHOut;
		ySize = height;
	}
	
	public Dimension getPreferredSize(){
		return new Dimension(xSize, ySize);
	}
	
	public Dimension getMinimumSize(){
		return getPreferredSize();
	}
	
	public int getWidth(){
		return xSize;
	}
	
	public int getHeight(){
		return ySize;
	}
}
