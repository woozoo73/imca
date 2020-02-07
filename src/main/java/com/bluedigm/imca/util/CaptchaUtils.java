package com.bluedigm.imca.util;

import org.base64.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.DeathByCaptcha.Captcha;
import com.DeathByCaptcha.Client;
import com.DeathByCaptcha.HttpClient;
import com.bluedigm.imca.config.Config;

public abstract class CaptchaUtils {

	private static final Logger log = LoggerFactory.getLogger(CaptchaUtils.class);

	public static String decaptcha(String sourceData, boolean noNumber) {
		String value = decaptcha(sourceData);

		if (noNumber) {
			value = value.replaceAll("0", "o");
			value = value.replaceAll("1", "l");
		}

		return value;
	}

	private static String decaptcha(String sourceData) {
		try {
			String[] parts = sourceData.split(",");
			String imageString = parts[1];

			byte[] imageByte = Base64.decode(imageString);
			
			log.info("username: {}, password: {}", Config.getDeathbycaptchaUsername(),
					Config.getDeathbycaptchaPassword());

			Client client = new HttpClient(Config.getDeathbycaptchaUsername(),
					Config.getDeathbycaptchaPassword());
			client.isVerbose = true;

			log.info("Your balance is {} US cents", client.getBalance());

			Captcha captcha = client.decode(imageByte, "", 0, null, "", 120);

			if (captcha == null) {
				throw new RuntimeException("Can't found");
			}

			log.info("CAPTCHA solved: {}", captcha.text);

			return captcha.text;
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			throw new RuntimeException(e);
		}
	}

}
