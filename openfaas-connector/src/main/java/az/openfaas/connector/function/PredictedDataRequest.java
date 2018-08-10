package az.openfaas.connector.function;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import az.openfaas.connector.Request;

/**
 * Function to deploy.
 *
 * eg:- API : /system/functions Method : POST Headers : {Content-Type :
 * application/json}
 *
 * Body : { service: string network: string image: string envProcess: string
 * envVars: [] secrets: [] registryAuth: string }
 *
 * Success Response:
 *
 *
 * Error Response:
 *
 *
 */
public class PredictedDataRequest extends Request {

	@JsonProperty("dataset")
	private String dataset;

	@JsonProperty("modelLocation")
	private String modelLocation;

	@JsonProperty("targetFileLocation")
	private String targetFileLocation;

	/**
	 * @return the dataset
	 */
	public String getDataset() {
		return dataset;
	}

	/**
	 * @param dataset
	 *            the dataset to set
	 */
	public void setDataset(String dataset) {
		this.dataset = dataset;
	}

	/**
	 * @return the modelLocation
	 */
	public String getModelLocation() {
		return modelLocation;
	}

	/**
	 * @param modelLocation
	 *            the modelLocation to set
	 */
	public void setModelLocation(String modelLocation) {
		this.modelLocation = modelLocation;
	}

	/**
	 * @return the targetFileLocation
	 */
	public String getTargetFileLocation() {
		return targetFileLocation;
	}

	/**
	 * @param targetFileLocation
	 *            the targetFileLocation to set
	 */
	public void setTargetFileLocation(String targetFileLocation) {
		this.targetFileLocation = targetFileLocation;
	}

}
