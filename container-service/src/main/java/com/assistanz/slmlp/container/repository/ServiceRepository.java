/**
 * This project objective is Server Less Machine Learning Platform.
 * This project is developing for ancode developers and it provides PAAS.
 */
package com.assistanz.slmlp.container.repository;


import org.springframework.data.repository.PagingAndSortingRepository;
import com.assistanz.slmlp.container.entity.NodeServices;



public interface ServiceRepository extends PagingAndSortingRepository<NodeServices, Long> {

	
}