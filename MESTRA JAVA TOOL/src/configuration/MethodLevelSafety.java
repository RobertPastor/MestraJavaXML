package configuration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.logging.Logger;

/**
 * class to manage Method Level and Safety as extracted from the configuration file.
 * @author t0007330
 * @since July 2016
 *
 */
public class MethodLevelSafety {

	final static Logger logger = Logger.getLogger(MethodLevelSafety.class.getName()); 

	// I A D T
	private ArrayList<String> Method = null;
	// CSCI System ...
	private ArrayList<String> Level = null;

	final static public String strSAFETY = "SAFETY";

	private String  strMethodLevelSafety = "";
	
	private boolean OneMethodNotExisting = false;
	private boolean OneLevelNotExisting = false;
	private boolean Safety = false;

	public MethodLevelSafety() {
		strMethodLevelSafety = "";
		
		OneMethodNotExisting = false;
		OneLevelNotExisting = false;
		Safety = false;
		
		Method = new ArrayList<String>();
		Level = new ArrayList<String>();
	}

	public MethodLevelSafety (MethodLevelSafety methodLevelSafety) {
		strMethodLevelSafety = RemoveSpaces(methodLevelSafety.strMethodLevelSafety);
		
		OneMethodNotExisting = methodLevelSafety.OneLevelNotExisting;
		OneLevelNotExisting = methodLevelSafety.OneLevelNotExisting;
		Safety = methodLevelSafety.isSafety();
		
		Method = new ArrayList<String>();
		Level = new ArrayList<String>();		
	}

	public MethodLevelSafety(String aStrMethodLevelSafety) {
		strMethodLevelSafety = RemoveSpaces(aStrMethodLevelSafety);
		
		OneMethodNotExisting = false;
		OneLevelNotExisting = false;
		
		Method = new ArrayList<String>();
		Level = new ArrayList<String>();
	}

	public MethodLevelSafety(String aStrMethodLevelSafety, 
			ConfigurationFileBaseReader aConfiguration) {
		strMethodLevelSafety = RemoveSpaces(aStrMethodLevelSafety);
		
		OneMethodNotExisting = false;
		OneLevelNotExisting = false;
		
		Method = new ArrayList<String>();
		Level = new ArrayList<String>();
		if (strMethodLevelSafety.length() > 0) {
			checkMethodLevelSafety(aConfiguration);
		}
	}

	private String RemoveSpaces(String source) {

		StringTokenizer st = new StringTokenizer(source," ",false);
		String t="";
		while (st.hasMoreElements()) t += st.nextElement();
		return t;
	}

	private String convertDash173toDash45(String source) {

		String char173 = new Character((char)173).toString();
		StringTokenizer st = new StringTokenizer(source,char173,false);
		String t="";
		while (st.hasMoreElements()) {
			if (t.length() == 0) {
				t = t + st.nextElement();
			}
			else {
				t = t + new Character((char)45).toString() + st.nextElement();
			}
		}
		return t;
	}

	public boolean setMethodLevelSafety(String aStrMethodLevelSafety, 
			ConfigurationFileBaseReader configuration) {

		strMethodLevelSafety = RemoveSpaces(aStrMethodLevelSafety);
		strMethodLevelSafety = convertDash173toDash45(strMethodLevelSafety);
		
		OneMethodNotExisting = false;
		OneLevelNotExisting = false;
		
		Method = new ArrayList<String>();
		Level = new ArrayList<String>();
		if (strMethodLevelSafety.length() > 0) {
			return checkMethodLevelSafety(configuration);
		}
		return false;
	}

	public boolean isSafety(ConfigurationFileBaseReader aConfiguration) {
		
		Safety = false;
	    if (strMethodLevelSafety.length()==0) return false;
	    
		StringTokenizer strToken1 = new StringTokenizer(strMethodLevelSafety,"/");
		while (strToken1.hasMoreTokens()) {
			String tok1 = strToken1.nextToken();
			if (tok1.equalsIgnoreCase(MethodLevelSafety.strSAFETY)) {
				Safety = true;
				return true;
			}
		}
		return false;
	}

