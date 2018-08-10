package az.openfaas.connector.function;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author ibrahim
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EnvVariable {
  
  @JsonProperty("nameSpace")
  private String nameSpace;

  @JsonProperty("volumeName")
  private String volumeName;

  public String getNameSpace() {
    return nameSpace;
  }

  public void setNameSpace(String nameSpace) {
    this.nameSpace = nameSpace;
  }

  public String getVolumeName() {
    return volumeName;
  }

  public void setVolumeName(String volumeName) {
    this.volumeName = volumeName;
  }
}
