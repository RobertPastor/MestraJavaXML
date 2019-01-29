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
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import mestra.CSCI_SSSRequirementAllocationList;
import mestra.IdentifierList;
import mestra.MestraBase;
import mestra.MestraFile;
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
import mestra.MestraTS;
import mestra.MestraTS_Collection;
import mestra.MestraTitle;
import configuration.ConfigurationFileBaseReader;
import configuration.MestraStyle;
import configuration.MestraStylesMap;
import configuration.MethodLevelSafety;

/**
 * 
 * This class manages the display of the MESTRA check results in a table view inside a frame.
 * @author Robert Pastor
 * @since May 2007
 */

public class MestraChecksResults {

	final static Logger logger = Logger.getLogger(MestraChecksResults.class.getName()); 

	public void viewResults(MestraFile mestraFile, ConfigurationFileBaseReader configuration) {

		final TableView view = InitBaseColumns(mestraFile, configuration);
		createFrame (mestraFile, view);
	}

	private void createFrame(MestraFile mestraFile, TableView view) {
		// create a floating frame to display the results
		JFrame frame = new JFrame(mestraFile.getLongFileName());

		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(new JScrollPane(view), BorderLayout.CENTER);
		//((Component)frame).setPreferredSize(new Dimension(600,400));
		frame.pack();
		frame.setVisible(true);

	}

	private DefaultTableViewModel loadDataModel(TableViewColumn[] BaseColumns,
			MestraFile mestraFile) {

		DefaultTableViewModel model = null;
		switch (mestraFile.getFileType().getMestraFileType()) {

		case SSS:
			MestraSSS_Collection mestraSSStags = mestraFile.getMestraSSStags();
			ArrayList<MestraSSS> mestraSSSCol = mestraSSStags.getMestraSSS_Collection();          
			model = new DefaultTableViewModel(BaseColumns, mestraSSSCol);
			break;

		case SRS:
			MestraSRS_Collection mestraSRStags = mestraFile.getMestraSRStags();
			ArrayList<MestraSRS> mestraSRSCol = mestraSRStags.getMestraSRS_Collection();
			model = new DefaultTableViewModel(BaseColumns, mestraSRSCol);
			break;

		case SDD:
			MestraSDD_Collection mestraSDDtags = mestraFile.getMestraSDDtags();
			ArrayList<MestraSDD> mestraSDDCol = mestraSDDtags.getMestraSDD_Collection();
			model = new DefaultTableViewModel(BaseColumns, mestraSDDCol);
			break;

		case MF:
			MestraMF_Collection mestraMFtags = mestraFile.getMestraMFtags();
			ArrayList<MestraMF> mestraMFCol = mestraMFtags.getMestraMF_Collection();
			model = new DefaultTableViewModel(BaseColumns, mestraMFCol);
			break;

		case TS:
			MestraTS_Collection mestraTStags = mestraFile.getMestraTStags();
			ArrayList<MestraTS> mestraTSCol = mestraTStags.getMestraTS_Collection();
			model = new DefaultTableViewModel(BaseColumns, mestraTSCol);
			break;
		}
		return model;
	}


	private String getMainMandatoryColumnHeader(MestraFile mestraFile,
			ConfigurationFileBaseReader configuration) {

		MestraStylesMap mestraStyles = configuration.getMestraStyles(mestraFile.getFileType());
		return mestraStyles.getMainMandatoryStyleHeader();
	}


	private String getTraceabilityColumnHeader(MestraFile mestraFile, ConfigurationFileBaseReader configuration) {
		MestraStylesMap mestraStyles = configuration.getMestraStyles(mestraFile.getFileType());
		return mestraStyles.getTraceabilityStyleHeader();
	}

