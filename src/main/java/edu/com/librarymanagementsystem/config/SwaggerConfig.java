package edu.com.librarymanagementsystem.config;

import edu.com.librarymanagementsystem.books.Book;
import edu.com.librarymanagementsystem.borrowingRecords.BorrowingRecord;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.annotations.media.Schema;

@Configuration
public class SwaggerConfig {
    String schemeName = "Auth";
    String bearerFormat = "JWT";
    String scheme = "bearer";
    @Bean
    public OpenAPI caseOpenAPI() {
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement()
                        .addList(schemeName)).components(new Components()
                        .addSecuritySchemes(
                                schemeName, new SecurityScheme()
                                        .name(schemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .bearerFormat(bearerFormat)
                                        .in(SecurityScheme.In.HEADER)
                                        .scheme(scheme)
                        )
                        .addSchemas("Books", ModelConverters.getInstance().readAllAsResolvedSchema(Book.class).schema)
                )
                .info(new Info()
                        .title("Library Management System")
                        .version("1.0")
                );
    }
}