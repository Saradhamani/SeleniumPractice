package seleniumScripts;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AjioTC{
	public static  ChromeDriver driver;
	public static ChromeOptions options;
	
	public static void main(String[] args) throws InterruptedException {
		System.setProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");
		options= new ChromeOptions();
		options.addArguments("--disable-notifications");
		driver= new ChromeDriver(options);
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		driver.get("https://www.ajio.com/shop/sale");
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		WebDriverWait wait = new WebDriverWait(driver, 50);
		//click allow location to close the alert box 
		//driver.findElementByClassName("locale__submit").click();
		//Enter Bags in the Search field and Select Bags in Women Handbags
		driver.findElementByName("searchVal").sendKeys("Bags",Keys.ARROW_DOWN);
		WebElement autoSuggestionList = driver.findElementByClassName("rilrtl-list");
		List<WebElement>sugg=autoSuggestionList.findElements(By.tagName("li"));
		for (WebElement ele : sugg) {
			if(ele.findElement(By.xpath("a/span")).getText().equalsIgnoreCase("Bags In")) {
				if(ele.findElement(By.xpath("a/span[2]")).getText().equals("Women Handbags")) {
					ele.findElement(By.tagName("a")).click();
					break;
				}
			}
		} 
		
		//click the five grid per row 
		driver.findElementByClassName("five-grid-container").click();
		//click sortby
		//driver.findElementByClassName("sortby-filter").click();
		WebElement srotByEle = driver.findElementByXPath("//div[@class='filter-dropdown']/select");
		Select sortByDD= new Select(srotByEle);
		sortByDD.selectByValue("newn");
		//Enter Price Range Min as 2500 and Max as 5000
		//click Price Option
		Thread.sleep(2000);
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElementByXPath("//span[text()='price']")));
		driver.findElementByXPath("//span[text()='price']").click();
		driver.findElementById("minPrice").sendKeys("2500");
		driver.findElementById("maxPrice").sendKeys("5000",Keys.ENTER);
		//driver.findElementByClassName("rilrtl-button ic-toparw").click();
		driver.executeScript("window.scrollBy(0,150)");
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='brand' and text()='TOMMY HILFIGER']/following-sibling::div[@class='name' and text()='Sling Bag with Chain Strap']")));
		WebElement ele=driver.findElementByXPath("//div[@class='brand' and text()='TOMMY HILFIGER']/following-sibling::div[@class='name' and text()='Sling Bag with Chain Strap']");
		Actions builder = new Actions(driver);
		builder.moveToElement(ele).click().perform();
		ele.click();
		List<String> windowHandles = new LinkedList<String>(driver.getWindowHandles());
		driver.switchTo().window(windowHandles.get(1));
		int prodPrice=Integer.parseInt(driver.findElementByClassName("prod-sp").getText().replaceAll("[^0-9]",""));
		String promo= driver.findElementByClassName("promo-desc").getText();
		int index=promo.indexOf("on");
		int minReqPromoPrice=Integer.parseInt(promo.substring(index+3,promo.indexOf(" ",index+3)));
		int priceAfterDiscount=0;
		String CouponCode = new String();
		
		if(prodPrice>minReqPromoPrice) {
			System.out.println("Discount is applicable");
			 priceAfterDiscount=Integer.parseInt(driver.findElementByXPath("//div[@class='promo-discounted-price']/span").getText().replaceAll("[^0-9]",""));
		 CouponCode=driver.findElementByClassName("promo-title").getText().replaceAll("Use Code","").trim();
			System.out.println("Coupoun Code is :"+ CouponCode+" Price After Discount:" + priceAfterDiscount);
			
			
		
		}else System.out.println("Discount not applicable");
		
		//check if its available for the pincode 560043
		driver.findElementByXPath("//span[contains(text(),'Enter Pin-code To Know Estimated Delivery Date')]").click();
		driver.findElementByName("pincode").sendKeys("560043");
		driver.findElementByClassName("edd-pincode-modal-submit-btn").click();
		Thread.sleep(1000);
		String delDate = driver.findElementByXPath("//span[@class='edd-message-success-details-highlighted']").getText();
		System.out.println("The item will be delivered on :"+delDate);
		driver.findElementByClassName("ic-pdp-add-cart").click();//add to Bag
		Thread.sleep(5000);
		//print customer care address, phone and email
		driver.findElementByClassName("other-info-toggle").click();
		String custCareDetails = driver.findElementByXPath("//span[text()='Customer Care Address']/following-sibling::span[2]").getText();
		System.out.println("Customer Care Details : "+custCareDetails);
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()='GO TO BAG']")));
		WebElement gotobag=driver.findElementByClassName("btn-cart");
		builder.moveToElement(gotobag).click().perform();
		Thread.sleep(3000);
		driver.executeScript("window.scrollBy(0,-150)");
		driver.findElementByClassName("btn-cart").click();//go to bag
		Thread.sleep(3000);
			//List<String>winHan= new LinkedList<String>(driver.getWindowHandles());
			//driver.switchTo().window(winHan.get(1));
		int orderTotal=Integer.parseInt(driver.findElementByXPath("//span[@class='price-value bold-font']").getText().replaceAll("[^0-9]", ""));
		if(prodPrice==orderTotal)
		{
			System.out.println("The product price is found to be correct");
		}
		driver.findElementById("couponCodeInput").sendKeys(CouponCode,Keys.ENTER);
		driver.findElementByXPath("//button[@class='rilrtl-button button apply-button ']").click();
		Thread.sleep(5000);
		
		int afterCoupon=(int) Float.parseFloat(driver.findElementByXPath("//div[@class='net-price best-price-strip']").getText().replaceAll("Rs.", "").replaceAll("[^0-9.]", ""));
		if(afterCoupon==priceAfterDiscount) System.out.println("Discount applied properly");
		System.out.println("After Coupon: " + afterCoupon + " Price after discount :"+ priceAfterDiscount);
		//delete it from bag
		Thread.sleep(2000);
		driver.findElementByClassName("delete-btn").click();
		driver.quit();
	}

}
