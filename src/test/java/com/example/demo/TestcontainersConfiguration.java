package com.example.demo;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfiguration {
	@Bean
	PostgreSQLContainer<?> sqlServerContainer() {
		return new PostgreSQLContainer<>(DockerImageName.parse("mcr.microsoft.com/mssql/server:latest"));
	}

}
