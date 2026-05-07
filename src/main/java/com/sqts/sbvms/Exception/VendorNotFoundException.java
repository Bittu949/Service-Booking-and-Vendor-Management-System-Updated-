package com.sqts.sbvms.Exception;

public class VendorNotFoundException extends RuntimeException{
    public VendorNotFoundException(String message){
        super(message);
    }
}
