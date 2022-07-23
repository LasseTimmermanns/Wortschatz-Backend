package de.lasse.duden;

import de.lasse.duden.database.FilterObj;
import de.lasse.duden.database.Word;
import de.lasse.duden.database.WordRepository;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.json.JSONArray;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FilterHelper {

    public static ArrayList<Word> processWithAllFilters(int frequencyValue, String[] kindValues, String[] utilizationValues,
                                                        EntityManager entityManager, WordRepository wordRepository){
        ArrayList<Word> output = new ArrayList<Word>();
        ArrayList<FilterObj> filters = new ArrayList<FilterObj>();
        for (String kind : kindValues) {
            if (frequencyValue != -1)
                filters.add(new FilterObj("frequencyFilter", "minFrequency", frequencyValue));
            if (kind != "allowAll")
                filters.add(new FilterObj("kindFilter", "kind", kind));
            for (String utilization : utilizationValues) {
                if (utilization != "allowAll")
                    filters.add(new FilterObj("utilizationFilter", "utilization", utilization));

                output.addAll(getWithFilters(filters, entityManager, wordRepository));

                if(utilization != "allowAll")
                    filters.remove(filters.size() - 1);
            }

            filters.clear();
        }
        return output;
    }

    public static ArrayList<Word> getRandomWordsLimited(ArrayList<Word> in, int limit){
        if(in.size() <= limit) {
            Collections.shuffle(in);
            return in;
        }

        ArrayList<Word> out = new ArrayList<>();
        for(int i = 0; i < limit; i++){
            int random = (int) (Math.random() * in.size());
            out.add(in.get(random));
            in.remove(random);
        }

        return out;
    }

    public static JSONArray reformatWordsToJsonArray(ArrayList<Word> words){
        JSONArray out = new JSONArray();
        for (Word w : words) out.put(w.toJsonObject());
        return out;
    }

    public static List<Word> getWithFilters(ArrayList<FilterObj> filters, EntityManager entityManager, WordRepository wordRepository) {
        List<Word> output;
        Session session = entityManager.unwrap(Session.class);

        for (FilterObj filter : filters) {
            Filter newFilter = session.enableFilter(filter.getName());
            newFilter.setParameter(filter.getParameterName(), filter.getParameter());
        }

        output = wordRepository.findAll();

        for (FilterObj filter : filters) session.disableFilter(filter.getName());

        return output;
    }

}
