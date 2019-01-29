package mestra;

/**
 * 
 * @author Pastor
 *
 */
public class CSCI_Allocation {

	String CSCI_Name = "";
	int NbrReferences = 0;
	boolean CSCI_NameExisting = false;
	
	/**
	 * constructor
	 */
	public CSCI_Allocation() {
		CSCI_Name = "";
		CSCI_NameExisting = false;
		NbrReferences = 0;
	}

	/**
	 * @return the cSCI_Name
	 */
	public String getCSCI_Name() {
		return CSCI_Name;
	}

	/**
	 * @param name the cSCI_Name to set
	 */
	public void setCSCI_Name(String name) {
		CSCI_Name = name;
	}

	/**
	 * @return the nbrReferences
	 */
	public int getNbrReferences() {
		return NbrReferences;
	}

	/**
	 * @param nbrReferences the nbrReferences to set
	 */
	public void setNbrReferences(int nbrReferences) {
		NbrReferences = nbrReferences;
	}

	/**
	 * @return the CSCI_NameExists
	 */
	public boolean isCSCI_NameExisting() {
		return CSCI_NameExisting;
	}

	/**
	 * @param nameExists the cSCI_NameExists to set
	 */
	public void setCSCI_NameExisting(boolean nameExists) {
		CSCI_NameExisting = nameExists;
	}
	
}
