/*******************************************************************
 *	Name              :	AHH Driver
 *	Description       : Driver Script for AHH Driver 
 *	Author            : Pallavi Sharma  
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
import com.alliance.page.AHH.spi.AHH_ClassInvoker;
public class AHH_Driver extends AHH_ClassInvoker {

	//Initialize and declare the global variables
	public static String strState = "";

	@Test( dataProvider = "DeviceManager")
	public void AHH (TestContainer testContainer) throws Exception 
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
			
			CustomLoadDataElements.LoadData("AHH",strDB,tcID,driver);

			startStep(testName, "Impersonate Menu Page", "Input Data in Impersonate Menu Page");
			impersonateMenu(tcID,changeURLData,"AHH");
			stopStep(testName, StepStatus.SUCCESS, null);

			if (!(getWebDriver().getTitle().contains("Alliance Name/Address"))){
				startStep(testName, "Main Menu Page", "Input Data in Main Menu Page");
				mainMenu(tcID,"AHH");
				stopStep(testName, StepStatus.SUCCESS, null);
			}


			CustomLoadDataElements.LoadRepository("AHH", driver);
			startStep(testName, "Name Address Page", "Input Data in Name Address Page");
			nameAddress(tcID); 
			stopStep(testName, StepStatus.SUCCESS, null);

			if(!(AlliancePageKeywords.strTcflow.equalsIgnoreCase("NB"))){
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

			//Continue with NB Transaction
			startStep(testName, "Household Page", "Input Data in NB - Household Page");
			houseHoldNBDetails(tcID);
			stopStep(testName, StepStatus.SUCCESS, null);
			
			startStep(testName, "Dwelling Page", "Input Data in NB - Dwelling Page");
			dwellingNBDetails(tcID);
			stopStep(testName, StepStatus.SUCCESS, null);
			
			startStep(testName, "Coverage Page", "Input Data in NB -Coverage Page");
			coverageNBDetails(tcID);
			stopStep(testName, StepStatus.SUCCESS, null);
			
			startStep(testName, "Bind Page", "Input Data in Bind Page and complete the transaction");
			bindDetails(tcID);
			stopStep(testName, StepStatus.SUCCESS, null);
			
		}			
		catch(Exception e){
			DeviceWebDriver driver = getWebDriver();
			CustomReporting.instance().screenshot(driver);
			throw new ScriptException(e.toString()); 
		}
	}
}

