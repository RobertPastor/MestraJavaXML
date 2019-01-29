package mestra;

import java.util.ArrayList;
import java.util.Iterator;

import configuration.ConfigurationFileBaseReader;

public class Trace_SRS_SSS_Collection {


    ArrayList<Trace_SRS_SSS> trace_SRS_SSS_Collection = null;
 
    public Trace_SRS_SSS_Collection(MestraFiles mestraFiles, ConfigurationFileBaseReader configuration) {

        trace_SRS_SSS_Collection = new ArrayList<Trace_SRS_SSS>();

        MestraSRS_Collection mestraSRS_Collection = mestraFiles.getMestraSRStags();
        if (mestraSRS_Collection != null) {
            Iterator<MestraSRS> Iter1 = mestraSRS_Collection.iterator();
            while (Iter1.hasNext()) {

                MestraSRS mestraSRS = Iter1.next();

                MestraSSS_Collection mestraSSS_Collection = mestraFiles.getMestraSSStags();
                if (mestraSSS_Collection != null) {
                    Iterator<MestraSSS> Iter2 = mestraSSS_Collection.iterator();

                    while (Iter2.hasNext()) {
                        MestraSSS mestraSSS = Iter2.next();

                        if (mestraSRS.IsCovering(mestraSSS.getIdentifier())) {
                            //                   
                            Trace_SRS_SSS trace_SRS_SSS = new Trace_SRS_SSS(mestraSRS, mestraSSS, configuration);
                            trace_SRS_SSS_Collection.add(trace_SRS_SSS);
                        }
                    }
                }
            }

            // -------- in this second part we identify unknown references from a SRS Requirement to an 
            // -------- unknown SSS Requirement (badly written or suppressed)

            // we list here all unknown references from SRS to SSS
            Iterator<MestraSRS> Iter3 = mestraSRS_Collection.iterator();

            while (Iter3.hasNext()) {
                MestraSRS mestraSRS = Iter3.next();

                IdentifierList SSS_ReferenceList = mestraSRS.getSSS_ReferencesList();
                Iterator<String> Iter4 = SSS_ReferenceList.iterator();
                while (Iter4.hasNext()) {
                    String SSS_Reference = Iter4.next();
                    if (mestraFiles.isSSSreferenceKnown(SSS_Reference) == false) {
                        // we build an artificial SSS Requirement and initialize it with the UNKNOW link from the SRS Requirement
                        MestraSSS mestraSSS = new MestraSSS(SSS_Reference , configuration.getMestraStyles(MestraFileType.MestraFileTypeEnum.SSS));

                        Trace_SRS_SSS trace_SRS_SSS = new Trace_SRS_SSS(mestraSRS, mestraSSS , configuration);
                        trace_SRS_SSS.setSSSreferenceUnknown(true);

                        trace_SRS_SSS_Collection.add(trace_SRS_SSS);

                    }
                }
            }
        }

    }

    /**
     * @return the trace_SRS_SSS_Collection
     */
    public ArrayList<Trace_SRS_SSS> getTrace_SRS_SSS_Collection() {
        return trace_SRS_SSS_Collection;
    }

}
