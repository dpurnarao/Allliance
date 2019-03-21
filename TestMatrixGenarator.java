/*******************************************************************
 *	Name              :	TestMatrixGenarator
 *	Description       : 
 *	Author            : Rooban
 *	Date Created      :	
 *	Modification Log  :                                                     
 *	Date		Initials     	Description of Modifications 
 *  
 ********************************************************************/
package com.alliance.functions.utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

public class TestMatrixGenarator {

	public static HashMap<String, ArrayList<String>>  testMatrixList = new HashMap<>();
	private static TestMatrixGenarator singleton = new TestMatrixGenarator();

	public static TestMatrixGenarator instance()
	{
		return singleton;
	}

	private TestMatrixGenarator()
	{

	}

	public void genarateTestMatrixList(String url) throws Exception
	{
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String query = "Select Distinct(TestScriptName)from tbl_TestMappingMatrix order by TestScriptName";

		String driver="net.ucanaccess.jdbc.UcanaccessDriver";
		Connection conn=null;

		conn=TestMatrixGenarator.getMSAccessConnection(url, driver);

		pstmt = conn.prepareStatement(query);
		rs = pstmt.executeQuery();		

		while(rs.next())
		{
			String testScriptName =rs.getObject("TestScriptName").toString();
			ArrayList<String> dataSetNameList= new ArrayList<String>();
			dataSetNameList.clear();

			//Get List of Data Set for the Script
			String query1 = "Select * from [tbl_TestMappingMatrix] where TestScriptName='" + testScriptName +"'" + " and Execute in ('Yes','yes')";
			PreparedStatement pstmt1 = conn.prepareStatement(query1);
			ResultSet rsDataSet = pstmt1.executeQuery();
			while(rsDataSet.next())
			{
				String datSetName =rsDataSet.getObject("DataSetName").toString();
				dataSetNameList.add(datSetName);
			}
			testMatrixList.put(testScriptName.toUpperCase(),dataSetNameList);
		}	
	}

	public HashMap<String, ArrayList<String>> getTestMatrixList()
	{
		return testMatrixList;		
	}

	public ArrayList<String> getTestList(String sMethodName)
	{
		ArrayList<String> returnList = new ArrayList<String>();
		String strKey = sMethodName.toUpperCase();
		if(testMatrixList.containsKey(strKey))
			returnList = testMatrixList.get(strKey);
		else
			returnList.add(sMethodName);

		return returnList;
	}

	private static Connection getMSAccessConnection( String url,String driver) throws Exception
	{
		Connection conn1 = null;
		Class.forName( driver );
		conn1 = DriverManager.getConnection(url);
		return conn1;
	}
}
