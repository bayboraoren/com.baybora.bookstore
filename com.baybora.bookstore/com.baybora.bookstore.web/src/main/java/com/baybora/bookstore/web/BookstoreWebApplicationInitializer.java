package com.baybora.bookstore.web;


import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.web.SpringServletContainerInitializer;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import com.baybora.bookstore.repository.config.RepositoryConfiguration;
import com.baybora.bookstore.repository.config.TestDataContextConfiguration;
import com.baybora.bookstore.service.config.ServiceConfiguration;
import com.baybora.bookstore.web.config.WebMvcContextConfiguration;
import com.baybora.bookstore.web.config.WebflowContextConfiguration;


/**
 * {@link WebApplicationInitializer} that will be called by Spring's {@link SpringServletContainerInitializer} as part
 * of the JEE {@link ServletContainerInit	ializer} pattern. This class will be called on application startup and will
 * configure our JEE and Spring configuration.
 * <p/>
 * 
 * It will first initializes our {@link AnnotationConfigWebApplicationContext} with the common {@link Configuration}
 * classes: {@link RepositoryConfiguration} and {@link TestDataContextConfiguration} using a typical JEE
 * {@link ContextLoaderListener}.
 * <p/>
 * 
 * Next it creates a {@link DispatcherServlet}, being a normal JEE Servlet which will create on its turn a child
 * {@link AnnotationConfigWebApplicationContext} configured with the Spring MVC {@link Configuration} classes
 * {@link WebMvcContextConfiguration} and {@link WebflowContextConfiguration}. This Servlet will be registered using
 * JEE's programmatical API support.
 * <p/>
 * 
 * Note: the {@link OpenEntityManagerInViewFilter} is only enabled for pages served soley via Spring MVC. For pages
 * being served via WebFlow we configured WebFlow to use the JpaFlowExecutionListener.
 * 
 * @author Marten Deinum
 * @author Koen Serneels
 * 
 */
public class BookstoreWebApplicationInitializer implements WebApplicationInitializer {

	private static final Class<?>[] basicConfigurationClasses = new Class<?>[] {
			ServiceConfiguration.class, RepositoryConfiguration.class,TestDataContextConfiguration.class};

	private static final String DISPATCHER_SERVLET_NAME = "dispatcher";

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		registerListener(servletContext);
		registerDispatcherServlet(servletContext);
		registerH2Console(servletContext);
		// We are using JpaFlowExecutionListener instead, but we enable it for Spring MVC served pages
		registerOpenEntityManagerInViewFilter(servletContext);

	}
	
	 
    private void registerH2Console(ServletContext servletContext) {
        ServletRegistration.Dynamic h2Servlet = servletContext.addServlet("h2Servlet", new org.h2.server.web.WebServlet());
        h2Servlet.setLoadOnStartup(1);
        h2Servlet.addMapping("/h2/*");        
    }	

	private void registerDispatcherServlet(ServletContext servletContext) {
		AnnotationConfigWebApplicationContext dispatcherContext = createContext(WebMvcContextConfiguration.class,
				WebflowContextConfiguration.class);
		ServletRegistration.Dynamic dispatcher = servletContext.addServlet(DISPATCHER_SERVLET_NAME,
				new DispatcherServlet(dispatcherContext));
		dispatcher.setLoadOnStartup(1);
		dispatcher.addMapping("/");
	}

	private void registerListener(ServletContext servletContext) {
		AnnotationConfigWebApplicationContext rootContext = createContext(basicConfigurationClasses);
		servletContext.addListener(new ContextLoaderListener(rootContext));
		servletContext.addListener(new RequestContextListener());
	}

	private void registerOpenEntityManagerInViewFilter(ServletContext servletContext) {
		FilterRegistration.Dynamic registration = servletContext.addFilter("openEntityManagerInView",
				new OpenEntityManagerInViewFilter());
		registration.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD), false,
				"*.htm");
	}

	/**
	 * Factory method to create {@link AnnotationConfigWebApplicationContext} instances.
	 * @param annotatedClasses
	 * @return
	 */
	private AnnotationConfigWebApplicationContext createContext(final Class<?>... annotatedClasses) {
		AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
		// Registers the application configuration with the root context
		context.register(annotatedClasses);
		return context;
	}
}