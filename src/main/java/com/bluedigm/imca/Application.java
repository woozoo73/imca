package com.bluedigm.imca;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	private String webDriverId = "webdriver.chrome.driver";

	@Value("${webdriver.path}")
	public void setWebDriverPath(String webDriverPath) {
		System.setProperty(webDriverId, webDriverPath);
	}

	private static String deathbycaptchaUsername;

	private static String deathbycaptchaPassword;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Value("${deathbycaptcha.username}")
	public void setDeathbycaptchaUsername(String deathbycaptchaUsername) {
		Application.deathbycaptchaUsername = deathbycaptchaUsername;
	}

	@Value("${deathbycaptcha.password}")
	public void setDeathbycaptchaPassword(String deathbycaptchaPassword) {
		Application.deathbycaptchaPassword = deathbycaptchaPassword;
	}

	public static String getDeathbycaptchaUsername() {
		return deathbycaptchaUsername;
	}

	public static String getDeathbycaptchaPassword() {
		return deathbycaptchaPassword;
	}

}
