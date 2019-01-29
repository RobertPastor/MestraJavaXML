package gui;

import gui.tableView.DefaultTableViewColumn;
import gui.tableView.DefaultTableViewModel;
import gui.tableView.TableView;
import gui.tableView.TableViewColumn;

import java.awt.BorderLayout;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import mestra.MestraBase;
import mestra.MestraFiles;
import mestra.MestraIdentifier;
import mestra.MestraSRS;
import mestra.MestraSSS;
import mestra.MestraScenario;
import mestra.MestraTitle;
import mestra.Trace_MF_SRS;
import mestra.Trace_MF_SRS_Collection;
import mestra.Trace_SDD_SRS;
import mestra.Trace_SDD_SRS_Collection;
import mestra.Trace_SRS_MF;
import mestra.Trace_SRS_MF_Collection;
import mestra.Trace_SRS_SDD;
import mestra.Trace_SRS_SDD_Collection;
import mestra.Trace_SRS_SSS;
import mestra.Trace_SRS_SSS_Collection;
import mestra.Trace_SRS_TS;
import mestra.Trace_SRS_TS_Collection;
import mestra.Trace_SSS_SRS;
import mestra.Trace_SSS_SRS_Collection;
import mestra.Trace_TS_SRS;
import mestra.Trace_TS_SRS_Collection;
import configuration.ConfigurationFileBaseReader;

public class TraceabilityResults {

	final static Logger logger = Logger.getLogger(TraceabilityResults.class.getName()); 

	private enum TraceUpDownEnum { UpDown, DownUp  }


	public void viewResults(MestraFiles mestraFiles, ConfigurationFileBaseReader configuration) {

		final TableView viewUpDown = initView (mestraFiles, configuration,TraceUpDownEnum.UpDown);
		final TableView viewDownUp = initView (mestraFiles, configuration,TraceUpDownEnum.DownUp);

		createFrame(mestraFiles, viewUpDown, TraceUpDownEnum.UpDown);
		createFrame(mestraFiles, viewDownUp, TraceUpDownEnum.DownUp);
	}

	private void createFrame(MestraFiles mestraFiles,TableView view,TraceUpDownEnum upDown) {

		// create a floating frame to display the results

		String FrameTitle = getFrameTitle(mestraFiles,upDown);

		JFrame frame = new JFrame(FrameTitle);
		frame.getContentPane().setLayout(new BorderLayout());

		frame.getContentPane().add(new JScrollPane(view), BorderLayout.CENTER);

		frame.pack();
		frame.setVisible(true);
	}

	private DefaultTableViewModel loadDataModel(TableViewColumn[] BaseColumns,
			MestraFiles mestraFiles,
			TraceUpDownEnum traceUpDown,
			ConfigurationFileBaseReader configuration) {

		DefaultTableViewModel model = null;

		MestraScenario mestraScenario = mestraFiles.getMestraScenario();
		switch (mestraScenario.getMestraScenarioEnum()) {
		case trace_SSS_SRS:
			switch (traceUpDown) {
			case UpDown:
				Trace_SSS_SRS_Collection trace_SSS_SRS_Collection = new Trace_SSS_SRS_Collection(mestraFiles, configuration);
				model = new DefaultTableViewModel(BaseColumns, trace_SSS_SRS_Collection.getTrace_SSS_SRS_Collection());
				break;
			case DownUp:
				Trace_SRS_SSS_Collection trace_SRS_SSS_Collection = new Trace_SRS_SSS_Collection(mestraFiles, configuration);
				model = new DefaultTableViewModel(BaseColumns, trace_SRS_SSS_Collection.getTrace_SRS_SSS_Collection());
				break;
			}			
			break;

		case trace_SRS_TS:
			switch (traceUpDown) {
			case UpDown:
				Trace_SRS_TS_Collection trace_SRS_TS_Collection = new Trace_SRS_TS_Collection(mestraFiles, configuration);
				model = new DefaultTableViewModel(BaseColumns, trace_SRS_TS_Collection.getTrace_SRS_TS_Collection());				
				break;
			case DownUp:
				Trace_TS_SRS_Collection trace_TS_SRS_Collection = new Trace_TS_SRS_Collection(mestraFiles, configuration);
				model = new DefaultTableViewModel(BaseColumns, trace_TS_SRS_Collection.getTrace_TS_SRS_Collection());			
				break;			
			}
			break;

		case trace_SRS_SDD:
			switch (traceUpDown) {
			case UpDown:
				Trace_SRS_SDD_Collection trace_SRS_SDD_Collection = new Trace_SRS_SDD_Collection(mestraFiles, configuration);
				model = new DefaultTableViewModel(BaseColumns, trace_SRS_SDD_Collection.getTrace_SRS_SDD_Collection());
				break;
			case DownUp:
				Trace_SDD_SRS_Collection trace_SDD_SRS_Collection = new Trace_SDD_SRS_Collection(mestraFiles, configuration);
				model = new DefaultTableViewModel(BaseColumns, trace_SDD_SRS_Collection.getTrace_SDD_SRS_Collection());
				break;
			}
			break;

		case trace_SRS_MF:
			switch (traceUpDown) {
			case UpDown:
				Trace_SRS_MF_Collection trace_SRS_MF_Collection = new Trace_SRS_MF_Collection(mestraFiles, configuration);
				model = new DefaultTableViewModel(BaseColumns, trace_SRS_MF_Collection.getTrace_SRS_MF_Collection());
				break;
			case DownUp:
				Trace_MF_SRS_Collection trace_MF_SRS_Collection = new Trace_MF_SRS_Collection(mestraFiles, configuration);
				model = new DefaultTableViewModel(BaseColumns, trace_MF_SRS_Collection.getTrace_MF_SRS_Collection());
				break;
			}
			break;

		default:
			logger.log(Level.SEVERE , "ERROR: Traceability Results: loadDataModel: should not achieve here!!!");
		break;
		}
		return model;
	}

