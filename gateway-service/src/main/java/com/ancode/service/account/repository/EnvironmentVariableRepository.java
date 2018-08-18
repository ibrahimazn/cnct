/*
 * 
 */
package com.ancode.service.account.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.ancode.service.account.entity.EnvironmentVariable;

/**
 * The Interface EnvironmentVariableRepository.
 */
public interface EnvironmentVariableRepository extends PagingAndSortingRepository<EnvironmentVariable, Long> {

}
