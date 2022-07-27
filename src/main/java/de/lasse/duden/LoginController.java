package de.lasse.duden;

import de.lasse.duden.database.Users.User;
import de.lasse.duden.database.Users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/login")
public class LoginController {


    @Autowired
    UserRepository userRepository;


    @CrossOrigin
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<String> register(@RequestParam("username") String username,
                                             @RequestParam("password") String password) {
        HttpHeaders responseHeaders = new HttpHeaders();
        System.out.println(username + " " + password);

        User user = new User(username, password);
        userRepository.save(user);

        return new ResponseEntity<String>("Created User", responseHeaders, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<String> login(@RequestParam("username") String username,
                                        @RequestParam("password") String password){

        HttpHeaders responseHeaders = new HttpHeaders();
        System.out.println(username + " " + password);
        return new ResponseEntity<String>("jea", responseHeaders, HttpStatus.OK);
    }

}
