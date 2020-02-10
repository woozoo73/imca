package com.bluedigm.imca.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.opennms.netmgt.model.OnmsNode;
import org.opennms.netmgt.model.OnmsNodeList;
import org.opennms.netmgt.provision.persist.requisition.Requisition;
import org.opennms.web.rest.v2.status.model.NodeDTO;
import org.opennms.web.rest.v2.status.model.NodeDTOList;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.bluedigm.imca.domain.Cpe;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OnmsService {

	@Resource(name = "onmsRest")
	RestTemplate onmsRest;

	public void add(Cpe cpe) {
		log.info("add cpe: {}", cpe);

		Requisition requisition = cpe.toRequisition();

		log.info("requisition: {}", requisition);

		onmsRest.postForEntity("rest/requisitions", requisition, null);
		onmsRest.put("rest/requisitions/{name}/import?rescanExisting=true", null, cpe.getName());
	}

	public List<Cpe> getCpes() {
		OnmsNodeList nodes = onmsRest.getForObject("rest/nodes?limit={limit}", OnmsNodeList.class, 0);
		log.info("getCpes nodes: {}", nodes);

		List<Cpe> cpes = new ArrayList<>();
		for (OnmsNode node : nodes.getObjects()) {
			cpes.add(new Cpe(node));
		}

		log.info("cpes: {}", cpes);

		return cpes;
	}

	public List<Cpe> getDownCpes() {
		NodeDTOList nodes = onmsRest.getForObject("api/v2/status/nodes/outages?severityFilter={severity}&limit={limit}",
				NodeDTOList.class, "Major", Integer.MAX_VALUE);
		log.info("getDownCpes nodes: {}", nodes);

		List<Cpe> cpes = new ArrayList<>();
		for (NodeDTO node : nodes.getObjects()) {
			cpes.add(new Cpe(node));
		}

		log.info("cpes: {}", cpes);

		return cpes;
	}

}
