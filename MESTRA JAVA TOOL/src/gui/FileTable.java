package gui;

import mestra.MestraFile;
import mestra.MestraFileType.MestraFileTypeEnum;


public class FileTable extends MestraFile {

	private boolean analysed = false;
	
	public FileTable () {
		super();
		analysed = false;
	}
    public FileTable(String filePath) {
        super(filePath);
        analysed = true;
    }
    
    public FileTable (String filePath,MestraFileTypeEnum mestraFileType) {
    	super(filePath);
    	this.setFileType(mestraFileType);
        analysed = true;
    }

	/**
	 * @return the analyzed boolean
	 */
	public boolean isAnalysed() {
		return analysed;
	}

	/**
	 * @param analysed the analyzed to set
	 */
	public void setAnalysed(boolean analysed) {
		this.analysed = analysed;
	}

	
}
