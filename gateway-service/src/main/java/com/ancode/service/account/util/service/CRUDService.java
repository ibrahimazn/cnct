/*
 * 
 */
package com.ancode.service.account.util.service;

import java.util.List;
import org.springframework.data.domain.Page;

import com.ancode.service.account.util.sorting.PagingAndSorting;

/**
 * Generic Service for Entity CRUD.
 *
 * @param <T>
 *          generic service
 */
public interface CRUDService<T> {

  /**
   * Generic method to save entity.
   *
   * @param t
   *          entity
   * @return saved entity
   * @throws Exception
   *           error occurs
   */
  T save(T t) throws Exception;

  /**
   * Generic method to update entity.
   *
   * @param t
   *          entity
   * @return updated entity
   * @throws Exception
   *           error occurs
   */
  T update(T t) throws Exception;

  /**
   * Generic method to delete entity.
   *
   * @param t
   *          entity
   * @throws Exception
   *           if error occurs
   */
  void delete(T t) throws Exception;

  /**
   * Generic method to delete entity.
   *
   * @param id
   *          of the entity
   * @throws Exception
   *           if error occurs
   */
  void delete(Long id) throws Exception;

  /**
   * Generic method to find entity.
   *
   * @param id
   *          of the entity
   * @return entity
   * @throws Exception
   *           if error occurs
   */
  T find(Long id) throws Exception;

  /**
   * Generic entity to find list of entities with paging.
   *
   * @param pagingAndSorting
   *          parameters
   * @return page result of entities
   * @throws Exception
   *           if error occurs
   */
  Page<T> findAll(PagingAndSorting pagingAndSorting) throws Exception;

  /**
   * Generic entity to find list of entities.
   *
   * @return result of entities
   * @throws Exception
   *           if error occurs.
   */
  List<T> findAll() throws Exception;

}
