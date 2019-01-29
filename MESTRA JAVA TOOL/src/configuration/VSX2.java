package configuration;


import java.io.File;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * 
 * A <b>Reasonably</b> Simple XML utility class.<br>
 * Currently this class has two main methods: parse() and write().<br>
 * Both work with the TreeModel class and are meant to help you<br>
 * view XML documents with JTree.  This version will embed icons<br>
 * and tool tip text in nodes if <code>icon</code> or<br>
 * <code>tiptext</code> attributes are found in the tag.<br>
 * 
 * @author t0007330
 * @since July 2016
 *
 */
public class VSX2 {

	public TreeModel parse(String filename) {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		XMLIconTreeHandler handler = new XMLIconTreeHandler();
		try {
			// Parse the input
			SAXParser saxParser = factory.newSAXParser();
			saxParser.parse( new File(filename), handler);
		}
		catch (Exception e) {
			System.err.println("File Read Error: " + e);
			e.printStackTrace();
			return new DefaultTreeModel(new DefaultMutableTreeNode("error"));
		}
		return new DefaultTreeModel(handler.getRoot());
	}

	public static class XMLIconTreeHandler extends DefaultHandler {
		private DefaultMutableTreeNode root, currentNode;  
		public DefaultMutableTreeNode getRoot() {
			return root;
		}

		// SAX Parser Handler methods...
		public void startElement(String namespaceURI,
				String lName, // local name
				String qName, // qualified name
				Attributes attrs)
		throws SAXException
		{
			String eName = lName; // element name
			if ("".equals(eName)) {
				eName = qName;
			}
			ITag t = new ITag(eName, attrs);
			DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(t);
			if (currentNode == null) {
				root = newNode;
			}
			else {
				// must not be the root node...
				currentNode.add(newNode);
			}
			currentNode = newNode;
		}

		public void endElement(String namespaceURI,
				String sName, // simple name
				String qName  // qualified name	
				)
		throws SAXException	{
			currentNode = (DefaultMutableTreeNode)currentNode.getParent();
		}

		public void characters(char buf[], int offset, int len) throws SAXException {
			String s = new String(buf, offset, len).trim();
			((ITag)currentNode.getUserObject()).addData(s);
		}
	}

	public static class ITag implements IconAndTipCarrier {
		private String name;
		private String data;
		private Attributes attr;
		private Icon icon;
		private String tipText;

		public ITag(String n, Attributes a) { 
			name = n; 
			attr = a;
			for (int i = 0; i < attr.getLength(); i++) {
				String aname = attr.getQName(i);
				String value = attr.getValue(i);
				if (aname.equals("icon")) {
					tipText = value;
					icon = new ImageIcon(value);
					break;
				}
			}
		}

		public String getName() { return name; }
		public Attributes getAttributes() { return attr; }
		public void setData(String d) { data = d; }
		public String getData() { return data; }
		public String getToolTipText() { return tipText; }
		public Icon getIcon() { return icon; }

		public void addData(String d) {
			if (data == null) {
				setData(d);
			}
			else {
				data += d;
			}
		}

		public String getAttributesAsString() {
			StringBuffer buf = new StringBuffer(256);
			for (int i = 0; i < attr.getLength(); i++) {
				buf.append(attr.getQName(i));
				buf.append("=\"");
				buf.append(attr.getValue(i));
				buf.append("\"");
			}
			return buf.toString();
		}

		public String toString() {
			String a = getAttributesAsString();
			//return name + ": " + a + (data == null ? "" :" (" + data + ")");
			return name + a + (data == null ? "" :" (" + data + ")");
		}
	}

	public static void main(String args[]) {
		
		String filePath = "";
		try {
			filePath = args[0];
		} catch (java.lang.ArrayIndexOutOfBoundsException e){
			filePath = "D:\\Users\\T0007330\\Documents\\03 - Languages_IDE_and_Tools\\06 - JAVA\\MESTRA JAVA TOOL\\src\\xml\\simple.xml";
			filePath = "D:\\MESTRA TOOL\\Mestra_Tool_V3_NEO.xml";
		}
		
		JFrame frame = new JFrame("Tree Renderer Test");
		VSX2 parser = new VSX2();
		JTree tree = new JTree(parser.parse(filePath));

		// Steal the default icons from the default renderer...
		DefaultTreeCellRenderer rend1 = new DefaultTreeCellRenderer();
		IconAndTipRenderer rend2 = new IconAndTipRenderer(
				rend1.getOpenIcon(),
				rend1.getClosedIcon(),
				rend1.getLeafIcon());
		tree.setCellRenderer(rend2);
		ToolTipManager.sharedInstance().registerComponent(tree);

		frame.getContentPane().add(new JScrollPane(tree));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(300,400);
		frame.setVisible(true);
	}
}
