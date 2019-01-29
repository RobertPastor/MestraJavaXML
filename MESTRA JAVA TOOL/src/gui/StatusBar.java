package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.BoundedRangeModel;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

/**
 * This class implements a Swing Status Bar.
 * @author t0007330 - Robert PASTOR
 * @since 2007
 *
 */
public class StatusBar extends JComponent {

	final static Logger logger = Logger.getLogger(StatusBar.class.getName()); 

	private static final long serialVersionUID = 1696597433259221588L;

	/**
	 * Progress of an operation.
	 * 
	 */
	private BoundedRangeModel progress = null;
	private MainGuiApp mainGuiApp = null;

	private int progressBarValue = 0;
	private int progressBarMaxValue = 100;

	public void startProgressBarRunner () {
		logger.info( " startProgressBarRunner");
		ProgressBarRunner progressBarRunner = new ProgressBarRunner();
		progressBarRunner.start();
	}

	class ProgressBarRunner implements Runnable {

		private Thread progressBarRunner = null;

		ProgressBarRunner() {
			logger.info( "StatusBar: instantiate the Thread");
			progressBarRunner = new Thread();    
		}

		public void start() {
			progressBarRunner.start();
			progressBarValue = 0;
		}

		public void run() {
			logger.info( "StatusBar: start Thread");
			while(progressBarValue <= progressBarMaxValue){
				
				try {
					Thread.sleep(10);
					logger.info( "StatusBar: set Value: "+progressBarValue);
					progress.setValue(progressBarValue);
				}
				catch(Exception e){}
			}
		}
	}


	class InternalClock extends JLabel implements Runnable
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 9192679325447513789L;
		private Thread updateHourThread = null;
		private Date CurrentTime = null;
		//private Font fonteHeure = new Font("Helvetica",Font.BOLD,12);
		//private Font fonteJour = new Font ("Helvetica",Font.PLAIN,8);
		private DateFormat formatHeure = null;
		//private DateFormat formatJour = null;

		InternalClock(Locale choixDeLaLocale)
		{
			//setPreferredSize(new Dimension(80,40));
			formatHeure = DateFormat.getTimeInstance(DateFormat.MEDIUM,choixDeLaLocale);
			//formatJour = DateFormat.getDateInstance(DateFormat.LONG,choixDeLaLocale);

			updateHourThread = new Thread(this);
			updateHourThread.start();
		}

		public void setHour()
		{
			CurrentTime = Calendar.getInstance().getTime();
			this.setText(formatHeure.format(CurrentTime));
		}

		public void run()
		{
			while (true)
			{
				setHour();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException uneException) {}
			}
		}
	}


	/**
	 *  component that will contain the clock - time
	 */
	private final InternalClock clock = new InternalClock(Locale.ENGLISH);

	/**
	 *  Component where the messages are written
	 */
	private JLabel message = null;	

	/**
	 * Configure the text zone.
	 */
	private static void config(final JLabel label)  {
		label.setOpaque(true);
		label.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLoweredBevelBorder(),
				BorderFactory.createEmptyBorder(0/*top*/, 6/*left*/, 0/*bottom*/,0/*right*/)));
	}


	/**
	 * Construct a new status bar.
	 */
	public StatusBar(MainGuiApp pMainGuiApp) {

		this.mainGuiApp = pMainGuiApp;
		logger.info( "StatusBar Empty constructor");
		logger.info(Locale.getDefault().getDisplayLanguage());
		logger.info(Locale.getDefault().getDisplayName());
		logger.info(Locale.getDefault().getLanguage());
		setLayout(new GridBagLayout());
		final JProgressBar progressBar = new JProgressBar();
		progressBar.setBackground(Color.yellow);
		
		message = new JLabel();
		message.setBackground(Color.green);
		
		final GridBagConstraints c = new GridBagConstraints();

		c.gridy=0;
		c.fill = GridBagConstraints.BOTH;

		c.gridx=0; 
		c.weightx=1; 
		add(this.message, c);

		c.gridx=1; 
		c.weightx=1; 
		add(progressBar, c);

		c.gridx=3;              
		add(this.clock, c);

		// configuration of the different parts of the Status Bar
		config(message);
		config(clock);
		
		final Dimension messageAreaSize = this.message.getMaximumSize();
		messageAreaSize.width = 550;
		this.message.setPreferredSize(messageAreaSize);

		final Dimension clockSize = this.clock.getPreferredSize();
		clockSize.width = 100;
		this.clock.setPreferredSize(clockSize);

		progressBar.setBorder(BorderFactory.createLoweredBevelBorder());
		this.progress = progressBar.getModel();
	}

	public void setMessage(String aMessage) {
		message.setText(aMessage);
		mainGuiApp.writeDebugMessage(aMessage);
	}

	public void setProgressBarMaxValue(int maxValue) {
		progressBarMaxValue = maxValue;
		progress.setMaximum(maxValue);
	}

	public void setProgressBarValue(int Value) {
		progressBarValue = Value;
		progress.setValue(Value);
	}
}
