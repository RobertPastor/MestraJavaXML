package gui;

import java.awt.Color;

import javax.swing.table.DefaultTableCellRenderer;

import configuration.MethodLevelSafety;
import mestra.CSCI_SSSRequirementAllocationList;
import mestra.IdentifierList;
import mestra.MestraIdentifier;
import mestra.MestraRevision;
import mestra.MestraTitle;
import mestra.Trace_MF_SRS;
import mestra.Trace_SDD_SRS;
import mestra.Trace_SRS_MF;
import mestra.Trace_SRS_SDD;
import mestra.Trace_SRS_SSS;
import mestra.Trace_SRS_TS;
import mestra.Trace_SSS_SRS;
import mestra.Trace_TS_SRS;

/**
 * The aim of this class is to color cells in the table view
 * @author PASTOR Robert
 *
 */
public class ColoredTableCellRenderer extends DefaultTableCellRenderer
{
    /**
     * 
     */
    private static final long serialVersionUID = -7530572436813577780L;

    private Color URGENCY_COLOR = Color.RED;
    private Color WARNING_COLOR = Color.YELLOW;
    private Color DEFAULT_BACKGROUND_COLOR = Color.WHITE;
    //private Color DEFAULT_BACKGROUND_COLOR = Color.BLACK;
    private Color DEFAULT_FOREGROUND_COLOR = Color.BLACK;

    public ColoredTableCellRenderer() {
        super();
    }
    
    // in a TS, if no references to SRS requirements then error in Excel results
    // in the table view, wrong back ground color when cell is selected
    //MestraToolVersion = "V0.0.12 dated 26 October 2007";

