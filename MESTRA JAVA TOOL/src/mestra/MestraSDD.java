package mestra;

import java.util.Iterator;
import java.util.StringTokenizer;

import configuration.ConfigurationFileBaseReader;
import configuration.MestraStylesMap;

public class MestraSDD extends MestraBase {

    private String SRSimplementedRequirements = "";
    private IdentifierList implementedSRSrequirements = null;

    public MestraSDD(MestraStylesMap mestraStylesMap) {
        super(mestraStylesMap);
        this.SRSimplementedRequirements = "";
        this.implementedSRSrequirements = new IdentifierList();
    }

    public MestraSDD(MestraSDD mestraSDD, ConfigurationFileBaseReader configuration) {
        super((MestraBase)mestraSDD, configuration);
        this.SRSimplementedRequirements = mestraSDD.SRSimplementedRequirements;
        this.implementedSRSrequirements = mestraSDD.implementedSRSrequirements;        
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
     * @return the sRSimplementedRequirements
     */
    public String getSRSimplementedRequirements() {
        return SRSimplementedRequirements;
    }

    /**
     * @param simplementedRequirements the sRSimplementedRequirements to set
     */
    public void setSRSimplementedRequirements(String simplementedRequirements) {
        SRSimplementedRequirements = SRSimplementedRequirements + simplementedRequirements;
        SRSimplementedRequirements = removeSpaces(SRSimplementedRequirements);
        implementedSRSrequirements.add(SRSimplementedRequirements);
    }

    /**
     * @return the implementedSRSrequirements
     */
    public IdentifierList getImplementedSRSrequirements() {
        return implementedSRSrequirements;
    }

    /**
     * @param implementedSRSrequirements the implementedSRSrequirements to set
     */
    public void setImplementedSRSrequirements(
            IdentifierList implementedSRSrequirements) {
        this.implementedSRSrequirements = implementedSRSrequirements;
    }

    public boolean IsCovering(String SRS_Identifier) {
        if (implementedSRSrequirements == null) {
            return false;
        }
        else {
            Iterator<String> Iter = implementedSRSrequirements.iterator();
            while (Iter.hasNext()) {
                if (Iter.next().equalsIgnoreCase(SRS_Identifier)) {
                    return true;
                }
            }
        }
        return false;
    }

    public Object clone() {

        MestraSDD mestraSDD = new MestraSDD(this, this.configuration);
        return mestraSDD;
    }
}
