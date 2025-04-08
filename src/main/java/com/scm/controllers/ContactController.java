package com.scm.controllers;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.scm.entities.Contact;
import com.scm.entities.User;
import com.scm.forms.ContactForm;
import com.scm.forms.ContactSearchForm;
import com.scm.helpers.AppConstants;
import com.scm.helpers.Helper;
import com.scm.helpers.Message;
import com.scm.helpers.MessageType;
import com.scm.services.ContactService;
import com.scm.services.ImageService;
import com.scm.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/user/contacts")
public class ContactController {

	private Logger logger= LoggerFactory.getLogger(ContactController.class);
	
	@Autowired
	private ContactService contactService;
	
	@Autowired
	private ImageService imageService;
	
	@Autowired
	private UserService userService;
	
	@RequestMapping("/add")
	//add contact page :handler
	public String addContactView(Model model) {
		
		
		ContactForm contactForm = new ContactForm();
		model.addAttribute("contactForm",contactForm);
		//contactForm.setFavorite(true);
		//contactForm.setName("vijay yadav"); // yha hamne object me string pass kr dii back-end se
		return "user/add_contact";
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String saveContact(@Valid @ModelAttribute ContactForm contactForm,BindingResult result,Authentication authentication,HttpSession session) {
		
		//process the form data 
		
		//validate form
		
		//TO-DO : add validate logic here
		if (result.hasErrors()) {
			
			// ye error ko print kr dega console pe jisse hum error ko read krke uska pta lgaenge
			result.getAllErrors().forEach(error -> logger.info(error.toString()));
			
			
			session.setAttribute("message",Message.builder()
					.content("Please correct the following errors")
					.type(MessageType.red)
					.build() );
			return "user/add_contact";
		}
		
		
		
		String username = Helper.getEmailOfLoggedInUser(authentication);
		
		// form --> contact
  	    User user =	 userService.getUserByEmail(username);
		
  	    // process the contact picture
  	    
  	    // image process
		
  	    //image ki information print krvane ke liye console pe (data process check krne ke liye)
  	    logger.info("file information :{}",contactForm.getContactImage().getOriginalFilename());
  	    
  	    // Upload krne ka code 
  	    
  	   
		 Contact contact =  new Contact();
		 contact.setName(contactForm.getName());
		 contact.setFavorite(contactForm.getFavorite());
		 contact.setEmail(contactForm.getEmail());
		 contact.setPhoneNumber(contactForm.getPhoneNumber());
		 contact.setAddress(contactForm.getAddress());
		 contact.setDescription(contactForm.getDescription());
		 contact.setUser(user);
		 contact.setLinkedInLink(contactForm.getLinkedInLink());
		 contact.setWebsiteLink(contactForm.getWebsiteLink());
		 
		if (contactForm.getContactImage() != null && !contactForm.getContactImage().isEmpty()) {
			
			 String filename = UUID.randomUUID().toString();   
		     String fileURL = imageService.uploadImage(contactForm.getContactImage(),filename);
		  	     
			 contact.setPicture(fileURL);
			 contact.setCloudinaryImagePublicId(filename);
		}
		 
		 
		// save contact to data-base
		contactService.save(contact);
		
		System.out.println(contactForm);
		
		// set the contact picture url
		
		// set the message to be displayed on the view
		 session.setAttribute("message", Message.builder()
					.content("You have successfully added a new contact")
					.type(MessageType.green)
					.build() );
		return "redirect:/user/contacts/add";
	}
	
	
	
	//view contacts 
	
	@RequestMapping
	public String viewContacts(
			@RequestParam(value="page",defaultValue = "0") int page,
			@RequestParam(value="size", defaultValue = AppConstants.PAGE_SIZE + "") int size,
			@RequestParam(value="sortBy", defaultValue = "name") String sortBy,
			@RequestParam(value="direction", defaultValue = "asc") String direction ,
			Model model, Authentication authentication) {
		
		//load all the user contacts 
		String username = Helper.getEmailOfLoggedInUser(authentication);
		
		User user = userService.getUserByEmail(username);
		
		// page wise contact ke liye 
		Page<Contact> pageContact = contactService.getByUser(user,page,size,sortBy,direction);
		
		model.addAttribute("pageContact", pageContact);
		model.addAttribute("pageSize", AppConstants.PAGE_SIZE);
		
		model.addAttribute("contactSearchForm", new ContactSearchForm());
		
		return "user/contacts";
	}
	
	//search handler 
	
	@RequestMapping("/search")
	public String searchHandler(
			@ModelAttribute ContactSearchForm contactSearchForm,
			@RequestParam(value= "size",defaultValue = AppConstants.PAGE_SIZE + "")int size,
			@RequestParam(value = "page",defaultValue = "0") int page,
			@RequestParam(value = "sortBy",defaultValue = "name") String sortBy,
			@RequestParam(value = "direction",defaultValue = "asc") String direction, 
			Model model,
			Authentication authentication
			) {
		
		logger.info("field {} keywork {}",contactSearchForm.getField(),contactSearchForm.getValue());
		
		var user=userService.getUserByEmail(Helper.getEmailOfLoggedInUser(authentication));
		
		Page<Contact> pageContact=null;
		if(contactSearchForm.getField().equalsIgnoreCase("name")) {
		 pageContact=contactService.searchByName(contactSearchForm.getValue(), size, page, sortBy, direction,user);
		}
		else if (contactSearchForm.getField().equalsIgnoreCase("email")) {
			pageContact = contactService.searchByEmail(contactSearchForm.getValue(), size, page, sortBy, direction,user);
		}
		else if (contactSearchForm.getField().equalsIgnoreCase("phone")) {
			pageContact = contactService.searchByPhoneNumber(contactSearchForm.getValue(), size, page, sortBy, direction,user);
		}
		
		logger.info("pageContact {}", pageContact);
		
		model.addAttribute("contactSearchForm", contactSearchForm);
		
		model.addAttribute("pageContact", pageContact);
		
		model.addAttribute("pageSize", AppConstants.PAGE_SIZE);
		
		return "user/search";
	}
	
	
	//delete contact
	@RequestMapping("/delete/{contactId}")
	public String deleteContact(@PathVariable("contactId") String contactId,HttpSession httpSession) {
		 contactService.delete(contactId);
		 logger.info("contactId{} deleted",contactId);
		 
		 httpSession.setAttribute("message", Message.builder()
				 .content("Contact is Deleted successfully !!")
				 .type(MessageType.green)
				 .build()
				 );
		 
		return "redirect:/user/contacts";
	}
	
	
	//update contact form view 
	@GetMapping("/view/{contactId}")
	public String updateContactFormView(@PathVariable("contactId") String contactId,
			Model model) {
		
		var contact = contactService.getById(contactId);
		
		ContactForm contactForm= new ContactForm();
		contactForm.setName(contact.getName());
		contactForm.setEmail(contact.getEmail());
		contactForm.setPhoneNumber(contact.getPhoneNumber());
		contactForm.setAddress(contact.getAddress());
		contactForm.setDescription(contact.getDescription());
		contactForm.setFavorite(contact.isFavorite());
		contactForm.setPicture(contact.getPicture());
		contactForm.setWebsiteLink(contact.getWebsiteLink());
		contactForm.setLinkedInLink(contact.getLinkedInLink());
		
		model.addAttribute("contactForm", contactForm);
		model.addAttribute("contactId", contactId);
		
		return "user/update_contact_view";
	}
	
	
	@RequestMapping(value="/update/{contactId}",method = RequestMethod.POST)
	public String updateContact(@PathVariable("contactId") String contactId,@Valid @ModelAttribute ContactForm contactForm
			,BindingResult bindingResult
			,Model model) {
		
		//update the contact
		if(bindingResult.hasErrors()) {
			return "user/update_contact_view";
		}
		
		//(agar update karenge to data update ho jayega wrna jo phle vala hai database me wo show ho jayega )
		var con = contactService.getById(contactId);
		con.setId(contactId);
		con.setName(contactForm.getName());
		con.setEmail(contactForm.getEmail());
		con.setPhoneNumber(contactForm.getPhoneNumber());
		con.setAddress(contactForm.getAddress());
		con.setDescription(contactForm.getDescription());
		con.setFavorite(contactForm.getFavorite());
		con.setWebsiteLink(contactForm.getWebsiteLink());
		con.setLinkedInLink(contactForm.getLinkedInLink());
		 
		
		// process updated picture
	if (contactForm.getContactImage() != null && !contactForm.getContactImage().isEmpty()) 
	 {	
		logger.info("file is not empty");
		String  fileName = UUID.randomUUID().toString();
		String imageUrl = imageService.uploadImage(contactForm.getContactImage() , fileName);
		con.setCloudinaryImagePublicId(fileName);
		con.setPicture(imageUrl);
		contactForm.setPicture(imageUrl);
		
	 }
	else {
		logger.info("file is empty");
	}
		var updateCon= contactService.update(con);
		logger.info("updated contact {}", updateCon);
		
		model.addAttribute("message", Message.builder().content("Contact Updated !!").type(MessageType.green).build());
		
		
		return "redirect:/user/contacts/view/" + contactId ;
	}
	
}



