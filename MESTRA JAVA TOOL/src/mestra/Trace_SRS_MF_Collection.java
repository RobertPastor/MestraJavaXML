package mestra;

import java.util.ArrayList;
import java.util.Iterator;

import configuration.ConfigurationFileBaseReader;

public class Trace_SRS_MF_Collection {

	private ArrayList<Trace_SRS_MF> trace_SRS_MF_Collection = null;

    public Trace_SRS_MF_Collection(MestraFiles mestraFiles, ConfigurationFileBaseReader configuration) {

    	trace_SRS_MF_Collection = new ArrayList<Trace_SRS_MF>();

        MestraSRS_Collection mestraSRS_Collection = mestraFiles.getMestraSRStags();
        if (mestraSRS_Collection != null) {
            Iterator<MestraSRS> Iter1 = mestraSRS_Collection.iterator();
            while (Iter1.hasNext()) {

                MestraSRS mestraSRS = Iter1.next();
                // we state by default that this Requirement is not covered
                mestraSRS.setCovered(false);

                MestraMF_Collection mestraMF_Collection = mestraFiles.getMestraMFtags();
                if (mestraMF_Collection != null) {
                    Iterator<MestraMF> Iter2 = mestraMF_Collection.iterator();

                    while (Iter2.hasNext()) {
                        MestraMF mestraMF = Iter2.next();
                        if (mestraMF.IsCovering(mestraSRS.getIdentifier())) {
                            //  set this Requirement as covered
                            mestraSRS.setCovered(true); 

                            Trace_SRS_MF trace_SRS_MF = new Trace_SRS_MF(mestraSRS, mestraMF , configuration);
                            this.trace_SRS_MF_Collection.add(trace_SRS_MF);
                        }
                    }
                }
 
                if (mestraSRS.isCovered() == false ) {

                    // the MESTRA SRS Requirement is not covered
                    // we build a trace item with an empty DOWNSTREAM MF component

                    Trace_SRS_MF trace_SRS_MF = new Trace_SRS_MF(mestraSRS, null, configuration);
                    trace_SRS_MF_Collection.add(trace_SRS_MF);  
                }
            }
        }
    }

    public ArrayList<Trace_SRS_MF> getTrace_SRS_MF_Collection() {
        return trace_SRS_MF_Collection;
    }

    public void setTrace_SRS_MF_Collection(
            ArrayList<Trace_SRS_MF> atrace_SRS_MF_Collection) {
        this.trace_SRS_MF_Collection = atrace_SRS_MF_Collection;
    }

}
