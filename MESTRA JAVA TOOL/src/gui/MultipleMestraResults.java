package gui;


import gui.tableView.DefaultTableViewColumn;
import gui.tableView.DefaultTableViewModel;
import gui.tableView.TableView;
import gui.tableView.TableViewColumn;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import mestra.CSCI_SSSRequirementAllocationList;
import mestra.IdentifierList;
import mestra.MestraBase;
import mestra.MestraFiles;
import mestra.MestraIdentifier;
import mestra.MestraMF;
import mestra.MestraMF_Collection;
import mestra.MestraRevision;
import mestra.MestraSDD;
import mestra.MestraSDD_Collection;
import mestra.MestraSRS;
import mestra.MestraSRS_Collection;
import mestra.MestraSSS;
import mestra.MestraSSS_Collection;
import mestra.MestraScenario;
import mestra.MestraTS;
import mestra.MestraTS_Collection;
import mestra.MestraTitle;
import configuration.ConfigurationFileBaseReader;
import configuration.MestraStyle;
import configuration.MestraStylesMap;
import configuration.MethodLevelSafety;

public class MultipleMestraResults {

    public void viewResults(MestraFiles mestraFiles, ConfigurationFileBaseReader configuration) {

        final TableView view = initView (mestraFiles, configuration);
        createFrame(view);
    }

    private void createFrame(TableView view) {

        // create a floating frame to display the results
        JFrame frame = new JFrame("Trace all");

        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(new JScrollPane(view), BorderLayout.CENTER);
        //((Component)frame).setPreferredSize(new Dimension(600,400));
        frame.pack();
        frame.setVisible(true);
    }
    
    /**
     * All the MestraFiles have the same type
     * @param mestraFiles
     * @param configuration
     * @param styleIndex
     * @return
     */
    private String getColumnHeader (MestraFiles mestraFiles,
            ConfigurationFileBaseReader configuration,
            int styleIndex) {
        
        MestraStylesMap mestraStyles = configuration.getMestraStyles(mestraFiles.getFirstFileType());
        return mestraStyles.getStyleHeader(styleIndex);
    }
    
