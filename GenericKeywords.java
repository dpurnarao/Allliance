/*******************************************************************
 *	Name              :	GenericKeywords
 *	Description       : Class contains generic keywords	
 *	Author            : Rooban
 *	Date Created      :	
 *	Modification Log  :                                                     
 *	Date		Initials     	Description of Modifications 
 ********************************************************************/
package com.alliance.functions.generic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.openqa.selenium.Alert;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.security.UserAndPassword;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.asserts.SoftAssert;
import org.xframium.container.SuiteContainer;
import org.xframium.device.factory.DeviceWebDriver;
import org.xframium.page.AbstractPage;
import org.xframium.page.Page;
import org.xframium.page.PageManager;
import org.xframium.page.StepStatus;
import org.xframium.page.data.PageData;
import org.xframium.page.data.PageDataManager;
import org.xframium.page.element.Element;
import org.xframium.page.keyWord.KeyWordDriver;
import org.xframium.page.keyWord.step.spi.KWSDumpState;

import com.alliance.functions.reporting.CustomReporting;
import com.alliance.functions.utility.CustomLoadDataElements;
import com.google.common.base.Function;

public class GenericKeywords extends AbstractPage {

	private static FileInputStream pic = null;
	private static FileOutputStream out = null;
	private static String screenShotWordDocPath = "";
	private static XWPFDocument doc = null;
	private static XWPFParagraph p = null;
	private static XWPFRun r = null;
	private Connection conn1 = null;
	private static KWSDumpState dumpState = new KWSDumpState();	
	private static Map contextMap = new HashMap<String, Object>(); 

	/*******************************************************************
	 * Name 		: getCustomWebDriver 
	 * Description 	: Used to get the webDriver Object
	 * Author 		: 
	 * Date Created : 11/4/2016 
	 * Modification Log : 
	 * Date	 		Initials 		Description of Modifications
	 * @return 
	 ********************************************************************/
	public DeviceWebDriver getCustomWebDriver()
	{	
		return (DeviceWebDriver) webDriver;
	}	

	/*******************************************************************
	 * Name 		: checkDefaultValue 
	 * Description 	: Used to compare the attribute value of an object 
	 * Author 		: Kiran Nidavani 
	 * Date Created : 11/4/2016
	 * Modification Log : 
	 * Date 		Initials 		Description of Modifications
	 ********************************************************************/
	public void checkDefaultValue(SoftAssert assertSoft, Element element, String strAttribute, String strExpValue) {
		String strActValue;

		strActValue = element.getAttribute(strAttribute);
		CustomReporting.instance().startSyntheticStep("COMPARE", element, new String[]{strAttribute,strExpValue});
		if (strActValue.equals(strExpValue)) {				
			CustomReporting.instance().completeStep(StepStatus.SUCCESS, element, null);
		}
		else {
			CustomReporting.instance().completeStep(StepStatus.FAILURE, element, null);
		}
		assertSoft.assertEquals(strExpValue, strActValue);		
	}

