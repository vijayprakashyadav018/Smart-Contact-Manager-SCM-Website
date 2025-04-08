package com.scm.config;


import java.beans.Customizer;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.scm.services.impl.SecurityCustomUserDetailService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;



@Configuration
public class SecurityConfig {
  
	// user create and login using java code with in memory
//	@Bean
//	public UserDetailsService userDetailsService() {
//		
//	UserDetails user1 =	User
//			.withDefaultPasswordEncoder()
//			.username("admin123")
//			.password("admin123")
//			.roles("ADMIN","USER")
//			.build();
//	
//	  UserDetails user2 =  User.withDefaultPasswordEncoder().username("user123").password("password").build();
//		
//		var inMemoryUserDetailsManager = new InMemoryUserDetailsManager(user1,user2);
//		return inMemoryUserDetailsManager;
//	}
	
	@Autowired
	private SecurityCustomUserDetailService userDetailService;
	
	
	@Autowired
	private OAuthAuthenticationSuccessHandler handler;
	
	@Autowired
	private AuthFailureHandler authFailureHandler;
	
	
	// configuration of authentication provider spring security
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider =new DaoAuthenticationProvider();
		//user detail service ka object :
		daoAuthenticationProvider.setUserDetailsService(userDetailService);
		// password encoder ka object 
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder() );
		return daoAuthenticationProvider;
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		
		// configuration 
		
		//URLS configure kiya hai ki konsi public rahenge aur konsi private rahenge
		httpSecurity.authorizeHttpRequests(authorize->{
			//authorize.requestMatchers("/home","/signup","/services").permitAll();
		     authorize.requestMatchers("/user/**").authenticated();
		     authorize.anyRequest().permitAll();
		});
		
		//form default login 
		//agr hame kuch bhi change krna hua to hum yaha ayenge: form login se related
		httpSecurity.formLogin(formLogin-> {
			
			
			formLogin.loginPage("/login");
			formLogin.loginProcessingUrl("/authenticate");
			formLogin.successForwardUrl("/user/profile");
			// formLogin.failureForwardUrl("/login?error=true");
			
			formLogin.usernameParameter("email");
			formLogin.passwordParameter("password");
//			formLogin.failureHandler(new AuthenticationFailureHandler() {
//				
//				@Override
//				public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
//						AuthenticationException exception) throws IOException, ServletException {
//					// TODO Auto-generated method stub
//					throw new UnsupportedOperationException("unimplemented method");
//				}
//			} );
//			
//			formLogin.successHandler(new AuthenticationSuccessHandler() {
//				
//				@Override
//				public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
//						Authentication authentication) throws IOException, ServletException {
//					// TODO Auto-generated method stub
//					throw new UnsupportedOperationException("unimplemented method");
//				}
//			});
			
			formLogin.failureHandler(authFailureHandler);
		});
		
		httpSecurity.csrf(AbstractHttpConfigurer::disable);
		httpSecurity.logout(logoutForm->{
			logoutForm.logoutUrl("/logout");
			logoutForm.logoutSuccessUrl("/login?logout=true");
		});
		
		
		// oauth configurations
		
		httpSecurity.oauth2Login(oauth ->{
			oauth.loginPage("/login");
			oauth.successHandler(handler);
		});
		
		 return httpSecurity.build();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
}
