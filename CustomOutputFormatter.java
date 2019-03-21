/*******************************************************************
 *	Name              :	CustomOutputFormatter
 *	Description       : Class to format output message
 *	Author            : Rooban
 *	Date Created      :	
 *	Modification Log  :                                                    
 *	Date		Initials     	Description of Modifications 
 *  
 ********************************************************************/
package com.alliance.functions.reporting;

import java.util.Properties;

public class CustomOutputFormatter {

	private static CustomOutputFormatter singleton = new CustomOutputFormatter();
	private Properties tempProperties;
	private static Properties outputFormatter = new Properties();

	private CustomOutputFormatter(){}

	public static CustomOutputFormatter instance()
	{
		return singleton;
	}

	public void loadCustomFormats()
	{
		tempProperties = new Properties();
		try
		{
			tempProperties.load(getClass().getResourceAsStream("additionalOutputFormatter.properties"));
			for(String strKey: tempProperties.stringPropertyNames())
			{
				outputFormatter.put(strKey.trim().toLowerCase(), tempProperties.getProperty(strKey));
			}
		}
		catch ( Exception e )
		{
			e.printStackTrace();
		}
	}

	public String getFormattedMessage( String name )
	{
		return outputFormatter.getProperty( name );
	}
}
