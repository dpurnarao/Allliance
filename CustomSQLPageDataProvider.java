/*******************************************************************
 *	Name              :	CustomSQLPageDataProvider
 *	Description       : Class to load SQL data from MS-SQL
 *	Author            : Rooban
 *	Date Created      :	
 *	Modification Log  :                                                     
 *	Date		Initials     	Description of Modifications 
 *  
 ********************************************************************/
package com.alliance.functions.utility;

import java.util.List;

import org.xframium.page.data.DefaultPageData;
import org.xframium.page.data.provider.SQLPageDataProvider;

public class CustomSQLPageDataProvider extends SQLPageDataProvider {

	/** The username. */
	private String username;

	/** The password. */
	private String password;

	/** The JDBC URL. */
	private String url;

	/** The driver class name. */
	private String driver;

	/** The query. */
	private String lob;

	private String testCase;	

	public CustomSQLPageDataProvider(String username, String password, String url, String driver,String lob,String testCase) {		
		super(username, password, url, driver, "");
		this.username = username;
		this.password = password;
		this.url = url;
		this.driver = driver;
		this.lob= lob;
		this.testCase=testCase;
	}

	@Override
	public void readPageData() {
		try {

			List<Object[][]> dataMaster = CustomSQLUtils.getResults(username, password, url, driver,lob,testCase);
			for (int recordCount = 0; recordCount < dataMaster.size(); recordCount++) {
				Object[][] data = dataMaster.get(recordCount);
				String[] str_array = ((String) (data[0][0])).split("\\.");
				String tableName = str_array[0];
				addRecordType(tableName, false);
				for (int iLoopCount = 1; iLoopCount < data.length; iLoopCount++) {	

					DefaultPageData currentRecord = new DefaultPageData(tableName, tableName + "-" + iLoopCount, true);

					for (int iLoopInnerCount = 0; iLoopInnerCount < data[iLoopCount].length; iLoopInnerCount++) {

						String [] columnnames= ((String) data[0][iLoopInnerCount]).split("\\.");
						String currentName = columnnames[1];
						String currentValue = (String.valueOf(data[iLoopCount][iLoopInnerCount])).replace(" "," ");

						if (currentValue==null)
							currentValue="";  
						// This is a reference to another page data table
						if (iLoopInnerCount==0)
							currentRecord.addPageData(currentName);

						currentRecord.addValue(currentName, currentValue);
						currentRecord.setContainsChildren(true);
					}
					addRecord(currentRecord);
				}
			}
		} catch (Exception e) {
			log.fatal("Error reading Excel Element File", e);
			log.fatal(e.toString());
			e.printStackTrace();
		}
	}

}
