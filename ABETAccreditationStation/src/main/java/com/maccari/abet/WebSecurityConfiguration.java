package com.maccari.abet;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
	@Autowired 
	private DataSource dataSource;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().dataSource(dataSource)
			.passwordEncoder(passwordEncoder)
			.usersByUsernameQuery("SELECT email, password, active FROM account"
					+ " WHERE email=?")
			.authoritiesByUsernameQuery("SELECT email, role FROM authority"
					+ " WHERE email=?");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/", "/home", "/login", "/403").permitAll()
			.antMatchers("/task/index").hasAnyRole("FACULTY")
			.antMatchers("/task/index", "/task/create", "/task/viewCreated").hasAnyRole("ASSESSMENT_COORD")
			.antMatchers("/doc/index").hasAnyRole("ABET_READER")
			.antMatchers("/manage", "/register", "/home/edit", "/task/index").hasAnyRole("ADMIN")
			.and().formLogin().loginPage("/login").defaultSuccessUrl("/")
			.and().exceptionHandling().accessDeniedPage("/403");
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}