/*
 * 
 */
package com.ancode.service.account.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.ancode.service.account.entity.Launcher;

/**
 * The Interface LauncherRepository.
 */
public interface LauncherRepository extends PagingAndSortingRepository<Launcher, Long> {

  /**
   * Find all by isActive.
   * 
   * @param isActive
   *          true.
   * @return launcherList.
   */
  @Query(value = "SELECT launcher FROM Launcher launcher WHERE launcher.isActive = :isActive")
  List<Launcher> findAllLaunchersByIsActive(@Param("isActive") Boolean isActive);
  
  /**
   * Find all by isActive.
   * 
   * @param isActive
   *          true.
   * @return launcherList.
   */
  @Query(value = "SELECT launcher FROM Launcher launcher WHERE launcher.isActive = :isActive AND launcher.type =:type")
  List<Launcher> findAllLaunchersByIsActiveAndType(@Param("isActive") Boolean isActive, @Param("type") String type);
  
  /**
   * Find by name and by isActive.
   * 
   * @param name
   *          name.
   * @param isActive
   *          true.
   * @return launcher.
   */
  @Query(value = "SELECT launcher FROM Launcher launcher WHERE launcher.name = :name and launcher.isActive = :isActive")
  Launcher findByNameAndIsActive(@Param("name") String name, @Param("isActive") Boolean isActive);
}
