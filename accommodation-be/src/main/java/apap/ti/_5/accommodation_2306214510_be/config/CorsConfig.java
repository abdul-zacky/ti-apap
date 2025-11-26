package apap.ti._5.accommodation_2306214510_be.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {



    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                String[] origins = new String[]{
                        "http://2306214510-fe.hafizmuh.site",
                        "https://2306214510-fe.hafizmuh.site",
                        "http://localhost:3000",
                        "http://localhost:5173"
                };

                System.out.println("=== CORS CONFIG DEBUG ===");
                System.out.println("Using hardcoded origins (allowedOriginPatterns): " + String.join(", ", origins));
                System.out.println("This should NOT use allowedOrigins with wildcard");
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
