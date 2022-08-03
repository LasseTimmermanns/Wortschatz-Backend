package de.lasse.duden.database.Users;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "Users")
public class User {

    @Id
    @Field(name = "subject")
    private String subject;
    @Field(name = "session_token")
    private String sessionToken;
    @Field(name = "session_iat")
    private long sessionIat;
    @Field(name = "session_exp")
    private long sessionExp;


    public User(String subject) {
        this.subject = subject;
    }

    public User(){}

    public String getSubject() {
        return subject;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public long getSessionIat() {
        return sessionIat;
    }

    public long getSessionExp() {
        return sessionExp;
    }

    public User createSession(){
        this.sessionToken = UserUtil.generateSessionToken();
        this.sessionIat = System.currentTimeMillis();
        this.sessionExp = this.sessionIat + 3600 * 1000;
        return this;
    }
}
