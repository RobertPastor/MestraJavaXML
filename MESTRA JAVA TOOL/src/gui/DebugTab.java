package gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import ToolVersion.MestraToolVersion;


/**
 * The aim of this class is to create a tag which will contain the Debug Messages
 * 
 * @author Pastor Robert
 * @since July 2007
 */
public class DebugTab extends JPanel implements ActionListener {

	final static Logger logger = Logger.getLogger(DebugTab.class.getName()); 

	private static final long serialVersionUID = 1714355560044600760L;

	private JTextArea textArea = null;
	private JButton textAreaClearButton = null;
	
	private void loadJavaMestraToolVersionRationale() {
		MestraToolVersion mestraToolVersion = new MestraToolVersion();
		
        this.textArea.append("------Last Version Rationale-------\n");
		this.textArea.append(mestraToolVersion.getMestraToolRationale()+"\n");
        this.textArea.append("------Last Version-----------------\n");
		this.textArea.append(mestraToolVersion.getMestraToolVersion()+"\n");
		this.textArea.append("-----------------------------------\n");
	}
	
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() instanceof JButton) {
		    this.textArea.setEditable(true);
			this.textArea.selectAll();
			this.textArea.cut();
            this.textArea.setEditable(false);
		}
		loadJavaMestraToolVersionRationale();
	}
	
	public DebugTab() {
		super(new GridBagLayout());
		//this.setLayout(new GridLayout(2,1));

		// the Panel containing the Text Area
		// -----------------------------------
		
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setFont(new Font("arial",Font.PLAIN,10));
		
        TitledBorder title;
        title = BorderFactory.createTitledBorder("Debug Tab: the following text area contains Debug messages...");
        
        JScrollPane scroller = new JScrollPane(textArea);
        scroller.setBorder(title);
        scroller.setPreferredSize(new Dimension(800, 300));
        
		GridBagConstraints c = new GridBagConstraints();   
        c.fill = GridBagConstraints.BOTH;
        c.gridx=0;
        c.gridy=0;
        c.weightx=1;
        c.weighty=5;
        
        add(scroller,c);
        
        // the Panel containing the button used to clear the text area
        // -----------------------------------
        JPanel jTextAreaClearButtonPanel = new JPanel();
        title = BorderFactory.createTitledBorder("Use the following button to clear the text area");
        jTextAreaClearButtonPanel.setBorder(title);
        
        // the button used to clear the Text Area
        // --------------------------------------
        textAreaClearButton = new JButton("Clear the Text Area");
        textAreaClearButton.setPreferredSize(new Dimension(240,26));
        textAreaClearButton.addActionListener(this);
        
        jTextAreaClearButtonPanel.add(textAreaClearButton);
        
        c.gridx=0;
        c.gridy=1;
        c.weightx=1;
        c.weighty=0;
        add(jTextAreaClearButtonPanel,c);
        // fill in the text area
        loadJavaMestraToolVersionRationale();
	}

	public void appendDebugMessage(String debugLineStr) {
		textArea.append(debugLineStr);
	}
}
