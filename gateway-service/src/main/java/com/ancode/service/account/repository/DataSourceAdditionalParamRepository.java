/*
 * 
 */
package com.ancode.service.account.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.ancode.service.account.entity.DataSourceAdditionalParam;

/**
 * The Interface DataSourceAdditionalParamRepository.
 */
public interface DataSourceAdditionalParamRepository
    extends PagingAndSortingRepository<DataSourceAdditionalParam, Long> {

}
