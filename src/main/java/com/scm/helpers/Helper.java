package com.scm.helpers;



import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;





public class Helper {
 
	public static String getEmailOfLoggedInUser(Authentication authentication) {
		
		
	
		//agar email is password se login kiya hai to : email kaise nikalenge
		
		if (authentication instanceof OAuth2AuthenticationToken) {
			
		
			var aOAuth2AuthenticationToken = (OAuth2AuthenticationToken)authentication;
			var clientId= aOAuth2AuthenticationToken.getAuthorizedClientRegistrationId();
			
			var oauth2User=(OAuth2User)authentication.getPrincipal();
			String username="";
			
			if (clientId.equalsIgnoreCase("google")) {
				
				
				//signin with google kiya hai to : email kaise nikalenge
				System.out.println("getting email from google");
				username=oauth2User.getAttribute("email").toString();
			}
			else if (clientId.equalsIgnoreCase("github")) {
				
				//signin with github kiya hai to : email kaise nikalenge
				System.out.println("getting email from github");
				username= oauth2User.getAttribute("email") !=null ? oauth2User.getAttribute("email").toString() : oauth2User.getAttribute("login").toString()+"@gmail.com";
			}
			
		
			return username;
		
		}
		else {
			System.out.println("getting data from local database");
			return authentication.getName();
		}
			
		
	}
  

	public static String getLinkForEmailVerification(String emailToken) {
		
		String link = "http://localhost:8081/auth/verify-email?token=" + emailToken;
		
		return link;
	}
	
}
