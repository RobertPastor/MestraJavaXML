package gui;

import java.awt.BorderLayout;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import ToolVersion.MestraToolVersion;

/**
 * class create in June 2007
 * @author t0007330
 *
 */
public class MainGuiSplash {

	final static Logger logger = Logger.getLogger(MainGuiSplash.class.getName()); 

	private static SplashWindow splashWindow = null;
	private static SplashWindowUpdate splashWindowUpdate = null;

	private static JFrame frame = null;

	// the progress bar is used to load the Java Classes
	final static int MaxProgressBarValue = 3000;

	static class SplashWindowUpdate extends Thread
	{
		private Thread updateThread = null;
		private int progressBarValue = 0;
		private boolean Continue = true;

		SplashWindowUpdate() {	
			updateThread = new Thread(this);
		}	

		public void start() {
			updateThread.start();
		}

		public void run() {

			while (Continue) {
				
				progressBarValue += 1;
				if (splashWindow == null) {
					Continue = false;
				}
				else {
					splashWindow.setProgressValue(progressBarValue);
				}
				try {
					Thread.sleep(10);
				} catch (InterruptedException uneException) {}
			}
		}
	}

	private static void setLookAndFeel(JFrame frame) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

		} catch (Exception e) { 
			logger.log(Level.SEVERE , "MainGui: Exception while setting look and feel " + e.getLocalizedMessage());
		}
		SwingUtilities.updateComponentTreeUI(frame);
	}

	/**
	 * Create the GUI and show it.  For thread safety,
	 * this method should be invoked from the
	 * event dispatch thread.
	 */

	public static void createAndShowGUI() {
		//Create and set up the window.
	    
	    String FrameTitle = "Java Mestra Tool ";
	    MestraToolVersion mestraToolVersion = new MestraToolVersion();
	    FrameTitle = FrameTitle + mestraToolVersion.getMestraToolVersion();
        
		frame = new JFrame(FrameTitle);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Create and set up the content pane.
		//JComponent newContentPane = new MainGuiApp();
		//newContentPane.setOpaque(true); //content panes must be opaque
		
		frame.getContentPane().add(new MainGuiApp(), BorderLayout.CENTER);
		//Display the window.
        frame.pack();
        frame.setVisible(true);
		
		setLookAndFeel(frame);
		logger.info( "Main window is now displayed");

		// Schedule a job for the event-dispatching thread:
		// suppressing the SplashWindow.
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				splashWindow.setVisible(false);          
				splashWindow.dispose();
			}
		});
	}


	public static void main(String[] args) {

		// The image to be displayed in the splash screen 	
		String imgName = "../clouds.jpg";

		//display of the splash screen
		splashWindow  = new SplashWindow(imgName,MaxProgressBarValue); 
		
		// launch the thread that will update the splash window
		splashWindowUpdate = new SplashWindowUpdate();
		splashWindowUpdate.start();

		//Turn off metal's use of bold fonts
		UIManager.put("swing.boldMetal", Boolean.FALSE);

		//Schedule a job for the event-dispatching thread:
		//creating and showing this application's GUI.
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}
}