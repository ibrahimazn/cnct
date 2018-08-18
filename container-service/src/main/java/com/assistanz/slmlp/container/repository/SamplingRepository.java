package com.assistanz.slmlp.container.repository;


import org.springframework.data.repository.PagingAndSortingRepository;
import com.assistanz.slmlp.container.entity.Sampling;


public interface SamplingRepository extends PagingAndSortingRepository<Sampling, Long> {

	
}