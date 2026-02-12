package com.mz.blog.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI blogManagementApi() {
        Server server = new Server();
        server.setUrl("http://localhost:8080");
        server.setDescription("Blog Management local dev server");

        Contact contact = new Contact();
        contact.setEmail("murtazamaimoon73@gmail.com");
        contact.setName("Murtaza Bohari");
        contact.setUrl("https://github.com/Murtaza-5253");

        License license = new License()
                .name("MIT License")
                .url("https://opensource.org/licenses/MIT");

        Info info = new Info()
                .title("Blog Management API")
                .version("1.0.0")
                .contact(contact)
                .description("REST API for managing blof articles,authors,categories and comments")
                .license(license);
        return new OpenAPI()
                .info(info)
                .servers(List.of(server));
    }
}
