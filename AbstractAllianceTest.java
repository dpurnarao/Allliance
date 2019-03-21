/*******************************************************************
 *	Name              :	AbstractAllianceTest
 *	Description       : New class which extends AbstractJavaTest, This class should be extended by all the local Tests 	
 *	Author            : 
 *	Date Created      :	12/12/2017
 *	Modification Log  :                                                     
 *	Date		Initials     	Description of Modifications 
 ********************************************************************
 ********************************************************************/
package com.alliance.functions.settings;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.testng.ITestContext;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.xframium.Initializable;
import org.xframium.device.DeviceManager;
import org.xframium.device.factory.DeviceWebDriver;
import org.xframium.device.ng.AbstractJavaTest;
import org.xframium.device.ng.TestContainer;
import org.xframium.device.ng.TestName;
import org.xframium.device.ng.TestPackage;
import org.xframium.driver.ConfigurationReader;
import org.xframium.driver.XMLConfigurationReader;
import org.xframium.exception.XFramiumException;
import org.xframium.page.PageManager;
import org.xframium.page.StepStatus;
import org.xframium.page.keyWord.KeyWordDriver;
import org.xframium.spi.Device;

import com.alliance.functions.reporting.CustomOutputFormatter;
import com.alliance.functions.reporting.CustomReporting;
import com.alliance.functions.utility.TestMatrixGenarator;

public class AbstractAllianceTest  extends AbstractJavaTest {

	private static ConfigurationReader cR = null;
	protected ThreadLocal<String> tcID = new ThreadLocal<String>();

	/*******************************************************************
	 *	Name              :	getWebDriver
	 *	Description       : Used to get the driver object
	 *	Author            :   
	 *	Parameters		  : 
	 *	Date Created      :	12/12/2017
	 *	Modification Log  :                                                     
	 *	Date		Initials     	Description of Modifications 
	 ********************************************************************/	
	public DeviceWebDriver getWebDriver()
	{		
		TestPackage testPackage = testPackageContainer.get(); 
		DeviceWebDriver driver = testPackage.getConnectedDevice().getWebDriver(); 
		String xFID = testPackage.getxFID();  
		driver.getExecutionContext().setxFID(xFID); 
		PageManager.instance(xFID).getPageCache().clear();
		return driver;
	}

	/**************** Test Case Name Reading and storing *******************/		

	protected String getTestName()
	{					
		//Return Test Cases
		TestPackage testPackage = testPackageContainer.get(); 
		tcID.set(testPackage.getTestName().getTestName());
		String dataSetName = testPackage.getTestName().getPersonaName();
		return(dataSetName);
	}	

	/************************ @DataProvider*********************************/	
	/*******************************************************************
	 *	Name              :	customDeviceManager
	 *	Description       : Used run Test Case on multiple data Set
	 *	Author            :   
	 *	Date Created      :	12/12/2017
	 *	Modification Log  :                                                     
	 *	Date		Initials     	Description of Modifications 
	 ********************************************************************/
	@DataProvider (name = "DeviceManager", parallel = true)
	public Object[][] getCustomDeviceData(Method currentMethod, ITestContext testContext)
	{
		String xFID;
		if (Initializable.xFID == null || Initializable.xFID.get() == null)
			xFID = (String) testContext.getAttribute( "xFID" );
		else
			xFID = Initializable.xFID.get();

		List<Device> deviceList = DeviceManager.instance(xFID).getDevices();		
		Object[][] testContainer = getDeviceData(deviceList, currentMethod, testContext, xFID);

		return testContainer;
	}

	/*******************************************************************
	 *	Name              :	getDeviceData
	 *	Description       : Override getDeviceData to include current method in test container
	 *	Author            :   
	 *	Date Created      :	12/12/2017
	 *	Modification Log  :                                                     
	 *	Date		Initials     	Description of Modifications 
	 ********************************************************************/

	protected Object[][] getDeviceData( List<Device> deviceList, Method currentMethod,ITestContext testContext, String xFID )
	{      
		List<String> dataSet =  TestMatrixGenarator.instance().getTestList(currentMethod.getName());

		TestName[] newArray = null;
		newArray = new TestName[dataSet.size() * deviceList.size()];
		for ( int i = 0; i < dataSet.size(); ++i )
		{
			for ( int j = 0; j < deviceList.size(); j++ )
			{
				newArray[i * deviceList.size() + j] = new TestName( currentMethod.getName());
				newArray[i * deviceList.size() + j].setPersonaName(dataSet.get(i));
			}
		}

		List<Device> fullDeviceList = new ArrayList<Device>( 10 );
		for ( Device d : deviceList )
		{
			for ( int i = 0; i < d.getAvailableDevices(); i++ )
				fullDeviceList.add( d.cloneDevice() );
		}

		StringBuilder logOut = new StringBuilder();
		logOut.append( "\r\n*********************************************************************\r\nPreparing to execute\r\n" );
		logOut.append( "\r\nAgainst the following " ).append(  deviceList.size() ).append( " devices\r\n" );
		for ( Device d : deviceList )
		{
			logOut.append( "\t" + d.getEnvironment() + "\r\n" );
		}

		try
		{
			testContext.getSuite().getXmlSuite().setDataProviderThreadCount( fullDeviceList.size() );
			log.warn( "Thread count configured as " + fullDeviceList.size() );
		}
		catch( Exception e )
		{
			System.setProperty( "dataproviderthreadcount", fullDeviceList.size() + "" );
			log.warn( "Thread count configured as " + fullDeviceList.size() + " via system property" );
		}

		Object[][] returnArray = new Object[newArray.length][1];
		for ( int i = 0; i < returnArray.length; i++ )
		{
			TestName[] temp = new TestName[1];
			temp[0] = newArray[i];
			TestContainer testContainer = new TestContainer( temp, fullDeviceList.toArray( new Device[0] ), xFID );
			returnArray[i][0] = testContainer;
		}

		log.warn( logOut.toString() );

		return returnArray;
	}

	/************************BeforeSuite*******************************************/

	@BeforeSuite
	public void setupSuite(ITestContext testContext)
	{

		String xFID = UUID.randomUUID().toString();
		Initializable.xFID.set( xFID ); 
		testContext.setAttribute("xFID", xFID);
		Map<String,String> customConfig = new HashMap<String,String>(5);
		customConfig.put( "xF-ID", Initializable.xFID.get() );

		File configurationFile; 
		String configFileName= System.getProperty("ConfigFile"); 
		System.out.println("FileName is:  "+ configFileName); 
		if(configFileName==null ||configFileName.equalsIgnoreCase("")) 
			configurationFile = new File("resources\\driverConfig.txt"); 
		else 
			configurationFile = new File(configFileName);  

		if ( configurationFile.getName().toLowerCase().endsWith( ".xml" ) )
			cR = new XMLConfigurationReader();
		else if ( configurationFile.getName().toLowerCase().endsWith( ".txt" ) )
			cR = new CustomTXTConfigurationReader();
		cR.readConfiguration( configurationFile, false,customConfig );	

		try {	
			String testsetPath = KeyWordDriver.instance(xFID).getConfigProperties().getProperty("testset.path");
			TestMatrixGenarator.instance().genarateTestMatrixList(testsetPath);			
		} catch (Exception e) {
			e.printStackTrace();
		}

		CustomOutputFormatter.instance().loadCustomFormats();
	}

	/*************************AfterSuite*******************************************/
	@AfterSuite
	public void cleanupSuite()
	{
		cR.afterSuite();
	}
	/*************************End AfterSuite***************************************/
}
