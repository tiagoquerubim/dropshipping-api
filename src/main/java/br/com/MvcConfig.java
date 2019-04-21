package br.com;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class MvcConfig implements WebMvcConfigurer {
	
	@Bean
	@Primary
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
    }
}
