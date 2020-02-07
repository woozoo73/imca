package com.bluedigm.imca.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.bluedigm.imca.domain.Cpe;
import com.bluedigm.imca.domain.EmailSetting;
import com.bluedigm.imca.domain.EmailUpdateRequest;
import com.bluedigm.imca.service.EmailService;

@Controller
public class EmailController {

	protected Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private EmailService emailService;

	@GetMapping("/email/form")
	public String form() {
		log.info("form");

		return "email/form";
	}

	@GetMapping("/email/view")
	public String view(final HttpServletRequest request, final Model model) {
		Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
		Cpe cpe = (Cpe) inputFlashMap.get("cpe");

		EmailSetting emailSetting = emailService.getEmailSetting(cpe);
		model.addAttribute("emailSetting", emailSetting);

		return "email/view";
	}

	@PostMapping("/email")
	public String update(@ModelAttribute final EmailUpdateRequest emailUpdateRequest,
			final RedirectAttributes redirectAttributes) {
		log.info("update: {}", emailUpdateRequest);

		Cpe cpe = emailUpdateRequest.getCpe();
		EmailSetting emailSetting = emailUpdateRequest.getEmailSetting();

		emailService.changeEmailSetting(cpe, emailSetting);

		redirectAttributes.addFlashAttribute("cpe", cpe);

		return "redirect:/email/view";
	}

}
