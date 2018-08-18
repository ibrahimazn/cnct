/**
 * This project objective is Server Less Machine Learning Platform.
 * This project is developing for ancode developers and it provides PAAS.
 */
package com.assistanz.slmlp.container.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.assistanz.slmlp.container.entity.OpenFaasFunctions;

public interface OpenFaasRepository extends PagingAndSortingRepository<OpenFaasFunctions, Long> {

  @Query(value = "SELECT function FROM OpenFaasFunctions function WHERE function.isActive = :isActive AND function.functionType = :functionType AND function.modelId = :modelId")
  OpenFaasFunctions findByModelAndIsActiveAndType(@Param("modelId") Long modelId, @Param("isActive") Boolean isActive,
      @Param("functionType") String functionType);
  
  @Query(value = "SELECT function FROM OpenFaasFunctions function WHERE function.isActive = :isActive AND function.modelId = :modelId")
  List<OpenFaasFunctions> findByModelAndIsActive(@Param("modelId") Long modelId, @Param("isActive") Boolean isActive);

}
