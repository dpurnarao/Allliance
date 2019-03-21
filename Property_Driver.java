/*******************************************************************
 *	Name              :	 Property Driver
 *	Description       : This Driver Script used to Execute Line 71, 78 and 70 Non AVP company
 *	Author            : ATS 
 *	Date Created      :	09/12/2017
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
import com.alliance.functions.generic.GenericKeywords;
import com.alliance.functions.reporting.CustomReporting;
import com.alliance.functions.utility.CustomLoadDataElements;
import com.alliance.page.property.spi.Property_ClassInvoker;

public class Property_Driver extends Property_ClassInvoker {

	@Test( dataProvider = "DeviceManager")
	public void Property (TestContainer testContainer) throws Exception {
		try{
			DeviceWebDriver driver = getWebDriver();
			TestName testName = testPackageContainer.get().getTestName();
			String tcID = getTestName();
			
			AlliancePageKeywords.testcase = tcID;
		 
			
			//Load server Data from ChangeURL
			PageData changeURLData= CustomLoadDataElements.LoadData(driver);
			
			String strDB=changeURLData.getData("Databasepath");
			AlliancePageKeywords.testEnvironment = changeURLData.getData("TestEnvironment");
			AlliancePageKeywords.strScreenShotFlag = changeURLData.getData("PrintScreen");
			
			//Load test data from SQL DB
			CustomLoadDataElements.LoadData("Property",strDB,tcID,driver);
			startStep(testName, "Impersonate Menu Page", "Input Data in Impersonate Menu Page");
			impersonateMenu(tcID,changeURLData,"Property");
			stopStep(testName, StepStatus.SUCCESS, null);
			GenericKeywords GK=new GenericKeywords();
			if (!(getWebDriver().getTitle().contains("Alliance Name/Address"))){
				
				startStep(testName, "Main Menu Page", "Input Data in Main Menu Page");
				mainMenu(tcID,"Property");
				stopStep(testName, StepStatus.SUCCESS, null);
			}
			CustomLoadDataElements.LoadRepository("Property", driver);
			if ((getWebDriver().getTitle().contains("Alliance Name Address"))){
				startStep(testName, "Name Address Page", "Input Data in Name Address Page");
				nameAddress(tcID); 
				stopStep(testName, StepStatus.SUCCESS, null);
			}	
			if(!(AlliancePageKeywords.strTcflow.equalsIgnoreCase("NB"))){
				startStep(testName, "Household Page", "Input Data in Quote - Household Page");
				householdQuote(tcID);
				stopStep(testName, StepStatus.SUCCESS, null);
				
				startStep(testName, "Dwelling Page", "Input Data in Quote - Dwelling Page");
				dwellingQuote(tcID);
				stopStep(testName, StepStatus.SUCCESS, null);
				
				startStep(testName, "Coverage Page", "Input Data in Quote - Coverage Page");
				coverageQuote(tcID);
				stopStep(testName, StepStatus.SUCCESS, null);
			}	
			startStep(testName, "Household Page", "Input Data in NB - Household Page");
			houseHoldNBDetails(tcID);
			stopStep(testName, StepStatus.SUCCESS, null);
			
			startStep(testName, "Dwelling Page", "Input Data in NB - Dwelling Page");
			dwellingNBDetails(tcID);
			stopStep(testName, StepStatus.SUCCESS, null);
			
			startStep(testName, "Coverage Page", "Input Data in Quote - Coverage Page");
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

