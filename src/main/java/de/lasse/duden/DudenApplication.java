package de.lasse.duden;

import de.lasse.duden.database.Users.UserUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;


@SpringBootApplication
public class DudenApplication {


    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(DudenApplication.class, args);
        UserUtil userUtil = applicationContext.getBean(UserUtil.class);
    }

}
