package az.ancode.filemanager.connector.fileset;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author ibrahim
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FileList {

	@JsonProperty("name")
	private String name;

	@JsonProperty("permission")
	private String permission;

	@JsonProperty("size")
	private Long size;

	@JsonProperty("lastModifiedDate")
	private String lastModifiedDate;

	@JsonProperty("isDir")
	private Boolean isDir;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public String getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(String lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public Boolean getIsDir() {
		return isDir;
	}

	public void setIsDir(Boolean isDir) {
		this.isDir = isDir;
	}

}
