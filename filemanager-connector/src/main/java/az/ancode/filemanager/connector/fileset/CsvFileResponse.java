package az.ancode.filemanager.connector.fileset;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

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
public class CsvFileResponse {

  @JsonProperty("heading")
  private List<String> heading;

  @JsonProperty("result")
  private List<ArrayList> result;

  @JsonProperty("message")
  private String message;

  @JsonProperty("status")
  private String status;

  @JsonProperty("totalRecords")
  private String totalRecords;

  /**
   * @return the heading
   */
  public List<String> getHeading() {
    return heading;
  }

  /**
   * @param heading the heading to set
   */
  public void setHeading(List<String> heading) {
    this.heading = heading;
  }


  /**
   * @return the message
   */
  public String getMessage() {
    return message;
  }

  /**
   * @param message the message to set
   */
  public void setMessage(String message) {
    this.message = message;
  }

  /**
   * @return the result
   */
  public List<ArrayList> getResult() {
    return result;
  }

  /**
   * @param result the result to set
   */
  public void setResult(List<ArrayList> result) {
    this.result = result;
  }

  /**
   * @return the status
   */
  public String getStatus() {
    return status;
  }

  /**
   * @param status the status to set
   */
  public void setStatus(String status) {
    this.status = status;
  }

  /**
   * @return the totalRecords
   */
  public String getTotalRecords() {
    return totalRecords;
  }

  /**
   * @param totalRecords the totalRecords to set
   */
  public void setTotalRecords(String totalRecords) {
    this.totalRecords = totalRecords;
  }

}
