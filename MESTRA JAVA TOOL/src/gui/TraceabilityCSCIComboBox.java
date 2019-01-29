package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import configuration.ConfigurationFileBaseReader;
import configuration.KnownCSCIs;

public class TraceabilityCSCIComboBox extends JPanel implements ActionListener {

	final static Logger logger = Logger.getLogger(TraceabilityCSCIComboBox.class.getName()); 

	/**
	 * 
	 */
	private static final long serialVersionUID = -8475143975899512592L;

	private String SelectedTraceabilityCSCI = null;
	private TraceabilityTab traceabilityTab = null;
	private JComboBox<String> comboBox = null;

	public String getSelectedTraceabilityCSCI() {
		return this.SelectedTraceabilityCSCI;
	}

	public void reload() {

		ConfigurationFileBaseReader aConfiguration = traceabilityTab.getConfiguration();
		if (aConfiguration == null) {
			//  Create the combo box, select the item at index 0.
			logger.log(Level.SEVERE , "Traceability CSCI Combo Box: Configuration is null!");
			//String[] strTraceabilityCSCI = { "ODS", "FDP", "DPR" };
			this.comboBox.addItem("ODS");
		}
		else {
			this.comboBox.removeAllItems();
			KnownCSCIs knownCSCIs = aConfiguration.getKnown_CSCIs();
			if (knownCSCIs == null) {
				//  Create the combo box, select the item at index 0.
				logger.log(Level.SEVERE , "Traceability CSCI Combo Box: known CSCIs is null!");
				//String[] strTraceabilityCSCI = { "ODS", "FDP", "DPR" };
				this.comboBox.addItem("ODS");
			} else  {
				Iterator<String> Iter = knownCSCIs.iterator();
				while (Iter.hasNext()) {
					this.comboBox.addItem(Iter.next());
				}
			}
		}

		// Indices start at 0.
		this.comboBox.setSelectedIndex(0);
		this.SelectedTraceabilityCSCI = (String)this.comboBox.getSelectedItem();
	}

	public TraceabilityCSCIComboBox(TraceabilityTab aTraceabilityTab) {

		super(new BorderLayout());
		this.traceabilityTab = aTraceabilityTab;

		this.comboBox = null;
		// this has to be in-line with the list of CSCI extracted from the configuration file
		ConfigurationFileBaseReader aConfiguration = traceabilityTab.getConfiguration();
		if (aConfiguration == null) {
			//  Create the combo box, select the item at index 0.
			String[] strTraceabilityCSCI = { "ODS", "FDP", "DPR" };
			this.comboBox = new JComboBox<String>(strTraceabilityCSCI);
		}
		else {
			KnownCSCIs knownCSCIs = aConfiguration.getKnown_CSCIs();
			if (knownCSCIs == null) {
				//  Create the combo box, select the item at index 0.
				String[] strTraceabilityCSCI = { "ODS", "FDP", "DPR" };
				this.comboBox = new JComboBox<String>(strTraceabilityCSCI);
			} else {
				this.comboBox = new JComboBox ( knownCSCIs.getKnownCSCIs().toArray() );
			}
		}
		this.comboBox.setBackground(Color.green);
		this.comboBox.setFont(new Font("Arial", Font.BOLD, 12));

		// Indices start at 0.
		this.comboBox.setSelectedIndex(0);

		this.SelectedTraceabilityCSCI = (String)comboBox.getSelectedItem();
		this.comboBox.addActionListener(this);

		//Lay out the demo.
		this.add(this.comboBox, BorderLayout.PAGE_START);
		this.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
	}

	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void actionPerformed(ActionEvent e) {

		//logger.info( "Traceability CSCI Combo Box: action performed");
		if (e.getSource() instanceof JComboBox) {
			this.comboBox = (JComboBox<String>)e.getSource();
			SelectedTraceabilityCSCI = (String)this.comboBox.getSelectedItem();
			//logger.info( "Traceability CSCI ComboBox: Selected Traceability CSCI is: "+SelectedTraceabilityCSCI);
		}


	}

	public void setEnabled(boolean trueFalse) {
		//logger.info( "Traceability CSCI Combo Box: Set Enabled: "+trueFalse);
		this.comboBox.setEnabled(trueFalse);
	}

	public void setSelectedTraceabilityCSCI(String csci) {
		this.SelectedTraceabilityCSCI = csci;
		this.comboBox.setSelectedItem(csci);

	}

}
