package com.bluedigm.imca.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

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
		log.info("cpe: {}", cpe);

		Requisition requisition = cpe.toRequisition();

		log.info("requisition: {}", requisition);

		onmsRest.postForEntity("rest/requisitions", requisition, null);
		onmsRest.put("rest/requisitions/{name}/import?rescanExisting=true", null, cpe.getName());
	}

	public List<Cpe> getDownCpes() {
		NodeDTOList nodes = onmsRest.getForObject("api/v2/status/nodes/outages?severityFilter={severity}",
				NodeDTOList.class, "Major");
		log.info("nodes: {}", nodes);

		List<Cpe> cpes = new ArrayList<>();
		for (NodeDTO node : nodes) {
			cpes.add(new Cpe(node));
		}

		log.info("cpes: {}", cpes);

		return cpes;
	}

}
