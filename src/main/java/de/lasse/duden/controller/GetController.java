package de.lasse.duden.controller;

import com.fasterxml.jackson.annotation.JsonView;
import de.lasse.duden.ResponseGenerator;
import de.lasse.duden.Views;
import de.lasse.duden.database.Users.User;
import de.lasse.duden.database.Users.UserRepository;
import de.lasse.duden.database.Utilization.UtilizationDisplay;
import de.lasse.duden.database.Utilization.UtilizationRepository;
import de.lasse.duden.database.Word.Word;
import de.lasse.duden.database.Word.WordInterfaceRepository;
import de.lasse.duden.database.Word.WordRepository;
import de.lasse.duden.database.Wordlists.CustomWordlistRepository;
import de.lasse.duden.database.Wordlists.WordlistDisplay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
@CrossOrigin
public class GetController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    WordRepository wordRepository;

    @Autowired
    WordInterfaceRepository wordInterfaceRepository;

    @Autowired
    CustomWordlistRepository customWordlistRepository;

    @Autowired
    UtilizationRepository utilizationRepository;

    @GetMapping("/api/get/wordlists")
    public ResponseEntity getUserWordlists(@RequestParam("token") String token){
        User user = userRepository.findUserBySessionToken(token);
        if(user == null) {
            Logger.getGlobal().info("Get Wordlists invalid token");
            return ResponseGenerator.createResponse("invalid token", 403);
        }

        List<WordlistDisplay> wordlists = customWordlistRepository.getUserWordlists(user.getSubject());
        return new ResponseEntity(wordlists, new HttpHeaders(), 200);
    }

    @GetMapping("/api/get/utilization")
    public ResponseEntity<List<UtilizationDisplay>> getUtilizations(@RequestParam("limit") Optional<Integer> limit) {
        long time = System.currentTimeMillis();
        HttpHeaders responseHeaders = new HttpHeaders();
        if(limit.isEmpty())
            Logger.getGlobal().info("Utilization get without limit");

        int limitInt = limit.orElse(100);
        List list = utilizationRepository.getUtilizations(limitInt);
        Logger.getGlobal().info("Utilization Processed Time: " + (System.currentTimeMillis() - time));
        return new ResponseEntity<>(list, responseHeaders, HttpStatus.OK);
    }

    @GetMapping("/api/get/foo")
    public ResponseEntity<String> getFoooooo() {
        HttpHeaders responseHeaders = new HttpHeaders();
        return new ResponseEntity<String>("bar", responseHeaders, HttpStatus.OK);
    }



    @GetMapping("/api/get/words")
    @JsonView(Views.GetWordsView.class)
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

    @GetMapping("/api/get/wordinfo")
    public ResponseEntity getWordInfo(@RequestParam("word") Optional<String> wordParam,
                                            @RequestParam("wordid") Optional<String> wordidParam){
        long time = System.currentTimeMillis();

        if(!wordParam.isEmpty()){
            Word word = wordInterfaceRepository.findWordByWord(wordParam.orElse(""));
            Logger.getGlobal().info("Words Processed Time: " + (System.currentTimeMillis() - time));
            return new ResponseEntity<Word>(word, new HttpHeaders(), 200);
        }

        if(!wordidParam.isEmpty()){
            Word word = wordInterfaceRepository.findWordById(wordidParam.orElse(""));
            Logger.getGlobal().info("Words Processed Time: " + (System.currentTimeMillis() - time));
            return new ResponseEntity<Word>(word, new HttpHeaders(), 200);
        }

        return ResponseGenerator.createResponse(400);
    }

}
