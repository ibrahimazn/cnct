package az.ldap.zabbix.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Conditions {

	private Integer conditiontype;
	
	private Integer operator;
	
	private String value;
	
	private String value2;
	
	private String formulaid;
	
	private String conditionid;

	public Integer getConditiontype() {
		return conditiontype;
	}

	public void setConditiontype(Integer conditiontype) {
		this.conditiontype = conditiontype;
	}

	public Integer getOperator() {
		return operator;
	}

	public void setOperator(Integer operator) {
		this.operator = operator;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue2() {
		return value2;
	}

	public void setValue2(String value2) {
		this.value2 = value2;
	}

	public String getFormulaid() {
		return formulaid;
	}

	public void setFormulaid(String formulaid) {
		this.formulaid = formulaid;
	}

	public String getConditionid() {
		return conditionid;
	}

	public void setConditionid(String conditionid) {
		this.conditionid = conditionid;
	}
}
