package mestra;

import java.util.Iterator;

import configuration.ConfigurationFileBaseReader;
import configuration.MestraStyle;
import configuration.MestraStylesMap;
import configuration.MethodLevelSafety;

/**
 * 
 * This is the base class for SSS, SRS, MF and TS MESTRA tags.
 * 
 * @author: Robert Pastor
 * @since: May 2007
 */

public class MestraBase extends MestraIdentifier implements Cloneable {

	// this is the file name (file path) 
	// where the MESTRA tags have been extracted from
	
	private String ShortFileName = "";
	
	// the string represents the VALUE extracted from the WORD file and the MestraStyle the data as extracted from the configuration file
	MestraStylesMap mestraStylesMap = null;

	private String Title = "";

	private String ChangeHistory = "";
	private boolean ChangeHistoryEmpty = true;

	private String ReleaseRevision = "";
	private boolean ReleaseRevisionMultipleMarkers = false;

	private String strMethodLevelSafety  = "";
	private MethodLevelSafety methodLevelSafety = null;
	
	private boolean Covered = false;
	protected ConfigurationFileBaseReader configuration = null;
	
	

	private void helper() {
		
		ShortFileName = "";
		this.mestraStylesMap.setTitleValue ("");
		this.mestraStylesMap.setChangeHistoryValue ("");
		
		ReleaseRevision = "";
		strMethodLevelSafety = "";
		methodLevelSafety = new MethodLevelSafety();
		
		ChangeHistoryEmpty = true;
		ReleaseRevisionMultipleMarkers = false;
		Covered = false;
	}
	
	public MestraBase(MestraStylesMap _mestraStylesMap) {
		super();
		this.mestraStylesMap = _mestraStylesMap.deepCopy();
		helper();
	}
	
	public MestraBase(MestraBase mestraBase, ConfigurationFileBaseReader aConfiguration) {
		
		super((MestraIdentifier)mestraBase);
		this.configuration = aConfiguration;
		this.mestraStylesMap = mestraBase.mestraStylesMap.deepCopy();
		
		setShortFileName (mestraBase.ShortFileName);
	
		this.mestraStylesMap.setTitleValue (mestraBase.getTitle());
		this.mestraStylesMap.setChangeHistoryValue (mestraBase.getChangeHistory());
		this.mestraStylesMap.setReleaseRevision (mestraBase.ReleaseRevision);
		this.mestraStylesMap.setMethodLevelSafety (mestraBase.strMethodLevelSafety, aConfiguration);
		setCovered (mestraBase.Covered);
	}

	
	public MestraBase(String unknown_reference, MestraStylesMap mestraStylesMap) {
		super(unknown_reference, false);

		this.mestraStylesMap = mestraStylesMap.deepCopy();
		helper();
	}

