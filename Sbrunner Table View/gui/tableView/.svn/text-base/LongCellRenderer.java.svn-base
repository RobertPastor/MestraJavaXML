package gui.tableView;



import javax.swing.table.DefaultTableCellRenderer;

public class LongCellRenderer extends DefaultTableCellRenderer { 
	/**
}
	 * 
	 */
	private static final long serialVersionUID = -8745102924365745031L;


	public LongCellRenderer() {
		super(); 
	}

	public void setValue(Object value) {
		if (value != null) {
			if (value instanceof Long) {
				Long l = new Long((Long)value);
				if (l == 0) {
					this.setText("--");
				}
				else {
					this.setText((value == null) ? "" : l.toString());
				}
			}
			else {
				this.setText("");
			}
		}
		else {
			this.setText("");
		}
	}

}
