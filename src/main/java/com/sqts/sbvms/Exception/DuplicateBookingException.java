package com.sqts.sbvms.Exception;

public class DuplicateBookingException extends RuntimeException{
    public DuplicateBookingException(String message){
        super(message);
    }
}
