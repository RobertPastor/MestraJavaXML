package mestra;

import configuration.ConfigurationFileBaseReader;

public class Trace_MF_SRS extends MestraMF {

    private MestraSRS mestraSRS = null;
    private boolean UnknownSRSreference = false;

    public Trace_MF_SRS(MestraMF mestraMF, MestraSRS aMestraSRS, ConfigurationFileBaseReader configuration) {

        super(mestraMF, configuration);
        UnknownSRSreference = false;
        if (aMestraSRS == null) {
            mestraSRS = null;
        }
        else {
            mestraSRS = (MestraSRS)aMestraSRS.clone();
        }
    }

    public void setSRSreferenceUnknown(boolean bool) {
        UnknownSRSreference = bool;
    }

    public Trace_MF_SRS getThis() {
        return this;
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

	/**
	 * @return the unknownSRSreference
	 */
	public boolean isSRSreferenceUnknown() {
		return UnknownSRSreference;
	}

	/**
	 * @param unknownSRSreference the unknownSRSreference to set
	 */
	public void setUnknownSRSreference(boolean unknownSRSreference) {
		UnknownSRSreference = unknownSRSreference;
	}

	public String getSRSIdentifier() {
		if (mestraSRS == null) {
			return "";
		}
		else {
			return mestraSRS.getIdentifier();
		}
	}

}
