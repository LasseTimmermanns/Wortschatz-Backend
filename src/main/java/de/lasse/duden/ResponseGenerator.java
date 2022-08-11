package de.lasse.duden;

import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseGenerator {

    public static ResponseEntity<String> createResponse(String message, String displayMessage, int responseCode){
        return createResponse(message, displayMessage, new JSONObject(), responseCode);
    }

    public static ResponseEntity<String> createResponse(String message, int responseCode){
        return createResponse(message, message, new JSONObject(), responseCode);
    }

    public static ResponseEntity createResponse(int responseCode){
        return new ResponseEntity(HttpStatus.resolve(responseCode));
    }

    public static ResponseEntity<String> createResponse(String message, JSONObject jsonObject, int responseCode){
        return createResponse(message, message, jsonObject, responseCode);
    }

    public static ResponseEntity<String> createResponse(String message, String displayMessage, JSONObject jsonObject, int responseCode){
        jsonObject.put("response_code", responseCode);
        jsonObject.put("message", message);
        jsonObject.put("display_message", displayMessage);

        return new ResponseEntity<>(jsonObject.toString(), new HttpHeaders(), responseCode);
    }

}
