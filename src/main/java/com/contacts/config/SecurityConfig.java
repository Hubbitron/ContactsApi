package com.contacts.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.contacts.filter.JwtRequestFilter;
import com.contacts.helper.PasswordEncoderBypass;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	PasswordEncoder passwordEncoder() {
		return new PasswordEncoderBypass();
	}
	
	@Autowired
	JwtRequestFilter jwtRequestFilter;
	
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}
	
	
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	    http
	    	.formLogin(AbstractHttpConfigurer::disable)
            .csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configure(http))
            .securityMatcher("/api/**")
            .authorizeHttpRequests(auth -> auth
            		
        		.requestMatchers(
    				"/api/update"
        			,"/api/getStates"
        			,"/api/getSingle/{id:\\d+}"
        			,"/api/delete/{id:\\d+}"
        			,"/api/update"
        			,"/api/insert"
    				,"/api/getAll"
    				,"/api/getUser"
    				,"/api/getProfilePic/{id:\\d+}"   				

        		).hasAnyAuthority("1")
        		
        		.requestMatchers(
    				"/api/getAll"
    				,"/api/getProfilePic/{id:\\d+}"   				
        		).hasAnyAuthority("2")
        		
        		.requestMatchers(
    				"/api/authenticate"
				).permitAll()
        		
        		.anyRequest().authenticated()
    		);
	    
	    http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
	    http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
		
	    return http.build();
	}
}
