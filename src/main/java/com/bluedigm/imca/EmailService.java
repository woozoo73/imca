package com.bluedigm.imca;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class EmailService extends ImcaService {

	public EmailSetting getEmailSetting(Cpe cpe) {
		WebDriver driver = driver();

		try {
			login(driver, cpe, 5);
			moveToConsole(driver);
			clickCpeMenu(driver);
			return getCpeEmailSetting(driver);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new RuntimeException(e);
		} finally {
			close(driver);
		}
	}

	public void changeEmailSetting(Cpe cpe, EmailSetting emailSetting) {
		WebDriver driver = driver();

		log.info("driver: {}", driver);

		try {
			login(driver, cpe, 5);
			moveToConsole(driver);
			clickCpeMenu(driver);
			changeCpeEmailSetting(driver, emailSetting);
			reloadCpeMenu(driver);
			verifyCpeEmailSetting(driver, emailSetting);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new RuntimeException(e);
		} finally {
			close(driver);
		}
	}

	private void login(WebDriver driver, Cpe cpe, int tryCount) {
		int i = 0;
		while (i < tryCount) {
			try {
				login(driver, cpe);

				return;
			} catch (Exception e) {
				log.warn(e.getMessage(), e);

				sleep(1_000L);
			}
			i++;
		}

		throw new RuntimeException("login failed: tryCount=" + tryCount);
	}

	private void login(WebDriver driver, Cpe cpe) {
		JavascriptExecutor js = (JavascriptExecutor) driver;

		driver.get(cpe.getUrl() + "/sess-bin/login_session.cgi");

		WebElement username = driver.findElement(By.name("username"));
		username.sendKeys(cpe.getUsername());

		sleep();

		WebElement passwd = driver.findElement(By.name("passwd"));
		passwd.sendKeys(cpe.getPassword());

		sleep();

		String script = "";
		script += "var __c = document.createElement('canvas');";
		script += "var __ctx = __c.getContext('2d');";
		script += "var __img = document.getElementById('iframe_captcha').contentWindow.document.getElementsByTagName('img')[0];";
		script += "__ctx.drawImage(__img, 0, 0, 200, 70, 0, 0, 200, 70);";
		script += "var __t = document.createElement('input');";
		script += "__t.setAttribute('id', 'captchaData');";
		script += "__t.setAttribute('type', 'hidden');";
		script += "__t.value = __c.toDataURL();";
		script += "return __t.value;";

		String captcha = (String) js.executeScript(script);

		log.info("captcha: {}", captcha);

		String decaptcha = CaptchaUtils.decaptcha(captcha, true);

		sleep();

		WebElement captchCode = driver.findElement(By.name("captcha_code"));
		captchCode.sendKeys(decaptcha);

		sleep();

		WebElement submitButton = driver.findElement(By.id("submit_bt"));
		submitButton.click();

		sleep(1_000L);

		String url = driver.getCurrentUrl();
		if (url.contains("/sess-bin/login_session.cgi")) {
			throw new RuntimeException("login failed!");
		}
	}

	private void moveToConsole(WebDriver driver) {
		By areaBy = By.xpath("//area[1]");

		WebDriverWait areaWait = new WebDriverWait(driver, 30);
		areaWait.until(ExpectedConditions.visibilityOfElementLocated(areaBy));

		WebElement area = driver.findElement(areaBy);
		area.click();
	}

	private void clickCpeMenu(WebDriver driver) {
		By mainBodyBy = By.xpath("//frame[@name='main_body']");

		WebDriverWait mainBodyWait = new WebDriverWait(driver, 30);
		mainBodyWait.until(ExpectedConditions.visibilityOfElementLocated(mainBodyBy));

		WebElement mainBodyFrame = driver.findElement(mainBodyBy);
		driver.switchTo().frame(mainBodyFrame);

		By naviMenuAdvanceBy = By.xpath("//iframe[@name='navi_menu_advance']");

		WebDriverWait naviMenuAdvanceWait = new WebDriverWait(driver, 30);
		naviMenuAdvanceWait.until(ExpectedConditions.visibilityOfElementLocated(naviMenuAdvanceBy));

		WebElement advancedFrame = driver.findElement(naviMenuAdvanceBy);
		driver.switchTo().frame(advancedFrame);

		By advancedMenuBy = By.xpath("//td[@id='advance_setup_td']//span");

		WebDriverWait advancedMenuWait = new WebDriverWait(driver, 30);
		advancedMenuWait.until(ExpectedConditions.visibilityOfElementLocated(advancedMenuBy));

		WebElement advancedMenu = driver.findElement(advancedMenuBy);
		advancedMenu.click();

		By sysconfMenuBy = By.xpath("//td[@id='sysconf_setup_td']//span");

		WebDriverWait sysconfMenuWait = new WebDriverWait(driver, 30);
		sysconfMenuWait.until(ExpectedConditions.visibilityOfElementLocated(sysconfMenuBy));

		WebElement sysconfMenu = driver.findElement(sysconfMenuBy);
		sysconfMenu.click();
		sleep();

		By cpeMenuBy = By.xpath("//td[@id='sysconf_setup']//tr[2]//span//a");

		WebDriverWait cpeMenuWait = new WebDriverWait(driver, 30);
		cpeMenuWait.until(ExpectedConditions.visibilityOfElementLocated(cpeMenuBy));

		WebElement cpeMenu = driver.findElement(cpeMenuBy);
		cpeMenu.click();
		sleep();

		driver.switchTo().defaultContent();
	}

	private void changeCpeEmailSetting(WebDriver driver, EmailSetting emailSetting) {
		By mainBodyBy = By.xpath("//frame[@name='main_body']");

		WebDriverWait mainBodyWait = new WebDriverWait(driver, 30);
		mainBodyWait.until(ExpectedConditions.visibilityOfElementLocated(mainBodyBy));

		WebElement mainBodyFrame = driver.findElement(mainBodyBy);
		driver.switchTo().frame(mainBodyFrame);

		By mainBy = By.xpath("//iframe[@name='main']");

		WebDriverWait mainWait = new WebDriverWait(driver, 30);
		mainWait.until(ExpectedConditions.visibilityOfElementLocated(mainBy));

		WebElement mainFrame = driver.findElement(mainBy);
		driver.switchTo().frame(mainFrame);

		By emailBy = By.xpath("//input[@name='email']");

		WebDriverWait emailWait = new WebDriverWait(driver, 30);
		emailWait.until(ExpectedConditions.visibilityOfElementLocated(emailBy));

		WebElement email = driver.findElement(emailBy);
		email.clear();
		sleep();
		email.sendKeys(emailSetting.getEmail());

		By sendEmailBy = By.xpath("//input[@name='send_email']");

		WebDriverWait sendEmailWait = new WebDriverWait(driver, 30);
		sendEmailWait.until(ExpectedConditions.visibilityOfElementLocated(sendEmailBy));

		WebElement sendEmail = driver.findElement(sendEmailBy);
		sendEmail.clear();
		sleep();
		sendEmail.sendKeys(emailSetting.getSendEmail());

		By smtpBy = By.xpath("//input[@name='smtp']");

		WebDriverWait smtpWait = new WebDriverWait(driver, 30);
		smtpWait.until(ExpectedConditions.visibilityOfElementLocated(smtpBy));

		WebElement smtp = driver.findElement(smtpBy);
		smtp.clear();
		sleep();
		smtp.sendKeys(emailSetting.getSmtp());

		By saveBy = By.xpath("//form[@name='email_fm']//button[@class='login_main_button']");

		WebDriverWait saveWait = new WebDriverWait(driver, 30);
		saveWait.until(ExpectedConditions.visibilityOfElementLocated(saveBy));

		WebElement save = driver.findElement(saveBy);
		save.click();

		// FIXME
		sleep(1_000L);

		driver.switchTo().defaultContent();
	}

	private void reloadCpeMenu(WebDriver driver) {
		By mainBodyBy = By.xpath("//frame[@name='main_body']");

		WebDriverWait mainBodyWait = new WebDriverWait(driver, 30);
		mainBodyWait.until(ExpectedConditions.visibilityOfElementLocated(mainBodyBy));

		WebElement mainBodyFrame = driver.findElement(mainBodyBy);
		driver.switchTo().frame(mainBodyFrame);

		By naviMenuAdvanceBy = By.xpath("//iframe[@name='navi_menu_advance']");

		WebDriverWait naviMenuAdvanceWait = new WebDriverWait(driver, 30);
		naviMenuAdvanceWait.until(ExpectedConditions.visibilityOfElementLocated(naviMenuAdvanceBy));

		WebElement advancedFrame = driver.findElement(naviMenuAdvanceBy);
		driver.switchTo().frame(advancedFrame);

		By cpeMenuBy = By.xpath("//td[@id='sysconf_setup']//tr[2]//span//a");

		WebDriverWait cpeMenuWait = new WebDriverWait(driver, 30);
		cpeMenuWait.until(ExpectedConditions.visibilityOfElementLocated(cpeMenuBy));

		WebElement cpeMenu = driver.findElement(cpeMenuBy);
		cpeMenu.click();
		sleep();

		driver.switchTo().defaultContent();
	}

	private EmailSetting getCpeEmailSetting(WebDriver driver) {
		By mainBodyBy = By.xpath("//frame[@name='main_body']");

		WebDriverWait mainBodyWait = new WebDriverWait(driver, 30);
		mainBodyWait.until(ExpectedConditions.visibilityOfElementLocated(mainBodyBy));

		WebElement mainBodyFrame = driver.findElement(mainBodyBy);
		driver.switchTo().frame(mainBodyFrame);

		By mainBy = By.xpath("//iframe[@name='main']");

		WebDriverWait mainWait = new WebDriverWait(driver, 30);
		mainWait.until(ExpectedConditions.visibilityOfElementLocated(mainBy));

		WebElement mainFrame = driver.findElement(mainBy);
		driver.switchTo().frame(mainFrame);

		By emailBy = By.xpath("//input[@name='email']");

		WebDriverWait emailWait = new WebDriverWait(driver, 30);
		emailWait.until(ExpectedConditions.visibilityOfElementLocated(emailBy));

		WebElement email = driver.findElement(emailBy);
		String emailValue = email.getAttribute("value");

		By sendEmailBy = By.xpath("//input[@name='send_email']");

		WebDriverWait sendEmailWait = new WebDriverWait(driver, 30);
		sendEmailWait.until(ExpectedConditions.visibilityOfElementLocated(sendEmailBy));

		WebElement sendEmail = driver.findElement(sendEmailBy);
		String sendEmailValue = sendEmail.getAttribute("value");

		By smtpBy = By.xpath("//input[@name='smtp']");

		WebDriverWait smtpWait = new WebDriverWait(driver, 30);
		smtpWait.until(ExpectedConditions.visibilityOfElementLocated(smtpBy));

		WebElement smtp = driver.findElement(smtpBy);
		String smtpValue = smtp.getAttribute("value");

		EmailSetting emailSetting = new EmailSetting();
		emailSetting.setEmail(emailValue);
		emailSetting.setSendEmail(sendEmailValue);
		emailSetting.setSmtp(smtpValue);

		driver.switchTo().defaultContent();

		return emailSetting;
	}

	private void verifyCpeEmailSetting(WebDriver driver, EmailSetting emailSetting) {
		By mainBodyBy = By.xpath("//frame[@name='main_body']");

		WebDriverWait mainBodyWait = new WebDriverWait(driver, 30);
		mainBodyWait.until(ExpectedConditions.visibilityOfElementLocated(mainBodyBy));

		WebElement mainBodyFrame = driver.findElement(mainBodyBy);
		driver.switchTo().frame(mainBodyFrame);

		By mainBy = By.xpath("//iframe[@name='main']");

		WebDriverWait mainWait = new WebDriverWait(driver, 30);
		mainWait.until(ExpectedConditions.visibilityOfElementLocated(mainBy));

		WebElement mainFrame = driver.findElement(mainBy);
		driver.switchTo().frame(mainFrame);

		By emailBy = By.xpath("//input[@name='email']");

		WebDriverWait emailWait = new WebDriverWait(driver, 30);
		emailWait.until(ExpectedConditions.visibilityOfElementLocated(emailBy));

		WebElement email = driver.findElement(emailBy);
		String emailValue = email.getAttribute("value");

		Assert.isTrue(emailValue.contentEquals(emailSetting.getEmail()),
				"email is wrong: expected: '" + emailSetting.getEmail() + "', but was: '" + emailValue + "'");

		By sendEmailBy = By.xpath("//input[@name='send_email']");

		WebDriverWait sendEmailWait = new WebDriverWait(driver, 30);
		sendEmailWait.until(ExpectedConditions.visibilityOfElementLocated(sendEmailBy));

		WebElement sendEmail = driver.findElement(sendEmailBy);
		String sendEmailValue = sendEmail.getAttribute("value");
		Assert.isTrue(sendEmailValue.contentEquals(emailSetting.getSendEmail()), "sendEmail is wrong: expected: '"
				+ emailSetting.getSendEmail() + "', but was: '" + sendEmailValue + "'");

		By smtpBy = By.xpath("//input[@name='smtp']");

		WebDriverWait smtpWait = new WebDriverWait(driver, 30);
		smtpWait.until(ExpectedConditions.visibilityOfElementLocated(smtpBy));

		WebElement smtp = driver.findElement(smtpBy);
		String smtpValue = smtp.getAttribute("value");
		Assert.isTrue(smtpValue.contentEquals(emailSetting.getSmtp()),
				"smtp is wrong: expected: '" + emailSetting.getSmtp() + "', but was: '" + smtpValue + "'");

		driver.switchTo().defaultContent();
	}

}
