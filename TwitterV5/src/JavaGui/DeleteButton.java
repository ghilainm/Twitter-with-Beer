package JavaGui;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;

@SuppressWarnings("serial")
public class DeleteButton extends JButton{
	private String tweetID;
	private boolean isMouseIn = false;
	
	public DeleteButton(String tweetID){
		this.tweetID = tweetID;
		this.addActionListener(new DeleteButtonActionListener());
		this.addMouseListener(new DeleteButtonMouseListener());
		this.setBorderPainted(false);
		this.setOpaque(false);
	}
	
	private class DeleteButtonActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			EventHandler.deleteEvent(tweetID);
		}
		
	}
	
	public Dimension getPreferredSize(){
		return new Dimension(ImageLoader.DELETE_BUTTON_WIDTH, ImageLoader.DELETE_BUTTON_HEIGHT);
	}
	
	public Dimension getMinimumSize(){
		return getPreferredSize();
	}
	
	public Dimension getMaximumSize(){
		return getPreferredSize();
	}
	
	public int getWidth(){
		return ImageLoader.DELETE_BUTTON_WIDTH;
	}
	
	public int getHeight(){
		return ImageLoader.DELETE_BUTTON_HEIGHT;
	}
	private class DeleteButtonMouseListener implements MouseListener{
		@Override
		public void mouseClicked(MouseEvent me) {
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			isMouseIn = true;
			repaint();
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			isMouseIn = false;
			repaint();
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
		}
		
	}
	
	protected void paintComponent(Graphics g){
		this.setBorderPainted(false);
		if(isMouseIn){
			g.drawImage(ImageLoader.DELETE_BUTTON_SELECTED, 0, 0, null);
		}
		else{
			g.drawImage(ImageLoader.DELETE_BUTTON, 0, 0, null);
		}
	}
}	
