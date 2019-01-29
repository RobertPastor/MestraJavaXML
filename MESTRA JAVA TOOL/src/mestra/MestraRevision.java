package mestra;

/**
 * this class manages a MESTRA Revision tag
 * @author Pastor
 *
 */
public class MestraRevision {

	private String Revision = "";
	private boolean Empty = false;
	private boolean MultipleMarkers = false;
	
	public MestraRevision(String revision,
			boolean empty,
			boolean multipleMarkers) {
		Revision = revision;
		Empty = empty;
		MultipleMarkers = multipleMarkers;
	}
	
	public MestraRevision(String revision) {
		Revision = revision;
		Empty = false;
		MultipleMarkers = false;
		if (Revision.length()==0) {
			Empty = true;
		}
	}
	
	public String getRevision() {
		return Revision;
	}
	
	public boolean isEmpty() {
		return Empty;
	}
	
	public boolean hasMultipleMarkers() {
		return MultipleMarkers;
	}
}
