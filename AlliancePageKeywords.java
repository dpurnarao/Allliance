/*******************************************************************
 *	Name              :	AlliancePageKeywords
 *	Description       : New class which extends Generic Keywords, This class should be extended by all the local pages  	
 *	Author            : Rooban
 *	Date Created      :	
 *	Modification Log  :                                                     
 *	Date		Initials     	Description of Modifications 
 ********************************************************************/
package com.alliance.functions.generic;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.xframium.device.factory.MorelandWebElement;
import org.xframium.exception.ScriptException;
import org.xframium.page.StepStatus;
import org.xframium.page.data.PageData;
import org.xframium.page.element.Element;
import com.alliance.functions.reporting.CustomReporting;

public class AlliancePageKeywords extends GenericKeywords {

	public static ArrayList<String> cName = new ArrayList<String>();
	public static ArrayList<String> oName = new ArrayList<String>();
	public static String testcase = "";	
	public static String testEnvironment = "";
	public static String strTargetTestSystem= "";
	public static String strScreenShotFlag="";
	public static String currentTran = "";
	public static String strTcflow = "";
	public static String strRole = "";
	public static String strLine = "";
	public static String strEffectiveDate="";
	public static String strPrimaryInsuredDOB = "";
	public static String strEditSection = "";
	public static String strNextPage = "";
	public static String strPolicyNumber = "";
	public static String strProduct = "";
	public static String strSBOActivity="";
	public static String strPolicyReorderFlag="";
	public static String strPolicyReorderDB="";
	public static String strState="";
	public static String strChannel = "";
	public static String strCompany="";
	public static String strTransType ="" ;
	public static String HTMLScreenshot="";

	public Element getClonedElement(String elementName)
	{
		return super.getElement(elementName).cloneElement();
	}

	/*******************************************************************
	 * Name 		: setValue 
	 * Description 	: Used to set the value to any object, Only when there is data in DB 
	 * Author 		: 
	 * Date Created : 11/4/2016
	 * Modification Log : 
	 * Date 		Initials 		Description of Modifications 
	 * 
	 ********************************************************************/
	public void setValue(Element element, String strValue) {
		if (!(strValue.isEmpty())) {

			String xFID = element.getExecutionContext().getxFID();
			element.setValue(strValue, xFID);		

		} else {
			System.out.println("Data not present for element " + element.getKey());
		}
	}

	/*******************************************************************
	 * Name 		: setValue 
	 * Description 	: Used to set value with clone element and add custom report
	 * Author 		: Rooban 
	 * Date Created : 11/01/2018
	 * Modification Log : 
	 * Date 		Initials 		Description of Modifications 
	 * 
	 ********************************************************************/
	public void setValue(Element element, String strValue, String report) {
		if (!(strValue.isEmpty())) {
			try {
				String xFID = element.getExecutionContext().getxFID();	
				CustomReporting.instance().startStep("SET", element, new String[]{strValue});
				element.setValue(strValue, xFID);
				CustomReporting.instance().completeStep(StepStatus.SUCCESS, element.getWebDriver(), null);
			} catch(Exception e){
				CustomReporting.instance().completeStep(StepStatus.FAILURE, element, e);
				throw new ScriptException(e.getMessage());
			}
		} else {
			System.out.println("Data not present for element " + element.getKey());
		}
	}

	/*******************************************************************
	 * Name 		: click 
	 * Description 	: Used to click any type of object
	 * Author 		:  
	 * Date Created : 11/4/2016 
	 * Modification Log : 
	 * Date 		Initials 		Description of Modifications 
	 * 05/15/2017 	BPAUB 			Updated to check if Element is visible, reporting needs to be updated
	 ********************************************************************/
	public void click(Element element) {
		if (isVisible(element)) {
			element.click();			
		} else {
			System.out.println(element.getKey() + " - element does not exist");
		}
	}

	/*******************************************************************
	 * Name 		: click 
	 * Description 	: Used to click cloned element
	 * Author 		: Rooban
	 * Date Created : 11/4/2016 
	 * Modification Log : 
	 * Date 		Initials 		Description of Modifications 
	 * 
	 ********************************************************************/
	public void click(Element element, String report) {
		if (isVisible(element)) {
			try {				
				CustomReporting.instance().startStep("CLICK", element, new String[]{});
				element.click();
				CustomReporting.instance().completeStep(StepStatus.SUCCESS, element.getWebDriver(), null);
			} catch(Exception e){
				CustomReporting.instance().completeStep(StepStatus.FAILURE, element, e);
				throw new ScriptException(e.getMessage());
			}						
		} else {
			System.out.println(element.getKey() + " - element does not exist");
		}
	}

