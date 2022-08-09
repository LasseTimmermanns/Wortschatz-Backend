package de.lasse.duden.controller;

import de.lasse.duden.ResponseGenerator;
import de.lasse.duden.TokenValidator;
import de.lasse.duden.database.Users.User;
import de.lasse.duden.database.Users.UserRepository;
import de.lasse.duden.database.Users.UserUtil;
import de.lasse.duden.database.Wordlists.CustomWordlistRepository;
import de.lasse.duden.database.Wordlists.Wordlist;
import de.lasse.duden.database.Wordlists.WordlistInterfaceRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.logging.Logger;

@RestController
@RequestMapping(value = "/api/create", method = RequestMethod.POST)
@CrossOrigin
public class PostController {

    @Autowired
    TokenValidator tokenValidator;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserUtil userUtil;

    @Autowired
    CustomWordlistRepository customWordlistRepository;

    @Autowired
    WordlistInterfaceRepository wordlistInterfaceRepository;

    @PostMapping("/session")
    public ResponseEntity<String> createUserAndSession(@RequestParam("idToken") Optional<String> optIdToken){
        String idToken = optIdToken.orElse("none");

        String subject = tokenValidator.getSubject(idToken);
        if(subject == null){
            Logger.getGlobal().info("User tried to get new Session with invalid idToken");
            return ResponseGenerator.createResponse("invalid idToken", HttpStatus.FORBIDDEN.value());
        }

        User user = userRepository.findUserBySubject(subject);

        if(user == null) user = userUtil.createUser(subject);

        User updated = user.createSession();
        userRepository.save(updated);

        Logger.getGlobal().info("Created new Session successful");

        JSONObject out = new JSONObject()
                .put("session_token", updated.getSessionToken())
                .put("session_iat", updated.getSessionIat())
                .put("session_exp", updated.getSessionExp());

        return ResponseGenerator.createResponse("Created new Session", out, HttpStatus.OK.value());
    }



    @PostMapping("/wordlist")
    public ResponseEntity createWordlist(@RequestParam("name") Optional<String> nameParam,
                                         @RequestParam("token") String token){
        User user = userRepository.findUserBySessionToken(token);
        if(user == null) {
            Logger.getGlobal().info("Create Wordlist invalid token");
            return ResponseGenerator.createResponse("Invalid Token", HttpStatus.UNAUTHORIZED.value());
        }

        String owner = user.getSubject();
        String name = nameParam.orElse("Wortliste " + (customWordlistRepository.getUserWordlists(owner).size() + 1));
        boolean isPublic = true;

        Wordlist wordlist = new Wordlist(owner, name, isPublic);
        wordlistInterfaceRepository.save(wordlist);

        return ResponseGenerator.createResponse("Created new Wordlist " + name + " successful", 200);
    }
}