	private TableView initColumnHeadersFor_SRS_TS(MestraFiles mestraFiles, ConfigurationFileBaseReader configuration) {
		DefaultTableViewModel model = null;
		try {
			TableViewColumn[] BaseColumns =
			{
					new DefaultTableViewColumn(
							"SRS File", 
							new Comparator<Object>() {
								public int compare(final Object o1, final Object o2) {
									final MestraBase p1 = (MestraBase) o1;
									final MestraBase p2 = (MestraBase) o2;
									return (p1.getShortFileName().compareTo(p2.getShortFileName()));
								}
							},
							MestraBase.class.getDeclaredMethod("getShortFileName", (Class[])null)),

							new DefaultTableViewColumn(
									"SRS Requirement", 
									new Comparator<Object>() {
										public int compare(final Object o1, final Object o2) {
											final MestraSRS p1 = (MestraSRS) o1;
											final MestraSRS p2 = (MestraSRS) o2;
											return (p1.getIdentifier().compareTo(p2.getIdentifier()));
										}
									},
									MestraBase.class.getDeclaredMethod("getMestraIdentifierClone", (Class[])null)),

											new DefaultTableViewColumn(
													"Covering Test Sheet",
													new Comparator<Object>() {
														public int compare(final Object o1, final Object o2) {
															final Trace_SRS_TS p1 = (Trace_SRS_TS) o1;
															final Trace_SRS_TS p2 = (Trace_SRS_TS) o2;
															return (p1.getTSIdentifier().compareTo(p2.getTSIdentifier()));
														}
													},
													Trace_SRS_TS.class.getDeclaredMethod("getThis", (Class[])null)),

			};
			model = loadDataModel(BaseColumns,mestraFiles,TraceUpDownEnum.UpDown, configuration);
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}		
		final TableView view = new TableView(model);
		return view;
	}

