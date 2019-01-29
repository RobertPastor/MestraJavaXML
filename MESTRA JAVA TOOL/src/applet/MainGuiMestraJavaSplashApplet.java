package applet;

import gui.MainGuiApp;
import gui.SplashWindow;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Main launcher of the Java Applet.
 * @author t0007330
 * @since June 2007
 *
 */
public class MainGuiMestraJavaSplashApplet extends Applet {

	final static Logger logger = Logger.getLogger(MainGuiMestraJavaSplashApplet.class.getName()); 

    /**
     * main
     */
    private static final long serialVersionUID = -3107464298252308210L;
    private static SplashWindow splashWindow = null;
    private static SplashWindowUpdate splashWindowUpdate = null;
    
    // the progress bar is used during the load of the Java Classes
    final static int MaxProgressBarValue = 3000;
    
    static class SplashWindowUpdate extends Thread {
    	
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
        	
            logger.info( "run method");
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

    public void init() {
        // init the Applet but do not launch it
        logger.info( "Applet is inited" );
    }
    
    public void start() {
    	
    	logger.info( "Applet is started" );

    	// this is the image displayed in the splash screen     
        String imgName = "clouds.jpg";
        
        //display of the splash screen
        splashWindow  = new SplashWindow(imgName, MaxProgressBarValue); 
        
        // start to update the splash screen
        splashWindowUpdate = new SplashWindowUpdate();
    	splashWindowUpdate.start();
    	
        // Turn off metal's use of bold fonts
        UIManager.put("swing.boldMetal", Boolean.FALSE);
        
        createAndShowGUI();
        showStatus("Loading...");
    }
    
    public void destroy()  {
        logger.info( "Applet being destroyed");
    }
    
    private void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        } catch (Exception e) { 
            logger.log(Level.SEVERE , " Exception while setting look and feel");
        }
        SwingUtilities.updateComponentTreeUI(this);
    }
    
    public void createAndShowGUI() {

        // Schedule a job for the event-dispatching thread:
        // suppressing the SplashWindow.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	
                add(new MainGuiApp(), BorderLayout.CENTER);
                setLookAndFeel();
                logger.info( "Applet is now displayed");
            	
                splashWindow.setVisible(false);          
                splashWindow.dispose();
            }
        });
    }
}