	/*******************************************************************
	 *	Name              :	_getRelativeDate
	 *	Description       : Used to get the relative date wrt Effective date
	 *	Author            : Pallavi Sharma
	 *	Date Created      :	08/15/2017
	 *	Modification Log  :                                                     
	 *	Date		Initials     	Description of Modifications 
	 * @return 
	 ********************************************************************/	
	public String getRelativeDate(String strRelation, String intDays, String intMonths, String intYears )
	{
		DateFormat dateFormate = new SimpleDateFormat("MM/dd/yyyy");

		String strEffectivedate = AlliancePageKeywords.strEffectiveDate;
		Calendar cal = Calendar.getInstance();

		String[] effectdate = strEffectivedate.split("/");
		String strMonth = effectdate[0];
		int Month = Integer.parseInt(strMonth)-1;
		String strDate= effectdate[1];
		int Days = Integer.parseInt(strDate);
		String strYear = effectdate[2];
		int Year = Integer.parseInt(strYear);

		if (intDays.isEmpty())
			intDays = "0";

		if (intMonths.isEmpty())
			intMonths = "0";

		if (intYears.isEmpty())
			intYears = "0";
		cal.set(Year, Month, Days);

		switch (strRelation.toUpperCase()) {
		case "PAST":
			cal.add(Calendar.DATE, -Integer.parseInt(intDays));
			cal.add(Calendar.MONTH, -Integer.parseInt(intMonths));
			cal.add(Calendar.YEAR, -Integer.parseInt(intYears));
			break;
		case "FUTURE":
			cal.add(Calendar.DATE, Integer.parseInt(intDays));
			cal.add(Calendar.MONTH, Integer.parseInt(intMonths));
			cal.add(Calendar.YEAR, Integer.parseInt(intYears));
			break;
		default:
			break;
		}	

		return dateFormate.format(cal.getTime());		
	}
	/*******************************************************************
	 * Name 		: getCalculatedDate 
	 * Description 	: Used to get the relative date wrt current date 
	 * Author 		: Kiran Nidavani 
	 * Date Created : 11/10/2016
	 * Modification Log : 
	 * Date 		Initials 		Description of Modifications
	 ********************************************************************/
	public String getCalculatedDate(String strRelation, String intDays, String intMonths, String intYears ) {
		DateFormat dateFormate = new SimpleDateFormat("MM/dd/yyyy");
		Date dateTodays = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(dateTodays);

		if (intDays.isEmpty())
			intDays = "0";

		if (intMonths.isEmpty())
			intMonths = "0";

		if (intYears.isEmpty())
			intYears = "0";
		switch (strRelation.toUpperCase()) {
		case "PAST":
			cal.add(Calendar.DATE, -Integer.parseInt(intDays));
			cal.add(Calendar.MONTH, -Integer.parseInt(intMonths));
			cal.add(Calendar.YEAR, -Integer.parseInt(intYears));
			break;
		case "FUTURE":
			cal.add(Calendar.DATE, Integer.parseInt(intDays));
			cal.add(Calendar.MONTH, Integer.parseInt(intMonths));
			cal.add(Calendar.YEAR, Integer.parseInt(intYears));
			break;
		default:
			break;
		}

		return dateFormate.format(cal.getTime());
	}

	/*******************************************************************
	 * Name 		: getRandomString 
	 * Description 	: Used to get the relative date wrt current date 
	 * Author 		: Kiran Nidavani 
	 * Date Created : 11/10/2016
	 * Modification Log : 
	 * Date 		Initials 		Description of Modifications
	 * 
	 ********************************************************************/
	public enum DATATYPE {
		number, varchar, character;
	}

	public String getRandomString(DATATYPE type, int length) {
		String strType = type.toString();
		String defaultString = "";

		if (!strType.equalsIgnoreCase("number"))
			defaultString = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

		if (!strType.equalsIgnoreCase("character"))
			defaultString = defaultString + "0123456789";

		StringBuilder strCh = new StringBuilder();
		Random rnd = new Random();

		for (int i = 0; i < length; i++)
			strCh.append(defaultString.charAt(rnd.nextInt(defaultString.length())));

		return strCh.toString();
	}

	/*******************************************************************
	 * Name 		: alertHandler 
	 * Description 	: Used to handle pop-up which comes up during execution 
	 * Author 		: Rooban 
	 * Date Created : 11/30/2016 
	 * Modification Log : 
	 * Date 		Initials 		Description of Modifications
	 * 
	 * @return
	 ********************************************************************/
	public void alertHandler(WebDriver driver, String strOperation) {
		try {

			WebDriverWait wait = new WebDriverWait(driver, 10);
			Alert alert = wait.until(ExpectedConditions.alertIsPresent());

			// Accept or dismiss the alert
			switch (strOperation.toUpperCase()) {
			case "OK":
				alert.accept();
				driver.switchTo().defaultContent();
				log.info("Clicked on OK Button successfully");
				CustomReporting.instance().startSyntheticStep(StepStatus.SUCCESS, "Alert handler - OK", (DeviceWebDriver) driver, null);
				break;
			case "CANCEL":
				alert.dismiss();
				driver.switchTo().defaultContent();
				log.info("Clicked on CANCEL Button successfully");
				CustomReporting.instance().startSyntheticStep(StepStatus.SUCCESS, "Alert handler - CANCEL", (DeviceWebDriver) driver, null);
				break;

			default:
				System.out.println("Pass the correct data either as 'OK' or 'CANCEL'");
				System.exit(1);
				break;
			}

		} catch (Exception e) {
			System.out.println("No Alert Pop-Up opened at this moment");
		}
	}

	/*******************************************************************
	 * Name 		: closeWindow 
	 * Description 	: Used to close popup windows which comes up during execution 
	 * Author 		: Rooban 
	 * Date Created : 11/30/2016
	 * Modification Log : 
	 * Date 		Initials 		Description of Modifications
	 * 
	 * @return
	 ********************************************************************/
	public void closeWindow(WebDriver driver) {
		String parentWindow = driver.getWindowHandle();

		for (String handle : driver.getWindowHandles()) {
			if (!handle.equals(parentWindow)) {
				driver.switchTo().window(handle);
				driver.close();
			}
		}
		driver.switchTo().window(parentWindow);
	}

