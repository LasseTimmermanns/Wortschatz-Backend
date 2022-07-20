package de.lasse.duden;

import de.lasse.duden.database.WordRepository;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;


@SpringBootApplication
public class DudenApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(DudenApplication.class, args);
        WordRepository userRepository = applicationContext.getBean(WordRepository.class);
    }

}
