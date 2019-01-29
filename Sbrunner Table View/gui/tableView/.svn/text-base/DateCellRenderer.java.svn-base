package gui.tableView;

import java.text.DateFormat;

import javax.swing.table.DefaultTableCellRenderer;

public class DateCellRenderer extends DefaultTableCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3347674736873317509L;
	
	private DateFormat formatter = null;
	
	public DateCellRenderer() {
		super(); 
	}

	public void setValue(Object value) {
		if (this.formatter == null) {
			this.formatter = DateFormat.getDateInstance();
		}
		setText((value == null) ? "" : formatter.format(value));
	}
}


