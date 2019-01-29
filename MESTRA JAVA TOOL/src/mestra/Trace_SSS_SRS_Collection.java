package mestra;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import configuration.ConfigurationFileBaseReader;

/**
 * 
 * This class manages a collection of traces between SSS and SRS Requirements
 * @author PASTOR Robert
 * @since July 2007
 *
 */
public class Trace_SSS_SRS_Collection  {

	final static Logger logger = Logger.getLogger(Trace_SSS_SRS_Collection.class.getName()); 

    ArrayList<Trace_SSS_SRS> trace_SSS_SRS_Collection = null;
   
    public Trace_SSS_SRS_Collection(MestraFiles mestraFiles, ConfigurationFileBaseReader configuration) {

        trace_SSS_SRS_Collection = new ArrayList<Trace_SSS_SRS>();

        MestraSSS_Collection mestraSSS_Collection = mestraFiles.getMestraSSStags();
        logger.log(Level.INFO , "size of Mestra SSS collection = " + mestraSSS_Collection.size());
        
        if (mestraSSS_Collection != null) {
            Iterator<MestraSSS> Iter1 = mestraSSS_Collection.iterator();
            while (Iter1.hasNext()) {

                MestraSSS mestraSSS = Iter1.next();
                // we state by default that this SSS Requirement is not covered
                mestraSSS.setCovered(false);

                MestraSRS_Collection mestraSRS_Collection = mestraFiles.getMestraSRStags();
                if (mestraSRS_Collection != null) {
                    Iterator<MestraSRS> Iter2 = mestraSRS_Collection.iterator();

                    while (Iter2.hasNext()) {
                        MestraSRS mestraSRS = Iter2.next();
                        if (mestraSRS.IsCovering(mestraSSS.getIdentifier())) {

                            //  set this SSS Requirement as covered
                            mestraSSS.setCovered(true); 
                            // the following boolean states that the MESTRA SSS Requirement is allocated to
                            // the CSCI for which the trace-ability is run

                            Trace_SSS_SRS trace_SSS_SRS = new Trace_SSS_SRS(mestraSSS, mestraSRS, mestraFiles.getTraceability_CSCI(), configuration);
                            trace_SSS_SRS_Collection.add(trace_SSS_SRS);
                        }
                    }
                }

                if (mestraSSS.isCovered() == false ) {
                    // specific case for CP-OH requirement
                    if ( (mestraSSS.isAllocated(mestraFiles.getTraceability_CSCI())) || 
                            ( (mestraSSS.isCPOH()) && (mestraFiles.getTraceability_CSCI().equalsIgnoreCase("ODS")))) {

                        // in this case, the SSS (or the CP-OH)) requirement should be covered.
                        Trace_SSS_SRS trace_SSS_SRS = new Trace_SSS_SRS(mestraSSS, null, mestraFiles.getTraceability_CSCI(), configuration);
                        trace_SSS_SRS_Collection.add(trace_SSS_SRS);    
                    }           
                }
            }
        }
    }

    /**
     * @return the trace_SSS_SRS_Collection
     */
    public ArrayList<Trace_SSS_SRS> getTrace_SSS_SRS_Collection() {
        return trace_SSS_SRS_Collection;
    }

}
