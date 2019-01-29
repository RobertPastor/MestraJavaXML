package configuration;

/**
 * IconAndTipRenderer.java
 * A renderer for our tree cells that have icons and tool tips.
 */


import java.awt.Color;
import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;

public class IconAndTipRenderer extends JLabel implements TreeCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6387198857940473408L;
	Color backColor = new Color(0xFF, 0xCC, 0xFF);
	Icon openIcon, closedIcon, leafIcon;
	String tipText = "";

	public IconAndTipRenderer(Icon open, Icon closed, Icon leaf) {
		openIcon = open;
		closedIcon = closed;
		leafIcon = leaf;
		setBackground(backColor);
		setForeground(Color.black);
	}

	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean selected,
			boolean expanded, boolean leaf,
			int row, boolean hasFocus) {
		setText(value.toString());
		if (selected) {
			setOpaque(true);
		}
		else {
			setOpaque(false);
		}

		// Try to find an IconAndTipCarrier version of the current node.
		IconAndTipCarrier itc = null;
		if (value instanceof DefaultMutableTreeNode) {
			Object uo = ((DefaultMutableTreeNode)value).getUserObject();
			if (uo instanceof IconAndTipCarrier) {
				itc = (IconAndTipCarrier)uo;
			}
		}
		else if (value instanceof IconAndTipCarrier) {
			itc = (IconAndTipCarrier)value;
		}
		if ((itc != null) && (itc.getIcon() != null)) {
			// Great! Use itc's values to customize this label
			setIcon(itc.getIcon());
			tipText = itc.getToolTipText();
		}
		else {
			// Hmmm, nothing available, so rely on the defaults.
			tipText = " ";
			if (expanded) {
				setIcon(openIcon);
			}
			else if (leaf) {
				setIcon(leafIcon);
			}
			else {
				setIcon(closedIcon);
			}
		}
		return this;
	}

	// Override the default to send back different strings for folders and leaves.
	public String getToolTipText() {
		return tipText;
	}
}
