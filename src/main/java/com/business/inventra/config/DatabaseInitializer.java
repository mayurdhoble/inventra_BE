package com.business.inventra.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.jdbc.datasource.init.ScriptException;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
public class DatabaseInitializer {

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    @Value("${inventra.db.initialize:false}")
    private boolean shouldInitialize;

    @Bean(name = "databaseInitializationRunner")
    public ApplicationRunner databaseInitializer(DataSource dataSource) throws SQLException {
        return args -> {
            if (!shouldInitialize) {
                System.out.println("Database initialization is disabled");
                return;
            }

            String scriptPath = driverClassName.contains("postgresql") 
                ? "db/postgres/init-data.sql" 
                : "db/mysql/init-data.sql";

            System.out.println("Initializing database with script: " + scriptPath);

            ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
            populator.addScript(new ClassPathResource(scriptPath));
            populator.setContinueOnError(true); // Continue even if some statements fail
            populator.setIgnoreFailedDrops(true); // Ignore failed drops
            populator.setSeparator(";"); // Use semicolon as separator
            
            try {
                populator.execute(dataSource);
                System.out.println("Database initialization completed successfully");
            } catch (ScriptException e) {
                System.err.println("Error executing database initialization script: " + e.getMessage());
                System.err.println("Script path: " + scriptPath);
                System.err.println("Error details: " + e.getCause());
                throw e; // Re-throw to fail fast if there's an error
            }
        };
    }
} 