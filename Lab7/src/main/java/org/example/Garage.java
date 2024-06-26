package org.example;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "garages")
public class Garage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String address;
    private String phoneNumber;
    private String ownerPhoneNumber;

    @ManyToMany(mappedBy = "garages", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Person> owners = new HashSet<>();

    @ManyToMany(mappedBy = "garages", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Car> cars = new HashSet<>();

    public Garage() {
    }

    public Garage(String address, String phoneNumber, String ownerPhoneNumber) {
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.ownerPhoneNumber = ownerPhoneNumber;
    }

    public int getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getOwnerPhoneNumber() {
        return ownerPhoneNumber;
    }

    public void setOwnerPhoneNumber(String ownerPhoneNumber) {
        this.ownerPhoneNumber = ownerPhoneNumber;
    }

    public Set<Person> getOwners() {
        return owners;
    }

    public Set<Car> getCars() {
        return cars;
    }
}
