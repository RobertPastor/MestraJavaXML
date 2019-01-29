package mestra;

import java.util.ArrayList;
import java.util.Iterator;

import configuration.ConfigurationFileBaseReader;

public class Trace_MF_SRS_Collection {

	private ArrayList<Trace_MF_SRS> trace_MF_SRS_Collection = null;

	public Trace_MF_SRS_Collection(MestraFiles mestraFiles, ConfigurationFileBaseReader configuration) {

		this.trace_MF_SRS_Collection = new ArrayList<Trace_MF_SRS>();

		MestraMF_Collection mestraMF_Collection = mestraFiles.getMestraMFtags();
		if (mestraMF_Collection != null) {
			Iterator<MestraMF> Iter1 = mestraMF_Collection.iterator();
			while (Iter1.hasNext()) {

				MestraMF mestraMF = Iter1.next();
				boolean found = false;

				MestraSRS_Collection mestraSRS_Collection = mestraFiles.getMestraSRStags();
				if (mestraSRS_Collection != null) {
					Iterator<MestraSRS> Iter2 = mestraSRS_Collection.iterator();

					while (Iter2.hasNext()) {
						MestraSRS mestraSRS = Iter2.next();

						if (mestraMF.IsCovering(mestraSRS.getIdentifier())) {

							Trace_MF_SRS trace_MF_SRS = new Trace_MF_SRS(mestraMF, mestraSRS , configuration);
							trace_MF_SRS.setSRSreferenceUnknown(false);
							trace_MF_SRS_Collection.add(trace_MF_SRS);
							found = true;
						}
					}
				}
				// this mestraMF is not covering any SRS : hence the SRS Requirement is wrong
				if (found == false) {
					Trace_MF_SRS trace_MF_SRS = new Trace_MF_SRS(mestraMF, null, configuration);
					trace_MF_SRS.setSRSreferenceUnknown(true);
					trace_MF_SRS_Collection.add(trace_MF_SRS);
				}
			}             
		}
	}

	public ArrayList<Trace_MF_SRS> getTrace_MF_SRS_Collection() {
		return this.trace_MF_SRS_Collection;
	}

	public void setTrace_MF_SRS_Collection(
			ArrayList<Trace_MF_SRS> atrace_MF_SRS_Collection) {
		this.trace_MF_SRS_Collection = atrace_MF_SRS_Collection;
	}


}
