package JavaGui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import TwitterApiImplementation.Tweet;


public abstract class LinePanel extends JScrollPane {

	private final int refreshPeriod = 360000;
	JPanel subLeftPane;
	Color color;
	
	public LinePanel(final Color color, final int xSize, final int ySize, final JPanel panel){
		super(panel);
		this.color = color;
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		subLeftPane = new JPanel();
		panel.add(subLeftPane);
		final JPanel subRightPane = new JPanel();
		subRightPane.setLayout(new BoxLayout(subRightPane, BoxLayout.PAGE_AXIS));
		subRightPane.add(Box.createGlue());
		subRightPane.setBackground(color);
		add(subRightPane);
		setPreferredSize(new Dimension(xSize,ySize));
		setBackground(color);
		final JButton moreButton = new JButton("More");
		moreButton.addActionListener(new moreButtonListener());
		final JButton refreshButton = new JButton("Refresh");
		refreshButton.addActionListener(new refreshButtonListener());
		subLeftPane.setBackground(color);
		subLeftPane.setLayout(new BoxLayout(subLeftPane, BoxLayout.Y_AXIS));
		subLeftPane.add(Box.createRigidArea(new Dimension(5,40)));
		final JPanel t = new JPanel();
		t.setLayout(new BoxLayout(t, BoxLayout.X_AXIS));
		t.add(Box.createRigidArea(new Dimension(20,10)));
		t.add(moreButton);
		t.add(Box.createRigidArea(new Dimension(50,10)));
		t.add(refreshButton);
		t.add(Box.createHorizontalGlue());
		t.setBackground(color);
		subLeftPane.add(t);
		subLeftPane.add(Box.createVerticalGlue());
	}
	
	protected void initRefresh() {
		final Timer refreshTimer = new Timer(true);
		refreshTimer.schedule(new RefreshThread(),0,refreshPeriod);
	}
	
	private class RefreshThread extends TimerTask{

		@Override
		public void run() {
			reFresh();
		}
		
	}
	
	protected abstract void reFresh();
	
	protected abstract void more();
	
	protected void reDraw(final LinkedList<Tweet> myLine){
		Iterator<Tweet> it = myLine.descendingIterator();
		while(it.hasNext()){
			subLeftPane.add(new TweetPane(it.next()),0);
			subLeftPane.add(Box.createRigidArea(new Dimension(5,10)),0);
		}
		subLeftPane.validate();
	}
	
	private class moreButtonListener implements ActionListener{
		
		public moreButtonListener() {
			super();
		}

		@Override
		public void actionPerformed(final ActionEvent arg0) {
			more();
		}
	}
	
	private class refreshButtonListener implements ActionListener{
		
		public refreshButtonListener() {
			super();
		}

		@Override
		public void actionPerformed(final ActionEvent arg0) {
			reFresh();
		}
	}
}
