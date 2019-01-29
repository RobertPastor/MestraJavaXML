package gui;

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
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JTextArea;

public class TextAreaDropTarget implements DropTargetListener {
	
	final static Logger logger = Logger.getLogger(MestraChecksResults.class.getName()); 

	JTextArea textArea = null;
	MestraChecksTab mestraChecksTab = null;

	public TextAreaDropTarget(JTextArea area) {
		this.textArea = area;
	}
	
	public TextAreaDropTarget(JTextArea area, MestraChecksTab tab) {
		this.textArea = area;
		this.mestraChecksTab = tab;
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
	 * this method is called when the file is dropped into the Text Area
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void drop(DropTargetDropEvent dtde) {
		// TODO Auto-generated method stub

		//logger.info( "Text Area Drop Target: [Target] drop");
		DropTargetContext targetContext = dtde.getDropTargetContext();

		boolean outcome = false;

		if ((dtde.getSourceActions() & DnDConstants.ACTION_COPY) != 0) {
			dtde.acceptDrop(DnDConstants.ACTION_COPY);
			//logger.info( "Text Area Drop Target: drop accepted");
		}
		else {
			dtde.rejectDrop();
			return;
		}	
		
        Transferable transferable = dtde.getTransferable();
        
        if (transferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
        	//logger.info( "Transferable supports Java File List Flavor");
            try {
                Object obj = transferable.getTransferData(DataFlavor.javaFileListFlavor);
                if (obj instanceof java.util.List) {
                    List<File> FileList = (List<File>)obj;
					
                    Iterator<File> Iter = FileList.iterator();
                    while (Iter.hasNext()) {
                    	File file = Iter.next();
                    	//logger.info( "JDropTablePanel: File: "+file.getAbsolutePath());
                    	if (file.isFile()) {
                    		textArea.setText(file.getAbsolutePath());
                    		if ( (FileList.size()==1) && (this.mestraChecksTab!= null)) {
                    			this.mestraChecksTab.analyseMestraFile(file.getAbsolutePath());
                    		}
                    	}
                    }
                }
            }
            catch (UnsupportedFlavorException e1) {
            	logger.log(Level.SEVERE , "Text Area Drop Target: Unsupported Flavor Exception " + e1.getLocalizedMessage());
            }
            catch (IOException e2) {
            	logger.log(Level.SEVERE , "Text Area Drop Target: IO Exception " + e2.getLocalizedMessage());
            }
 
		}
		targetContext.dropComplete(outcome);
	}

	public void dropActionChanged(DropTargetDragEvent dtde) {
		// TODO Auto-generated method stub

	}

}
