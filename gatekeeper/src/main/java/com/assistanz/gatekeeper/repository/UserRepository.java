package com.assistanz.gatekeeper.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.assistanz.gatekeeper.model.User;

/**
 * User repository for get data from database.
 *
 */
@Repository("userRepository")
public interface UserRepository extends PagingAndSortingRepository<User, Long> {

    /**
     * Find user by the Email.
     *
     * @param email user email
     * @return user
     */
     User findByEmail(String email);

     /**
      * List all the active users.
      *
      * @param active active status
      * @return users list
      */
     List<User> findUserByActive(Integer active);

     /**
      * List active users with pagination.
      *
      * @param pageable page
      * @param active active status
      * @return user
      */
     @Query(value = "SELECT user FROM User user WHERE user.active = :active")
     Page<User> findByUserIsActive(Pageable pageable, @Param("active") Integer active);
     
     
     /**
      * Find user by the username.
      *
      * @param userName user name.
      * @return user
      */
     @Query(value = "SELECT user FROM User user WHERE user.active = 1 AND user.userName = :userName")
     User findByUsername(@Param("userName") String userName);
     
     /**
      * Find user by the emailId.
      *
      * @param email email.
      * @return user
      */
     @Query(value = "SELECT user FROM User user WHERE user.active = 1 AND user.email = :email")
     User findByEmailId(@Param("email") String email);

}
