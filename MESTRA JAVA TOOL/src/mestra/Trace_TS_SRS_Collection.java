package mestra;

import java.util.ArrayList;
import java.util.Iterator;

import configuration.ConfigurationFileBaseReader;

public class Trace_TS_SRS_Collection {

    ArrayList<Trace_TS_SRS> trace_TS_SRS_Collection = null;
    
    public Trace_TS_SRS_Collection(MestraFiles mestraFiles, ConfigurationFileBaseReader configuration) {

        trace_TS_SRS_Collection = new ArrayList<Trace_TS_SRS>();

        MestraTS_Collection mestraTS_Collection = mestraFiles.getMestraTStags();
        if (mestraTS_Collection != null) {
            Iterator<MestraTS> Iter1 = mestraTS_Collection.iterator();
            while (Iter1.hasNext()) {

                MestraTS mestraTS = Iter1.next();

                MestraSRS_Collection mestraSRS_Collection = mestraFiles.getMestraSRStags();
                if (mestraSRS_Collection != null) {
                    Iterator<MestraSRS> Iter2 = mestraSRS_Collection.iterator();

                    while (Iter2.hasNext()) {
                        MestraSRS mestraSRS = Iter2.next();

                        if (mestraTS.IsCovering(mestraSRS.getIdentifier())) {
                            // set this SRS Requirement as covered
                            mestraSRS.setCovered(true);

                            Trace_TS_SRS trace_TS_SRS = new Trace_TS_SRS(mestraTS, mestraSRS, configuration);                   
                            trace_TS_SRS_Collection.add(trace_TS_SRS);
                        }
                    }
                }
            }

            // -------- in this second part we identify unknown references from a TS Id to an 
            // -------- unknown SRS Requirement (badly written or suppressed)

            // we list here all unknown references from TS to SRS
            Iterator<MestraTS> Iter3 = mestraTS_Collection.iterator();
            while (Iter3.hasNext()) {
                MestraTS mestraTS = Iter3.next();

                IdentifierList aSRSrequirementsList = mestraTS.getTestedSRSrequirementsList();

                Iterator<String> Iter4 = aSRSrequirementsList.iterator();
                while (Iter4.hasNext()) {
                    String SRS_Reference = Iter4.next();
                    if (mestraFiles.isSRSreferenceKnown(SRS_Reference) == false) {
                        // we build an artificial SSS Requirement and 
                        // initialize it with the UNKNOW link from the SRS Requirement
                        MestraSRS mestraSRS = new MestraSRS(SRS_Reference, configuration.getMestraStyles(MestraFileType.MestraFileTypeEnum.SRS));

                        Trace_TS_SRS trace_TS_SRS = new Trace_TS_SRS(mestraTS, mestraSRS, configuration);
                        trace_TS_SRS.setSRSreferenceUnknown(true);
                        
                        trace_TS_SRS_Collection.add(trace_TS_SRS);

                    }
                }
            }
        }
    }

    /**
     * @return the trace_TS_SRS_Collection
     */
    public ArrayList<Trace_TS_SRS> getTrace_TS_SRS_Collection() {
        return trace_TS_SRS_Collection;
    }

    /**
     * @param trace_TS_SRS_Collection the trace_TS_SRS_Collection to set
     */
    public void setTrace_TS_SRS_Collection(
            ArrayList<Trace_TS_SRS> trace_TS_SRS_Collection) {
        this.trace_TS_SRS_Collection = trace_TS_SRS_Collection;
    }

}
