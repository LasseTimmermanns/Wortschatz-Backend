package de.lasse.duden.controller;

import de.lasse.duden.ResponseGenerator;
import de.lasse.duden.database.Users.User;
import de.lasse.duden.database.Users.UserRepository;
import de.lasse.duden.database.Wordlists.CustomWordlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
@RequestMapping(value = "/api/update", method = RequestMethod.PUT)
@CrossOrigin
public class PutController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    CustomWordlistRepository customWordlistRepository;

    @PutMapping("/word")
    public ResponseEntity<String> putWord(@RequestParam("token") String token,
                                          @RequestParam("word") String word,
                                          @RequestParam("wordlistids") String wordlistIds){

        User user = userRepository.findUserBySessionToken(token);
        if(user == null) {
            Logger.getGlobal().info("Put Word invalid token");
            return ResponseGenerator.createResponse("Invalid Token", HttpStatus.UNAUTHORIZED.value());
        }

        String subject = user.getSubject();

        for(String wordlistId : wordlistIds.split(",")){
            customWordlistRepository.addWordToWordlist(wordlistId, word, subject);
        }

        return ResponseGenerator.createResponse("Ok", 200);
    }

}
