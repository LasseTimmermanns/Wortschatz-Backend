package de.lasse.duden.database.Word;

import org.hibernate.annotations.*;
import org.json.JSONObject;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;



/*@FilterDefs({
        @FilterDef(name = "kindFilter",
                parameters = @ParamDef(name = "kind", type = "java.lang.String")),
        @FilterDef(name = "frequencyFilter",
                parameters = @ParamDef(name = "frequency", type = "java.lang.short")),

})
@Filter(name = "kindFilter")*/
@FilterDef(name="frequencyFilter",
        parameters = @ParamDef( name="minFrequency", type="integer" ),
        defaultCondition = "frequency >= :minFrequency")
@FilterDef(name="kindFilter",
        parameters = @ParamDef( name="kind", type="java.lang.String" ),
        defaultCondition = "kind = :kind")
@FilterDef(name="utilizationFilter",
        parameters = @ParamDef( name="utilization", type="java.lang.String" ),
        defaultCondition = "utilization = :utilization")

@Entity
@Table(name = "dictionary")
@Filter(name = "frequencyFilter")
@Filter(name = "kindFilter")
@Filter(name = "utilizationFilter")
public class Word {

    @Id
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
