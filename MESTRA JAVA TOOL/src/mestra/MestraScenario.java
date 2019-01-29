package mestra;

import mestra.MestraFileType.MestraFileTypeEnum;

/**
 * This class manages the different Scenario
 * which can be run with the tool
 * a scenario can be either : 
 * <br>1) TRACE_ALL meaning extract all markers or 
 * <br>2) trace-ability scenarios, 
 * <br>typical trace-ability scenario values are: trace_SSS_SRS, trace_SRS_SDD, trace_SRS_MF, trace_SRS_TS
 *  
 * @author Pastor Robert
 * @since July 2007
 */

public class MestraScenario {

	/**
	 * Defines the different kind of scenario to run
	 */
	public static enum MestraScenarioEnum { trace_All, 
		trace_SSS_SRS,
        trace_SRS_SDD,
		trace_SRS_MF,
		trace_SRS_TS  }

    final static String[] strTraceabilityTypes = { "SSS_SRS", "SRS_SDD" ,"SRS_MF", "SRS_TS" };
    
	private MestraScenarioEnum mestraScenario = MestraScenarioEnum.trace_All;

	public MestraScenario() {
		this.mestraScenario = MestraScenarioEnum.trace_All;
	}
	
	public MestraScenario(String str) {
		this.mestraScenario = getMestraScenarioFromString(str);
	}
	
	public MestraScenario(MestraScenarioEnum mestraScenarioEnum) {
	    this.mestraScenario = mestraScenarioEnum;
	}
	
	/**
	 * @return the mestraScenario
	 */
	public MestraScenarioEnum getMestraScenarioEnum() {
		return mestraScenario;
	}

	/**
	 * @param mestraScenario the mestraScenario to set
	 */
	public void setMestraScenario(MestraScenarioEnum mestraScenario) {
		this.mestraScenario = mestraScenario;
	}

	public void setMestraScenario(String str) {
		this.mestraScenario = getMestraScenarioFromString(str);
	}
	/**
	 * @return the strTraceabilityTypes
	 */
	public String[] getStrTraceabilityTypes() {
		return MestraScenario.strTraceabilityTypes;
	}
	
	public static MestraScenarioEnum getMestraScenarioFromString(String str) {
		if (str.equalsIgnoreCase(strTraceabilityTypes[0])) {
			return MestraScenarioEnum.trace_SSS_SRS;
		}
		else {
			if (str.equalsIgnoreCase(strTraceabilityTypes[1])) {
				return MestraScenarioEnum.trace_SRS_SDD;
			}
			else {
				if (str.equalsIgnoreCase(strTraceabilityTypes[2])) {
					return MestraScenarioEnum.trace_SRS_MF;
				}
				else {
					if (str.equalsIgnoreCase(strTraceabilityTypes[3])) {
						return MestraScenarioEnum.trace_SRS_TS;
					}
					else {
						return MestraScenarioEnum.trace_All;
					}
				}
			}
		}
	}

    // knowing the type of traceability we can get the type of UpStream and DownStream files
    public MestraFileTypeEnum getMestraFileTypeUpStream() {
        switch (mestraScenario) {
        case trace_SSS_SRS:
            return MestraFileType.MestraFileTypeEnum.SSS;
            
        case trace_SRS_SDD:
            return MestraFileType.MestraFileTypeEnum.SRS;
            
        case trace_SRS_MF:
            return MestraFileType.MestraFileTypeEnum.SRS;
            
        case trace_SRS_TS:
            return MestraFileType.MestraFileTypeEnum.SRS;
        
        case trace_All:
            return MestraFileType.MestraFileTypeEnum.SSS;
        }
        return MestraFileType.MestraFileTypeEnum.SSS;
    }
    
     public MestraFileTypeEnum getMestraFileTypeDownStream () {
         switch (mestraScenario) {
         case trace_SSS_SRS:
             return MestraFileType.MestraFileTypeEnum.SRS;
             
         case trace_SRS_SDD:
             return MestraFileType.MestraFileTypeEnum.SDD;
             
         case trace_SRS_MF:
             return MestraFileType.MestraFileTypeEnum.MF;
             
         case trace_SRS_TS:
             return MestraFileType.MestraFileTypeEnum.TS;
             
         case trace_All:
             return MestraFileType.MestraFileTypeEnum.SSS;
         }
         return MestraFileType.MestraFileTypeEnum.SSS;
     }
    
}
