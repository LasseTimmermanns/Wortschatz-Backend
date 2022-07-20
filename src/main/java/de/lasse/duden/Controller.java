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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
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
                                           @RequestParam("kind") Optional<String[]> kindParam) {

        int frequencyValue = frequencyParam.orElse(-1);
        String[] utilizationValues = utilizationParam.orElse(new String[]{"allowAll"});
        String[] kindValues = kindParam.orElse(new String[]{"allowAll"});

        ArrayList<Word> wordArrayList =
                FilterHelper.processWithAllFilters(frequencyValue, kindValues, utilizationValues, entityManager, wordRepository);

        HttpHeaders responseHeaders = new HttpHeaders();
        return new ResponseEntity<String>(FilterHelper.reformatWordsToJsonArray(wordArrayList).toString(), responseHeaders, HttpStatus.OK);
    }

}
