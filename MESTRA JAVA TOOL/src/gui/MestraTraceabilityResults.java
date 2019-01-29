package gui;

import gui.tableView.DefaultTableViewColumn;
import gui.tableView.DefaultTableViewModel;
import gui.tableView.TableView;
import gui.tableView.TableViewColumn;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import mestra.MestraBase;
import mestra.MestraFiles;
import mestra.MestraIdentifier;
import mestra.MestraSSS;
import mestra.MestraScenario;
import mestra.MestraTitle;
import mestra.Trace_SRS_SSS;
import mestra.Trace_SRS_SSS_Collection;
import mestra.Trace_SSS_SRS;
import mestra.Trace_SSS_SRS_Collection;
import configuration.ConfigurationFileBaseReader;

/**
 * This class manages the display of two traceability frames
 * @author Pastor
 *
 */
public class MestraTraceabilityResults {

	private TableView viewUpDown = null;
	private TableView viewDownUp = null;
	private enum TraceUpDownEnum { UpDown, DownUp  }

	public void viewResults(MestraFiles mestraFiles, ConfigurationFileBaseReader aConfiguration) {

		viewUpDown = initView (mestraFiles, aConfiguration, TraceUpDownEnum.UpDown);
		viewDownUp = initView (mestraFiles, aConfiguration, TraceUpDownEnum.DownUp);
		createUpDownFrame(mestraFiles);
		createDownUpFrame(mestraFiles);

	}

	private DefaultTableViewModel loadDataModel(TableViewColumn[] BaseColumns,
			MestraFiles mestraFiles,
			TraceUpDownEnum traceUpDown,
			ConfigurationFileBaseReader aConfiguration) {

		DefaultTableViewModel model = null;
		switch (traceUpDown) {
		case UpDown:
			Trace_SSS_SRS_Collection trace_SSS_SRS_Collection = new Trace_SSS_SRS_Collection(mestraFiles, aConfiguration);
			model = new DefaultTableViewModel(BaseColumns, trace_SSS_SRS_Collection.getTrace_SSS_SRS_Collection());
			break;
		case DownUp:
			Trace_SRS_SSS_Collection trace_SRS_SSS_Collection = new Trace_SRS_SSS_Collection(mestraFiles, aConfiguration);
			model = new DefaultTableViewModel(BaseColumns, trace_SRS_SSS_Collection.getTrace_SRS_SSS_Collection());
			break;
		}

		return model;
	}

	private TableView initColumnHeadersFor_SRS_SSS(MestraFiles mestraFiles, ConfigurationFileBaseReader aConfiguration) {

		DefaultTableViewModel model = null;  
		try {
			TableViewColumn[] BaseColumns =
			{
					new DefaultTableViewColumn(
						"SRS File", 
						MestraBase.class.getDeclaredMethod("getShortFileName", (Class[])null)), 

					new DefaultTableViewColumn(
						"SRS Requirement", 
						MestraIdentifier.class.getDeclaredMethod("getIdentifier", (Class[])null)),  

					new DefaultTableViewColumn(
						"SRS Requirement Title",
						MestraBase.class.getDeclaredMethod("getTitle", (Class[])null)),

					new DefaultTableViewColumn(
						"Covered SSS Requirement",
						Trace_SRS_SSS.class.getDeclaredMethod("getThis", (Class[])null)),

			};

			model = loadDataModel(BaseColumns, mestraFiles, TraceUpDownEnum.DownUp, aConfiguration);
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		final TableView view = new TableView(model);
		return view;
	}

	private TableView initColumnHeadersFor_SSS_SRS(MestraFiles mestraFiles, ConfigurationFileBaseReader aConfiguration) {

		DefaultTableViewModel model = null;  
		try {
			TableViewColumn[] BaseColumns =
			{
					new DefaultTableViewColumn(
						"SSS File", 
						MestraBase.class.getDeclaredMethod("getShortFileName", (Class[])null)), 

					new DefaultTableViewColumn(
						"SSS Requirement", 
						MestraIdentifier.class.getDeclaredMethod("getIdentifier", (Class[])null)), 

					new DefaultTableViewColumn(
						"SSS Requirement Title",
						MestraBase.class.getDeclaredMethod("getTitle", (Class[])null)),

					new DefaultTableViewColumn(
						"Allocation",
						MestraSSS.class.getDeclaredMethod("getRequirementAllocation", (Class[])null)),

					new DefaultTableViewColumn(
						"SRS covering Requirement",
						Trace_SSS_SRS.class.getDeclaredMethod("getThis", (Class[])null))

			};
			model = loadDataModel(BaseColumns, mestraFiles, TraceUpDownEnum.UpDown, aConfiguration);
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		final TableView view = new TableView(model);
		return view;
	}

	private TableView initView(MestraFiles mestraFiles,
			ConfigurationFileBaseReader aConfiguration,
			TraceUpDownEnum traceUpDown) {

		TableView view = null;

		MestraScenario mestraScenario = mestraFiles.getMestraScenario();
		switch (mestraScenario.getMestraScenarioEnum()) {
		case trace_SSS_SRS:
			switch (traceUpDown) {
			case UpDown:
				view = initColumnHeadersFor_SSS_SRS(mestraFiles, aConfiguration);
				break;
			case DownUp:
				view = initColumnHeadersFor_SRS_SSS(mestraFiles, aConfiguration);
				break;
			}
			break;
		case trace_SRS_TS:
			assert(true == false);
			break;
		default:
			break;
		}

		view.setDefaultRenderer(MestraBase.class,new ColoredTableCellRenderer());
		view.setDefaultRenderer(MestraSSS.class,new ColoredTableCellRenderer());
		view.setDefaultRenderer(MestraIdentifier.class,new ColoredTableCellRenderer());
		view.setDefaultRenderer(MestraTitle.class,new ColoredTableCellRenderer());
		view.setDefaultRenderer(Trace_SSS_SRS.class,new ColoredTableCellRenderer());
		view.setDefaultRenderer(Trace_SRS_SSS.class,new ColoredTableCellRenderer());

		view.setMakeIndex(true);
		return view;

	}

	private void createFrame(String FrameTitle,TraceUpDownEnum traceUpDown) {

		// create a floating frame to display the results
		JFrame frame = new JFrame(FrameTitle);
		frame.getContentPane().setLayout(new BorderLayout());
		switch (traceUpDown) {
		case UpDown:
			frame.getContentPane().add(new JScrollPane(viewUpDown), BorderLayout.CENTER);
			break;
		case DownUp:
			frame.getContentPane().add(new JScrollPane(viewDownUp), BorderLayout.CENTER);
			break;
		}
		frame.setPreferredSize(new Dimension(600,400));
		frame.pack();
		frame.setVisible(true);
	}

	private void createUpDownFrame(MestraFiles mestraFiles) {
		createFrame(getFrameTitle(mestraFiles,TraceUpDownEnum.UpDown),TraceUpDownEnum.UpDown);
	}

	private void createDownUpFrame(MestraFiles mestraFiles) {
		createFrame(getFrameTitle(mestraFiles,TraceUpDownEnum.DownUp),TraceUpDownEnum.DownUp);
	}

	private String getFrameTitle(MestraFiles mestraFiles,TraceUpDownEnum traceUpDown) {

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
