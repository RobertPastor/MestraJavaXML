package applet;

import gui.MainGuiApp;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class MainMain extends JPanel {

	/**
	 * Create the GUI and show it.  For thread safety,
	 * this method should be invoked from
	 * the event dispatch thread.
	 */
	private static void createAndShowGUI() {
		//Create and set up the window.
		JFrame frame = new JFrame("XML Java Mestra");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Add content to the window.
		Container container = new Container();
		container.setLayout(new GridLayout());
		container.add(new MainGuiApp());
		
		frame.add(container, BorderLayout.CENTER);
		frame.setPreferredSize(new Dimension(1200, 700));
		
		container.validate();
		container.revalidate();
		container.repaint();

		//Display the window.
		frame.pack();
		frame.setVisible(true);
		
	}

	public static void main(String[] args) {
		//Schedule a job for the event dispatch thread:
		//creating and showing this application's GUI.
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				//Turn off metal's use of bold fonts
				UIManager.put("swing.boldMetal", Boolean.FALSE);
				createAndShowGUI();
			}
		});
	}
}
