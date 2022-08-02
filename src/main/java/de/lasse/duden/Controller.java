package de.lasse.duden;

import de.lasse.duden.database.Users.User;
import de.lasse.duden.database.Users.UserRepository;
import de.lasse.duden.database.Users.UserUtil;
import de.lasse.duden.database.Word.Word;
import de.lasse.duden.database.Word.WordRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class Controller {

    @Autowired
    WordRepository wordRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserUtil userUtil;

    @PersistenceContext
    private EntityManager entityManager;

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

    @GetMapping("/foo")
    public ResponseEntity<String> getFoooooo(@RequestParam("word") String word_key) {
        HttpHeaders responseHeaders = new HttpHeaders();
        //JSONArray jsonArray = new JSONArray(wordRepository.findAllDistinctUtilization());
        return new ResponseEntity<String>(wordRepository.findAllDistinctUtilization().toString(), responseHeaders, HttpStatus.OK);
    }

    @GetMapping("/utilization")
    public ResponseEntity<String> getUtilization(@RequestParam("limit") Optional<Integer> limitParam) {
        HttpHeaders responseHeaders = new HttpHeaders();

        int limitValue = limitParam.orElse(1000);

        List<String> in = wordRepository.findAllDistinctUtilization();
        JSONArray out = new JSONArray();

        int i = 0;
        for(String s : in){
            if(i > limitValue) break;
            String[] parts = s.split(",");
            if(parts[0].equals("null")) continue;
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("utilization", parts[0]);
            jsonObject.put("count", parts[parts.length - 1]);
            out.put(jsonObject);
            i++;
        }

        return new ResponseEntity<String>(out.toString(), responseHeaders, HttpStatus.OK);
    }

    @GetMapping("/getWords")
    public ResponseEntity<String> getWords(@RequestParam("frequency") Optional<Integer> frequencyParam,
                                           @RequestParam("utilization") Optional<String[]> utilizationParam,
                                           @RequestParam("kind") Optional<String[]> kindParam,
                                           @RequestParam("limit") Optional<Integer> limitParam) {

        long time = System.currentTimeMillis();

        int frequencyValue = frequencyParam.orElse(-1);
        int limitValue = limitParam.orElse(20);
        String[] utilizationValues = utilizationParam.orElse(new String[]{"allowAll"});
        String[] kindValues = kindParam.orElse(new String[]{"allowAll"});

        ArrayList<Word> wordArrayList =
                FilterHelper.processWithAllFilters(frequencyValue, kindValues, utilizationValues, entityManager, wordRepository);

        HttpHeaders responseHeaders = new HttpHeaders();
        ArrayList<Word> output = FilterHelper.getRandomWordsLimited(wordArrayList, limitValue);
        System.out.println("Processed Time: " + (System.currentTimeMillis() - time));
        return new ResponseEntity<String>(FilterHelper.reformatWordsToJsonArray(output).toString(), responseHeaders, HttpStatus.OK);
    }

}
