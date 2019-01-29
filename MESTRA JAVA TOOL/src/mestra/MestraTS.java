package mestra;

import java.util.Iterator;
import java.util.StringTokenizer;

import configuration.ConfigurationFileBaseReader;
import configuration.MestraStylesMap;

/**
 * 
 * the <code>MestraTS</code> class stores and manipulates <br>
 * data specific to the MESTRA markers contained in a Test Sheet
 * 
 * @author Pastor Robert
 * @since June 2007
 * 
 * 20 September 2007: improvement to manage SRS Requirements on several rows
 */
public class MestraTS extends MestraBase {

	private String TestedSRSrequirements = "";
	private IdentifierList testedSRSrequirementsList = null;
	

	public MestraTS(MestraStylesMap mestraStylesMap) {
		super(mestraStylesMap);
		TestedSRSrequirements =  "";
		testedSRSrequirementsList = new IdentifierList();
	}
	
	public MestraTS(MestraTS mestraTS, ConfigurationFileBaseReader configuration) {
		super((MestraBase)mestraTS, configuration);
		TestedSRSrequirements = mestraTS.TestedSRSrequirements;
		testedSRSrequirementsList = mestraTS.testedSRSrequirementsList;
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
	 * @return the testedSRSrequirements
	 */
	public String getStrTestedSRSrequirements() {
		return TestedSRSrequirements;
	}

	/**
	 * @param testedSRSrequirements the testedSRSrequirements to set
	 */
	public void setTestedSRSrequirements(String testedSRSrequirements) {
		if (TestedSRSrequirements.length()==0) {
			TestedSRSrequirements = testedSRSrequirements;			
		}
		else {
			TestedSRSrequirements = TestedSRSrequirements + strSeparator + testedSRSrequirements; 
		}
		TestedSRSrequirements = removeSpaces(TestedSRSrequirements); 
		testedSRSrequirementsList.add(TestedSRSrequirements);
	}

	/**
	 * @return the implementedSRSrequirements
	 */
	public IdentifierList getTestedSRSrequirementsList() {
		return testedSRSrequirementsList;
	}

	/**
	 * @param implementedSRSrequirements the implementedSRSrequirements to set
	 */
	public void setTestedSRSrequirements(
			IdentifierList testedSRSrequirements) {
		this.testedSRSrequirementsList = testedSRSrequirements;
	}
	
	public boolean IsCovering(String SRS_Identifier) {
		if (testedSRSrequirementsList == null) {
			return false;
		}
		else {
			Iterator<String> Iter = testedSRSrequirementsList.iterator();
			while (Iter.hasNext()) {
				if (Iter.next().equalsIgnoreCase(SRS_Identifier)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public Object clone() {
        
        MestraTS mestraTS = new MestraTS(this, this.configuration);
        return mestraTS;
    }
	
	// in a TS, if no references to SRS requirements then error in Excel results
    // MestraToolVersion = "V0.0.12 dated 26 October 2007";
	public void computeWarningsErrors() {
		
		if (testedSRSrequirementsList.isEmpty()) {
			addWarning("Empty SRS References List!");
		}
		super.computeWarningsErrors();
	}


}
