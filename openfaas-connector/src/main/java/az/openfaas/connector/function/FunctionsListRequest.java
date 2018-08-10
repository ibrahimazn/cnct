package az.openfaas.connector.function;

import java.util.List;

import az.openfaas.connector.Request;

/**
 * Function to deploy.
 * 
 * eg:- API : /system/functions Method : GET Headers : {Content-Type : application/json}
 * 
 * Body : { service: string network: string image: string envProcess: string
 * envVars: [] secrets: [] registryAuth: string }
 * 
 * Success Response:
 * 
 *  
 * Error Response:
 * 
 * 
 */
public class FunctionsListRequest extends Request {

	private List<String> functions;

	public List<String> getFunctions() {
		return functions;
	}

	public void setFunctions(List<String> functions) {
		this.functions = functions;
	}

}
