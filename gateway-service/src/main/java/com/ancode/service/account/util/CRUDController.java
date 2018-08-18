/*
 * 
 */
package com.ancode.service.account.util;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Generic Controller to handle REST and common actions for Application.
 *
 * @param <T>
 *          the generic type
 */
public class CRUDController<T> extends ExceptionHandlingController {

  /**
   * REST action to create an entity from JSON object.
   *
   * @param t
   *          - The generic entity to create
   * @return The entity in JSON based on the Accept headers
   * @throws Exception
   *           in case of any errors
   */
  @RequestMapping(method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = {
      MediaType.APPLICATION_JSON_VALUE })
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public T create(@RequestBody T t) throws Exception {
    return createT(t);
  }

  /**
   * REST action to update an entity from JSON object.
   *
   * @param t
   *          - The generic entity to update
   * @param id
   *          - Id of the entity to update
   * @return The entity in JSON based on the Accept headers
   * @throws Exception
   *           if internal error occurs.
   */
  @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = {
      MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
  @ResponseStatus(HttpStatus.ACCEPTED)
  @ResponseBody
  public T update(@RequestBody T t, @PathVariable("id") Long id) throws Exception {
    return updateT(id, t);
  }

  /**
   * REST action to delete entity.
   *
   * @param id
   *          - Id of the entity to delete
   * @throws Exception
   *           if internal error occurs.
   */
  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = { MediaType.APPLICATION_JSON_VALUE })
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable("id") Long id) throws Exception {
    deleteT(id);
  }

  /**
   * REST action to get entity.
   *
   * @param id
   *          - Id of the entity to read
   * @return The entity value
   * @throws Exception
   *           if internal error occurs.
   */
  @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public T read(@PathVariable("id") Long id) throws Exception {
    return get(id);
  }

  /**
   * REST action to get list of entities.
   *
   * @param sortBy
   *          example request param: sort(+name) - for Ascending order,
   *          sort(-name) - for Descending order
   * @param range
   *          example request header: Range: items=0-9
   * @param limit
   *          example request param: 25
   * @param request
   *          to set range
   * @param response
   *          to set range
   * @return list example response header: Content-Range: items 0-9/4500
   * @throws Exception
   *           if any issue occurs
   */
  @RequestMapping(method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<T> list(@RequestParam String sortBy, @RequestHeader(value = "Range") String range,
      @RequestParam(required = false) Integer limit, HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    return null;
  }

  /**
   * Protected create method.
   *
   * @param t
   *          - The generic entity for create
   * @return entity object
   * @throws Exception
   *           if any issue occurs
   */
  protected T createT(T t) throws Exception {
    return t;
  }

  /**
   * Protected updated method.
   *
   * @param id
   *          - Id of the entity
   * @param t
   *          - The generic entity for update
   * @return entity object
   * @throws Exception
   *           if any issue occurs
   */
  protected T updateT(Long id, T t) throws Exception {
    return t;
  }

  /**
   * Protected delete method.
   *
   * @param id
   *          - Id of the entity to delete
   * @throws Exception
   *           if any issue occurs
   */
  protected void deleteT(Long id) throws Exception {
    // Do nothing
  }

  /**
   * Protected get method.
   *
   * @param id
   *          - Id of the entity to get
   * @return entity object
   * @throws Exception
   *           if any issue occurs
   */
  protected T get(Long id) throws Exception {
    return null;
  }

  /**
   * Protected get method.
   *
   * @return entity list
   * @throws Exception
   *           if any issue occurs
   */
  protected List<T> getAll() throws Exception {
    return null;
  }
}
