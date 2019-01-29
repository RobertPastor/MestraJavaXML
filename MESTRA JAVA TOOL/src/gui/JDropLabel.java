package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetContext;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.TransferHandler;

public class JDropLabel extends JPanel implements DropTargetListener {

	final static Logger logger = Logger.getLogger(JDropLabel.class.getName()); 

	private static final long serialVersionUID = -3124336025340735661L;
	
	private MestraChecksTab mestraChecksTab = null;
	private JLabel label = null;
	
	public String getText() {
	    return label.getText();
    }
	
	public void setText(String text) {
		label.setText(text);
	}
    
	// Constructor
	public JDropLabel(final MestraChecksTab tab) {
		
		super(new BorderLayout());
		this.mestraChecksTab = tab;
		
		this.label = new JLabel("Drop Here");		
		this.label.setBorder(BorderFactory.createLineBorder(Color.BLUE));			
		this.label.setTransferHandler(new TransferHandler("text"));
		
		PropertyChangeListener pl = new PropertyChangeListener() {
			public void propertyChange (PropertyChangeEvent e) {
				String propertyName = e.getPropertyName();
				if (propertyName.equalsIgnoreCase("text")) {
					String fileToAnalyse = (String)e.getNewValue();

					mestraChecksTab.analyseMestraFile(fileToAnalyse);
				}
			}
		};
		this.label.addPropertyChangeListener(pl);
		add(this.label, BorderLayout.CENTER);
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
        
		DropTargetContext targetContext = dtde.getDropTargetContext();
		boolean outcome = false;

		if ((dtde.getSourceActions() & DnDConstants.ACTION_COPY) != 0) {
			dtde.acceptDrop(DnDConstants.ACTION_COPY);
		}
		else {
			dtde.rejectDrop();
			return;
		}	
        Transferable transferable = dtde.getTransferable();
        
        if (transferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {

        	try {
                Object obj = transferable.getTransferData(DataFlavor.javaFileListFlavor);
                if (obj instanceof java.util.List) {
                    List<File> FileList = (List<File>)obj;
					
                    Iterator<File> Iter = FileList.iterator();
                    while (Iter.hasNext()) {
                    	File file = Iter.next();

                    	if (file.isFile()) {
                    		label.setText(file.getAbsolutePath());
                    	}
                    }
                }
            }
            catch (UnsupportedFlavorException e1) {
                logger.log(Level.INFO , "JDropLabel: Unsupported Flavor Exception "+ e1.getLocalizedMessage());
            }
            catch (IOException e2) {
                logger.log(Level.INFO , "JDropLabel: IO Exception" + e2.getLocalizedMessage());
            }
 
		}
		targetContext.dropComplete(outcome);

        
    }

	public void dropActionChanged(DropTargetDragEvent dtde) {
		// TODO Auto-generated method stub
		
	}


}
