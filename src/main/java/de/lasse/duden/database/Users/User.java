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

    @Field(name = "own_wordlists")
    private String[] ownWordlistIds;

    @Field(name = "following_wordlists")
    private String[] followingWordlistIds;

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

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public void setSessionIat(long sessionIat) {
        this.sessionIat = sessionIat;
    }

    public void setSessionExp(long sessionExp) {
        this.sessionExp = sessionExp;
    }

    public String[] getOwnWordlistIds() {
        return ownWordlistIds;
    }

    public void setOwnWordlistIds(String[] ownWordlistIds) {
        this.ownWordlistIds = ownWordlistIds;
    }

    public String[] getFollowingWordlistIds() {
        return followingWordlistIds;
    }

    public void setFollowingWordlistIds(String[] followingWordlistIds) {
        this.followingWordlistIds = followingWordlistIds;
    }

    public User createSession(){
        this.sessionToken = UserUtil.generateSessionToken();
        this.sessionIat = System.currentTimeMillis();
        this.sessionExp = this.sessionIat + 3600 * 1000;
        return this;
    }
}
