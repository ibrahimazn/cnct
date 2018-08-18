/**
 * This project objective is Server Less Machine Learning Platform.
 * This project is developing for ancode developers and it provides PAAS.
 */
package com.assistanz.slmlp.container.service;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.assistanz.slmlp.container.constants.GenericConstants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import az.ancode.kubectl.connector.kubernetes.KafkaKubeEvent;

/**
 * The Class KafkaConsumerService.
 */
@Service
public class KafkaConsumerService {

  /** The Constant log. */
  private static final Logger log = LoggerFactory.getLogger(KafkaConsumerService.class);

  /** The deployment service. */
  @Autowired
  private DeploymentService deploymentService;

  /** The volume service. */
  @Autowired
  private VolumeService volumeService;

  /**
   * On message.
   *
   * @param message the message
   * @throws JSONException the JSON exception
   */
  @KafkaListener(topics = "${kafka.topic}")
  public void onMessage(String message) throws JSONException {
    KafkaKubeEvent responseReference = new KafkaKubeEvent();
    try {
      Gson gson = new GsonBuilder().create();
      JsonParser parser = new JsonParser();
      JsonObject resoponseJson = parser.parse(message).getAsJsonObject();
      String resoponseForGson = resoponseJson.get("EventValue").getAsString();
      JsonObject eventValueObject = parser.parse(resoponseForGson).getAsJsonObject();
      String responseJson = eventValueObject.toString();

      responseReference = gson.fromJson(responseJson, KafkaKubeEvent.class);

      handleEventAction(responseReference);
    } catch (Exception e) {
    }

    // log.info("received content = '{}'", jsonObj);
  }
  
  /**
   * On message for training engines status.
   *
   * @param message the message
   * @throws JSONException the JSON exception
   */
  @KafkaListener(topics = "${kafka.tensor.topic}")
  public void onMessageListener(String message) throws JSONException {
    try {
      JsonParser parser = new JsonParser();
      JsonObject resoponseJson = parser.parse(message).getAsJsonObject();
      log.debug("received content ", resoponseJson.getAsString());
    } catch (Exception e) {
    }

    // log.info("received content = '{}'", jsonObj);
  }

  /**
   * Handle event action.
   *
   * @param event the event
   * @throws Exception the exception
   */
  public void handleEventAction(KafkaKubeEvent event) throws Exception {
    String kindString = event.getInvolvedObject().getKind();
    String kindName = kindString.toUpperCase();
    switch (kindName) {
    case GenericConstants.DEPLOYMENT:
    case GenericConstants.REPLICASET:
    case GenericConstants.POD:
      deploymentService.updateKafkaEvents(event, kindName);
      break;
    case GenericConstants.PERSISTENTVOLUMECLAIM:
      volumeService.updateKafkaEvents(event, kindName);
      break;
    default:
      break;

    }

  }
}
