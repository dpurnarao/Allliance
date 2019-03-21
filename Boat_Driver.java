/*******************************************************************
 *	Name              :	Boat_Driver
 *	Description       : Driver class for Boat Quote-NB LOB
 *	Author            : ATS  
 *	Date Created      :	09/22/2017
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
import com.alliance.page.boat.spi.Boat_ClassInvoker;

public class Boat_Driver extends Boat_ClassInvoker {	

	// INITIALIZE AND DECLARE GLOBAL VARIABLES
	public static String strState = "";
//	public static String tcID = "";
	public static String strAddStd = "";

	@Test( dataProvider = "DeviceManager")
	public void Boat (TestContainer testContainer) throws Exception 
	{		
		try{ 		
			DeviceWebDriver driver = getWebDriver();
			// LOAD TEST CASE NAME
			TestName testName = testPackageContainer.get().getTestName();
			String tcID = getTestName();
			AlliancePageKeywords.testcase = tcID;
			strState = tcID.substring(0, 2);
			strAddStd = tcID.substring(tcID.length()-2,tcID.length());

			//  LOAD REPOSITORY FOR IMPERSONATE MENU PAGE
//			CustomLoadDataElements.LoadRepository("ImpersonatePage");

			// Load server Data from ChangeURL
			PageData changeURLData= CustomLoadDataElements.LoadData(driver);
			String strDB=changeURLData.getData("Databasepath");
			AlliancePageKeywords.testEnvironment = changeURLData.getData("TestEnvironment");
			AlliancePageKeywords.strScreenShotFlag = changeURLData.getData("PrintScreen");

			// Load test data of BOAT LOB from SQL DB
			CustomLoadDataElements.LoadData("BOAT",strDB,tcID, driver);

			//Impersonate, Main menu and Name/Address Page	
			startStep(testName, "Impersonate Menu Page", "Input Data in Impersonate Menu Page");
			impersonateMenu(tcID,changeURLData,"BOAT");
			stopStep(testName, StepStatus.SUCCESS, null);

			if (!(getWebDriver().getTitle().contains("Alliance Name/Address"))){
				startStep(testName, "Main Menu Page", "Input Data in Main Menu Page");
				mainMenu(tcID,"BOAT");
				stopStep(testName, StepStatus.SUCCESS, null);
			}

			CustomLoadDataElements.LoadRepository("BOAT", driver);
			startStep(testName, "Name Address Page", "Input Data in Name Address Page");
			nameAddress(tcID); 
			stopStep(testName, StepStatus.SUCCESS, null);

//**
			// Quote Transaction
			if(!(AlliancePageKeywords.strTcflow.equalsIgnoreCase("NB"))){
				
				startStep(testName, "Household Page", "Input Data in Quote - Household Page");
				householdQuotePage(tcID);
				stopStep(testName, StepStatus.SUCCESS, null);
				
				startStep(testName, "Vehicle Page", "Input Data in Quote - Vehicle Page");
				vehicleQuotePage(tcID);
				stopStep(testName, StepStatus.SUCCESS, null);
				
				startStep(testName, "Coverage Page", "Input Data in Quote - Coverage Page");
				coverageQuotePage(tcID);
				stopStep(testName, StepStatus.SUCCESS, null);
			}	

			// Continue with NB Transaction
			startStep(testName, "Household Page", "Input Data in NB - Household Page");
			householdNBDetails(tcID);
			stopStep(testName, StepStatus.SUCCESS, null);
			
			startStep(testName, "Vehicle Page", "Input Data in NB - Vehicle Page");
			vehicleNBDetails(tcID);
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

