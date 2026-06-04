package com.sqts.sbvms.Exception;

public class InvalidVendorStatusException extends RuntimeException{
    public InvalidVendorStatusException(String message){
        super(message);
    }
}
