package gui.tableView;

import java.awt.Color;

import javax.swing.table.DefaultTableCellRenderer;



public class ColoredTableCellRenderer extends DefaultTableCellRenderer {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7530572436813577780L;

	public  Color RED = Color.red;
	
	public class ColorData
	{
		public  Color  m_color;
		public  Object m_data;
		public  Color GREEN = new Color(0, 128, 0);
		
		public  Color WHITE = Color.white;

		public ColorData(Color color, Object data) {
			m_color = color;
			m_data  = data;
		}

		public ColorData(String data,boolean warning) {
			if (warning == true) {
				m_color = RED;  
			}
			else {
				m_color = WHITE;
			}
			m_data = data;
		}

		public ColorData(Double data) {
			m_color = data.doubleValue() >= 0 ? GREEN : RED;
			m_data  = data;
		}

		public String toString() {
			return m_data.toString();
		}
	}

	public ColoredTableCellRenderer() {
		super();
	}
	
	public void setValue(Object value)  {
		System.out.println("Colored Table Cell Renderer: set Value");
		if (value instanceof ColorData) {
			System.out.println("Colored Table Cell Renderer: set Value: ColorData");

			this.setBackground(((ColorData)value).m_color);
			
			setText(((ColorData)value).m_data.toString());
			
		}
		else {
			if (value instanceof TestTableView.Row) {
				//System.out.println("Colored Table Cell Renderer: set Value: is Test Table View Row");
			}
			else {
				super.setValue(value);
			}
		}
	}
}

