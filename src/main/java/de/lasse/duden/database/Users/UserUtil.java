package de.lasse.duden.database.Users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.logging.Logger;

@Service
public class UserUtil {

    @Autowired
    UserRepository userRepository;

    private static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static SecureRandom rnd = new SecureRandom();

    public User createUser(String subject){
        Logger.getGlobal().info("Created User with Subject " + subject);
        User user = new User(subject);
        userRepository.save(user);
        return user;
    }

    public static String generateSessionToken(){
        int len = 23;
        StringBuilder sb = new StringBuilder(len);
        for(int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }



}
