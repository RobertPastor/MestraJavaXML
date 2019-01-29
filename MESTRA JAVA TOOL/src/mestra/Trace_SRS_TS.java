package mestra;

import configuration.ConfigurationFileBaseReader;

public class Trace_SRS_TS extends MestraSRS {

	
	/*
	 * this class traces a SRS requirement to a TS requirement
	 * it is a down stream trace
	 * 
	 */
	private MestraTS mestraTS = null;
	
	public Trace_SRS_TS(MestraSRS mestraSRS,MestraTS aMestraTS, ConfigurationFileBaseReader configuration) {
		super(mestraSRS, configuration);
		if (aMestraTS == null) {
			mestraTS = null;
		}
		else {
			mestraTS = (MestraTS)aMestraTS.clone();
		}
	}
	
	public String getTSIdentifier () {
		if (mestraTS == null) {
			return "";
		}
		else {
			return mestraTS.getIdentifier();
		}
	}
	
	public Trace_SRS_TS getThis() {
		return this;
	}

	/**
	 * @return the mestraTS
	 */
	public MestraTS getMestraTS() {
		return mestraTS;
	}

	/**
	 * @param mestraTS the mestraTS to set
	 */
	public void setMestraTS(MestraTS mestraTS) {
		this.mestraTS = mestraTS;
	}
	
}
