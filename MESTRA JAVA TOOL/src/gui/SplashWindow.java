package gui;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

/**
 * this class displays a splash screen.
 * @author Pastor
 * @since June 2007
 *
 */
public class SplashWindow extends JFrame {     


	private static final long serialVersionUID = -9137583838458479685L;

	private JProgressBar progressBar = null;   
	private int maxValue = 0;   

	public static URL getURL(String jpgFileName) {
		java.net.URL base = SplashWindow.class.getResource("./"+jpgFileName);
		return base;
	}

	public SplashWindow(String imageFileName, int intProgressMaxValue) { 

		super();
		this.setTitle("Java Mestra Tool");
		// 	initialise progress value when to close the screen      
		this.maxValue = intProgressMaxValue;                    
		// add the progress bar    
		progressBar = new JProgressBar(0, intProgressMaxValue);   
		getContentPane().add(progressBar, BorderLayout.SOUTH);                 
		// add a label with the icon       
		ClassLoader classLoader = this.getClass().getClassLoader();
		//System.out.println(imageFileName);
		
		JLabel label = new JLabel(new ImageIcon(classLoader.getResource(imageFileName)), JLabel.CENTER);
		//label.setText("Java Mestra Tool");
		// add the label to the panel     
		getContentPane().add(label, BorderLayout.NORTH);   

		// center the splash screen    
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();     
		Dimension labelSize = label.getPreferredSize();   
		setLocation(screenSize.width / 2 - (labelSize.width / 2),     
				screenSize.height / 2 - (labelSize.height / 2));

		// hide the splash screen when some one clicks on it   
		addMouseListener(new MouseAdapter() {     
			public void mousePressed(MouseEvent e) {      
				setVisible(false);               
				dispose();           
			}      
		});     
		// display the splash screen  
		pack();
		setVisible(true);   
	}

	// change the value of the progress bar   
	public void setProgressValue(int value) {

		progressBar.setValue(value);
		this.paint(this.getGraphics());

		// when getting to the max value
		// close the splash screen and launch the thread   
		if (value >= maxValue) {           
			try {               
				SwingUtilities.invokeAndWait(closerRunner);  
			}
			catch (InterruptedException e) { 
				e.printStackTrace();           
			} catch (InvocationTargetException e) {         
				e.printStackTrace();            
			}       
		}  
	}

	// thread to close the splash screen   
	final Runnable closerRunner = new Runnable() {   
		public void run() {  
			setVisible(false);          
			dispose();        
		}  
	};
}