	private TableView InitBaseColumns(MestraFile mestraFile,
			ConfigurationFileBaseReader configuration) {

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
									getMainMandatoryColumnHeader(mestraFile, configuration) ,
									new Comparator<Object>() {
										public int compare(final Object o1, final Object o2)
										{
											final MestraIdentifier p1 = (MestraIdentifier) o1;
											final MestraIdentifier p2 = (MestraIdentifier) o2;
											return (p1.getIdentifier().compareTo(p2.getIdentifier()));
										}
									},
									MestraBase.class.getDeclaredMethod("getMestraIdentifierClone", (Class[])null), 
									MestraBase.class.getDeclaredMethod("setMestraIdentifierClone", new Class[] {String.class})),

				};
			ArrayList<TableViewColumn> arrayList = new ArrayList<TableViewColumn>(Arrays.asList(BaseColumns));

			// add all mestra tags found in the file that are defined in the configuration file 
			// except the Mandatory one => Identifier and the traceability one at the last position
			// loop through the configuration
			MestraStylesMap mestraStylesMap = configuration.getMestraStyles(mestraFile.getFileType());
			Iterator<MestraStyle> Iter = mestraStylesMap.getValues();

			while (Iter.hasNext()) {
				final MestraStyle mestraStyle = Iter.next();
				if ((mestraStyle.isMandatory() == false) && (mestraStyle.isTraceability() == false)){


					if (mestraStyle.getDisplayMestraStyle()) {
						if (mestraStyle.hasMethodLevelSafetyAttribute()) {
							// 29th July 2016 - Robert - changed to get Mestra Value with Mestra Style header as argument
							arrayList.add(new DefaultTableViewColumn(
									mestraStyle.getMestraStyleHeader(),
									new Comparator<Object>() {
										public int compare(final Object o1, final Object o2) 
										{
											final MestraBase p1 = (MestraBase) o1;
											final MestraBase p2 = (MestraBase) o2;
											String str1 = p1.getMestraValue(mestraStyle.getMestraStyleHeader());
											String str2 = p2.getMestraValue(mestraStyle.getMestraStyleHeader());
											return (str1.compareTo(str2));
										}
									},
		                            MestraBase.class.getDeclaredMethod("getMethodLevelSafety", (Class[])null))
									);

						} else {
							// 29th July 2016 - Robert - changed to get Mestra Value with Mestra Style header as argument
							arrayList.add(new DefaultTableViewColumn(
									mestraStyle.getMestraStyleHeader(),
									new Comparator<Object>() {
										public int compare(final Object o1, final Object o2) 
										{
											final MestraBase p1 = (MestraBase) o1;
											final MestraBase p2 = (MestraBase) o2;
											String str1 = p1.getMestraValue(mestraStyle.getMestraStyleHeader());
											String str2 = p2.getMestraValue(mestraStyle.getMestraStyleHeader());
											return (str1.compareTo(str2));
										}
									},
									MestraBase.class.getDeclaredMethod("getMestraValue", String.class ),
									mestraStyle.getMestraStyleHeader()
									) );
						}
					}
				}
			}

			// transform to an ArrayList in order to add what's missing
			// i.e. what is specific to each class SSS, SRS, MF, SDD, TS and 
			// that is not in the MESTRA BASE class

			switch (mestraFile.getFileType().getMestraFileType()) {
			case SSS:
				arrayList.add(new DefaultTableViewColumn(
						getTraceabilityColumnHeader(mestraFile, configuration),
						new Comparator<Object>() {
							public int compare(final Object o1, final Object o2)
							{
								final MestraSSS p1 = (MestraSSS) o1;
								final MestraSSS p2 = (MestraSSS) o2;
								return (p1.getRequirementAllocation().compareTo(p2.getRequirementAllocation()));
							}
						},
						MestraSSS.class.getDeclaredMethod("getRequirementAllocationList", (Class[])null)));
				break;

			case SRS:
				arrayList.add(new DefaultTableViewColumn(
						getTraceabilityColumnHeader(mestraFile, configuration),
						new Comparator<Object>() {
							public int compare(final Object o1, final Object o2)
							{
								final MestraSRS p1 = (MestraSRS) o1;
								final MestraSRS p2 = (MestraSRS) o2;
								return (p1.getSSS_ReferencesList().getIdentifierListString().compareTo(p2.getSSS_ReferencesList().getIdentifierListString()));
							}
						},
						MestraSRS.class.getDeclaredMethod("getSSS_ReferencesList", (Class[])null)));
				break; 

			case SDD:
				arrayList.add(new DefaultTableViewColumn(
						getTraceabilityColumnHeader(mestraFile,configuration),
						new Comparator<Object>() {
							public int compare(final Object o1, final Object o2)
							{
								final MestraSDD p1 = (MestraSDD) o1;
								final MestraSDD p2 = (MestraSDD) o2;
								return (p1.getImplementedSRSrequirements().getIdentifierListString().compareTo(p2.getImplementedSRSrequirements().getIdentifierListString()));
							}
						},
						MestraSDD.class.getDeclaredMethod("getImplementedSRSrequirements", (Class[])null)));
				break;  

			case MF:
				arrayList.add(new DefaultTableViewColumn(
						getTraceabilityColumnHeader(mestraFile,configuration),
						new Comparator<Object>() {
							public int compare(final Object o1, final Object o2)
							{
								final MestraMF p1 = (MestraMF) o1;
								final MestraMF p2 = (MestraMF) o2;
								return (p1.getDesignParts().compareTo(p2.getDesignParts()));
							}
						},
						MestraMF.class.getDeclaredMethod("getDesignParts", (Class[])null)));
				break;               

			case TS:
				arrayList.add(new DefaultTableViewColumn(
						getTraceabilityColumnHeader(mestraFile,configuration),
						new Comparator<Object>() {
							public int compare(final Object o1, final Object o2)
							{
								final MestraTS p1 = (MestraTS) o1;
								final MestraTS p2 = (MestraTS) o2;
								return (p1.getTestedSRSrequirementsList().getIdentifierListString().compareTo(p2.getTestedSRSrequirementsList().getIdentifierListString()));
							}
						},
						MestraTS.class.getDeclaredMethod("getTestedSRSrequirementsList", (Class[])null)));
				break;

			}
			// get back to the table view
			TableViewColumn[] FinalColumns = (TableViewColumn[]) arrayList.toArray(new TableViewColumn[arrayList.size()]);
			model = loadDataModel(FinalColumns,mestraFile);
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		final TableView view = new TableView(model);

		// these are all the classes where we need to highlight with a specific colors
		// some MESTRA rules violations such as duplicated identifier or empty title 
		// or unknown CSCI in the allocation list, etc.
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
