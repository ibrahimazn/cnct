/*
 * 
 */
package com.ancode.service.account.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.ancode.service.account.entity.OfflineDeployment;

/**
 * The Interface OfflineDeploymentRepository.
 */
public interface OfflineDeploymentRepository extends PagingAndSortingRepository<OfflineDeployment, Long> {

}
