package configuration;

import gui.tableView.DefaultTableViewColumn;
import gui.tableView.DefaultTableViewModel;
import gui.tableView.TableView;
import gui.tableView.TableViewColumn;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;


/**
 * This class builds a table which contains the File type SSS, SRS, etc.
 * and the styles as defined in the MESTRA EXCEL configuration sheet
 * @author t0007330
 * @since June 2007
 */

public class ConfigurationTableView extends JPanel {

	final static Logger logger = Logger.getLogger(ConfigurationTableView.class.getName()); 

	private static final long serialVersionUID = -6754921422556063784L;
	private TableView view = null;
	private JScrollPane scroller = null;
	private TableViewColumn[] tableViewColumns = null;
	private DefaultTableViewModel model = null;
	
	
	public ConfigurationTableView(ConfigurationFileXLSReader aConfiguration, String PanelTitle) {
		
        super(new GridBagLayout());
        
		tableViewColumns = initTableViewColumns();
		model = new DefaultTableViewModel(tableViewColumns);
		view = new TableView(model);
        view.setMakeIndex(true);
        //view.setPreferredSize(new Dimension(600,300));
        
		loadDataModel(aConfiguration);
        
        scroller = new JScrollPane(view);
        scroller.createVerticalScrollBar();
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

	public void reload(ConfigurationFileXLSReader aConfiguration) {
		logger.info( "ConfigurationTable View: reload: remove all");
		if (aConfiguration != null) {
			logger.info( "ConfigurationTable View: reload: init columns agains");
			loadDataModel(aConfiguration);
		}
	}
	
	private void loadDataModel(ConfigurationFileXLSReader aConfiguration) {
		
		logger.info( "Configuration Table View: model row count: "+ model.getRowCount());
		model.clear();
				      
		MestraStylesMap mestraStyles = null;
        if (aConfiguration != null) {
        	
        	// any type of MESTRA styles are optional in the configuration file
        	mestraStyles = aConfiguration.getMestraStyles_SSS();
            if (mestraStyles != null) {
                Iterator<MestraStyle> Iter1 = mestraStyles.getValues();
                while (Iter1.hasNext()) {
                    MestraStyle mestraStyle = Iter1.next();                
                    model.addRow(mestraStyle);
                }
            }
            
            mestraStyles = aConfiguration.getMestraStyles_SRS();
            if (mestraStyles != null) {
                Iterator<MestraStyle> Iter2 = mestraStyles.getValues();
                while (Iter2.hasNext()) {
                    MestraStyle mestraStyle = Iter2.next();                
                    model.addRow(mestraStyle);
                }
            }

            mestraStyles = aConfiguration.getMestraStyles_SDD();
            if (mestraStyles != null) {
                Iterator<MestraStyle> Iter3 = mestraStyles.getValues();
                while (Iter3.hasNext()) {
                    MestraStyle mestraStyle = Iter3.next();                
                    model.addRow(mestraStyle);
                }
            }
            
            mestraStyles = aConfiguration.getMestraStyles_MF();
            if (mestraStyles != null) {
                Iterator<MestraStyle> Iter4 = mestraStyles.getValues();
                while (Iter4.hasNext()) {
                    MestraStyle mestraStyle = Iter4.next();                
                    model.addRow(mestraStyle);
                }
            }
            
            mestraStyles = aConfiguration.getMestraStyles_TS();
            if (mestraStyles != null) {
                Iterator<MestraStyle> Iter5 = mestraStyles.getValues();
                while (Iter5.hasNext()) {
                    MestraStyle mestraStyle = Iter5.next();                
                    model.addRow(mestraStyle);
                }
            }
        }
	}
	
	private TableViewColumn[] initTableViewColumns() {
        try {
            TableViewColumn[] BaseColumns =
            {
            		new DefaultTableViewColumn(
                            "File Type", 
                            MestraStyle.class.getDeclaredMethod("getMestraFileType", (Class[]) null ),
                            MestraStyle.class.getDeclaredMethod("setMestraFileType", new Class[] {String.class})),
                            
                    new DefaultTableViewColumn(
                            "Header",
                            MestraStyle.class.getDeclaredMethod("getMestraStyleHeader", (Class[])null),
                            MestraStyle.class.getDeclaredMethod("setMestraStyleHeader", new Class[] {String.class})),
                            
                    new DefaultTableViewColumn(
                            "Mestra Style",
                            MestraStyle.class.getDeclaredMethod("getMestraStyle", (Class[])null),
                            MestraStyle.class.getDeclaredMethod("setMestraStyle", new Class[] {String.class})) ,
                            
                    new DefaultTableViewColumn(
                            "Display",
                            MestraStyle.class.getDeclaredMethod("getDisplayMestraStyle", (Class[])null),
                            MestraStyle.class.getDeclaredMethod("setDisplayMestraStyle", new Class[] {boolean.class}))  
                            
            };
            return BaseColumns;
        }
        catch (Exception e) {
            e.printStackTrace();
            logger.log(Level.SEVERE , "ConfigurationTableView: error while building the table " + e.getLocalizedMessage());
        }
        return null;
	}
	
}
