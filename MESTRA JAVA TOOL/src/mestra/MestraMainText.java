package mestra;

import java.util.ArrayList;
import java.util.Iterator;

public class MestraMainText {
	
	private ArrayList<String> textList = null;

	public MestraMainText() {
		this.textList = new ArrayList<String>();
	}
	
	public boolean add(String strText) {
		if (this.textList != null) {
			return this.textList.add(strText);
		}
		return false;
	}

	public String getText() {
		String text = "";
		if (this.textList != null) {
			if (this.textList.size()>0) {
				Iterator<String> Iter = this.textList.iterator();
				while (Iter.hasNext()) {
					text += Iter.next();
				}
			}
		}
		return text;
	}
}
