/*
 *
 */
package com.ancode.service.account.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.ancode.service.account.constants.GenericConstants;
import com.ancode.service.account.entity.Department;
import com.ancode.service.account.entity.User;
import com.ancode.service.account.entity.User.UserType;
import com.ancode.service.account.error.exception.CustomGenericException;
import com.ancode.service.account.service.UserService;
import com.ancode.service.account.util.ApiController;
import com.ancode.service.account.util.CRUDController;
import com.ancode.service.account.util.KubernetesService;
import com.ancode.service.account.util.sorting.PagingAndSorting;
import com.wordnik.swagger.annotations.ApiOperation;

/**
 * User Controller.
 */
@RestController
@RequestMapping("/api/user")
public class UserController extends CRUDController<User> implements ApiController {
  /** Service reference for User. */
  @Autowired
  private UserService userService;


  /** Service reference for Kubernetes. */
  @Autowired
  private KubernetesService k8sService;

  /** Background image directory. */
  @Value("${profile.image.dir}")
  private String backgroundLogoDir;

  /** The secret key. */
  @Value(value = "${aes.salt.secretKey}")
  private String secretKey;
  
  /** Grafana URL. */
  @Value(value = "${grafana.url}")
  private String grafanaUrl;
  
  /** Grafana Port. */
  @Value(value = "${grafana.port}")
  private String grafanaPort;

  @ApiOperation(value = SW_METHOD_CREATE, notes = "Create a new user.", response = User.class)
  @Override
  public User create(@RequestBody User user) throws Exception {
    return userService.save(user);
  }

  @ApiOperation(value = SW_METHOD_READ, notes = "Read an existing user.", response = User.class)
  @Override
  public User read(@PathVariable(PATH_ID) Long id) throws Exception {
    return userService.find(id);
  }

  @ApiOperation(value = SW_METHOD_UPDATE, notes = "Update an existing user.", response = User.class)
  @Override
  public User update(@RequestBody User user, @PathVariable(PATH_ID) Long id) throws Exception {
    return userService.update(user);
  }

