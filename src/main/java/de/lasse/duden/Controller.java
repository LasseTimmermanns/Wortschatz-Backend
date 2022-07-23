package de.lasse.duden;

import de.lasse.duden.database.FilterObj;
import de.lasse.duden.database.Word;
import de.lasse.duden.database.WordRepository;
import org.apache.coyote.Response;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class Controller {

    @Autowired
    WordRepository wordRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @GetMapping("/foo")
    public ResponseEntity<String> getFoooooo(@RequestParam("word") String word_key) {
        HttpHeaders responseHeaders = new HttpHeaders();
        return new ResponseEntity<String>("BAR", responseHeaders, HttpStatus.OK);
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
