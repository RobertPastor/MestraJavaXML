package mestra;

import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.StringTokenizer;
import java.util.TreeMap;

import configuration.ConfigurationFileBaseReader;
import configuration.KnownCSCIs;

/**
 * class responsible for managing a list of req allocated to a set of CSCI.
 * @since 2007
 * @author Pastor Robert
 *
 */
public class CSCI_SSSFileAllocationList {
	
	//private static final Logger logger = Logger.getLogger(CSCI_SSSFileAllocationList.class.getName()); 

	private SortedMap<String, CSCI_Allocation> CSCI_SSSFileAllocationList = null;

	public CSCI_SSSFileAllocationList() {
		//logger.info( " constructor ");
		this.CSCI_SSSFileAllocationList = new TreeMap<String, CSCI_Allocation>();
	}
	
	/**
	 * remove all mappings.
	 */
	public void clear() {
		this.CSCI_SSSFileAllocationList.clear();
	}


	private String removeSpaces(String s) {
		StringTokenizer st = new StringTokenizer(s," ",false);
		String t="";
		while (st.hasMoreElements()) {
			t += st.nextElement();
		}
		return t;
	}

	/**
	 * increment the number of times a CSCI name is found in a SSS file
	 * if not existing create the entry
	 */ 
	public void increment(String strCSCI, ConfigurationFileBaseReader configuration) {

		if (this.CSCI_SSSFileAllocationList.containsKey(removeSpaces(strCSCI).toUpperCase())) {
			
			// we have to browse through the list of CSCI already found
			Iterator<Map.Entry<String, CSCI_Allocation>> Iter = this.CSCI_SSSFileAllocationList.entrySet().iterator();
			while (Iter.hasNext()) {
				Map.Entry<String, CSCI_Allocation> entry = Iter.next();
				
				if (entry.getKey().equalsIgnoreCase(removeSpaces(strCSCI).toUpperCase())) {		
					
					CSCI_Allocation aCSCI_Allocation = entry.getValue();
					int NbrRef = aCSCI_Allocation.getNbrReferences();
					
					NbrRef++;
					aCSCI_Allocation.setNbrReferences(NbrRef);
				}
			}
			
		} else {
			// list of CSCI defined in the configuration file
			KnownCSCIs knownCSCIs = configuration.getKnown_CSCIs();
			
			// create a new entry
			String key = removeSpaces(strCSCI).toUpperCase();
			CSCI_Allocation aCSCI_Allocation = new CSCI_Allocation();
			aCSCI_Allocation.setCSCI_Name(key);
			aCSCI_Allocation.setNbrReferences((int)1);
			// we set the fact that the CSCI exists
			aCSCI_Allocation.setCSCI_NameExisting(knownCSCIs.exists(strCSCI));

			this.CSCI_SSSFileAllocationList.put(key, aCSCI_Allocation);
			
		}
	}

	public boolean contains(String strCSCI) {

		return 	this.CSCI_SSSFileAllocationList.containsKey(removeSpaces(strCSCI).toUpperCase());
	}

	public void add(ConfigurationFileBaseReader configuration,
			CSCI_SSSRequirementAllocationList SSS_ReqAllocationList) {

		if ( SSS_ReqAllocationList != null) {
			Iterator<String> Iter = SSS_ReqAllocationList.iterator();
			while (Iter.hasNext()) {
				String strCSCI_Name = Iter.next();
				// we have to increment if necessary the number of references
				this.increment(strCSCI_Name,configuration);	
			}
		}
	}

	public Iterator<Map.Entry<String, CSCI_Allocation>> iterator() {
		return this.CSCI_SSSFileAllocationList.entrySet().iterator();
	}

}