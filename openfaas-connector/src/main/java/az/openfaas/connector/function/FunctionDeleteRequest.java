package az.openfaas.connector.function;

import az.openfaas.connector.Request;

/**
 * Function to deploy.
 * 
 * eg:- API : /system/functions Method : DELETE Headers : {Content-Type :
 * application/json}
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
public class FunctionDeleteRequest extends Request {

  private String functionName;

  public String getFunctionName() {
    return functionName;
  }

  public void setFunctionName(String functionName) {
    this.functionName = functionName;
  }

}
