package mestra;

import configuration.ConfigurationFileBaseReader;
import configuration.MestraStylesMap;


/**
 * Class managing a MESTRA SSS set of MESTRA markers for one requirement.
 * @author t0007330 - Robert PASTOR
 * @since June 2007
 *
 */
public class MestraSSS extends MestraBase implements Cloneable {

	private String strRequirementAllocation = "";
	private CSCI_SSSRequirementAllocationList requirementAllocationList = null;
	
	
	public MestraSSS(MestraStylesMap mestraStylesMap) {
		super(mestraStylesMap);
		this.strRequirementAllocation = "";
		this.requirementAllocationList = new CSCI_SSSRequirementAllocationList();
	}
	
	public MestraSSS(MestraSSS mestraSSS, ConfigurationFileBaseReader configuration) {
		super((MestraBase)mestraSSS, configuration);
		this.strRequirementAllocation = mestraSSS.strRequirementAllocation;
		this.requirementAllocationList = mestraSSS.requirementAllocationList;		
	}
	
	/*public MestraSSS(String Identifier) {
		super(Identifier);
		this.strRequirementAllocation = "";
		this.requirementAllocationList = null;
	}*/
	
	public MestraSSS(String unknown_Reference, MestraStylesMap mestraStylesMap) {
		super(unknown_Reference, mestraStylesMap);
		this.strRequirementAllocation = "";
		this.requirementAllocationList = new CSCI_SSSRequirementAllocationList();

	}

	/**
	 * @return the clone of this
	 */
	public Object clone() {
		MestraSSS mestraSSS = new MestraSSS(this, this.configuration);
        return mestraSSS;
    }

	/**
	 * @return the requirementAllocation
	 */
	public String getRequirementAllocation() {
		return this.strRequirementAllocation;
	}
	
	/**
	 * @param requirementAllocation the requirementAllocation to set
	 * the list of CSCI to which a SSS requirement is allocated
	 * is defined in the Configuration file
	 * we pass here a pointer in order to make the check on this list
	 * and find unknown CSCI names
	 */
	public void setRequirementAllocation(String requirementAllocation, ConfigurationFileBaseReader  configuration) {
		this.strRequirementAllocation = strRequirementAllocation + requirementAllocation;
		this.requirementAllocationList = new CSCI_SSSRequirementAllocationList(strRequirementAllocation, configuration);
	}
	
	/**
	 * @return the requirementAllocationList
	 */
	public CSCI_SSSRequirementAllocationList getRequirementAllocationList() {
		return this.requirementAllocationList;
	}
	
	/**
	 * @param requirementAllocationList the requirementAllocationList to set
	 */
	public void setRequirementAllocationList(
			CSCI_SSSRequirementAllocationList requirementAllocationList) {
		this.requirementAllocationList = requirementAllocationList;
	}
	
	/**
	 * 
	 * This method tells you if a SSS Requirement is allocated to a CSCI
	 * 
	 * @param String strCSCI
	 * @return TRUE if the CSCI provided in parameter is in the allocation List, 
	 * FALSE otherwise
	 */
	public boolean isAllocated(String strCSCI) {
		if (this.requirementAllocationList != null) {
			return this.requirementAllocationList.contains(strCSCI);
		}
		else {
			return false;
		}
	}
	
	// in the trace-ability runs there is a special case where the upstream file is a CP-OH
	// CP-OH requirements are not allocated - have no allocation
	public boolean isCPOH() {
		if ((this.strRequirementAllocation.length()==0) || (this.requirementAllocationList == null)) {
			return true;
		}
		return false;
	}
	
	public void computeWarningsErrors() {
		if (this.requirementAllocationList.isEmpty()) {
			addWarning("Empty Allocation List!");
		}
		if (this.requirementAllocationList.containsOneUnknownCSCI()) {
			addWarning("Unknown CSCIs in Allocation List!");
		}
		super.computeWarningsErrors();
	}
}
