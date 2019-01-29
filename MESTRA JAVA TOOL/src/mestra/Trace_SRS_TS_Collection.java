package mestra;

import java.util.ArrayList;
import java.util.Iterator;

import configuration.ConfigurationFileBaseReader;

public class Trace_SRS_TS_Collection {

    ArrayList<Trace_SRS_TS> trace_SRS_TS_Collection = null;
    
    public Trace_SRS_TS_Collection(MestraFiles mestraFiles, ConfigurationFileBaseReader configuration) {

        trace_SRS_TS_Collection = new ArrayList<Trace_SRS_TS>();

        MestraSRS_Collection mestraSRS_Collection = mestraFiles.getMestraSRStags();
        if (mestraSRS_Collection != null) {
            Iterator<MestraSRS> Iter1 = mestraSRS_Collection.iterator();
            while (Iter1.hasNext()) {

                MestraSRS mestraSRS = Iter1.next();
                // we state by default that this Requirement is not covered
                mestraSRS.setCovered(false);

                MestraTS_Collection mestraTS_Collection = mestraFiles.getMestraTStags();
                if (mestraTS_Collection != null) {
                    Iterator<MestraTS> Iter2 = mestraTS_Collection.iterator();

                    while (Iter2.hasNext()) {
                        MestraTS mestraTS = Iter2.next();
                        if (mestraTS.IsCovering(mestraSRS.getIdentifier())) {
                            //  set this Requirement as covered
                            mestraSRS.setCovered(true); 

                            Trace_SRS_TS trace_SRS_TS = new Trace_SRS_TS(mestraSRS, mestraTS, configuration);
                            trace_SRS_TS_Collection.add(trace_SRS_TS);
                        }
                    }
                }

                if (mestraSRS.isCovered() == false ) {

                    Trace_SRS_TS trace_SRS_TS = new Trace_SRS_TS(mestraSRS, null, configuration);
                    trace_SRS_TS_Collection.add(trace_SRS_TS);  
                }
            }
        }
    }

    /**
     * @return the trace_SRS_TS_Collection
     */
    public ArrayList<Trace_SRS_TS> getTrace_SRS_TS_Collection() {
        return trace_SRS_TS_Collection;
    }

    /**
     * @param trace_SRS_TS_Collection the trace_SRS_TS_Collection to set
     */
    public void setTrace_SRS_TS_Collection(
            ArrayList<Trace_SRS_TS> trace_SRS_TS_Collection) {
        this.trace_SRS_TS_Collection = trace_SRS_TS_Collection;
    }
}
