package JavaGui;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.Box;
import javax.swing.JPanel;

import TwitterApiImplementation.Tweet;

@SuppressWarnings("serial")
public class TweetPane extends JPanel{
	
	public static final Color bgC = DisplayConstant.backGroundTweetColor;
	
	public TweetPane(Tweet t){
		super();
		setOpaque(false);
		setBackground(new Color(0f, 0f, 0f));
		FlowLayout fl = new FlowLayout();
		fl.setAlignment(FlowLayout.LEFT);
		setLayout(fl);
		add(Box.createGlue());
		AuthorDeleteTweetPanel author = new AuthorDeleteTweetPanel(t.getPosterName(), t.getTweetID());
		add(author);
		TextFieldPanel textFieldPanel = new TextFieldPanel(t.getMsg());
		add(textFieldPanel);
		DateField dateField = new DateField(t.getPostingDate().toString());
		add(dateField);
		setBackground(new Color(0.5f,0,0));
		add(Box.createGlue());
		setVisible(true);
		
		int xSize = author.getWidth();
		xSize = Math.max(xSize, textFieldPanel.getWidth());
		xSize = Math.max(xSize, dateField.getWidth());
		
		int ySize = author.getHeight();
		ySize += textFieldPanel.getHeight();
		ySize += dateField.getHeight()+20;
		
		author.setWidth(xSize);
		
		setPreferredSize(new Dimension(xSize+30,ySize));
		setMaximumSize(new Dimension(xSize+30,ySize));
	}
	
	protected void paintComponent(Graphics g) {
		int x = 0;
	    int y = 0;
	    int w = getWidth();
	    int h = getHeight();
	    int arc = 30;

	    Graphics2D g2 = (Graphics2D) g.create();
	    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
	            RenderingHints.VALUE_ANTIALIAS_ON);

	    g2.setColor(bgC);
	    g2.fillRoundRect(x, y, w, h, arc, arc);

	    g2.setStroke(new BasicStroke(2.5f));
	    g2.setColor(new Color(1,1, 1,0.8f));
	    g2.drawRoundRect(x, y, w, h, arc, arc); 
	    
	    g2.dispose();
    }
}
