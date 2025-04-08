package com.scm.controllers;



import java.security.MessageDigest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;

import com.scm.entities.User;
import com.scm.forms.UserForm;
import com.scm.helpers.Message;
import com.scm.helpers.MessageType;
import com.scm.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;


@Controller
public class PageController {

	@Autowired
	private UserService userService;
	
	@GetMapping("/")
	public String index() {
		return "redirect:/home";
	}
	
	@RequestMapping("/home")
	public String home(Model model ) {
		
		System.out.println("home page handler");
		
		//sending data to view
		model.addAttribute("name", "Substring Technologies");
		model.addAttribute("youtubechannel", "Gaming School");
		model.addAttribute("channellink", "https://www.youtube.com/watch?v=HexFqifusOk&list=PLY0XN_peiGxgh7sI1WPic5xjyc4F2mBAv&index=90");
		return "home";

	}
	// about us (route)
   @RequestMapping("/about")
   public String aboutPage(Model model) {
	   model.addAttribute("isLogin",true);
	   System.out.println("about page loading");
	   return "about";
   }
 
	
	// services
   @RequestMapping("/services")
   public String servicesPage() {
	   System.out.println("services page loading");
	   return "services";
   }
   
   
   // contact page
   @GetMapping("/contact")
   public String contact() {
       return new String("contact");
   }

   
   // Login page (this is showing login page)
   @GetMapping("/login")
   public String login() {
       return new String("login");
   }
   
   
   //Signup page (registration page)
   @GetMapping("/signup")
   public String signup(Model model) {
	   
	   UserForm userForm = new UserForm();
	   // default data bhi daal sakte hai
	    //  userForm.setName("vijay yadav");
	     // userForm.setAbout("this about :write something about yourself");
	   model.addAttribute("userForm",userForm);
	   
       return "signup";
   }
   
  //processing signup(signup ka data get kringe  )(process registration)
   @RequestMapping(value ="/do-register", method = RequestMethod.POST)
   public String processRegister(@Valid @ModelAttribute UserForm userForm,BindingResult rBindingResult, HttpSession session) {
	   System.out.println("Processing signup");
	   //fetch form data
	     //UserForm
	      System.out.println(userForm);
	   
	   //validate form data 
	      //TO-DO::Validate userForm
	      if (rBindingResult.hasErrors()) {
	    	  return "signup";
	      }
	   //save to database 
	      //user service 
	      //UserForm -->User
	   //   User user = User.builder().name(userForm.getName()).email(userForm.getEmail()).password(userForm.getPassword()).about(userForm.getAbout()).phoneNumber(userForm.getPhoneNumber()).profilePic("https://img.freepik.com/premium-vector/default-avatar-profile-icon-social-media-user-image-gray-avatar-icon-blank-profile-silhouette-vector-illustration_561158-3383.jpg?semt=ais_hybrid").build();
	    
	      User user = new User();
	      user.setAbout(userForm.getAbout());
	      user.setEmail(userForm.getEmail());
	      user.setPassword(userForm.getPassword());
	      user.setName(userForm.getName());
	      user.setEnabled(false);
	      user.setPhoneNumber(userForm.getPhoneNumber());
	      user.setProfilePic("https://img.freepik.com/premium-vector/default-avatar-profile-icon-social-media-user-image-gray-avatar-icon-blank-profile-silhouette-vector-illustration_561158-3383.jpg?semt=ais_hybrid");
	      
	      User savedUser = userService.saveUser(user);
	     
	     System.out.println("user saved:");
	   //message ="Registration Successful"
	   // add the message: 
	     
	   Message message =  Message.builder().content("Registration Successful").type(MessageType.green).build();
	     
	     session.setAttribute("message", message);
	   //redirect to login page
	   return "redirect:/signup";
   }
  
 
}
