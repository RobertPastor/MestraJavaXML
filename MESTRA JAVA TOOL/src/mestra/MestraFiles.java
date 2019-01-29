package mestra;

import gui.StatusBar;

import java.util.Iterator;
import java.util.logging.Logger;

import poi.ExcelOutputFile;
import mestra.MestraFileType.MestraFileTypeEnum;
import configuration.ConfigurationFileBaseReader;

/**
 * this class manages a set of MESTRA files 
 * and the different collection of MESTRA markers extracted from this set
 * 
 * @author Pastor Robert
 * @since July 2007
 *
 */
public class MestraFiles extends MestraFilesBase {

	final static Logger logger = Logger.getLogger(MestraFiles.class.getName()); 

	// the kind of scenario which is running
	private MestraScenario mestraScenario = null;

	// the name of the CSCI to filter the SSS Requirements allocated to this CSCI
	private String Traceability_CSCI = "";

	// upstream and down stream collections used in trace-ability runs
	private MestraSSS_Collection mestraSSStags = null;
	private MestraSRS_Collection mestraSRStags = null;
	private MestraSDD_Collection mestraSDDtags = null;
	private MestraMF_Collection  mestraMFtags = null;
	private MestraTS_Collection  mestraTStags = null;

	private CSCI_SSSFileAllocationList SSS_FileAllocationList = null;

	public MestraFiles() {
		super();
		this.mestraScenario = null;
		this.Traceability_CSCI = "";
		this.mestraSSStags = null;
		this.mestraSRStags = null;
		this.mestraSDDtags = null;
		this.mestraMFtags = null;
		this.mestraTStags = null;

	}

