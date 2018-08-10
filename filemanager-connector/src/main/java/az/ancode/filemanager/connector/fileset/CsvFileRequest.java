package az.ancode.filemanager.connector.fileset;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import az.ancode.filemanager.connector.Request;

/**
 * filemanager list call.
 * 
 * eg:- API : /file-manager Method : GET Headers : {Content-Type :
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
public class CsvFileRequest extends Request {
	
	private String uuid;
	
	private String pvc;
	
	private String filename;
    
    private String start;
	
    private String end;

    /**
     * @return the uuid
     */
    public String getUuid() {
      return uuid;
    }

    /**
     * @param uuid the uuid to set
     */
    public void setUuid(String uuid) {
      this.uuid = uuid;
    }

    /**
     * @return the pvc
     */
    public String getPvc() {
      return pvc;
    }

    /**
     * @param pvc the pvc to set
     */
    public void setPvc(String pvc) {
      this.pvc = pvc;
    }

    /**
     * @return the filename
     */
    public String getFilename() {
      return filename;
    }

    /**
     * @param filename the filename to set
     */
    public void setFilename(String filename) {
      this.filename = filename;
    }

    /**
     * @return the start
     */
    public String getStart() {
      return start;
    }

    /**
     * @param start the start to set
     */
    public void setStart(String start) {
      this.start = start;
    }

    /**
     * @return the end
     */
    public String getEnd() {
      return end;
    }

    /**
     * @param end the end to set
     */
    public void setEnd(String end) {
      this.end = end;
    }

}