	/*******************************************************************
	 * Name 		: clickMorelandWeb 
	 * Description 	: Used to click Moreland WebElement as _click is not able to perform click 
	 * Author 		: Satish M and bhanu Date
	 * Created 		: 1/6/2017 
	 * Modification Log : 
	 * Date 		Initials 		Description of Modifications
	 ********************************************************************/
	public void clickNativeElement(Element currentElement) throws Exception {
		try {
			CustomReporting.instance().startStep("CLICK", currentElement, new String[]{});
			getNativeElement(currentElement).click();
			Thread.sleep(1000);
			CustomReporting.instance().completeStep(StepStatus.SUCCESS, currentElement.getWebDriver(), null);
		} catch (Exception e) {
			CustomReporting.instance().completeStep(StepStatus.FAILURE, currentElement, e);
			throw new ScriptException(e.getMessage());
		}		
	}

	/*******************************************************************
	 * Name 		: getNativeElement 
	 * Description 	: Used to click Moreland WebElement as _click is not able to perform click 
	 * Author 		: Satish M and bhanu 
	 * Date Created : 1/6/2017 
	 * Modification Log : 
	 * Date 		Initials 		Description of Modifications
	 * 
	 * @return
	 ********************************************************************/
	public WebElement getNativeElement(Element element) {
		WebElement nativeElement = (WebElement) element.getNative();

		if (nativeElement instanceof MorelandWebElement)
			nativeElement = ((MorelandWebElement) nativeElement).getWebElement();
		return nativeElement;
	}

	/*******************************************************************
	 * Name 		: getAttributeValue 
	 * Description 	: Used to retrieve the data from application either by innerHTML or Value 
	 * Author 		: Rooban 
	 * Date Created : 11/30/2016
	 * Modification Log : 
	 * Date 		Initials 		Description of Modifications
	 * 
	 * @input parameter: innerHTML or Value or any attribute name
	 * @return
	 ********************************************************************/
	public String getAttributeValue(Element element, String strAttribute) {
		String strApplicationValue = null;
		// Get the Attribute value
		strApplicationValue = element.getAttribute(strAttribute);

		return strApplicationValue;
	}

	/*******************************************************************
	 * Name 		: isEnable 
	 * Description 	: Used to check if the field element is enabled in application 
	 * Author 		: Vikash 
	 * Parameter	: element
	 * Date Created : 03/14/2017
	 * Modification Log : 
	 * Date 		Initials 		Description of Modifications
	 * 
	 * @return boolean success
	 ********************************************************************/
	public boolean isEnable(Element element) {
		boolean success = false;
		try {
			WebElement nativeElement = (WebElement) element.getNative();
			return success = nativeElement.isEnabled();
		} catch (Exception e) {
			return success;
		}
	}
	
	/*******************************************************************
	 * Name 		: isEnable 
	 * Description 	: Used to check if the field element is enabled in application 
	 * Author 		: Rooban 
	 * Parameter	: element, Time in Seconds
	 * Date Created : 01/22/2018
	 * Modification Log : 
	 * Date 		Initials 		Description of Modifications
	 * 
	 * @return boolean success
	 ********************************************************************/
	public boolean isEnable(Element element, long timeSeconds) {
		boolean success = false;
		
		long impWait = element.getWebDriver().getImplicitWait();		
		if (timeSeconds > 0) {
			element.getWebDriver().manage().timeouts().implicitlyWait(timeSeconds, TimeUnit.SECONDS);
		}
		
		try {
			WebElement nativeElement = (WebElement) element.getNative();
			return success = nativeElement.isEnabled();
		} catch (Exception e) {
			return success;
		}
		finally {
			element.getWebDriver().manage().timeouts().implicitlyWait(impWait, TimeUnit.MILLISECONDS);
		}
	}

