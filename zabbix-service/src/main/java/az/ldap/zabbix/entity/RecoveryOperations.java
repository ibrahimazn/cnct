package az.ldap.zabbix.entity;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RecoveryOperations {

	private String operationid;
	
	private String actionid;
	
	private Integer operationtype;
	
	private List<String> opconditions;
	
	private String evaltype;
		
	private Message opmessage;
	
	private List<Users> opmessage_usr;

	public String getOperationid() {
		return operationid;
	}

	public void setOperationid(String operationid) {
		this.operationid = operationid;
	}

	public String getActionid() {
		return actionid;
	}

	public void setActionid(String actionid) {
		this.actionid = actionid;
	}

	public Integer getOperationtype() {
		return operationtype;
	}

	public void setOperationtype(Integer operationtype) {
		this.operationtype = operationtype;
	}

	public List<String> getOpconditions() {
		return opconditions;
	}

	public void setOpconditions(List<String> opconditions) {
		this.opconditions = opconditions;
	}

	public String getEvaltype() {
		return evaltype;
	}

	public void setEvaltype(String evaltype) {
		this.evaltype = evaltype;
	}

	public Message getOpmessage() {
		return opmessage;
	}

	public void setOpmessage(Message opmessage) {
		this.opmessage = opmessage;
	}

	public List<Users> getOpmessage_usr() {
		return opmessage_usr;
	}

	public void setOpmessage_usr(List<Users> opmessage_usr) {
		this.opmessage_usr = opmessage_usr;
	}
}
