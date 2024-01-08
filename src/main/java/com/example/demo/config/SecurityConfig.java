package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.example.demo.config.*;


import jakarta.servlet.Filter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private UserAuthenticationProvider userAuthenticationPrivider;
	private UserAuthenticationEntryPoint userAutehnticationEntryPoint;
	
	public SecurityConfig(UserAuthenticationProvider userAuthenticationPrivider,
			UserAuthenticationEntryPoint userAutehnticationEntryPoint) {
		super();
		this.userAuthenticationPrivider = userAuthenticationPrivider;
		this.userAutehnticationEntryPoint = userAutehnticationEntryPoint;
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
		http
		  .exceptionHandling().authenticationEntryPoint(userAutehnticationEntryPoint)
		  .and()
		  .addFilterBefore(new JwtAuthFilter(userAuthenticationPrivider), BasicAuthenticationFilter.class)
          .csrf().disable()
		  .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		  .and()
		  .authorizeHttpRequests((requests)-> requests
				  .requestMatchers("/login","/register").permitAll()
				  .anyRequest().authenticated()
				  
				  );
		  
		return http.build();
	}
	
	
}