	/*******************************************************************
	 * Name 		: isVisible 
	 * Description 	: Used to check if the field element is displayed in application 
	 * Author 		: Rooban 
	 * Parameter	: element
	 * Date Created : 01/05/2017
	 * Modification Log : 
	 * Date 		Initials 		Description of Modifications
	 * 
	 * @return boolean success
	 ********************************************************************/
	public boolean isVisible(Element element) {
		boolean success = false;
		try {
			WebElement nativeElement = (WebElement) element.getNative();
			return success = nativeElement.isDisplayed();
		} catch (Exception e) {
			return success;
		}
	}
	
	/*******************************************************************
	 * Name 		: isVisible 
	 * Description 	: Used to check if the field element is displayed in application 
	 * Author 		: Rooban 
	 * Parameter	: element, Time in Seconds
	 * Date Created : 01/05/2018
	 * Modification Log : 
	 * Date 		Initials 		Description of Modifications
	 * 
	 * @return boolean success
	 ********************************************************************/
	public boolean isVisible(Element element, int timeSeconds) {
		boolean success = false;
		
		long impWait = element.getWebDriver().getImplicitWait();
		if (timeSeconds > 0) {
			element.getWebDriver().manage().timeouts().implicitlyWait(timeSeconds, TimeUnit.SECONDS);
		}
		
		try {
			WebElement nativeElement = (WebElement) element.getNative();
			return success = nativeElement.isDisplayed();
		} catch (Exception e) {
			return success;
		}
		finally {
			element.getWebDriver().manage().timeouts().implicitlyWait(impWait, TimeUnit.MILLISECONDS);
		}
	}

	/*******************************************************************
	 * Name 		: selectByPartOfVisibleText 
	 * Description 	: Used to select options with partial text from xFramium element 
	 * Author 		: bhanu 
	 * Date Created : 1/6/2017 
	 * Modification Log : 
	 * Date 		Initials 		Description of Modifications
	 * 05/15/2017 	BPAUB 			Override method to accept WebElement
	 ********************************************************************/
	public void selectByPartOfVisibleText(Element element, String value) {

		if (!value.isEmpty()) {
			String xFID = element.getExecutionContext().getxFID();
			Select sel = new Select(getNativeElement(element));
			List<WebElement> optionElements = sel.getOptions();
			String strValue = "";

			for (WebElement optionElement : optionElements) {
				if (StringUtils.containsIgnoreCase(optionElement.getText(), value)) {
					strValue = optionElement.getText();
					element.setValue(strValue, xFID);
					break;
				}
			}

			if (strValue.isEmpty()) {
				System.out.println("Value " + value + " doesn't exist " + element.getKey());
				element.setValue(value, xFID);
			}			
		}
	}

	/*******************************************************************
	 * Name 		: selectByPartOfVisibleText 
	 * Description 	: Used to select options with partial text from Selenium WebElement 
	 * Author 		: bhanu 
	 * Date Created : 1/6/2017 
	 * Modification Log : 
	 * Date 		Initials 		Description of Modifications
	 * 05/15/2017 	BPAUB 			Override method to accept WebElement
	 ********************************************************************/
	public void selectByPartOfVisibleText(WebElement element, String value) {

		Select sel = new Select(element);
		List<WebElement> optionElements = sel.getOptions();
		String strValue = "";

		for (WebElement optionElement : optionElements) {
			if (StringUtils.containsIgnoreCase(optionElement.getText(), value)) {
				strValue = optionElement.getText();
				String optionIndex = optionElement.getAttribute("index");
				sel.selectByIndex(Integer.parseInt(optionIndex));				
				break;
			}
		}

		if (strValue.isEmpty()) {
			System.out.println("Value " + value + " doesn't exist in the list box");
			element.sendKeys(value);
		}

	}

	/*******************************************************************
	 * Name 		: getSelectedOption 
	 * Description 	: Used to get the selected value from web list 
	 * Author 		: Bhanu Prakash 
	 * Date Created : 05/15/2017
	 * Modification Log : 
	 * Date 		Initials 		Description of Modifications
	 ********************************************************************/
	public String getSelectedOption(Element element) {
		Select mySelect = new Select(getNativeElement(element));
		WebElement option = mySelect.getFirstSelectedOption();
		return option.getText();
	}

