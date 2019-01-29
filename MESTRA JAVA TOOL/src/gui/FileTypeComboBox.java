package gui;

/**
 *  Robert Pastor : May 2007
 *  This class manages a Combo Box allowing to select the file type
 *  analysed during the Mestra Checks
 *  
 *  This combo box is visible inside the Mestra Checks tab
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import mestra.MestraFileType;


public class FileTypeComboBox extends JPanel implements ActionListener {
	/**
	 * 
	 */
	final static Logger logger = Logger.getLogger(FileTypeComboBox.class.getName()); 

	private static final long serialVersionUID = -6344035297670484902L;

	private String SelectedFileType = "";
	private JComboBox<String> comboBox = null;
	private TextAreaWithBackGroundImage textFileDropArea = null;

	public FileTypeComboBox() {
		super(new BorderLayout());

		//Create the combo box
		this.comboBox = new JComboBox<String>(MestraFileType.getStrFileTypes());
		// color it
		this.comboBox.setBackground(Color.yellow);
		this.comboBox.setFont(new Font("Arial", Font.BOLD, 12));
		
		// Indices start at 0; select the item at index 0.
		this.comboBox.setSelectedIndex(0);
		this.SelectedFileType = (String)this.comboBox.getSelectedItem();
		this.comboBox.addActionListener(this);

		//Lay out the demo.
		this.add(this.comboBox, BorderLayout.PAGE_START);
		this.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
	}

	/** 
	 * Listens to the combo box. 
	 * */
	@SuppressWarnings("unchecked")
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof JComboBox) {
			JComboBox<String> cb = (JComboBox<String>)e.getSource();
			this.SelectedFileType = (String)cb.getSelectedItem();
			logger.log(Level.INFO, "Selected file type = " + this.SelectedFileType);
			// notify changes in order for the background image of the text area to change
			this.textFileDropArea.notifyChanges(this.SelectedFileType);
		}
	}

	public String getSelectedFileType() {
		return this.SelectedFileType;
	}

	public void notifyChangesTo(TextAreaWithBackGroundImage textFileDropArea) {
		// this is the text area that is receiving the file paths to analyse => manage a background image below this text area
		this.textFileDropArea = textFileDropArea;
	}

}

