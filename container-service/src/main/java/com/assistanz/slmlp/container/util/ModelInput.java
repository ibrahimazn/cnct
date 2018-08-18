/**
 * 
 */
package com.assistanz.slmlp.container.util;

/**
 * @author testuser
 *
 */
public class ModelInput {

  private String dataset;

  private String splitratio;

  private String modelLocation;

  public String getSplitratio() {
    return splitratio;
  }

  public void setSplitratio(String splitratio) {
    this.splitratio = splitratio;
  }

  public String getDataset() {
    return dataset;
  }

  public void setDataset(String dataset) {
    this.dataset = dataset;
  }

  public String getModelLocation() {
    return modelLocation;
  }

  public void setModelLocation(String modelLocation) {
    this.modelLocation = modelLocation;
  }

}
