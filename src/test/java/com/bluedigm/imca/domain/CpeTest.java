package com.bluedigm.imca.domain;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.opennms.netmgt.provision.persist.requisition.Requisition;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class CpeTest {

	@Test
	void test() {
		Cpe cpe = new Cpe();
		cpe.setName("test-02");
		cpe.setCity("test-building");
		cpe.setBuilding("test-building");
		cpe.setIp("127.0.0.1");
		cpe.setServices(Arrays.asList("HTTP", "HTTPS", "SNMP"));

		log.info("cpe: {}", cpe);

		Requisition requisition = cpe.toRequisition();

		log.info("requisition: {}", requisition);
	}

}
