/*******************************************************************
 *	Name              :	Pup_Driver
 *	Description       : Driver class for Pup Quote-NB LOB
 *	Author            : ATS  
 *	Date Created      :	11/24/2017
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
import com.alliance.functions.generic.AlliancePageKeywords;
import com.alliance.functions.reporting.CustomReporting;
import com.alliance.functions.utility.CustomLoadDataElements;
import com.alliance.page.pup.spi.Pup_ClassInvoker;


public class Pup_Driver extends Pup_ClassInvoker {	

	//Initialize and declare the global variables
	public static String strState = "";
	public static String tcID = "";
	public static String strAddStd = "";

	@Test( dataProvider = "DeviceManager")
	public void PUP (TestContainer testContainer) throws Exception 
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
			CustomLoadDataElements.LoadData("PUP",strDB,tcID, driver);

			//Impersonate, Main menu and Name/Address Page
			startStep(testName, "Impersonate Menu Page", "Input Data in Impersonate Menu Page");
			impersonateMenu(tcID,changeURLData,"PUP");
			stopStep(testName, StepStatus.SUCCESS, null);
			
			if (!(getWebDriver().getTitle().contains("Alliance Name/Address"))){
				startStep(testName, "Main Menu Page", "Input Data in Main Menu Page");
				mainMenu(tcID,"PUP");
				stopStep(testName, StepStatus.SUCCESS, null);
			}
			
			CustomLoadDataElements.LoadRepository("PUP", driver);
					
			//Quote Transaction
			if(!(AlliancePageKeywords.strTcflow.equalsIgnoreCase("NB"))){
				startStep(testName, "SupportingPolicies Page", "Input Data in Quote - SupportingPolicies Page");
				supportingPolicies(tcID);
				stopStep(testName, StepStatus.SUCCESS, null);
				
				startStep(testName, "Household Page", "Input Data in Quote - Household Page");
				householdQuoteDetails(tcID);
				stopStep(testName, StepStatus.SUCCESS, null);
				
				startStep(testName, "ExposureDetails Page", "Input Data in Quote - ExposureDetails Page");
				exposureQuoteDetails(tcID);
				stopStep(testName, StepStatus.SUCCESS, null);
				
				startStep(testName, "Coverage Page", "Input Data in Quote - Coverage Page");
				coverageQuoteDetails(tcID);
				stopStep(testName, StepStatus.SUCCESS, null);
			}	
			
			//Continue with NB Transaction
			startStep(testName, "SupportingPolicies Page", "Input Data in NB - SupportingPolicies Page");
			supportingPolicies(tcID);
			stopStep(testName, StepStatus.SUCCESS, null);
			
			startStep(testName, "Household Page", "Input Data in NB - Household Page");
			houseHoldNBDetails(tcID);
			stopStep(testName, StepStatus.SUCCESS, null);
			
			startStep(testName, "ExposureDetails Page", "Input Data in NB - ExposureDetails Page");
			exposureNBDetails(tcID);
			stopStep(testName, StepStatus.SUCCESS, null);
			
			startStep(testName, "Coverage Page", "Input Data in NB - Coverage Page");
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

