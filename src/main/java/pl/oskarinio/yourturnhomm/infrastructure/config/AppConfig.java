package pl.oskarinio.yourturnhomm.infrastructure.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import pl.oskarinio.yourturnhomm.infrastructure.usecase.database.UserRepositoryUseCase;

import java.time.Clock;
import java.time.ZoneId;

@Configuration
class AppConfig {
    @Autowired
    private UserRepositoryUseCase userRepositoryUseCase;

    @Bean
    public LocalValidatorFactoryBean getValidator(MessageSource messageSource){
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource);
        return bean;
    }

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean
    public Clock clock(){
        return Clock.system(ZoneId.of("Europe/Warsaw"));
    }
}
