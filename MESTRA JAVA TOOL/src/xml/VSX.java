package xml;


import java.io.File;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * 
 * A simple XML parser that builds a tree from SAX events.<br>
 * The xml file should be passed as a command line argument.
 * @author t0007330
 * @since 2016
 *
 */
public class VSX {

	public TreeModel parse(String filename) {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		XMLTreeHandler handler = new XMLTreeHandler();
		try {
			// Parse the input.
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

	public static class XMLTreeHandler extends DefaultHandler {
		private DefaultMutableTreeNode root, currentNode;  
		public DefaultMutableTreeNode getRoot() {
			return root;
		}


		/**
		 * SAX Parser Handler methods...
		 */
		public void startElement(String namespaceURI, String lName, String qName,
				Attributes attrs) throws SAXException {
			String eName = lName; // Element name
			if ("".equals(eName)) eName = qName;
			Tag t = new Tag(eName, attrs);
			DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(t);
			if (currentNode == null) {
				root = newNode;
			}
			else {
				// Must not be the root node...
				currentNode.add(newNode);
			}
			currentNode = newNode;
		}

		public void endElement(String namespaceURI, String sName, String qName)
				throws SAXException {
			currentNode = (DefaultMutableTreeNode)currentNode.getParent();
		}

		public void characters(char buf[], int offset, int len) throws SAXException {
			String s = new String(buf, offset, len).trim();
			((Tag)currentNode.getUserObject()).addData(s);
		}
	}

	public static class Tag {
		private String name;
		private String data;
		private Attributes attr;

		public Tag(String n, Attributes a) { name = n; attr = a; }

		public String getName() { return name; }
		public Attributes getAttributes() { return attr; }
		public void setData(String d) { data = d; }
		public String getData() { return data; }

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
			return name + a + (data == null ? "" :" (" + data + ")");
		}
	}

	public static void main(String args[]) {

		String filePath = "";
		try {
			filePath = args[0];
		} catch (java.lang.ArrayIndexOutOfBoundsException e){
			filePath = "D:\\Users\\T0007330\\Documents\\03 - Languages_IDE_and_Tools\\06 - JAVA\\MESTRA JAVA TOOL\\src\\xml\\simple.xml";
			filePath = "D:\\MESTRA TOOL\\Mestra_Tool_V3.xml";

		}

		JFrame frame = new JFrame("VSX Test");
		VSX parser = new VSX();
		JTree tree = new JTree(parser.parse(filePath));
		frame.getContentPane().add(new JScrollPane(tree));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(300,400);
		frame.setVisible(true);
	}
}