	private boolean checkMethodLevelSafety(ConfigurationFileBaseReader aConfiguration) {

	    // we are first looking to split the string in token separated by a SLASH
	    // if no SLASH then we look in each item to split in token separated by a DASH
	    
		isSafety(aConfiguration);
		StringTokenizer strToken1 = new StringTokenizer(strMethodLevelSafety,"/");
		while (strToken1.hasMoreTokens()) {
		    
			String tok1 = strToken1.nextToken();
			if (tok1.equalsIgnoreCase(MethodLevelSafety.strSAFETY)==false) {

			    // this is taken from the configuration file
			    // the list of authorized IADT key letters
				MethodLevelSafety configurationMethodLevelSafety = aConfiguration.getMethodLevelSafety();

				StringTokenizer strToken2 = new StringTokenizer(tok1,"-");
				if (strToken2.countTokens() > 0) {

					String tok2 = strToken2.nextToken();
					// first token is always IADT method 
					if (configurationMethodLevelSafety.methodExists(tok2) == true) {
						Method.add(tok2);
					}
					else {	
						logger.log(java.util.logging.Level.SEVERE, "checkMethodLevelSafety: Method : "+tok2+" does not exist");
						OneMethodNotExisting = true;
					}
					if (strToken2.countTokens() > 0) {
						String tok3 = strToken2.nextToken();
						// second one is always Level Method
						if (configurationMethodLevelSafety.levelExists(tok3) == true) {
							Level.add(tok3);
						}
						else {
							logger.info( "MestraBase: checkMethodLevelSafety: Level : "+tok3+" does not exist");
							OneLevelNotExisting = true;
						}
					}
				}
			}
			else {
				Safety = true;
			}
		}
		if ((OneMethodNotExisting == true) || (OneLevelNotExisting == true)) {
			return false;
		}
		else {
			return true;			
		}
	}

	public boolean addMethod(String strMethod) {
		
		return (this.Method.add(strMethod));
	}

	public boolean addLevel(String strLevel) {
		return (this.Level.add(strLevel));
	}

	public boolean methodExists(String strMethod) {
		Iterator<String> Iter = Method.iterator();
		while (Iter.hasNext()) {
			if (Iter.next().equalsIgnoreCase(strMethod)) {
				return true;
			}
		}
		return false;
	}

	public boolean levelExists(String strLevel) {
		Iterator<String> Iter = Level.iterator();
		while (Iter.hasNext()) {
			if (Iter.next().equalsIgnoreCase(strLevel)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @return the level
	 */
	public ArrayList<String> getLevel() {
		return this.Level;
	}

	/**
	 * @param level the level to set
	 */
	public void setLevel(ArrayList<String> level) {
		this.Level = level;
	}

	/**
	 * @return the method
	 */
	public ArrayList<String> getMethod() {
		return this.Method;
	}

	/**
	 * @param method the method to set
	 */
	public void setMethod(ArrayList<String> method) {
		this.Method = method;
	}

	/**
	 * @return the oneLevelNotExisting
	 */
	public boolean isOneLevelNotExisting() {
		return this.OneLevelNotExisting;
	}

	/**
	 * @return the oneMethodNotExisting
	 */
	public boolean isOneMethodNotExisting() {
		return this.OneMethodNotExisting;
	}

	/**
	 * @return the strMethodLevelSafety
	 */
	public String getStrMethodLevelSafety() {
		return this.strMethodLevelSafety;
	}

	/**
	 * @param strMethodLevelSafety the strMethodLevelSafety to set
	 */
	public void setStrMethodLevelSafety(String strMethodLevelSafety) {
		this.strMethodLevelSafety = RemoveSpaces(strMethodLevelSafety);
	}

	/**
	 * @return the methodLevelSafetyEmpty
	 */
	public boolean isMethodLevelSafetyEmpty() {
		return (this.strMethodLevelSafety.length()==0);
	}

	public boolean isSafety() {
		return this.Safety;
	}

	public void setSafety(boolean safety) {
		this.Safety = safety;
	}
	
	public String getMethodLevelStr() {

		String result = "";
		StringTokenizer strToken1 = new StringTokenizer(this.strMethodLevelSafety,"/");
		while (strToken1.hasMoreTokens()) {
		    
			String tok1 = strToken1.nextToken();
			if (tok1.equalsIgnoreCase(MethodLevelSafety.strSAFETY)==false) {
				result = result + tok1;
			}
		}
		return result;
	}

}
