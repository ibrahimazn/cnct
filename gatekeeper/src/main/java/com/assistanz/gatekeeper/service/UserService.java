package com.assistanz.gatekeeper.service;

import java.util.List;
import org.springframework.data.domain.Page;

import com.assistanz.gatekeeper.model.User;
import com.assistanz.gatekeeper.util.sorting.PagingAndSorting;

/** Service Interface for the User. */
public interface UserService extends CRUDService<User> {

    
     /**
      * Find User by email.
      *
      * @param email user email
      * @return user
      */
      User findByEmail(String email);
      
      /**
       * Find by username.
       *
       * @param userName user name
       * @return user
       */
       User findByUsername(String userName);

     /**
      * Method tolist all the availvle active Users.
      *
      * @return users list
      */
     List<User> findAllByIsActive();

     /**
      * List active users with pagination.
      *
      * @param pagingAndSorting pagination and sorting
      * @param active active status
      * @return user list
      * @throws Exception if any error occurs
      */
     Page<User> findAllByIsActive(PagingAndSorting pagingAndSorting, Integer active) throws Exception;

}
