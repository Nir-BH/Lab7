package org.example;

import javax.persistence.*;

@Entity
@Table(name = "images")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String url;

    @OneToOne
    @JoinColumn(name = "car_id")
    private Car car;

    public Image() {
    }

    public Image(String url) {
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }
}
