package az.ancode.filemanager.connector.fileset;

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
public class FileListResponse {
	
	@JsonProperty("result")
	private List<FileList> result;

	@JsonProperty("status")
	private String status;

	public List<FileList> getResult() {
		return result;
	}

	public void setResult(List<FileList> result) {
		this.result = result;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
