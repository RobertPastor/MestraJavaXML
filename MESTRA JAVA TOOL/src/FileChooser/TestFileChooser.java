package FileChooser;

import gui.FileChooserFileFilter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class TestFileChooser extends JPanel implements ActionListener {

	final static Logger logger = Logger.getLogger(TestFileChooser.class.getName()); 

	private static final long serialVersionUID = -3418126678788847957L;
	/**
     * @param args
     */
    
    private JButton fcButton = null;
    private JFileChooser fc = null;
    private JFrame jFCframe = null;
    private FileChooserFixer fileChooserFixer = null;

    public TestFileChooser() {
        
        logger.info("panel displayed");
        fcButton = new JButton();
        fcButton.addActionListener(this);
        this.add(fcButton);
        
    }
    
    public void actionPerformed(ActionEvent e) {

    	if (e.getSource() == fcButton) {

    		logger.info("fc Button pressed");
            
            jFCframe = new JFrame("Configuration file chooser");
            jFCframe.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

            fc = new JFileChooser("d:/mestra tool");
            fc.setMultiSelectionEnabled(false);
            fc.setDragEnabled(true);
            fc.setControlButtonsAreShown(false);
            //((Component)fc).setPreferredSize(new Dimension(600, 400));

            FileChooserFileFilter filter = new FileChooserFileFilter(new String("doc"), "Microsoft Word documents");
            fc.addChoosableFileFilter(filter);
            fc.setFileFilter(filter);
            
            fileChooserFixer = new FileChooserFixer(fc,this);
            

            // Applet specific bug with Java 1.4 
            // need to call getContentPane before adding the file chooser
            jFCframe.getContentPane().add(fc);
            
            jFCframe.pack();
            jFCframe.setVisible(true);
            
        }
        else {
            if (e.getSource() == fileChooserFixer) {
                logger.log(Level.WARNING , "source == fileChooserFixer");
            }
            if (e.getSource() == fc) {
                logger.log(Level.WARNING , "source == fileChooser");
            }
        }
    }

}
