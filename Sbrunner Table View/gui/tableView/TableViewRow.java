package gui.tableView;

import java.util.Date;

public class TableViewRow {

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

		/**
		 * Constructs
		 * @param pName the row name
		 * @param pDescription the row description
		 * @param pNumber the row number
		 * @param pData the row data
		 * @param pCreate creation date
		 */
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
	}


}
