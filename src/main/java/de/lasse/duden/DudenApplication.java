package de.lasse.duden;

import de.lasse.duden.database.Users.UserRepository;
import de.lasse.duden.database.Word.WordRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;


@SpringBootApplication
public class DudenApplication {

    public static ConfigurableApplicationContext applicationContext;

    public static void main(String[] args) {
        applicationContext = SpringApplication.run(DudenApplication.class, args);
    }

}
