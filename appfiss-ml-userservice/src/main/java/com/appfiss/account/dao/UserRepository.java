/*
 * 
 */
package com.appfiss.account.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.appfiss.account.entity.User;
import com.appfiss.account.entity.User.UserType;

/**
 * User repository for manipulate data from user table.
 *
 */
@Repository("userRepository")
public interface UserRepository extends PagingAndSortingRepository<User, Long> {

  /**
   * Find by name of the user..
   *
   * @param userName
   *          the user name
   * @param active
   *          the active
   * @return user.
   */
  @Query(value = "SELECT user FROM User user WHERE user.userName = :userName AND user.active = :active")
  User findByNameAndIsActive(@Param("userName") String userName, @Param("active") int active);

  /**
   * Find all by isActive.
   *
   * @param pageable
   *          pageable.
   * @param active
   *          the active
   * @return userList.
   */
  @Query(value = "SELECT user FROM User user WHERE user.active = :active")
  Page<User> findAllByIsActive(Pageable pageable, @Param("active") boolean active);

  /**
   * Find user by search text.
   *
   * @param pageable
   *          page
   * @param search
   *          search text
   * @param active
   *          the active
   * @param userType
   *          the user type
   * @return user list
   */
  @Query(value = "SELECT user FROM User user WHERE user.active = :active AND user.userType = :userType AND (user.userName LIKE %:search%)")
  Page<User> findBySearchText(Pageable pageable, @Param("search") String search, @Param("active") boolean active,
      @Param("userType") UserType userType);
  
  /**
   * Find user by search text.
   *
   * @param pageable
   *          page
   * @param search
   *          search text
   * @param active
   *          the active
   * @return user list
   */
  @Query(value = "SELECT user FROM User user WHERE user.active = :active AND (user.userName LIKE %:search%)")
  Page<User> findBySearchText(Pageable pageable, @Param("search") String search, @Param("active") boolean active);

  /**
   * List all the active users.
   *
   * @param active
   *          active status
   * @return users list
   */
  List<User> findUserByActive(Integer active);

}
