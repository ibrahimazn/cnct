/*
 *
 */
package com.assistanz.slmlp.container.controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.assistanz.slmlp.container.constants.GenericConstants;
import com.assistanz.slmlp.container.entity.TrainingJob;
import com.assistanz.slmlp.container.service.TrainingJobService;
import com.assistanz.slmlp.container.util.ApiController;
import com.assistanz.slmlp.container.util.CRUDController;
import com.assistanz.slmlp.container.util.sorting.PagingAndSorting;
import com.wordnik.swagger.annotations.Api;

/**
 * Training controller will expose REST API for create, list, stop etc.,
 * Also here we can train multiple models against the training job.
 *
 */
@RestController
@RequestMapping("/api/trainingjobs")
@Api(value = "Namespace", description = "Operations with TrainingJobs", produces = "application/json")
public class TrainingJobController extends CRUDController<TrainingJob> implements ApiController {

  /** Service reference to volume. */
  @Autowired
  private TrainingJobService trainingJobService;

  @Override
  public TrainingJob create(TrainingJob volume) throws Exception {
    return trainingJobService.save(volume);
  }

  @Override
  public TrainingJob update(TrainingJob volume, Long id) throws Exception {
    return trainingJobService.update(volume);
  }

  @Override
  public void delete(Long id) throws Exception {
    trainingJobService.delete(id);
  }

  @Override
  public TrainingJob read(Long id) throws Exception {
    return super.read(id);
  }

  @Override
  public List<TrainingJob> list(String sortBy, String range, Integer limit, HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    return trainingJobService.findAll();
  }

  @RequestMapping(value = "list", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<TrainingJob> listWithPagination(@RequestParam String sortBy,
      @RequestHeader(value = RANGE) String range, @RequestParam(required = false) Integer limit,
      HttpServletRequest request, HttpServletResponse response) throws Exception {
    PagingAndSorting page = new PagingAndSorting(range, sortBy, limit, TrainingJob.class);
    Page<TrainingJob> pageResponse = trainingJobService.findAllByIsActive(page, true);
    response.setHeader(GenericConstants.CONTENT_RANGE_HEADER, page.getPageHeaderValue(pageResponse));
    return pageResponse.getContent();
  }
  
  @RequestMapping(value = "listbymodel", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<TrainingJob> listByModelWithPagination(@RequestParam String sortBy, @RequestParam Long modelId,
      @RequestHeader(value = RANGE) String range, @RequestParam(required = false) Integer limit,
      HttpServletRequest request, HttpServletResponse response) throws Exception {
    PagingAndSorting page = new PagingAndSorting(range, sortBy, limit, TrainingJob.class);
    Page<TrainingJob> pageResponse = trainingJobService.findAllByIsActiveAndModel(page, true, modelId);
    response.setHeader(GenericConstants.CONTENT_RANGE_HEADER, page.getPageHeaderValue(pageResponse));
    return pageResponse.getContent();
  }

  /**
   * REST action to start the training against the dataset.
   *
   * @param trainingJob - The training job entity to train
   * @return The entity in JSON based on the Accept headers
   * @throws Exception in case of any errors
   */
  @RequestMapping(value = "startTraining", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = {
          MediaType.APPLICATION_JSON_VALUE })
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public TrainingJob startTraining(@RequestBody TrainingJob trainingJob,
		  @RequestHeader(required = false) Long userId,
	      @RequestHeader(required = false) String userName) throws Exception {
	  return trainingJobService.startTraining(trainingJob, userId, userName);
  }
}
