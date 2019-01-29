package mestra;

import java.util.ArrayList;
import java.util.Iterator;

import mestra.MestraFileType.MestraFileTypeEnum;

/**
 * This class manages a list of Mestra files.
 * @author t0007330
 * @since July 2007
 *
 */
public class MestraFilesBase {

	// set of MESTRA files to be analyzed
	protected ArrayList<MestraFile> mestraFiles = null;

	public MestraFilesBase () {
		this.mestraFiles = new ArrayList<MestraFile>();
	}

	public Iterator<MestraFile> iterator() {
		return this.mestraFiles.iterator();
	}

	public void add(MestraFile mestraFile ) {
		this.mestraFiles.add(mestraFile);
	}

	/**
	 * @param mestraFiles the mestraFiles to set
	 */
	public void setMestraFiles(ArrayList<MestraFile> mestraFiles) {
		this.mestraFiles = mestraFiles;
	}

	/**
	 * @return the mestraFiles
	 */
	public ArrayList<MestraFile> getMestraFiles() {
		return this.mestraFiles;
	}

	public MestraFileTypeEnum getFirstFileType() {

		Iterator<MestraFile> Iter = mestraFiles.iterator();
		while ( Iter.hasNext() ){
			MestraFile mestraFile = Iter.next();
			return mestraFile.getFileType().getMestraFileType();
		}
		return MestraFileType.MestraFileTypeEnum.SSS;
	}

	/**
	 * this function checks that there is at least one file of the provided type
	 */
	public boolean AtLeastOneFile(MestraFileTypeEnum mestraFileType) {
		Iterator<MestraFile> Iter = this.mestraFiles.iterator();
		while ( Iter.hasNext() ){
			MestraFile mestraFile = Iter.next();
			if (mestraFile.Is(mestraFileType)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * this function returns TRUE if all the files have the provided type
	 * @param mestraFileArrayList
	 */
	public boolean AllFilesSameType (MestraFileTypeEnum mestraFileType) {
		Iterator<MestraFile> Iter = this.mestraFiles.iterator();
		while ( Iter.hasNext() ){
			MestraFile mestraFile = Iter.next();
			if (mestraFile.Is(mestraFileType) == false) {
				return false;
			}
		}
		return true;
	}

	public void addAll(ArrayList<MestraFile> mestraFileArrayList) {
		this.mestraFiles.addAll(mestraFileArrayList);
	}

	public int size() {
		return this.mestraFiles.size();
	}

	public MestraFile first() {
		if (this.mestraFiles.size()>0) {
			Iterator<MestraFile> Iter = this.mestraFiles.iterator();
			if (Iter.hasNext()) {
				MestraFile mestraFile = Iter.next();
				return mestraFile;
			}
		}
		return null;
	}

}