	/*******************************************************************
	 * Name 		: handleIMSecurityAlert 
	 * Description 	: Used to handle windows authentication popup which comes up on clicking Alliance URL 
	 * Author 		: Rooban 
	 * Date Created : 11/30/2016 
	 * Modification Log : 
	 * Date 		Initials		Description of Modifications
	 * @param driver 
	 * 
	 * @return
	 ********************************************************************/
	public void handleIMSecurityAlert(DeviceWebDriver driver) throws InterruptedException {
		WebDriverWait wait = new WebDriverWait(driver, 15);
		wait.until(ExpectedConditions.alertIsPresent());

		// driver.switchTo().alert().authenticateUsing(new UserAndPassword("tst-allaut1","Allianceaut1"));
		driver.switchTo().alert().authenticateUsing(new UserAndPassword("tst-allaut2", "Holiday123"));
		driver.switchTo().defaultContent();
	}

	/*******************************************************************
	 * Name 		: compareSubStringCaseSensitive 
	 * Description 	: Used to compare sub string with case sensitive 
	 * Author 		: Rooban 
	 * Date Created : 11/30/2016
	 * Modification Log : 
	 * Date 		Initials 		Description of Modifications
	 * 
	 * @return
	 ********************************************************************/
	public boolean compareSubStringCaseSensitive(String strChar, String strSearchChar) {
		Boolean blnCondition = false;

		if (StringUtils.contains(strChar, strSearchChar)) {
			blnCondition = true;
		}
		return blnCondition;
	}

	/*******************************************************************
	 * Name 		: compareSubStringIgnorecase 
	 * Description 	: Used to compare sub string with Ignore Case 
	 * Author 		: Rooban 
	 * Date Created : 11/30/2016
	 * Modification Log : 
	 * Date 		Initials 		Description of Modifications
	 * 
	 * @return
	 ********************************************************************/
	public boolean compareSubStringIgnorecase(String strChar, String strSearchChar) {
		boolean blnCondition = false;

		if (StringUtils.containsIgnoreCase(strChar, strSearchChar)) {
			blnCondition = true;
		}
		return blnCondition;
	}

	/*******************************************************************
	 * Name 		: compareStringCaseSensitive 
	 * Description 	: Used to compare strings with Case sensitive 
	 * Author 		: Rooban 
	 * Date Created : 11/30/2016
	 * Modification Log : 
	 * Date 		Initials 		Description of Modifications
	 * 
	 * @return
	 ********************************************************************/
	public boolean compareStringCaseSensitive(String strChar, String strSearchChar) {
		Boolean blnCondition = false;

		if (strChar.equals(strSearchChar)) {
			blnCondition = true;
		}
		return blnCondition;
	}

	/*******************************************************************
	 * Name 		: compareStringIgnorecase 
	 * Description 	: Used to compare strings with Ignore Case 
	 * Author 		: Rooban 
	 * Date Created : 11/30/2016 
	 * Modification Log : 
	 * Date 		Initials 		Description of Modifications
	 * 
	 * @return
	 ********************************************************************/
	public boolean compareStringIgnorecase(String strChar, String strSearchChar) {
		boolean blnCondition = false;

		if (strChar.equalsIgnoreCase(strSearchChar)) {
			blnCondition = true;
		}
		return blnCondition;
	}

	/*******************************************************************
	 * Name 		: splitDatabyPosition 
	 * Description 	: Used to split and return the data by position
	 * Author 		: Rooban 
	 * Date Created : 11/30/2016 
	 * Modification Log : 
	 * Date 		Initials 		Description of Modifications
	 * 
	 * @return
	 ********************************************************************/
	public String splitDatabyPosition(String strChar, int intStartPosition, int intEndPosition) {
		String strData = null;
		strData = strChar.substring(intStartPosition, intEndPosition);		
		return strData;
	}

	/*******************************************************************
	 * Name 		: splitDatabyDelimiter 
	 * Description 	: Used to split the data by delimiter and return the data as an array 
	 * Author 		: Rooban 
	 * Date Created : 11/30/2016 
	 * Modification Log : 
	 * Date 		Initials 		Description of Modifications
	 * 
	 * @return
	 ********************************************************************/
	public String[] splitDatabyDelimiter(String strChar, String strDelimiter) {
		String[] strData;
		strData = strChar.split(strDelimiter);		
		return strData;
	}	

