package az.ancode.filemanager.connector.upload;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

/**
 * Deployment.
 * 
 * Body : { }
 * 
 * Success Response:
 * 
 * 
 * Error Response:
 * 
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FileUploadResponse {
	
	@JsonProperty("result")
	private String result;

	@JsonProperty("status")
	private String status;

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
