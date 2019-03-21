/*******************************************************************
 *	Name              :	LPPEN Driver
 *	Description       : LPP END driver Script
 *	Author            : ATS 
 *	Date Created      :	11/23/2016
 *	Modification Log  :                                                     
 *	Date		Initials     	Description of Modifications 
 ********************************************************************/
package com.alliance.test;

import org.testng.annotations.Test;
import org.xframium.device.factory.DeviceWebDriver;
import org.xframium.device.ng.TestContainer;
import org.xframium.device.ng.TestName;
import org.xframium.exception.ScriptException;
import org.xframium.page.StepStatus;
import org.xframium.page.data.PageData;
import org.xframium.spi.Device;

import com.alliance.functions.generic.AlliancePageKeywords;
import com.alliance.functions.reporting.CustomReporting;
import com.alliance.functions.utility.CustomLoadDataElements;
import com.alliance.page.lppEN.spi.lppEN_ClassInvoker;

public class LPPEN_Driver extends lppEN_ClassInvoker {	

	//Initialize and declare the global variables
	public static String strState = "";
	public static String tcID = "";
	public static String strAddStd = "";

	@Test( dataProvider = "DeviceManager")
	public void LPPEN (TestContainer testContainer) throws Exception 
	{		
		try{ 
			DeviceWebDriver driver = getWebDriver();
			
			//Load Test case name
			TestName testName = testPackageContainer.get().getTestName();
			tcID = getTestName();
			AlliancePageKeywords.testcase = tcID;
			strState = tcID.substring(0, 2);
			strAddStd = tcID.substring(tcID.length()-2,tcID.length());
			
			//Load server Data from ChangeURL
			PageData changeURLData= CustomLoadDataElements.LoadData(driver);
			String strDB=changeURLData.getData("Databasepath");
			AlliancePageKeywords.testEnvironment = changeURLData.getData("TestEnvironment");
			AlliancePageKeywords.strScreenShotFlag = changeURLData.getData("PrintScreen");
						
			//Load test data from SQL DB
			CustomLoadDataElements.LoadData("LPPEN",strDB,tcID, driver);

			//Load repository for Impersonate menu page
			//**CustomLoadDataElements.LoadRepository("ImpersonatePage",driver);

			//Impersonate and Main menu Page
			startStep(testName, "Impersonate Menu Page", "Input Data in Impersonate Menu Page");
			impersonateMenu(changeURLData,"LPPEN");
			stopStep(testName, StepStatus.SUCCESS, null);
			
			if (getWebDriver().getTitle().contains("Main Menu")) {
				startStep(testName, "Main Menu Page", "Input Data in Main Menu Page");
				mainMenu("LPPEN");
				stopStep(testName, StepStatus.SUCCESS, null);
			}

			//Load LPPEN Repository
			CustomLoadDataElements.LoadRepository("LPPEN", driver);

			//Endorsement Menu class
			startStep(testName, "Endorsement Menu Page", "Input Data in Endorsement Menu Page");
			endorsementMenu();
			stopStep(testName, StepStatus.SUCCESS, null);
			
			boolean quit = false;
			while (!quit) {
				//Call to respective classes depending on the endorsement type selected -- 
				switch (AlliancePageKeywords.strNextPage) {
				case "Household":
					startStep(testName, "Household Page", "Input Data in END - Household Page");
					householdENDetails();
					AlliancePageKeywords.strNextPage = "Dwelling";
					stopStep(testName, StepStatus.SUCCESS, null);
					break;

				case "Dwelling":
					startStep(testName, "Dwelling Page", "Input Data in END - Dwelling Page");
					dwellingENDetails();
					AlliancePageKeywords.strNextPage = "Coverage";
					stopStep(testName, StepStatus.SUCCESS, null);
					break;

				case "Coverage":
					startStep(testName, "Coverage Page", "Input Data in END - Coverage Page");
					coverageENDetails();
					AlliancePageKeywords.strNextPage = "Summary";
					stopStep(testName, StepStatus.SUCCESS, null);
					break;

				case "TerminatePolicy": 
					startStep(testName, "Terminate Page", "Input Data in Terminate Page");
					terminateDetails(); 
					AlliancePageKeywords.strNextPage = "Summary";
					stopStep(testName, StepStatus.SUCCESS, null);
					break;

				case "Summary":
					startStep(testName, "Summary Page", "Input Data in Summary Page");
					summaryDetails();
					quit = true;
					stopStep(testName, StepStatus.SUCCESS, null);
					break;                           

				}
			}
		}catch(Exception e){
			DeviceWebDriver driver = getWebDriver();
        	CustomReporting.instance().screenshot(driver);
        	throw new ScriptException(e.toString());  	
		}	
	}
}

