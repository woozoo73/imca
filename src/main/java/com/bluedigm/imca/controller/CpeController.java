package com.bluedigm.imca.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.bluedigm.imca.domain.Cpe;
import com.bluedigm.imca.service.OnmsService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class CpeController {

	@Autowired
	private OnmsService onmsService;

	@GetMapping("/cpe/dashboard")
	public String dashboard(final Model model) {
		log.info("dashboard");

		List<Cpe> cpes = onmsService.getCpes();
		model.addAttribute("cpes", cpes);

		return "cpe/dashboard";
	}

	@GetMapping("/cpe/form")
	public String form() {
		log.info("form");

		return "cpe/form";
	}

	@PostMapping("/cpe")
	public String create(@ModelAttribute final Cpe cpe) {
		log.info("create cpe: {}", cpe);

		cpe.setName("TEST-NMS_" + cpe.getNameSuffix());

		onmsService.add(cpe);

		return "redirect:/cpe/dashboard";
	}

}
