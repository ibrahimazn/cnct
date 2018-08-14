package az.ldap.zabbix.entity;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Operations {
	
	private String operationid;
	
	private String actionid;
	
	private Integer operationtype;
	
	private String esc_period;
	
	private Integer esc_step_from;
	
	private List<String> opconditions;
	
	private Message opmessage;
	
	private Integer evaltype;
	
	private Integer esc_step_to;
	
	private List<Groups> opmessage_grp;
	
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

	public String getEsc_period() {
		return esc_period;
	}

	public void setEsc_period(String esc_period) {
		this.esc_period = esc_period;
	}

	public Integer getEsc_step_from() {
		return esc_step_from;
	}

	public void setEsc_step_from(Integer esc_step_from) {
		this.esc_step_from = esc_step_from;
	}

	public List<String> getOpconditions() {
		return opconditions;
	}

	public void setOpconditions(List<String> opconditions) {
		this.opconditions = opconditions;
	}

	public Integer getEvaltype() {
		return evaltype;
	}

	public void setEvaltype(Integer evaltype) {
		this.evaltype = evaltype;
	}

	public Integer getEsc_step_to() {
		return esc_step_to;
	}

	public void setEsc_step_to(Integer esc_step_to) {
		this.esc_step_to = esc_step_to;
	}

	public List<Groups> getOpmessage_grp() {
		return opmessage_grp;
	}

	public void setOpmessage_grp(List<Groups> opmessage_grp) {
		this.opmessage_grp = opmessage_grp;
	}

	public List<Users> getOpmessage_usr() {
		return opmessage_usr;
	}

	public void setOpmessage_usr(List<Users> opmessage_usr) {
		this.opmessage_usr = opmessage_usr;
	}

	public Message getOpmessage() {
		return opmessage;
	}

	public void setOpmessage(Message opmessage) {
		this.opmessage = opmessage;
	}
}
