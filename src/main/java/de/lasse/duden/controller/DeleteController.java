package de.lasse.duden.controller;

import de.lasse.duden.ResponseGenerator;
import de.lasse.duden.database.Users.User;
import de.lasse.duden.database.Users.UserRepository;
import de.lasse.duden.database.Wordlists.Wordlist;
import de.lasse.duden.database.Wordlists.WordlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
@RequestMapping(value = "/api/delete", method = RequestMethod.DELETE)
@CrossOrigin
public class DeleteController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    WordlistRepository wordlistRepository;

    @DeleteMapping("/wordlist")
    public ResponseEntity deleteWordlist(@RequestParam("wordlist") String wordlistId,
                                         @RequestParam("token") String token){
        User user = userRepository.findUserBySessionToken(token);
        if(user == null) {
            Logger.getGlobal().info("Delete Wordlist invalid token");
            return ResponseGenerator.createResponse("Invalid Token", HttpStatus.UNAUTHORIZED.value());
        }

        Wordlist wordlist = wordlistRepository.findWordlistById(wordlistId);
        if(wordlist == null)
            return ResponseGenerator.createResponse("Wordlist not found", HttpStatus.BAD_REQUEST.value());

        if(!wordlist.getOwner().equals(user.getSubject()))
            return ResponseGenerator.createResponse("User is not owner of Wordlist", HttpStatus.UNAUTHORIZED.value());

        wordlistRepository.delete(wordlist);
        return ResponseGenerator.createResponse("Wordlist deleted successful", 200);
    }
}
