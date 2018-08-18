/**
 * This project objective is Server Less Machine Learning Platform.
 * This project is developing for ancode developers and it provides PAAS.
 */
package com.assistanz.slmlp.container.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.assistanz.slmlp.container.entity.Volume;
import com.assistanz.slmlp.container.service.VolumeService;
import com.assistanz.slmlp.container.util.ApiController;
import com.assistanz.slmlp.container.util.CRUDController;
import com.wordnik.swagger.annotations.Api;

/**
 * Volume controller will expose REST API for create user volume, and will give
 * pvc for launchers to attach user mount point.
 *
 */
@RestController
@RequestMapping("/api/volumes")
@Api(value = "Namespace", description = "Operations with Volume", produces = "application/json")
public class VolumeController extends CRUDController<Volume> implements ApiController {

  /** Service reference to volume. */
  @Autowired
  private VolumeService volumeService;

  @Override
  public Volume create(Volume volume) throws Exception {
    return volumeService.save(volume);
  }

  @Override
  public Volume update(Volume volume, Long id) throws Exception {
    return volumeService.update(volume);
  }

  @Override
  public void delete(Long id) throws Exception {
    volumeService.delete(id);
  }

  @Override
  public Volume read(Long id) throws Exception {
    return super.read(id);
  }

  @Override
  public List<Volume> list(String sortBy, String range, Integer limit, HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    return volumeService.findAll();
  }

}
