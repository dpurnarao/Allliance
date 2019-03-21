package com.alliance.test;

import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;

import com.sun.org.apache.bcel.internal.generic.GOTO;

public class Testing {

	public static void main(String[] args) throws InterruptedException {
		
		
		System.setProperty("webdriver.ie.driver", "C://Selenium//IEDriverServer.exe");
        WebDriver driver = new InternetExplorerDriver();
        driver.get("http://www.naukri.com/");
		
		String currentWindow = driver.getWindowHandle();
		Set<String> allWindows = driver.getWindowHandles(); 
		for(String windowHandle  : allWindows) {
			if(!windowHandle.equals(currentWindow)) {
				driver.switchTo().window(windowHandle);
				driver.close();
			}
		}
        
		/* System.setProperty("webdriver.ie.driver", "C://Selenium//IEDriverServer.exe");
        WebDriver driver = new InternetExplorerDriver();
        
        driver.get("C:\\Selenium\\Additional Interested Party.htm");*/
		
        /*driver.findElement(By.name("occupantInfo[5].firstName")).sendKeys("rooban");
        driver.findElement(By.name("occupantInfo[5].lastName")).sendKeys("suresh");
        driver.findElement(By.name("occupantInfo[5].gender")).sendKeys("Male");
        driver.findElement(By.name("occupantInfo[5].relationshipToPrimary")).sendKeys("Child/Parent");
        driver.findElement(By.name("occupantInfo[5].dobMM")).sendKeys("01");
        driver.findElement(By.name("occupantInfo[5].dobDD")).sendKeys("11");
        driver.findElement(By.name("occupantInfo[5].dobYYYY")).sendKeys("1987");
        driver.findElement(By.name("occupantInfo[5].dobYYYY")).sendKeys(Keys.TAB);
        
        //driver.findElement(By.name("occupantInfo[5].nonDriverReasonCd")).sendKeys("Other");
        driver.findElement(By.name("occupantInfo[5].nonDriverReasonCd")).sendKeys("Has Own Car and Insurance");*/
        
        /*List<WebElement> ele = driver.findElements(By.xpath("//select[@name = 'displayInterest.type']"));
        
        System.out.println(ele.size());
        
       for (WebElement webElement : ele) {
    	   webElement.sendKeys("Lienholder");
		
    	   if (webElement.isDisplayed()) {
    		   
    		   webElement.sendKeys("testing");;
    		   
    		   String abc = webElement.getAttribute("id");
    		   String def = webElement.getAttribute("name");
    		   String xyz = webElement.getTagName();
		
    	   }
       }*/
       
      
      /* driver.findElement(By.name("occupantInfo[5].excludeReasonNarrative2")).sendKeys("test1");
       driver.findElement(By.name("occupantInfo[5].excludeReasonNarrative")).sendKeys("test2");*/
	}

}
