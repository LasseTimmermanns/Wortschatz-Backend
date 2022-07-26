package de.lasse.duden;

import de.lasse.duden.database.Word;
import de.lasse.duden.database.WordRepository;
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

    @PersistenceContext
    private EntityManager entityManager;

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
            if(parts[0] == "null") continue;
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("utilization", parts[0]);
            jsonObject.put("count", parts[1]);
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