	public Object clone() throws CloneNotSupportedException {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			throw new Error("MestraBase: This should not occur since we implement Cloneable");
		}
	}
	
	public MestraIdentifier getMestraIdentifierClone() {
		return (new MestraIdentifier(this.getIdentifier(), this.isDuplicated()));
	}
	
	public void setMestraIdentifierClone(String identifier) {
		this.Identifier = identifier;
	}
	
	public MestraTitle getMestraTitleClone() {
		return (new MestraTitle(this.getTitle(),this.isTitleEmpty()));
	}
	
	public MestraRevision getMestraReleaseRevisionClone() {
		return (new MestraRevision(this.getReleaseRevision(),
				this.isReleaseRevisionEmpty(),
				this.ReleaseRevisionMultipleMarkers));
	}
	
	/**
	 * @return the changeHistory
	 */
	public String getChangeHistory() {
		return ChangeHistory;
	}
	/**
	 * @param changeHistory the changeHistory to set
	 */
	public void setChangeHistory(String changeHistory) {
		ChangeHistory = ChangeHistory + changeHistory;
		if (ChangeHistory.length() > 0) {
			ChangeHistoryEmpty = false;
		}
		else {
			// An empty Change History is acceptable and is neither a WARNING nor an ERROR
			// it means only that from the very beginning since its creation
			// the requirement has never been changed
			// neither due to a PCR nor due to an ECR
			ChangeHistoryEmpty = true;
		}
	}
	
	/**
	 * @return the releaseRevision
	 */
	public String getReleaseRevision() {
		return ReleaseRevision;
	}
	
	/**
	 * @param releaseRevision the releaseRevision to set
	 */
	public void setReleaseRevision(String releaseRevision) {
		if ((ReleaseRevision.length()>0) && (releaseRevision.length()>0)) {
			ReleaseRevisionMultipleMarkers = true;
		}
		ReleaseRevision = ReleaseRevision + releaseRevision;
		if (ReleaseRevision.length() == 0) {
			addWarning("Release Revision is empty!");
		} 
	}
	
	/**
	 * @return the title of the Base MESTRA marker
	 */
	public String getTitle() {
		return this.mestraStylesMap.getTitleValue();
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String titleValue) {

		// Franck LALANE - bug - title content is duplicated
		//this.mestraStylesMap.setTitleValue(titleValue);
		this.Title = titleValue;
		
	}

	public String getStrMethodLevelSafety() {
		return strMethodLevelSafety;
	}


	public void setMethodLevelSafety(String aMethodLevelSafety,
			ConfigurationFileBaseReader configuration) {
		strMethodLevelSafety = strMethodLevelSafety + aMethodLevelSafety;
		methodLevelSafety.setMethodLevelSafety(strMethodLevelSafety, configuration) ;
	}
	/**
	 * @return the changeHistoryIsEmpty
	 */
	public boolean isChangeHistoryEmpty() {
		return ChangeHistoryEmpty;
	}
	/**
	 * @param changeHistoryIsEmpty the changeHistoryIsEmpty to set
	 */
	public void setChangeHistoryEmpty(boolean changeHistoryEmpty) {
		ChangeHistoryEmpty = changeHistoryEmpty;
	}
	
	/**
	 * @return the titleIsEmpty
	 */
	public boolean isTitleEmpty() {
		return this.Title.length() == 0;
	}
	
	/**
	 * @return the releaseRevisionIsEmpty
	 */
	public boolean isReleaseRevisionEmpty() {
		return this.ReleaseRevision.length() == 0;
	}
	
	/**
	 * @return the shortFileName
	 */
	public String getShortFileName() {
		return this.ShortFileName;
	}
	
	/**
	 * @param shortFileName the shortFileName to set
	 */
	public void setShortFileName(String shortFileName) {
		this.ShortFileName = shortFileName;
	}
	
	/**
	 * @return the coverage
	 */
	public boolean isCovered() {
		return this.Covered;
	}
	
	/**
	 * @param covered the covered to set
	 */
	public void setCovered(boolean covered) {
		this.Covered = covered;
	}

	/**
	 * @return the methodLevelSafety
	 */
	public MethodLevelSafety getMethodLevelSafety() {
		return this.methodLevelSafety;
	}
	
	public void computeWarningsErrors() {
			
		if (this.isTitleEmpty()) {
			addWarning("Title is empty!");
		}
		if (this.isReleaseRevisionEmpty()) {
			addWarning("Release Revision is empty!");
		}
		if (ReleaseRevisionMultipleMarkers) {
			addWarning("Release Revision with Multiple Markers");
		}
		if (methodLevelSafety.isMethodLevelSafetyEmpty()) {
			addWarning("MethodLevelSafety is empty!");
		}
		if ( (methodLevelSafety.isOneLevelNotExisting()) || 
				(methodLevelSafety.isOneMethodNotExisting()) ) {
			addError("Error in field Method Level Safety!");
		}
		super.computeWarningsErrors();
	}

	public boolean hasReleaseRevisionMultipleMarkers() {
		return ReleaseRevisionMultipleMarkers;
	}

	public void setReleaseRevisionMultipleMarkers(
			boolean releaseRevisionMultipleMarkers) {
		ReleaseRevisionMultipleMarkers = releaseRevisionMultipleMarkers;
	}

	public String getMestraValue(String mestraStyleHeader) {
		
		Iterator<MestraStyle> Iter = this.mestraStylesMap.getValues();
		while (Iter.hasNext()) {
			MestraStyle mestraStyle = Iter.next();
			if (mestraStyle.getMestraStyleHeader().equalsIgnoreCase(mestraStyleHeader)   ) {
				return mestraStyle.getMestraValue();
			}
		}
		return "";
	}
	
	
	public void setMestraValue(String mestraStyleHeader , String mestraValue, ConfigurationFileBaseReader aConfiguration) {
		
		Iterator<MestraStyle> Iter = this.mestraStylesMap.getValues();
		while (Iter.hasNext()) {
			MestraStyle mestraStyle = Iter.next();
			if (mestraStyle.getMestraStyleHeader().equalsIgnoreCase(mestraStyleHeader)   ) {
				mestraStyle.setMestraValue(mestraValue);
				// update the map
				this.mestraStylesMap.put(mestraStyleHeader, mestraStyle);
				// update specific fields
				if (mestraStyle.hasTitleAttribute()) {
					this.setTitle(mestraValue);
				}
				if (mestraStyle.hasChangesAttribute()) {
					this.setChangeHistory(mestraValue);
				}
				if (mestraStyle.hasRevisionAttribute()) {
					this.setReleaseRevision(mestraValue);
				}
				if (mestraStyle.hasMethodLevelSafetyAttribute()) {
					this.setMethodLevelSafety(mestraValue, aConfiguration);
				}
				break;
			}
		}
	}
	
	public MestraStylesMap getMestraStylesMap() {
		return this.mestraStylesMap;
	}
	
	public ConfigurationFileBaseReader getConfiguration() {
		return this.configuration;
	}
}
