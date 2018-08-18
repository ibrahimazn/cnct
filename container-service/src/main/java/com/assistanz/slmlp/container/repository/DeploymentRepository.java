/**
 * This project objective is Server Less Machine Learning Platform.
 * This project is developing for ancode developers and it provides PAAS.
 */
package com.assistanz.slmlp.container.repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import com.assistanz.slmlp.container.entity.Deployment;

public interface DeploymentRepository
    extends PagingAndSortingRepository<com.assistanz.slmlp.container.entity.Deployment, Long> {

  @Query(value = "SELECT deployment FROM Deployment deployment WHERE deployment.name = :name AND deployment.isActive = :isActive")
  Deployment findByName(@Param("name") String name, @Param("isActive") Boolean active);

  @Query(value = "SELECT deployment FROM Deployment deployment WHERE deployment.isActive = :isActive")
  List<Deployment> findAllByActive(@Param("isActive") Boolean active);

  
  @Query(value = "SELECT deployment FROM Deployment deployment WHERE deployment.isActive = :isActive AND deployment.userId = :userId AND deployment.type = :type")
  List<Deployment> findAllByActiveAndUserAndType(@Param("isActive") Boolean active, @Param("userId") Long userId,
      @Param("type") String type);

  /**
   * Find all deployments by user and in active projects.
   *
   * @param active true/false.
   * @param userId the userId.
   * @param type the launcher.
   * @param projectIds active projectIds.
   * @return deployments.
   */
  @Query(value = "SELECT deployment FROM Deployment deployment WHERE deployment.isActive = :isActive AND deployment.userId = :userId AND deployment.projectId in :projectIds")
  List<Deployment> findAllByActiveUserIdAndProjects(@Param("isActive") Boolean active, @Param("userId") Long userId,
       @Param("projectIds") List<Long> projectIds);


  @Query(value = "SELECT deployment FROM Deployment deployment WHERE deployment.namespaceId = :namespaceId "
      + " AND deployment.isActive = :isActive")
  Deployment findByNameSpaceIdAndIsActive(@Param("namespaceId") Long namespaceId, @Param("isActive") Boolean active);

  @Query(value = "SELECT deployment FROM Deployment deployment WHERE deployment.uid = :uid AND deployment.isActive = :isActive")
  Deployment findByUidAndIsActive(@Param("uid") String uid, @Param("isActive") Boolean active);

  @Query(value = "SELECT deployment FROM Deployment deployment WHERE deployment.message LIKE %:replicaSetName% AND deployment.isActive = :isActive")
  Deployment findByReplicaSetName(@Param("replicaSetName") String replicaSetName, @Param("isActive") Boolean active);

  /**
   * Find all deployments by active user.
   * @param active true or false.
   * @param userId the userId.
   * @return deployments.
   */
  @Query(value = "SELECT deployment FROM Deployment deployment WHERE deployment.isActive = :isActive AND deployment.userId = :userId")
  List<Deployment> findAllByActiveAndUser(@Param("isActive") Boolean active, @Param("userId") Long userId);
}
