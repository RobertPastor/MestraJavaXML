package configuration;

import java.util.Iterator;
import java.util.SortedSet;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.logging.Logger;


/**
 * 
 * this class stores the CSCI names as extracted from the configuration file.
 * @author Robert Pastor
 * @since April 2007
 */

public class KnownCSCIs {
	
	final static Logger logger = Logger.getLogger(KnownCSCIs.class.getName()); 

	/**
	 * 
	 *  we use here a set to avoid duplicates in the CSCI names
	 *  we use Sorted Set in order to sort alphabethically the names
	 *  when it is time to display them in the list drop down menu in the Trace-ability Tab...
	 */
	
	private SortedSet<String> knownCSCIs = null;

	public  KnownCSCIs() {
		logger.info( "Known CSCIs init");
		this.knownCSCIs = new TreeSet<String>();
	}
	
	private String removeSpaces(String s) {
		StringTokenizer st = new StringTokenizer(s," ",false);
		String t = "";
		while (st.hasMoreElements()){
			t += st.nextElement();
		}
		return t;
	}
	
	public boolean add(String strCSCI) {
		//logger.info( "Known CSCIs: add: "+strCSCI);
		return this.knownCSCIs.add(removeSpaces(strCSCI.toUpperCase()));
		
	}
	
	public boolean exists(String Searched_CSCI) {
		if (this.knownCSCIs.isEmpty() == false) {
			Iterator<String> Iter = this.knownCSCIs.iterator();
			while ( Iter.hasNext() ){
				String str = Iter.next();
				if (str.equalsIgnoreCase(Searched_CSCI)) {
					return true;
				}
			}
		}
		return false;
	}

    public SortedSet<String> getKnownCSCIs() {
        return this.knownCSCIs;
    }

    public Iterator<String> iterator() {
    	return this.knownCSCIs.iterator();
    }
}
