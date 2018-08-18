package com.assistanz.slmlp.container.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.assistanz.slmlp.container.entity.Model;
import com.assistanz.slmlp.container.entity.TrainingJob;
import com.assistanz.slmlp.container.util.service.CRUDService;
import com.assistanz.slmlp.container.util.sorting.PagingAndSorting;

@Service
public interface TrainingJobService extends CRUDService<TrainingJob> {

  /**
   * Find all the training jobs by pagination.
   *
   * @param pagingAndSorting
   *          pagination object,
   * @param isActive
   *          active record check variable.
   * @return training jobs list.
   * @throws Exception
   *           if error.
   */
  Page<TrainingJob> findAllByIsActive(PagingAndSorting pagingAndSorting, Boolean isActive) throws Exception;

  /**
   * Find all the training jobs by pagination and model.
   *
   * @param pagingAndSorting
   *          pagination object,
   * @param modelId
   *          model id.
   * @param isActive
   *          active record check variable.
   * @return training jobs list.
   * @throws Exception
   *           if error.
   */
  Page<TrainingJob> findAllByIsActiveAndModel(PagingAndSorting pagingAndSorting, Boolean isActive, Long modelId)
      throws Exception;

  /**
   * Start the training jobs.
   *
   * @param trainingJob
   * @return
   * @throws Exception
   */
  TrainingJob startTraining(TrainingJob trainingJob, Long userId, String userName) throws Exception;

  /**
   * Find by model id and is active.
   *
   * @param active the active
   * @param model the model
   * @return the list
   * @throws Exception the exception
   */
  List<TrainingJob> findByModelIdAndIsActive(Boolean active, Model model) throws Exception;

  /**
   * Gets the by job name.
   *
   * @param jobName
   *          the job name
   * @return the by job name
   * @throws Exception
   *           the exception
   */
  TrainingJob getByJobName(String jobName) throws Exception;

}
