/*******************************************************************
 *	Name              :	LPP_Driver
 *	Description       : Script for LPP driver
 *	Author            : PQS-ATS 
 *	Date Created      :	
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
import com.alliance.page.lpp.spi.LPP_ClassInvoker;

public class LPP_Driver extends LPP_ClassInvoker{
	
	//Initialize and declare the global variables
		public static String strState = "";
		public static String tcID = "";
		public static String strAddStd = "";

	@Test( dataProvider = "DeviceManager")
	public void LPP (TestContainer testContainer) throws Exception 
	{	
		try{ 			
			DeviceWebDriver driver = getWebDriver();
			
			//Load Test case name
			TestName testName = testPackageContainer.get().getTestName();
			String tcID = getTestName();
			AlliancePageKeywords.testcase = tcID;
			strState = tcID.substring(0, 2);
			strAddStd = tcID.substring(tcID.length()-2,tcID.length());
			
			//Load server Data from ChangeURL
			PageData changeURLData= CustomLoadDataElements.LoadData(driver);
			String strDB=changeURLData.getData("Databasepath");
			AlliancePageKeywords.testEnvironment = changeURLData.getData("TestEnvironment");
			AlliancePageKeywords.strScreenShotFlag = changeURLData.getData("PrintScreen");

			//Load test data from SQL DB
			CustomLoadDataElements.LoadData("LPP",strDB,tcID, driver);
						
			//Impersonate, Main menu and Name/Address Page
			
			startStep(testName, "Impersonate Menu Page", "Input Data in Impersonate Menu Page");
			impersonateMenu(tcID,changeURLData,"LPP");
			stopStep(testName, StepStatus.SUCCESS, null);
						
			if (!(getWebDriver().getTitle().contains("Alliance Name/Address"))){
				startStep(testName, "Main Menu Page", "Input Data in Main Menu Page");
				mainMenu(tcID,"LPP");
				stopStep(testName, StepStatus.SUCCESS, null);
			}
			
			CustomLoadDataElements.LoadRepository("LPP", driver);
			startStep(testName, "Name Address Page", "Input Data in Name Address Page");
			nameAddress(tcID);
			stopStep(testName, StepStatus.SUCCESS, null);
			
			//Quote Transaction
			if(!(AlliancePageKeywords.strTcflow.equalsIgnoreCase("NB"))){
				
				startStep(testName, "Household Page", "Input Data in Quote - Household Page");
				lppHouseholdQuoteDetails(tcID);
				stopStep(testName, StepStatus.SUCCESS, null);
				
				startStep(testName, "Dwelling Page", "Input Data in Quote - Dwelling Page");
				lppDwellingQuoteDetails(tcID);
				stopStep(testName, StepStatus.SUCCESS, null);
				
				startStep(testName, "Coverage Page", "Input Data in Quote - Coverage Page");
				lppCoverageQuoteDetails(tcID);
				stopStep(testName, StepStatus.SUCCESS, null);
			}
			
			//Continue with NB Transaction 
			startStep(testName, "Household Page", "Input Data in NB - Household Page");
			lppHouseholdNBDetails(tcID);
			stopStep(testName, StepStatus.SUCCESS, null);
			
			startStep(testName, "Dwelling Page", "Input Data in NB - Dwelling Page");
			lppDwellingNBDetails(tcID);
			stopStep(testName, StepStatus.SUCCESS, null);
			
			startStep(testName, "Coverage Page", "Input Data in NB -Coverage Page");
			lppCoverageNBDetails(tcID);
			stopStep(testName, StepStatus.SUCCESS, null);
			
			startStep(testName, "Bind Page", "Input Data in Bind Page and complete the transaction");
			bindDetails(tcID);
			stopStep(testName, StepStatus.SUCCESS, null);

		}catch(Exception e){
			DeviceWebDriver driver = getWebDriver();
			CustomReporting.instance().screenshot(driver);
			throw new ScriptException(e.toString());       	
		}	
	}
}
