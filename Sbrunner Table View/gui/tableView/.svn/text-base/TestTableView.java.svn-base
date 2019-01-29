package gui.tableView;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import gui.tableView.DefaultTableViewColumn;
import gui.tableView.DefaultTableViewModel;
import gui.tableView.TableView;
import gui.tableView.TableViewColumn;


/**
 * Test the table view.
 *
 * @author Stéphane Brunner, Last modified by: $Author: stbrunner $ 
 * @version $Revision: 1.9 $ $Date: 2004/09/05 19:21:45 $.
 * Revision history:
 * $Log: TestTableView.java,v $
 * Revision 1.9  2004/09/05 19:21:45  stbrunner
 * add select by first letter
 *
 * Revision 1.8  2004/02/15 19:29:01  stbrunner
 * *** empty log message ***
 *
 * Revision 1.7  2003/06/06 06:31:57  stbrunner
 * Change description
 *
 */
public class TestTableView {
	/**
	 * Run the application
	 * @param pArgs the arguments
	 */
	public static void main(String[] pArgs) {
		try {
			TableViewColumn[] columns =
			{
					new DefaultTableViewColumn(
							"Name",
							Row.class.getDeclaredMethod("getName", (Class[]) null),
							Row.class.getDeclaredMethod("setName", new Class[] {String.class})),
					new DefaultTableViewColumn(
									"Description",
									Row.class.getDeclaredMethod("getDescription", (Class[]) null),
									Row.class.getDeclaredMethod("setDescription", new Class[] {String.class})),
					new DefaultTableViewColumn(
											"Number",
											Row.class.getDeclaredMethod("getNumber", (Class[]) null),
											Row.class.getDeclaredMethod("setNumber", new Class[] {Integer.class})),
					new DefaultTableViewColumn(
													"Data",
													Row.class.getDeclaredMethod("getData", (Class[]) null),
													Row.class.getDeclaredMethod("setData", new Class[] {String.class})),
					new DefaultTableViewColumn(
															"Create",
															Row.class.getDeclaredMethod("getCreate", (Class[]) null),
															Row.class.getDeclaredMethod("setCreate", new Class[] {Date.class})),
															new DefaultTableViewColumn(
																	"True False",
																	Row.class.getDeclaredMethod("isTrueFalse", (Class[]) null),
																	Row.class.getDeclaredMethod("setTrueFalse", new Class[] {boolean.class} ))
			};

			Row[] rows =
			{
					new Row("toto", "This is my toto", 1, "Hello", new Date(1000000000000L),true),
					new Row("ooo", "This is my ooo", 2, "999", new Date(1000000000000L),true),
					new Row("aaa", "This is my aaa", 3, "888", new Date(200000000000L),true),
					new Row("bbb", "This is my bbb", 4, "777", new Date(300000000000L),true),
					new Row("ccc", "This is my ccc", 1, "666", new Date(400000000000L),true),
					new Row("ddd", "This is my ddd", 1, "555", new Date(500000000000L),true),
					new Row("eee", "This is my eee", 1, "444", new Date(600000000000L),true),
					new Row("fff", "This is my fff", 1, "333", new Date(700000000000L),true),
					new Row("ggg", "This is my ggg", 1, "222", new Date(800000000000L),true),
					new Row("hhh", "This is my hhh", 1, "111", new Date(900000000000L),true)};

			final DefaultTableViewModel model = new DefaultTableViewModel(columns, Arrays.asList(rows));

			for (int i = 0 ; i < 20000 ; i++) {
				model.addRow(new Row("" + 1, "This is my " + i, 1, "" + (20000 - i), new Date(),false));
			}

			JFrame frame = new JFrame();
			final TableView view = new TableView(model);
			view.setMakeIndex(true);
			frame.getContentPane().setLayout(new BorderLayout());
			frame.getContentPane().add(new JScrollPane(view), BorderLayout.CENTER);

			JPanel panelBtn = new JPanel();
			JButton btnNew = new JButton("Add");
			JButton btnRemove = new JButton("Remove");
			btnNew.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent pEvent) {
					List<Object> selection = view.getSelectedRowObjects();
					model.addRow(new Row("New row", "This is s new row", 1, "Data of new row", new Date()));
					view.addRowSelection(selection);
				}
			});
			btnRemove.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent pEvent) {
					List<Object> selection = view.getSelectedRowObjects();
					model.removeRows(selection);
				}
			});

			panelBtn.setLayout(new GridLayout(2, 1));
			panelBtn.add(btnNew);
			panelBtn.add(btnRemove);
			frame.getContentPane().add(panelBtn, BorderLayout.EAST);

			frame.setSize(600, 400);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.validate();
			frame.setVisible(true);
		}
		catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	/**
	 * Defines a row
	 * @author sbrunner
	 */
	public static class Row {
		private String mName;
		private String mDescription;
		private Integer mNumber;
		private String mData;
		private Date mCreate;
		private String mCode;
		private String mLatitude;
		private String mLongitude;
		private boolean mTrueFalse;

		/**
		 * Constructs
		 * @param pName the row name
		 * @param pDescription the row description
		 * @param pNumber the row number
		 * @param pData the row data
		 * @param pCreate creation date
		 */
		public Row(String pName, String pDescription, int pNumber, String pData, Date pCreate,boolean pTrueFalse) {
			mName = pName;
			mDescription = pDescription;
			mNumber = new Integer(pNumber);
			mData = pData;
			mCreate = pCreate;
			mTrueFalse = pTrueFalse;
		}

		public Row(String pName, String pDescription, int pNumber, String pData, Date pCreate) {
			mName = pName;
			mDescription = pDescription;
			mNumber = new Integer(pNumber);
			mData = pData;
			mCreate = pCreate;
		}
		public Row(String pName, String pDescription, String pLong, String pLat) {
			mName = pName;
			mDescription = pDescription;
			mLongitude = pLong;
			mLatitude = pLat;
		}
		public void setCode(String code) {
			mCode = code;
		}
		public String getCode() {
			return mCode;
		}
		public void setLatitude (String pLat) {
			mLatitude = pLat;
		}
		public String getLatitude () {
			return mLatitude;
		}
		public void setLongitude (String pLong) {
			mLongitude = pLong;
		}
		public String getLongitude () {
			return mLongitude;
		}
		/**
		 * Gets the name
		 * @return String the name
		 */
		public String getName() {
			return mName;
		}

		/**
		 * Sets the name
		 * @param pName the name
		 */
		public void setName(String pName) {
			mName = pName;
		}

		/**
		 * Gets the description
		 * @return String the description
		 */
		public String getDescription() {
			return mDescription;
		}

		/**
		 * Sets the description
		 * @param pDescription the description
		 */
		public void setDescription(String pDescription) {
			mDescription = pDescription;
		}

		/**
		 * Gets the number
		 * @return Integer the number
		 */
		public Integer getNumber() {
			return mNumber;
		}

		/**
		 * sets the number
		 * @param pNumber the number
		 */
		public void setNumber(Integer pNumber) {
			mNumber = pNumber;
		}

		/**
		 * Gets the data
		 * @return String the data
		 */
		public String getData() {
			return mData;
		}

		/** 
		 * Sets the data
		 * @param pData the data
		 */
		public void setData(String pData) {
			mData = pData;
		}

		/**
		 * Gets the creation date
		 * @return Date the creation date
		 */
		public Date getCreate() {
			return mCreate;
		}

		/**
		 * Sets the creation date
		 * @param pCreate the creation date
		 */
		public void setCreate(Date pCreate) {
			mCreate = pCreate;
		}
		/**
		 * @return the trueFalse
		 */
		public boolean isTrueFalse() {
			return mTrueFalse;
		}
		/**
		 * @param trueFalse the trueFalse to set
		 */
		public void setTrueFalse(boolean trueFalse) {
			mTrueFalse = trueFalse;
		}
	}
}
