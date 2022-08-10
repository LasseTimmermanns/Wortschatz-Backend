package de.lasse.duden.controller;

import com.fasterxml.jackson.annotation.JsonView;
import de.lasse.duden.ResponseGenerator;
import de.lasse.duden.Views;
import de.lasse.duden.database.Users.User;
import de.lasse.duden.database.Users.UserRepository;
import de.lasse.duden.database.Wordlists.Wordlist;
import de.lasse.duden.database.Wordlists.WordlistInterfaceRepository;
import org.json.JSONObject;
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
        return new ResponseEntity(wordlistIsValid(wordlistid), new HttpHeaders(), 200);
    }

    @GetMapping("/hasaccess")
    public ResponseEntity hasAccess(@RequestParam("wordlistid") String wordlistid,
                                    @RequestParam("token") Optional<String> tokenParam){
        return ResponseGenerator.createResponse("OK", new JSONObject().put("access", userHasAccess(wordlistid, tokenParam)), 200);
    }

    @GetMapping("/byid")
    @JsonView(Views.LearnWordlistView.class)
    public Object getById(@RequestParam("wordlistid") String wordlistid,
                                    @RequestParam("token") Optional<String> tokenParam){
        if(!wordlistIsValid(wordlistid))
            return ResponseGenerator.createResponse("Wordlist not found", 400);

        if(!userHasAccess(wordlistid, tokenParam))
            return ResponseGenerator.createResponse("User has no permission", 400);

        return wordlistInterfaceRepository.findWordlistById(wordlistid);
    }

    private boolean wordlistIsValid(String wordlistid){
        return wordlistInterfaceRepository.findWordlistById(wordlistid) != null;
    }

    private boolean userHasAccess(String wordlistid, Optional<String> tokenParam){
        Wordlist wordlist = wordlistInterfaceRepository.findWordlistById(wordlistid);
        if(wordlist == null)
            return false;

        if(wordlist.isPublic())
            return true;

        if(tokenParam.isEmpty())
            return false;

        User user = userRepository.findUserBySessionToken(tokenParam.orElse(""));
        if(user == null)
            return false;

        if(wordlist.getOwner().equals(user.getSubject()))
            return true;

        return false;
    }



}