	/*******************************************************************
	 * Name 		: fReplace 
	 * Description 	: Used to remove special characters from given String 
	 * Author 		: Bhanu Prakash 
	 * Date Created : 05/15/2017
	 * Modification Log : 
	 * Date 		Initials 		Description of Modifications
	 ********************************************************************/
	public String fReplace(String MainString) {
		String pat = "\\W";
		Pattern pattern = Pattern.compile(pat);
		Matcher match = pattern.matcher(MainString);
		return match.replaceAll("").toUpperCase();
	}

	/*******************************************************************
	 * Name 		: waitForTitle(String pageTitle)
	 * Description 	: Wait for the page to load wrt Title
	 * Author 		: Bhanu
	 * Date Created : 
	 * Modification Log : 
	 * Date 		Initials 		Description of Modifications
	 * 
	 * @return
	 ********************************************************************/	
	public void waitForTitle(String pageTitle){
		WebDriverWait webDriverWait  = new WebDriverWait(getCustomWebDriver(), 20000);
		webDriverWait.until(ExpectedConditions.titleContains(pageTitle));
	}

	/*******************************************************************
	 * Name 		: waitForPageLoad 
	 * Description 	: waits till current page is loaded.
	 * Author 		: Bhanu Prakash(bpaub) 
	 * Date Created : 
	 * Modification Log : 
	 * Date			Initials 		Description of Modifications
	 ********************************************************************/
	public void waitForPageLoad(WebDriver driver) {
		Wait<WebDriver> wait = new WebDriverWait(driver, 20);
		wait.until(new Function<WebDriver, Boolean>() {
			public Boolean apply(WebDriver driver) {
				System.out.println("Current Window State : " + String.valueOf(((JavascriptExecutor) driver).executeScript("return document.readyState")));
				return String.valueOf(((JavascriptExecutor) driver).executeScript("return document.readyState")).equals("complete");
			}
		});
	}

	/*******************************************************************
	 * Name 		: failureScreenprint 
	 * Description 	: Used to insert failed step with screenshot in report 
	 * Author 		: Lelson Kumar 
	 * Date Created : 02/21/2017
	 * Modification Log : 
	 * Date 		Initials 		Description of Modifications
	 * @throws Exception 
	 ********************************************************************/	
	@SuppressWarnings("unchecked")
	public void failureScreenprint(Page page) throws Exception {

		SuiteContainer sC = new SuiteContainer();		

		dumpState.executeStep(page, getCustomWebDriver(), contextMap, contextMap, contextMap, sC, getExecutionContext());
	}

	/*******************************************************************
	 * Name 		: createFoldernFile 
	 * Description 	: Used to create Folder along with DateTime stamp
	 * Author 		: Lelson Kumar 
	 * Date Created : 02/21/2017
	 * Modification Log : 
	 * Date 		Initials 		Description of Modifications
	 ********************************************************************/
	public String createFoldernFile() throws Exception
	{
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
		String Datetime = dateFormat.format(now);

		String xFID = getCustomWebDriver().getExecutionContext().getxFID();

		String ScrnPrntPath = KeyWordDriver.instance(xFID).getConfigProperties().getProperty("screenshot.path") + "/" + AlliancePageKeywords.testcase + "_" + Datetime;
		String ScrnPrntName = AlliancePageKeywords.testcase + "_" + AlliancePageKeywords.strTargetTestSystem + "_" + AlliancePageKeywords.testEnvironment + ".docx";
		String path = ScrnPrntPath + "/" + ScrnPrntName;
		File file = new File(ScrnPrntPath);
		file.mkdir();

		return path;
	}

