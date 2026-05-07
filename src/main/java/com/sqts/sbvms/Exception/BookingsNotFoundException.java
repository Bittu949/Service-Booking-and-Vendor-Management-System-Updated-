package com.sqts.sbvms.Exception;

public class BookingsNotFoundException extends RuntimeException{
    public BookingsNotFoundException(String message){
        super(message);
    }
}
