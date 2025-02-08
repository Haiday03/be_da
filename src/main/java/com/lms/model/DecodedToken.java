package com.lms.model;

import com.lms.model.enumerations.Role;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.tomcat.util.codec.binary.Base64;

import java.io.UnsupportedEncodingException;

public class DecodedToken {

    public String sub;
    public String username;
    public String userId;
    public Role role;
    public String exp;

    public static DecodedToken getDecoded(String encodedToken) throws UnsupportedEncodingException {
        String[] pieces = encodedToken.split("\\.");
        String b64payload = pieces[1];
        String jsonString = new String(Base64.decodeBase64(b64payload), "UTF-8");
        DecodedToken decodedToken = new Gson().fromJson(jsonString, DecodedToken.class);
        return decodedToken;
    }

    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }

}
