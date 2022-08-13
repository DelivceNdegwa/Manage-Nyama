package com.delivce.managenyama.models;

public class Supplier {
    long id, phone;
    String name, location;

    public Supplier() {

    }

    public Supplier(long phone, String name, String location) {
        this.phone = phone;
        this.name = name;
        this.location = location;
    }

    public Supplier(long id, long phone, String name, String location) {
        this.id = id;
        this.phone = phone;
        this.name = name;
        this.location = location;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
