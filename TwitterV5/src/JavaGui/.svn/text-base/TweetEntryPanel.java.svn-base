package JavaGui;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JTextArea;
import TwitterApiImplementation.Tweet;

@SuppressWarnings("serial")
public class TweetEntryPanel extends JTextArea{
	private static final int  maxCharPerLine = (int) (Tweet.maxCharPerLine/1.5);
	private static final int maxChar = 140;
	private static final Color BorderColor = DisplayConstant.backGroundTweetColor;
	
	public TweetEntryPanel(){
		setColumns(maxCharPerLine);
		setRows((maxChar/maxCharPerLine)+1);
		setLineWrap(true);
		setTabSize(140);
		setEditable(true);
		setOpaque(false);
		setFont(DisplayConstant.tweetPolice);
		setForeground(DisplayConstant.tweetColor);
		setBorder(new CurvedBorder(BorderColor));
		addKeyListener(new KeyListener(){
			@Override
			public void keyTyped(KeyEvent arg0) {
				if(getText().length()>maxChar){
					setText(getText().substring(0, maxChar));
					new MsgBox("Sorry you can't exceed 140 characters");
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				
			}
		});
	}
	
	
	public int getHeight(){
		return getPreferredSize().height;
	}
	
}
