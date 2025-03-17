package com.example.AddressBook.Exception;

public class AddressBookNotFoundException extends  RuntimeException{
    public AddressBookNotFoundException(String message){
        super(message);
    }
}
