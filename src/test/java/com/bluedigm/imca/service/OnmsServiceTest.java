package com.bluedigm.imca.service;

import java.util.Arrays;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bluedigm.imca.domain.Cpe;

@SpringBootTest
class OnmsServiceTest {

	@Autowired
	OnmsService onmsService;

	String name;

	@Test
	public void add() {
		Cpe cpe = new Cpe();
		cpe.setName("test-" + UUID.randomUUID());
		cpe.setCity("test-building");
		cpe.setBuilding("test-building");
		// cpe.setIp("127.0.0.1");
		cpe.setIp("192.168.100.222");
		cpe.setServices(Arrays.asList("HTTP", "HTTPS", "SNMP"));

		onmsService.add(cpe);
	}

	@Test
	public void getDownCpes() {
		onmsService.getDownCpes();
	}
	
}