	public boolean isSRSreferenceKnown(String SRS_Reference) {

		Iterator<MestraSRS> Iter = mestraSRStags.iterator();
		while (Iter.hasNext()) {
			MestraSRS mestraSRS = Iter.next();
			if (SRS_Reference.equalsIgnoreCase(mestraSRS.getIdentifier())) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param SSS_Reference
	 * @return
	 */

	public boolean isSSSreferenceKnown(String SSS_Reference) {

		Iterator<MestraSSS> Iter = mestraSSStags.iterator();
		while (Iter.hasNext()) {
			MestraSSS mestraSSS = Iter.next();
			if (SSS_Reference.equalsIgnoreCase(mestraSSS.getIdentifier())) {
				return true;
			}
		}
		if (SSS_Reference.equalsIgnoreCase("Req_derived")) {
			return true;
		}
		return false;
	}

	/**
	 * we loop through the collection of files composing the trace-ability run
	 * and we filter-group those markers which belong to the Upstream files
	 * 
	 * @param mestraScenario
	 */
	private void BuildUpstreamCollection(MestraScenario mestraScenario) {
		logger.info ("BuildUpstreamCollection scenario: " + mestraScenario.getMestraFileTypeUpStream());

		Iterator<MestraFile> Iter = null;
		switch (mestraScenario.getMestraScenarioEnum()) {
		case trace_All:
			break;
		case trace_SSS_SRS:
			// we loop through the collection of files composing the trace-ability run
			// and we filter those markers which belong to the Upstream files
			this.mestraSSStags = new MestraSSS_Collection();			
			Iter = mestraFiles.iterator();
			while ( Iter.hasNext() ){
				MestraFile mestraFile = Iter.next();
				if (mestraFile.IsSSS()) {
					mestraSSStags.addAll(mestraFile.getMestraSSStags());
				}
			}
			break;
			// the following cases are all grouped
			// because we are building up the upstream MARKERS for the SRS
			// the SRS being the UPSTREAM documents in all the three following
			// trace-ability exercises
		case trace_SRS_SDD:
		case trace_SRS_MF:
		case trace_SRS_TS:
			mestraSRStags = new MestraSRS_Collection ();
			Iter = mestraFiles.iterator();
			while ( Iter.hasNext() ){
				MestraFile mestraFile = Iter.next();
				if (mestraFile.IsSRS()) {
					mestraSRStags.addAll(mestraFile.getMestraSRStags());
				}
			}
			break;
		}
	}

	/**
	 * This method is dedicated to build the Down Stream Collection of Identifiers
	 * @param mestraScenario
	 */
	private void BuildDownstreamCollection(MestraScenario mestraScenario) {
		logger.info( "BuildDownstreamCollection scenario: " + mestraScenario.getMestraFileTypeDownStream());
		Iterator<MestraFile> Iter;

		// we loop through the collection of files composing the trace-ability run
		// and we filter those markers which belong to the DownStream files

		switch (mestraScenario.getMestraScenarioEnum()) {
		case trace_All:
			break;
		case trace_SSS_SRS:
			mestraSRStags = new MestraSRS_Collection();
			Iter = mestraFiles.iterator();
			while ( Iter.hasNext() ){
				MestraFile mestraFile = Iter.next();
				if (mestraFile.IsSRS()) {
					mestraSRStags.addAll(mestraFile.getMestraSRStags());
				}
			}
			break;

		case trace_SRS_MF:
			mestraMFtags = new MestraMF_Collection();
			Iter = mestraFiles.iterator();
			while ( Iter.hasNext() ){
				MestraFile mestraFile = Iter.next();
				if (mestraFile.IsMF()) {
					mestraMFtags.addAll(mestraFile.getMestraMFtags());
				}
			}
			break;

		case trace_SRS_TS:
			mestraTStags = new MestraTS_Collection();
			Iter = mestraFiles.iterator();
			while (Iter.hasNext()){
				MestraFile mestraFile = Iter.next();
				if (mestraFile.IsTS()) {
					mestraTStags.addAll(mestraFile.getMestraTStags());
				}
			}
			break;

		case trace_SRS_SDD:
			mestraSDDtags = new MestraSDD_Collection();
			Iter = mestraFiles.iterator();
			while (Iter.hasNext()){
				MestraFile mestraFile = Iter.next();
				// we are building downstream collection hence isSDD here
				if (mestraFile.IsSDD()) {
					logger.info( "BuildDownstreamCollection: "+mestraFile.getShortFileName());
					mestraSDDtags.addAll(mestraFile.getMestraSDDtags());
				}
			}
			break;
		}
	}

	private void initSSS_FileAllocationList(ConfigurationFileBaseReader aConfiguration) {

		if (mestraSSStags != null) {
			SSS_FileAllocationList = new CSCI_SSSFileAllocationList();
			Iterator<MestraSSS> Iter = mestraSSStags.iterator();
			while (Iter.hasNext()) {
				MestraSSS mestraSSS = Iter.next();
				SSS_FileAllocationList.add(aConfiguration, mestraSSS.getRequirementAllocationList());
			}
			logger.info( "init SSS File Allocation List done!");
		}
	}

	private void BuildMultipleMestraFilesCollection(MestraScenario mestraScenario,
			ConfigurationFileBaseReader aConfiguration) {

		logger.info( "BuildMultipleMestraFilesCollection scenario: " + mestraScenario.getMestraScenarioEnum().toString());
		Iterator<MestraFile> Iter;
		Iter = mestraFiles.iterator();
		while (Iter.hasNext()){
			MestraFile mestraFile = Iter.next();
			if (mestraFile.IsSSS()) {
				if (mestraSSStags == null) {
					mestraSSStags = new MestraSSS_Collection();
				}
				mestraSSStags.addAll(mestraFile.getMestraSSStags());
			}
			if (mestraFile.IsSRS()) {
				if (mestraSRStags == null) {
					mestraSRStags = new MestraSRS_Collection();
				}
				mestraSRStags.addAll(mestraFile.getMestraSRStags());
			}
			if (mestraFile.IsSDD()) {
				if (mestraSDDtags == null) {
					mestraSDDtags = new MestraSDD_Collection();
				}
				mestraSDDtags.addAll(mestraFile.getMestraSDDtags());
			}
			if (mestraFile.IsMF()) {
				if (mestraMFtags == null) {
					mestraMFtags = new MestraMF_Collection();
				}
				mestraMFtags.addAll(mestraFile.getMestraMFtags());
			}
			if (mestraFile.IsTS()) {
				if (mestraTStags == null) {
					mestraTStags = new MestraTS_Collection();
				}
				mestraTStags.addAll(mestraFile.getMestraTStags());
			}
		}
		// initialize the list of whole set of CSCI allocation in all the MESTRA Files
		initSSS_FileAllocationList(aConfiguration);
	}



	public boolean RunMestraScenario(MestraScenario mestraScenario,
			ConfigurationFileBaseReader aConfiguration,
			String traceability_CSCI,
			StatusBar statusBar){

		this.Traceability_CSCI = traceability_CSCI;
		this.mestraScenario = mestraScenario;
		boolean bool = false;

		switch (mestraScenario.getMestraScenarioEnum()) {
		case trace_SSS_SRS:
			if (AtLeastOneFile(MestraFileTypeEnum.SSS) && AtLeastOneFile(MestraFileTypeEnum.SRS)) {
				Iterator<MestraFile> Iter = iterator();
				while ( Iter.hasNext() ){
					MestraFile mestraFile = Iter.next();
					if (mestraFile.IsSSS() || mestraFile.IsSRS()) {

						logger.info( "MestraFiles: File to analyse: " + mestraFile.getLongFileName());
						statusBar.setMessage("Analysing: " + mestraFile.getShortFileName());
						mestraFile.ExtractAllMestraMarkers(aConfiguration, statusBar);
					}
				}
				// even if an extraction fails we can build the Collections
				BuildUpstreamCollection(mestraScenario);
				BuildDownstreamCollection(mestraScenario);
				logger.info( "MestraFiles: Upstream and DownStream Collection built!");

				ExcelOutputFile outputFileXL = new ExcelOutputFile();
				bool = outputFileXL.WriteTraceabilityResults(this, aConfiguration);
			}
			else {
				logger.info( "MestraFiles : cannot run traceability scenario: need at least one SSS and one SRS");
				bool = false;
			}
			break;

		case trace_SRS_MF:
			if (AtLeastOneFile(MestraFileTypeEnum.SRS) && AtLeastOneFile(MestraFileTypeEnum.MF)) {
				Iterator<MestraFile> Iter = mestraFiles.iterator();
				while ( Iter.hasNext() ){
					MestraFile mestraFile = Iter.next();
					if (mestraFile.IsSRS() || mestraFile.IsMF()) {
						logger.info( "MestraFiles: File to analyse: " + mestraFile.getLongFileName());
						mestraFile.ExtractAllMestraMarkers(aConfiguration,statusBar);
					}
				}
				BuildUpstreamCollection(mestraScenario);
				BuildDownstreamCollection(mestraScenario);
				logger.info( "MestraFiles: Upstream and DownStream Collection built!");

				ExcelOutputFile outputsXL = new ExcelOutputFile();
				bool = outputsXL.WriteTraceabilityResults(this,aConfiguration);
			}
			else {
				logger.info( "MestraFiles : cannot run traceability scenario: need at least one SRS and one MF");
				bool = false;
			}
			break;

		case trace_SRS_SDD:
			if (AtLeastOneFile(MestraFileTypeEnum.SRS) && AtLeastOneFile(MestraFileTypeEnum.SDD)) {
				Iterator<MestraFile> Iter = mestraFiles.iterator();
				while ( Iter.hasNext() ){
					MestraFile mestraFile = Iter.next();
					if (mestraFile.IsSRS() || mestraFile.IsSDD()) {
						logger.info( "MestraFiles: File to analyse: " + mestraFile.getLongFileName());
						mestraFile.ExtractAllMestraMarkers(aConfiguration,statusBar);
					}
				}
				BuildUpstreamCollection(mestraScenario);
				BuildDownstreamCollection(mestraScenario);
				logger.info( "MestraFiles: Upstream and DownStream Collection built!");

				ExcelOutputFile outputsXL = new ExcelOutputFile();
				bool = outputsXL.WriteTraceabilityResults(this,aConfiguration);
			}
			else {
				logger.info( "MestraFiles : cannot run traceability scenario: need at least one SRS and one SDD");
				bool = false;
			}
			break;

		case trace_SRS_TS:
			if (AtLeastOneFile(MestraFileTypeEnum.SRS) && AtLeastOneFile(MestraFileTypeEnum.TS)) {
				Iterator<MestraFile> Iter = mestraFiles.iterator();
				while ( Iter.hasNext() ){
					MestraFile mestraFile = Iter.next();
					if (mestraFile.IsSRS() || mestraFile.IsTS()) {
						logger.info( "MestraFiles: File to analyse: " + mestraFile.getLongFileName());
						mestraFile.ExtractAllMestraMarkers(aConfiguration,statusBar);
					}
				}
				BuildUpstreamCollection(mestraScenario);
				BuildDownstreamCollection(mestraScenario);
				logger.info( "MestraFiles: Upstream and DownStream Collection built!");

				ExcelOutputFile outputsXL = new ExcelOutputFile();
				bool = outputsXL.WriteTraceabilityResults(this,aConfiguration);
			}
			else {
				logger.info( "MestraFiles : cannot run traceability scenario: need at least one SRS and one TS");
				bool = false;
			}
			break;

		case trace_All:

			// the Trace ALL scenario is used for extracting Markers from one File
			// but also for extracting Markers for a set of Files of the same type (Multiple)

			if (mestraFiles.isEmpty() == false) {
				logger.info( "MestraFiles: ExtractAllMestraMarkers: set is not empty");

				if (mestraFiles.size() == 1) {
					Iterator<MestraFile> Iter = mestraFiles.iterator();
					while ( Iter.hasNext() ){
						MestraFile mestraFile = Iter.next();

						logger.info( "MestraFiles: File to analyse: " + mestraFile.getLongFileName());
						mestraFile.ExtractAllMestraMarkers(aConfiguration,statusBar);

						ExcelOutputFile outputsXL = new ExcelOutputFile();
						bool = outputsXL.WriteResults(mestraFile,aConfiguration);
					}
				}
				else {
					logger.info( "MestraFiles: several MESTRA files to analyse!!!");
					Iterator<MestraFile> Iter = mestraFiles.iterator();
					while ( Iter.hasNext() ){
						MestraFile mestraFile = Iter.next();

						logger.info( "MestraFiles: File to analyse: " + mestraFile.getLongFileName());
						mestraFile.ExtractAllMestraMarkers(aConfiguration,statusBar);

					}
					BuildMultipleMestraFilesCollection(mestraScenario, aConfiguration);

					ExcelOutputFile outputsXL = new ExcelOutputFile();
					bool = outputsXL.WriteMultipleMestraFilesResults(this, aConfiguration);
				}
			}
			else {
				logger.info( "MestraFiles: ExtractAllMestraMarkers: set is empty!");
				bool = false;
			}
			break;

		default: {
			logger.info( "ERROR: should not achieve here!! all cases not dealt with!!!");
			bool = false;
			}
		}

		logger.info( "MestraFiles: end of scenario: " + mestraScenario.toString()+" ...");
		return bool;
	}

	/**
	 * @return the traceability_CSCI
	 */
	public String getTraceability_CSCI() {
		return this.Traceability_CSCI;
	}

	/**
	 * @param traceability_CSCI the traceability_CSCI to set
	 */
	public void setTraceability_CSCI(String traceability_CSCI) {
		this.Traceability_CSCI = traceability_CSCI;
	}

	/**
	 * @return the mestraMFtags
	 */
	public MestraMF_Collection getMestraMFtags() {
		return this.mestraMFtags;
	}

	/**
	 * @param mestraMFtags the mestraMFtags to set
	 */
	public void setMestraMFtags(MestraMF_Collection mestraMFtags) {
		this.mestraMFtags = mestraMFtags;
	}

	/**
	 * @return the mestraSRStags
	 */
	public MestraSRS_Collection getMestraSRStags() {
		return this.mestraSRStags;
	}

	/**
	 * @param mestraSRStags the mestraSRStags to set
	 */
	public void setMestraSRStags(MestraSRS_Collection mestraSRStags) {
		this.mestraSRStags = mestraSRStags;
	}

	/**
	 * @return the mestraSSStags
	 */
	public MestraSSS_Collection getMestraSSStags() {
		return this.mestraSSStags;
	}

	/**
	 * @param mestraSSStags the mestraSSStags to set
	 */
	public void setMestraSSStags(MestraSSS_Collection mestraSSStags) {
		this.mestraSSStags = mestraSSStags;
	}

	/**
	 * @return the mestraTStags
	 */
	public MestraTS_Collection getMestraTStags() {
		return this.mestraTStags;
	}

	/**
	 * @param mestraTStags the mestraTStags to set
	 */
	public void setMestraTStags(MestraTS_Collection mestraTStags) {
		this.mestraTStags = mestraTStags;
	}

	/**
	 * @return the mestraScenario
	 */
	public MestraScenario getMestraScenario() {
		return this.mestraScenario;
	}

	/**
	 * @param mestraScenario the mestraScenario to set
	 */
	public void setMestraScenario(MestraScenario mestraScenario) {
		this.mestraScenario = mestraScenario;
	}

	/**
	 * @return the mestraSDDtags
	 */
	public MestraSDD_Collection getMestraSDDtags() {
		return this.mestraSDDtags;
	}

	/**
	 * @param mestraSDDtags the mestraSDDtags to set
	 */
	public void setMestraSDDtags(MestraSDD_Collection mestraSDDtags) {
		this.mestraSDDtags = mestraSDDtags;
	}

	public CSCI_SSSFileAllocationList getSSS_FileAllocationList() {
		return this.SSS_FileAllocationList;
	}

	public void setSSS_FileAllocationList(
			CSCI_SSSFileAllocationList fileAllocationList) {
		this.SSS_FileAllocationList = fileAllocationList;
	}
}