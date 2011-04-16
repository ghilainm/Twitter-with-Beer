package JavaGui;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class AuthorDeleteTweetPanel extends JPanel{
	
	private AuthorField af;
	private DeleteButton db;
	private int margeOut = 10;
	private int space = 20;
	private int xSize;
	private int ySize;
	
	public AuthorDeleteTweetPanel(String authorName, String tweetID){
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		af = new AuthorField(authorName);
		add(Box.createHorizontalStrut(margeOut));
		add(af);
		add(Box.createHorizontalGlue());
		db = new DeleteButton(tweetID);
		add(db);
		add(Box.createHorizontalStrut(margeOut));
		setOpaque(false);
		setWidth(0);
		setHeight();
	}
	
	public void setWidth(int minWidth){
		xSize = Math.max(minWidth,af.getWidth()+ImageLoader.DELETE_BUTTON_WIDTH +margeOut*2 + space);
	}
	
	public void setHeight(){
		ySize = Math.max(af.getHeight(), db.getHeight());
	}
	public Dimension getPreferredSize(){
		return new Dimension(xSize, ySize);
	}
	
	public Dimension getMinimumSize(){
		return getPreferredSize();
	}
	
	public int getHeight(){
		return getPreferredSize().height;
	}
	
	public int getWidth(){
		return getPreferredSize().width;
	}
	
}
