package apap.ti._5.accommodation_2306214510_be.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import javax.sql.DataSource;

@Configuration
public class DataInitializer {
    
    @Bean
    public CommandLineRunner loadData(DataSource dataSource) {
        return args -> {
            try {
                ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
                populator.addScript(new ClassPathResource("data.sql"));
                populator.execute(dataSource);
                System.out.println("✓ Data loaded successfully from data.sql");
            } catch (Exception e) {
                System.err.println("Error loading data: " + e.getMessage());
                e.printStackTrace();
            }
        };
    }
}
