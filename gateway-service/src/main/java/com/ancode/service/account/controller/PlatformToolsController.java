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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ancode.service.account.entity.PlatformTools;
import com.ancode.service.account.service.PlatformToolsService;
import com.ancode.service.account.util.ApiController;
import com.ancode.service.account.util.CRUDController;
import com.wordnik.swagger.annotations.Api;

/**
 * The Class PlatformToolsController.
 */
@RestController
@RequestMapping("/api/platformtools")
@Api(value = "PlatformTools", description = "Operations with PlatformTools", produces = "application/json")
public class PlatformToolsController extends CRUDController<PlatformTools> implements ApiController {

  /** Service reference to PlatformTools. */
  @Autowired
  private PlatformToolsService platformToolsService;

  /**
   * List all PlatformTools.
   *
   * @return PlatformTools.
   * @throws Exception
   *           unhandled errors.
   */
  @RequestMapping(value = "listbyisactive", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  protected List<PlatformTools> findAllByActive() throws Exception {
    return platformToolsService.findAllPlatformToolsByIsActive(true);
  }
}
