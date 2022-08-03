package de.lasse.duden.database.Word;

import org.json.JSONObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection="Dictionary")
public class Word {

    @Id
    private String id;

    private String word;

    private String kind, utilization, synonyms, description;
    private int frequency;

    public String getKind(){
        return this.kind;
    }

    public String getWord() {
        return word;
    }

    public String getUtilization() {
        return utilization;
    }

    public String getSynonyms() {
        return synonyms;
    }

    public String getDescription() {
        return description;
    }

    public int getFrequency() {
        return frequency;
    }


    public JSONObject toJsonObject(){
        JSONObject obj = new JSONObject();
        obj.put("word", this.word);
        obj.put("kind", this.kind);
        obj.put("utilization", this.utilization);
        obj.put("synonyms", this.synonyms);
        obj.put("frequency", this.frequency);
        obj.put("description", this.description);
        return obj;
    }


}
