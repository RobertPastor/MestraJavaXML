package mestra;

public class MestraTitle {

	private String Title = "";
	private boolean Empty = false;
	
	public MestraTitle(String title,boolean empty) {
		Title = title;
		Empty = empty;
	}
	
	public String getTitle() {
		return Title;
	}
	
	public boolean isEmpty() {
		return Empty;
	}
}
