package com.bluedigm.imca;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

public abstract class ImcaService {

	protected Logger log = LoggerFactory.getLogger(getClass());

	@Value("${webdriver.id}")
	private String webDriverId = "webdriver.chrome.driver";

	@Value("${webdriver.path}")
	private String webDriverPath = "C:/work/project/now-nms/chromedriver_win32/chromedriver.exe";

	public ImcaService() {
		init();
	}

	public void init() {
		System.setProperty(webDriverId, webDriverPath);
	}

	protected WebDriver driver() {
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--disable-extensions");
		options.setCapability("ignoreProtectedModeSettings", true);
		options.setExperimentalOption("excludeSwitches", new String[] { "enable-automation", "load-extension" });
		Map<String, Object> prefs = new HashMap<String, Object>();
		prefs.put("credentials_enable_service", false);
		prefs.put("profile.password_manager_enabled", false);
		options.setExperimentalOption("prefs", prefs);

		WebDriver driver = new ChromeDriver(options);

		return driver;
	}

	protected static void sleep() {
		sleep(100L);
	}

	protected static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			// ignore
		}
	}

	protected void close(WebDriver driver) {
		driver.close();
	}

}
