package mestra;

import java.util.ArrayList;
import java.util.Iterator;

public class ErrorWarning {

	/*
	 * September 2007
	 * Author : Robert Pastor
	 * This class manages all errors and Warnings raised on one MESTRA Marker
	 * 
	 */
	
	private ArrayList<String> Errors = null;
	private ArrayList<String> Warnings = null;
	
	public ErrorWarning() {
		Errors = new ArrayList<String>();
		Warnings = new ArrayList<String>();
	}

	public boolean addError(String strError) {

		if (Errors != null) 
			return Errors.add(strError);
		else
			return false;
	}
	
	public boolean addWarning(String strWarning) {

		if (Warnings != null) 
			return Warnings.add(strWarning);
		else
			return false;
	}
	
	public Iterator<String> ErrorsIterator() {
		return Errors.iterator();
	}
	
	public Iterator<String> WarningsIterator() {
		return Warnings.iterator();
	}
}
