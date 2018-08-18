/*
 * 
 */
package com.assistanz.slmlp.container.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.assistanz.slmlp.container.entity.Model;
import com.assistanz.slmlp.container.entity.ModelVersion;
import com.assistanz.slmlp.container.service.ModelVersionService;
import com.assistanz.slmlp.container.util.ApiController;
import com.assistanz.slmlp.container.util.CRUDController;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;


/**
 * The Class ModelVersionController.
 */
@RestController
@RequestMapping("/api/modelversions")
@Api(value = "Model Versions", description = "Operations with ModelVersion", produces = "application/json")
public class ModelVersionController extends CRUDController<ModelVersion> implements ApiController {

  /** Service reference to modelversion. */
  @Autowired
  private ModelVersionService modelService;

  @ApiOperation(value = SW_METHOD_CREATE, notes = "Create a new ModelVersion.", response = Model.class)
  @Override
  public ModelVersion create(@RequestBody ModelVersion modelVersion) throws Exception {
    return modelService.save(modelVersion);
  }

  @ApiOperation(value = SW_METHOD_UPDATE, notes = "Update an existing ModelVersion.", response = Model.class)
  @Override
  public ModelVersion update(@RequestBody ModelVersion modelVersion, @PathVariable(PATH_ID) Long id) throws Exception {
    return modelService.update(modelVersion);
  }
  
  @RequestMapping(value = "listbymodel", method = RequestMethod.GET, produces = {
      MediaType.APPLICATION_JSON_VALUE })
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<ModelVersion> findByProject(@RequestParam Long modelId) throws Exception {
    return modelService.findAllByIsActiveAndModel(true,modelId);
  }

}
