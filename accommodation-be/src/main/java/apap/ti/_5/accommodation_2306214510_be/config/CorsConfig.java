package apap.ti._5.accommodation_2306214510_be.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Value("${CORS_ALLOWED_ORIGINS}")
    private String allowedOrigins;

    @PostConstruct
    public void init() {
        System.out.println("===========================================");
        System.out.println("CorsConfig bean is being created/loaded!");
        System.out.println("CORS_ALLOWED_ORIGINS: " + allowedOrigins);
        System.out.println("===========================================");
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                String[] origins = allowedOrigins.split(",");

                System.out.println("=== CORS CONFIG DEBUG ===");
                System.out.println("Using origins from environment variable: " + String.join(", ", origins));
                System.out.println("========================");

                registry.addMapping("/**")
                        .allowedOriginPatterns(origins)
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true)
                        .exposedHeaders("Authorization");
            }
        };
    }
}
