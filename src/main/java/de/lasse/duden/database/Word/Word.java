package de.lasse.duden.database.Word;

import com.fasterxml.jackson.annotation.JsonView;
import de.lasse.duden.Views;
import org.json.JSONObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection="Dictionary")
public class Word {

    @Id
    @JsonView(Views.GetWordsView.class)
    private String id;

    @JsonView(Views.GetWordsView.class)
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public void setUtilization(String utilization) {
        this.utilization = utilization;
    }

    public void setSynonyms(String synonyms) {
        this.synonyms = synonyms;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
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
