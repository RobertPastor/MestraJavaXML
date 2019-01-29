package gui;

import java.util.ArrayList;

/**
 * This class is used to manage a collection of files.
 * @author PASTOR Robert
 *
 */
public class FilesTable {

	private ArrayList<FileTable> filesTable = null;
	
	public FilesTable() {
		filesTable = new ArrayList<FileTable>();
	}

	/**
	 * @return the filesTable
	 */
	public ArrayList<FileTable> getFilesTable() {
		return filesTable;
	}

	/**
	 * @param filesTable the filesTable to set
	 */
	public void setFilesTable(ArrayList<FileTable> filesTable) {
		this.filesTable = filesTable;
	}
}
