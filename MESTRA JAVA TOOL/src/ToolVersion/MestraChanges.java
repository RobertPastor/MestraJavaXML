package ToolVersion;

public class MestraChanges {

	private String Rationale;
	private String Version;
	public MestraChanges (String aRationale,String aVersion) {
		this.Rationale = aRationale;
		this.Version = aVersion;
	}
	public String getRationale() {
		return this.Rationale;
	}
	public void setRationale(String rationale) {
		this.Rationale = rationale;
	}
	public String getVersion() {
		return this.Version;
	}
	public void setVersion(String version) {
		this.Version = version;
	}
}

