package az.ancode.filemanager.connector.lfs;

import org.json.JSONObject;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * .
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
public class LFSTrackResponse {
	
	@JsonProperty("result")
	private JSONObject result;

	@JsonProperty("status")
	private String status;

	public JSONObject getResult() {
		return result;
	}

	public void setResult(JSONObject result) {
		this.result = result;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
