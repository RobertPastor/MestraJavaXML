package configuration;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.border.TitledBorder;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 * this class is responsible for building a Panel with a Tree.<br>
 * The tree will display the elements as found in the XML configuration file.<br>
 * @author t0007330- Robert PASTOR
 * @since July 2016
 *
 */
public class ConfigurationTreeView extends JPanel {

	/**
	 * serial version
	 */
	private static final long serialVersionUID = 4202071644282694502L;

	final static Logger logger = Logger.getLogger(ConfigurationTreeView.class.getName());

	private JTree jTree = null;
	private JScrollPane scroller = null;
	private String configurationXMLfilePath = "";


	public ConfigurationTreeView(String configurationXMLfilePath, String PanelTitle) {
		
		super(new GridBagLayout());
		this.configurationXMLfilePath = configurationXMLfilePath;
		
		logger.info(" constructor : " + this.configurationXMLfilePath );
		VSX2 parser = new VSX2();

		this.jTree = new JTree(parser.parse(configurationXMLfilePath));
		//this.jTree.setBackground(Color.cyan);

		// Steal the default icons from the default renderer...
		DefaultTreeCellRenderer rend1 = new DefaultTreeCellRenderer();
		IconAndTipRenderer rend2 = new IconAndTipRenderer(
				rend1.getOpenIcon(),
				rend1.getClosedIcon(),
				rend1.getLeafIcon());
		this.jTree.setCellRenderer(rend2);
		ToolTipManager.sharedInstance().registerComponent(this.jTree);

		this.scroller = new JScrollPane(this.jTree);
		this.scroller.createVerticalScrollBar();

		this.add(this.scroller);
		this.setSize(300,400);
		this.setVisible(true);

		//scroller.setPreferredSize(new Dimension(600, 300));

		GridBagConstraints c = new GridBagConstraints();   
		c.fill = GridBagConstraints.BOTH;
		c.gridx=0;
		c.gridy=0;
		c.weightx=1;
		c.weighty=2;

		add(scroller,c);

		TitledBorder title;
		title = BorderFactory.createTitledBorder(PanelTitle);
		this.setBorder(title);
		this.setPreferredSize(new Dimension(600,600));
	}

	
	public String getConfigurationXMLfilePath() {
		return this.configurationXMLfilePath;
	}
	
	public void reload(ConfigurationFileXMLReader aConfiguration) {
		logger.info( "Configuration Tree View: reload: remove all");
		this.jTree.removeAll();
		if (aConfiguration != null) {
			logger.info( "Configuration Tree View: reload: init Tree again.");
			VSX2 parser = new VSX2();
			this.jTree.setModel(parser.parse(this.configurationXMLfilePath ));
		}
	}
}
