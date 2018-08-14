package az.ldap.sync.util;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import az.ldap.zabbix.entity.Alert;
import az.ldap.zabbix.entity.Events;

@SuppressWarnings("serial")
@JsonIgnoreProperties(ignoreUnknown = true)
public class VMDashboards implements Serializable {
	
	private String status;
	
	private String createDateTime;
	
	private String upTime;
	
	private String hostName;
	
	private String information;
	
	private String bootTime;
	
	private String openedFiles;
	
	private String processes;
	
	private String loggedUsers;
	
	private String runningProcess;
	
	private String agentStatus;
	
	private Long items;
	
	private Long graphs;

	private Long triggers;
	
	private Long totalMemory;
	
	private Long usedMemory;
	
	private Long availableMemory;
	
	private List<Events> messages;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreateDateTime() {
		return createDateTime;
	}

	public void setCreateDateTime(String createDateTime) {
		this.createDateTime = createDateTime;
	}

	public String getUpTime() {
		return upTime;
	}

	public void setUpTime(String upTime) {
		this.upTime = upTime;
	}

	public Long getItems() {
		return items;
	}

	public void setItems(Long items) {
		this.items = items;
	}

	public Long getGraphs() {
		return graphs;
	}

	public void setGraphs(Long graphs) {
		this.graphs = graphs;
	}

	public Long getTriggers() {
		return triggers;
	}

	public void setTriggers(Long triggers) {
		this.triggers = triggers;
	}

	public Long getTotalMemory() {
		return totalMemory;
	}

	public void setTotalMemory(Long totalMemory) {
		this.totalMemory = totalMemory;
	}

	public Long getUsedMemory() {
		return usedMemory;
	}

	public void setUsedMemory(Long usedMemory) {
		this.usedMemory = usedMemory;
	}

	public Long getAvailableMemory() {
		return availableMemory;
	}

	public void setAvailableMemory(Long availableMemory) {
		this.availableMemory = availableMemory;
	}

	public List<Events> getMessages() {
		return messages;
	}

	public void setMessages(List<Events> messages) {
		this.messages = messages;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getInformation() {
		return information;
	}

	public void setInformation(String information) {
		this.information = information;
	}

	public String getBootTime() {
		return bootTime;
	}

	public void setBootTime(String bootTime) {
		this.bootTime = bootTime;
	}

	public String getOpenedFiles() {
		return openedFiles;
	}

	public void setOpenedFiles(String openedFiles) {
		this.openedFiles = openedFiles;
	}

	public String getProcesses() {
		return processes;
	}

	public void setProcesses(String processes) {
		this.processes = processes;
	}

	public String getLoggedUsers() {
		return loggedUsers;
	}

	public void setLoggedUsers(String loggedUsers) {
		this.loggedUsers = loggedUsers;
	}

	public String getRunningProcess() {
		return runningProcess;
	}

	public void setRunningProcess(String runningProcess) {
		this.runningProcess = runningProcess;
	}

	public String getAgentStatus() {
		return agentStatus;
	}

	public void setAgentStatus(String agentStatus) {
		this.agentStatus = agentStatus;
	}
}
