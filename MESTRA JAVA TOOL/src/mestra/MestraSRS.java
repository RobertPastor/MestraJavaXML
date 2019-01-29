package mestra;

import java.util.Iterator;
import java.util.StringTokenizer;

import configuration.ConfigurationFileBaseReader;
import configuration.MestraStylesMap;

/**
 * This class extends MestraBase
 * This class is dedicated to manage MESTRA MARKERS for a SRS
 * @author Pastor Robert
 *
 */

public class MestraSRS extends MestraBase implements Cloneable {

	private String strSSS_References;
	private IdentifierList SSS_ReferencesList = null;

	/**
	 * Constructor
	 */
	public MestraSRS(MestraStylesMap mestraStylesMap) {
		super(mestraStylesMap);
		this.strSSS_References = "";
		this.SSS_ReferencesList = new IdentifierList();
	}
	
	
	public MestraSRS (MestraSRS mestraSRS , ConfigurationFileBaseReader configuration) {
		super((MestraBase)mestraSRS, configuration);
		this.strSSS_References = mestraSRS.strSSS_References;
		this.SSS_ReferencesList = mestraSRS.SSS_ReferencesList;
	}
	
	public MestraSRS(String unknown_reference, MestraStylesMap mestraStylesMap) {
		super(unknown_reference, mestraStylesMap);
		this.strSSS_References = "";
		this.SSS_ReferencesList = new IdentifierList();
	}

	/**
	 * @return the clone of this
	 */
	public Object clone() {      
        MestraSRS mestraSRS = new MestraSRS(this, this.configuration);
        return mestraSRS;
    }
	
	/**
	 * @return the strSSS_References
	 */
	public String getStrSSS_References() {
		return this.strSSS_References;
	}

	/**
	 * 
	 * @param String s
	 * @return the input String without any spaces in it
	 */
	private String removeSpaces(String s) {
		StringTokenizer st = new StringTokenizer(s," ",false);
		String t="";
		while (st.hasMoreElements()) t += st.nextElement();
		return t;
	}
	
private String tabRemover(String s) {
		
		StringTokenizer st = new StringTokenizer(s,"\t",false);
		String t="";
		while (st.hasMoreElements()) t += st.nextElement();
		return t;
	}

	/**
	 * initializes the SSS References List
	 */
	private void initSSS_ReferencesList() {
		SSS_ReferencesList.clear();
		StringTokenizer strToken = new StringTokenizer(strSSS_References,"/");
		while (strToken.hasMoreTokens()) {
			String str = strToken.nextToken();
			str = removeSpaces(str);
			str = tabRemover(str);
			SSS_ReferencesList.add(str);
		}
	}
	
	/**
	 * @param strSSS_References the strSSS_References to set
	 */
	public void setStrSSS_References(String strSSS_References) {
		this.strSSS_References = this.strSSS_References + strSSS_References;
		initSSS_ReferencesList();
	}

	/**
	 * @return the sSS_ReferencesList
	 */
	public IdentifierList getSSS_ReferencesList() {
		return this.SSS_ReferencesList;
	}
	/**
	 * @param referencesList the sSS_ReferencesList to set
	 */
	public void setSSS_ReferencesList(IdentifierList referencesList) {
		this.SSS_ReferencesList = referencesList;
	}

	public boolean IsCovering(String SSS_Identifier) {
		if (this.SSS_ReferencesList == null) {
			return false;
		}
		else {
			Iterator<String> Iter = this.SSS_ReferencesList.iterator();
			while (Iter.hasNext()) {
				if (Iter.next().equalsIgnoreCase(SSS_Identifier)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean isReqDerived() {
		if (this.SSS_ReferencesList != null) {
			if (this.SSS_ReferencesList.containsReqDerived()) {
				return true;
			}
		}
		return false;
	}
	
	public void computeWarningsErrors() {
		
		if (this.SSS_ReferencesList.isEmpty()) {
			addWarning("Empty SSS References List!");
		}
		if ((this.SSS_ReferencesList.containsReqDerived()) && (this.SSS_ReferencesList.size()>1)) {
			addWarning("if Req Derived cannot link to others references!");
		}
		super.computeWarningsErrors();
	}

}