  /**
   * Soft deleting the user from the table.
   *
   * @param user
   *          user object.
   * @param id
   *          user's id.
   * @throws Exception
   *           unhandled errors.
   */
  @ApiOperation(value = SW_METHOD_DELETE, notes = "Delete an existing user.")
  @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE, produces = {
      MediaType.APPLICATION_JSON_VALUE })
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void softDelete(@RequestBody User user, @PathVariable(PATH_ID) Long id) throws Exception {
    /** Doing Soft delete from the user table. */
    userService.softDelete(user);
  }

  /**
   * Find all with pagination.
   *
   * @param sortBy
   *          id.
   * @param range
   *          range.
   * @param limit
   *          limit.
   * @param request
   *          http request.
   * @param response
   *          http response.
   * @return dpartments.
   * @throws Exception
   *           if error occurs.
   */
  @RequestMapping(value = "list", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<User> listView(@RequestParam String sortBy, @RequestHeader(value = RANGE) String range,
      @RequestParam(required = false) Integer limit, HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    PagingAndSorting page = new PagingAndSorting(range, sortBy, limit, User.class);
    Page<User> pageResponse = userService.findAllByIsActive(page, true);
    response.setHeader(GenericConstants.CONTENT_RANGE_HEADER, page.getPageHeaderValue(pageResponse));
    return pageResponse.getContent();
  }

  /**
   * Find by search text.
   *
   * @param userType
   *          the user type
   * @param sortBy
   *          sortBy.
   * @param range
   *          range.
   * @param limit
   *          limit.
   * @param searchText
   *          searchText.
   * @param request
   *          httpRequest.
   * @param response
   *          httpResponse.
   * @return users.
   * @throws Exception
   *           if error occurs.
   */
  @RequestMapping(value = "findbysearchtext", method = RequestMethod.GET, produces = {
      MediaType.APPLICATION_JSON_VALUE })
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<User> findBySearchText(@RequestParam String userType, @RequestParam String sortBy,
      @RequestHeader(value = RANGE) String range, @RequestParam(required = false) Integer limit,
      @RequestParam String searchText, HttpServletRequest request, HttpServletResponse response) throws Exception {
    sortBy = "-id";
    PagingAndSorting page = new PagingAndSorting(range, sortBy, limit, User.class);
    Page<User> pageResponse = null;
    if(userType.equalsIgnoreCase("undefined") || userType.equalsIgnoreCase("null")) {
      pageResponse = userService.findAllBySearchText(page, searchText);
    } else {
      pageResponse = userService.findAllBySearchTextWithUserType(page, searchText, userType);
    }
    response.setHeader(GenericConstants.CONTENT_RANGE_HEADER, page.getPageHeaderValue(pageResponse));
    return pageResponse.getContent();
  }

  /**
   * Save user.
   *
   * @param user
   *          the user
   * @param profileImgFile
   *          the profile img file
   * @return the user
   * @throws Exception
   *           the exception
   */
  @RequestMapping(value = "saveuser", method = RequestMethod.POST, produces = {
      MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
  @ResponseStatus(HttpStatus.ACCEPTED)
  @ResponseBody
  public User saveUser(@RequestBody User user, @RequestParam(value = "profileImgFile") Object profileImgFile)
      throws Exception {
    return null;
  }

  /**
   * Save theme setting details.
   *
   * @param departmentId
   *          departmentId.
   * @param email
   *          email.
   * @param firstName
   *          firstName.
   * @param lastName
   *          lastName.
   * @param mobilePhone
   *          mobilePhone.
   * @param password
   *          password.
   * @param userType
   *          userType.
   * @param userName
   *          userName.
   * @param profileImgFile
   *          profileImgFile.
   * @return user.
   * @throws Exception
   *           if error.
   */
  @RequestMapping(value = "/save", method = RequestMethod.POST)
  @ResponseBody
  public User handleFileUpload(@RequestParam(value = "departmentId") Long departmentId,
      @RequestParam(required=false, value = "email") String email, @RequestParam(value = "firstName") String firstName,
      @RequestParam(value = "lastName") String lastName, @RequestParam(required=false, value = "mobilePhone") String mobilePhone,
      @RequestParam(value = "password") String password, @RequestParam(value = "userType") String userType,
      @RequestParam(value = "userName") String userName,
      @RequestParam(value = "profileImgFile") MultipartFile[] profileImgFile) throws Exception {
    User user = new User();
    if (departmentId != null) {
      user.setDepartmentId(departmentId);
    }
    if (email != null) {
      user.setEmail(email);
    }
    if (firstName != null) {
      user.setFirstName(firstName);
    }
    if (lastName != null) {
      user.setLastName(lastName);
    }
    if (password != null) {
      user.setPassword(password);
    }
    if (mobilePhone != null) {
      user.setPhone(mobilePhone);
    }
    if (userName != null) {
      user.setUserName(userName);
    }
    if (profileImgFile != null) {
      user = uploadProfileImageFile(user, profileImgFile);
    }
    user.setUserType(UserType.valueOf(userType.toUpperCase()));
    user.setActive(1);
    user = userService.save(user);
    return user;
  }

  /**
   * Save theme setting details.
   *
   * @param id
   *          the id
   * @param departmentId
   *          departmentId.
   * @param email
   *          email.
   * @param firstName
   *          firstName.
   * @param lastName
   *          lastName.
   * @param mobilePhone
   *          mobilePhone.
   * @param password
   *          password.
   * @param userType
   *          userType.
   * @param userName
   *          userName.
   * @param profileImgFile
   *          profileImgFile.
   * @return user.
   * @throws Exception
   *           if error.
   */
  @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
  @ResponseBody
  public User updateUser(@PathVariable(PATH_ID) Long id, @RequestParam(value = "departmentId") Long departmentId,
      @RequestParam(required=false, value = "email") String email, @RequestParam(value = "firstName") String firstName,
      @RequestParam(value = "lastName") String lastName, @RequestParam(required=false, value = "mobilePhone") String mobilePhone,
      @RequestParam(value = "password") String password, @RequestParam(value = "userType") String userType,
      @RequestParam(value = "userName") String userName,
      @RequestParam(value = "profileImgFile") MultipartFile[] profileImgFile) throws Exception {
    User user = new User();
    if (id != null) {
      user.setId(id);
    }
    if (departmentId != null) {
      user.setDepartmentId(departmentId);
    }
    if (email != null) {
      user.setEmail(email);
    }
    if (firstName != null) {
      user.setFirstName(firstName);
    }
    if (lastName != null) {
      user.setLastName(lastName);
    }
    if (password != null) {
      user.setPassword(password);
    }
    if (mobilePhone != null) {
      user.setPhone(mobilePhone);
    }
    if (userName != null) {
      user.setUserName(userName);
    }
    if (profileImgFile != null) {
      user = uploadProfileImageFile(user, profileImgFile);
    }

    user.setUserType(UserType.valueOf(userType.toUpperCase()));
    user.setActive(1);
    user = userService.update(user);
    return user;
  }

  /**
   * Update the application logo image.
   *
   * @param user
   *          the user
   * @param profileImgFile
   *          the profile img file
   * @return theme settings
   * @throws IOException
   *           if error.
   */
  private User uploadProfileImageFile(User user, MultipartFile[] profileImgFile) throws IOException {
    InputStream inputStream = null;
    OutputStream outputStream = null;
    int i = 0;
    BufferedImage image = null;
    if (profileImgFile != null) {
      for (MultipartFile file : profileImgFile) {
        i++;
        if ((profileImgFile.length == 1) || (profileImgFile.length > 1) && i == 2) {
          String fileName = "user_logo-" + user.getUserName() + ".jpg";
          File newFile = new File(backgroundLogoDir + "/" + "user_logo-" + user.getUserName() + ".jpg");
          image = ImageIO.read(file.getInputStream());
          int height = image.getHeight();
          int width = image.getWidth();
          if (width <= 120 && height <= 120) {
            user.setProfileImgPath(backgroundLogoDir);
            user.setProfileImgFile(fileName);
            try {
              inputStream = file.getInputStream();
              if (!newFile.exists()) {
                newFile.createNewFile();
              }
              outputStream = new FileOutputStream(newFile);
              int read = 0;
              byte[] bytes = new byte[1024];

              while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
              }
            } catch (IOException e) {
              e.printStackTrace();
            }
            newFile.getAbsolutePath();
          } else {
            throw new CustomGenericException(GenericConstants.NOT_IMPLEMENTED,
                "Profile image resolution should be smaller than or equal to " + 120 + "x" + 120);
          }
        }
      }
    }
    return user;
  }

  /**
   * Method to list all the available user lists.
   *
   * @return user list
   * @throws Exception
   *           if any error occurs
   */
  @RequestMapping(value = "listbyisactive", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  protected List<User> findAllByIsActive() throws Exception {
    return userService.findAllByIsActive();
  }

  @RequestMapping(value = "launchfunction", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  protected User launchFunction() throws Exception {
    Authentication auth = authentication();
    User user = userService.findByUserNameAndIsActive(auth.getName(), 1);
    String userName = k8sService.convertNameToK8sFromat(user.getUserName());
    return userService.createFunction(user.getId(), userName);
  }

  /**
   * Authentication.
   *
   * @return the authentication
   */
  private Authentication authentication() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return authentication;
  }
  
  @RequestMapping(value = "findactiveuser", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public User findActiveUser() throws Exception {
    Authentication auth = authentication();
    User user = userService.findByUserNameAndIsActive(auth.getName(), 1);
    user.setGrafanaUrl(grafanaUrl+":"+grafanaPort);
    return user;
  }
}
