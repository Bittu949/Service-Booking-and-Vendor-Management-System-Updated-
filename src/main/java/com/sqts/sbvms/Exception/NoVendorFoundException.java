package com.sqts.sbvms.Exception;

public class NoVendorFoundException extends RuntimeException{
    public NoVendorFoundException(String message){
        super(message);
    }
}
