package mestra;

import java.util.ArrayList;
import java.util.Iterator;

import configuration.ConfigurationFileBaseReader;

public class Trace_SRS_SDD_Collection {

    private ArrayList<Trace_SRS_SDD> trace_SRS_SDD_Collection = null;

    public Trace_SRS_SDD_Collection(MestraFiles mestraFiles , ConfigurationFileBaseReader configuration) {

        trace_SRS_SDD_Collection = new ArrayList<Trace_SRS_SDD>();

        MestraSRS_Collection mestraSRS_Collection = mestraFiles.getMestraSRStags();
        if (mestraSRS_Collection != null) {
            Iterator<MestraSRS> Iter1 = mestraSRS_Collection.iterator();
            while (Iter1.hasNext()) {

                MestraSRS mestraSRS = Iter1.next();
                // we state by default that this Requirement is not covered
                mestraSRS.setCovered(false);

                MestraSDD_Collection mestraSDD_Collection = mestraFiles.getMestraSDDtags();
                if (mestraSDD_Collection != null) {
                    Iterator<MestraSDD> Iter2 = mestraSDD_Collection.iterator();

                    while (Iter2.hasNext()) {
                        MestraSDD mestraSDD = Iter2.next();
                        if (mestraSDD.IsCovering(mestraSRS.getIdentifier())) {
                            //  set this Requirement as covered
                            mestraSRS.setCovered(true); 

                            Trace_SRS_SDD trace_SRS_SDD = new Trace_SRS_SDD(mestraSRS, mestraSDD, configuration);
                            trace_SRS_SDD_Collection.add(trace_SRS_SDD);
                        }
                    }
                }
 
                if (mestraSRS.isCovered() == false ) {

                    // the MESTRA SRS Requirement is not covered
                    // we build a trace item with an empty DOWNSTREAM SDD component

                    Trace_SRS_SDD trace_SRS_SDD = new Trace_SRS_SDD(mestraSRS, null, configuration);
                    trace_SRS_SDD_Collection.add(trace_SRS_SDD);  
                }
            }
        }
    }

    public ArrayList<Trace_SRS_SDD> getTrace_SRS_SDD_Collection() {
        return trace_SRS_SDD_Collection;
    }

    public void setTrace_SRS_SDD_Collection(
            ArrayList<Trace_SRS_SDD> trace_SRS_SDD_Collection) {
        this.trace_SRS_SDD_Collection = trace_SRS_SDD_Collection;
    }

}
