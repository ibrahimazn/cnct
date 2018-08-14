package az.ldap.zabbix.entity;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "hosts_interfaces")
@SuppressWarnings("serial")
public class HostInterface implements Serializable {
	@Id
	private String id;

	@Field("dns")
	private String dns;
	
	@Field("interfaceid")
	private String interfaceId;

	@Field("ipaddr")
	private String ipAddress;

	@Field("port")
	private String port;
	
	@Field("items")
	private Integer items;
	
	@Field("graphs")
	private Integer graphs;
	
	@Field("host_id")
	private String hostId;

	@Field("default")
	private Integer defaultAgent;

	/** type of the agent. */
	@Field("type")
	private Integer type;

	@Field("use_ip")
	private Integer useIp;

	/**
	 * @return the dns
	 */
	public String getDns() {
		return dns;
	}

	/**
	 * @param dns
	 *            the dns to set
	 */
	public void setDns(String dns) {
		this.dns = dns;
	}

	/**
	 * @return the ipAddress
	 */
	public String getIpAddress() {
		return ipAddress;
	}

	/**
	 * @param ipAddress
	 *            the ipAddress to set
	 */
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	/**
	 * @return the port
	 */
	public String getPort() {
		return port;
	}

	/**
	 * @param port
	 *            the port to set
	 */
	public void setPort(String port) {
		this.port = port;
	}

	/**
	 * @return the defaultAgent
	 */
	public Integer getDefaultAgent() {
		return defaultAgent;
	}

	/**
	 * @param defaultAgent
	 *            the defaultAgent to set
	 */
	public void setDefaultAgent(Integer defaultAgent) {
		this.defaultAgent = defaultAgent;
	}

	/**
	 * @return the type
	 */
	public Integer getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(Integer type) {
		this.type = type;
	}

	/**
	 * @return the useIp
	 */
	public Integer getUseIp() {
		return useIp;
	}

	/**
	 * @param useIp
	 *            the useIp to set
	 */
	public void setUseIp(Integer useIp) {
		this.useIp = useIp;
	}
	
	/**
	 * @return the hostId
	 */
	public String getHostId() {
		return hostId;
	}

	/**
	 * @param hostId the hostId to set
	 */
	public void setHostId(String hostId) {
		this.hostId = hostId;
	}

	/**
	 * @return the interfaceId
	 */
	public String getInterfaceId() {
		return interfaceId;
	}

	/**
	 * @param interfaceId the interfaceId to set
	 */
	public void setInterfaceId(String interfaceId) {
		this.interfaceId = interfaceId;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public Integer getItems() {
		return items;
	}

	public void setItems(Integer items) {
		this.items = items;
	}

	public Integer getGraphs() {
		return graphs;
	}

	public void setGraphs(Integer graphs) {
		this.graphs = graphs;
	}
}
