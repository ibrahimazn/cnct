package com.assistanz.slmlp.container.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.assistanz.slmlp.container.entity.Model;
import com.assistanz.slmlp.container.entity.TrainingJob;

public interface TrainingJobRepository extends PagingAndSortingRepository<TrainingJob, Long> {

  /**
   * Find all the training active training jobs.
   *
   * @param pageable
   *          pagination object with limit and sorting.
   * @param isActive
   *          boolean variable for getting active records.
   * @return training jobs.
   */
  @Query(value = "SELECT trainingjob FROM TrainingJob trainingjob WHERE trainingjob.isActive = :isActive")
  Page<TrainingJob> findAllByIsActive(Pageable pageable, @Param("isActive") Boolean isActive);

  /**
   * Find all by is active and model.
   *
   * @param pageable
   *          the pageable
   * @param isActive
   *          the is active
   * @param model
   *          the model
   * @return the page
   */
  @Query(value = "SELECT trainingjob FROM TrainingJob trainingjob WHERE trainingjob.isActive = :isActive AND :model MEMBER OF trainingjob.models)")
  Page<TrainingJob> findAllByIsActiveAndModel(Pageable pageable, @Param("isActive") Boolean isActive,
      @Param("model") Model model);

  /**
   * Find all by active and model.
   *
   * @param isActive the is active
   * @param model the model
   * @return the list
   */
  @Query(value = "SELECT trainingjob FROM TrainingJob trainingjob WHERE trainingjob.isActive = :isActive AND :model MEMBER OF trainingjob.models)")
  List<TrainingJob> findAllByActiveAndModel(@Param("isActive") Boolean isActive, @Param("model") Model model);

  /**
   * Find by job name and is active.
   *
   * @param name
   *          the name
   * @param isActive
   *          the is active
   * @return the training job
   */
  @Query(value = "SELECT trainingjob FROM TrainingJob trainingjob WHERE trainingjob.jobName = :name AND trainingjob.isActive = :isActive")
  TrainingJob findByJobNameAndIsActive(@Param("name") String name, @Param("isActive") Boolean isActive);
}
