package org.example;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "persons")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String firstName;
    private String lastName;
    private String password;
    private String email;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Car> cars = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "person_garage",
            joinColumns = @JoinColumn(name = "person_id"),
            inverseJoinColumns = @JoinColumn(name = "garage_id")
    )
    private Set<Garage> garages = new HashSet<>();

    public Person() {
    }

    public Person(String firstName, String lastName, String password, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Car> getCars() {
        return cars;
    }

    public void addCar(Car car) {
        cars.add(car);
        car.setOwner(this);
    }

    public Set<Garage> getGarages() {
        return garages;
    }

    public void addGarage(Garage garage) {
        garages.add(garage);
        garage.getOwners().add(this);
    }
}
