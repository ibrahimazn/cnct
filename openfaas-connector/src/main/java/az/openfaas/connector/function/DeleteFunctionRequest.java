package az.openfaas.connector.function;

import java.util.List;

/**
 * @author ibrahim
 *
 */
public class DeleteFunctionRequest {
	
	private String functionName;

	public String getFunctionName() {
		return functionName;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

}