    /**
     * 
     * @param mestraFiles
     * @param configuration
     * @return
     */
    private TableView initColumnHeaders(MestraFiles mestraFiles, ConfigurationFileBaseReader configuration) {

        DefaultTableViewModel model = null;  
        try {
            TableViewColumn[] BaseColumns =
            {
                    new DefaultTableViewColumn(
                            "Id", 
                            new Comparator<Object>() {
                                public int compare(final Object o1, final Object o2)
                                {
                                    final MestraIdentifier p1 = (MestraIdentifier) o1;
                                    final MestraIdentifier p2 = (MestraIdentifier) o2;
                                    if (p1.getUniqueId() < p2.getUniqueId())
                                        return -1;
                                    else if (p1.getUniqueId() == p2.getUniqueId())
                                        return 0;
                                    else
                                        return 1;
                                }
                            },
                            MestraIdentifier.class.getDeclaredMethod("getStrUniqueId", (Class[])null)), 

                    new DefaultTableViewColumn(
                            "File",
                            new Comparator<Object>() {
                                public int compare(final Object o1, final Object o2) {
                                    final MestraBase p1 = (MestraBase) o1;
                                    final MestraBase p2 = (MestraBase) o2;
                                    return (p1.getShortFileName().compareTo(p2.getShortFileName()));
                                }
                            },
                            MestraBase.class.getDeclaredMethod("getShortFileName", (Class[])null)), 

                    new DefaultTableViewColumn(
                                    getColumnHeader(mestraFiles,configuration,MestraBase.MestraStyleIdentifier),
                                    new Comparator<Object>() {
                                        public int compare(final Object o1, final Object o2) {
                                            final MestraIdentifier p1 = (MestraIdentifier) o1;
                                            final MestraIdentifier p2 = (MestraIdentifier) o2;
                                            return (p1.getIdentifier().compareTo(p2.getIdentifier()));
                                        }
                                    },
                            MestraBase.class.getDeclaredMethod("getMestraIdentifierClone", (Class[])null)),  


                            new DefaultTableViewColumn(
                                    "Allocation",
                                    new Comparator<Object>() {
                                                        public int compare(final Object o1, final Object o2) {
                                                            final MestraSSS p1 = (MestraSSS) o1;
                                                            final MestraSSS p2 = (MestraSSS) o2;
                                                            return (p1.getRequirementAllocation().compareTo(p2.getRequirementAllocation()));
                                                        }
                                                    },
                            MestraSSS.class.getDeclaredMethod("getRequirementAllocation", (Class[])null))

            };
            
            ArrayList<TableViewColumn> arrayList = new ArrayList<TableViewColumn>(Arrays.asList(BaseColumns));

            // add all mestra tags found in the file that are defined in the configuration file 
            // except the Mandatory one => Identifier and the traceability one at the last position
            // loop through the configuration
            MestraStylesMap mestraStylesMap = configuration.getMestraStyles_SSS();
            Iterator<MestraStyle> Iter = mestraStylesMap.getValues();
            while (Iter.hasNext()) {
            	MestraStyle mestraStyle = Iter.next();
            	if ((mestraStyle.isMandatory() == false) && (mestraStyle.isTraceability() == false)){
            		
            		arrayList.add(new DefaultTableViewColumn(mestraStyle.getMestraStyleHeader()));
            		
            	}
            }
            
            model = loadDataModel(BaseColumns,mestraFiles);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        final TableView view = new TableView(model);
        return view;
    }

    private DefaultTableViewModel loadDataModel(TableViewColumn[] BaseColumns,
            MestraFiles mestraFiles) {
        
        DefaultTableViewModel model = null;
        switch (mestraFiles.getFirstFileType()) {
        
        case SSS:
            MestraSSS_Collection mestraSSStags = mestraFiles.getMestraSSStags();
            ArrayList<MestraSSS> mestraSSSCol = mestraSSStags.getMestraSSS_Collection();          
            model = new DefaultTableViewModel(BaseColumns, mestraSSSCol);
            break;
            
        case SRS:
            MestraSRS_Collection mestraSRStags = mestraFiles.getMestraSRStags();
            ArrayList<MestraSRS> mestraSRSCol = mestraSRStags.getMestraSRS_Collection();
            model = new DefaultTableViewModel(BaseColumns, mestraSRSCol);
            break;
            
        case SDD:
            MestraSDD_Collection mestraSDDtags = mestraFiles.getMestraSDDtags();
            ArrayList<MestraSDD> mestraSDDCol = mestraSDDtags.getMestraSDD_Collection();
            model = new DefaultTableViewModel(BaseColumns, mestraSDDCol);
            break;
            
        case MF:
            MestraMF_Collection mestraMFtags = mestraFiles.getMestraMFtags();
            ArrayList<MestraMF> mestraMFCol = mestraMFtags.getMestraMF_Collection();
            model = new DefaultTableViewModel(BaseColumns, mestraMFCol);
            break;
            
        case TS:
            MestraTS_Collection mestraTStags = mestraFiles.getMestraTStags();
            ArrayList<MestraTS> mestraTSCol = mestraTStags.getMestraTS_Collection();
            model = new DefaultTableViewModel(BaseColumns, mestraTSCol);
            break;
        }
        return model;
    }
    
    /**
     * 
     * @param mestraFiles
     * @param configuration
     * @return
     */
    private TableView initView(MestraFiles mestraFiles,
            ConfigurationFileBaseReader configuration) {

        TableView view = null;

        MestraScenario mestraScenario = mestraFiles.getMestraScenario();
        switch (mestraScenario.getMestraScenarioEnum()) {
        case trace_All:
            view = initColumnHeaders(mestraFiles,configuration);
		default:
			break;
        }

        // we use a specific class to render ERRORs and WARNINGs using colors in the table view
                
        view.setDefaultRenderer(MestraIdentifier.class,new ColoredTableCellRenderer());
        view.setDefaultRenderer(MestraBase.class,new ColoredTableCellRenderer());
        view.setDefaultRenderer(MestraSSS.class,new ColoredTableCellRenderer());
        view.setDefaultRenderer(MestraTitle.class,new ColoredTableCellRenderer());
        view.setDefaultRenderer(MestraRevision.class,new ColoredTableCellRenderer());
        view.setDefaultRenderer(MethodLevelSafety.class,new ColoredTableCellRenderer());
        view.setDefaultRenderer(CSCI_SSSRequirementAllocationList.class,new ColoredTableCellRenderer());
        view.setDefaultRenderer(IdentifierList.class,new ColoredTableCellRenderer());

        view.setMakeIndex(true);
        return view;
    }
}
