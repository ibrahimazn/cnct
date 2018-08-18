/*
 * 
 */
package com.ancode.service.account.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.ancode.service.account.entity.DataSetConfiguration;

/**
 * The Interface DataSetSourceRepository.
 */
public interface DataSetSourceRepository extends PagingAndSortingRepository<DataSetConfiguration, Long> {

}
