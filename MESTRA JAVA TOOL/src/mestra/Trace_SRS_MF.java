package mestra;

import configuration.ConfigurationFileBaseReader;

public class Trace_SRS_MF extends MestraSRS {

	private MestraMF mestraMF = null;

    public Trace_SRS_MF(MestraSRS mestraSRS, MestraMF aMestraMF, ConfigurationFileBaseReader configuration) {

        super(mestraSRS, configuration);
        if (aMestraMF == null) {
            this.mestraMF = null;
        }
        else {
            this.mestraMF = (MestraMF)aMestraMF.clone();
        }
    }

    public Trace_SRS_MF getThis() {
        return this;
    }

	/**
	 * @return the mestraSDD
	 */
	public MestraMF getMestraMF() {
		return this.mestraMF;
	}

	
	public void setMestraMF(MestraMF aMestraMF) {
		this.mestraMF = aMestraMF;
	}

	public String getMFIdentifier() {
		if (this.mestraMF == null) {
			return "";
		}
		else {
			return this.mestraMF.getIdentifier();
		}
	}

}
