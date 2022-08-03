package de.lasse.duden;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson.JacksonFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class TokenValidator {

    private String CLIENT_ID = "623953722915-s65tp1q2h696mk8fl0kv4uqibq5t702e.apps.googleusercontent.com";

    private GoogleIdTokenVerifier verifier;
    //https://developers.google.com/identity/sign-in/web/backend-auth#:~:text=After%20you%20receive%20the%20ID,to%20verify%20the%20token's%20signature.

    public TokenValidator(){
        verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
                .setAudience(Collections.singletonList(CLIENT_ID))
                .build();
    }

    public String getSubject(String idTokenString){
        GoogleIdToken idToken;

        try{
            idToken = verifier.verify(idTokenString);
        }catch(Exception e){
            return null;
        }

        if(idToken == null) return null;
        return idToken.getPayload().getSubject();
    }
}
