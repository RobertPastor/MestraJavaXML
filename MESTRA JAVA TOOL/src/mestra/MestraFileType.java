package mestra;

/**
 * 
 * @author PASTOR Robert
 * @since September 2007
 *
 */
public class MestraFileType {

    public static enum MestraFileTypeEnum { SSS, SRS, SDD, MF, TS  }
    static String[] strFileTypes = { "SSS", "SRS", "SDD" , "MF", "TS" };

    private MestraFileTypeEnum mestraFileType = MestraFileTypeEnum.SSS;
    private String strMestraFileType = "";

    public MestraFileType () {
        this.mestraFileType = MestraFileTypeEnum.SSS;
        strMestraFileType = strFileTypes[MestraFileTypeEnum.SSS.ordinal()];
    }

    public MestraFileType(MestraFileTypeEnum aMestraFileType) {
        this.mestraFileType = aMestraFileType;
        strMestraFileType = strFileTypes[aMestraFileType.ordinal()];
    }

    public MestraFileType(String strFileType) {
        internalSet(strFileType);
    }

    public String getStrMestraFileType() {
        return strMestraFileType;
    }

    public void setStrMestraFileType (String strFileType) {
        internalSet(strFileType);
    }

    private void internalSet(String strFileType) {
        if ((strFileType.equalsIgnoreCase("SSS")) || (strFileType.equalsIgnoreCase("SCN_SSS"))) {
            this.mestraFileType = MestraFileTypeEnum.SSS;
            this.strMestraFileType = "SSS";
        }
        else {
            if ((strFileType.equalsIgnoreCase("SRS")) || (strFileType.equalsIgnoreCase("SCN_SRS"))) {
                this.mestraFileType = MestraFileTypeEnum.SRS;
                this.strMestraFileType = "SRS";
            }
            else {
                if (strFileType.equalsIgnoreCase("MF")) {
                    this.mestraFileType = MestraFileTypeEnum.MF;
                    this.strMestraFileType = "MF";
                }
                else {
                    if (strFileType.equalsIgnoreCase("SDD")) {
                        this.mestraFileType = MestraFileTypeEnum.SDD;
                        this.strMestraFileType = "SDD";
                    }
                    else {
                        if (strFileType.equalsIgnoreCase("TS")) {
                            this.mestraFileType = MestraFileTypeEnum.TS;
                            this.strMestraFileType = "TS";
                        }
                        else {
                            this.mestraFileType = MestraFileTypeEnum.SSS;
                            this.strMestraFileType = "SSS";
                        }
                    }
                }
            }
        }   
    }


    public boolean CheckFileType(String strFileType) {

        if (  (strFileType.equalsIgnoreCase("SSS")) || (strFileType.equalsIgnoreCase("SCN_SSS")) ||
                (strFileType.equalsIgnoreCase("SRS")) || (strFileType.equalsIgnoreCase("SCN_SRS")) ||
                (strFileType.equalsIgnoreCase("MF"))  || (strFileType.equalsIgnoreCase("SDD"))     ||
                (strFileType.equalsIgnoreCase("TS")) ) {
            return true;
        }
        return false;
    }

    public MestraFileTypeEnum getMestraFileType() {
        return mestraFileType;
    }

    public void setMestraFileType(MestraFileTypeEnum aMestraFileType) {
        this.mestraFileType = aMestraFileType;
        strMestraFileType = strFileTypes[aMestraFileType.ordinal()];
    }

    public boolean Is(MestraFileTypeEnum mestraFileTypeEnum) {
        return (mestraFileType == mestraFileTypeEnum);
    }

    public boolean isSSS() {
        return (mestraFileType == MestraFileTypeEnum.SSS);
    }

    public boolean isSRS() {
        return (mestraFileType == MestraFileTypeEnum.SRS);
    }

    public boolean isSDD() {
        return (mestraFileType == MestraFileTypeEnum.SDD);
    }

    public boolean isMF() {
        return (mestraFileType == MestraFileTypeEnum.MF);
    }

    public boolean isTS() {
        return (mestraFileType == MestraFileTypeEnum.TS);
    }

    public static String getStrFileType(MestraFileTypeEnum aMestraFileType ) {
        return strFileTypes[aMestraFileType.ordinal()];
    }

    /**
     * @return the strFileTypes
     */
    public static String[] getStrFileTypes() {
        return strFileTypes;
    }
}