	private TableView initColumnHeadersFor_TS_SRS(MestraFiles mestraFiles, ConfigurationFileBaseReader configuration) {
		DefaultTableViewModel model = null;

		try {
			TableViewColumn[] BaseColumns =
			{
					new DefaultTableViewColumn(
							"TS File",
							new Comparator<Object>() {
								public int compare(final Object o1, final Object o2) {
									final MestraBase p1 = (MestraBase) o1;
									final MestraBase p2 = (MestraBase) o2;
									return (p1.getShortFileName().compareTo(p2.getShortFileName()));
								}
							},
							MestraBase.class.getDeclaredMethod("getShortFileName", (Class[])null)),

							new DefaultTableViewColumn(
									"Test Identifier",
									new Comparator<Object>() {
										public int compare(final Object o1, final Object o2)
										{
											final MestraIdentifier p1 = (MestraIdentifier) o1;
											final MestraIdentifier p2 = (MestraIdentifier) o2;
											return (p1.getIdentifier().compareTo(p2.getIdentifier()));
										}
									},
									MestraBase.class.getDeclaredMethod("getMestraIdentifierClone", (Class[])null)),

											new DefaultTableViewColumn(
													"Covered SRS Requirement",
													new Comparator<Object>() {
														public int compare(final Object o1, final Object o2) {
															final Trace_TS_SRS p1 = (Trace_TS_SRS) o1;
															final Trace_TS_SRS p2 = (Trace_TS_SRS) o2;
															return (p1.getSRSIdentifier().compareTo(p2.getSRSIdentifier()));
														}
													},
													Trace_TS_SRS.class.getDeclaredMethod("getThis", (Class[])null)),

			};
			model = loadDataModel(BaseColumns, mestraFiles, TraceUpDownEnum.DownUp, configuration);
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		final TableView view = new TableView(model);
		return view;
	}	

	private TableView initColumnHeadersFor_SRS_SSS(MestraFiles mestraFiles, ConfigurationFileBaseReader configuration) {

		DefaultTableViewModel model = null;  
		try {
			TableViewColumn[] BaseColumns =
			{
					new DefaultTableViewColumn(
							"SRS File",
							new Comparator<Object>() {
								public int compare(final Object o1, final Object o2)
								{
									final MestraBase p1 = (MestraBase) o1;
									final MestraBase p2 = (MestraBase) o2;
									return (p1.getShortFileName().compareTo(p2.getShortFileName()));
								}
							},
							MestraBase.class.getDeclaredMethod("getShortFileName", (Class[])null)), 

							new DefaultTableViewColumn(
									"SRS Requirement",
									new Comparator<Object>() {
										public int compare(final Object o1, final Object o2)
										{
											final MestraIdentifier p1 = (MestraIdentifier) o1;
											final MestraIdentifier p2 = (MestraIdentifier) o2;
											return (p1.getIdentifier().compareTo(p2.getIdentifier()));
										}
									},
									// call a method that returns a MestraIdentifier to used in the colored Column class  
									MestraBase.class.getDeclaredMethod("getMestraIdentifierClone", (Class[])null)),

									

											new DefaultTableViewColumn(
													"Covered SSS Requirement",
													new Comparator<Object>() {
														public int compare(final Object o1, final Object o2) {
															final Trace_SRS_SSS p1 = (Trace_SRS_SSS) o1;
															final Trace_SRS_SSS p2 = (Trace_SRS_SSS) o2;
															return (p1.getMestraSSS().getIdentifier().compareTo(p2.getMestraSSS().getIdentifier()));
														}
													},

													Trace_SRS_SSS.class.getDeclaredMethod("getThis", (Class[])null)),

			};
			model = loadDataModel(BaseColumns, mestraFiles, TraceUpDownEnum.DownUp, configuration);
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		final TableView view = new TableView(model);
		return view;
	}


	private TableView initColumnHeadersFor_SSS_SRS(MestraFiles mestraFiles, ConfigurationFileBaseReader configuration) {

		DefaultTableViewModel model = null;  
		try {
			TableViewColumn[] BaseColumns =
			{
					new DefaultTableViewColumn(
							"SSS File",
							new Comparator<Object>() {
								public int compare(final Object o1, final Object o2) {
									final MestraBase p1 = (MestraBase) o1;
									final MestraBase p2 = (MestraBase) o2;
									return (p1.getShortFileName().compareTo(p2.getShortFileName()));
								}
							},
							MestraBase.class.getDeclaredMethod("getShortFileName", (Class[])null)), 

							new DefaultTableViewColumn(
									"SSS Requirement",
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
													MestraSSS.class.getDeclaredMethod("getRequirementAllocation", (Class[])null)),

													new DefaultTableViewColumn(
															"SRS covering Requirement",
															new Comparator<Object>() {
																public int compare(final Object o1, final Object o2) {
																	final Trace_SSS_SRS p1 = (Trace_SSS_SRS) o1;
																	final Trace_SSS_SRS p2 = (Trace_SSS_SRS) o2;
																	// warning : using getSRSIdentifier because getMestraSRS
																	return (p1.getSRSIdentifier().compareTo(p2.getSRSIdentifier()));
																}
															},
															Trace_SSS_SRS.class.getDeclaredMethod("getThis", (Class[])null))

			};
			model = loadDataModel(BaseColumns, mestraFiles, TraceUpDownEnum.UpDown, configuration);
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		final TableView view = new TableView(model);
		return view;
	}

	private TableView initColumnHeadersFor_MF_SRS(MestraFiles mestraFiles, ConfigurationFileBaseReader configuration) {
		DefaultTableViewModel model = null;
		try {
			TableViewColumn[] BaseColumns =
			{
					new DefaultTableViewColumn(
							"MF File", 
							new Comparator<Object>() {
								public int compare(final Object o1, final Object o2) {
									final MestraBase p1 = (MestraBase) o1;
									final MestraBase p2 = (MestraBase) o2;
									return (p1.getShortFileName().compareTo(p2.getShortFileName()));
								}
							},
							MestraBase.class.getDeclaredMethod("getShortFileName", (Class[])null)),

							new DefaultTableViewColumn(
									"SRS Implement Requirements", 
									new Comparator<Object>() {
										public int compare(final Object o1, final Object o2) {
											final MestraIdentifier p1 = (MestraIdentifier) o1;
											final MestraIdentifier p2 = (MestraIdentifier) o2;
											return (p1.getIdentifier().compareTo(p2.getIdentifier()));
										}
									},
									// call a method that returns a MestraIdentifier to used in the colored Column class  
									MestraBase.class.getDeclaredMethod("getMestraIdentifierClone", (Class[])null)), 

									


									new DefaultTableViewColumn(
									"SRS Requirements",
									new Comparator<Object>() {
									public int compare(final Object o1, final Object o2) {
									final Trace_MF_SRS p1 = (Trace_MF_SRS) o1;
									final Trace_MF_SRS p2 = (Trace_MF_SRS) o2;
									return (p1.getSRSIdentifier().compareTo(p2.getSRSIdentifier()));
									}
									},
									Trace_MF_SRS.class.getDeclaredMethod("getThis", (Class[])null)),

			};
			model = loadDataModel(BaseColumns, mestraFiles, TraceUpDownEnum.DownUp, configuration);
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}       
		final TableView view = new TableView(model);
		return view;

	}

	private TableView initColumnHeadersFor_SRS_MF(MestraFiles mestraFiles, ConfigurationFileBaseReader configuration) {

		DefaultTableViewModel model = null; 
		try {
			TableViewColumn[] BaseColumns =
			{
					new DefaultTableViewColumn(
							"SRS File", 
							new Comparator<Object>() {
								public int compare(final Object o1, final Object o2) {
									final MestraBase p1 = (MestraBase) o1;
									final MestraBase p2 = (MestraBase) o2;
									return (p1.getShortFileName().compareTo(p2.getShortFileName()));
								}
							},
							MestraBase.class.getDeclaredMethod("getShortFileName", (Class[])null)),

							new DefaultTableViewColumn(
									"SRS Requirement", 
									new Comparator<Object>() {
										public int compare(final Object o1, final Object o2) {
											final MestraIdentifier p1 = (MestraIdentifier) o1;
											final MestraIdentifier p2 = (MestraIdentifier) o2;
											return (p1.getIdentifier().compareTo(p2.getIdentifier()));
										}
									},
									// call a method that returns a MestraIdentifier to used in the colored Column class  
									MestraBase.class.getDeclaredMethod("getMestraIdentifierClone", (Class[])null)), 

									

											new DefaultTableViewColumn(
													"MF Components",
													new Comparator<Object>() {
														public int compare(final Object o1, final Object o2) {
															final Trace_SRS_MF p1 = (Trace_SRS_MF) o1;
															final Trace_SRS_MF p2 = (Trace_SRS_MF) o2;
															return (p1.getMFIdentifier().compareTo(p2.getMFIdentifier()));
														}
													},
													Trace_SRS_MF.class.getDeclaredMethod("getThis", (Class[])null)),

			};
			model = loadDataModel(BaseColumns, mestraFiles, TraceUpDownEnum.UpDown, configuration);
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}       
		final TableView view = new TableView(model);
		return view;

	}

	private TableView initColumnHeadersFor_SRS_SDD(MestraFiles mestraFiles, ConfigurationFileBaseReader configuration) {
		DefaultTableViewModel model = null; 
		try {
			TableViewColumn[] BaseColumns =
			{
					new DefaultTableViewColumn(
							"SRS File", 
							new Comparator<Object>() {
								public int compare(final Object o1, final Object o2) {
									final MestraBase p1 = (MestraBase) o1;
									final MestraBase p2 = (MestraBase) o2;
									return (p1.getShortFileName().compareTo(p2.getShortFileName()));
								}
							},
							MestraBase.class.getDeclaredMethod("getShortFileName", (Class[])null)),

							new DefaultTableViewColumn(
									"SRS Requirement", 
									new Comparator<Object>() {
										public int compare(final Object o1, final Object o2) {
											final MestraIdentifier p1 = (MestraIdentifier) o1;
											final MestraIdentifier p2 = (MestraIdentifier) o2;
											return (p1.getIdentifier().compareTo(p2.getIdentifier()));
										}
									},
									// call a method that returns a MestraIdentifier to used in the colored Column class  
									MestraBase.class.getDeclaredMethod("getMestraIdentifierClone", (Class[])null)), 

									

											new DefaultTableViewColumn(
													"SDD Components",
													new Comparator<Object>() {
														public int compare(final Object o1, final Object o2) {
															final Trace_SRS_SDD p1 = (Trace_SRS_SDD) o1;
															final Trace_SRS_SDD p2 = (Trace_SRS_SDD) o2;
															return (p1.getSDDIdentifier().compareTo(p2.getSDDIdentifier()));
														}
													},
													Trace_SRS_SDD.class.getDeclaredMethod("getThis", (Class[])null)),

			};
			model = loadDataModel(BaseColumns, mestraFiles, TraceUpDownEnum.UpDown, configuration);
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}       
		final TableView view = new TableView(model);
		return view;

	}

	private TableView initColumnHeadersFor_SDD_SRS(MestraFiles mestraFiles, ConfigurationFileBaseReader configuration) {
		DefaultTableViewModel model = null; 
		try {
			TableViewColumn[] BaseColumns =
			{
					new DefaultTableViewColumn(
							"SDD File", 
							new Comparator<Object>() {
								public int compare(final Object o1, final Object o2)
								{
									final MestraBase p1 = (MestraBase) o1;
									final MestraBase p2 = (MestraBase) o2;
									return (p1.getShortFileName().compareTo(p2.getShortFileName()));
								}
							},
							MestraBase.class.getDeclaredMethod("getShortFileName", (Class[])null)),

							new DefaultTableViewColumn(
									"SDD Component", 
									new Comparator<Object>() {
										public int compare(final Object o1, final Object o2) {
											final MestraIdentifier p1 = (MestraIdentifier) o1;
											final MestraIdentifier p2 = (MestraIdentifier) o2;
											return (p1.getIdentifier().compareTo(p2.getIdentifier()));
										}
									},
									// call a method that returns a MestraIdentifier to used in the colored Column class  
									MestraBase.class.getDeclaredMethod("getMestraIdentifierClone", (Class[])null)),

									

											new DefaultTableViewColumn(
													"SRS Requirements Implemented",
													new Comparator<Object>() {
														public int compare(final Object o1, final Object o2) {
															final Trace_SDD_SRS p1 = (Trace_SDD_SRS) o1;
															final Trace_SDD_SRS p2 = (Trace_SDD_SRS) o2;
															return (p1.getSRSIdentifier().compareTo(p2.getSRSIdentifier()));
														}
													},
													Trace_SDD_SRS.class.getDeclaredMethod("getThis", (Class[])null)),

			};
			model = loadDataModel(BaseColumns, mestraFiles, TraceUpDownEnum.DownUp, configuration);
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}       
		final TableView view = new TableView(model);
		return view;
	}

	private TableView initView(MestraFiles mestraFiles,
			ConfigurationFileBaseReader configuration,
			TraceUpDownEnum traceUpDown) {

		TableView view = null;

		MestraScenario mestraScenario = mestraFiles.getMestraScenario();
		switch (mestraScenario.getMestraScenarioEnum()) {
		case trace_SSS_SRS:
			switch (traceUpDown) {
			case UpDown:
				view = initColumnHeadersFor_SSS_SRS(mestraFiles, configuration);
				break;
			case DownUp:
				view = initColumnHeadersFor_SRS_SSS(mestraFiles, configuration);
				break;
			}
			break;

		case trace_SRS_TS:
			switch (traceUpDown) {
			case UpDown:
				view = initColumnHeadersFor_SRS_TS(mestraFiles, configuration);
				break;
			case DownUp:
				view = initColumnHeadersFor_TS_SRS(mestraFiles, configuration);
				break;			
			}
			break;

		case trace_SRS_SDD:
			switch (traceUpDown) {
			case UpDown:
				view = initColumnHeadersFor_SRS_SDD(mestraFiles, configuration);
				break;
			case DownUp:
				view = initColumnHeadersFor_SDD_SRS(mestraFiles, configuration);
				break;
			}
			break;

		case trace_All:
			System.err.println("ERROR: Multiple Mestra Files analysis Results: initView:  not yet implemented");
			break;

			//view = initColumnHeadersForMultipleFiles(mestraFiles);

		case trace_SRS_MF:
			switch (traceUpDown) {
			case UpDown:
				view = initColumnHeadersFor_SRS_MF(mestraFiles, configuration);
				break;
			case DownUp:
				view = initColumnHeadersFor_MF_SRS(mestraFiles, configuration);
				break;
			}
			//System.err.println("ERROR: Traceability Results: initView: Trace SRS MF not yet implemented");
			break;

		default:
			System.err.println("ERROR: Traceability Results: initView: should not achieve here!!!");
		break;
		}

		// we use a specific class to render ERRORs and WARNINGs using colors in the table view
		view.setDefaultRenderer(MestraBase.class,new ColoredTableCellRenderer());
		view.setDefaultRenderer(MestraSSS.class,new ColoredTableCellRenderer());
		view.setDefaultRenderer(MestraIdentifier.class,new ColoredTableCellRenderer());
		view.setDefaultRenderer(MestraTitle.class,new ColoredTableCellRenderer());
		view.setDefaultRenderer(Trace_SSS_SRS.class,new ColoredTableCellRenderer());
		view.setDefaultRenderer(Trace_SRS_SSS.class,new ColoredTableCellRenderer());
		view.setDefaultRenderer(Trace_SRS_TS.class,new ColoredTableCellRenderer());
		view.setDefaultRenderer(Trace_TS_SRS.class,new ColoredTableCellRenderer());
		view.setDefaultRenderer(Trace_SRS_SDD.class,new ColoredTableCellRenderer());
		view.setDefaultRenderer(Trace_SDD_SRS.class,new ColoredTableCellRenderer());
		view.setDefaultRenderer(Trace_SRS_MF.class, new ColoredTableCellRenderer());
		view.setDefaultRenderer(Trace_MF_SRS.class, new ColoredTableCellRenderer());
		

		view.setMakeIndex(true);
		return view;

	}

	private String getFrameTitle(MestraFiles mestraFiles, TraceUpDownEnum traceUpDown) {

		String FrameTitle = "";
		MestraScenario mestraScenario = mestraFiles.getMestraScenario();

		switch (mestraScenario.getMestraScenarioEnum()) {
		case trace_SSS_SRS:
			switch (traceUpDown) {
			case UpDown:
				FrameTitle = "SSS_SRS_";
				FrameTitle += mestraFiles.getTraceability_CSCI();
				break;
			case DownUp:
				FrameTitle = "SRS_SSS_";
				FrameTitle += mestraFiles.getTraceability_CSCI();
				break;
			}
			break;

		case trace_SRS_SDD:
			switch (traceUpDown) {
			case UpDown:
				FrameTitle = "SRS_SDD";
				break;
			case DownUp:
				FrameTitle = "SDD_SRS";
				break;
			}
			break;

		case trace_SRS_MF:
			switch (traceUpDown) {
			case UpDown:
				FrameTitle = "SRS_MF";
				break;
			case DownUp:
				FrameTitle = "MF_SRS";
				break;
			}
			break;

		case trace_SRS_TS:
			switch (traceUpDown) {
			case UpDown:
				FrameTitle = "SRS_TS";
				break;
			case DownUp:
				FrameTitle = "TS_SRS";
				break;
			}
			break;
		default:
			break;
		}
		return FrameTitle;
	}
}
