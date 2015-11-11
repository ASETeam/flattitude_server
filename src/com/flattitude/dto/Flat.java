package com.flattitude.dto;

public class Flat {
    private final int id = 0;
    private String serverid;
    private String name;
    private String country;
    private String city;
    private String postcode;
    private String address;

    
    public Flat(String name, String country, String city, String postcode, String address) {
    	this.setName(name);
    	this.setCountry(country);
    	this.setCity(city);
    	this.setPostcode(postcode);
    	this.setAddress(address);
    }
    
    public int getId() {
        return id;
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
}