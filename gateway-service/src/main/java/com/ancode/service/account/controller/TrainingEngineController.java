/*
 * 
 */
package com.ancode.service.account.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ancode.service.account.entity.TrainingEngine;
import com.ancode.service.account.service.TrainingEngineService;
import com.ancode.service.account.util.ApiController;
import com.ancode.service.account.util.CRUDController;
import com.wordnik.swagger.annotations.Api;

/**
 * The Class TrainingEngineController.
 */
@RestController
@RequestMapping("/api/trainingengine")
@Api(value = "TrainingEngine", description = "Operations with TrainingEngine", produces = "application/json")
public class TrainingEngineController extends CRUDController<TrainingEngine> implements ApiController {

  /** Service reference to TrainingEngine. */
  @Autowired
  private TrainingEngineService trainingEngineService;

  /**
   * List all TrainingEngines.
   *
   * @return trainingEngines.
   * @throws Exception
   *           unhandled errors.
   */
  @RequestMapping(value = "listbyisactive", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  protected List<TrainingEngine> findAllByActive() throws Exception {
    return trainingEngineService.findAllTrainingEnginesByIsActive(true);
  }
  
  /**
   * List all TrainingEngines.
   *
   * @return trainingEngines.
   * @throws Exception
   *           unhandled errors.
   */
  @RequestMapping(value = "listbytype", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  protected List<TrainingEngine> findAllByActiveAndType(@RequestParam String type) throws Exception {
    return trainingEngineService.findAllTrainingEnginesByIsActiveAndType(true, type);
  }
}
