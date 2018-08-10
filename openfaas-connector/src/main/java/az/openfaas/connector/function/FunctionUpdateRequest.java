package az.openfaas.connector.function;

import az.openfaas.connector.Request;

/**
 * Update Function to deploy.
 * 
 * eg:- API : /system/functions Method : PUT Headers : {Content-Type : application/json}
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
public class FunctionUpdateRequest extends Request {

	private Function function;

	public Function getFunction() {
		return function;
	}

	public void setFunction(Function function) {
		this.function = function;
	}
}
