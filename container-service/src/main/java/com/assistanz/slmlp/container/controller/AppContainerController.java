/**
 * This project objective is Server Less Machine Learning Platform.
 * This project is developing for ancode developers and it provides PAAS.
 */
package com.assistanz.slmlp.container.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.assistanz.slmlp.container.entity.AppContainer;
import com.assistanz.slmlp.container.service.ContainerService;
import com.assistanz.slmlp.container.util.ApiController;
import com.assistanz.slmlp.container.util.CRUDController;
import com.wordnik.swagger.annotations.Api;

/**
 * AppContainer controller is for the CRUD operations of various ancode
 * application containers.
 *
 */
@RestController
@RequestMapping("/api/containers")
@Api(value = "AppContainer", description = "Operations with AppContainer", produces = "application/json")
public class AppContainerController extends CRUDController<AppContainer> implements ApiController {

    /** Service reference to Container. */
    @Autowired
    private ContainerService containerService;

    @Override
    public AppContainer create(AppContainer launcher) throws Exception {
        return containerService.save(launcher);
    }

    @Override
    public AppContainer update(AppContainer launcher, Long id) throws Exception {
        return containerService.update(launcher);
    }

    @Override
    public void delete(Long id) throws Exception {
        containerService.delete(id);
    }

    @Override
    public AppContainer read(Long id) throws Exception {
        return containerService.find(id);
    }

    /**
     * List all active containers.
     * 
     * @param request HttpServletRequest.
     * @param response HttpServletResponse.
     * @param userId userId.
     * @throws Exception if error occurs.
     * @return the all launchers
     */
    @RequestMapping(value = "/listbyactive", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE })
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<AppContainer> getAllLaunchers(HttpServletRequest request, HttpServletResponse response,@RequestHeader(required = false) String userId) throws Exception {
        return containerService.findAll();
    }

}
