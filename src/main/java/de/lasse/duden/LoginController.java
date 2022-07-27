package de.lasse.duden;

import de.lasse.duden.database.Users.User;
import de.lasse.duden.database.Users.UserRepository;
import org.json.JSONObject;
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

        if(userRepository.findUserByUsername(username) != null)
            return createResponse("User already exists", 401);

        if(!username.matches("[a-zA-Z0-9_-]*"))
            return createResponse("Username contains unallowed characters", 400);

        if(username.length() < 3)
            return createResponse("Username has to be bigger than 3", 400);

        if(password.length() < 8)
            return createResponse("Password has to be bigger than 8", 400);

        if(!password.matches("[a-zA-Z0-9_-]*"))
            return createResponse("Username has invalid characters", 400);


        User user = new User(username, password);
        userRepository.save(user);

        return createResponse("Created User", 200);

    }

    ResponseEntity<String> createResponse(String message, int responseCode){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("response_code", responseCode);
        jsonObject.put("message", message);

        return new ResponseEntity<String>(jsonObject.toString(), new HttpHeaders(), responseCode);
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
