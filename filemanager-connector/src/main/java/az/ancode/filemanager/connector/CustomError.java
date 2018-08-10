package az.ancode.filemanager.connector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CustomError {

	 /** Global error list attribute. */
    @JsonProperty
    private List<String> globalError = new ArrayList<String>();

    /** Field errors attribute. */
    @JsonProperty
    private HashMap<String, String> fieldErrors = new HashMap<String, String>();

	public List<String> getGlobalError() {
		return globalError;
	}

	public void setGlobalError(List<String> globalError) {
		this.globalError = globalError;
	}

	public HashMap<String, String> getFieldErrors() {
		return fieldErrors;
	}

	public void setFieldErrors(HashMap<String, String> fieldErrors) {
		this.fieldErrors = fieldErrors;
	}
}
