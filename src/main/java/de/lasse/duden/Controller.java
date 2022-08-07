package de.lasse.duden;

import de.lasse.duden.database.Users.User;
import de.lasse.duden.database.Users.UserRepository;
import de.lasse.duden.database.Users.UserUtil;
import de.lasse.duden.database.Utilization.UtilizationDisplay;
import de.lasse.duden.database.Utilization.UtilizationRepository;
import de.lasse.duden.database.Word.WordRepository;
import de.lasse.duden.database.Word.Word;
import de.lasse.duden.database.Wordlists.CustomWordlistRepository;
import de.lasse.duden.database.Wordlists.Wordlist;
import de.lasse.duden.database.Wordlists.WordlistDisplay;
import de.lasse.duden.database.Wordlists.WordlistRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class Controller {


    @Autowired
    UserUtil userUtil;

    @Autowired
    UserRepository userRepository;

    @Autowired
    WordRepository wordRepository;

    @Autowired
    WordlistRepository wordlistRepository;

    @Autowired
    CustomWordlistRepository customWordlistRepository;

    @Autowired
    UtilizationRepository utilizationRepository;

    @Autowired
    TokenValidator tokenValidator;

    @PostMapping("/create/session")
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

    @GetMapping("/get/wordlists")
    public ResponseEntity getUserWordlists(@RequestParam("token") String token){
        User user = userRepository.findUserBySessionToken(token);
        if(user == null) {
            Logger.getGlobal().info("Get Wordlists invalid token");
            return ResponseGenerator.createResponse("invalid token", 403);
        }

        List<WordlistDisplay> wordlists = customWordlistRepository.getUserWordlists(user.getSubject());
        return new ResponseEntity(wordlists, new HttpHeaders(), 200);
    }

    @PostMapping("/create/wordlist")
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
        wordlistRepository.save(wordlist);

        return ResponseGenerator.createResponse("Created new Wordlist " + name + " successful", 200);
    }

    @PutMapping("/put/word")
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

    @DeleteMapping("/delete/wordlist")
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

    @GetMapping("/get/utilization")
    public ResponseEntity<List<UtilizationDisplay>> getUtilizations(@RequestParam("limit") Optional<Integer> limit) {
        long time = System.currentTimeMillis();
        HttpHeaders responseHeaders = new HttpHeaders();
        if(limit.isEmpty())
            Logger.getGlobal().info("Utilization get without limitInt");

        int limitInt = limit.orElse(100);
        List list = utilizationRepository.getUtilizations(limitInt);
        Logger.getGlobal().info("Utilization Processed Time: " + (System.currentTimeMillis() - time));
        return new ResponseEntity<>(list, responseHeaders, HttpStatus.OK);
    }

    @GetMapping("/foo")
    public ResponseEntity<String> getFoooooo() {
        HttpHeaders responseHeaders = new HttpHeaders();
        return new ResponseEntity<String>("bar", responseHeaders, HttpStatus.OK);
    }



    @GetMapping("/get/words")
    public ResponseEntity<List<Word>> getWords(@RequestParam("frequency") Optional<Integer> frequencyParam,
                                               @RequestParam("utilization") Optional<String[]> utilizationParam,
                                               @RequestParam("kind") Optional<String[]> kindParam,
                                               @RequestParam("limit") Optional<Integer> limitParam) {

        long time = System.currentTimeMillis();

        int frequency = frequencyParam.orElse(1);
        int limit = limitParam.orElse(5);
        Collection<String> utilization = Arrays.asList(utilizationParam.orElse(new String[]{}));
        Collection<String> kind = Arrays.asList(kindParam.orElse(new String[]{}));

        HttpHeaders responseHeaders = new HttpHeaders();
        List<Word> output = wordRepository.getWordsWithFilter(utilization, kind, frequency, limit);
        Logger.getGlobal().info("Words Processed Time: " + (System.currentTimeMillis() - time));
        return new ResponseEntity<List<Word>>(output, responseHeaders, HttpStatus.OK);
    }

}
