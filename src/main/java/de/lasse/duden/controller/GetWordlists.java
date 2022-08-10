package de.lasse.duden.controller;

import de.lasse.duden.ResponseGenerator;
import de.lasse.duden.database.Users.User;
import de.lasse.duden.database.Users.UserRepository;
import de.lasse.duden.database.Wordlists.Wordlist;
import de.lasse.duden.database.Wordlists.WordlistInterfaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.logging.Logger;

@RequestMapping("/api/get/wordlist")
@CrossOrigin
@RestController
public class GetWordlists {

    @Autowired
    UserRepository userRepository;

    @Autowired
    WordlistInterfaceRepository wordlistInterfaceRepository;

    @GetMapping("/user")
    public ResponseEntity getWordlistsFromUser(@RequestParam("token") String token){
        User user = userRepository.findUserBySessionToken(token);
        if(user == null) {
            Logger.getGlobal().info("Get Wordlists invalid token");
            return ResponseGenerator.createResponse("invalid token", 403);
        }

        return new ResponseEntity(wordlistInterfaceRepository.findWordlistsByOwner(user.getSubject()), new HttpHeaders(), 200);
    }

    @GetMapping("/notuser")
    public ResponseEntity getWordlistsNotFromUser(@RequestParam("token") String token){
        User user = userRepository.findUserBySessionToken(token);
        if(user == null) {
            Logger.getGlobal().info("Get Wordlists invalid token");
            return ResponseGenerator.createResponse("invalid token", 403);
        }

        return new ResponseEntity(wordlistInterfaceRepository.findWordlistsByOwnerNot(user.getSubject()), new HttpHeaders(), 200);
    }

    @GetMapping("/anyuser")
    public ResponseEntity getWordlists(){
        return new ResponseEntity(wordlistInterfaceRepository.findAll(), new HttpHeaders(), 200);
    }

    @GetMapping("/isvalid")
    public ResponseEntity isValid(@RequestParam("wordlistid") String wordlistid){
        return new ResponseEntity(wordlistInterfaceRepository.findWordlistById(wordlistid) != null, new HttpHeaders(), 200);
    }

    @GetMapping("/hasaccess")
    public ResponseEntity hasAccess(@RequestParam("wordlistid") String wordlistid,
                                    @RequestParam("token") Optional<String> tokenParam){
        Wordlist wordlist = wordlistInterfaceRepository.findWordlistById(wordlistid);
        if(wordlist == null)
            return new ResponseEntity(false, new HttpHeaders(), 200);

        if(wordlist.isPublic())
            return new ResponseEntity(true, new HttpHeaders(), 200);

        if(tokenParam.isEmpty())
            return new ResponseEntity(false, new HttpHeaders(), 200);

        User user = userRepository.findUserBySessionToken(tokenParam.orElse(""));
        if(user == null)
            return new ResponseEntity(false, new HttpHeaders(), 200);

        if(wordlist.getOwner().equals(user.getSubject()))
            return new ResponseEntity(true, new HttpHeaders(), 200);

        return new ResponseEntity(false, new HttpHeaders(), 200);
    }



}