	/*******************************************************************
	 * Name 		: takeScreenShot 
	 * Description 	: Used to take screen shot and save in word document
	 * Author 		: Lelson Kumar 
	 * Date Created : 07/10/2017
	 * Modification Log : 
	 * Date 		Initials 		Description of Modifications
	 * 
	 * @return
	 ********************************************************************/
	public void takeScreenShot(String ClassName)
	{
		try {
			long startTime = System.currentTimeMillis();

			//log.info("Taking screenshot initiated");
			if (ClassName.toUpperCase().contains("IMPERSONATE")) {
				screenShotWordDocPath = createFoldernFile();
				out = new FileOutputStream(new File(screenShotWordDocPath));
				doc = new XWPFDocument();
				p = doc.createParagraph();
				r = p.createRun();
			}

			File scrFile = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);
			r.setBold(true);
			r.setFontSize(12);
			r.setText(ClassName);
			r.addTab();
			r.setText("URL->" + getCustomWebDriver().getCurrentUrl());

			pic = new FileInputStream(scrFile);
			r.addPicture(pic, XWPFDocument.PICTURE_TYPE_PNG, "", Units.toEMU(535), Units.toEMU(400));

			out = new FileOutputStream(screenShotWordDocPath);
			doc.write(out);
			out.flush();
			pic.close();
			out.close();

			//log.info("Screenshot saved successfully");
			String xFID = getCustomWebDriver().getExecutionContext().getxFID();
			if (ClassName.toUpperCase().contains("IMPERSONATE")) {
				String reportDataDetails = "Screenshot has been saved sucessfully to : " + screenShotWordDocPath;

				PageManager.instance(xFID).addExecutionTiming(PageManager.instance(xFID).getExecutionId(getCustomWebDriver()), 
						PageManager.instance(xFID).getDeviceName(getCustomWebDriver()), "", System.currentTimeMillis() - startTime, StepStatus.REPORT, reportDataDetails, 10000);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*******************************************************************
	 * Name 		: exeDetailsReportToDB 
	 * Description 	: Used to insert execution details to Oracle DB 
	 * Author 		: Tashique 
	 * Date Created : 3/19/2017
	 * Modification Log : 
	 * Date 		Initials 		Description of Modifications
	 * 
	 * @return
	 ********************************************************************/
	public void exeDetailsReportToDB(String strTestCaseID, String sCtrlNumber, String strTransactionTimestamp) {
		try {

			CustomLoadDataElements.LoadData(getCustomWebDriver());

			String xFID = getCustomWebDriver().getExecutionContext().getxFID();
			PageData data = PageDataManager.instance(xFID).getPageData("Sheet1", "TC");

			//String str_DES_TEST_RUN_ID = "";
			String str_DES_TEST_CASE_ID = strTestCaseID.toUpperCase().trim();
			String str_DES_TEST_SET_NAME = data.getData("TESTSETNAME").toUpperCase().trim();
			String str_DES_STATE_CD = strTestCaseID.substring(0, 2).toUpperCase();
			String str_DES_LINE_CD = strTestCaseID.substring(4, 6);
			String str_DES_TRAN_TYPE = strTestCaseID.substring(9, 11).toUpperCase();
			String str_DES_BASE_CTRL_NUMBER = "";
			String str_DES_REG_CTRL_NUMBER = sCtrlNumber.trim();
			String str_DES_COMPANY = strTestCaseID.substring(6, 9);
			String str_DES_COMPANY_CD = "";

			switch (str_DES_COMPANY.toUpperCase()) {
			case "APC":
				str_DES_COMPANY_CD = "065";
				break;
			case "AIC":
				str_DES_COMPANY_CD = "010";
				break;
			case "IND":
				str_DES_COMPANY_CD = "060";
				break;
			case "AFC":
				str_DES_COMPANY_CD = "027";
				break;
			case "AFI":
				str_DES_COMPANY_CD = "085";
				break;
			case "FIC":
				str_DES_COMPANY_CD = "070";
				break;
			case "ANJ":
				str_DES_COMPANY_CD = "386";
				break;
			case "ACM":
				str_DES_COMPANY_CD = "068";
				break;
			case "ATL":
				str_DES_COMPANY_CD = "063";
				break;
			case "NIC":
				str_DES_COMPANY_CD = "021";
				break;
			case "AVP":
				str_DES_COMPANY_CD = "064";
				break;
			default:
				System.out.println("CompanyName: " + str_DES_COMPANY + " Not Matching any companyID");
				break;
			}

			String str_DES_RELEASE_DATE = data.getData("EffectiveDate").trim();
			String str_DES_RUN_LEVEL = "R";
			String str_DES_RELEASE_WEEK = data.getData("TestLevel").toUpperCase().trim();
			//String str_DES_BASELINE = "";
			String str_DES_TRANSACTION_TS = strTransactionTimestamp.trim();
			String str_DES_ANALYST_NTID = "";
			String str_DES_TEST_CASE_STATUS = "";
			String str_DES_SOURCE_SERVER = data.getData("TargetTestSystem").toUpperCase().trim();
			String str_DES_SOURCE_ID = data.getData("Databasepath").toUpperCase().trim();
			String str_DES_LAST_UPDATED_BY_USERID = "SYSTEM";
			//String str_DES_LAST_UPDATED_TS = ""; // current timestamp
			String str_DES_BASELINE_TS = "";
			String str_DES_CREATED_BY_USERID = "SYSTEM";
			//String str_DES_CREATED_ON_TS = ""; // current timestamp

			if (str_DES_LINE_CD.length() < 3) {

				str_DES_LINE_CD = "0" + str_DES_LINE_CD;
			}

			str_DES_TRANSACTION_TS = str_DES_TRANSACTION_TS.substring(0, 2) + "/"
					+ str_DES_TRANSACTION_TS.substring(2, 4) + "/" + str_DES_TRANSACTION_TS.substring(4, 6) + " "
					+ str_DES_TRANSACTION_TS.substring(6, 8) + ":" + str_DES_TRANSACTION_TS.substring(8, 10) + ":"
					+ str_DES_TRANSACTION_TS.substring(10, 12);

			String dbURL = "jdbc:oracle:thin:@ldap://oid:389/cn=OracleContext,dc=allstate,dc=com/XASOR100";
			Class.forName("oracle.jdbc.driver.OracleDriver");
			String strUserName = "XAUSRRPTR";
			String strPwd = "rpt2015R";
			conn1 = DriverManager.getConnection(dbURL, strUserName, strPwd);

			if (conn1.isValid(20)) {

				String strSelectQuery = "SELECT COUNT(*) FROM XAT_DC_EXECUTION_SUMMARY " + "WHERE DES_TEST_CASE_ID = '"
						+ str_DES_TEST_CASE_ID + "' and DES_RELEASE_DATE = TO_DATE('" + str_DES_RELEASE_DATE
						+ "', 'mm/dd,yyyy') and DES_RELEASE_WEEK = '" + str_DES_RELEASE_WEEK + "' "
						+ "GROUP BY DES_TEST_CASE_ID,DES_RELEASE_DATE,DES_RELEASE_WEEK " + "HAVING COUNT(*)>=1";

				Statement sta = conn1.createStatement();
				sta.executeQuery(strSelectQuery);
				ResultSet result = sta.getResultSet();

				if (!result.next()) {
					str_DES_TEST_CASE_STATUS = "NE"; // If no records found then testcase status as New "NE"
				} else {
					str_DES_TEST_CASE_STATUS = "DU"; // 'If records found then testcase status as Duplicate "DU"
				}

				// Insert all records into database
				String strQuerry = "INSERT INTO XAT_DC_EXECUTION_SUMMARY "
						+ "(DES_TEST_RUN_ID, DES_TEST_CASE_ID,DES_TEST_SET_NAME,DES_STATE_CD ,DES_LINE_CD,DES_TRAN_TYPE,"
						+ "DES_BASE_CTRL_NUMBER,DES_REG_CTRL_NUMBER,DES_RELEASE_DATE,DES_RUN_LEVEL,DES_RELEASE_WEEK,DES_BASELINE,DES_TRANSACTION_TS,"
						+ "DES_ANALYST_NTID,DES_TEST_CASE_STATUS,DES_SOURCE_SERVER,DES_SOURCE_ID,DES_LAST_UPDATED_BY_USERID,DES_LAST_UPDATED_TS,DES_BASELINE_TS,DES_COMPANY_CD,DES_CREATED_BY_USERID,DES_CREATED_ON_TS) "
						+ "VALUES ((XAQ_TEST_RUN_ID_SEQ.NEXTVAL),'" + str_DES_TEST_CASE_ID + "','"
						+ str_DES_TEST_SET_NAME + "','" + str_DES_STATE_CD + "', '" + str_DES_LINE_CD + "', '"
						+ str_DES_TRAN_TYPE + "', '" + str_DES_BASE_CTRL_NUMBER + "','" + str_DES_REG_CTRL_NUMBER
						+ "',TO_DATE('" + str_DES_RELEASE_DATE + "', 'mm/dd,yyyy'),'" + str_DES_RUN_LEVEL + "', '"
						+ str_DES_RELEASE_WEEK + "' ,'',TO_TIMESTAMP('" + str_DES_TRANSACTION_TS
						+ "', 'YY/MM/DD HH24:MI:SS'), '" + str_DES_ANALYST_NTID + "','" + str_DES_TEST_CASE_STATUS
						+ "', '" + str_DES_SOURCE_SERVER + "','" + str_DES_SOURCE_ID + "','"
						+ str_DES_LAST_UPDATED_BY_USERID + "',current_timestamp,'" + str_DES_BASELINE_TS + "','"
						+ str_DES_COMPANY_CD + "','" + str_DES_CREATED_BY_USERID + "',current_timestamp)";

				sta.executeQuery(strQuerry); 
			}
			else {
				System.out.println("DB connection is failed");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn1 != null) {
				try {
					conn1.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/***********************************************************************************
	 *	Name              :	fDateDiffYears(String firstDate, String lastDate)
	 *	Description       : Return difference between two dates in years
	 *	PARAMETERS		  : firstDate in String format - "MM/DD/YYYY" (09/20/1985 - DOB)
	 *						lastDate in String format - "MM/DD/YYYY" (09/20/2017 - Effective Date)
	 *						e.g. fDateDiffYears("09/20/1985", "09/20/2017")
	 *	Author            : Rooban
	 *	Date Created      :	09/20/2017
	 *	Modification Log  :                                                     
	 *	Date		Initials     	Description of Modifications 
	 * @return Years Difference in Integer format
	 * @throws ParseException 
	 *
	 ************************************************************************************/
	public int fDateDiffYears(String firstDate, String lastDate) throws ParseException {		

		DateFormat df = new SimpleDateFormat("MM/dd/yyyy"); 

		Date startDate = df.parse(firstDate);
		Date EndDate = df.parse(lastDate);	

		Calendar calSD = Calendar.getInstance();
		Calendar calED = Calendar.getInstance();

		calSD.setTime(startDate);
		calED.setTime(EndDate);

		int intYearsDiff =  calED.get(Calendar.YEAR) - calSD.get(Calendar.YEAR);
		if (calSD.get(Calendar.MONTH) > calED.get(Calendar.MONTH) || 
				(calSD.get(Calendar.MONTH) == calED.get(Calendar.MONTH) && calSD.get(Calendar.DATE) > calED.get(Calendar.DATE))) {
			intYearsDiff--;
		}
		return intYearsDiff;
	}

	/*******************************************************************
	 *	Name              :	getDbColumnName
	 *	Description       : Used to filter Db column name 
	 *	Author            : Pallavi Sharma
	 *	Date Created      :	10/24/2017
	 *	Modification Log  :                                                     
	 *	Date		Initials     	Description of Modifications 
	 * @return 
	 ********************************************************************/

	public String[] getDbColumnName(PageData dbRowData ) throws Exception {
		String[] fieldname = dbRowData.getFieldNames();
		ArrayList<String> list = new ArrayList<String>(Arrays.asList(fieldname));
		list.remove("recordName");
		list.remove("typeName");
		list.remove("active");
		return fieldname = list.toArray(new String[0]);
	}

	/***********************************************************************************
	 *	Name              :	policyReorder()
	 *	Description       : used to fetch userNTID and agentNumber from policyreorder table for  policy reorder process								
	 *	Author            : Lelson Kumar
	 *	Date Created      :	11/03/2017
	 *	Modification Log  :                                                     
	 *	Date		Initials     	Description of Modifications 
	 * @return
	 ************************************************************************************/
	public String policyReorder(){

		String url="";
		String outputData="";
		Connection conn=null;
		Statement stmt  = null;
		ResultSet PolicyReorderinfo = null;

		try{
			switch(AlliancePageKeywords.strPolicyReorderDB){
			case "RR":
				url="jdbc:sqlserver://p4sst006:1433;databaseName=P4DQCTEST;integratedSecurity=true"; 
				conn=DriverManager.getConnection(url);
				break;

			case "RTD":
				url= "jdbc:sqlserver://p4sst006:1433;databaseName=P4DAllianceQCTest;integratedSecurity=true";
				conn=DriverManager.getConnection(url);
				break;
			}	
			String sqlQuery= "select * from tbl_PolicyReorder where Role="+"'"+AlliancePageKeywords.strRole+"'";
			stmt = conn.createStatement();
			PolicyReorderinfo=stmt.executeQuery(sqlQuery);

			while (PolicyReorderinfo.next()) {
				outputData=PolicyReorderinfo.getObject("UserNTID").toString()+"/"+PolicyReorderinfo.getObject("AgentNumber").toString();
			}
			conn.close();
		}
		catch(Exception e){
			log.info("Failed to connect "+AlliancePageKeywords.strPolicyReorderDB+" DataBase");
			e.printStackTrace();
		}
		return outputData;
	}

	/***********************************************************************************
	 *	Name              :	fZeroFill()
	 *	Description       : used to prefix Zero as per given parameter								
	 *	Author            : Vikash
	 *	Date Created      :	11/03/2017
	 *	Modification Log  :                                                     
	 *	Date		Initials     	Description of Modifications 
	 * @return
	 ************************************************************************************/
	public String fZeroFill(String val , int length){
		while (val.length() < length){
			val = 0 + val;
		}
		return val;
	}

	/*******************************************************************
	 * Name 		: fZip(Integer zipCode)
	 * Description 	: prefix 0 if Zip code is less then 5 digits
	 * Author 		: Vikash
	 * Date Created : 
	 * Modification Log : 
	 * Date 		Initials 		Description of Modifications
	 * 
	 * @return zipCode
	 ********************************************************************/	
	public String fZip(String zipCode){
		if (!zipCode.isEmpty()){
			int lZipCode= zipCode.toString().length();
			while(lZipCode <5) {
				zipCode = 0 + zipCode ;
				lZipCode++;
			}
		}
		return zipCode;
	}

	/*******************************************************************
	 *	Name              :	getRecords
	 *	Description       : Get all the records from the table corresponding to the test case 
	 *	PARAMETERS		  : 
	 *	Author            : Rooban
	 *	Date Created      :	08/31/2017
	 *	Modification Log  :                                                     
	 *	Date		Initials     	Description of Modifications 
	 *
	 ********************************************************************/
	protected PageData[] getRecords(String tableName)
	{		
		String xFID = getCustomWebDriver().getExecutionContext().getxFID();
		PageData[] pageData = PageDataManager.instance(xFID).getRecords(tableName);
		return pageData;
	}

	/*******************************************************************
	 *	Name              :	removeDuplicates
	 *	Description       : Remove duplicate records from the queried record ser 
	 *	PARAMETERS		  : 
	 *	Author            : Rooban
	 *	Date Created      :	08/31/2017
	 *	Modification Log  :                                                     
	 *	Date		Initials     	Description of Modifications 
	 *
	 ********************************************************************/
	protected PageData[] removeDuplicates(PageData[] pageData)
	{
		int noElements = pageData.length;
		boolean flag=false;
		PageData[] pageDatarLocal=new PageData[pageData.length];
		int k=0;     
		for (int i= 0; i<noElements;i++)
		{
			flag = false;
			for (int j= 0; j<k;j++)
			{
				if(pageData[i].getName()==pageDatarLocal[j].getName()){
					flag=true;
				}
			}

			if(flag==false)
			{
				pageDatarLocal[k] = pageData[i];
				k++;
			}
		}      
		return (PageData[]) Arrays.copyOfRange(pageDatarLocal, 0, k);
	}

	/*******************************************************************
	 *	Name              :	getFilteredRecords
	 *	Description       : Get filtered record set based on a specific column condition
	 *	PARAMETERS		  : recordSet - Record set needs to be filtered based on condition
	 *						filterName - DB column name
	 *						filterValue - DB column value (ex. driver number, sort number)
	 *	Author            : Rooban
	 *	Date Created      :	08/31/2017
	 *	Modification Log  :                                                     
	 *	Date		Initials     	Description of Modifications 
	 *
	 ********************************************************************/
	public PageData[] getFilteredRecords(PageData[] recordSet, String filterName, String filterValue)
	{
		PageData[] pageData = new PageData[recordSet.length];
		int j = 0;

		for (int i = 0; i < recordSet.length; i++) {
			if (recordSet[i].getData(filterName).equals(filterValue)){
				pageData[j] = recordSet[i];
				j++;
			}
		}
		pageData = (PageData[]) Arrays.copyOfRange(pageData, 0, j);		
		return pageData;
	}

	/*******************************************************************
	 *	Name              :	takeHTMLScreenshot
	 *	Description       : Take Screenshot and attach with the HTML Report
	 *	PARAMETERS		  : 
	 *	Author            : Rooban
	 *	Date Created      :	02/01/2018
	 *	Modification Log  :                                                     
	 *	Date		Initials     	Description of Modifications 
	 *
	 ********************************************************************/
	public String takeHTMLScreenshot(DeviceWebDriver deviceWebDriver){		
		return KeyWordDriver.instance(deviceWebDriver.getExecutionContext().getxFID()).getConfigProperties().getProperty("screenShot.HTMLReport");
	}

	@Override
	public void initializePage() {

	}

}