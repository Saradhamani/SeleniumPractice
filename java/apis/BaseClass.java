package apis;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

public class BaseClass {
	protected RemoteWebDriver driver;
	ChromeOptions options;
	public BaseClass() {
		options= new ChromeOptions();
		options.addArguments("--disable-notifications");
		driver= new ChromeDriver(options);
	}

}
