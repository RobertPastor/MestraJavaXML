package gui.tableView;

import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;

class CheckCellRenderer extends JCheckBox implements TableCellRenderer
{
    /**
     * 
     */
    private static final long serialVersionUID = -6396739182721916334L;
    protected static Border m_noFocusBorder;

    public CheckCellRenderer() {
        super();
        m_noFocusBorder = new EmptyBorder(1, 2, 1, 2);
        this.setOpaque(true);
        this.setBorder(m_noFocusBorder);
        //this.setComponentOrientation(new ComponentOrientation());
    }

    public Component getTableCellRendererComponent(JTable table,
            Object value, boolean isSelected, boolean hasFocus, 
            int row, int column) 
    {
    	//System.out.println("CheckCellRenderer: getTableCellRendererComponent");
        if (value instanceof Boolean) {
            Boolean b = (Boolean)value;
            setSelected(b.booleanValue());
        }

        setBackground(isSelected && !hasFocus ? 
                table.getSelectionBackground() : table.getBackground());
        setForeground(isSelected && !hasFocus ? 
                table.getSelectionForeground() : table.getForeground());

        setFont(table.getFont());
        setBorder(hasFocus ? UIManager.getBorder(
        "Table.focusCellHighlightBorder") : m_noFocusBorder);

        return this;
    }
}
