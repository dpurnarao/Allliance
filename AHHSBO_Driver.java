/*******************************************************************
 *	Name              :	AHHSBO_Driver
 *	Description       : Driver class for AHH-SBO LOB
 *	Author            : ATS  
 *	Date Created      :	10/16/2017
 *	Modification Log  :                                                     
 *	Date		Initials     	Description of Modifications 
 ********************************************************************
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
import com.alliance.page.AHHEN.spi.AHHEN_ClassInvoker;

public class AHHSBO_Driver extends AHHEN_ClassInvoker {	

	//Initialize and declare the global variables
	public static String strState = "";
	public static String tcID = "";
	public static String strAddStd = "";

	@Test( dataProvider = "DeviceManager")
	public void AHHSBO (TestContainer testContainer) throws Exception 
	{		
		try{ 	
			DeviceWebDriver driver = getWebDriver();

			//Load Test case name
			TestName testName = testPackageContainer.get().getTestName();
			String tcID = getTestName();
			AlliancePageKeywords.testcase = tcID;
			strState = tcID.substring(0, 2);

			//Load server Data from ChangeURL
			PageData changeURLData= CustomLoadDataElements.LoadData(driver);
			String strDB=changeURLData.getData("Databasepath");
			AlliancePageKeywords.testEnvironment = changeURLData.getData("TestEnvironment");
			AlliancePageKeywords.strScreenShotFlag = changeURLData.getData("PrintScreen");

			//Load test data from SQL DB
			CustomLoadDataElements.LoadData("AHHSBO",strDB,tcID, driver);

			//Impersonate and Main menu Page
			startStep(testName, "Impersonate Menu Page", "Input Data in Impersonate Menu Page");
			impersonateMenu(changeURLData,"AHHEN");
			stopStep(testName, StepStatus.SUCCESS, null);

			if (getWebDriver().getTitle().contains("Main Menu")) {
				startStep(testName, "Main Menu Page", "Input Data in Main Menu Page");
				mainMenu("AHHEN");
				stopStep(testName, StepStatus.SUCCESS, null);
			}
			CustomLoadDataElements.LoadRepository("AHHEN", driver);

			//Policy View class
			startStep(testName, "Policy View Page", "Input Data in Policy View Page");
			policyView();
			stopStep(testName, StepStatus.SUCCESS, null);

			//Call to Cancel-Rewrite and Transfer-IN flow classes
			if ((AlliancePageKeywords.strSBOActivity.equals("CR")) || (AlliancePageKeywords.strSBOActivity.equals("TI"))){
				startStep(testName, "Endorsement Menu Page", "Input Data in Endorsement Menu Page");
				endorsementMenu();
				stopStep(testName, StepStatus.SUCCESS, null);

				startStep(testName, "Terminate Page", "Input Data in Terminate Page");
				terminateDetails();
				stopStep(testName, StepStatus.SUCCESS, null);

				startStep(testName, "Summary Page", "Input Data in Summary Page");
				summaryDetails();
				stopStep(testName, StepStatus.SUCCESS, null);
			}



			//Load AHH Repository
			CustomLoadDataElements.LoadRepository("AHH",driver);

			//Call to SBO - New Quote classes
			if (AlliancePageKeywords.strSBOActivity.equals("NQ")){
				startStep(testName, "Household Page", "Input Data in Quote - Household Page");
				QuoteHousehold(tcID);
				stopStep(testName, StepStatus.SUCCESS, null);

				startStep(testName, "Dwelling Page", "Input Data in Quote - Dwelling Page");
				QuoteDwelling(tcID);
				stopStep(testName, StepStatus.SUCCESS, null);

				startStep(testName, "Coverage Page", "Input Data in Quote - Coverage Page");
				covergaeQouteDetails(tcID);
				stopStep(testName, StepStatus.SUCCESS, null);
			}

			AlliancePageKeywords.currentTran = "NB";

			//Call to SBO - New Business classes
			startStep(testName, "Household Page", "Input Data in NB - Household Page");
			houseHoldNBDetails(tcID);
			stopStep(testName, StepStatus.SUCCESS, null);

			startStep(testName, "Dwelling Page", "Input Data in NB - Dwelling Page");
			dwellingNBDetails(tcID);
			stopStep(testName, StepStatus.SUCCESS, null);

			startStep(testName, "Coverage Page", "Input Data in NB - Coverage Page");
			coverageNBDetails(tcID);
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