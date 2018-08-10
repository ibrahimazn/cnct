package az.openfaas.connector.function;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author ibrahim
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AsyncFunction {
	@JsonProperty("input")
	private String input;

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}
}
