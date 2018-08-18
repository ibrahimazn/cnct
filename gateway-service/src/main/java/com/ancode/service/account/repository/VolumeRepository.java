/*
 * 
 */
package com.ancode.service.account.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.ancode.service.account.entity.Volume;


/**
 * The Interface Volume.
 */
public interface VolumeRepository extends PagingAndSortingRepository<Volume, Long> {

  /**
   * Find by name space.
   *
   * @param namespace the namespace
   * @return the list
   */
  @Query(value = "SELECT volume FROM Volume volume WHERE volume.namespace = :namespace")
  List<Volume> findByNameSpace(@Param("namespace") String namespace);
}
