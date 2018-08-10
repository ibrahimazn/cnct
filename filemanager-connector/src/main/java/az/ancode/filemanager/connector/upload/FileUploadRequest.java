package az.ancode.filemanager.connector.upload;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import az.ancode.filemanager.connector.Request;

/**
 * filemanager upload call.
 * 
 * eg:- API : /upload Method : POST Headers : {Content-Type :
 * application/x-www-form-urlencoded}
 * 
 * Query parameters : uuid, pvc, path,file
 * 
 * Success Response:
 * 
 * 
 * Error Response:
 * 
 * 
 */
@JsonIgnoreProperties
public class FileUploadRequest extends Request {
	
	private String uuid;
	
	private String pvc;
	
	private String path;
	
	private MultipartFile file;
	
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
	public MultipartFile getFile() {
		return file;
	}
	public void setFile(MultipartFile file) {
		this.file = file;
	}
}
