package com.assistanz.gatekeeper.controller;

import java.security.Principal;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.assistanz.gatekeeper.constants.GenericConstants;
import com.assistanz.gatekeeper.model.User;
import com.assistanz.gatekeeper.service.UserService;
import com.assistanz.gatekeeper.util.sorting.PagingAndSorting;
import com.assistanz.gatekeeper.util.web.ApiController;
import com.assistanz.gatekeeper.util.web.CRUDController;

/**
 * Login Controller.
 */
@RestController
@RequestMapping("/api/user")
public class UserController extends CRUDController<User> implements ApiController {
	
    /** Service reference for User. */
    @Autowired
    private UserService userService;

    /**
     * Holder for both Model and View in the web MVC framework for login.
     *
     * @return modelview
     */
    @RequestMapping(value = {"/", "/login"},  method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE })
    public ModelAndView login() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }

    /**
     * Holder for both Model and View in the web MVC framework for Registration.
     *
     * @return modelview
     */
    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public ModelAndView registration() {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
        User user = new User();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("registration");
        } else {
            modelAndView.setViewName("login");
        }
        return modelAndView;
    }

    /**
     * Method for User Creation.
     *
     * @param user user
     * @param bindingResult binding result
     * @return user user
     * @throws Exception 
     */
    @RequestMapping(value = "/registrations", method = RequestMethod.POST)
    @ResponseBody
    public User createNewUser(@RequestBody User user, BindingResult bindingResult) throws Exception {

        ModelAndView modelAndView = new ModelAndView();
        User userExists = userService.findByUsername(user.getUserName());
        if (userExists != null) {
            bindingResult
                    .rejectValue("email", "error.user",
                            "There is already a user registered with the email provided");
        }
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("registration");
        } else {
            userService.save(user);
            modelAndView.addObject("successMessage", "User has been registered successfully");
            modelAndView.addObject("user", new User());
            modelAndView.setViewName("registration");

        }
        return user;
    }
    
    
    /**
     * Method for User Creation.
     *
     * @param user user
     * @param bindingResult binding result
     * @return user user
     * @throws Exception 
     */
    @Override
    public User create(@RequestBody User user) throws Exception {
        user = userService.save(user);
        return user;
    }


    /**
     * Method for Updating existing Users.
     *
     * @param user user
     * @return user
     * @throws Exception if any error occurs
     */
    @RequestMapping(value = "/updateUser", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE })
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public User updateUser(@RequestBody User user) throws Exception {
       return userService.update(user);
    }

    /**
     * Default Success URL for after login.
     *
     * @param principal user principal.
     * @return model and view
     */
    @RequestMapping(value = "/admin/home", method = RequestMethod.GET)
    public ModelAndView home(Principal principal) {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
//        User user = userService.findUserByEmail(auth.getName());
//        System.out.println("User Principal -" + principal.toString());
//        modelAndView.addObject("userName", "Welcome " + user.getName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
//        modelAndView.addObject("adminMessage","Content Available Only for Users with Admin Role");
        	modelAndView.setViewName("admin/home");
        } else {
            modelAndView.setViewName("login");
        }
        return modelAndView;
    }

    /**
     * Soft delete method to delete user.
     *
     * @param user user
     * @param id user id
     * @throws Exception if any error occurs.
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE, produces = {
            MediaType.APPLICATION_JSON_VALUE })
    public void softDelete(@PathVariable("id") Long id) throws Exception {
        User userObj = userService.find(id);
        userService.softDelete(userObj);
    }

    /**
     * Method to read the user object.
     *
     * @param id user id
     * @return user
     * @throws Exception if any error occurs
     */
    @RequestMapping(value = "/read/{id}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE })
    public User read(@PathVariable("id") Long id) throws Exception {
        return userService.find(id);
    }

    /**
     * Method to list all the available user lists.
     *
     * @return user list
     * @throws Exception if any error occurs
     */
    @RequestMapping(value = "list", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    protected List<User> findAllByIsActive() throws Exception {
        return userService.findAllByIsActive();
    }

    /**
     * List Users with Pagination.
     *
     * @param sortBy sort by fiels
     * @param range range
     * @param limit limit
     * @param request request
     * @param response response
     * @return user
     * @throws Exception if error occurs
     */
    @RequestMapping(value = "listview", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<User> listView(@RequestParam String sortBy, @RequestHeader(value = RANGE) String range,
            @RequestParam(required = false) Integer limit, HttpServletRequest request, HttpServletResponse response)
                    throws Exception {
        PagingAndSorting page = new PagingAndSorting(range, sortBy, limit, User.class);
        Page<User> pageResponse = userService.findAllByIsActive(page, 1);
        response.setHeader(GenericConstants.CONTENT_RANGE_HEADER, page.getPageHeaderValue(pageResponse));
        return pageResponse.getContent();
    }

}
