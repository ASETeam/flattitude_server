package com.flattitude.dto;

public class Flat {
    private int id = 0;
    private String serverid;
    private String name;
    private String country;
    private String city;
    private String postcode;
    private String address;
    private String iban;

    
    public Flat(String name, String country, String city, String postcode, String address, String iban) {
    	this.setName(name);
    	this.setCountry(country);
    	this.setCity(city);
    	this.setPostcode(postcode);
    	this.setAddress(address);
    	this.setIban(iban);
    }
    
    public int getID() {
        return id;
    }

    public void setID(int flatId) {
    	this.id = flatId;
    }
    
    public String getServerid() {
        return serverid;
    }

    public void setServerid(String serverid) {
        this.serverid = serverid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }
}