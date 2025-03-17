package com.example.AddressBook.model;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
public class AddressBook implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String email;
    private String phone;


    public AddressBook(){}

    public  AddressBook(int id, String name, String email, String phone){
        this.id=id;
        this.name=name;
        this.email=email;
        this.phone=phone;
    }
    public int getId(){
        return id;
    }

    public String getName(){
        return name;
    }
    public String getEmail(){
        return  email;
    }
    public String getPhone(){
        return  phone;
    }

    public void setName(String name){
        this.name=name;

    }
    public  void setEmail(String email){
        this.email=email;
    }
    public void setPhone(String phone){
        this.phone=phone;
    }


}
