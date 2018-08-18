/*
 * 
 */
package com.ancode.service.account.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ancode.service.account.entity.SourceRepo;
import com.ancode.service.account.service.SourceRepoService;
import com.ancode.service.account.util.ApiController;
import com.ancode.service.account.util.CRUDController;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

/**
 * The Class SourceRepoController.
 */
@RestController
@RequestMapping("/api/sourceRepo")
@Api(value = "SourceRepo", description = "Operations with SourceRepo", produces = "application/json")
public class SourceRepoController extends CRUDController<SourceRepo> implements ApiController {

  /** Service reference to SourceRepo. */
  @Autowired
  private SourceRepoService sourceRepoService;

  @ApiOperation(value = SW_METHOD_CREATE, notes = "Create a new SourceRepo.", response = SourceRepo.class)
  @Override
  public SourceRepo create(@RequestBody SourceRepo sourceRepo) throws Exception {
    return sourceRepoService.save(sourceRepo);
  }

  @ApiOperation(value = SW_METHOD_READ, notes = "Read an existing SourceRepo.", response = SourceRepo.class)
  @Override
  public SourceRepo read(@PathVariable(PATH_ID) Long id) throws Exception {
    return sourceRepoService.find(id);
  }

  @ApiOperation(value = SW_METHOD_UPDATE, notes = "Update an existing SourceRepo.", response = SourceRepo.class)
  @Override
  public SourceRepo update(@RequestBody SourceRepo sourceRepo, @PathVariable(PATH_ID) Long id) throws Exception {
    return sourceRepoService.update(sourceRepo);
  }

  /**
   * Soft deleting the SourceRepo from the table.
   *
   * @param sourceRepo
   *          the source repo
   * @param id
   *          SourceRepo's id.
   * @throws Exception
   *           unhandled errors.
   */
  @ApiOperation(value = SW_METHOD_DELETE, notes = "Delete an existing SourceRepo.")
  @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE, produces = {
      MediaType.APPLICATION_JSON_VALUE })
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void softDelete(@RequestBody SourceRepo sourceRepo, @PathVariable(PATH_ID) Long id) throws Exception {
    /** Doing Soft delete from the SourceRepo table. */
    sourceRepoService.softDelete(sourceRepo);
  }

  /**
   * List all SourceRepos.
   *
   * @return SourceRepos.
   * @throws Exception
   *           unhandled errors.
   */
  @RequestMapping(value = "listbyisactive", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  protected List<SourceRepo> findAllByActive() throws Exception {
    return sourceRepoService.findAllByIsActive(true);
  }
}
