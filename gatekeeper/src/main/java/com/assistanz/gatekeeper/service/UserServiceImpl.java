package com.assistanz.gatekeeper.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.assistanz.gatekeeper.constants.GenericConstants;
import com.assistanz.gatekeeper.model.Role;
import com.assistanz.gatekeeper.model.User;
import com.assistanz.gatekeeper.repository.UserRepository;
import com.assistanz.gatekeeper.util.AppValidator;
import com.assistanz.gatekeeper.util.EncryptionUtil;
import com.assistanz.gatekeeper.util.error.Errors;
import com.assistanz.gatekeeper.util.error.exception.ApplicationException;
import com.assistanz.gatekeeper.util.error.exception.CustomGenericException;
import com.assistanz.gatekeeper.util.sorting.PagingAndSorting;
import java.util.Base64;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * User service implementation class.
 */
@Service("userService")
public class UserServiceImpl implements UserService {

	/** Constant for user. */
    public static final String USER = "user";

	/** Validator attribute. */
    @Autowired
    private AppValidator validator;

    /** Reference for User Repository .*/
    @Autowired
    private UserRepository userRepository;

    /** Reference for Role service .*/
    @Autowired
    private RoleService roleService;

    /** Reference for Ldap service .*/
    @Autowired
    private LdapService ldapService;

    /** Reference for BCrypt Password Encoder.*/
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    /* Secret key value is append. */
    @Value(value = "${aes.salt.secretKey}")private String secretKey;

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User findByUsername(String userName) {
        return userRepository.findByUsername(userName);
    }

    @Override
    public User save(User user) throws Exception, CustomGenericException {
    	Errors errors = validator.rejectIfNullEntity(USER, user);
        errors = validator.validateEntity(user, errors);
        errors = this.validateUserExist(errors, user.getUserName());
        errors = this.validateEmail(errors, user.getEmail());
        if (errors.hasErrors()) {
            // 1.1. If there is entity mismatch then it throws error.
          if (errors.getGlobalError() != null) {
            throw new Exception(errors.getGlobalError().get(0));
          }
          if (errors.getFieldErrors() != null) {
            throw new  Exception(errors.getFieldErrors().get(0));
          }
        } else {
	        user.setActive(1);
	        List<Role> userRole = user.getRoles();
	        List<Role> roleList = new ArrayList<Role>();
	        if ( userRole != null && roleList != null ) {
	        for (Role role : userRole) {
	          Role roleObj = roleService.find(role.getId());
	          roleList.add(roleObj);
	        }
	        user.setRoles(roleList);
	        }
	        ldapService.saveUser(user);
	        String strEncoded = Base64.getEncoder().encodeToString(secretKey.getBytes(GenericConstants.CHARACTER_ENCODING));
	        byte[] decodedKey = Base64.getDecoder().decode(strEncoded);
	        SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, GenericConstants.ENCRYPT_ALGORITHM);
	        String encryptedPassword = new String(EncryptionUtil.encrypt(user.getPassword(), originalKey));
	        user.setPassword(encryptedPassword);
	        user = userRepository.save(user);
        }
        return user;
    }

    private Errors validateUserExist(Errors errors, String userName) throws Exception {
        if (this.findByUsername(userName) != null) {
            errors.addGlobalError("Username already exist");
        }
        return errors;
    }

    /**
     * Check the user email already exist.
     *
     * @param errors email validation error object
     * @param email email address
     * @param status user status
     * @return error object
     * @throws Exception raise if error
     */
    private Errors validateEmail(Errors errors, String email) throws Exception {
        if (userRepository.findByEmailId(email) != null) {
            errors.addGlobalError("Email.already.exist");
        }
        return errors;
    }

    @Override
    public User update(User user) throws Exception {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setActive(1);
        return userRepository.save(user);
    }

    @Override
    public User softDelete(User user) throws Exception {
        user.setActive(0);
        ldapService.deleteUser(user);
        return userRepository.save(user);
    }

	@Override
	public void delete(User user) throws Exception {
		userRepository.delete(user);

	}

	@Override
	public void delete(Long id) throws Exception {
		userRepository.delete(id);
	}

	@Override
	public User find(Long id) throws Exception {
		return userRepository.findOne(id);
	}

	@Override
	public Page<User> findAll(PagingAndSorting pagingAndSorting) throws Exception {
		return userRepository.findAll(pagingAndSorting.toPageRequest());
	}

	@Override
	public List<User> findAll() throws Exception {
		return (List<User>) userRepository.findAll();
	}

	@Override
    public List<User> findAllByIsActive() {
         return userRepository.findUserByActive(1);
    }

    @Override
    public Page<User> findAllByIsActive(PagingAndSorting pagingAndSorting, Integer active) throws Exception {
        return userRepository.findByUserIsActive(pagingAndSorting.toPageRequest(), 1);
    }

}
