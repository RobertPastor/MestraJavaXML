package gui;

import gui.tableView.DefaultTableViewColumn;
import gui.tableView.DefaultTableViewModel;
import gui.tableView.TableViewColumn;
import gui.tableView.TableViewModel;
import gui.tableView.TableViewRow;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetContext;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import mestra.MestraFile;
import mestra.MestraFileType.MestraFileTypeEnum;

public class JDropTablePanel extends JPanel implements DropTargetListener {

	final static Logger logger = Logger.getLogger(JDropTablePanel.class.getName()); 

	/**
	 * 
	 */
	private static final long serialVersionUID = 6139530081303866466L;

	private static final String DefaultTableModel = null;

	private CustomTableView customTableView = null;
	private MestraFileTypeEnum mestraFileType = null;

	public JDropTablePanel (String strPanelTitle,
			MestraFileTypeEnum pMestraFileType) {

		super(new BorderLayout());
		this.setOpaque(false);
		this.mestraFileType = pMestraFileType;

		TitledBorder title;
		title = BorderFactory.createTitledBorder(strPanelTitle);
		this.setBorder(title);

		this.customTableView = InitTableColumns();
		this.customTableView.setOpaque(false);
		customTableView.setPreferredSize(new Dimension(800, 200));
		
		JScrollPane jScrollPane = new JScrollPane(customTableView);
		jScrollPane.setPreferredSize(new Dimension(800, 200));

		jScrollPane.setOpaque(false);
		
		this.add(jScrollPane, BorderLayout.CENTER);
		this.setPreferredSize(new Dimension(800,300));
		
	}
	
	@Override
	public boolean isOpaque() {
		return false;
	}

	public void setMestraFileType(MestraFileTypeEnum pMestraFileType) {
		logger.info( "Drop Table Panel: Mestra File Type: "+pMestraFileType);
		this.mestraFileType = pMestraFileType;
	}

	private DefaultTableViewModel loadDataModel(TableViewColumn[] BaseColumns) {				 

		final DefaultTableViewModel model = new DefaultTableViewModel(BaseColumns);
		return model;
	}


	public CustomTableView InitTableColumns () {

		DefaultTableViewModel model = null;  
		try {
			TableViewColumn[] BaseColumns =
				{
					new DefaultTableViewColumn(
							"File Path", 
							MestraFile.class.getDeclaredMethod("getLongFileName", (Class[]) null ),
							MestraFile.class.getDeclaredMethod("setLongFileName", new Class[] {String.class})),

							new DefaultTableViewColumn(
									"File Type",
									MestraFile.class.getDeclaredMethod("getStrFileType", (Class[])null),
									MestraFile.class.getDeclaredMethod("setStrFileType", new Class[] {String.class})),

									new DefaultTableViewColumn(
											"Trace Yes / No",
											FileTable.class.getDeclaredMethod("isAnalysed", (Class[])null),
											FileTable.class.getDeclaredMethod("setAnalysed", new Class[] {boolean.class}))  

				};
			model = loadDataModel(BaseColumns);
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.log(Level.SEVERE , "Drop Table: error while building the table");

		}
		final CustomTableView view = new CustomTableView(model);
		view.getTableHeader().setBackground(Color.lightGray);
		view.getColumnModel().getColumn(0).setPreferredWidth(700);
		view.getColumnModel().getColumn(1).setPreferredWidth(20);
		view.getColumnModel().getColumn(2).setPreferredWidth(20);
		view.setMakeIndex(true);
		view.setOpaque(false);

		//view.setTransferHandler(new TableTransferHandler());
		return view;
	}

	public void dragEnter(DropTargetDragEvent dtde) {
		// TODO Auto-generated method stub

	}

	public void dragExit(DropTargetEvent dte) {
		// TODO Auto-generated method stub

	}

	public void dragOver(DropTargetDragEvent dtde) {
		// TODO Auto-generated method stub

	}
	/**
	 * True if the file with the long file name is already in the table
	 * @param model
	 * @param FileName
	 * @return
	 */
	private boolean AlreadyIn(DefaultTableViewModel model,String FileName) {
		boolean AlreadyIn = false;
		Iterator<Object> Iter = model.iterator();
		while (Iter.hasNext()) {
			Object obj = Iter.next();
			if (obj instanceof FileTable) {
				FileTable fileTable = (FileTable)obj;
				if (fileTable.getLongFileName().equalsIgnoreCase(FileName)) {
					AlreadyIn = true;
					return AlreadyIn;
				}
			}
		}
		return AlreadyIn;
	}

	@SuppressWarnings("unchecked")
	public void drop(DropTargetDropEvent dtde) {

		DropTargetContext targetContext = dtde.getDropTargetContext();
		if ((dtde.getSourceActions() & DnDConstants.ACTION_COPY) != 0) {
			dtde.acceptDrop(DnDConstants.ACTION_COPY);
			logger.info ( "JDrop Table: drop accepted");
		}
		else {
			dtde.rejectDrop();
			return;
		}       

		Transferable transferable = dtde.getTransferable();
		DefaultTableViewModel model = (DefaultTableViewModel)customTableView.getDataModel();

		if (transferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
			logger.info( "Transferable supports Java File List Flavor");
			try {
				Object obj = transferable.getTransferData(DataFlavor.javaFileListFlavor);
				if (obj instanceof java.util.List) {
					List<File> FileList = (List<File>)obj;

					Iterator<File> Iter = FileList.iterator();
					while (Iter.hasNext()) {
						File file = Iter.next();
						logger.info(  "JDropTablePanel: File: "+file.getAbsolutePath());
						if (file.isFile() && (AlreadyIn(model,file.getAbsolutePath()) == false)) {
							model.addRow(new FileTable(file.getAbsolutePath(), mestraFileType));
						}
					}
				}
			}
			catch (UnsupportedFlavorException e1) {
				logger.log(Level.SEVERE , "Text Area Drop Target: Unsupported Flavor Exception");
			}
			catch (IOException e2) {
				logger.log(Level.SEVERE , "Text Area Drop Target: IO Exception");
			}
		}
		targetContext.dropComplete(true);
	}

	public void dropActionChanged(DropTargetDragEvent dtde) {
		// TODO Auto-generated method stub

	}

	public ArrayList<MestraFile> getMestraFileTableList() {

		ArrayList<MestraFile> fileTableList = new ArrayList<MestraFile>();

		int i;
		TableViewModel model = customTableView.getDataModel();
		FileTable fileTable ;
		for ( i = 0 ; i < customTableView.getRowCount(); i++) {
			Object obj = model.getRowObject(i);
			if (obj instanceof FileTable) {
				fileTable = (FileTable)obj;
				if (fileTable.isAnalysed()) {
					fileTableList.add((MestraFile)fileTable);
				}
			}
		}
		return fileTableList;
	}

	public int getRowCount() {
		return (customTableView.getRowCount());
	}

	public void notifyChanges(MestraFileTypeEnum mestraFileTypeEnum) {
		this.customTableView.notifyChanges(mestraFileTypeEnum);
		// clear content of the table
		while (((TableViewModel)this.customTableView.getDataModel()).getRowCount()>0) {
			Object rowObject = this.customTableView.getDataModel().getRowObject(0);
			// System.out.println("row Object found");
			if (rowObject instanceof FileTable) {
				// System.out.println(" row object is of type TableViewRow");
				((DefaultTableViewModel)this.customTableView.getDataModel()).removeRow(rowObject);
			}
		}
		//((DefaultTableViewModel)this.customTableView.getDataModel()).clear();
	}

	
}
