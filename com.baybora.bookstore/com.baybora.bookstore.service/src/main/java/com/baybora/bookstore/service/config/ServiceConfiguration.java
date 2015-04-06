package com.baybora.bookstore.service.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.baybora.bookstore.repository.config.RepositoryConfiguration;
import com.baybora.bookstore.repository.config.TestDataContextConfiguration;

@Configuration
@ComponentScan(basePackages={"com.baybora.bookstore.service"})
public class ServiceConfiguration {

}
