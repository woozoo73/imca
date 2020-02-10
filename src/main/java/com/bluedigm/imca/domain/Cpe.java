package com.bluedigm.imca.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.opennms.netmgt.model.OnmsNode;
import org.opennms.netmgt.model.PrimaryType;
import org.opennms.netmgt.provision.persist.requisition.Requisition;
import org.opennms.netmgt.provision.persist.requisition.RequisitionInterface;
import org.opennms.netmgt.provision.persist.requisition.RequisitionMonitoredService;
import org.opennms.netmgt.provision.persist.requisition.RequisitionNode;
import org.opennms.web.rest.v2.status.model.NodeDTO;

import lombok.Data;

@Data
public class Cpe {

	private Integer id;

	private String name;

	private String nameSuffix;

	private String ip;

	private List<String> services;

	private String city;

	private String building;

	private String consoleUrl;

	private String username;

	private String password;

	private Date createTime;

	public Cpe() {
	}

	public Cpe(NodeDTO node) {
		this.id = node.getId();
		this.name = node.getName();
	}

	public Cpe(OnmsNode node) {
		this.id = node.getId();
		this.name = node.getLabel();
		this.createTime = node.getCreateTime();
	}

	/**
	 * Order of SNMP
	 * 
	 * P: primary S: secondary N: not eligible
	 */
	private String snmpPrimary = "P";

	public Requisition toRequisition() {
		Requisition requisition = new Requisition();
		requisition.setForeignSource(this.getName());
		List<RequisitionNode> nodes = new ArrayList<>();
		requisition.setNodes(nodes);
		RequisitionNode node = new RequisitionNode();
		nodes.add(node);
		node.setNodeLabel(this.getName());
		node.setForeignId(String.valueOf(System.currentTimeMillis()));
		node.setCity(this.getCity());
		node.setBuilding(this.getBuilding());
		RequisitionInterface iface = new RequisitionInterface();
		node.getInterfaces().add(iface);
		iface.setIpAddr(this.getIp());
		iface.setSnmpPrimary(PrimaryType.get(this.getSnmpPrimary()));
		if (this.getServices() != null && !this.getServices().isEmpty()) {
			List<RequisitionMonitoredService> services = new ArrayList<>();
			iface.setMonitoredServices(services);
			for (String s : this.getServices()) {
				services.add(new RequisitionMonitoredService(s));
			}
		}

		return requisition;
	}

}
