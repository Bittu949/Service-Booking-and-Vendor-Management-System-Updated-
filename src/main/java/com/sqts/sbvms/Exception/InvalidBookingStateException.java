package com.sqts.sbvms.Exception;

public class InvalidBookingStateException extends RuntimeException{
    public InvalidBookingStateException(String message){
        super(message);
    }
}
