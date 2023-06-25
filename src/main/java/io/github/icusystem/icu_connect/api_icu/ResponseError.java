package io.github.icusystem.icu_connect.api_icu;

public class ResponseError {

    public int Code;
    public String Data;

    public ResponseError(int code, String data){
        this.Code = code;
        this.Data = data;
    }
}
