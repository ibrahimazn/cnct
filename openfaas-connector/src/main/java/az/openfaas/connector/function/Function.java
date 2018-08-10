package az.openfaas.connector.function;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author ibrahim
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Function {
  @JsonProperty("service")
  private String service;

  @JsonProperty("image")
  private String image;

  @JsonProperty("network")
  private Integer network;

  @JsonProperty("envProcess")
  private String envProcess;

  @JsonProperty("envVars")
  private EnvVariable envVars;

  @JsonProperty("secrets")
  private List<String> secrets;

  @JsonProperty("registryAuth")
  private String registryAuth;

  public String getService() {
    return service;
  }

  public void setService(String service) {
    this.service = service;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public Integer getNetwork() {
    return network;
  }

  public void setNetwork(Integer network) {
    this.network = network;
  }

  public String getEnvProcess() {
    return envProcess;
  }

  public void setEnvProcess(String envProcess) {
    this.envProcess = envProcess;
  }

  public List<String> getSecrets() {
    return secrets;
  }

  public void setSecrets(List<String> secrets) {
    this.secrets = secrets;
  }

  public String getRegistryAuth() {
    return registryAuth;
  }

  public void setRegistryAuth(String registryAuth) {
    this.registryAuth = registryAuth;
  }

  public EnvVariable getEnvVars() {
    return envVars;
  }

  public void setEnvVars(EnvVariable envVars) {
    this.envVars = envVars;
  }

}
