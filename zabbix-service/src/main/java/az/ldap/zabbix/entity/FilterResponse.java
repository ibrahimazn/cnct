package az.ldap.zabbix.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FilterResponse {
	
	private Integer evaltype;
	
	private String formula;
	
	private String eval_formula;
	
	private List<Conditions> conditions;

	public Integer getEvaltype() {
		return evaltype;
	}

	public void setEvaltype(Integer evaltype) {
		this.evaltype = evaltype;
	}

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	public String getEval_formula() {
		return eval_formula;
	}

	public void setEval_formula(String eval_formula) {
		this.eval_formula = eval_formula;
	}

	public List<Conditions> getConditions() {
		return conditions;
	}

	public void setConditions(List<Conditions> conditions) {
		this.conditions = conditions;
	}
}
