package az.ancode.filemanager.connector.lfs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import az.ancode.filemanager.connector.Request;

/**
 * lfs install call.
 * 
 * eg:- API : /install-lfs Method : POST Headers : {Content-Type :
 * application/x-www-form-urlencoded}
 * 
 * Query parameters : uuid, pvc, path
 * 
 * Success Response:
 * 
 * 
 * Error Response:
 * 
 * 
 */
@JsonIgnoreProperties
public class LFSInstallRequest extends Request {

	private String uuid;

	private String pvc;

	private String path;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getPvc() {
		return pvc;
	}

	public void setPvc(String pvc) {
		this.pvc = pvc;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
