/*******************************************************************
 *	Name              :	CustomLoadDataElements
 *	Description       : Class to load page data from MS-SQL and Excel and page elements from MS-Access
 *	Author            : Rooban
 *	Date Created      :	
 *	Modification Log  :                                                     
 *	Date		Initials     	Description of Modifications 
 *  
 ********************************************************************/
package com.alliance.functions.utility;

import java.io.File;
import org.xframium.device.factory.DeviceWebDriver;
import org.xframium.device.ng.AbstractSeleniumTest;
import org.xframium.page.PageManager;
import org.xframium.page.data.PageData;
import org.xframium.page.data.PageDataManager;
import org.xframium.page.data.provider.ExcelPageDataProvider;
import org.xframium.page.element.provider.SQLElementProvider;
import org.xframium.page.keyWord.KeyWordDriver;
import org.xframium.page.listener.LoggingExecutionListener;

public class CustomLoadDataElements  extends AbstractSeleniumTest{

	/*******************************************************************
	 *	Name              :	LoadRepository
	 *	Description       : Method to load page elements from MS-Access depends on LOB
	 *	Author            : Rooban
	 *	Date Created      :	
	 *	Modification Log  :                                                     
	 *	Date		Initials     	Description of Modifications 
	 *  
	 ********************************************************************/
	public static void LoadRepository(String lob, DeviceWebDriver driver)
	{		
		String xFID = driver.getxFID();
		String username = KeyWordDriver.instance(xFID).getConfigProperties().getProperty("jdbc.username");
		String password = KeyWordDriver.instance(xFID).getConfigProperties().getProperty("jdbc.password");
		String url = KeyWordDriver.instance(xFID).getConfigProperties().getProperty("jdbc.url");
		String driverClassName = KeyWordDriver.instance(xFID).getConfigProperties().getProperty("jdbc.driverClassName");
		String default_qyuery = KeyWordDriver.instance(xFID).getConfigProperties().getProperty("pageManagement.query");

		if (lob.equalsIgnoreCase("Auto")){			 
			url="jdbc:ucanaccess://C://Selenium//Auto.accdb";			
		}
		else if (lob.equalsIgnoreCase("AutoEN")){
			url="jdbc:ucanaccess://C://Selenium//AutoEN.accdb";  
		}
		else if (lob.equalsIgnoreCase("Property")){
			url="jdbc:ucanaccess://C://Selenium//AllianceProperty.accdb"; 
		}
		else if (lob.equalsIgnoreCase("PropertyEN")){
			url="jdbc:ucanaccess://C://Selenium//PropertyEN.accdb"; 
		}
		else if (lob.equalsIgnoreCase("AHH")){
			url="jdbc:ucanaccess://C://Selenium//AllianceAHH.accdb"; 
		}
		else if (lob.equalsIgnoreCase("AHHEN")){
			url="jdbc:ucanaccess://C://Selenium//AHHEN.accdb";
		}
		else if (lob.equalsIgnoreCase("Mcycle")){
			url="jdbc:ucanaccess://C://Selenium//Mcycle.accdb";
		} 
		else if (lob.equalsIgnoreCase("McycleEN")){
			url="jdbc:ucanaccess://C://Selenium//McycleEN.accdb";
		}
		else if (lob.equalsIgnoreCase("LPP")){
			url="jdbc:ucanaccess://C://Selenium//LPP.accdb";
		} 
		else if (lob.equalsIgnoreCase("LPPEN")){
			url="jdbc:ucanaccess://C://Selenium//LPPEN.accdb";
		}		
		else if (lob.equalsIgnoreCase("PUP")){
			url="jdbc:ucanaccess://C://Selenium//PUP.accdb";  
		}
		else if (lob.equalsIgnoreCase("PUPEN")){
			url="jdbc:ucanaccess://C://Selenium//PUPEN.accdb";  
		}
		else if (lob.equalsIgnoreCase("Boat")){
			url="jdbc:ucanaccess://C://Selenium//Boat.accdb"; 
		}
		else if (lob.equalsIgnoreCase("BoatEN")){
			url="jdbc:ucanaccess://C://Selenium//BoatEN.accdb"; 
		}

		PageManager.instance(xFID).setSiteName( "Alliance" );
		PageManager.instance(xFID).registerExecutionListener( new LoggingExecutionListener() );
		PageManager.instance(xFID).setElementProvider(new SQLElementProvider(username, password, url, driverClassName, default_qyuery));
	}	

	/*******************************************************************
	 *	Name              :	LoadData
	 *	Description       : Method to load page data from MS-SQL depends on LOB, Test Case and DB
	 *	Author            : Rooban
	 *	Date Created      :	
	 *	Modification Log  :                                                     
	 *	Date		Initials     	Description of Modifications 
	 *  
	 ********************************************************************/
	public static void LoadData(String lob,String strDB, String testName, DeviceWebDriver driver)
	{
		String url="";
		String username="";

		if (strDB.equalsIgnoreCase("RTD")){
			url= "jdbc:sqlserver://p4sst006:1433;databaseName=P4DAllianceQCTest;integratedSecurity=true";
			username="AllianceRTDDB";
		}
		else if (strDB.equalsIgnoreCase("RR")){ 
			url="jdbc:sqlserver://p4sst006:1433;databaseName=P4DQCTEST;integratedSecurity=true"; 
			username="AllianceRRDB";
		}
		else if (strDB.equalsIgnoreCase("PT")){
			url="jdbc:sqlserver://p4sst006:1433;databaseName=P4DPTQCTest;integratedSecurity=true"; 
			username="AllianceFTDB";
		}

		String password="Pa$$w0rd";
		String driverClassName="com.microsoft.sqlserver.jdbc.SQLServerDriver";

		String xFID = driver.getxFID();
		PageDataManager.instance(xFID).setPageDataProvider( new CustomSQLPageDataProvider(username, password, url, driverClassName,lob,testName));
	}

	/*******************************************************************
	 *	Name              :	LoadData
	 *	Description       : Method to load environment details from MS-Excel
	 *	Author            : Rooban
	 *	Date Created      :	
	 *	Modification Log  :                                                     
	 *	Date		Initials     	Description of Modifications 
	 *  
	 ********************************************************************/
	public static PageData LoadData(DeviceWebDriver driver)
	{	
		//data provider for reading data from excel
		String xFID = driver.getxFID();
		String strChangeurlPath = KeyWordDriver.instance(xFID).getConfigProperties().getProperty("changeUrl.path");
		PageDataManager.instance(xFID).setPageDataProvider( new ExcelPageDataProvider(new File(strChangeurlPath+"/ChangeURL.xlsx"),"Sheet1"));

		PageData pagedata = PageDataManager.instance(xFID).getPageData("Sheet1","TC");
		return pagedata;
	}
}