    public void setValue(Object value)  {
        
        this.setBackground(DEFAULT_BACKGROUND_COLOR);
        this.setForeground(DEFAULT_FOREGROUND_COLOR);
        
        if (value instanceof MestraIdentifier) {

            MestraIdentifier mestraIdentifier = (MestraIdentifier)value;
            if (mestraIdentifier.isDuplicated()) {
                this.setBackground(URGENCY_COLOR);
                this.setForeground(DEFAULT_FOREGROUND_COLOR);
            }
            else {
                if (mestraIdentifier.containsIllegalCharacters()) {
                    this.setBackground(WARNING_COLOR);
                    this.setForeground(DEFAULT_FOREGROUND_COLOR);
                }
                else {
                	this.setBackground(DEFAULT_BACKGROUND_COLOR);
                	this.setForeground(DEFAULT_FOREGROUND_COLOR);
                }
            }
            setText(mestraIdentifier.getIdentifier());
        }

        if (value instanceof MethodLevelSafety) {

        	MethodLevelSafety methodLevelSafety = (MethodLevelSafety)value;
            if (methodLevelSafety.isMethodLevelSafetyEmpty() || 
            		methodLevelSafety.isOneLevelNotExisting() || 
            		methodLevelSafety.isOneMethodNotExisting()) {
                this.setBackground(WARNING_COLOR);
                this.setForeground(DEFAULT_FOREGROUND_COLOR);
            }
            else {
            	this.setBackground(DEFAULT_BACKGROUND_COLOR);
            	this.setForeground(DEFAULT_FOREGROUND_COLOR);
            }
            setText(methodLevelSafety.getStrMethodLevelSafety());
        }

        if (value instanceof MestraTitle) {
            MestraTitle mestraTitle = (MestraTitle)value;
            if (mestraTitle.isEmpty() ) {
                this.setBackground(WARNING_COLOR);
            }
            else {
            	this.setBackground(DEFAULT_BACKGROUND_COLOR);
            	this.setForeground(DEFAULT_FOREGROUND_COLOR);
            }
            setText(mestraTitle.getTitle());	
        }
        
        if (value instanceof MestraRevision) {
        	MestraRevision mestraRevision = (MestraRevision)value;
            if (mestraRevision.isEmpty() || mestraRevision.hasMultipleMarkers()) {
                this.setForeground(DEFAULT_FOREGROUND_COLOR);
                this.setBackground(WARNING_COLOR);
            }
            else {
            	this.setBackground(DEFAULT_BACKGROUND_COLOR);
            	this.setForeground(DEFAULT_FOREGROUND_COLOR);
            }
            setText(mestraRevision.getRevision());	
        }

        if (value instanceof CSCI_SSSRequirementAllocationList) {
            CSCI_SSSRequirementAllocationList aCSCI_SSSRequirementAllocationList = (CSCI_SSSRequirementAllocationList)value;
            if ( (aCSCI_SSSRequirementAllocationList.containsOneUnknownCSCI() == true ) || 
                    (aCSCI_SSSRequirementAllocationList.isEmpty()) ) {
                this.setBackground(WARNING_COLOR);
            }
            else {
            	this.setBackground(DEFAULT_BACKGROUND_COLOR);
            	this.setForeground(DEFAULT_FOREGROUND_COLOR);
            }
            setText(aCSCI_SSSRequirementAllocationList.getStrRequirementAllocation());    
        }
        
        if (value instanceof IdentifierList) {
        	IdentifierList aIdentifierList = (IdentifierList) value;
        	if (aIdentifierList.isEmpty()) {
                this.setBackground(WARNING_COLOR);        		
        	}
        	else {
        		// if in a SRS reference list to SSS Requirements there is Req_Derived it should be alone
        		// hence the size of the list can then not be > 1
        		if ((aIdentifierList.containsReqDerived()) && (aIdentifierList.size()>1)) {
        			this.setBackground(WARNING_COLOR);
        		}
        		else {
                	this.setBackground(DEFAULT_BACKGROUND_COLOR);
                	this.setForeground(DEFAULT_FOREGROUND_COLOR);        			
        		}
        	}
        	setText(aIdentifierList.getIdentifierListString());
        }
        
        if (value instanceof Trace_SSS_SRS) {
        	Trace_SSS_SRS trace_SSS_SRS = (Trace_SSS_SRS)value;
            if ( (trace_SSS_SRS.isAllocated() == true) && 
                    (trace_SSS_SRS.isCovered() == false) ) {
                this.setBackground(URGENCY_COLOR);
            }
            else {
            	if ( (trace_SSS_SRS.isAllocated() == false) && 
            		 (trace_SSS_SRS.isCovered() == true) ) {
            		this.setBackground(WARNING_COLOR);
            	}
            	else {
                	this.setBackground(DEFAULT_BACKGROUND_COLOR);
                	this.setForeground(DEFAULT_FOREGROUND_COLOR);            		
            	}
            }
            setText(trace_SSS_SRS.getSRSIdentifier());    
        }
        
        if (value instanceof Trace_SRS_SSS) {
        	Trace_SRS_SSS trace_SRS_SSS = (Trace_SRS_SSS)value;           
        	if (trace_SRS_SSS.isSSSreferenceUnknown()) {
        		this.setBackground(WARNING_COLOR);
        	}
        	else {
            	this.setBackground(DEFAULT_BACKGROUND_COLOR);
            	this.setForeground(DEFAULT_FOREGROUND_COLOR);            		
        	}
            setText(trace_SRS_SSS.getSSSIdentifier());    
        }
        
        if (value instanceof Trace_SRS_TS) {
        	Trace_SRS_TS trace_SRS_TS = (Trace_SRS_TS)value;           
        	if (trace_SRS_TS.isCovered() == false) {
        		this.setBackground(URGENCY_COLOR);
        	}
        	else {
            	this.setBackground(DEFAULT_BACKGROUND_COLOR);
            	this.setForeground(DEFAULT_FOREGROUND_COLOR);            		
        	}
            setText(trace_SRS_TS.getTSIdentifier());    
        }
        
        if (value instanceof Trace_TS_SRS) {
        	Trace_TS_SRS trace_TS_SRS = (Trace_TS_SRS)value;           
        	if (trace_TS_SRS.isSRSreferenceUnknow()) {
        		this.setBackground(WARNING_COLOR);
        	}
        	else {
            	this.setBackground(DEFAULT_BACKGROUND_COLOR);
            	this.setForeground(DEFAULT_FOREGROUND_COLOR);            		
        	}
            setText(trace_TS_SRS.getSRSIdentifier());    
        }
        
        if (value instanceof Trace_SRS_SDD) {
        	Trace_SRS_SDD trace_SRS_SDD = (Trace_SRS_SDD)value;           
        	if (trace_SRS_SDD.isCovered() == false) {
        		this.setBackground(URGENCY_COLOR);
        	}
        	else {
            	this.setBackground(DEFAULT_BACKGROUND_COLOR);
            	this.setForeground(DEFAULT_FOREGROUND_COLOR);            		
        	}
            setText(trace_SRS_SDD.getSDDIdentifier());    
        }
        
        if (value instanceof Trace_SDD_SRS) {
        	Trace_SDD_SRS trace_SDD_SRS = (Trace_SDD_SRS)value;           
        	if (trace_SDD_SRS.isSRSreferenceUnknown()) {
        		this.setBackground(WARNING_COLOR);
        	}
        	else {
            	this.setBackground(DEFAULT_BACKGROUND_COLOR);
            	this.setForeground(DEFAULT_FOREGROUND_COLOR);            		
        	}
            setText(trace_SDD_SRS.getSRSIdentifier());    
        }
        
        /**
         * 13 march 2009
         */
        if (value instanceof Trace_SRS_MF) {
        	Trace_SRS_MF trace_SRS_MF = (Trace_SRS_MF)value;           
        	if (trace_SRS_MF.isCovered() == false) {
        		this.setBackground(URGENCY_COLOR);
        	}
        	else {
            	this.setBackground(DEFAULT_BACKGROUND_COLOR);
            	this.setForeground(DEFAULT_FOREGROUND_COLOR);            		
        	}
            setText(trace_SRS_MF.getMFIdentifier());  
        }
        
        if (value instanceof Trace_MF_SRS) {
        	Trace_MF_SRS trace_MF_SRS = (Trace_MF_SRS)value;           
        	if (trace_MF_SRS.isSRSreferenceUnknown()) {
        		this.setBackground(WARNING_COLOR);
        	}
        	else {
            	this.setBackground(DEFAULT_BACKGROUND_COLOR);
            	this.setForeground(DEFAULT_FOREGROUND_COLOR);            		
        	}
            setText(trace_MF_SRS.getSRSIdentifier());  
        }
        
        if (value instanceof String) {
        	setText((String)value);
        }
    }

}




