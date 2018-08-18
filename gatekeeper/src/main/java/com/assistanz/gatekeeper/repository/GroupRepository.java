package com.assistanz.gatekeeper.repository;

import java.util.List;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.assistanz.gatekeeper.model.Group;

/**
 * Group repository for get data from database.
 *
 */
@Repository
public interface GroupRepository extends PagingAndSortingRepository<Group, Long> {

    /**
     * List the Groups.
     *
     * @return list of groups.
     */
    @Query(value = "select group from Group group")
    List<Group> getGroupList();

}
