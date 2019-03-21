/*******************************************************************
 *	Name              :	CustomReporting
 *	Description       : Class to load custom reporting methods
 *	Author            : Rooban
 *	Date Created      :	
 *	Modification Log  :                                                    
 *	Date		Initials     	Description of Modifications 
 *  
 ********************************************************************/
package com.alliance.functions.reporting;

import java.util.List;
import java.util.ArrayList;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.xframium.device.factory.DeviceWebDriver;
import org.xframium.exception.ScriptException;
import org.xframium.exception.XFramiumException;
import org.xframium.page.StepStatus;
import org.xframium.page.element.Element;
import org.xframium.page.keyWord.KeyWordParameter;
import org.xframium.page.keyWord.KeyWordStep;
import org.xframium.page.keyWord.KeyWordParameter.ParameterType;
import org.xframium.page.keyWord.KeyWordStep.StepFailure;
import org.xframium.page.keyWord.step.KeyWordStepFactory;
import org.xframium.page.keyWord.step.SyntheticStep;

import com.alliance.functions.settings.AbstractAllianceTest;

public class CustomReporting extends AbstractAllianceTest {

	private static CustomReporting singleton=new CustomReporting();

	public static CustomReporting instance()
	{
		return singleton;
	}

	private CustomReporting(){}

	//Report custom start step using elements
	public void startStep(String type, Element element, String[] args)
	{
		KeyWordStep step = KeyWordStepFactory.instance().createStep( element.getName(), element.getPageName(), true, type, "", false, StepFailure.IGNORE, false, "", "", "", 0, "", 0, "", "", "", null, "", false, false, "", "", null, "" );

		if(args!=null)
		{
			for(String value: args)
			{
				step.addParameter( new KeyWordParameter( ParameterType.STATIC, value, "", "" ) );			
			}
		}

		element.getExecutionContext().startStep( step, null, null );
	}

	//Report synthetic start step using elements
	public void startSyntheticStep(String type, Element element, String[] args)
	{
		String message = null;
		if ( type != null )
		{
			try
			{
				String name = element.getName();
				String pageName = element.getPageName();
				List<Object> arrayList = new ArrayList<Object>();
				arrayList.add(name);
				arrayList.add(pageName);
				if(args!=null)
					for(String value: args)
					{
						arrayList.add(value);			
					}

				message = CustomOutputFormatter.instance().getFormattedMessage(type.trim().toLowerCase());
				if ( message != null )
					message = String.format( message, arrayList.toArray() );
				else
					message =type;

				element.getExecutionContext().startStep( new SyntheticStep( element.getName(),element.getPageName(), message),null, null ); 
			}
			catch ( Exception ex )
			{
				log.error( "Error formatting message", new ScriptException(ex.getMessage()) );
				startSyntheticStep(StepStatus.FAILURE,"Error in genarating Report statement",element.getWebDriver(),new ScriptException(ex.getMessage()));               
			}
		}
	}

	//Report synthetic start step using driver
	public void startSyntheticStep(String type, DeviceWebDriver driver, String[] args)
	{
		driver.getExecutionContext().startStep( new SyntheticStep("Step","Step", type),null, null ); 
	}

	//Reporting Exception/Failure
	public void startSyntheticStep(Exception ex,DeviceWebDriver driver)
	{
		startSyntheticStep(StepStatus.FAILURE,"Click link for more information",driver,ex);		
	}

	//Reporting Message/Report
	public void startSyntheticStep(String message,DeviceWebDriver driver)
	{
		startSyntheticStep(StepStatus.REPORT,message,driver,null);		
	}

	//Report synthetic start step using driver and complete step
	public void startSyntheticStep(StepStatus status,String message, DeviceWebDriver driver,Exception ex)
	{
		driver.getExecutionContext().startStep( new SyntheticStep("Step","Step", message),null, null ); 
		driver.getExecutionContext().completeStep(status, ex);
	}

	//Report custom complete step using elements
	public void completeStep(StepStatus status, Element element, Exception ex)
	{
		completeStep(status,element.getWebDriver(),ex);
	}

	//Report custom complete step using driver
	public void completeStep(StepStatus status, DeviceWebDriver driver, Exception ex)
	{
		if(ExceptionUtils.indexOfType(ex, XFramiumException.class)==-1 && ex!=null)
			driver.getExecutionContext().completeStep(status, new ScriptException(ex.getMessage()));
		else
			driver.getExecutionContext().completeStep(status, ex);
	}
	
	public void screenshot(DeviceWebDriver driver) throws Exception{
		dumpState(driver);
	}
	
	public void screenshot(DeviceWebDriver driver, String strScreenshot) throws Exception{
		if (strScreenshot.equalsIgnoreCase("YES")) {	
			dumpState(driver);
		}
	}
	
	public void screenshot(DeviceWebDriver driver, String strScreenshot, String pageName) throws Exception{
		if (strScreenshot.equalsIgnoreCase("YES")) {
			dumpState(driver,pageName,2,5);
		}
	}
}