package com.sqts.sbvms.Exception;

public class PasswordMismatchedException extends RuntimeException{
    public PasswordMismatchedException(String message){
        super(message);
    }
}
