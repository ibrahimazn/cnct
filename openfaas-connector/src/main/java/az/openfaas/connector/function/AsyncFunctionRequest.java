package az.openfaas.connector.function;

import az.openfaas.connector.Request;

/**
 * @author ibrahim
 *
 */
public class AsyncFunctionRequest  extends Request {
	
	private AsyncFunction input;

	public AsyncFunction getInput() {
		return input;
	}

	public void setInput(AsyncFunction input) {
		this.input = input;
	}

}
