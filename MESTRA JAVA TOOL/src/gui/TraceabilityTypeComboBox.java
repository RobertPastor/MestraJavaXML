package gui;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import mestra.MestraFileType.MestraFileTypeEnum;
import mestra.MestraScenario;

/**
 * @author: Robert Pastor
 * @since : May 2007
 * this class manages a Combo Box 
 * which allows to change the type of traceability to run.
 */
public class TraceabilityTypeComboBox extends JPanel implements ActionListener {

	final static Logger logger = Logger.getLogger(TraceabilityTypeComboBox.class.getName()); 

	private static final long serialVersionUID = 5313709574783343617L;

	private String SelectedTraceabilityType = "";
	private TraceabilityTab traceabilityTab = null;
	private MestraScenario mestraScenario = null;
	private JComboBox<String> comboBox = null;
	
	private JDropTablePanel jUpStreamDropPanel = null;
	private JDropTablePanel jDownStreamDropPanel = null;

	public String getSelectedTraceabilityType() {
		return SelectedTraceabilityType;
	}

	public TraceabilityTypeComboBox(TraceabilityTab traceabilityTab) {

		super(new BorderLayout());
		this.traceabilityTab = traceabilityTab;

		this.mestraScenario = new MestraScenario();

		//Create the COMBO box, select the item at index 0.
		this.comboBox = new JComboBox<String>(this.mestraScenario.getStrTraceabilityTypes());
		this.comboBox.setBackground(Color.gray);
		this.comboBox.setFont(new Font("Arial", Font.BOLD, 12));

		// set the MESTRA scenario
		this.mestraScenario.setMestraScenario(MestraScenario.MestraScenarioEnum.trace_SSS_SRS);

		// Indices start at 0.
		this.comboBox.setSelectedIndex(0);
		this.SelectedTraceabilityType = (String)this.comboBox.getSelectedItem();
		this.comboBox.addActionListener(this);

		//Lay out the demo.
		this.add(this.comboBox, BorderLayout.PAGE_START);
		this.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
	}

	@SuppressWarnings("unchecked")
	public void actionPerformed(ActionEvent e) {

		logger.info( "Traceability type Combo Box: action performed");
		if (e.getSource() instanceof JComboBox) {
			this.comboBox = (JComboBox<String>)e.getSource();
			this.SelectedTraceabilityType = (String)this.comboBox.getSelectedItem();

			logger.info( "Traceability type ComboBox: Selected Traceability type is: "+ this.SelectedTraceabilityType);
			mestraScenario.setMestraScenario(this.SelectedTraceabilityType);
			traceabilityTab.updateTraceabilityType(this.SelectedTraceabilityType);
			// notify the changes to the drop panel=> changes their background images
			notifyChanges();
						
		}

	}
	
	private void notifyChanges() {
		
		switch (MestraScenario.getMestraScenarioFromString(this.SelectedTraceabilityType)) {
		case trace_SSS_SRS:
			this.jUpStreamDropPanel.notifyChanges(MestraFileTypeEnum.SSS);
			this.jDownStreamDropPanel.notifyChanges(MestraFileTypeEnum.SRS);
			break;
		case trace_SRS_SDD:
			this.jUpStreamDropPanel.notifyChanges(MestraFileTypeEnum.SRS);
			this.jDownStreamDropPanel.notifyChanges(MestraFileTypeEnum.SDD);
			break;
		case trace_SRS_MF:
			this.jUpStreamDropPanel.notifyChanges(MestraFileTypeEnum.SRS);
			this.jDownStreamDropPanel.notifyChanges(MestraFileTypeEnum.MF);
			break;
		case trace_SRS_TS:
			this.jUpStreamDropPanel.notifyChanges(MestraFileTypeEnum.SRS);
			this.jDownStreamDropPanel.notifyChanges(MestraFileTypeEnum.TS);
			break;
		default:
			break;
		}
		
	}

	public MestraScenario getMestraScenario() {
		logger.info( "Traceability type Combo Box: scenario is: "+SelectedTraceabilityType);
		return mestraScenario;
	}

	public void notifyChangesTo(JDropTablePanel jUpStreamDropTable,
			JDropTablePanel jDownStreamDropTable) {
		// TODO Auto-generated method stub
		//System.out.println("notify changes ");
		this.jUpStreamDropPanel = jUpStreamDropTable;
		this.jDownStreamDropPanel = jDownStreamDropTable;
		
		// notify the changes to the drop panel=> changes their background images
		notifyChanges();
	}
	
}
