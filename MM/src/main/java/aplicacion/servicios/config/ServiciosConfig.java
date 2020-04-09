package aplicacion.servicios.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(ignoreResourceNotFound = true, value = "classpath:acciones.properties")
public class ServiciosConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}