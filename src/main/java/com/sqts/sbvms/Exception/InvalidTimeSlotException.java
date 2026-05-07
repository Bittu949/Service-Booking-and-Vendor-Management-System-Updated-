package com.sqts.sbvms.Exception;

public class InvalidTimeSlotException extends RuntimeException{
    public InvalidTimeSlotException(String message){
        super(message);
    }
}
