package de.lasse.duden.database.Users;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "subject")
    private String subject;
    @Column(name = "session_token")
    private String sessionToken;
    @Column(name = "session_iat")
    private long sessionIat;
    @Column(name = "session_exp")
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
