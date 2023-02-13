package com.sixdee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "com.sixdee" })
public class LmsApuaNewApplication{
	
	public static void main(String[] args) {
		SpringApplication.run(LmsApuaNewApplication.class, args);

	}

	/*
	 * @SuppressWarnings("deprecation")
	 * 
	 * public WebMvcConfigurer corsConfigurer() { return new
	 * WebMvcConfigurerAdapter() {
	 * 
	 * @Override public void addCorsMappings(CorsRegistry registry) {
	 * registry.addMapping("/loyaltyMangamement").allowedOrigins("*"); } }; }
	 */

	/*
	 * @Bean public DispatcherServletRegistrationBean
	 * dispatcherServletRegistrationBean() { return new
	 * DispatcherServletRegistrationBean(dispatcherServlet(), "/"); }
	 * 
	 * @Bean public DispatcherServlet dispatcherServlet() { return new
	 * DispatcherServlet(); }
	 */

}
