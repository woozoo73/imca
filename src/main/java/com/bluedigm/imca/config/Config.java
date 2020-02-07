package com.bluedigm.imca.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
@Component
public class Config {

	private static Logger log = LoggerFactory.getLogger(Config.class);

	@Value("${onms.url}")
	private String onmsUrl;

	@Value("${onms.admin.username}")
	private String onmsAdminUsername;

	@Value("${onms.admin.password}")
	private String onmsAdminPassword;

	private String webDriverId = "webdriver.chrome.driver";

	@Value("${webdriver.path}")
	public void setWebDriverPath(String webDriverPath) {
		log.info("webdriver.path: {}", webDriverPath);

		System.setProperty(webDriverId, webDriverPath);
	}

	private static boolean webdriverHeadless;

	private static String deathbycaptchaUsername;

	private static String deathbycaptchaPassword;

	@Value("${webdriver.headless:true}")
	public void setWebdriverHeadless(boolean webdriverHeadless) {
		log.info("webdriver.headless: {}", webdriverHeadless);

		Config.webdriverHeadless = webdriverHeadless;
	}

	@Value("${deathbycaptcha.username}")
	public void setDeathbycaptchaUsername(String deathbycaptchaUsername) {
		log.info("deathbycaptcha.username: {}", deathbycaptchaUsername);

		Config.deathbycaptchaUsername = deathbycaptchaUsername;
	}

	@Value("${deathbycaptcha.password}")
	public void setDeathbycaptchaPassword(String deathbycaptchaPassword) {
		log.info("deathbycaptcha.password: {}", deathbycaptchaPassword);

		Config.deathbycaptchaPassword = deathbycaptchaPassword;
	}

	public static boolean getWebdriverHeadless() {
		return webdriverHeadless;
	}

	public static String getDeathbycaptchaUsername() {
		return deathbycaptchaUsername;
	}

	public static String getDeathbycaptchaPassword() {
		return deathbycaptchaPassword;
	}

	@Bean
	public RestTemplate onmsRest() {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(onmsUrl));
		restTemplate.getInterceptors().add(onmsAuthorizationInterceptor());

		return restTemplate;
	}

	@Bean
	public ClientHttpRequestInterceptor onmsAuthorizationInterceptor() {
		return new BasicAuthorizationInterceptor(onmsAdminUsername, onmsAdminPassword);
	}

}
