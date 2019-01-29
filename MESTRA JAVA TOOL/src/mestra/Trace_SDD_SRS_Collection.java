package mestra;

import java.util.ArrayList;
import java.util.Iterator;

import configuration.ConfigurationFileBaseReader;

/**
 * 
 * 
 * @author PASTOR Robert
 *
 */
public class Trace_SDD_SRS_Collection {

    ArrayList<Trace_SDD_SRS> trace_SDD_SRS_Collection = null;
    
    public Trace_SDD_SRS_Collection(MestraFiles mestraFiles, ConfigurationFileBaseReader configuration) {

        trace_SDD_SRS_Collection = new ArrayList<Trace_SDD_SRS>();

        MestraSDD_Collection mestraSDD_Collection = mestraFiles.getMestraSDDtags();
        if (mestraSDD_Collection != null) {
            Iterator<MestraSDD> Iter1 = mestraSDD_Collection.iterator();
            while (Iter1.hasNext()) {

                MestraSDD mestraSDD = Iter1.next();

                MestraSRS_Collection mestraSRS_Collection = mestraFiles.getMestraSRStags();
                if (mestraSRS_Collection != null) {
                    Iterator<MestraSRS> Iter2 = mestraSRS_Collection.iterator();

                    while (Iter2.hasNext()) {
                        MestraSRS mestraSRS = Iter2.next();

                        if (mestraSDD.IsCovering(mestraSRS.getIdentifier())) {
                            //                   
                            Trace_SDD_SRS trace_SDD_SRS = new Trace_SDD_SRS(mestraSDD, mestraSRS, configuration);
                            trace_SDD_SRS_Collection.add(trace_SDD_SRS);
                        }
                    }
                }
            }
            
            // -------- in this second part we identify unknown references from a SDD Requirement to an 
            // -------- unknown SRS Requirement (badly written or suppressed)

            // we list here all unknown references from SDD to SRS
            Iterator<MestraSDD> Iter3 = mestraSDD_Collection.iterator();

            while (Iter3.hasNext()) {
                MestraSDD mestraSDD = Iter3.next();

                IdentifierList implementedSRSrequirements = mestraSDD.getImplementedSRSrequirements();
                Iterator<String> Iter4 = implementedSRSrequirements.iterator();
                while (Iter4.hasNext()) {

                    String SRS_Reference = Iter4.next();
                    if (mestraFiles.isSRSreferenceKnown(SRS_Reference) == false) {
                        // we build an artificial SRS Requirement 
                        // and initialize it with the UNKNOW link from the SDD Component
                        MestraSRS mestraSRS = new MestraSRS(SRS_Reference , configuration.getMestraStyles(MestraFileType.MestraFileTypeEnum.SRS));

                        Trace_SDD_SRS trace_SDD_SRS = new Trace_SDD_SRS(mestraSDD, mestraSRS , configuration);
                        trace_SDD_SRS.setSRSreferenceUnknown(true);

                        trace_SDD_SRS_Collection.add(trace_SDD_SRS);
                    }
                }
            }
        }
    }

    public ArrayList<Trace_SDD_SRS> getTrace_SDD_SRS_Collection() {
        return trace_SDD_SRS_Collection;
    }

    public void setTrace_SDD_SRS_Collection(
            ArrayList<Trace_SDD_SRS> trace_SDD_SRS_Collection) {
        this.trace_SDD_SRS_Collection = trace_SDD_SRS_Collection;
    }


}
