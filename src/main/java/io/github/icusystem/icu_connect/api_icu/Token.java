package io.github.icusystem.icu_connect.api_icu;

public class Token {

    public String access_token;
    public Long expires_in;

    public Token(String access_token, Long expires_in){
        this.access_token = access_token;
        this.expires_in = expires_in;
    }


}
