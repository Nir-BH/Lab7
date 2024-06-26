package org.example;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "cars")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String licensePlate;
    private double price;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Person owner;

    @Column(name = "manufacturing_year")
    private int year;

    @OneToOne(mappedBy = "car", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Image image;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "car_garage",
            joinColumns = @JoinColumn(name = "car_id"),
            inverseJoinColumns = @JoinColumn(name = "garage_id")
    )
    private Set<Garage> garages = new HashSet<>();

    public Car() {
    }

    public Car(String licensePlate, double price, int year) {
        this.licensePlate = licensePlate;
        this.price = price;
        this.year = year;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getId() {
        return id;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
        image.setCar(this);
    }

    public Set<Garage> getGarages() {
        return garages;
    }

    public void addGarage(Garage garage) {
        garages.add(garage);
        garage.getCars().add(this);
    }
}
