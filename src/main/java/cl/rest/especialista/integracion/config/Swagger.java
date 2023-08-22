package cl.rest.especialista.integracion.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.google.common.base.Predicate;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Component
@Configuration
@EnableSwagger2
public class Swagger implements WebMvcConfigurer {
	    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry
                .addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry
                .addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }


    /**
     * Paths.
     *
     * @return the predicate
     */
    private static Predicate<String> paths() {
        return PathSelectors.regex("/.*");
    }


    /**
     * Docket.
     *
     * @return the docket
     */
    @Bean
    public Docket docket() {


        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Prueba Especialista Integración")
                .useDefaultResponseMessages(false)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("cl.rest.especialista.integracion.controller"))
                .paths(paths())
                .build();
    }
    

    /**
     * Api info.
     *
     * @return the api info
     */
    private static ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("BCI")
                .license("BCI")
                .version("1.0")
                .build();
    }
}

