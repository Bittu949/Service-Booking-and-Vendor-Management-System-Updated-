package com.sqts.sbvms.Exception;

public class InvalidBookingStatusException extends RuntimeException{
    public InvalidBookingStatusException(String message){
        super(message);
    }
}
