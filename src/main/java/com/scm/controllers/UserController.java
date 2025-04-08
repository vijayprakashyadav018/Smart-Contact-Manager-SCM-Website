package com.scm.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.scm.entities.User;
import com.scm.helpers.Helper;
import com.scm.services.UserService;



@Controller
@RequestMapping("/user")
public class UserController {

	
	private Logger logger =LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private UserService userService;
	
	
	// user dash-board page
	
	 @RequestMapping(value = "/dashboard")
	 public String userDashboard() {
	 	 return "user/dashboard";
	 }
	 
	// user profile page
		
     @RequestMapping(value = "/profile")
		 public String userProfile(Model model,Authentication authentication) {
    	    
    	String username = Helper.getEmailOfLoggedInUser(authentication);
    	 
    	     logger.info("User logged in:{}",username);
    	     
    	     //database se data ko fetch : get user from db
    	     
    	    User user= userService.getUserByEmail(username);
    	     
    	     System.out.println(user.getName());
    	     System.out.println(user.getEmail());
    	     
    	     model.addAttribute("loggedInUser",user);
    	     
		 	 return "user/profile";
		 }
	
	// user add contacts page 
	
	// user view contacts 
	
	// user edit contact 
	
	// user delete contact
	
	// user search contact
}
