package mestra;

import configuration.ConfigurationFileBaseReader;

/**
 * 
 * This class traces a SSS to a SRS :<br> 
 * it corresponds to one record-row of the trace-ability matrix.<br>
 * @author Robert Pastor
 * @since June 2007
 * 
 */

public class Trace_SSS_SRS extends MestraSSS {


	private MestraSRS mestraSRS = null;
	private String traceabilityCSCI = "";

	/**
	 * returns the SRS Identifier of the trace_SSS_SRS or ""
	 * @return
	 */
	public String getSRSIdentifier () {
		if (this.mestraSRS == null) {
			return "";
		}
		else {
			return this.mestraSRS.getIdentifier();
		}
	}

	public Trace_SSS_SRS getThis() {
		return this;
	}

	public Trace_SSS_SRS (MestraSSS mestraSSS, MestraSRS aMestraSRS, String CSCI,  ConfigurationFileBaseReader configuration) {
		super(mestraSSS, configuration);
		traceabilityCSCI = CSCI;
		if (aMestraSRS == null) {
			mestraSRS = null;
		}
		else {
			mestraSRS = (MestraSRS)aMestraSRS.clone();
		}
	}

	/**
	 * @return the mestraSRS
	 */
	public MestraSRS getMestraSRS() {
		return mestraSRS;
	}

	/**
	 * @param mestraSRS the mestraSRS to set
	 */
	public void setMestraSRS(MestraSRS mestraSRS) {
		this.mestraSRS = mestraSRS;
	}
	
	public boolean isAllocated() {
	    if ( super.isAllocated(traceabilityCSCI) ) {
	        return true;
	    }
	    else
	        if ( super.isCPOH() && ( traceabilityCSCI.equalsIgnoreCase("ODS") )) {
	            return true;
	        }
	    return false;
	}
}



