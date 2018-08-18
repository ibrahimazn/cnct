/*
 *
 */
package com.ancode.service.account.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.ancode.service.account.constants.GenericConstants;
import com.ancode.service.account.entity.Department;
import com.ancode.service.account.error.exception.ApplicationException;
import com.ancode.service.account.repository.DepartmentRepository;
import com.ancode.service.account.util.AppValidator;
import com.ancode.service.account.util.error.Errors;
import com.ancode.service.account.util.sorting.PagingAndSorting;

/**
 * The Class DepartmentServiceImpl.
 */
@Service
public class DepartmentServiceImpl implements DepartmentService {

  /** DepartmentRepository reference. */
  @Autowired
  private DepartmentRepository departmentRepo;

  /** Constant for Department. */
  private static final String DEPARTMENT = "department";

  /** Validator attribute. */
  @Autowired
  private AppValidator validator;

  @Override
  public Department save(Department department) throws Exception {
    this.validateDepartment(department);
    Errors errors = validator.rejectIfNullEntity(DEPARTMENT, department);
    errors = validator.validateEntity(department, errors);
    if (errors.hasErrors()) {
      throw new ApplicationException(errors);
    }
    department.setIsActive(true);
    return departmentRepo.save(department);
  }

  @Override
  public Department update(Department department) throws Exception {
    Errors errors = validator.rejectIfNullEntity(DEPARTMENT, department);
    errors = validator.validateEntity(department, errors);
    if (errors.hasErrors()) {
      throw new ApplicationException(errors);
    }
    return departmentRepo.save(department);
  }

  @Override
  public void delete(Department department) throws Exception {
    departmentRepo.delete(department);
  }

  @Override
  public void delete(Long id) throws Exception {
    departmentRepo.delete(id);
  }

  @Override
  public Department softDelete(Department department) throws Exception {
    department.setIsActive(false);
    return departmentRepo.save(department);
  }

  @Override
  public Department find(Long id) throws Exception {
    return departmentRepo.findOne(id);
  }

  @Override
  public Page<Department> findAll(PagingAndSorting pagingAndSorting) throws Exception {
    return departmentRepo.findAll(pagingAndSorting.toPageRequest());
  }

  @Override
  public List<Department> findAll() throws Exception {
    return (List<Department>) departmentRepo.findAll();
  }

  @Override
  public Page<Department> findAllByIsActive(PagingAndSorting pagingAndSorting, Boolean isActive) throws Exception {
    return departmentRepo.findAllByIsActive(pagingAndSorting.toPageRequest(), isActive);
  }

  @Override
  public Page<Department> findAllBySearchText(PagingAndSorting pagingAndSorting, String searchText) throws Exception {
    return departmentRepo.findBySearchText(pagingAndSorting.toPageRequest(), searchText, true);
  }

  /**
   * Validate the department.
   *
   * @param department
   *          reference of the department.
   * @throws Exception
   *           error occurs.
   */
  private void validateDepartment(Department department) throws Exception {
    Errors errors = validator.rejectIfNullEntity(DEPARTMENT, department);
    errors = validator.validateEntity(department, errors);
    Department departments = departmentRepo.findByNameAndIsActive(department.getName(), true);
    if (departments != null && !departments.getId().equals(department.getId())) {
      errors.addFieldError(GenericConstants.NAME, "error.department.already.exist");
    }
    if (errors.hasErrors()) {
      throw new ApplicationException(errors);
    }
  }

  @Override
  public List<Department> findAllDepartmentsByIsActive(Boolean isActive) throws Exception {
    return departmentRepo.findAllDepartmentsByIsActive(isActive);
  }
}