	/*******************************************************************
	 * Name 		: selectEditList 
	 * Description 	: used to select value from element having select & Edit properties from coverage page 
	 * Author 		: Kumar Natasha
	 * Date Created : 
	 * Modification Log : 
	 * Date 		Initials 		Description of Modifications 
	 * 05/15/2017 	BPAUB 			modified to use with single element
	 ********************************************************************/
	public void selectEditList(Element element, String data) {

		if (!(data.isEmpty())) {
			element.click();
			WebElement modifiedElement = getCustomWebDriver().findElement(By.name(element.getKey().replace("selectedId_control", "selectedId_selector")));
			selectByPartOfVisibleText(modifiedElement, data);
		}
	}

	/*******************************************************************
	 * Name 		: selectTerritory 
	 * Description 	: used to select value from select Territory field in vehicles page. 
	 * Author 		: Bhanu Prakash(bpaub) 
	 * Date Created : 
	 * Modification Log : 
	 * Date 		Initials 		Description of Modifications
	 ********************************************************************/
	public void selectTerritory(Element element, String data) {

		if (!(data.isEmpty())) {
			try {
				CustomReporting.instance().startStep("SET", element, new String[]{data});
				click(element);
				WebElement modifiedElement = getCustomWebDriver().findElement(By.id(element.getKey() + "_dropTwo"));
				selectByPartOfVisibleText(modifiedElement, data);
				CustomReporting.instance().completeStep(StepStatus.SUCCESS, element.getWebDriver(), null);
			} catch (Exception e) {
				CustomReporting.instance().completeStep(StepStatus.FAILURE, element, e);
				throw new ScriptException(e.getMessage());
			}	
		}
	}

/*******************************************************************
 * Name 		: selectSubModel
 * Description 	: used to select value from Sub Model field in vehicles page. 
 * Author 		: Rooban 
 * Date Created : 
 * Modification Log : 
 * Date 		Initials 		Description of Modifications
 ********************************************************************/
public void selectSubModel(Element element, String data) {

	if (!(data.isEmpty())) {
		click(element.cloneElement().addToken("ddToken", "control"));
		Element cloneElement = element.cloneElement().addToken("ddToken", "selector");
		selectByPartOfVisibleText(cloneElement, data);
	}
}

/*******************************************************************
 * Name 		: selectSubModel
 * Description 	: used to select value from Sub Model field in vehicles page with two tokens. 
 * Author 		: Rooban 
 * Date Created : 
 * Modification Log : 
 * Date 		Initials 		Description of Modifications
 ********************************************************************/
public void selectSubModel(Element element, String data, String tokenValue) {

	if (!(data.isEmpty())) {
		try {
			CustomReporting.instance().startStep("SET", element, new String[]{data});
			click(element.cloneElement().addToken("vehtoken", tokenValue).addToken("ddToken", "control"));
			Element cloneElement = element.cloneElement().addToken("vehtoken", tokenValue).addToken("ddToken", "selector");
			selectByPartOfVisibleText(cloneElement, data);
			CustomReporting.instance().completeStep(StepStatus.SUCCESS, element.getWebDriver(), null);
		} catch (Exception e) {
			CustomReporting.instance().completeStep(StepStatus.FAILURE, element, e);
			throw new ScriptException(e.getMessage());
		}
	}
}

/*******************************************************************
 *	Name              :	orderCR
 *	Description       : Used to get order Credit Report
 *	PARAMETERS		  : 
 *	Author            :  Bhanu Prakash (bpaub)
 *	Date Created      :	06/09/2017
 *	Modification Log  :                                                     
 *	Date		Initials     	Description of Modifications 
 * @throws InterruptedException 
 *
 ********************************************************************/
public void orderCR() throws InterruptedException{

	String strLine = AlliancePageKeywords.strLine;		
	try {
		CustomReporting.instance().startSyntheticStep("Order CR is SUCCESS", getCustomWebDriver(), new String[]{});

		getCustomWebDriver().findElement(By.xpath("//img[@alt= 'Order CR']")).click();
		waitForTitle("Allstate");
		if(getCustomWebDriver().getTitle().contains("Order Consumer Reports Confirmation")){

			if ((strLine.equalsIgnoreCase("70"))||(strLine.equalsIgnoreCase("32"))||(strLine.equalsIgnoreCase("88")))
			{
				getCustomWebDriver().findElement(By.xpath("//input[contains(@name,'ordercr_1')][@value='Y']")).click();
				getCustomWebDriver().findElement(By.xpath("//input[contains(@name,'OK')][@value='OK']")).click();	
			}
			else
			{
				getCustomWebDriver().findElement(By.xpath("//input[contains(@name,'rivacy')][@value='Y']")).click();
				getCustomWebDriver().findElement(By.id("ok_button")).click();
			}
		}
		Thread.sleep(1000);
		CustomReporting.instance().completeStep(StepStatus.SUCCESS, getCustomWebDriver(), null);
	} catch (Exception e) {
		CustomReporting.instance().completeStep(StepStatus.FAILURE, getCustomWebDriver(), e);
		throw new ScriptException(e.getMessage());
	}		
}

/*******************************************************************
 *	Name              :	orderLIS
 *	Description       : Used to get order LIS
 *	PARAMETERS		  : 
 *	Author            :  Bhanu Prakash (bpaub)
 *	Date Created      :	06/09/2017
 *	Modification Log  :                                                     
 *	Date		Initials     	Description of Modifications 
 * @throws InterruptedException 
 *
 ********************************************************************/
public void orderLIS() throws InterruptedException{

	String strLine = AlliancePageKeywords.strLine;

	try {
		CustomReporting.instance().startSyntheticStep("Order LIS is SUCCESS", getCustomWebDriver(), new String[]{});

		getCustomWebDriver().findElement(By.xpath("//img[@alt= 'Order LIS']")).click();
		waitForTitle("Allstate");
		if(getCustomWebDriver().getTitle().contains("Order Consumer Reports Confirmation")){

			if ((strLine.equalsIgnoreCase("70"))||(strLine.equalsIgnoreCase("32"))||(strLine.equalsIgnoreCase("88")))
			{
				getCustomWebDriver().findElement(By.xpath("//input[contains(@name,'ordercr_1')][@value='Y']")).click();
				getCustomWebDriver().findElement(By.xpath("//input[contains(@name,'OK')][@value='OK']")).click();	
			}
			else
			{
				getCustomWebDriver().findElement(By.xpath("//input[contains(@name,'rivacy')][@value='Y']")).click();
				getCustomWebDriver().findElement(By.id("ok_button")).click();
			}
		}
		Thread.sleep(1000);
		CustomReporting.instance().completeStep(StepStatus.SUCCESS, getCustomWebDriver(), null);
	} catch (Exception e) {
		CustomReporting.instance().completeStep(StepStatus.FAILURE, getCustomWebDriver(), e);
		throw new ScriptException(e.getMessage());
	}
}

/*******************************************************************
 *	Name              :	orderMVR
 *	Description       : Used to get order MVR
 *	PARAMETERS		  : 
 *	Author            :  Bhanu Prakash (bpaub)
 *	Date Created      :	06/09/2017
 *	Modification Log  :                                                     
 *	Date		Initials     	Description of Modifications 
 * @throws InterruptedException 
 *
 ********************************************************************/
public void orderMVR() throws InterruptedException{

	String strLine = AlliancePageKeywords.strLine;

	try {
		CustomReporting.instance().startSyntheticStep("Order MVR is SUCCESS", getCustomWebDriver(), new String[]{});
		getCustomWebDriver().findElement(By.xpath("//img[@alt='Order MVR']")).click();
		waitForTitle("Allstate");
		if(getCustomWebDriver().getTitle().contains("Order Consumer Reports Confirmation")){

			if ((strLine.equalsIgnoreCase("70"))||(strLine.equalsIgnoreCase("32"))||(strLine.equalsIgnoreCase("88")))
			{
				getCustomWebDriver().findElement(By.xpath("//input[contains(@name,'ordercr_1')][@value='Y']")).click();
				getCustomWebDriver().findElement(By.xpath("//input[contains(@name,'OK')][@value='OK']")).click();	
			}
			else
			{
				getCustomWebDriver().findElement(By.xpath("//input[contains(@name,'rivacy')][@value='Y']")).click();
				getCustomWebDriver().findElement(By.id("ok_button")).click();
			}
		}
		Thread.sleep(1000);
		CustomReporting.instance().completeStep(StepStatus.SUCCESS, getCustomWebDriver(), null);
	} catch (Exception e) {
		CustomReporting.instance().completeStep(StepStatus.FAILURE, getCustomWebDriver(), e);
		throw new ScriptException(e.getMessage());
	}	
}

/*******************************************************************
 *	Name              :	fWebDataEntry
 *	Description       : Enter data in application with Token concept
 *	PARAMETERS		  : cName - Array of Element names
 *						tokenName - Element token name
 *						tokenValue - token value to replace for element
 *						pageTestData - PageData to be used
 *						E_ is used for Edit box elements
 *						L_ is used for select elements.
 *						W_ is used for Edit list  objects.
 *	Author            : Bhanu Prakash (bpaub)
 *	Date Created      :	05/18/2017
 *	Modification Log  :                                                     
 *	Date		Initials     	Description of Modifications 
 *8/17/2017     Pallavi Sharma(pshr9)  Function modified to Add checkbox
 ********************************************************************/
public void fWebDataEntry(ArrayList<String> cName , PageData dbRowData, String tokenName, String tokenValue)
{
	for (String obj : cName){
		String fieldType = obj.substring(0,2);
		obj = obj.substring(2,obj.length());	
		String strValue = dbRowData.getData(obj);

		try{
			switch(fieldType.toUpperCase()) {

			case "E_" :	
				if (!(strValue.isEmpty())) {
					CustomReporting.instance().startStep("SET", getElement(obj), new String[]{strValue});
					setValue(getClonedElement(obj).addToken(tokenName,tokenValue),strValue);
					CustomReporting.instance().completeStep(StepStatus.SUCCESS, getClonedElement(obj).getWebDriver(), null);
				}
				break;					

			case "L_" :
				if (!(strValue.isEmpty())) {
					CustomReporting.instance().startStep("SET", getElement(obj), new String[]{strValue});
					setValue(getClonedElement(obj).addToken(tokenName,tokenValue),strValue);
					CustomReporting.instance().completeStep(StepStatus.SUCCESS, getClonedElement(obj).getWebDriver(), null);
				}				
				break;

			case "W_" :
				if (!(strValue.isEmpty())) {
					CustomReporting.instance().startStep("SET", getElement(obj), new String[]{strValue});
					selectEditList(getClonedElement(obj).addToken(tokenName,tokenValue),strValue);
					CustomReporting.instance().completeStep(StepStatus.SUCCESS, getClonedElement(obj).getWebDriver(), null);
				}					
				break; 

			case "C_" :	
				if (!(strValue.isEmpty())) {
					CustomReporting.instance().startStep("CLICK", getElement(obj), new String[]{});
					click(getClonedElement(obj).addToken(tokenName,tokenValue));
					CustomReporting.instance().completeStep(StepStatus.SUCCESS, getClonedElement(obj).getWebDriver(), null);
				}
				break;
			}
		}
		catch(Exception e){
			CustomReporting.instance().completeStep(StepStatus.FAILURE, getElement(obj), e);
			throw new ScriptException(e.getMessage());
		}
	}
	cName.removeAll(cName);
}

/*******************************************************************
 *	Name              :	fWebDataEntry
 *	Description       : Enter data in application without Token concept
 *	PARAMETERS		  : cName - Array of Element names
 *						pageTestData - PageData to be used
 *						E_ is used for Edit/ select elements.
 *						W_ is used for Edit list  objects.
 *	Author            :  Bhanu Prakash (bpaub)
 *	Date Created      :	05/18/2017
 *	Modification Log  :                                                     
 *	Date		Initials     	Description of Modifications 
 *
 ********************************************************************/
public void fWebDataEntry(ArrayList<String> cName , PageData dbRowData)
{
	String strPage = this.getClass().getInterfaces()[ 0 ].getSimpleName();

	for (String obj : cName){
		String fieldType = obj.substring(0,2);
		obj = obj.substring(2,obj.length());
		String strValue = dbRowData.getData(obj);

		switch(fieldType.toUpperCase()) {

		case "E_" :
			setValue(getElement(strPage, obj), strValue);
			break;

		case "L_" :
			setValue(getElement(strPage, obj),strValue);
			break;

		case "W_" :
			selectEditList(getElement(strPage, obj),strValue);
			break; 

		case "C_" :
			if (!(dbRowData.getData(obj).isEmpty())) {
				click(getElement(strPage, obj));
			}
			break;
		}

	}
	cName.removeAll(cName);
}

/*******************************************************************
 *	Name              :	fWebDataEntry
 *	Description       : Enter data in application with Token concept provided Column and Object names are different
 *	PARAMETERS		  : cName - Array of Element names
 *						oName - Array of DB column names
 *						tokenName - Element token name
 *						tokenValue - token value to replace for element
 *						dbRow - Row number of test data
 *						pageTestData - PageData to be used
 *	Author            :  Bhanu Prakash (bpaub)
 *	Date Created      :	05/18/2017
 *	Modification Log  :                                                     
 *	Date		Initials     	Description of Modifications 
 *
 ********************************************************************/
public void fWebDataEntry(ArrayList<String> oName ,ArrayList<String> cName, String tokenName, String tokenValue, PageData dbRowData)
{		
	if (oName.size() == cName.size()) {
		int index = 0;
		for (String obj : oName){
			String fieldType = obj.substring(0,2);
			obj = obj.substring(2,obj.length());
			String strValue = dbRowData.getData(cName.get(index));

			try {
				switch (fieldType.toUpperCase()) {

				case "E_":
					if (!(strValue.isEmpty())) {
						CustomReporting.instance().startStep("SET", getElement(obj), new String[]{strValue});
						setValue(getClonedElement(obj).addToken(tokenName, tokenValue),strValue);
						CustomReporting.instance().completeStep(StepStatus.SUCCESS, getClonedElement(obj).getWebDriver(), null);
					}
					break;

				case "L_" :
					if (!(strValue.isEmpty())) {
						CustomReporting.instance().startStep("SET", getElement(obj), new String[]{strValue});
						setValue(getClonedElement(obj).addToken(tokenName, tokenValue),strValue);
						CustomReporting.instance().completeStep(StepStatus.SUCCESS, getClonedElement(obj).getWebDriver(), null);
					}
					break;

				case "W_":
					if (!(strValue.isEmpty())) {
						CustomReporting.instance().startStep("SET", getElement(obj), new String[]{strValue});
						selectEditList(getClonedElement(obj).addToken(tokenName, tokenValue),strValue);
						CustomReporting.instance().completeStep(StepStatus.SUCCESS, getClonedElement(obj).getWebDriver(), null);
					}
					break;
				}
				index++;

			} catch (Exception e) {
				CustomReporting.instance().completeStep(StepStatus.FAILURE, getElement(obj), e);
				throw new ScriptException(e.getMessage());
			}
		}
	}
	oName.removeAll(oName);
	cName.removeAll(cName);
}

/*******************************************************************
 *	Name              :	fWebDataEntry
 *	Description       : Enter data in application without Token concept provided Column and Object names are different
 *	PARAMETERS		  : cName - Array of Element names
 *						oName - Array of DB column names
 *						dbRow - Row number of test data
 *						pageTestData - PageData to be used
 *	Author            : Rooban
 *	Date Created      :	09/21/2017
 *	Modification Log  :                                                     
 *	Date		Initials     	Description of Modifications 
 *
 ********************************************************************/
public void fWebDataEntry(ArrayList<String> oName ,ArrayList<String> cName, PageData dbRowData)
{
	String strPage = this.getClass().getInterfaces()[ 0 ].getSimpleName();

	if (oName.size() == cName.size()) {
		int index = 0;
		for (String obj : oName){
			String fieldType = obj.substring(0,2);
			obj = obj.substring(2,obj.length());
			switch (fieldType.toUpperCase()) {

			case "E_":					
				setValue(getElement(strPage, obj),dbRowData.getData(cName.get(index)));
				break;

			case "L_" :
				setValue(getElement(strPage, obj),dbRowData.getData(cName.get(index)));
				break;

			case "W_":
				selectEditList(getElement(strPage, obj),dbRowData.getData(cName.get(index)));
				break;
			}
			index++;
		}
	}
	oName.removeAll(oName);
	cName.removeAll(cName);
}

/*******************************************************************/
public void takeScreenShot(){
	if(strScreenShotFlag.equalsIgnoreCase("Yes")){
		takeScreenShot(this.getClass().getSimpleName());
	}
}

/*******************************************************************/
public void failureScreenprint() throws Exception{
	failureScreenprint(this);
}
}

