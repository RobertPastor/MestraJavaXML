package mestra;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

import configuration.ConfigurationFileBaseReader;
import configuration.KnownCSCIs;

/**
 * This class manages the set of CSCI names
 * to which ONE SSS requirement is allocated
 * 
 * @author Pastor Robert
 * 
 */
public class CSCI_SSSRequirementAllocationList {

	// we use here a set because there is no point having duplicates
	// indeed a SSS Requirement should not have more than one occurrence of a CSCI name
	
	private Set<String> CSCI_AllocationList;
	private String strCSCI_Allocation = "";
	private boolean OneUnknownCSCI = false;
	
    public boolean containsOneUnknownCSCI() {
        return OneUnknownCSCI;
    }
    
    /**
     * Returns TRUE if the Set is empty
     * @return true if the set of CSCI allocated to the SSS Requirement is EMPTY
     * @author Pastor Robert
     * @since June 2007
     */
    public boolean isEmpty() {
        return (CSCI_AllocationList.isEmpty());
    }
    
    public CSCI_SSSRequirementAllocationList() {

        //CSCI_AllocationList = new HashSet<String>();
        CSCI_AllocationList = new TreeSet<String>();
        strCSCI_Allocation = "";
        OneUnknownCSCI = false;

    }
    
    public CSCI_SSSRequirementAllocationList(String str, ConfigurationFileBaseReader configuration) {

        CSCI_AllocationList = new HashSet<String>();
        strCSCI_Allocation = str;
        OneUnknownCSCI = false;
        
        KnownCSCIs  knownCSCIs = configuration.getKnown_CSCIs();

        StringTokenizer strToken = new StringTokenizer(str,"/");
        while (strToken.hasMoreTokens()) {
            String strCSCI = removeSpaces(strToken.nextToken().toUpperCase());
            if (knownCSCIs.exists(strCSCI) == false) {
                OneUnknownCSCI = true;
            }
            CSCI_AllocationList.add(strCSCI);
        }
    }
	
	private String removeSpaces(String s) {
		StringTokenizer st = new StringTokenizer(s," ",false);
		String t="";
		while (st.hasMoreElements()) t += st.nextElement();
		return t;
	}

	/**
	 * @return the requirementAllocationList
	 */
	public Set<String> getRequirementAllocationList() {
		return CSCI_AllocationList;
	}

	/**
	 * @return the CSCI_AllocationList
	 */
	public Set<String> getCSCI_AllocationList() {
		return CSCI_AllocationList;
	}
	/**
	 * @param allocationList the cSCI_AllocationList to set
	 */
	public void setCSCI_AllocationList(Set<String> allocationList) {
		CSCI_AllocationList = allocationList;
	}
	/**
	 * @return the strRequirementAllocation
	 */
	public String getStrRequirementAllocation() {
		return strCSCI_Allocation;
	}
	/**
	 * @param strRequirementAllocation the strRequirementAllocation to set
	 */
	public void setStrRequirementAllocation(String strRequirementAllocation) {
		this.strCSCI_Allocation = strRequirementAllocation;
	}
	/**
	 * @param requirementAllocationList the requirementAllocationList to set
	 */
	public void setRequirementAllocationList(
			Set<String> requirementAllocationList) {
		CSCI_AllocationList = requirementAllocationList;
	}

	public Iterator<String> iterator() {
		return CSCI_AllocationList.iterator();
	}

	public boolean add(String strCSCI) {
		return (CSCI_AllocationList.add(removeSpaces(strCSCI)));
	}

	public void add(CSCI_SSSRequirementAllocationList SSS_ReqAllocationList) {
		if ( SSS_ReqAllocationList != null) {
			Iterator<String> Iter = SSS_ReqAllocationList.iterator();
			while (Iter.hasNext()) {
				CSCI_AllocationList.add(Iter.next());
			}
		}
	}

	public boolean contains (String strCSCI) {
		if ( CSCI_AllocationList.isEmpty() == true) {
			return false;
		}
		else {
			Iterator<String> Iter = CSCI_AllocationList.iterator();
			while (Iter.hasNext()) {
				if (Iter.next().equalsIgnoreCase(strCSCI)) {
					return true;
				}
			}
		}
		return false;
	}
}
