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
import java.util.logging.Logger;



public class LabelDropTarget implements DropTargetListener {
	
	final static Logger logger = Logger.getLogger(MestraChecksResults.class.getName()); 
    
    private JDropLabel label = null;
    
    public LabelDropTarget(JDropLabel jDropLabel) {
        this.label = jDropLabel;
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

    public void drop(DropTargetDropEvent dtde) {
        // TODO Auto-generated method stub
    	logger.info("LabelDropTarget: [Target] drop");
        
		DropTargetContext targetContext = dtde.getDropTargetContext();

		boolean outcome = false;

		if ((dtde.getSourceActions() & DnDConstants.ACTION_COPY) != 0) {
			dtde.acceptDrop(DnDConstants.ACTION_COPY);
			logger.info("Label Drop Target: drop accepted");
		}
		else {
			dtde.rejectDrop();
			return;
		}	
        Transferable transferable = dtde.getTransferable();
        
        if (transferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
        	logger.info("Transferable supports Java File List Flavor");
            try {
                Object obj = transferable.getTransferData(DataFlavor.javaFileListFlavor);
                if (obj instanceof java.util.List) {
                    List<File> FileList = (List<File>) obj;
					
                    Iterator<File> Iter = FileList.iterator();
                    while (Iter.hasNext()) {
                    	File file = Iter.next();
                    	logger.info("JDropTablePanel: File: "+file.getAbsolutePath());
                    	if (file.isFile()) {
                    		label.setText(file.getAbsolutePath());
                    	}
                    }
                }
            }
            catch (UnsupportedFlavorException e1) {
            	logger.info( "Text Area Drop Target: Unsupported Flavor Exception");
            }
            catch (IOException e2) {
            	logger.info( "Text Area Drop Target: IO Exception");
            }
 
		}
		targetContext.dropComplete(outcome);

    }

    public void dropActionChanged(DropTargetDragEvent dtde) {
        // TODO Auto-generated method stub
        
    }

}
